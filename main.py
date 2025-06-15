from flask import Flask, render_template, jsonify, request, redirect
import requests
from bs4 import BeautifulSoup
import re
from urllib.parse import urljoin, urlparse

app = Flask(__name__)

def is_mobile_device(user_agent):
    mobile_keywords = ['mobile', 'android', 'iphone', 'ipad', 'ipod', 'blackberry', 'windows phone']
    user_agent = user_agent.lower()
    return any(keyword in user_agent for keyword in mobile_keywords)

def scrape_manga_list(query=None):
    if query:
        return search_manga(query)
    
    url = "https://azoramoon.com"
    headers = {
        'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36',
        'Accept-Language': 'ar,en;q=0.9,en-US;q=0.8'
    }
    response = requests.get(url, headers=headers)
    response.encoding = 'utf-8'
    soup = BeautifulSoup(response.text, 'html.parser')
    manga_list = []
    manga_items = soup.select("div.page-item-detail")
    
    for item in manga_items:
        title_elem = item.select_one("div.post-title h3 a")
        if not title_elem:
            continue
            
        title = title_elem.text.strip()
        full_url = title_elem['href']
        # Extract the manga slug from the full URL
        manga_slug = full_url.split('manga/')[-1].strip('/')
        # Create a clean URL without any domain information
        clean_url = manga_slug
        
        # Try to get the highest quality thumbnail
        thumbnail = item.select_one("img.img-responsive")
        if thumbnail:
            # Try to get data-src first as it often contains the full-quality image
            thumbnail_url = thumbnail.get('data-src') or thumbnail.get('data-lazy-src') or thumbnail.get('src', '')
            # Remove any -110x150 or similar suffix from the URL to get full size
            thumbnail_url = re.sub(r'-\d+x\d+(?=\.[a-zA-Z]+$)', '', thumbnail_url)
        else:
            thumbnail_url = ""
        
        manga_list.append({
            'title': title,
            'url': clean_url,
            'thumbnail': thumbnail_url
        })
    
    return manga_list

def search_manga(query, page=1):
    try:
        headers = {
            'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36',
            'Accept-Language': 'ar,en;q=0.9,en-US;q=0.8'
        }
        
        # Use API search
        data = {
            'action': 'wp-manga-search-manga',
            'title': query
        }
        response = requests.post(
            'https://azoramoon.com/wp-admin/admin-ajax.php',
            data=data,
            headers=headers
        )
        response.encoding = 'utf-8'
        
        try:
            results = response.json()
            if results.get('success'):
                manga_list = []
                for item in results.get('data', []):
                    # For API results, try to get the thumbnail from manga details
                    full_url = item.get('url', '')
                    # Extract the manga slug from the full URL
                    manga_slug = full_url.split('manga/')[-1].strip('/')
                    # Create a clean URL without any domain information
                    clean_url = manga_slug
                    
                    try:
                        details = scrape_manga_details(full_url)
                        thumbnail = details.get('thumbnail', '')
                        # Remove any -110x150 or similar suffix from the URL to get full size
                        thumbnail = re.sub(r'-\d+x\d+(?=\.[a-zA-Z]+$)', '', thumbnail)
                    except:
                        thumbnail = ''
                    
                    manga_list.append({
                        'title': item.get('title', ''),
                        'url': clean_url,
                        'thumbnail': thumbnail
                    })
                return manga_list
        except ValueError:
            pass
            
        return []
    except Exception as e:
        print(f"Error searching manga: {str(e)}")
        return []

def scrape_manga_details(url):
    headers = {
        'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36',
        'Accept-Language': 'ar,en;q=0.9,en-US;q=0.8'
    }
    response = requests.get(url, headers=headers)
    response.encoding = 'utf-8'
    soup = BeautifulSoup(response.text, 'html.parser')
    
    # Basic details
    title_element = soup.select_one("div.post-title h1")
    arabic_title = title_element.text.strip() if title_element else ''
    
    # Try to find English title in alternative titles section
    alternative_titles_div = soup.select_one("div.post-content_item:-soup-contains('عناوين أخرى')")
    english_title = None
    if alternative_titles_div:
        alternative_titles = alternative_titles_div.select_one("div.summary-content").text.strip()
        # Look for English title in alternative titles (usually in parentheses)
        english_match = re.search(r'\((.*?)\)', alternative_titles)
        if english_match:
            english_title = english_match.group(1).strip()
    
    thumbnail = soup.select_one("div.summary_image img")
    thumbnail_url = thumbnail['src'] if thumbnail else ''
    
    description_div = soup.select_one("div.description-summary div.summary__content")
    description = description_div.text.strip() if description_div else ''
    
    # Metadata
    genres = [a.text.strip() for a in soup.select("div.genres-content a")]
    
    # Chapters
    chapters = []
    chapter_items = soup.select("li.wp-manga-chapter")
    for chapter in chapter_items:
        link = chapter.select_one("a")
        if link and link.get('href'):
            # Extract the chapter path from the full URL
            chapter_path = link['href'].split('lekmanga.net/')[-1].strip('/')
            # Create a clean chapter URL
            clean_chapter_url = chapter_path
            chapters.append({
                'title': link.text.strip(),
                'url': clean_chapter_url
            })
    
    return {
        'title': arabic_title,
        'english_title': english_title,
        'thumbnail': thumbnail_url,
        'description': description,
        'genres': genres,
        'chapters': chapters
    }

def scrape_chapter_images(url):
    try:
        # Clean and validate URL
        parsed_url = urlparse(url)
        if not parsed_url.netloc:
            # If no host, assume it's lekmanga.net
            url = f"https://azoramoon.com{'/'+url.lstrip('/') if not url.startswith('/') else url}"
        
        headers = {
            'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36',
            'Accept-Language': 'ar,en;q=0.9,en-US;q=0.8'
        }
        response = requests.get(url, headers=headers)
        response.encoding = 'utf-8'
        if not response.ok:
            print(f"Failed to fetch chapter: {response.status_code}")
            return []
            
        soup = BeautifulSoup(response.text, 'html.parser')
        images = []
        
        # Try multiple possible selectors for images
        image_selectors = [
            "div.reading-content img",
            "div.page-break img",
            ".wp-manga-chapter-img",
            "div[class*='chapter'] img"
        ]
        
        for selector in image_selectors:
            for img in soup.select(selector):
                src = img.get('src') or img.get('data-src')
                if src:
                    # Make sure URL is absolute
                    src = urljoin(url, src)
                    if src and src not in images:
                        images.append(src)
        
        # Remove duplicates while preserving order
        return list(dict.fromkeys(images))
    except Exception as e:
        print(f"Error scraping chapter images: {str(e)}")
        return []

@app.route('/')
def home():
    query = request.args.get('q', '')
    mangas = scrape_manga_list(query if query else None)
    template = 'mobile/index.html' if is_mobile_device(request.user_agent.string) else 'index.html'
    return render_template(template, 
                         mangas=mangas,
                         query=query)

@app.route('/search')
def search():
    if is_mobile_device(request.user_agent.string):
        query = request.args.get('q', '')
        recent_searches = []  # We're using localStorage for recent searches, so this can stay empty
        return render_template('mobile/search.html',
                             query=query,
                             recent_searches=recent_searches)
    return redirect('/')

@app.route('/manga/<path:manga_url>')
def manga_detail(manga_url):
    # Clean up the manga URL
    if manga_url.startswith('https://'):
        # Extract the manga slug from the full URL
        manga_slug = manga_url.split('manga/')[-1].strip('/')
    else:
        # Remove any 'manga/' prefix if it exists
        manga_slug = manga_url.replace('manga/', '').strip('/')
    
    # Construct the full URL for scraping
    base_url = "https://azoramoon.com"
    fixed_url = f"{base_url}/eries/{manga_slug}/"
    
    details = scrape_manga_details(fixed_url)
    
    # Pass the clean URL to the template
    clean_manga_url = f"manga/{manga_slug}"
    template = 'mobile/manga.html' if is_mobile_device(request.user_agent.string) else 'manga.html'
    return render_template(template, manga=details, manga_url=clean_manga_url)

@app.route('/read/<path:chapter_url>')
def read_chapter(chapter_url):
    try:
        # Validate the chapter URL
        if not chapter_url or not chapter_url.startswith('manga/'):
            return "Invalid chapter URL", 400
            
        # Construct the full URL for scraping
        full_chapter_url = f"https://azoramoon.com/{chapter_url}"
        
        # Extract manga slug from chapter path
        manga_slug = chapter_url.split('/')[1]  # Get the manga slug from the path
        
        # Get manga details to find the current chapter index
        manga_url = f"{manga_slug}"
        manga_details = scrape_manga_details(f"https://azoramoon.com/series/{manga_slug}/")
        
        # Find current chapter index
        current_chapter_index = None
        for i, chapter in enumerate(manga_details['chapters']):
            if chapter['url'] == chapter_url:
                current_chapter_index = i
                break
        
        if current_chapter_index is None:
            return "Chapter not found", 404
            
        # Get previous and next chapters
        prev_chapter = manga_details['chapters'][current_chapter_index + 1] if current_chapter_index + 1 < len(manga_details['chapters']) else None
        next_chapter = manga_details['chapters'][current_chapter_index - 1] if current_chapter_index > 0 else None
        
        # Scrape chapter images
        images = scrape_chapter_images(full_chapter_url)
        
        if not images:
            return "No images found for this chapter", 404

        template = 'mobile/reader.html' if is_mobile_device(request.user_agent.string) else 'reader.html'
        return render_template(template, 
                             images=images,
                             manga_url=manga_url,
                             prev_chapter=prev_chapter,
                             next_chapter=next_chapter)
    except Exception as e:
        print(f"Error reading chapter: {str(e)}")
        return "Error reading chapter", 500

@app.route('/favorites')
def favorites():
    template = 'mobile/favorites.html' if is_mobile_device(request.user_agent.string) else 'favorites.html'
    return render_template(template)

@app.route('/ads.txt')
def ads_txt():
    return 'google.com, pub-5348286475991825, DIRECT, f08c47fec0942fa0', 200, {'Content-Type': 'text/plain'}

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000, debug=True)

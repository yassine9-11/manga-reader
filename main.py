from flask import Flask, render_template, jsonify, request, redirect, make_response
import requests
from bs4 import BeautifulSoup
import re
from urllib.parse import urljoin, urlparse
import json
import os
import uuid
from datetime import datetime, timezone
import platform

app = Flask(__name__)

# User data management
USER_DATA_DIR = 'user_data'
if not os.path.exists(USER_DATA_DIR):
    os.makedirs(USER_DATA_DIR)

def get_current_time():
    return datetime.now(timezone.utc).strftime('%Y-%m-%d/%H:%M')

def get_user_data_file(device_id):
    return os.path.join(USER_DATA_DIR, f'{device_id}.json')

def get_device_type(user_agent):
    if is_mobile_device(user_agent):
        return 'android'
    else:
        # Check for common desktop OS identifiers
        user_agent = user_agent.lower()
        if 'windows' in user_agent:
            return 'windows'
        elif 'macintosh' in user_agent or 'mac os' in user_agent:
            return 'mac'
        elif 'linux' in user_agent:
            return 'linux'
        else:
            return 'desktop'

def load_user_data(device_id):
    file_path = get_user_data_file(device_id)
    current_time = get_current_time()
    if os.path.exists(file_path):
        with open(file_path, 'r', encoding='utf-8') as f:
            return json.load(f)
    return {
        'device_id': device_id,
        'device_type': get_device_type(request.user_agent.string),
        'device_name': platform.node() if not is_mobile_device(request.user_agent.string) else 'android device',
        'created_at': current_time,
        'last_active': current_time,
        'visit_count': 0,
        'chapters_read': 0,
        'favorite_mangas': [],
        'reading_history': []
    }

def save_user_data(data):
    file_path = get_user_data_file(data['device_id'])
    data['last_active'] = get_current_time()
    with open(file_path, 'w', encoding='utf-8') as f:
        json.dump(data, f, ensure_ascii=False, indent=4)

def get_or_create_device_id():
    device_id = request.cookies.get('device_id')
    if not device_id:
        device_id = str(uuid.uuid4())
    return device_id

def is_mobile_device(user_agent):
    mobile_keywords = ['mobile', 'android', 'iphone', 'ipad', 'ipod', 'blackberry', 'windows phone']
    user_agent = user_agent.lower()
    return any(keyword in user_agent for keyword in mobile_keywords)

def scrape_manga_list(query=None):
    if query:
        return search_manga(query)
    
    url = "https://lekmanga.net"
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
            'https://lekmanga.net/wp-admin/admin-ajax.php',
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
            url = f"https://lekmanga.net{'/'+url.lstrip('/') if not url.startswith('/') else url}"
        
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
    
    # Handle user data
    device_id = get_or_create_device_id()
    user_data = load_user_data(device_id)
    current_time = get_current_time()
    
    # Update visit information
    user_data['last_active'] = current_time
    user_data['visit_count'] += 1
    save_user_data(user_data)
    
    response = make_response(render_template(template, 
                                          mangas=mangas,
                                          query=query,
                                          user_data=user_data))
    response.set_cookie('device_id', device_id, max_age=31536000)  # 1 year expiry
    return response

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
    base_url = "https://lekmanga.net"
    fixed_url = f"{base_url}/manga/{manga_slug}/"
    
    details = scrape_manga_details(fixed_url)
    
    # Handle user data
    device_id = get_or_create_device_id()
    user_data = load_user_data(device_id)
    
    # Check if manga is in favorites
    details['is_favorite'] = manga_slug in [m['slug'] for m in user_data['favorite_mangas']]
    
    clean_manga_url = f"manga/{manga_slug}"
    template = 'mobile/manga.html' if is_mobile_device(request.user_agent.string) else 'manga.html'
    return render_template(template, manga=details, manga_url=clean_manga_url, user_data=user_data)

@app.route('/read/<path:chapter_url>')
def read_chapter(chapter_url):
    try:
        # Validate the chapter URL
        if not chapter_url or not chapter_url.startswith('manga/'):
            return "Invalid chapter URL", 400
            
        # Construct the full URL for scraping
        full_chapter_url = f"https://lekmanga.net/{chapter_url}"
        
        # Extract manga slug from chapter path
        manga_slug = chapter_url.split('/')[1]  # Get the manga slug from the path
        
        # Get manga details to find the current chapter index
        manga_url = f"{manga_slug}"
        manga_details = scrape_manga_details(f"https://lekmanga.net/manga/{manga_slug}/")
        
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

        # Handle user data
        device_id = get_or_create_device_id()
        user_data = load_user_data(device_id)
        
        # Update chapters read count
        user_data['chapters_read'] += 1
        
        # Create new history entry
        history_entry = {
            'manga_slug': manga_slug,
            'chapter_url': chapter_url,
            'timestamp': get_current_time()
        }
        
        # Remove old entry for this manga if it exists and add new one
        user_data['reading_history'] = [entry for entry in user_data['reading_history'] 
                                      if entry['manga_slug'] != manga_slug]
        user_data['reading_history'].append(history_entry)
        
        # Save updated user data
        save_user_data(user_data)
        
        template = 'mobile/reader.html' if is_mobile_device(request.user_agent.string) else 'reader.html'
        return render_template(template, 
                             images=images,
                             manga_url=manga_url,
                             prev_chapter=prev_chapter,
                             next_chapter=next_chapter,
                             user_data=user_data)
    except Exception as e:
        print(f"Error reading chapter: {str(e)}")
        return "Error reading chapter", 500

@app.route('/favorites')
def favorites():
    device_id = get_or_create_device_id()
    user_data = load_user_data(device_id)
    template = 'mobile/favorites.html' if is_mobile_device(request.user_agent.string) else 'favorites.html'
    return render_template(template, user_data=user_data)

@app.route('/api/toggle_favorite', methods=['POST'])
def toggle_favorite():
    try:
        device_id = get_or_create_device_id()
        user_data = load_user_data(device_id)
        
        data = request.get_json()
        manga_slug = data.get('manga_slug')
        manga_title = data.get('manga_title')
        manga_thumbnail = data.get('manga_thumbnail')
        
        if not manga_slug:
            return jsonify({'error': 'Missing manga_slug'}), 400
            
        # Find if manga is already in favorites
        existing_favorites = [m for m in user_data['favorite_mangas'] if m['slug'] == manga_slug]
        
        if existing_favorites:
            # Remove from favorites
            user_data['favorite_mangas'] = [m for m in user_data['favorite_mangas'] if m['slug'] != manga_slug]
            is_favorite = False
        else:
            # Add to favorites
            user_data['favorite_mangas'].append({
                'slug': manga_slug,
                'title': manga_title,
                'thumbnail': manga_thumbnail,
                'added_at': get_current_time()
            })
            is_favorite = True
            
        save_user_data(user_data)
        return jsonify({'success': True, 'is_favorite': is_favorite})
    except Exception as e:
        return jsonify({'error': str(e)}), 500

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000, debug=True)

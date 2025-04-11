package eu.kanade.tachiyomi.multisrc.madara;

import android.net.Uri;
import android.util.Base64;
import eu.kanade.tachiyomi.lib.cryptoaes.CryptoAES;
import eu.kanade.tachiyomi.lib.i18n.Intl;
import eu.kanade.tachiyomi.network.OkHttpExtensionsKt;
import eu.kanade.tachiyomi.network.RequestsKt;
import eu.kanade.tachiyomi.source.model.Filter;
import eu.kanade.tachiyomi.source.model.FilterList;
import eu.kanade.tachiyomi.source.model.MangasPage;
import eu.kanade.tachiyomi.source.model.Page;
import eu.kanade.tachiyomi.source.model.SChapter;
import eu.kanade.tachiyomi.source.model.SManga;
import eu.kanade.tachiyomi.source.online.ParsedHttpSource;
import eu.kanade.tachiyomi.util.JsoupExtensionsKt;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.Metadata;
import kotlin.NoWhenBranchMatchedException;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.Unit;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.collections.MapsKt;
import kotlin.collections.SetsKt;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.CharsKt;
import kotlin.text.Charsets;
import kotlin.text.MatchResult;
import kotlin.text.Regex;
import kotlin.text.RegexOption;
import kotlin.text.StringsKt;
import kotlinx.coroutines.BuildersKt;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.CoroutineScopeKt;
import kotlinx.coroutines.CoroutineStart;
import kotlinx.coroutines.Dispatchers;
import kotlinx.coroutines.Job;
import kotlinx.serialization.json.Json;
import kotlinx.serialization.json.JsonElement;
import kotlinx.serialization.json.JsonElementKt;
import kotlinx.serialization.json.JsonObject;
import kotlinx.serialization.json.JsonPrimitive;
import okhttp3.CacheControl;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import rx.Observable;

@Metadata(d1 = {"\u0000ú\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010$\n\u0002\b\t\n\u0002\u0010\u0011\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b \n\u0002\u0010\u0012\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\t\n\u0002\b(\b&\u0018\u0000 Ñ\u00012\u00020\u0001:\u001eÎ\u0001Ï\u0001Ð\u0001Ñ\u0001Ò\u0001Ó\u0001Ô\u0001Õ\u0001Ö\u0001×\u0001Ø\u0001Ù\u0001Ú\u0001Û\u0001Ü\u0001B'\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007¢\u0006\u0002\u0010\bJ\t\u0010\u0001\u001a\u00020\u0003H\u0014J\u0014\u0010\u0001\u001a\u00030\u00012\b\u0010\u0001\u001a\u00030\u0001H\u0014J\u001a\u0010\u0001\u001a\t\u0012\u0005\u0012\u00030\u0001002\b\u0010\u0001\u001a\u00030\u0001H\u0014J\t\u0010\u0001\u001a\u00020\u0003H\u0014J\u0014\u0010\u0001\u001a\u00030\u00012\b\u0010\u0001\u001a\u00030\u0001H\u0004J\u0016\u0010\u0001\u001a\u0005\u0018\u00010\u00012\b\u0010\u0001\u001a\u00030\u0001H\u0014J\u0014\u0010\u0001\u001a\u00030\u00012\b\u0010\u0001\u001a\u00030\u0001H\u0004J\t\u0010$\u001a\u00030\u0001H\u0004J-\u0010\u0001\u001a\n\u0012\u0005\u0012\u00030\u00010\u00012\u0007\u0010\u0001\u001a\u00020)2\u0007\u0010\u0001\u001a\u00020\u00032\b\u0010\u0001\u001a\u00030\u0001H\u0016J\n\u0010\u0001\u001a\u00030\u0001H\u0014J\n\u0010\u0001\u001a\u00030\u0001H\u0016J\n\u0010\u0001\u001a\u00030\u0001H\u0014J\u0015\u0010\u0001\u001a\u0004\u0018\u00010\u00032\b\u0010\u0001\u001a\u00030\u0001H\u0014J\u0014\u0010\u0001\u001a\u00030\u00012\b\u0010\u0001\u001a\u00030 \u0001H\u0014J\u0013\u0010¡\u0001\u001a\u00020\u00032\b\u0010\u0001\u001a\u00030\u0001H\u0014J\u0014\u0010¢\u0001\u001a\u00030£\u00012\b\u0010\u0001\u001a\u00030\u0001H\u0014J\u000b\u0010¤\u0001\u001a\u0004\u0018\u00010\u0003H\u0014J\u0014\u0010¥\u0001\u001a\u00030\u00012\b\u0010\u0001\u001a\u00030\u0001H\u0014J\u0013\u0010¦\u0001\u001a\u00030\u00012\u0007\u0010\u0001\u001a\u00020)H\u0014J\t\u0010§\u0001\u001a\u00020\u0003H\u0014J\u001b\u0010¨\u0001\u001a\u00030©\u00012\u000f\u0010ª\u0001\u001a\n\u0012\u0005\u0012\u00030\u00010«\u0001H\u0004J\u001c\u0010¬\u0001\u001a\u00030\u00012\u0007\u0010\u0001\u001a\u00020)2\u0007\u0010­\u0001\u001a\u00020%H\u0004J\u0014\u0010®\u0001\u001a\u00030£\u00012\b\u0010\u0001\u001a\u00030\u0001H\u0014J\u0013\u0010¯\u0001\u001a\u00030\u00012\u0007\u0010°\u0001\u001a\u00020\u0003H\u0014J\u001a\u0010±\u0001\u001a\t\u0012\u0005\u0012\u00030 \u0001002\b\u0010\u0001\u001a\u00030\u0001H\u0014J\u0014\u0010²\u0001\u001a\u00030\u00012\b\u0010³\u0001\u001a\u00030\u0001H\u0014J\u0015\u0010´\u0001\u001a\u00030µ\u00012\t\u0010¶\u0001\u001a\u0004\u0018\u00010\u0003H\u0016J\u0019\u0010·\u0001\u001a\b\u0012\u0004\u0012\u000201002\b\u0010\u0001\u001a\u00030\u0001H\u0014J\u0013\u0010¸\u0001\u001a\u00030µ\u00012\u0007\u0010¶\u0001\u001a\u00020\u0003H\u0014J\u0014\u0010¹\u0001\u001a\u00030£\u00012\b\u0010\u0001\u001a\u00030\u0001H\u0014J\u000b\u0010º\u0001\u001a\u0004\u0018\u00010\u0003H\u0014J\u0014\u0010»\u0001\u001a\u00030\u00012\b\u0010\u0001\u001a\u00030\u0001H\u0014J\u0013\u0010¼\u0001\u001a\u00030\u00012\u0007\u0010\u0001\u001a\u00020)H\u0014J\t\u0010½\u0001\u001a\u00020\u0003H\u0014J&\u0010¾\u0001\u001a\u00030\u00012\u0007\u0010\u0001\u001a\u00020)2\u0007\u0010\u0001\u001a\u00020\u00032\b\u0010\u0001\u001a\u00030\u0001H\u0014J\u0014\u0010¿\u0001\u001a\u00030£\u00012\b\u0010\u0001\u001a\u00030\u0001H\u0014J\u000b\u0010À\u0001\u001a\u0004\u0018\u00010\u0003H\u0014J\u0014\u0010Á\u0001\u001a\u00030\u00012\b\u0010\u0001\u001a\u00030\u0001H\u0014J&\u0010Â\u0001\u001a\u00030\u00012\u0007\u0010\u0001\u001a\u00020)2\u0007\u0010\u0001\u001a\u00020\u00032\b\u0010\u0001\u001a\u00030\u0001H\u0014J\t\u0010Ã\u0001\u001a\u00020\u0003H\u0014J\u0012\u0010Ä\u0001\u001a\u00020\u00032\u0007\u0010\u0001\u001a\u00020)H\u0014J&\u0010Å\u0001\u001a\u00030\u00012\u0007\u0010\u0001\u001a\u00020)2\u0007\u0010\u0001\u001a\u00020\u00032\b\u0010\u0001\u001a\u00030\u0001H\u0014J\b\u0010x\u001a\u00020%H\u0004J\u0013\u0010Æ\u0001\u001a\u00030\u00012\u0007\u0010Ç\u0001\u001a\u00020\u0003H\u0014J\"\u0010È\u0001\u001a\u00020%*\u00020\u00032\r\u0010É\u0001\u001a\b\u0012\u0004\u0012\u00020\u00030\u0014H\u0002¢\u0006\u0003\u0010Ê\u0001J\r\u0010Ë\u0001\u001a\u00020e*\u00020\u0003H\u0004J\u000f\u0010Ì\u0001\u001a\u0004\u0018\u00010\u0003*\u00020\u0003H\u0004J\u000b\u0010Í\u0001\u001a\u00020%*\u00020\u0003R \u0010\t\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00030\nX\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0014\u0010\r\u001a\u00020\u0003X\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0014\u0010\u0010\u001a\u00020\u0003XD¢\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u000fR\u0014\u0010\u0004\u001a\u00020\u0003X\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u000fR\u001c\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00030\u0014X\u0004¢\u0006\n\n\u0002\u0010\u0017\u001a\u0004\b\u0015\u0010\u0016R\u0014\u0010\u0018\u001a\u00020\u0003XD¢\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u000fR\u0014\u0010\u001a\u001a\u00020\u0003XD¢\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u000fR\u0014\u0010\u001c\u001a\u00020\u0003XD¢\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u000fR\u0014\u0010\u001e\u001a\u00020\u001fX\u0004¢\u0006\b\n\u0000\u001a\u0004\b \u0010!R\u001c\u0010\"\u001a\b\u0012\u0004\u0012\u00020\u00030\u0014X\u0004¢\u0006\n\n\u0002\u0010\u0017\u001a\u0004\b#\u0010\u0016R\u000e\u0010\u0006\u001a\u00020\u0007X\u0004¢\u0006\u0002\n\u0000R\u0014\u0010$\u001a\u00020%XD¢\u0006\b\n\u0000\u001a\u0004\b&\u0010'R\u000e\u0010(\u001a\u00020)X\u000e¢\u0006\u0002\n\u0000R\u0014\u0010*\u001a\u00020%XD¢\u0006\b\n\u0000\u001a\u0004\b+\u0010'R \u0010,\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00030\nX\u0004¢\u0006\b\n\u0000\u001a\u0004\b-\u0010\fR\u000e\u0010.\u001a\u00020%X\u000e¢\u0006\u0002\n\u0000R \u0010/\u001a\b\u0012\u0004\u0012\u00020100X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b2\u00103\"\u0004\b4\u00105R\u001c\u00106\u001a\b\u0012\u0004\u0012\u00020\u00030\u0014X\u0004¢\u0006\n\n\u0002\u0010\u0017\u001a\u0004\b7\u0010\u0016R\u0014\u00108\u001a\u000209X\u0004¢\u0006\b\n\u0000\u001a\u0004\b:\u0010;R\u001b\u0010<\u001a\u00020=8TX\u0002¢\u0006\f\n\u0004\b@\u0010A\u001a\u0004\b>\u0010?R\u0011\u0010\u0005\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\bB\u0010\u000fR\u000e\u0010C\u001a\u00020DX\u000e¢\u0006\u0002\n\u0000R\u0014\u0010E\u001a\u00020\u0003XD¢\u0006\b\n\u0000\u001a\u0004\bF\u0010\u000fR\u0014\u0010G\u001a\u00020\u0003XD¢\u0006\b\n\u0000\u001a\u0004\bH\u0010\u000fR\u0014\u0010I\u001a\u00020\u0003XD¢\u0006\b\n\u0000\u001a\u0004\bJ\u0010\u000fR\u0014\u0010K\u001a\u00020\u0003XD¢\u0006\b\n\u0000\u001a\u0004\bL\u0010\u000fR\u0014\u0010M\u001a\u00020\u0003XD¢\u0006\b\n\u0000\u001a\u0004\bN\u0010\u000fR\u0014\u0010O\u001a\u00020\u0003XD¢\u0006\b\n\u0000\u001a\u0004\bP\u0010\u000fR\u0014\u0010Q\u001a\u00020\u0003XD¢\u0006\b\n\u0000\u001a\u0004\bR\u0010\u000fR\u0014\u0010S\u001a\u00020\u0003XD¢\u0006\b\n\u0000\u001a\u0004\bT\u0010\u000fR\u001b\u0010U\u001a\u00020\u00038TX\u0002¢\u0006\f\n\u0004\bW\u0010A\u001a\u0004\bV\u0010\u000fR\u0014\u0010X\u001a\u00020\u0003XD¢\u0006\b\n\u0000\u001a\u0004\bY\u0010\u000fR\u0014\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\b\n\u0000\u001a\u0004\bZ\u0010\u000fR\u000e\u0010[\u001a\u00020%X\u000e¢\u0006\u0002\n\u0000R\u001c\u0010\\\u001a\b\u0012\u0004\u0012\u00020\u00030\u0014X\u0004¢\u0006\n\n\u0002\u0010\u0017\u001a\u0004\b]\u0010\u0016R \u0010^\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00030\nX\u0004¢\u0006\b\n\u0000\u001a\u0004\b_\u0010\fR\u0014\u0010`\u001a\u00020\u0003XD¢\u0006\b\n\u0000\u001a\u0004\ba\u0010\u000fR\u0014\u0010b\u001a\u00020\u0003XD¢\u0006\b\n\u0000\u001a\u0004\bc\u0010\u000fR\u0014\u0010d\u001a\u00020eX\u0004¢\u0006\b\n\u0000\u001a\u0004\bf\u0010gR\u000e\u0010h\u001a\u00020iX\u0004¢\u0006\u0002\n\u0000R\u0014\u0010j\u001a\u00020\u0003XD¢\u0006\b\n\u0000\u001a\u0004\bk\u0010\u000fR\u0014\u0010l\u001a\u00020%XD¢\u0006\b\n\u0000\u001a\u0004\bm\u0010'R\u0014\u0010n\u001a\u00020\u0003XD¢\u0006\b\n\u0000\u001a\u0004\bo\u0010\u000fR \u0010p\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00030\nX\u0004¢\u0006\b\n\u0000\u001a\u0004\bq\u0010\fR\u0014\u0010r\u001a\u00020%XD¢\u0006\b\n\u0000\u001a\u0004\bs\u0010'R\u0014\u0010t\u001a\u00020uX\u0004¢\u0006\b\n\u0000\u001a\u0004\bv\u0010wR\u0014\u0010x\u001a\u00020yX\u0004¢\u0006\b\n\u0000\u001a\u0004\bz\u0010{R\u0014\u0010|\u001a\u00020%XD¢\u0006\b\n\u0000\u001a\u0004\b}\u0010'R\u001e\u0010~\u001a\u000208DX\u0002¢\u0006\u000f\n\u0005\b\u0001\u0010A\u001a\u0006\b\u0001\u0010\u0001¨\u0006Ý\u0001"}, d2 = {"Leu/kanade/tachiyomi/multisrc/madara/Madara;", "Leu/kanade/tachiyomi/source/online/ParsedHttpSource;", "name", "", "baseUrl", "lang", "dateFormat", "Ljava/text/SimpleDateFormat;", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/text/SimpleDateFormat;)V", "adultContentFilterOptions", "", "getAdultContentFilterOptions", "()Ljava/util/Map;", "altName", "getAltName", "()Ljava/lang/String;", "altNameSelector", "getAltNameSelector", "getBaseUrl", "canceledStatusList", "", "getCanceledStatusList", "()[Ljava/lang/String;", "[Ljava/lang/String;", "chapterProtectorSelector", "getChapterProtectorSelector", "chapterUrlSelector", "getChapterUrlSelector", "chapterUrlSuffix", "getChapterUrlSuffix", "client", "Lokhttp3/OkHttpClient;", "getClient", "()Lokhttp3/OkHttpClient;", "completedStatusList", "getCompletedStatusList", "fetchGenres", "", "getFetchGenres", "()Z", "fetchGenresAttempts", "", "filterNonMangaItems", "getFilterNonMangaItems", "genreConditionFilterOptions", "getGenreConditionFilterOptions", "genresFetched", "genresList", "", "Leu/kanade/tachiyomi/multisrc/madara/Madara$Genre;", "getGenresList", "()Ljava/util/List;", "setGenresList", "(Ljava/util/List;)V", "hiatusStatusList", "getHiatusStatusList", "intl", "Leu/kanade/tachiyomi/lib/i18n/Intl;", "getIntl", "()Leu/kanade/tachiyomi/lib/i18n/Intl;", "json", "Lkotlinx/serialization/json/Json;", "getJson", "()Lkotlinx/serialization/json/Json;", "json$delegate", "Lkotlin/Lazy;", "getLang", "loadMoreRequestDetected", "Leu/kanade/tachiyomi/multisrc/madara/Madara$LoadMoreDetection;", "mangaDetailsSelectorArtist", "getMangaDetailsSelectorArtist", "mangaDetailsSelectorAuthor", "getMangaDetailsSelectorAuthor", "mangaDetailsSelectorDescription", "getMangaDetailsSelectorDescription", "mangaDetailsSelectorGenre", "getMangaDetailsSelectorGenre", "mangaDetailsSelectorStatus", "getMangaDetailsSelectorStatus", "mangaDetailsSelectorTag", "getMangaDetailsSelectorTag", "mangaDetailsSelectorThumbnail", "getMangaDetailsSelectorThumbnail", "mangaDetailsSelectorTitle", "getMangaDetailsSelectorTitle", "mangaEntrySelector", "getMangaEntrySelector", "mangaEntrySelector$delegate", "mangaSubString", "getMangaSubString", "getName", "oldChapterEndpointDisabled", "ongoingStatusList", "getOngoingStatusList", "orderByFilterOptions", "getOrderByFilterOptions", "pageListParseSelector", "getPageListParseSelector", "popularMangaUrlSelector", "getPopularMangaUrlSelector", "salted", "", "getSalted", "()[B", "scope", "Lkotlinx/coroutines/CoroutineScope;", "searchMangaUrlSelector", "getSearchMangaUrlSelector", "sendViewCount", "getSendViewCount", "seriesTypeSelector", "getSeriesTypeSelector", "statusFilterOptions", "getStatusFilterOptions", "supportsLatest", "getSupportsLatest", "updatingRegex", "Lkotlin/text/Regex;", "getUpdatingRegex", "()Lkotlin/text/Regex;", "useLoadMoreRequest", "Leu/kanade/tachiyomi/multisrc/madara/Madara$LoadMoreStrategy;", "getUseLoadMoreRequest", "()Leu/kanade/tachiyomi/multisrc/madara/Madara$LoadMoreStrategy;", "useNewChapterEndpoint", "getUseNewChapterEndpoint", "xhrHeaders", "Lokhttp3/Headers;", "getXhrHeaders", "()Lokhttp3/Headers;", "xhrHeaders$delegate", "chapterDateSelector", "chapterFromElement", "Leu/kanade/tachiyomi/source/model/SChapter;", "element", "Lorg/jsoup/nodes/Element;", "chapterListParse", "response", "Lokhttp3/Response;", "chapterListSelector", "countViews", "", "document", "Lorg/jsoup/nodes/Document;", "countViewsRequest", "Lokhttp3/Request;", "detectLoadMore", "fetchSearchManga", "Lrx/Observable;", "Leu/kanade/tachiyomi/source/model/MangasPage;", "page", "query", "filters", "Leu/kanade/tachiyomi/source/model/FilterList;", "genresRequest", "getFilterList", "headersBuilder", "Lokhttp3/Headers$Builder;", "imageFromElement", "imageRequest", "Leu/kanade/tachiyomi/source/model/Page;", "imageUrlParse", "latestUpdatesFromElement", "Leu/kanade/tachiyomi/source/model/SManga;", "latestUpdatesNextPageSelector", "latestUpdatesParse", "latestUpdatesRequest", "latestUpdatesSelector", "launchIO", "Lkotlinx/coroutines/Job;", "block", "Lkotlin/Function0;", "loadMoreRequest", "popular", "mangaDetailsParse", "oldXhrChaptersRequest", "mangaId", "pageListParse", "pageListRequest", "chapter", "parseChapterDate", "", "date", "parseGenres", "parseRelativeDate", "popularMangaFromElement", "popularMangaNextPageSelector", "popularMangaParse", "popularMangaRequest", "popularMangaSelector", "searchLoadMoreRequest", "searchMangaFromElement", "searchMangaNextPageSelector", "searchMangaParse", "searchMangaRequest", "searchMangaSelector", "searchPage", "searchRequest", "xhrChaptersRequest", "mangaUrl", "containsIn", "array", "(Ljava/lang/String;[Ljava/lang/String;)Z", "decodeHex", "getSrcSetImage", "notUpdating", "AdultContentFilter", "ArtistFilter", "AuthorFilter", "Companion", "Genre", "GenreCheckBox", "GenreConditionFilter", "GenreList", "LoadMoreDetection", "LoadMoreStrategy", "OrderByFilter", "StatusFilter", "Tag", "UriPartFilter", "YearFilter", "madara_release"}, k = 1, mv = {1, 7, 1}, xi = 48)
/* compiled from: Madara.kt */
public abstract class Madara extends ParsedHttpSource {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    /* access modifiers changed from: private */
    public static final Regex URL_REGEX = new Regex("^(https?://[^\\s/$.?#].[^\\s]*)$");
    public static final String URL_SEARCH_PREFIX = "slug:";
    private final Map<String, String> adultContentFilterOptions;
    private final String altName;
    private final String altNameSelector;
    private final String baseUrl;
    private final String[] canceledStatusList;
    private final String chapterProtectorSelector;
    private final String chapterUrlSelector;
    private final String chapterUrlSuffix;
    private final OkHttpClient client;
    private final String[] completedStatusList;
    private final SimpleDateFormat dateFormat;
    private final boolean fetchGenres;
    private int fetchGenresAttempts;
    private final boolean filterNonMangaItems;
    private final Map<String, String> genreConditionFilterOptions;
    private boolean genresFetched;
    private List<Genre> genresList;
    private final String[] hiatusStatusList;
    private final Intl intl;
    private final Lazy json$delegate;
    private final String lang;
    private LoadMoreDetection loadMoreRequestDetected;
    private final String mangaDetailsSelectorArtist;
    private final String mangaDetailsSelectorAuthor;
    private final String mangaDetailsSelectorDescription;
    private final String mangaDetailsSelectorGenre;
    private final String mangaDetailsSelectorStatus;
    private final String mangaDetailsSelectorTag;
    private final String mangaDetailsSelectorThumbnail;
    private final String mangaDetailsSelectorTitle;
    private final Lazy mangaEntrySelector$delegate;
    private final String mangaSubString;
    private final String name;
    private boolean oldChapterEndpointDisabled;
    private final String[] ongoingStatusList;
    private final Map<String, String> orderByFilterOptions;
    private final String pageListParseSelector;
    private final String popularMangaUrlSelector;
    private final byte[] salted;
    private final CoroutineScope scope;
    private final String searchMangaUrlSelector;
    private final boolean sendViewCount;
    private final String seriesTypeSelector;
    private final Map<String, String> statusFilterOptions;
    private final boolean supportsLatest;
    private final Regex updatingRegex;
    private final LoadMoreStrategy useLoadMoreRequest;
    private final boolean useNewChapterEndpoint;
    private final Lazy xhrHeaders$delegate;

    @Metadata(d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0005\b\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005¨\u0006\u0006"}, d2 = {"Leu/kanade/tachiyomi/multisrc/madara/Madara$LoadMoreDetection;", "", "(Ljava/lang/String;I)V", "Pending", "True", "False", "madara_release"}, k = 1, mv = {1, 7, 1}, xi = 48)
    /* compiled from: Madara.kt */
    private enum LoadMoreDetection {
        Pending,
        True,
        False
    }

    @Metadata(d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0005\b\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005¨\u0006\u0006"}, d2 = {"Leu/kanade/tachiyomi/multisrc/madara/Madara$LoadMoreStrategy;", "", "(Ljava/lang/String;I)V", "AutoDetect", "Always", "Never", "madara_release"}, k = 1, mv = {1, 7, 1}, xi = 48)
    /* compiled from: Madara.kt */
    public enum LoadMoreStrategy {
        AutoDetect,
        Always,
        Never
    }

    @Metadata(k = 3, mv = {1, 7, 1}, xi = 48)
    /* compiled from: Madara.kt */
    public /* synthetic */ class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0;

        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0010 */
        static {
            /*
                eu.kanade.tachiyomi.multisrc.madara.Madara$LoadMoreStrategy[] r0 = eu.kanade.tachiyomi.multisrc.madara.Madara.LoadMoreStrategy.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                eu.kanade.tachiyomi.multisrc.madara.Madara$LoadMoreStrategy r1 = eu.kanade.tachiyomi.multisrc.madara.Madara.LoadMoreStrategy.Always     // Catch:{ NoSuchFieldError -> 0x0010 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0010 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0010 }
            L_0x0010:
                eu.kanade.tachiyomi.multisrc.madara.Madara$LoadMoreStrategy r1 = eu.kanade.tachiyomi.multisrc.madara.Madara.LoadMoreStrategy.Never     // Catch:{ NoSuchFieldError -> 0x0019 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0019 }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0019 }
            L_0x0019:
                $EnumSwitchMapping$0 = r0
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: eu.kanade.tachiyomi.multisrc.madara.Madara.WhenMappings.<clinit>():void");
        }
    }

    public String getName() {
        return this.name;
    }

    public String getBaseUrl() {
        return this.baseUrl;
    }

    public final String getLang() {
        return this.lang;
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public /* synthetic */ Madara(String str, String str2, String str3, SimpleDateFormat simpleDateFormat, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this(str, str2, str3, (i & 8) != 0 ? new SimpleDateFormat("MMMM dd, yyyy", Locale.US) : simpleDateFormat);
    }

    public Madara(String str, String str2, String str3, SimpleDateFormat simpleDateFormat) {
        String str4 = str;
        String str5 = str2;
        String str6 = str3;
        SimpleDateFormat simpleDateFormat2 = simpleDateFormat;
        Intrinsics.checkNotNullParameter(str4, "name");
        Intrinsics.checkNotNullParameter(str5, "baseUrl");
        Intrinsics.checkNotNullParameter(str6, "lang");
        Intrinsics.checkNotNullParameter(simpleDateFormat2, "dateFormat");
        this.name = str4;
        this.baseUrl = str5;
        this.lang = str6;
        this.dateFormat = simpleDateFormat2;
        this.supportsLatest = true;
        this.client = getNetwork().getCloudflareClient();
        this.xhrHeaders$delegate = LazyKt.lazy(new Madara$xhrHeaders$2(this));
        this.json$delegate = LazyKt.lazy(Madara$special$$inlined$injectLazy$1.INSTANCE);
        Set of = SetsKt.setOf(new String[]{"en", "pt-BR", "es"});
        ClassLoader classLoader = getClass().getClassLoader();
        Intrinsics.checkNotNull(classLoader);
        Intl intl2 = new Intl(str3, of, "en", classLoader, (Function1) null, 16, (DefaultConstructorMarker) null);
        this.intl = intl2;
        this.filterNonMangaItems = true;
        this.mangaEntrySelector$delegate = LazyKt.lazy(new Madara$mangaEntrySelector$2(this));
        this.genresList = CollectionsKt.emptyList();
        this.fetchGenres = true;
        this.mangaSubString = "manga";
        this.useLoadMoreRequest = LoadMoreStrategy.AutoDetect;
        this.loadMoreRequestDetected = LoadMoreDetection.Pending;
        this.popularMangaUrlSelector = "div.post-title a";
        this.statusFilterOptions = MapsKt.mapOf(new Pair[]{TuplesKt.to(intl2.get("status_filter_completed"), "end"), TuplesKt.to(intl2.get("status_filter_ongoing"), "on-going"), TuplesKt.to(intl2.get("status_filter_canceled"), "canceled"), TuplesKt.to(intl2.get("status_filter_on_hold"), "on-hold")});
        this.orderByFilterOptions = MapsKt.mapOf(new Pair[]{TuplesKt.to(intl2.get("order_by_filter_relevance"), ""), TuplesKt.to(intl2.get("order_by_filter_latest"), "latest"), TuplesKt.to(intl2.get("order_by_filter_az"), "alphabet"), TuplesKt.to(intl2.get("order_by_filter_rating"), "rating"), TuplesKt.to(intl2.get("order_by_filter_trending"), "trending"), TuplesKt.to(intl2.get("order_by_filter_views"), "views"), TuplesKt.to(intl2.get("order_by_filter_new"), "new-manga")});
        this.genreConditionFilterOptions = MapsKt.mapOf(new Pair[]{TuplesKt.to(intl2.get("genre_condition_filter_or"), ""), TuplesKt.to(intl2.get("genre_condition_filter_and"), "1")});
        this.adultContentFilterOptions = MapsKt.mapOf(new Pair[]{TuplesKt.to(intl2.get("adult_content_filter_all"), ""), TuplesKt.to(intl2.get("adult_content_filter_none"), "0"), TuplesKt.to(intl2.get("adult_content_filter_only"), "1")});
        this.searchMangaUrlSelector = "div.post-title a";
        this.completedStatusList = new String[]{"Completed", "Completo", "Completado", "Concluído", "Concluido", "Finalizado", "Achevé", "Terminé", "Hoàn Thành", "مكتملة", "مكتمل", "已完结", "Tamamlandı", "Đã hoàn thành", "Завершено", "Tamamlanan", "Complété"};
        this.ongoingStatusList = new String[]{"OnGoing", "Продолжается", "Updating", "Em Lançamento", "Em lançamento", "Em andamento", "Em Andamento", "En cours", "En Cours", "En cours de publication", "Ativo", "Lançando", "Đang Tiến Hành", "Devam Ediyor", "Devam ediyor", "In Corso", "In Arrivo", "مستمرة", "مستمر", "En Curso", "En curso", "Emision", "Curso", "En marcha", "Publicandose", "Publicándose", "En emision", "连载中", "Em Lançamento", "Devam Ediyo", "Đang làm", "Em postagem", "Devam Eden", "Em progresso", "Em curso", "Atualizações Semanais"};
        this.hiatusStatusList = new String[]{"On Hold", "Pausado", "En espera", "Durduruldu", "Beklemede", "Đang chờ", "متوقف", "En Pause", "Заморожено", "En attente"};
        this.canceledStatusList = new String[]{"Canceled", "Cancelado", "İptal Edildi", "Güncel", "Đã hủy", "ملغي", "Abandonné", "Заброшено", "Annulé"};
        this.mangaDetailsSelectorTitle = "div.post-title h3, div.post-title h1, #manga-title > h1";
        this.mangaDetailsSelectorAuthor = "div.author-content > a, div.manga-authors > a";
        this.mangaDetailsSelectorArtist = "div.artist-content > a";
        this.mangaDetailsSelectorStatus = "div.summary-content, div.summary-heading:contains(Status) + div";
        this.mangaDetailsSelectorDescription = "div.description-summary div.summary__content, div.summary_content div.post-content_item > h5 + div, div.summary_content div.manga-excerpt";
        this.mangaDetailsSelectorThumbnail = "div.summary_image img";
        this.mangaDetailsSelectorGenre = "div.genres-content a";
        this.mangaDetailsSelectorTag = "div.tags-content a";
        this.seriesTypeSelector = ".post-content_item:contains(Type) .summary-content";
        this.altNameSelector = ".post-content_item:contains(Alt) .summary-content";
        this.altName = intl2.get("alt_names_heading");
        this.updatingRegex = new Regex("Updating|Atualizando", RegexOption.IGNORE_CASE);
        this.chapterUrlSelector = "a";
        this.chapterUrlSuffix = "?style=list";
        this.pageListParseSelector = "div.page-break, li.blocks-gallery-item, .reading-content .text-left:not(:has(.blocks-gallery-item)) img";
        this.chapterProtectorSelector = "#chapter-protector-data";
        this.sendViewCount = true;
        byte[] bytes = "Salted__".getBytes(Charsets.UTF_8);
        Intrinsics.checkNotNullExpressionValue(bytes, "this as java.lang.String).getBytes(charset)");
        this.salted = bytes;
        this.scope = CoroutineScopeKt.CoroutineScope(Dispatchers.getIO());
    }

    public boolean getSupportsLatest() {
        return this.supportsLatest;
    }

    public OkHttpClient getClient() {
        return this.client;
    }

    /* access modifiers changed from: protected */
    public Headers.Builder headersBuilder() {
        Headers.Builder headersBuilder = Madara.super.headersBuilder();
        return headersBuilder.add("Referer", getBaseUrl() + '/');
    }

    /* access modifiers changed from: protected */
    public final Headers getXhrHeaders() {
        return (Headers) this.xhrHeaders$delegate.getValue();
    }

    /* access modifiers changed from: protected */
    public Json getJson() {
        return (Json) this.json$delegate.getValue();
    }

    /* access modifiers changed from: protected */
    public final Intl getIntl() {
        return this.intl;
    }

    /* access modifiers changed from: protected */
    public boolean getFilterNonMangaItems() {
        return this.filterNonMangaItems;
    }

    /* access modifiers changed from: protected */
    public String getMangaEntrySelector() {
        return (String) this.mangaEntrySelector$delegate.getValue();
    }

    /* access modifiers changed from: protected */
    public List<Genre> getGenresList() {
        return this.genresList;
    }

    /* access modifiers changed from: protected */
    public void setGenresList(List<Genre> list) {
        Intrinsics.checkNotNullParameter(list, "<set-?>");
        this.genresList = list;
    }

    /* access modifiers changed from: protected */
    public boolean getFetchGenres() {
        return this.fetchGenres;
    }

    /* access modifiers changed from: protected */
    public String getMangaSubString() {
        return this.mangaSubString;
    }

    /* access modifiers changed from: protected */
    public LoadMoreStrategy getUseLoadMoreRequest() {
        return this.useLoadMoreRequest;
    }

    /* access modifiers changed from: protected */
    public final void detectLoadMore(Document document) {
        LoadMoreDetection loadMoreDetection;
        Intrinsics.checkNotNullParameter(document, "document");
        if (getUseLoadMoreRequest() == LoadMoreStrategy.AutoDetect && this.loadMoreRequestDetected == LoadMoreDetection.Pending) {
            boolean z = document.selectFirst("nav.navigation-ajax") != null;
            if (z) {
                loadMoreDetection = LoadMoreDetection.True;
            } else if (!z) {
                loadMoreDetection = LoadMoreDetection.False;
            } else {
                throw new NoWhenBranchMatchedException();
            }
            this.loadMoreRequestDetected = loadMoreDetection;
        }
    }

    /* access modifiers changed from: protected */
    public final boolean useLoadMoreRequest() {
        int i = WhenMappings.$EnumSwitchMapping$0[getUseLoadMoreRequest().ordinal()];
        if (i == 1) {
            return true;
        }
        if (i == 2 || this.loadMoreRequestDetected != LoadMoreDetection.True) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public MangasPage popularMangaParse(Response response) {
        Intrinsics.checkNotNullParameter(response, "response");
        Element element = null;
        boolean z = true;
        Document asJsoup$default = JsoupExtensionsKt.asJsoup$default(response, (String) null, 1, (Object) null);
        Iterable select = asJsoup$default.select(popularMangaSelector());
        Intrinsics.checkNotNullExpressionValue(select, "document.select(popularMangaSelector())");
        Iterable<Element> iterable = select;
        Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterable, 10));
        for (Element popularMangaFromElement : iterable) {
            arrayList.add(popularMangaFromElement(popularMangaFromElement));
        }
        List list = (List) arrayList;
        String popularMangaNextPageSelector = popularMangaNextPageSelector();
        if (popularMangaNextPageSelector != null) {
            element = asJsoup$default.selectFirst(popularMangaNextPageSelector);
        }
        if (element == null) {
            z = false;
        }
        detectLoadMore(asJsoup$default);
        return new MangasPage(list, z);
    }

    /* access modifiers changed from: protected */
    public String popularMangaSelector() {
        return "div.page-item-detail:not(:has(a[href*='bilibilicomics.com']))" + getMangaEntrySelector() + " , .manga__item";
    }

    public String getPopularMangaUrlSelector() {
        return this.popularMangaUrlSelector;
    }

    /* access modifiers changed from: protected */
    public SManga popularMangaFromElement(Element element) {
        Intrinsics.checkNotNullParameter(element, "element");
        SManga create = SManga.Companion.create();
        Element selectFirst = element.selectFirst(getPopularMangaUrlSelector());
        Intrinsics.checkNotNull(selectFirst);
        String attr = selectFirst.attr("abs:href");
        Intrinsics.checkNotNullExpressionValue(attr, "it.attr(\"abs:href\")");
        setUrlWithoutDomain(create, attr);
        String ownText = selectFirst.ownText();
        Intrinsics.checkNotNullExpressionValue(ownText, "it.ownText()");
        create.setTitle(ownText);
        Element selectFirst2 = element.selectFirst("img");
        if (selectFirst2 != null) {
            Intrinsics.checkNotNullExpressionValue(selectFirst2, "it");
            create.setThumbnail_url(imageFromElement(selectFirst2));
        }
        return create;
    }

    /* access modifiers changed from: protected */
    public Request popularMangaRequest(int i) {
        if (useLoadMoreRequest()) {
            return loadMoreRequest(i, true);
        }
        return RequestsKt.GET$default(getBaseUrl() + '/' + getMangaSubString() + '/' + searchPage(i) + "?m_orderby=views", getHeaders(), (CacheControl) null, 4, (Object) null);
    }

    /* access modifiers changed from: protected */
    public String popularMangaNextPageSelector() {
        if (useLoadMoreRequest()) {
            return "body:not(:has(.no-posts))";
        }
        return "div.nav-previous, nav.navigation-ajax, a.nextpostslink";
    }

    /* access modifiers changed from: protected */
    public String latestUpdatesSelector() {
        return popularMangaSelector();
    }

    /* access modifiers changed from: protected */
    public SManga latestUpdatesFromElement(Element element) {
        Intrinsics.checkNotNullParameter(element, "element");
        return popularMangaFromElement(element);
    }

    /* access modifiers changed from: protected */
    public Request latestUpdatesRequest(int i) {
        if (useLoadMoreRequest()) {
            return loadMoreRequest(i, false);
        }
        return RequestsKt.GET$default(getBaseUrl() + '/' + getMangaSubString() + '/' + searchPage(i) + "?m_orderby=latest", getHeaders(), (CacheControl) null, 4, (Object) null);
    }

    /* access modifiers changed from: protected */
    public String latestUpdatesNextPageSelector() {
        return popularMangaNextPageSelector();
    }

    /* access modifiers changed from: protected */
    public MangasPage latestUpdatesParse(Response response) {
        Intrinsics.checkNotNullParameter(response, "response");
        MangasPage popularMangaParse = popularMangaParse(response);
        HashSet hashSet = new HashSet();
        ArrayList arrayList = new ArrayList();
        for (Object next : popularMangaParse.getMangas()) {
            if (hashSet.add(((SManga) next).getUrl())) {
                arrayList.add(next);
            }
        }
        return new MangasPage(arrayList, popularMangaParse.getHasNextPage());
    }

    /* access modifiers changed from: protected */
    public final Request loadMoreRequest(int i, boolean z) {
        FormBody.Builder builder = new FormBody.Builder((Charset) null, 1, (DefaultConstructorMarker) null);
        builder.add("action", "madara_load_more");
        builder.add("page", String.valueOf(i - 1));
        builder.add("template", "madara-core/content/content-archive");
        builder.add("vars[orderby]", "meta_value_num");
        builder.add("vars[paged]", "1");
        builder.add("vars[post_type]", "wp-manga");
        builder.add("vars[post_status]", "publish");
        builder.add("vars[meta_key]", z ? "_wp_manga_views" : "_latest_update");
        builder.add("vars[order]", "desc");
        builder.add("vars[sidebar]", "right");
        builder.add("vars[manga_archives_item_layout]", "big_thumbnail");
        RequestBody build = builder.build();
        return RequestsKt.POST$default(getBaseUrl() + "/wp-admin/admin-ajax.php", getXhrHeaders(), build, (CacheControl) null, 8, (Object) null);
    }

    public Observable<MangasPage> fetchSearchManga(int i, String str, FilterList filterList) {
        Intrinsics.checkNotNullParameter(str, "query");
        Intrinsics.checkNotNullParameter(filterList, "filters");
        if (!StringsKt.startsWith$default(str, URL_SEARCH_PREFIX, false, 2, (Object) null)) {
            return Madara.super.fetchSearchManga(i, str, filterList);
        }
        HttpUrl.Builder newBuilder = HttpUrl.Companion.get(getBaseUrl()).newBuilder();
        newBuilder.addPathSegment(getMangaSubString());
        newBuilder.addPathSegment(StringsKt.substringAfter$default(str, URL_SEARCH_PREFIX, (String) null, 2, (Object) null));
        HttpUrl build = newBuilder.build();
        Observable<MangasPage> map = OkHttpExtensionsKt.asObservableSuccess(getClient().newCall(RequestsKt.GET$default(build, getHeaders(), (CacheControl) null, 4, (Object) null))).map(new Madara$$ExternalSyntheticLambda0(new Madara$fetchSearchManga$1(this, build)));
        Intrinsics.checkNotNullExpressionValue(map, "override fun fetchSearch…ge, query, filters)\n    }");
        return map;
    }

    /* access modifiers changed from: private */
    public static final MangasPage fetchSearchManga$lambda$7(Function1 function1, Object obj) {
        Intrinsics.checkNotNullParameter(function1, "$tmp0");
        return (MangasPage) function1.invoke(obj);
    }

    /* access modifiers changed from: protected */
    public String searchPage(int i) {
        if (i == 1) {
            return "";
        }
        return "page/" + i + '/';
    }

    /* access modifiers changed from: protected */
    public Request searchMangaRequest(int i, String str, FilterList filterList) {
        Intrinsics.checkNotNullParameter(str, "query");
        Intrinsics.checkNotNullParameter(filterList, "filters");
        if (useLoadMoreRequest()) {
            return searchLoadMoreRequest(i, str, filterList);
        }
        return searchRequest(i, str, filterList);
    }

    /* access modifiers changed from: protected */
    public Request searchRequest(int i, String str, FilterList filterList) {
        Intrinsics.checkNotNullParameter(str, "query");
        Intrinsics.checkNotNullParameter(filterList, "filters");
        HttpUrl.Companion companion = HttpUrl.Companion;
        HttpUrl.Builder newBuilder = companion.get(getBaseUrl() + '/' + searchPage(i)).newBuilder();
        newBuilder.addQueryParameter("s", str);
        newBuilder.addQueryParameter("post_type", "wp-manga");
        for (AuthorFilter authorFilter : (Iterable) filterList) {
            if (authorFilter instanceof AuthorFilter) {
                AuthorFilter authorFilter2 = authorFilter;
                if (!StringsKt.isBlank((CharSequence) authorFilter2.getState())) {
                    newBuilder.addQueryParameter("author", (String) authorFilter2.getState());
                }
            } else if (authorFilter instanceof ArtistFilter) {
                ArtistFilter artistFilter = (ArtistFilter) authorFilter;
                if (!StringsKt.isBlank((CharSequence) artistFilter.getState())) {
                    newBuilder.addQueryParameter("artist", (String) artistFilter.getState());
                }
            } else if (authorFilter instanceof YearFilter) {
                YearFilter yearFilter = (YearFilter) authorFilter;
                if (!StringsKt.isBlank((CharSequence) yearFilter.getState())) {
                    newBuilder.addQueryParameter("release", (String) yearFilter.getState());
                }
            } else if (authorFilter instanceof StatusFilter) {
                for (Tag tag : (Iterable) ((StatusFilter) authorFilter).getState()) {
                    if (((Boolean) tag.getState()).booleanValue()) {
                        newBuilder.addQueryParameter("status[]", tag.getId());
                    }
                }
            } else if (authorFilter instanceof OrderByFilter) {
                OrderByFilter orderByFilter = (OrderByFilter) authorFilter;
                if (((Number) orderByFilter.getState()).intValue() != 0) {
                    newBuilder.addQueryParameter("m_orderby", orderByFilter.toUriPart());
                }
            } else if (authorFilter instanceof AdultContentFilter) {
                newBuilder.addQueryParameter("adult", ((AdultContentFilter) authorFilter).toUriPart());
            } else if (authorFilter instanceof GenreConditionFilter) {
                newBuilder.addQueryParameter("op", ((GenreConditionFilter) authorFilter).toUriPart());
            } else if (authorFilter instanceof GenreList) {
                Collection arrayList = new ArrayList();
                for (Object next : (Iterable) ((GenreList) authorFilter).getState()) {
                    if (((Boolean) ((GenreCheckBox) next).getState()).booleanValue()) {
                        arrayList.add(next);
                    }
                }
                List<GenreCheckBox> list = (List) arrayList;
                if (!list.isEmpty()) {
                    for (GenreCheckBox id : list) {
                        newBuilder.addQueryParameter("genre[]", id.getId());
                    }
                }
            }
        }
        return RequestsKt.GET$default(newBuilder.build(), getHeaders(), (CacheControl) null, 4, (Object) null);
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v10, resolved type: java.util.List} */
    /* access modifiers changed from: protected */
    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public okhttp3.Request searchLoadMoreRequest(int r17, java.lang.String r18, eu.kanade.tachiyomi.source.model.FilterList r19) {
        /*
            r16 = this;
            r0 = r18
            r1 = r19
            java.lang.String r2 = "query"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r0, r2)
            java.lang.String r2 = "filters"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r1, r2)
            okhttp3.FormBody$Builder r2 = new okhttp3.FormBody$Builder
            r3 = 0
            r4 = 1
            r2.<init>(r3, r4, r3)
            java.lang.String r5 = "action"
            java.lang.String r6 = "madara_load_more"
            r2.add(r5, r6)
            int r5 = r17 + -1
            java.lang.String r5 = java.lang.String.valueOf(r5)
            java.lang.String r6 = "page"
            r2.add(r6, r5)
            java.lang.String r5 = "template"
            java.lang.String r6 = "madara-core/content/content-search"
            r2.add(r5, r6)
            java.lang.String r5 = "vars[paged]"
            java.lang.String r6 = "1"
            r2.add(r5, r6)
            java.lang.String r5 = "vars[template]"
            java.lang.String r6 = "archive"
            r2.add(r5, r6)
            java.lang.String r5 = "vars[sidebar]"
            java.lang.String r6 = "right"
            r2.add(r5, r6)
            java.lang.String r5 = "vars[post_type]"
            java.lang.String r6 = "wp-manga"
            r2.add(r5, r6)
            java.lang.String r5 = "vars[post_status]"
            java.lang.String r6 = "publish"
            r2.add(r5, r6)
            java.lang.String r5 = "vars[manga_archives_item_layout]"
            java.lang.String r6 = "big_thumbnail"
            r2.add(r5, r6)
            boolean r5 = r16.getFilterNonMangaItems()
            if (r5 == 0) goto L_0x006c
            java.lang.String r5 = "vars[meta_query][0][key]"
            java.lang.String r6 = "_wp_manga_chapter_type"
            r2.add(r5, r6)
            java.lang.String r5 = "vars[meta_query][0][value]"
            java.lang.String r6 = "manga"
            r2.add(r5, r6)
        L_0x006c:
            java.lang.String r5 = "vars[s]"
            r2.add(r5, r0)
            boolean r0 = r16.getFilterNonMangaItems()
            java.lang.Iterable r1 = (java.lang.Iterable) r1
            java.util.ArrayList r5 = new java.util.ArrayList
            r5.<init>()
            java.util.Collection r5 = (java.util.Collection) r5
            java.util.Iterator r6 = r1.iterator()
        L_0x0082:
            boolean r7 = r6.hasNext()
            if (r7 == 0) goto L_0x0094
            java.lang.Object r7 = r6.next()
            boolean r8 = r7 instanceof eu.kanade.tachiyomi.multisrc.madara.Madara.GenreList
            if (r8 == 0) goto L_0x0082
            r5.add(r7)
            goto L_0x0082
        L_0x0094:
            java.util.List r5 = (java.util.List) r5
            java.lang.Object r5 = kotlin.collections.CollectionsKt.firstOrNull(r5)
            eu.kanade.tachiyomi.multisrc.madara.Madara$GenreList r5 = (eu.kanade.tachiyomi.multisrc.madara.Madara.GenreList) r5
            r6 = 10
            if (r5 == 0) goto L_0x00fc
            java.lang.Object r5 = r5.getState()
            java.util.List r5 = (java.util.List) r5
            if (r5 == 0) goto L_0x00fc
            java.lang.Iterable r5 = (java.lang.Iterable) r5
            java.util.ArrayList r3 = new java.util.ArrayList
            r3.<init>()
            java.util.Collection r3 = (java.util.Collection) r3
            java.util.Iterator r5 = r5.iterator()
        L_0x00b5:
            boolean r7 = r5.hasNext()
            if (r7 == 0) goto L_0x00d2
            java.lang.Object r7 = r5.next()
            r8 = r7
            eu.kanade.tachiyomi.multisrc.madara.Madara$GenreCheckBox r8 = (eu.kanade.tachiyomi.multisrc.madara.Madara.GenreCheckBox) r8
            java.lang.Object r8 = r8.getState()
            java.lang.Boolean r8 = (java.lang.Boolean) r8
            boolean r8 = r8.booleanValue()
            if (r8 == 0) goto L_0x00b5
            r3.add(r7)
            goto L_0x00b5
        L_0x00d2:
            java.util.List r3 = (java.util.List) r3
            java.lang.Iterable r3 = (java.lang.Iterable) r3
            java.util.ArrayList r5 = new java.util.ArrayList
            int r7 = kotlin.collections.CollectionsKt.collectionSizeOrDefault(r3, r6)
            r5.<init>(r7)
            java.util.Collection r5 = (java.util.Collection) r5
            java.util.Iterator r3 = r3.iterator()
        L_0x00e5:
            boolean r7 = r3.hasNext()
            if (r7 == 0) goto L_0x00f9
            java.lang.Object r7 = r3.next()
            eu.kanade.tachiyomi.multisrc.madara.Madara$GenreCheckBox r7 = (eu.kanade.tachiyomi.multisrc.madara.Madara.GenreCheckBox) r7
            java.lang.String r7 = r7.getId()
            r5.add(r7)
            goto L_0x00e5
        L_0x00f9:
            r3 = r5
            java.util.List r3 = (java.util.List) r3
        L_0x00fc:
            if (r3 != 0) goto L_0x0102
            java.util.List r3 = kotlin.collections.CollectionsKt.emptyList()
        L_0x0102:
            java.util.Iterator r1 = r1.iterator()
            r5 = 0
            r7 = 0
        L_0x0108:
            boolean r8 = r1.hasNext()
            if (r8 == 0) goto L_0x0456
            java.lang.Object r8 = r1.next()
            eu.kanade.tachiyomi.source.model.Filter r8 = (eu.kanade.tachiyomi.source.model.Filter) r8
            boolean r9 = r8 instanceof eu.kanade.tachiyomi.multisrc.madara.Madara.AuthorFilter
            java.lang.String r10 = "][terms]"
            java.lang.String r11 = "name"
            java.lang.String r12 = "][field]"
            java.lang.String r13 = "][taxonomy]"
            java.lang.String r14 = "vars[tax_query]["
            if (r9 == 0) goto L_0x0171
            eu.kanade.tachiyomi.multisrc.madara.Madara$AuthorFilter r8 = (eu.kanade.tachiyomi.multisrc.madara.Madara.AuthorFilter) r8
            java.lang.Object r9 = r8.getState()
            java.lang.CharSequence r9 = (java.lang.CharSequence) r9
            boolean r9 = kotlin.text.StringsKt.isBlank(r9)
            if (r9 != 0) goto L_0x0108
            java.lang.StringBuilder r9 = new java.lang.StringBuilder
            r9.<init>(r14)
            r9.append(r7)
            r9.append(r13)
            java.lang.String r9 = r9.toString()
            java.lang.String r13 = "wp-manga-author"
            r2.add(r9, r13)
            java.lang.StringBuilder r9 = new java.lang.StringBuilder
            r9.<init>(r14)
            r9.append(r7)
            r9.append(r12)
            java.lang.String r9 = r9.toString()
            r2.add(r9, r11)
            java.lang.StringBuilder r9 = new java.lang.StringBuilder
            r9.<init>(r14)
            r9.append(r7)
            r9.append(r10)
            java.lang.String r9 = r9.toString()
            java.lang.Object r8 = r8.getState()
            java.lang.String r8 = (java.lang.String) r8
            r2.add(r9, r8)
        L_0x016e:
            int r7 = r7 + 1
            goto L_0x0108
        L_0x0171:
            boolean r9 = r8 instanceof eu.kanade.tachiyomi.multisrc.madara.Madara.ArtistFilter
            if (r9 == 0) goto L_0x01c2
            eu.kanade.tachiyomi.multisrc.madara.Madara$ArtistFilter r8 = (eu.kanade.tachiyomi.multisrc.madara.Madara.ArtistFilter) r8
            java.lang.Object r9 = r8.getState()
            java.lang.CharSequence r9 = (java.lang.CharSequence) r9
            boolean r9 = kotlin.text.StringsKt.isBlank(r9)
            if (r9 != 0) goto L_0x0108
            java.lang.StringBuilder r9 = new java.lang.StringBuilder
            r9.<init>(r14)
            r9.append(r7)
            r9.append(r13)
            java.lang.String r9 = r9.toString()
            java.lang.String r13 = "wp-manga-artist"
            r2.add(r9, r13)
            java.lang.StringBuilder r9 = new java.lang.StringBuilder
            r9.<init>(r14)
            r9.append(r7)
            r9.append(r12)
            java.lang.String r9 = r9.toString()
            r2.add(r9, r11)
            java.lang.StringBuilder r9 = new java.lang.StringBuilder
            r9.<init>(r14)
            r9.append(r7)
            r9.append(r10)
            java.lang.String r9 = r9.toString()
            java.lang.Object r8 = r8.getState()
            java.lang.String r8 = (java.lang.String) r8
            r2.add(r9, r8)
            goto L_0x016e
        L_0x01c2:
            boolean r9 = r8 instanceof eu.kanade.tachiyomi.multisrc.madara.Madara.YearFilter
            if (r9 == 0) goto L_0x0214
            eu.kanade.tachiyomi.multisrc.madara.Madara$YearFilter r8 = (eu.kanade.tachiyomi.multisrc.madara.Madara.YearFilter) r8
            java.lang.Object r9 = r8.getState()
            java.lang.CharSequence r9 = (java.lang.CharSequence) r9
            boolean r9 = kotlin.text.StringsKt.isBlank(r9)
            if (r9 != 0) goto L_0x0108
            java.lang.StringBuilder r9 = new java.lang.StringBuilder
            r9.<init>(r14)
            r9.append(r7)
            r9.append(r13)
            java.lang.String r9 = r9.toString()
            java.lang.String r13 = "wp-manga-release"
            r2.add(r9, r13)
            java.lang.StringBuilder r9 = new java.lang.StringBuilder
            r9.<init>(r14)
            r9.append(r7)
            r9.append(r12)
            java.lang.String r9 = r9.toString()
            r2.add(r9, r11)
            java.lang.StringBuilder r9 = new java.lang.StringBuilder
            r9.<init>(r14)
            r9.append(r7)
            r9.append(r10)
            java.lang.String r9 = r9.toString()
            java.lang.Object r8 = r8.getState()
            java.lang.String r8 = (java.lang.String) r8
            r2.add(r9, r8)
            goto L_0x016e
        L_0x0214:
            boolean r9 = r8 instanceof eu.kanade.tachiyomi.multisrc.madara.Madara.StatusFilter
            r10 = 93
            java.lang.String r11 = "][key]"
            java.lang.String r15 = "vars[meta_query]["
            if (r9 == 0) goto L_0x02ca
            eu.kanade.tachiyomi.multisrc.madara.Madara$StatusFilter r8 = (eu.kanade.tachiyomi.multisrc.madara.Madara.StatusFilter) r8
            java.lang.Object r8 = r8.getState()
            java.lang.Iterable r8 = (java.lang.Iterable) r8
            java.util.ArrayList r9 = new java.util.ArrayList
            r9.<init>()
            java.util.Collection r9 = (java.util.Collection) r9
            java.util.Iterator r8 = r8.iterator()
        L_0x0231:
            boolean r12 = r8.hasNext()
            if (r12 == 0) goto L_0x024e
            java.lang.Object r12 = r8.next()
            r13 = r12
            eu.kanade.tachiyomi.multisrc.madara.Madara$Tag r13 = (eu.kanade.tachiyomi.multisrc.madara.Madara.Tag) r13
            java.lang.Object r13 = r13.getState()
            java.lang.Boolean r13 = (java.lang.Boolean) r13
            boolean r13 = r13.booleanValue()
            if (r13 == 0) goto L_0x0231
            r9.add(r12)
            goto L_0x0231
        L_0x024e:
            java.util.List r9 = (java.util.List) r9
            java.lang.Iterable r9 = (java.lang.Iterable) r9
            java.util.ArrayList r8 = new java.util.ArrayList
            int r12 = kotlin.collections.CollectionsKt.collectionSizeOrDefault(r9, r6)
            r8.<init>(r12)
            java.util.Collection r8 = (java.util.Collection) r8
            java.util.Iterator r9 = r9.iterator()
        L_0x0261:
            boolean r12 = r9.hasNext()
            if (r12 == 0) goto L_0x0275
            java.lang.Object r12 = r9.next()
            eu.kanade.tachiyomi.multisrc.madara.Madara$Tag r12 = (eu.kanade.tachiyomi.multisrc.madara.Madara.Tag) r12
            java.lang.String r12 = r12.getId()
            r8.add(r12)
            goto L_0x0261
        L_0x0275:
            java.util.List r8 = (java.util.List) r8
            r9 = r8
            java.util.Collection r9 = (java.util.Collection) r9
            boolean r9 = r9.isEmpty()
            if (r9 != 0) goto L_0x0108
            java.lang.StringBuilder r9 = new java.lang.StringBuilder
            r9.<init>(r15)
            r9.append(r0)
            r9.append(r11)
            java.lang.String r9 = r9.toString()
            java.lang.String r11 = "_wp_manga_status"
            r2.add(r9, r11)
            java.lang.Iterable r8 = (java.lang.Iterable) r8
            java.util.Iterator r8 = r8.iterator()
            r9 = 0
        L_0x029b:
            boolean r11 = r8.hasNext()
            if (r11 == 0) goto L_0x03b3
            java.lang.Object r11 = r8.next()
            int r12 = r9 + 1
            if (r9 >= 0) goto L_0x02ac
            kotlin.collections.CollectionsKt.throwIndexOverflow()
        L_0x02ac:
            java.lang.String r11 = (java.lang.String) r11
            java.lang.StringBuilder r13 = new java.lang.StringBuilder
            r13.<init>(r15)
            r13.append(r0)
            java.lang.String r14 = "][value]["
            r13.append(r14)
            r13.append(r9)
            r13.append(r10)
            java.lang.String r9 = r13.toString()
            r2.add(r9, r11)
            r9 = r12
            goto L_0x029b
        L_0x02ca:
            boolean r9 = r8 instanceof eu.kanade.tachiyomi.multisrc.madara.Madara.OrderByFilter
            if (r9 == 0) goto L_0x0368
            eu.kanade.tachiyomi.multisrc.madara.Madara$OrderByFilter r8 = (eu.kanade.tachiyomi.multisrc.madara.Madara.OrderByFilter) r8
            java.lang.Object r9 = r8.getState()
            java.lang.Number r9 = (java.lang.Number) r9
            int r9 = r9.intValue()
            if (r9 == 0) goto L_0x0108
            java.lang.String r8 = r8.toUriPart()
            int r9 = r8.hashCode()
            java.lang.String r10 = "vars[meta_key]"
            java.lang.String r11 = "meta_value_num"
            java.lang.String r12 = "vars[order]"
            java.lang.String r13 = "vars[orderby]"
            java.lang.String r14 = "DESC"
            switch(r9) {
                case -1109880953: goto L_0x0349;
                case -938102371: goto L_0x0334;
                case 112204398: goto L_0x031e;
                case 1394955557: goto L_0x0308;
                case 1920525939: goto L_0x02f3;
                default: goto L_0x02f1;
            }
        L_0x02f1:
            goto L_0x035e
        L_0x02f3:
            java.lang.String r9 = "alphabet"
            boolean r8 = r8.equals(r9)
            if (r8 != 0) goto L_0x02fc
            goto L_0x035e
        L_0x02fc:
            java.lang.String r8 = "post_title"
            r2.add(r13, r8)
            java.lang.String r8 = "ASC"
            r2.add(r12, r8)
            goto L_0x0108
        L_0x0308:
            java.lang.String r9 = "trending"
            boolean r8 = r8.equals(r9)
            if (r8 != 0) goto L_0x0311
            goto L_0x035e
        L_0x0311:
            r2.add(r13, r11)
            java.lang.String r8 = "_wp_manga_week_views_value"
            r2.add(r10, r8)
            r2.add(r12, r14)
            goto L_0x0108
        L_0x031e:
            java.lang.String r9 = "views"
            boolean r8 = r8.equals(r9)
            if (r8 != 0) goto L_0x0327
            goto L_0x035e
        L_0x0327:
            r2.add(r13, r11)
            java.lang.String r8 = "_wp_manga_views"
            r2.add(r10, r8)
            r2.add(r12, r14)
            goto L_0x0108
        L_0x0334:
            java.lang.String r9 = "rating"
            boolean r8 = r8.equals(r9)
            if (r8 != 0) goto L_0x033d
            goto L_0x035e
        L_0x033d:
            java.lang.String r8 = "vars[orderby][query_average_reviews]"
            r2.add(r8, r14)
            java.lang.String r8 = "vars[orderby][query_total_reviews]"
            r2.add(r8, r14)
            goto L_0x0108
        L_0x0349:
            java.lang.String r9 = "latest"
            boolean r8 = r8.equals(r9)
            if (r8 == 0) goto L_0x035e
            r2.add(r13, r11)
            r2.add(r12, r14)
            java.lang.String r8 = "_latest_update"
            r2.add(r10, r8)
            goto L_0x0108
        L_0x035e:
            java.lang.String r8 = "date"
            r2.add(r13, r8)
            r2.add(r12, r14)
            goto L_0x0108
        L_0x0368:
            boolean r9 = r8 instanceof eu.kanade.tachiyomi.multisrc.madara.Madara.AdultContentFilter
            if (r9 == 0) goto L_0x03b7
            eu.kanade.tachiyomi.multisrc.madara.Madara$AdultContentFilter r8 = (eu.kanade.tachiyomi.multisrc.madara.Madara.AdultContentFilter) r8
            java.lang.Object r9 = r8.getState()
            java.lang.Number r9 = (java.lang.Number) r9
            int r9 = r9.intValue()
            if (r9 == 0) goto L_0x0108
            java.lang.StringBuilder r9 = new java.lang.StringBuilder
            r9.<init>(r15)
            r9.append(r0)
            r9.append(r11)
            java.lang.String r9 = r9.toString()
            java.lang.String r10 = "manga_adult_content"
            r2.add(r9, r10)
            java.lang.StringBuilder r9 = new java.lang.StringBuilder
            r9.<init>(r15)
            r9.append(r0)
            java.lang.String r10 = "][compare]"
            r9.append(r10)
            java.lang.String r9 = r9.toString()
            java.lang.Object r8 = r8.getState()
            java.lang.Number r8 = (java.lang.Number) r8
            int r8 = r8.intValue()
            if (r8 != r4) goto L_0x03ae
            java.lang.String r8 = "not exists"
            goto L_0x03b0
        L_0x03ae:
            java.lang.String r8 = "exists"
        L_0x03b0:
            r2.add(r9, r8)
        L_0x03b3:
            int r0 = r0 + 1
            goto L_0x0108
        L_0x03b7:
            boolean r9 = r8 instanceof eu.kanade.tachiyomi.multisrc.madara.Madara.GenreConditionFilter
            if (r9 == 0) goto L_0x03ea
            eu.kanade.tachiyomi.multisrc.madara.Madara$GenreConditionFilter r8 = (eu.kanade.tachiyomi.multisrc.madara.Madara.GenreConditionFilter) r8
            java.lang.Object r8 = r8.getState()
            java.lang.Number r8 = (java.lang.Number) r8
            int r8 = r8.intValue()
            if (r8 != r4) goto L_0x0108
            r8 = r3
            java.util.Collection r8 = (java.util.Collection) r8
            boolean r8 = r8.isEmpty()
            if (r8 != 0) goto L_0x0108
            java.lang.StringBuilder r8 = new java.lang.StringBuilder
            r8.<init>(r14)
            r8.append(r7)
            java.lang.String r9 = "][operation]"
            r8.append(r9)
            java.lang.String r8 = r8.toString()
            java.lang.String r9 = "AND"
            r2.add(r8, r9)
            goto L_0x0108
        L_0x03ea:
            boolean r8 = r8 instanceof eu.kanade.tachiyomi.multisrc.madara.Madara.GenreList
            if (r8 == 0) goto L_0x0108
            r8 = r3
            java.util.Collection r8 = (java.util.Collection) r8
            boolean r8 = r8.isEmpty()
            if (r8 != 0) goto L_0x0108
            java.lang.StringBuilder r8 = new java.lang.StringBuilder
            r8.<init>(r14)
            r8.append(r7)
            r8.append(r13)
            java.lang.String r8 = r8.toString()
            java.lang.String r9 = "wp-manga-genre"
            r2.add(r8, r9)
            java.lang.StringBuilder r8 = new java.lang.StringBuilder
            r8.<init>(r14)
            r8.append(r7)
            r8.append(r12)
            java.lang.String r8 = r8.toString()
            java.lang.String r9 = "slug"
            r2.add(r8, r9)
            r8 = r3
            java.lang.Iterable r8 = (java.lang.Iterable) r8
            java.util.Iterator r8 = r8.iterator()
            r9 = 0
        L_0x0427:
            boolean r11 = r8.hasNext()
            if (r11 == 0) goto L_0x016e
            java.lang.Object r11 = r8.next()
            int r12 = r9 + 1
            if (r9 >= 0) goto L_0x0438
            kotlin.collections.CollectionsKt.throwIndexOverflow()
        L_0x0438:
            java.lang.String r11 = (java.lang.String) r11
            java.lang.StringBuilder r13 = new java.lang.StringBuilder
            r13.<init>(r14)
            r13.append(r7)
            java.lang.String r15 = "][terms]["
            r13.append(r15)
            r13.append(r9)
            r13.append(r10)
            java.lang.String r9 = r13.toString()
            r2.add(r9, r11)
            r9 = r12
            goto L_0x0427
        L_0x0456:
            okhttp3.FormBody r0 = r2.build()
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = r16.getBaseUrl()
            r1.append(r2)
            java.lang.String r2 = "/wp-admin/admin-ajax.php"
            r1.append(r2)
            java.lang.String r3 = r1.toString()
            okhttp3.Headers r4 = r16.getXhrHeaders()
            r5 = r0
            okhttp3.RequestBody r5 = (okhttp3.RequestBody) r5
            r7 = 8
            r8 = 0
            r6 = 0
            okhttp3.Request r0 = eu.kanade.tachiyomi.network.RequestsKt.POST$default(r3, r4, r5, r6, r7, r8)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: eu.kanade.tachiyomi.multisrc.madara.Madara.searchLoadMoreRequest(int, java.lang.String, eu.kanade.tachiyomi.source.model.FilterList):okhttp3.Request");
    }

    /* access modifiers changed from: protected */
    public Map<String, String> getStatusFilterOptions() {
        return this.statusFilterOptions;
    }

    /* access modifiers changed from: protected */
    public Map<String, String> getOrderByFilterOptions() {
        return this.orderByFilterOptions;
    }

    /* access modifiers changed from: protected */
    public Map<String, String> getGenreConditionFilterOptions() {
        return this.genreConditionFilterOptions;
    }

    /* access modifiers changed from: protected */
    public Map<String, String> getAdultContentFilterOptions() {
        return this.adultContentFilterOptions;
    }

    @Metadata(d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\b\u0016\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B1\u0012\u0006\u0010\u0003\u001a\u00020\u0002\u0012\u0018\u0010\u0004\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00020\u00060\u0005\u0012\b\b\u0002\u0010\u0007\u001a\u00020\b¢\u0006\u0002\u0010\tJ\u0006\u0010\u000b\u001a\u00020\u0002R\"\u0010\u0004\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00020\u00060\u0005X\u0004¢\u0006\u0004\n\u0002\u0010\n¨\u0006\f"}, d2 = {"Leu/kanade/tachiyomi/multisrc/madara/Madara$UriPartFilter;", "Leu/kanade/tachiyomi/source/model/Filter$Select;", "", "displayName", "vals", "", "Lkotlin/Pair;", "state", "", "(Ljava/lang/String;[Lkotlin/Pair;I)V", "[Lkotlin/Pair;", "toUriPart", "madara_release"}, k = 1, mv = {1, 7, 1}, xi = 48)
    /* compiled from: Madara.kt */
    public static class UriPartFilter extends Filter.Select<String> {
        private final Pair<String, String>[] vals;

        /* JADX INFO: this call moved to the top of the method (can break code semantics) */
        public /* synthetic */ UriPartFilter(String str, Pair[] pairArr, int i, int i2, DefaultConstructorMarker defaultConstructorMarker) {
            this(str, pairArr, (i2 & 4) != 0 ? 0 : i);
        }

        public final String toUriPart() {
            return (String) this.vals[((Number) getState()).intValue()].getSecond();
        }

        /* JADX WARNING: Illegal instructions before constructor call */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public UriPartFilter(java.lang.String r6, kotlin.Pair<java.lang.String, java.lang.String>[] r7, int r8) {
            /*
                r5 = this;
                java.lang.String r0 = "displayName"
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r6, r0)
                java.lang.String r0 = "vals"
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r7, r0)
                java.util.ArrayList r0 = new java.util.ArrayList
                int r1 = r7.length
                r0.<init>(r1)
                java.util.Collection r0 = (java.util.Collection) r0
                int r1 = r7.length
                r2 = 0
                r3 = 0
            L_0x0015:
                if (r3 >= r1) goto L_0x0025
                r4 = r7[r3]
                java.lang.Object r4 = r4.getFirst()
                java.lang.String r4 = (java.lang.String) r4
                r0.add(r4)
                int r3 = r3 + 1
                goto L_0x0015
            L_0x0025:
                java.util.List r0 = (java.util.List) r0
                java.util.Collection r0 = (java.util.Collection) r0
                java.lang.String[] r1 = new java.lang.String[r2]
                java.lang.Object[] r0 = r0.toArray(r1)
                r5.<init>(r6, r0, r8)
                r5.vals = r7
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: eu.kanade.tachiyomi.multisrc.madara.Madara.UriPartFilter.<init>(java.lang.String, kotlin.Pair[], int):void");
        }
    }

    @Metadata(d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0005\b\u0016\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003¢\u0006\u0002\u0010\u0005R\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007¨\u0006\b"}, d2 = {"Leu/kanade/tachiyomi/multisrc/madara/Madara$Tag;", "Leu/kanade/tachiyomi/source/model/Filter$CheckBox;", "name", "", "id", "(Ljava/lang/String;Ljava/lang/String;)V", "getId", "()Ljava/lang/String;", "madara_release"}, k = 1, mv = {1, 7, 1}, xi = 48)
    /* compiled from: Madara.kt */
    public static class Tag extends Filter.CheckBox {
        private final String id;

        /* JADX INFO: super call moved to the top of the method (can break code semantics) */
        public Tag(String str, String str2) {
            super(str, false, 2, (DefaultConstructorMarker) null);
            Intrinsics.checkNotNullParameter(str, "name");
            Intrinsics.checkNotNullParameter(str2, "id");
            this.id = str2;
        }

        public final String getId() {
            return this.id;
        }
    }

    @Metadata(d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004¨\u0006\u0005"}, d2 = {"Leu/kanade/tachiyomi/multisrc/madara/Madara$AuthorFilter;", "Leu/kanade/tachiyomi/source/model/Filter$Text;", "title", "", "(Ljava/lang/String;)V", "madara_release"}, k = 1, mv = {1, 7, 1}, xi = 48)
    /* compiled from: Madara.kt */
    protected static final class AuthorFilter extends Filter.Text {
        /* JADX INFO: super call moved to the top of the method (can break code semantics) */
        public AuthorFilter(String str) {
            super(str, (String) null, 2, (DefaultConstructorMarker) null);
            Intrinsics.checkNotNullParameter(str, "title");
        }
    }

    @Metadata(d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004¨\u0006\u0005"}, d2 = {"Leu/kanade/tachiyomi/multisrc/madara/Madara$ArtistFilter;", "Leu/kanade/tachiyomi/source/model/Filter$Text;", "title", "", "(Ljava/lang/String;)V", "madara_release"}, k = 1, mv = {1, 7, 1}, xi = 48)
    /* compiled from: Madara.kt */
    protected static final class ArtistFilter extends Filter.Text {
        /* JADX INFO: super call moved to the top of the method (can break code semantics) */
        public ArtistFilter(String str) {
            super(str, (String) null, 2, (DefaultConstructorMarker) null);
            Intrinsics.checkNotNullParameter(str, "title");
        }
    }

    @Metadata(d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004¨\u0006\u0005"}, d2 = {"Leu/kanade/tachiyomi/multisrc/madara/Madara$YearFilter;", "Leu/kanade/tachiyomi/source/model/Filter$Text;", "title", "", "(Ljava/lang/String;)V", "madara_release"}, k = 1, mv = {1, 7, 1}, xi = 48)
    /* compiled from: Madara.kt */
    protected static final class YearFilter extends Filter.Text {
        /* JADX INFO: super call moved to the top of the method (can break code semantics) */
        public YearFilter(String str) {
            super(str, (String) null, 2, (DefaultConstructorMarker) null);
            Intrinsics.checkNotNullParameter(str, "title");
        }
    }

    @Metadata(d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\b\u0002\b\u0004\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u001b\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00020\u0006¢\u0006\u0002\u0010\u0007¨\u0006\b"}, d2 = {"Leu/kanade/tachiyomi/multisrc/madara/Madara$StatusFilter;", "Leu/kanade/tachiyomi/source/model/Filter$Group;", "Leu/kanade/tachiyomi/multisrc/madara/Madara$Tag;", "title", "", "status", "", "(Ljava/lang/String;Ljava/util/List;)V", "madara_release"}, k = 1, mv = {1, 7, 1}, xi = 48)
    /* compiled from: Madara.kt */
    protected static final class StatusFilter extends Filter.Group<Tag> {
        /* JADX INFO: super call moved to the top of the method (can break code semantics) */
        public StatusFilter(String str, List<? extends Tag> list) {
            super(str, list);
            Intrinsics.checkNotNullParameter(str, "title");
            Intrinsics.checkNotNullParameter(list, "status");
        }
    }

    @Metadata(d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\b\u0004\u0018\u00002\u00020\u0001B1\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0018\u0010\u0004\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00030\u00060\u0005\u0012\b\b\u0002\u0010\u0007\u001a\u00020\b¢\u0006\u0002\u0010\t¨\u0006\n"}, d2 = {"Leu/kanade/tachiyomi/multisrc/madara/Madara$OrderByFilter;", "Leu/kanade/tachiyomi/multisrc/madara/Madara$UriPartFilter;", "title", "", "options", "", "Lkotlin/Pair;", "state", "", "(Ljava/lang/String;Ljava/util/List;I)V", "madara_release"}, k = 1, mv = {1, 7, 1}, xi = 48)
    /* compiled from: Madara.kt */
    protected static final class OrderByFilter extends UriPartFilter {
        /* JADX INFO: this call moved to the top of the method (can break code semantics) */
        public /* synthetic */ OrderByFilter(String str, List list, int i, int i2, DefaultConstructorMarker defaultConstructorMarker) {
            this(str, list, (i2 & 4) != 0 ? 0 : i);
        }

        /* JADX INFO: super call moved to the top of the method (can break code semantics) */
        public OrderByFilter(String str, List<Pair<String, String>> list, int i) {
            super(str, (Pair[]) list.toArray(new Pair[0]), i);
            Intrinsics.checkNotNullParameter(str, "title");
            Intrinsics.checkNotNullParameter(list, "options");
        }
    }

    @Metadata(d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0004\u0018\u00002\u00020\u0001B'\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0018\u0010\u0004\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00030\u00060\u0005¢\u0006\u0002\u0010\u0007¨\u0006\b"}, d2 = {"Leu/kanade/tachiyomi/multisrc/madara/Madara$GenreConditionFilter;", "Leu/kanade/tachiyomi/multisrc/madara/Madara$UriPartFilter;", "title", "", "options", "", "Lkotlin/Pair;", "(Ljava/lang/String;Ljava/util/List;)V", "madara_release"}, k = 1, mv = {1, 7, 1}, xi = 48)
    /* compiled from: Madara.kt */
    protected static final class GenreConditionFilter extends UriPartFilter {
        /* JADX INFO: super call moved to the top of the method (can break code semantics) */
        public GenreConditionFilter(String str, List<Pair<String, String>> list) {
            super(str, (Pair[]) list.toArray(new Pair[0]), 0, 4, (DefaultConstructorMarker) null);
            Intrinsics.checkNotNullParameter(str, "title");
            Intrinsics.checkNotNullParameter(list, "options");
        }
    }

    @Metadata(d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0004\u0018\u00002\u00020\u0001B'\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0018\u0010\u0004\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00030\u00060\u0005¢\u0006\u0002\u0010\u0007¨\u0006\b"}, d2 = {"Leu/kanade/tachiyomi/multisrc/madara/Madara$AdultContentFilter;", "Leu/kanade/tachiyomi/multisrc/madara/Madara$UriPartFilter;", "title", "", "options", "", "Lkotlin/Pair;", "(Ljava/lang/String;Ljava/util/List;)V", "madara_release"}, k = 1, mv = {1, 7, 1}, xi = 48)
    /* compiled from: Madara.kt */
    protected static final class AdultContentFilter extends UriPartFilter {
        /* JADX INFO: super call moved to the top of the method (can break code semantics) */
        public AdultContentFilter(String str, List<Pair<String, String>> list) {
            super(str, (Pair[]) list.toArray(new Pair[0]), 0, 4, (DefaultConstructorMarker) null);
            Intrinsics.checkNotNullParameter(str, "title");
            Intrinsics.checkNotNullParameter(list, "options");
        }
    }

    @Metadata(d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0004\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u001b\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006¢\u0006\u0002\u0010\b¨\u0006\t"}, d2 = {"Leu/kanade/tachiyomi/multisrc/madara/Madara$GenreList;", "Leu/kanade/tachiyomi/source/model/Filter$Group;", "Leu/kanade/tachiyomi/multisrc/madara/Madara$GenreCheckBox;", "title", "", "genres", "", "Leu/kanade/tachiyomi/multisrc/madara/Madara$Genre;", "(Ljava/lang/String;Ljava/util/List;)V", "madara_release"}, k = 1, mv = {1, 7, 1}, xi = 48)
    /* compiled from: Madara.kt */
    protected static final class GenreList extends Filter.Group<GenreCheckBox> {
        /* JADX WARNING: Illegal instructions before constructor call */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public GenreList(java.lang.String r5, java.util.List<eu.kanade.tachiyomi.multisrc.madara.Madara.Genre> r6) {
            /*
                r4 = this;
                java.lang.String r0 = "title"
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r5, r0)
                java.lang.String r0 = "genres"
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r6, r0)
                java.lang.Iterable r6 = (java.lang.Iterable) r6
                java.util.ArrayList r0 = new java.util.ArrayList
                r1 = 10
                int r1 = kotlin.collections.CollectionsKt.collectionSizeOrDefault(r6, r1)
                r0.<init>(r1)
                java.util.Collection r0 = (java.util.Collection) r0
                java.util.Iterator r6 = r6.iterator()
            L_0x001d:
                boolean r1 = r6.hasNext()
                if (r1 == 0) goto L_0x003a
                java.lang.Object r1 = r6.next()
                eu.kanade.tachiyomi.multisrc.madara.Madara$Genre r1 = (eu.kanade.tachiyomi.multisrc.madara.Madara.Genre) r1
                eu.kanade.tachiyomi.multisrc.madara.Madara$GenreCheckBox r2 = new eu.kanade.tachiyomi.multisrc.madara.Madara$GenreCheckBox
                java.lang.String r3 = r1.getName()
                java.lang.String r1 = r1.getId()
                r2.<init>(r3, r1)
                r0.add(r2)
                goto L_0x001d
            L_0x003a:
                java.util.List r0 = (java.util.List) r0
                r4.<init>(r5, r0)
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: eu.kanade.tachiyomi.multisrc.madara.Madara.GenreList.<init>(java.lang.String, java.util.List):void");
        }
    }

    @Metadata(d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0005\u0018\u00002\u00020\u0001B\u0017\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003¢\u0006\u0002\u0010\u0005R\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007¨\u0006\b"}, d2 = {"Leu/kanade/tachiyomi/multisrc/madara/Madara$GenreCheckBox;", "Leu/kanade/tachiyomi/source/model/Filter$CheckBox;", "name", "", "id", "(Ljava/lang/String;Ljava/lang/String;)V", "getId", "()Ljava/lang/String;", "madara_release"}, k = 1, mv = {1, 7, 1}, xi = 48)
    /* compiled from: Madara.kt */
    public static final class GenreCheckBox extends Filter.CheckBox {
        private final String id;

        /* JADX INFO: super call moved to the top of the method (can break code semantics) */
        public GenreCheckBox(String str, String str2) {
            super(str, false, 2, (DefaultConstructorMarker) null);
            Intrinsics.checkNotNullParameter(str, "name");
            Intrinsics.checkNotNullParameter(str2, "id");
            this.id = str2;
        }

        /* JADX INFO: this call moved to the top of the method (can break code semantics) */
        public /* synthetic */ GenreCheckBox(String str, String str2, int i, DefaultConstructorMarker defaultConstructorMarker) {
            this(str, (i & 2) != 0 ? str : str2);
        }

        public final String getId() {
            return this.id;
        }
    }

    @Metadata(d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0006\u0018\u00002\u00020\u0001B\u0017\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003¢\u0006\u0002\u0010\u0005R\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\u0007¨\u0006\t"}, d2 = {"Leu/kanade/tachiyomi/multisrc/madara/Madara$Genre;", "", "name", "", "id", "(Ljava/lang/String;Ljava/lang/String;)V", "getId", "()Ljava/lang/String;", "getName", "madara_release"}, k = 1, mv = {1, 7, 1}, xi = 48)
    /* compiled from: Madara.kt */
    public static final class Genre {
        private final String id;
        private final String name;

        public Genre(String str, String str2) {
            Intrinsics.checkNotNullParameter(str, "name");
            Intrinsics.checkNotNullParameter(str2, "id");
            this.name = str;
            this.id = str2;
        }

        /* JADX INFO: this call moved to the top of the method (can break code semantics) */
        public /* synthetic */ Genre(String str, String str2, int i, DefaultConstructorMarker defaultConstructorMarker) {
            this(str, (i & 2) != 0 ? str : str2);
        }

        public final String getId() {
            return this.id;
        }

        public final String getName() {
            return this.name;
        }
    }

    public FilterList getFilterList() {
        launchIO(new Madara$getFilterList$1(this));
        Filter[] filterArr = new Filter[6];
        filterArr[0] = (Filter) new AuthorFilter(this.intl.get("author_filter_title"));
        filterArr[1] = (Filter) new ArtistFilter(this.intl.get("artist_filter_title"));
        filterArr[2] = (Filter) new YearFilter(this.intl.get("year_filter_title"));
        String str = this.intl.get("status_filter_title");
        Map<String, String> statusFilterOptions2 = getStatusFilterOptions();
        Collection arrayList = new ArrayList(statusFilterOptions2.size());
        for (Map.Entry next : statusFilterOptions2.entrySet()) {
            arrayList.add(new Tag((String) next.getKey(), (String) next.getValue()));
        }
        filterArr[3] = (Filter) new StatusFilter(str, (List) arrayList);
        filterArr[4] = (Filter) new OrderByFilter(this.intl.get("order_by_filter_title"), MapsKt.toList(getOrderByFilterOptions()), 0);
        filterArr[5] = (Filter) new AdultContentFilter(this.intl.get("adult_content_filter_title"), MapsKt.toList(getAdultContentFilterOptions()));
        List mutableListOf = CollectionsKt.mutableListOf(filterArr);
        if (!getGenresList().isEmpty()) {
            CollectionsKt.addAll(mutableListOf, CollectionsKt.listOf(new Filter[]{(Filter) new Filter.Separator((String) null, 1, (DefaultConstructorMarker) null), (Filter) new Filter.Header(this.intl.get("genre_filter_header")), (Filter) new GenreConditionFilter(this.intl.get("genre_condition_filter_title"), MapsKt.toList(getGenreConditionFilterOptions())), (Filter) new GenreList(this.intl.get("genre_filter_title"), getGenresList())}));
        } else if (getFetchGenres()) {
            CollectionsKt.addAll(mutableListOf, CollectionsKt.listOf(new Filter[]{(Filter) new Filter.Separator((String) null, 1, (DefaultConstructorMarker) null), (Filter) new Filter.Header(this.intl.get("genre_missing_warning"))}));
        }
        return new FilterList(mutableListOf);
    }

    /* access modifiers changed from: protected */
    public MangasPage searchMangaParse(Response response) {
        Intrinsics.checkNotNullParameter(response, "response");
        Element element = null;
        boolean z = true;
        Document asJsoup$default = JsoupExtensionsKt.asJsoup$default(response, (String) null, 1, (Object) null);
        Iterable select = asJsoup$default.select(searchMangaSelector());
        Intrinsics.checkNotNullExpressionValue(select, "document.select(searchMangaSelector())");
        Iterable<Element> iterable = select;
        Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterable, 10));
        for (Element searchMangaFromElement : iterable) {
            arrayList.add(searchMangaFromElement(searchMangaFromElement));
        }
        List list = (List) arrayList;
        String searchMangaNextPageSelector = searchMangaNextPageSelector();
        if (searchMangaNextPageSelector != null) {
            element = asJsoup$default.selectFirst(searchMangaNextPageSelector);
        }
        if (element == null) {
            z = false;
        }
        detectLoadMore(asJsoup$default);
        return new MangasPage(list, z);
    }

    /* access modifiers changed from: protected */
    public String searchMangaSelector() {
        return "div.c-tabs-item__content , .manga__item";
    }

    /* access modifiers changed from: protected */
    public String getSearchMangaUrlSelector() {
        return this.searchMangaUrlSelector;
    }

    /* access modifiers changed from: protected */
    public SManga searchMangaFromElement(Element element) {
        Intrinsics.checkNotNullParameter(element, "element");
        SManga create = SManga.Companion.create();
        Element selectFirst = element.selectFirst(getSearchMangaUrlSelector());
        Intrinsics.checkNotNull(selectFirst);
        String attr = selectFirst.attr("abs:href");
        Intrinsics.checkNotNullExpressionValue(attr, "it.attr(\"abs:href\")");
        setUrlWithoutDomain(create, attr);
        String ownText = selectFirst.ownText();
        Intrinsics.checkNotNullExpressionValue(ownText, "it.ownText()");
        create.setTitle(ownText);
        Element selectFirst2 = element.selectFirst("img");
        if (selectFirst2 != null) {
            Intrinsics.checkNotNullExpressionValue(selectFirst2, "it");
            create.setThumbnail_url(imageFromElement(selectFirst2));
        }
        return create;
    }

    /* access modifiers changed from: protected */
    public String searchMangaNextPageSelector() {
        return popularMangaNextPageSelector();
    }

    /* access modifiers changed from: protected */
    public final String[] getCompletedStatusList() {
        return this.completedStatusList;
    }

    /* access modifiers changed from: protected */
    public final String[] getOngoingStatusList() {
        return this.ongoingStatusList;
    }

    /* access modifiers changed from: protected */
    public final String[] getHiatusStatusList() {
        return this.hiatusStatusList;
    }

    /* access modifiers changed from: protected */
    public final String[] getCanceledStatusList() {
        return this.canceledStatusList;
    }

    /* access modifiers changed from: protected */
    public SManga mangaDetailsParse(Document document) {
        String ownText;
        String str;
        String ownText2;
        Document document2 = document;
        Intrinsics.checkNotNullParameter(document2, "document");
        SManga create = SManga.Companion.create();
        Element selectFirst = document2.selectFirst(getMangaDetailsSelectorTitle());
        Intrinsics.checkNotNull(selectFirst);
        String ownText3 = selectFirst.ownText();
        Intrinsics.checkNotNullExpressionValue(ownText3, "selectFirst(mangaDetailsSelectorTitle)!!.ownText()");
        create.setTitle(ownText3);
        List eachText = document2.select(getMangaDetailsSelectorAuthor()).eachText();
        Intrinsics.checkNotNullExpressionValue(eachText, "select(mangaDetailsSelectorAuthor).eachText()");
        Collection arrayList = new ArrayList();
        for (Object next : eachText) {
            String str2 = (String) next;
            Intrinsics.checkNotNullExpressionValue(str2, "it");
            if (notUpdating(str2)) {
                arrayList.add(next);
            }
        }
        String joinToString$default = CollectionsKt.joinToString$default((List) arrayList, (CharSequence) null, (CharSequence) null, (CharSequence) null, 0, (CharSequence) null, (Function1) null, 63, (Object) null);
        String str3 = null;
        if (StringsKt.isBlank(joinToString$default)) {
            joinToString$default = null;
        }
        if (joinToString$default != null) {
            create.setAuthor(joinToString$default);
        }
        List eachText2 = document2.select(getMangaDetailsSelectorArtist()).eachText();
        Intrinsics.checkNotNullExpressionValue(eachText2, "select(mangaDetailsSelectorArtist).eachText()");
        Collection arrayList2 = new ArrayList();
        for (Object next2 : eachText2) {
            String str4 = (String) next2;
            Intrinsics.checkNotNullExpressionValue(str4, "it");
            if (notUpdating(str4)) {
                arrayList2.add(next2);
            }
        }
        String joinToString$default2 = CollectionsKt.joinToString$default((List) arrayList2, (CharSequence) null, (CharSequence) null, (CharSequence) null, 0, (CharSequence) null, (Function1) null, 63, (Object) null);
        if (!StringsKt.isBlank(joinToString$default2)) {
            str3 = joinToString$default2;
        }
        if (str3 != null) {
            create.setArtist(str3);
        }
        Elements select = document2.select(getMangaDetailsSelectorDescription());
        String text = select.select("p").text();
        Intrinsics.checkNotNullExpressionValue(text, "it.select(\"p\").text()");
        if (text.length() > 0) {
            Iterable select2 = select.select("p");
            Intrinsics.checkNotNullExpressionValue(select2, "it.select(\"p\")");
            create.setDescription(CollectionsKt.joinToString$default(select2, "\n\n", (CharSequence) null, (CharSequence) null, 0, (CharSequence) null, Madara$mangaDetailsParse$1$7$1.INSTANCE, 30, (Object) null));
        } else {
            create.setDescription(select.text());
        }
        Element selectFirst2 = document2.selectFirst(getMangaDetailsSelectorThumbnail());
        if (selectFirst2 != null) {
            Intrinsics.checkNotNullExpressionValue(selectFirst2, "it");
            create.setThumbnail_url(imageFromElement(selectFirst2));
        }
        Element last = document2.select(getMangaDetailsSelectorStatus()).last();
        if (last != null) {
            String text2 = last.text();
            Intrinsics.checkNotNullExpressionValue(text2, "it.text()");
            CharSequence charSequence = text2;
            Appendable sb = new StringBuilder();
            int length = charSequence.length();
            int i = 0;
            for (int i2 = 0; i2 < length; i2++) {
                char charAt = charSequence.charAt(i2);
                if (Character.isLetterOrDigit(charAt) || CharsKt.isWhitespace(charAt)) {
                    sb.append(charAt);
                }
            }
            String sb2 = ((StringBuilder) sb).toString();
            Intrinsics.checkNotNullExpressionValue(sb2, "filterTo(StringBuilder(), predicate).toString()");
            String obj = StringsKt.trim(sb2).toString();
            if (containsIn(obj, this.completedStatusList)) {
                i = 2;
            } else if (containsIn(obj, this.ongoingStatusList)) {
                i = 1;
            } else if (containsIn(obj, this.hiatusStatusList)) {
                i = 6;
            } else if (containsIn(obj, this.canceledStatusList)) {
                i = 5;
            }
            create.setStatus(Integer.valueOf(i).intValue());
        }
        Iterable select3 = document2.select(getMangaDetailsSelectorGenre());
        Intrinsics.checkNotNullExpressionValue(select3, "select(mangaDetailsSelectorGenre)");
        Iterable<Element> iterable = select3;
        Collection arrayList3 = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterable, 10));
        for (Element text3 : iterable) {
            String text4 = text3.text();
            Intrinsics.checkNotNullExpressionValue(text4, "element.text()");
            Locale locale = Locale.ROOT;
            Intrinsics.checkNotNullExpressionValue(locale, "ROOT");
            String lowerCase = text4.toLowerCase(locale);
            Intrinsics.checkNotNullExpressionValue(lowerCase, "this as java.lang.String).toLowerCase(locale)");
            arrayList3.add(lowerCase);
        }
        Set mutableSet = CollectionsKt.toMutableSet((List) arrayList3);
        if (getMangaDetailsSelectorTag().length() > 0) {
            Iterable<Element> select4 = document2.select(getMangaDetailsSelectorTag());
            Intrinsics.checkNotNullExpressionValue(select4, "select(mangaDetailsSelectorTag)");
            for (Element element : select4) {
                if (!mutableSet.contains(element.text()) && element.text().length() <= 25) {
                    String text5 = element.text();
                    Intrinsics.checkNotNullExpressionValue(text5, "element.text()");
                    if (!StringsKt.contains(text5, "read", true)) {
                        String text6 = element.text();
                        Intrinsics.checkNotNullExpressionValue(text6, "element.text()");
                        if (!StringsKt.contains(text6, getName(), true)) {
                            String text7 = element.text();
                            Intrinsics.checkNotNullExpressionValue(text7, "element.text()");
                            if (!StringsKt.contains(text7, StringsKt.replace$default(getName(), " ", "", false, 4, (Object) null), true)) {
                                String text8 = element.text();
                                Intrinsics.checkNotNullExpressionValue(text8, "element.text()");
                                if (!StringsKt.contains(text8, create.getTitle(), true)) {
                                    String text9 = element.text();
                                    Intrinsics.checkNotNullExpressionValue(text9, "element.text()");
                                    if (!StringsKt.contains(text9, getAltName(), true)) {
                                        String text10 = element.text();
                                        Intrinsics.checkNotNullExpressionValue(text10, "element.text()");
                                        Locale locale2 = Locale.ROOT;
                                        Intrinsics.checkNotNullExpressionValue(locale2, "ROOT");
                                        String lowerCase2 = text10.toLowerCase(locale2);
                                        Intrinsics.checkNotNullExpressionValue(lowerCase2, "this as java.lang.String).toLowerCase(locale)");
                                        mutableSet.add(lowerCase2);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        Element selectFirst3 = document2.selectFirst(getSeriesTypeSelector());
        if (!(selectFirst3 == null || (ownText2 = selectFirst3.ownText()) == null)) {
            Intrinsics.checkNotNullExpressionValue(ownText2, "ownText()");
            if (ownText2.length() != 0 && notUpdating(ownText2) && !Intrinsics.areEqual(ownText2, "-") && !mutableSet.contains(ownText2)) {
                Locale locale3 = Locale.ROOT;
                Intrinsics.checkNotNullExpressionValue(locale3, "ROOT");
                String lowerCase3 = ownText2.toLowerCase(locale3);
                Intrinsics.checkNotNullExpressionValue(lowerCase3, "this as java.lang.String).toLowerCase(locale)");
                mutableSet.add(lowerCase3);
            }
        }
        create.setGenre(CollectionsKt.joinToString$default(CollectionsKt.toList(mutableSet), (CharSequence) null, (CharSequence) null, (CharSequence) null, 0, (CharSequence) null, Madara$mangaDetailsParse$1$12.INSTANCE, 31, (Object) null));
        Element selectFirst4 = document2.selectFirst(getAltNameSelector());
        if (!(selectFirst4 == null || (ownText = selectFirst4.ownText()) == null)) {
            Intrinsics.checkNotNullExpressionValue(ownText, "ownText()");
            if (!StringsKt.isBlank(ownText) && notUpdating(ownText)) {
                CharSequence description = create.getDescription();
                if (description == null || StringsKt.isBlank(description)) {
                    str = getAltName() + ' ' + ownText;
                } else {
                    str = create.getDescription() + "\n\n" + getAltName() + ' ' + ownText;
                }
                create.setDescription(str);
            }
        }
        return create;
    }

    public String getMangaDetailsSelectorTitle() {
        return this.mangaDetailsSelectorTitle;
    }

    public String getMangaDetailsSelectorAuthor() {
        return this.mangaDetailsSelectorAuthor;
    }

    public String getMangaDetailsSelectorArtist() {
        return this.mangaDetailsSelectorArtist;
    }

    public String getMangaDetailsSelectorStatus() {
        return this.mangaDetailsSelectorStatus;
    }

    public String getMangaDetailsSelectorDescription() {
        return this.mangaDetailsSelectorDescription;
    }

    public String getMangaDetailsSelectorThumbnail() {
        return this.mangaDetailsSelectorThumbnail;
    }

    public String getMangaDetailsSelectorGenre() {
        return this.mangaDetailsSelectorGenre;
    }

    public String getMangaDetailsSelectorTag() {
        return this.mangaDetailsSelectorTag;
    }

    public String getSeriesTypeSelector() {
        return this.seriesTypeSelector;
    }

    public String getAltNameSelector() {
        return this.altNameSelector;
    }

    public String getAltName() {
        return this.altName;
    }

    public Regex getUpdatingRegex() {
        return this.updatingRegex;
    }

    public final boolean notUpdating(String str) {
        Intrinsics.checkNotNullParameter(str, "<this>");
        return !getUpdatingRegex().containsMatchIn(str);
    }

    /* access modifiers changed from: protected */
    public String imageFromElement(Element element) {
        Intrinsics.checkNotNullParameter(element, "element");
        if (element.hasAttr("data-src")) {
            return element.attr("abs:data-src");
        }
        if (element.hasAttr("data-lazy-src")) {
            return element.attr("abs:data-lazy-src");
        }
        if (element.hasAttr("srcset")) {
            String attr = element.attr("abs:srcset");
            Intrinsics.checkNotNullExpressionValue(attr, "element.attr(\"abs:srcset\")");
            return getSrcSetImage(attr);
        } else if (element.hasAttr("data-cfsrc")) {
            return element.attr("abs:data-cfsrc");
        } else {
            return element.attr("abs:src");
        }
    }

    /* access modifiers changed from: protected */
    public final String getSrcSetImage(String str) {
        Comparable comparable;
        Intrinsics.checkNotNullParameter(str, "<this>");
        Regex regex = URL_REGEX;
        Collection arrayList = new ArrayList();
        for (Object next : StringsKt.split$default(str, new String[]{" "}, false, 0, 6, (Object) null)) {
            if (regex.matches((CharSequence) next)) {
                arrayList.add(next);
            }
        }
        Iterator it = ((List) arrayList).iterator();
        if (!it.hasNext()) {
            comparable = null;
        } else {
            Comparable str2 = ((String) it.next()).toString();
            while (it.hasNext()) {
                Comparable str3 = ((String) it.next()).toString();
                if (str2.compareTo(str3) < 0) {
                    str2 = str3;
                }
            }
            comparable = str2;
        }
        return (String) comparable;
    }

    /* access modifiers changed from: protected */
    public boolean getUseNewChapterEndpoint() {
        return this.useNewChapterEndpoint;
    }

    /* access modifiers changed from: protected */
    public Request oldXhrChaptersRequest(String str) {
        Intrinsics.checkNotNullParameter(str, "mangaId");
        RequestBody build = new FormBody.Builder((Charset) null, 1, (DefaultConstructorMarker) null).add("action", "manga_get_chapters").add("manga", str).build();
        return RequestsKt.POST$default(getBaseUrl() + "/wp-admin/admin-ajax.php", getXhrHeaders(), build, (CacheControl) null, 8, (Object) null);
    }

    /* access modifiers changed from: protected */
    public Request xhrChaptersRequest(String str) {
        Intrinsics.checkNotNullParameter(str, "mangaUrl");
        return RequestsKt.POST$default(str + "/ajax/chapters", getXhrHeaders(), (RequestBody) null, (CacheControl) null, 12, (Object) null);
    }

    /*  JADX ERROR: JadxRuntimeException in pass: CodeShrinkVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Don't wrap MOVE or CONST insns: 0x002a: MOVE  (r4v2 java.util.Collection) = (r2v3 java.util.Collection)
        	at jadx.core.dex.instructions.args.InsnArg.wrapArg(InsnArg.java:164)
        	at jadx.core.dex.visitors.shrink.CodeShrinkVisitor.assignInline(CodeShrinkVisitor.java:133)
        	at jadx.core.dex.visitors.shrink.CodeShrinkVisitor.checkInline(CodeShrinkVisitor.java:118)
        	at jadx.core.dex.visitors.shrink.CodeShrinkVisitor.shrinkBlock(CodeShrinkVisitor.java:65)
        	at jadx.core.dex.visitors.shrink.CodeShrinkVisitor.shrinkMethod(CodeShrinkVisitor.java:43)
        	at jadx.core.dex.visitors.shrink.CodeShrinkVisitor.visit(CodeShrinkVisitor.java:35)
        */
    protected java.util.List<eu.kanade.tachiyomi.source.model.SChapter> chapterListParse(okhttp3.Response r6) {
        /*
            r5 = this;
            java.lang.String r0 = "response"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r6, r0)
            r0 = 0
            r1 = 1
            org.jsoup.nodes.Document r6 = eu.kanade.tachiyomi.util.JsoupExtensionsKt.asJsoup$default(r6, r0, r1, r0)
            eu.kanade.tachiyomi.multisrc.madara.Madara$chapterListParse$1 r2 = new eu.kanade.tachiyomi.multisrc.madara.Madara$chapterListParse$1
            r2.<init>(r5, r6)
            kotlin.jvm.functions.Function0 r2 = (kotlin.jvm.functions.Function0) r2
            r5.launchIO(r2)
            java.lang.String r2 = "div[id^=manga-chapters-holder]"
            org.jsoup.select.Elements r2 = r6.select(r2)
            java.lang.String r3 = r5.chapterListSelector()
            org.jsoup.select.Elements r3 = r6.select(r3)
            boolean r4 = r3.isEmpty()
            if (r4 == 0) goto L_0x00a3
            r4 = r2
            java.util.Collection r4 = (java.util.Collection) r4
            if (r4 == 0) goto L_0x00a3
            boolean r4 = r4.isEmpty()
            if (r4 == 0) goto L_0x0035
            goto L_0x00a3
        L_0x0035:
            java.lang.String r6 = r6.location()
            java.lang.String r3 = "document.location()"
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r6, r3)
            java.lang.String r3 = "/"
            java.lang.CharSequence r3 = (java.lang.CharSequence) r3
            java.lang.String r6 = kotlin.text.StringsKt.removeSuffix(r6, r3)
            java.lang.String r3 = "data-id"
            java.lang.String r2 = r2.attr(r3)
            boolean r3 = r5.getUseNewChapterEndpoint()
            if (r3 != 0) goto L_0x0061
            boolean r3 = r5.oldChapterEndpointDisabled
            if (r3 == 0) goto L_0x0057
            goto L_0x0061
        L_0x0057:
            java.lang.String r3 = "mangaId"
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r2, r3)
            okhttp3.Request r2 = r5.oldXhrChaptersRequest(r2)
            goto L_0x0065
        L_0x0061:
            okhttp3.Request r2 = r5.xhrChaptersRequest(r6)
        L_0x0065:
            okhttp3.OkHttpClient r3 = r5.getClient()
            okhttp3.Call r2 = r3.newCall(r2)
            okhttp3.Response r2 = r2.execute()
            boolean r3 = r5.getUseNewChapterEndpoint()
            if (r3 != 0) goto L_0x0094
            int r3 = r2.code()
            r4 = 400(0x190, float:5.6E-43)
            if (r3 != r4) goto L_0x0094
            r2.close()
            r5.oldChapterEndpointDisabled = r1
            okhttp3.Request r6 = r5.xhrChaptersRequest(r6)
            okhttp3.OkHttpClient r2 = r5.getClient()
            okhttp3.Call r6 = r2.newCall(r6)
            okhttp3.Response r2 = r6.execute()
        L_0x0094:
            org.jsoup.nodes.Document r6 = eu.kanade.tachiyomi.util.JsoupExtensionsKt.asJsoup$default(r2, r0, r1, r0)
            java.lang.String r0 = r5.chapterListSelector()
            org.jsoup.select.Elements r3 = r6.select(r0)
            r2.close()
        L_0x00a3:
            java.lang.String r6 = "chapterElements"
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r3, r6)
            java.lang.Iterable r3 = (java.lang.Iterable) r3
            java.util.ArrayList r6 = new java.util.ArrayList
            r0 = 10
            int r0 = kotlin.collections.CollectionsKt.collectionSizeOrDefault(r3, r0)
            r6.<init>(r0)
            java.util.Collection r6 = (java.util.Collection) r6
            java.util.Iterator r0 = r3.iterator()
        L_0x00bb:
            boolean r1 = r0.hasNext()
            if (r1 == 0) goto L_0x00cf
            java.lang.Object r1 = r0.next()
            org.jsoup.nodes.Element r1 = (org.jsoup.nodes.Element) r1
            eu.kanade.tachiyomi.source.model.SChapter r1 = r5.chapterFromElement(r1)
            r6.add(r1)
            goto L_0x00bb
        L_0x00cf:
            java.util.List r6 = (java.util.List) r6
            return r6
        */
        throw new UnsupportedOperationException("Method not decompiled: eu.kanade.tachiyomi.multisrc.madara.Madara.chapterListParse(okhttp3.Response):java.util.List");
    }

    /* access modifiers changed from: protected */
    public String chapterListSelector() {
        return "li.wp-manga-chapter";
    }

    /* access modifiers changed from: protected */
    public String chapterDateSelector() {
        return "span.chapter-release-date";
    }

    public String getChapterUrlSelector() {
        return this.chapterUrlSelector;
    }

    public String getChapterUrlSuffix() {
        return this.chapterUrlSuffix;
    }

    /* access modifiers changed from: protected */
    public SChapter chapterFromElement(Element element) {
        long j;
        String attr;
        String attr2;
        Intrinsics.checkNotNullParameter(element, "element");
        SChapter create = SChapter.Companion.create();
        Element selectFirst = element.selectFirst(getChapterUrlSelector());
        Intrinsics.checkNotNull(selectFirst);
        String attr3 = selectFirst.attr("abs:href");
        StringBuilder sb = new StringBuilder();
        Intrinsics.checkNotNullExpressionValue(attr3, "it");
        String str = null;
        sb.append(StringsKt.substringBefore$default(attr3, "?style=paged", (String) null, 2, (Object) null));
        sb.append(!StringsKt.endsWith$default(attr3, getChapterUrlSuffix(), false, 2, (Object) null) ? getChapterUrlSuffix() : "");
        create.setUrl(sb.toString());
        String text = selectFirst.text();
        Intrinsics.checkNotNullExpressionValue(text, "urlElement.text()");
        create.setName(text);
        Element selectFirst2 = element.selectFirst("img:not(.thumb)");
        if (selectFirst2 == null || (attr2 = selectFirst2.attr("alt")) == null) {
            Element selectFirst3 = element.selectFirst("span a");
            if (selectFirst3 == null || (attr = selectFirst3.attr("title")) == null) {
                Element selectFirst4 = element.selectFirst(chapterDateSelector());
                if (selectFirst4 != null) {
                    str = selectFirst4.text();
                }
                j = parseChapterDate(str);
            } else {
                Intrinsics.checkNotNullExpressionValue(attr, "attr(\"title\")");
                j = Long.valueOf(parseRelativeDate(attr)).longValue();
            }
        } else {
            Intrinsics.checkNotNullExpressionValue(attr2, "attr(\"alt\")");
            j = Long.valueOf(parseRelativeDate(attr2)).longValue();
        }
        create.setDate_upload(j);
        return create;
    }

    private static final long parseChapterDate$tryParse(SimpleDateFormat simpleDateFormat, String str) {
        try {
            Date parse = simpleDateFormat.parse(str);
            if (parse != null) {
                return parse.getTime();
            }
            return 0;
        } catch (ParseException unused) {
            return 0;
        }
    }

    public long parseChapterDate(String str) {
        if (str == null) {
            return 0;
        }
        if (new WordSet("yesterday", "يوم واحد").startsWith(str)) {
            Calendar instance = Calendar.getInstance();
            instance.add(5, -1);
            instance.set(11, 0);
            instance.set(12, 0);
            instance.set(13, 0);
            instance.set(14, 0);
            return instance.getTimeInMillis();
        } else if (new WordSet("today").startsWith(str)) {
            Calendar instance2 = Calendar.getInstance();
            instance2.set(11, 0);
            instance2.set(12, 0);
            instance2.set(13, 0);
            instance2.set(14, 0);
            return instance2.getTimeInMillis();
        } else if (new WordSet("يومين").startsWith(str)) {
            Calendar instance3 = Calendar.getInstance();
            instance3.add(5, -2);
            instance3.set(11, 0);
            instance3.set(12, 0);
            instance3.set(13, 0);
            instance3.set(14, 0);
            return instance3.getTimeInMillis();
        } else if (new WordSet("ago", "atrás", "önce", "قبل").endsWith(str)) {
            return parseRelativeDate(str);
        } else {
            if (new WordSet("hace").startsWith(str)) {
                return parseRelativeDate(str);
            }
            CharSequence charSequence = str;
            if (new Regex("\\b\\d+ jour").containsMatchIn(charSequence)) {
                return parseRelativeDate(str);
            }
            if (!new Regex("\\d(st|nd|rd|th)").containsMatchIn(charSequence)) {
                return parseChapterDate$tryParse(this.dateFormat, str);
            }
            Iterable<String> split$default = StringsKt.split$default(charSequence, new String[]{" "}, false, 0, 6, (Object) null);
            Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(split$default, 10));
            for (String str2 : split$default) {
                CharSequence charSequence2 = str2;
                if (new Regex("\\d\\D\\D").containsMatchIn(charSequence2)) {
                    str2 = new Regex("\\D").replace(charSequence2, "");
                }
                arrayList.add(str2);
            }
            return parseChapterDate$tryParse(this.dateFormat, CollectionsKt.joinToString$default((List) arrayList, " ", (CharSequence) null, (CharSequence) null, 0, (CharSequence) null, (Function1) null, 62, (Object) null));
        }
    }

    /* access modifiers changed from: protected */
    public long parseRelativeDate(String str) {
        String value;
        Integer intOrNull;
        String str2 = str;
        Intrinsics.checkNotNullParameter(str2, "date");
        MatchResult find$default = Regex.find$default(new Regex("(\\d+)"), str2, 0, 2, (Object) null);
        if (find$default == null || (value = find$default.getValue()) == null || (intOrNull = StringsKt.toIntOrNull(value)) == null) {
            return 0;
        }
        int intValue = intOrNull.intValue();
        Calendar instance = Calendar.getInstance();
        if (new WordSet("hari", "gün", "jour", "día", "dia", "day", "วัน", "ngày", "giorni", "أيام", "天").anyWordIn(str2)) {
            instance.add(5, -intValue);
            return instance.getTimeInMillis();
        } else if (new WordSet("jam", "saat", "heure", "hora", "hour", "ชั่วโมง", "giờ", "ore", "ساعة", "小时").anyWordIn(str2)) {
            instance.add(10, -intValue);
            return instance.getTimeInMillis();
        } else if (new WordSet("menit", "dakika", "min", "minute", "minuto", "นาที", "دقائق").anyWordIn(str2)) {
            instance.add(12, -intValue);
            return instance.getTimeInMillis();
        } else if (new WordSet("detik", "segundo", "second", "วินาที").anyWordIn(str2)) {
            instance.add(13, -intValue);
            return instance.getTimeInMillis();
        } else if (new WordSet("week", "semana").anyWordIn(str2)) {
            instance.add(5, (-intValue) * 7);
            return instance.getTimeInMillis();
        } else if (new WordSet("month", "mes").anyWordIn(str2)) {
            instance.add(2, -intValue);
            return instance.getTimeInMillis();
        } else if (!new WordSet("year", "año").anyWordIn(str2)) {
            return 0;
        } else {
            instance.add(1, -intValue);
            return instance.getTimeInMillis();
        }
    }

    /* access modifiers changed from: protected */
    public Request pageListRequest(SChapter sChapter) {
        Intrinsics.checkNotNullParameter(sChapter, "chapter");
        if (StringsKt.startsWith$default(sChapter.getUrl(), "http", false, 2, (Object) null)) {
            return RequestsKt.GET$default(sChapter.getUrl(), getHeaders(), (CacheControl) null, 4, (Object) null);
        }
        return Madara.super.pageListRequest(sChapter);
    }

    public String getPageListParseSelector() {
        return this.pageListParseSelector;
    }

    public String getChapterProtectorSelector() {
        return this.chapterProtectorSelector;
    }

    /* access modifiers changed from: protected */
    public List<Page> pageListParse(Document document) {
        String str;
        String substringAfter$default;
        String str2;
        Document document2 = document;
        Intrinsics.checkNotNullParameter(document2, "document");
        launchIO(new Madara$pageListParse$1(this, document2));
        Element selectFirst = document2.selectFirst(getChapterProtectorSelector());
        if (selectFirst == null) {
            Iterable select = document2.select(getPageListParseSelector());
            Intrinsics.checkNotNullExpressionValue(select, "document.select(pageListParseSelector)");
            Iterable iterable = select;
            Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterable, 10));
            int i = 0;
            for (Object next : iterable) {
                int i2 = i + 1;
                if (i < 0) {
                    CollectionsKt.throwIndexOverflow();
                }
                Element selectFirst2 = ((Element) next).selectFirst("img");
                if (selectFirst2 != null) {
                    Intrinsics.checkNotNullExpressionValue(selectFirst2, "it");
                    str2 = imageFromElement(selectFirst2);
                } else {
                    str2 = null;
                }
                String location = document.location();
                Intrinsics.checkNotNullExpressionValue(location, "document.location()");
                arrayList.add(new Page(i, location, str2, (Uri) null, 8, (DefaultConstructorMarker) null));
                i = i2;
            }
            return (List) arrayList;
        }
        String attr = selectFirst.attr("src");
        Intrinsics.checkNotNullExpressionValue(attr, "it");
        if (!StringsKt.startsWith$default(attr, "data:text/javascript;base64,", false, 2, (Object) null)) {
            attr = null;
        }
        if (attr == null || (substringAfter$default = StringsKt.substringAfter$default(attr, "data:text/javascript;base64,", (String) null, 2, (Object) null)) == null) {
            str = selectFirst.html();
        } else {
            byte[] decode = Base64.decode(substringAfter$default, 0);
            Intrinsics.checkNotNullExpressionValue(decode, "decode(it, Base64.DEFAULT)");
            str = new String(decode, Charsets.UTF_8);
        }
        Intrinsics.checkNotNullExpressionValue(str, "chapterProtectorHtml");
        String substringBefore$default = StringsKt.substringBefore$default(StringsKt.substringAfter$default(str, "wpmangaprotectornonce='", (String) null, 2, (Object) null), "';", (String) null, 2, (Object) null);
        JsonObject jsonObject = JsonElementKt.getJsonObject(getJson().parseToJsonElement(StringsKt.replace$default(StringsKt.substringBefore$default(StringsKt.substringAfter$default(str, "chapter_data='", (String) null, 2, (Object) null), "';", (String) null, 2, (Object) null), "\\/", "/", false, 4, (Object) null)));
        Object obj = jsonObject.get("ct");
        Intrinsics.checkNotNull(obj);
        byte[] decode2 = Base64.decode(JsonElementKt.getJsonPrimitive((JsonElement) obj).getContent(), 0);
        Object obj2 = jsonObject.get("s");
        Intrinsics.checkNotNull(obj2);
        byte[] plus = ArraysKt.plus(this.salted, decodeHex(JsonElementKt.getJsonPrimitive((JsonElement) obj2).getContent()));
        Intrinsics.checkNotNullExpressionValue(decode2, "unsaltedCiphertext");
        byte[] plus2 = ArraysKt.plus(plus, decode2);
        CryptoAES cryptoAES = CryptoAES.INSTANCE;
        String encodeToString = Base64.encodeToString(plus2, 0);
        Intrinsics.checkNotNullExpressionValue(encodeToString, "encodeToString(ciphertext, Base64.DEFAULT)");
        Iterable jsonArray = JsonElementKt.getJsonArray(getJson().parseToJsonElement(JsonElementKt.getJsonPrimitive(getJson().parseToJsonElement(cryptoAES.decrypt(encodeToString, substringBefore$default))).getContent()));
        Collection arrayList2 = new ArrayList(CollectionsKt.collectionSizeOrDefault(jsonArray, 10));
        int i3 = 0;
        for (Object next2 : jsonArray) {
            int i4 = i3 + 1;
            if (i3 < 0) {
                CollectionsKt.throwIndexOverflow();
            }
            String location2 = document.location();
            Intrinsics.checkNotNullExpressionValue(location2, "document.location()");
            arrayList2.add(new Page(i3, location2, JsonElementKt.getJsonPrimitive((JsonElement) next2).getContent(), (Uri) null, 8, (DefaultConstructorMarker) null));
            i3 = i4;
        }
        return (List) arrayList2;
    }

    /* access modifiers changed from: protected */
    public Request imageRequest(Page page) {
        Intrinsics.checkNotNullParameter(page, "page");
        String imageUrl = page.getImageUrl();
        Intrinsics.checkNotNull(imageUrl);
        return RequestsKt.GET$default(imageUrl, getHeaders().newBuilder().set("Referer", page.getUrl()).build(), (CacheControl) null, 4, (Object) null);
    }

    /* access modifiers changed from: protected */
    public String imageUrlParse(Document document) {
        Intrinsics.checkNotNullParameter(document, "document");
        return "";
    }

    /* access modifiers changed from: protected */
    public boolean getSendViewCount() {
        return this.sendViewCount;
    }

    /* access modifiers changed from: protected */
    public Request countViewsRequest(Document document) {
        JsonPrimitive jsonPrimitive;
        Intrinsics.checkNotNullParameter(document, "document");
        Element selectFirst = document.selectFirst("script#wp-manga-js-extra");
        String data = selectFirst != null ? selectFirst.data() : null;
        if (data == null) {
            return null;
        }
        JsonObject jsonObject = JsonElementKt.getJsonObject(getJson().parseToJsonElement(StringsKt.substringBeforeLast$default(StringsKt.substringAfter$default(data, "var manga = ", (String) null, 2, (Object) null), ";", (String) null, 2, (Object) null)));
        JsonElement jsonElement = (JsonElement) jsonObject.get("enable_manga_view");
        if (!Intrinsics.areEqual((jsonElement == null || (jsonPrimitive = JsonElementKt.getJsonPrimitive(jsonElement)) == null) ? null : jsonPrimitive.getContent(), "1")) {
            return null;
        }
        FormBody.Builder add = new FormBody.Builder((Charset) null, 1, (DefaultConstructorMarker) null).add("action", "manga_views");
        Object obj = jsonObject.get("manga_id");
        Intrinsics.checkNotNull(obj);
        FormBody.Builder add2 = add.add("manga", JsonElementKt.getJsonPrimitive((JsonElement) obj).getContent());
        if (jsonObject.get("chapter_slug") != null) {
            Object obj2 = jsonObject.get("chapter_slug");
            Intrinsics.checkNotNull(obj2);
            add2.add("chapter", JsonElementKt.getJsonPrimitive((JsonElement) obj2).getContent());
        }
        RequestBody build = add2.build();
        Headers.Builder headersBuilder = headersBuilder();
        String location = document.location();
        Intrinsics.checkNotNullExpressionValue(location, "document.location()");
        Headers build2 = headersBuilder.set("Referer", location).build();
        return RequestsKt.POST$default(getBaseUrl() + "/wp-admin/admin-ajax.php", build2, build, (CacheControl) null, 8, (Object) null);
    }

    /* access modifiers changed from: protected */
    public final void countViews(Document document) {
        Intrinsics.checkNotNullParameter(document, "document");
        if (getSendViewCount()) {
            try {
                Request countViewsRequest = countViewsRequest(document);
                if (countViewsRequest != null) {
                    getClient().newCall(countViewsRequest).execute().close();
                }
            } catch (Exception unused) {
            }
        }
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0045, code lost:
        r3 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:?, code lost:
        kotlin.io.CloseableKt.closeFinally(r1, r2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x0049, code lost:
        throw r3;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void fetchGenres() {
        /*
            r4 = this;
            boolean r0 = r4.getFetchGenres()
            if (r0 == 0) goto L_0x0056
            int r0 = r4.fetchGenresAttempts
            r1 = 3
            if (r0 >= r1) goto L_0x0056
            boolean r0 = r4.genresFetched
            if (r0 != 0) goto L_0x0056
            r0 = 1
            okhttp3.OkHttpClient r1 = r4.getClient()     // Catch:{ Exception -> 0x0051, all -> 0x004a }
            okhttp3.Request r2 = r4.genresRequest()     // Catch:{ Exception -> 0x0051, all -> 0x004a }
            okhttp3.Call r1 = r1.newCall(r2)     // Catch:{ Exception -> 0x0051, all -> 0x004a }
            okhttp3.Response r1 = r1.execute()     // Catch:{ Exception -> 0x0051, all -> 0x004a }
            java.io.Closeable r1 = (java.io.Closeable) r1     // Catch:{ Exception -> 0x0051, all -> 0x004a }
            r2 = r1
            okhttp3.Response r2 = (okhttp3.Response) r2     // Catch:{ all -> 0x0043 }
            r3 = 0
            org.jsoup.nodes.Document r2 = eu.kanade.tachiyomi.util.JsoupExtensionsKt.asJsoup$default(r2, r3, r0, r3)     // Catch:{ all -> 0x0043 }
            java.util.List r2 = r4.parseGenres(r2)     // Catch:{ all -> 0x0043 }
            kotlin.io.CloseableKt.closeFinally(r1, r3)     // Catch:{ Exception -> 0x0051, all -> 0x004a }
            r4.genresFetched = r0     // Catch:{ Exception -> 0x0051, all -> 0x004a }
            r1 = r2
            java.util.Collection r1 = (java.util.Collection) r1     // Catch:{ Exception -> 0x0051, all -> 0x004a }
            boolean r1 = r1.isEmpty()     // Catch:{ Exception -> 0x0051, all -> 0x004a }
            if (r1 != 0) goto L_0x003d
            r3 = r2
        L_0x003d:
            if (r3 == 0) goto L_0x0051
            r4.setGenresList(r3)     // Catch:{ Exception -> 0x0051, all -> 0x004a }
            goto L_0x0051
        L_0x0043:
            r2 = move-exception
            throw r2     // Catch:{ all -> 0x0045 }
        L_0x0045:
            r3 = move-exception
            kotlin.io.CloseableKt.closeFinally(r1, r2)     // Catch:{ Exception -> 0x0051, all -> 0x004a }
            throw r3     // Catch:{ Exception -> 0x0051, all -> 0x004a }
        L_0x004a:
            r1 = move-exception
            int r2 = r4.fetchGenresAttempts
            int r2 = r2 + r0
            r4.fetchGenresAttempts = r2
            throw r1
        L_0x0051:
            int r1 = r4.fetchGenresAttempts
            int r1 = r1 + r0
            r4.fetchGenresAttempts = r1
        L_0x0056:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: eu.kanade.tachiyomi.multisrc.madara.Madara.fetchGenres():void");
    }

    /* access modifiers changed from: protected */
    public Request genresRequest() {
        return RequestsKt.GET$default(getBaseUrl() + "/?s=genre&post_type=wp-manga", getHeaders(), (CacheControl) null, 4, (Object) null);
    }

    /* access modifiers changed from: protected */
    public List<Genre> parseGenres(Document document) {
        Intrinsics.checkNotNullParameter(document, "document");
        Element selectFirst = document.selectFirst("div.checkbox-group");
        List list = (List) (selectFirst != null ? selectFirst.select("div.checkbox") : null);
        if (list == null) {
            list = CollectionsKt.emptyList();
        }
        Iterable<Element> iterable = list;
        Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterable, 10));
        for (Element element : iterable) {
            Element selectFirst2 = element.selectFirst("label");
            Intrinsics.checkNotNull(selectFirst2);
            String text = selectFirst2.text();
            Intrinsics.checkNotNullExpressionValue(text, "li.selectFirst(\"label\")!!.text()");
            Element selectFirst3 = element.selectFirst("input[type=checkbox]");
            Intrinsics.checkNotNull(selectFirst3);
            String val = selectFirst3.val();
            Intrinsics.checkNotNullExpressionValue(val, "li.selectFirst(\"input[type=checkbox]\")!!.`val`()");
            arrayList.add(new Genre(text, val));
        }
        return (List) arrayList;
    }

    /* access modifiers changed from: protected */
    public final byte[] decodeHex(String str) {
        Intrinsics.checkNotNullParameter(str, "<this>");
        if (str.length() % 2 == 0) {
            Iterable<String> chunked = StringsKt.chunked(str, 2);
            Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(chunked, 10));
            for (String parseInt : chunked) {
                arrayList.add(Byte.valueOf((byte) Integer.parseInt(parseInt, CharsKt.checkRadix(16))));
            }
            return CollectionsKt.toByteArray((List) arrayList);
        }
        throw new IllegalStateException("Must have an even length".toString());
    }

    /* access modifiers changed from: protected */
    public final byte[] getSalted() {
        return this.salted;
    }

    /* access modifiers changed from: protected */
    public final Job launchIO(Function0<Unit> function0) {
        Intrinsics.checkNotNullParameter(function0, "block");
        return BuildersKt.launch$default(this.scope, (CoroutineContext) null, (CoroutineStart) null, new Madara$launchIO$1(function0, (Continuation<? super Madara$launchIO$1>) null), 3, (Object) null);
    }

    @Metadata(d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u0011\u0010\u0003\u001a\u00020\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006R\u000e\u0010\u0007\u001a\u00020\bXT¢\u0006\u0002\n\u0000¨\u0006\t"}, d2 = {"Leu/kanade/tachiyomi/multisrc/madara/Madara$Companion;", "", "()V", "URL_REGEX", "Lkotlin/text/Regex;", "getURL_REGEX", "()Lkotlin/text/Regex;", "URL_SEARCH_PREFIX", "", "madara_release"}, k = 1, mv = {1, 7, 1}, xi = 48)
    /* compiled from: Madara.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final Regex getURL_REGEX() {
            return Madara.URL_REGEX;
        }
    }

    private final boolean containsIn(String str, String[] strArr) {
        Collection arrayList = new ArrayList(strArr.length);
        for (String lowerCase : strArr) {
            String lowerCase2 = lowerCase.toLowerCase(Locale.ROOT);
            Intrinsics.checkNotNullExpressionValue(lowerCase2, "this as java.lang.String).toLowerCase(Locale.ROOT)");
            arrayList.add(lowerCase2);
        }
        String lowerCase3 = str.toLowerCase(Locale.ROOT);
        Intrinsics.checkNotNullExpressionValue(lowerCase3, "this as java.lang.String).toLowerCase(Locale.ROOT)");
        return ((List) arrayList).contains(lowerCase3);
    }
}

package eu.kanade.tachiyomi.extension.ar.mangalek;

import android.content.SharedPreferences;
import android.widget.Toast;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import eu.kanade.tachiyomi.multisrc.madara.Madara;
import eu.kanade.tachiyomi.network.RequestsKt;
import eu.kanade.tachiyomi.source.ConfigurableSource;
import eu.kanade.tachiyomi.source.model.FilterList;
import eu.kanade.tachiyomi.source.model.MangasPage;
import eu.kanade.tachiyomi.source.model.SManga;
import eu.kanade.tachiyomi.source.online.HttpSource;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import kotlin.Deprecated;
import kotlin.DeprecationLevel;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.Metadata;
import kotlin.ReplaceWith;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Reflection;
import kotlin.reflect.KType;
import kotlinx.serialization.DeserializationStrategy;
import kotlinx.serialization.KSerializer;
import kotlinx.serialization.Serializable;
import kotlinx.serialization.SerializersKt;
import kotlinx.serialization.StringFormat;
import kotlinx.serialization.descriptors.SerialDescriptor;
import kotlinx.serialization.encoding.CompositeEncoder;
import kotlinx.serialization.internal.ArrayListSerializer;
import kotlinx.serialization.internal.PluginExceptionsKt;
import kotlinx.serialization.internal.SerializationConstructorMarker;
import kotlinx.serialization.modules.SerializersModule;
import okhttp3.CacheControl;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.Response;

@Metadata(d1 = {"\u0000n\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0007\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\u0018\u00002\u00020\u00012\u00020\u0002:\u000212B\u0005¢\u0006\u0002\u0010\u0003J\u0012\u0010\u001c\u001a\u00020\u001d2\b\u0010\u001e\u001a\u0004\u0018\u00010\u0005H\u0016J\u0010\u0010\u001f\u001a\u00020 2\u0006\u0010!\u001a\u00020\"H\u0014J \u0010#\u001a\u00020$2\u0006\u0010%\u001a\u00020&2\u0006\u0010'\u001a\u00020\u00052\u0006\u0010(\u001a\u00020)H\u0014J\u0010\u0010*\u001a\u00020+2\u0006\u0010,\u001a\u00020-H\u0016J\u001a\u0010.\u001a\u0002H/\"\u0006\b\u0000\u0010/\u0018\u0001*\u00020\"H\b¢\u0006\u0002\u00100R\u001b\u0010\u0004\u001a\u00020\u00058VX\u0002¢\u0006\f\n\u0004\b\b\u0010\t\u001a\u0004\b\u0006\u0010\u0007R\u0014\u0010\n\u001a\u00020\u0005XD¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\u0007R\u0014\u0010\f\u001a\u00020\rXD¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u000e\u0010\u0010\u001a\u00020\u0011X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0011X\u0004¢\u0006\u0002\n\u0000R\u001b\u0010\u0013\u001a\u00020\u00148BX\u0002¢\u0006\f\n\u0004\b\u0017\u0010\t\u001a\u0004\b\u0015\u0010\u0016R\u0014\u0010\u0018\u001a\u00020\u0019X\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u001b¨\u00063"}, d2 = {"Leu/kanade/tachiyomi/extension/ar/mangalek/Mangalek;", "Leu/kanade/tachiyomi/multisrc/madara/Madara;", "Leu/kanade/tachiyomi/source/ConfigurableSource;", "()V", "baseUrl", "", "getBaseUrl", "()Ljava/lang/String;", "baseUrl$delegate", "Lkotlin/Lazy;", "chapterUrlSuffix", "getChapterUrlSuffix", "fetchGenres", "", "getFetchGenres", "()Z", "formatOne", "Ljava/text/SimpleDateFormat;", "formatTwo", "preferences", "Landroid/content/SharedPreferences;", "getPreferences", "()Landroid/content/SharedPreferences;", "preferences$delegate", "useLoadMoreRequest", "Leu/kanade/tachiyomi/multisrc/madara/Madara$LoadMoreStrategy;", "getUseLoadMoreRequest", "()Leu/kanade/tachiyomi/multisrc/madara/Madara$LoadMoreStrategy;", "parseChapterDate", "", "date", "searchMangaParse", "Leu/kanade/tachiyomi/source/model/MangasPage;", "response", "Lokhttp3/Response;", "searchMangaRequest", "Lokhttp3/Request;", "page", "", "query", "filters", "Leu/kanade/tachiyomi/source/model/FilterList;", "setupPreferenceScreen", "", "screen", "Landroidx/preference/PreferenceScreen;", "parseAs", "T", "(Lokhttp3/Response;)Ljava/lang/Object;", "SearchEntryDto", "SearchResponseDto", "tachiyomi-ar.mangalek-v1.4.49_release"}, k = 1, mv = {1, 7, 1}, xi = 48)
/* compiled from: Mangalek.kt */
public final class Mangalek extends Madara implements ConfigurableSource {
    private final Lazy baseUrl$delegate = LazyKt.lazy(new Mangalek$baseUrl$2(this));
    private final String chapterUrlSuffix = "";
    private final boolean fetchGenres;
    private final SimpleDateFormat formatOne = new SimpleDateFormat("MMMM dd, yyyy", new Locale("ar"));
    private final SimpleDateFormat formatTwo = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    private final Lazy preferences$delegate = LazyKt.lazy(new Mangalek$special$$inlined$getPreferencesLazy$default$1((HttpSource) this));
    private final Madara.LoadMoreStrategy useLoadMoreRequest = Madara.LoadMoreStrategy.Always;

    public Mangalek() {
        super("مانجا ليك", MangalekKt.MIRROR_PREF_DEFAULT_VALUE, "ar", new SimpleDateFormat("MMMM dd, yyyy", new Locale("ar")));
    }

    /* access modifiers changed from: protected */
    public boolean getFetchGenres() {
        return this.fetchGenres;
    }

    /* access modifiers changed from: protected */
    public Madara.LoadMoreStrategy getUseLoadMoreRequest() {
        return this.useLoadMoreRequest;
    }

    public String getChapterUrlSuffix() {
        return this.chapterUrlSuffix;
    }

    public String getBaseUrl() {
        return (String) this.baseUrl$delegate.getValue();
    }

    /* access modifiers changed from: private */
    public final SharedPreferences getPreferences() {
        return (SharedPreferences) this.preferences$delegate.getValue();
    }

    public void setupPreferenceScreen(PreferenceScreen preferenceScreen) {
        Intrinsics.checkNotNullParameter(preferenceScreen, "screen");
        Preference listPreference = new ListPreference(preferenceScreen.getContext());
        listPreference.setKey("MIRROR");
        listPreference.setTitle("تعديل رابط مانجا ليك");
        listPreference.setEntries((CharSequence[]) MangalekKt.getMIRROR_PREF_ENTRY_VALUES());
        listPreference.setEntryValues((CharSequence[]) MangalekKt.getMIRROR_PREF_ENTRY_VALUES());
        listPreference.setDefaultValue(MangalekKt.MIRROR_PREF_DEFAULT_VALUE);
        listPreference.setSummary("%s");
        listPreference.setOnPreferenceChangeListener(new Mangalek$$ExternalSyntheticLambda0(preferenceScreen));
        preferenceScreen.addPreference(listPreference);
    }

    /* access modifiers changed from: private */
    public static final boolean setupPreferenceScreen$lambda$1$lambda$0(PreferenceScreen preferenceScreen, Preference preference, Object obj) {
        Intrinsics.checkNotNullParameter(preferenceScreen, "$screen");
        Toast.makeText(preferenceScreen.getContext(), ".لتطبيق الإعدادات الجديدة Tachiyomi أعد تشغيل", 1).show();
        return true;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:6:?, code lost:
        r4 = r3.formatTwo.parse(r4);
        kotlin.jvm.internal.Intrinsics.checkNotNull(r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:?, code lost:
        return r4.getTime();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:?, code lost:
        return 0;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x0013 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public long parseChapterDate(java.lang.String r4) {
        /*
            r3 = this;
            r0 = 0
            if (r4 != 0) goto L_0x0005
            return r0
        L_0x0005:
            java.text.SimpleDateFormat r2 = r3.formatOne     // Catch:{ ParseException -> 0x0013 }
            java.util.Date r2 = r2.parse(r4)     // Catch:{ ParseException -> 0x0013 }
            kotlin.jvm.internal.Intrinsics.checkNotNull(r2)     // Catch:{ ParseException -> 0x0013 }
            long r0 = r2.getTime()     // Catch:{ ParseException -> 0x0013 }
            goto L_0x0020
        L_0x0013:
            java.text.SimpleDateFormat r2 = r3.formatTwo     // Catch:{ ParseException -> 0x0020 }
            java.util.Date r4 = r2.parse(r4)     // Catch:{ ParseException -> 0x0020 }
            kotlin.jvm.internal.Intrinsics.checkNotNull(r4)     // Catch:{ ParseException -> 0x0020 }
            long r0 = r4.getTime()     // Catch:{ ParseException -> 0x0020 }
        L_0x0020:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: eu.kanade.tachiyomi.extension.ar.mangalek.Mangalek.parseChapterDate(java.lang.String):long");
    }

    /* access modifiers changed from: protected */
    public Request searchMangaRequest(int i, String str, FilterList filterList) {
        Intrinsics.checkNotNullParameter(str, "query");
        Intrinsics.checkNotNullParameter(filterList, "filters");
        return RequestsKt.POST$default(getBaseUrl() + "/wp-admin/admin-ajax.php", getHeaders(), new FormBody.Builder((Charset) null, 1, (DefaultConstructorMarker) null).add("action", "wp-manga-search-manga").add("title", str).build(), (CacheControl) null, 8, (Object) null);
    }

    private final /* synthetic */ <T> T parseAs(Response response) {
        StringFormat json = getJson();
        String string = response.body().string();
        SerializersModule serializersModule = json.getSerializersModule();
        Intrinsics.reifiedOperationMarker(6, "T");
        DeserializationStrategy serializer = SerializersKt.serializer(serializersModule, (KType) null);
        Intrinsics.checkNotNull(serializer, "null cannot be cast to non-null type kotlinx.serialization.KSerializer<T of kotlinx.serialization.internal.Platform_commonKt.cast>");
        KSerializer kSerializer = (KSerializer) serializer;
        return json.decodeFromString(serializer, string);
    }

    @Metadata(d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\r\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\b\b\u0018\u0000 !2\u00020\u0001:\u0002 !B1\b\u0017\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u000e\u0010\u0004\u001a\n\u0012\u0004\u0012\u00020\u0006\u0018\u00010\u0005\u0012\u0006\u0010\u0007\u001a\u00020\b\u0012\b\u0010\t\u001a\u0004\u0018\u00010\n¢\u0006\u0002\u0010\u000bB\u001b\u0012\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005\u0012\u0006\u0010\u0007\u001a\u00020\b¢\u0006\u0002\u0010\fJ\u000f\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005HÆ\u0003J\t\u0010\u0012\u001a\u00020\bHÆ\u0003J#\u0010\u0013\u001a\u00020\u00002\u000e\b\u0002\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u00052\b\b\u0002\u0010\u0007\u001a\u00020\bHÆ\u0001J\u0013\u0010\u0014\u001a\u00020\b2\b\u0010\u0015\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0016\u001a\u00020\u0003HÖ\u0001J\t\u0010\u0017\u001a\u00020\u0018HÖ\u0001J!\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\u00002\u0006\u0010\u001c\u001a\u00020\u001d2\u0006\u0010\u001e\u001a\u00020\u001fHÇ\u0001R\u0017\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0011\u0010\u0007\u001a\u00020\b¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010¨\u0006\""}, d2 = {"Leu/kanade/tachiyomi/extension/ar/mangalek/Mangalek$SearchResponseDto;", "", "seen1", "", "data", "", "Leu/kanade/tachiyomi/extension/ar/mangalek/Mangalek$SearchEntryDto;", "success", "", "serializationConstructorMarker", "Lkotlinx/serialization/internal/SerializationConstructorMarker;", "(ILjava/util/List;ZLkotlinx/serialization/internal/SerializationConstructorMarker;)V", "(Ljava/util/List;Z)V", "getData", "()Ljava/util/List;", "getSuccess", "()Z", "component1", "component2", "copy", "equals", "other", "hashCode", "toString", "", "write$Self", "", "self", "output", "Lkotlinx/serialization/encoding/CompositeEncoder;", "serialDesc", "Lkotlinx/serialization/descriptors/SerialDescriptor;", "$serializer", "Companion", "tachiyomi-ar.mangalek-v1.4.49_release"}, k = 1, mv = {1, 7, 1}, xi = 48)
    @Serializable
    /* compiled from: Mangalek.kt */
    public static final class SearchResponseDto {
        public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
        private final List<SearchEntryDto> data;
        private final boolean success;

        public static /* synthetic */ SearchResponseDto copy$default(SearchResponseDto searchResponseDto, List<SearchEntryDto> list, boolean z, int i, Object obj) {
            if ((i & 1) != 0) {
                list = searchResponseDto.data;
            }
            if ((i & 2) != 0) {
                z = searchResponseDto.success;
            }
            return searchResponseDto.copy(list, z);
        }

        public final List<SearchEntryDto> component1() {
            return this.data;
        }

        public final boolean component2() {
            return this.success;
        }

        public final SearchResponseDto copy(List<SearchEntryDto> list, boolean z) {
            Intrinsics.checkNotNullParameter(list, "data");
            return new SearchResponseDto(list, z);
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof SearchResponseDto)) {
                return false;
            }
            SearchResponseDto searchResponseDto = (SearchResponseDto) obj;
            return Intrinsics.areEqual(this.data, searchResponseDto.data) && this.success == searchResponseDto.success;
        }

        public int hashCode() {
            int hashCode = this.data.hashCode() * 31;
            boolean z = this.success;
            if (z) {
                z = true;
            }
            return hashCode + (z ? 1 : 0);
        }

        public String toString() {
            return "SearchResponseDto(data=" + this.data + ", success=" + this.success + ')';
        }

        @Metadata(d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u000f\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004HÆ\u0001¨\u0006\u0006"}, d2 = {"Leu/kanade/tachiyomi/extension/ar/mangalek/Mangalek$SearchResponseDto$Companion;", "", "()V", "serializer", "Lkotlinx/serialization/KSerializer;", "Leu/kanade/tachiyomi/extension/ar/mangalek/Mangalek$SearchResponseDto;", "tachiyomi-ar.mangalek-v1.4.49_release"}, k = 1, mv = {1, 7, 1}, xi = 48)
        /* compiled from: Mangalek.kt */
        public static final class Companion {
            public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
                this();
            }

            private Companion() {
            }

            public final KSerializer<SearchResponseDto> serializer() {
                return (KSerializer) Mangalek$SearchResponseDto$$serializer.INSTANCE;
            }
        }

        @Deprecated(level = DeprecationLevel.HIDDEN, message = "This synthesized declaration should not be used directly", replaceWith = @ReplaceWith(expression = "", imports = {}))
        public /* synthetic */ SearchResponseDto(int i, List list, boolean z, SerializationConstructorMarker serializationConstructorMarker) {
            if (3 != (i & 3)) {
                PluginExceptionsKt.throwMissingFieldException(i, 3, Mangalek$SearchResponseDto$$serializer.INSTANCE.getDescriptor());
            }
            this.data = list;
            this.success = z;
        }

        public SearchResponseDto(List<SearchEntryDto> list, boolean z) {
            Intrinsics.checkNotNullParameter(list, "data");
            this.data = list;
            this.success = z;
        }

        @JvmStatic
        public static final void write$Self(SearchResponseDto searchResponseDto, CompositeEncoder compositeEncoder, SerialDescriptor serialDescriptor) {
            Intrinsics.checkNotNullParameter(searchResponseDto, "self");
            Intrinsics.checkNotNullParameter(compositeEncoder, "output");
            Intrinsics.checkNotNullParameter(serialDescriptor, "serialDesc");
            compositeEncoder.encodeSerializableElement(serialDescriptor, 0, new ArrayListSerializer(Mangalek$SearchEntryDto$$serializer.INSTANCE), searchResponseDto.data);
            compositeEncoder.encodeBooleanElement(serialDescriptor, 1, searchResponseDto.success);
        }

        public final List<SearchEntryDto> getData() {
            return this.data;
        }

        public final boolean getSuccess() {
            return this.success;
        }
    }

    @Metadata(d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\b\b\u0018\u0000 \u001e2\u00020\u0001:\u0002\u001d\u001eB-\b\u0017\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\b\u0010\u0006\u001a\u0004\u0018\u00010\u0005\u0012\b\u0010\u0007\u001a\u0004\u0018\u00010\b¢\u0006\u0002\u0010\tB\u0019\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0005¢\u0006\u0002\u0010\nJ\t\u0010\u000e\u001a\u00020\u0005HÆ\u0003J\t\u0010\u000f\u001a\u00020\u0005HÆ\u0003J\u001d\u0010\u0010\u001a\u00020\u00002\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u0005HÆ\u0001J\u0013\u0010\u0011\u001a\u00020\u00122\b\u0010\u0013\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0014\u001a\u00020\u0003HÖ\u0001J\t\u0010\u0015\u001a\u00020\u0005HÖ\u0001J!\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u00002\u0006\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\u001cHÇ\u0001R\u0011\u0010\u0006\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\f¨\u0006\u001f"}, d2 = {"Leu/kanade/tachiyomi/extension/ar/mangalek/Mangalek$SearchEntryDto;", "", "seen1", "", "url", "", "title", "serializationConstructorMarker", "Lkotlinx/serialization/internal/SerializationConstructorMarker;", "(ILjava/lang/String;Ljava/lang/String;Lkotlinx/serialization/internal/SerializationConstructorMarker;)V", "(Ljava/lang/String;Ljava/lang/String;)V", "getTitle", "()Ljava/lang/String;", "getUrl", "component1", "component2", "copy", "equals", "", "other", "hashCode", "toString", "write$Self", "", "self", "output", "Lkotlinx/serialization/encoding/CompositeEncoder;", "serialDesc", "Lkotlinx/serialization/descriptors/SerialDescriptor;", "$serializer", "Companion", "tachiyomi-ar.mangalek-v1.4.49_release"}, k = 1, mv = {1, 7, 1}, xi = 48)
    @Serializable
    /* compiled from: Mangalek.kt */
    public static final class SearchEntryDto {
        public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
        private final String title;
        private final String url;

        public SearchEntryDto() {
            this((String) null, (String) null, 3, (DefaultConstructorMarker) null);
        }

        public static /* synthetic */ SearchEntryDto copy$default(SearchEntryDto searchEntryDto, String str, String str2, int i, Object obj) {
            if ((i & 1) != 0) {
                str = searchEntryDto.url;
            }
            if ((i & 2) != 0) {
                str2 = searchEntryDto.title;
            }
            return searchEntryDto.copy(str, str2);
        }

        public final String component1() {
            return this.url;
        }

        public final String component2() {
            return this.title;
        }

        public final SearchEntryDto copy(String str, String str2) {
            Intrinsics.checkNotNullParameter(str, "url");
            Intrinsics.checkNotNullParameter(str2, "title");
            return new SearchEntryDto(str, str2);
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof SearchEntryDto)) {
                return false;
            }
            SearchEntryDto searchEntryDto = (SearchEntryDto) obj;
            return Intrinsics.areEqual(this.url, searchEntryDto.url) && Intrinsics.areEqual(this.title, searchEntryDto.title);
        }

        public int hashCode() {
            return (this.url.hashCode() * 31) + this.title.hashCode();
        }

        public String toString() {
            return "SearchEntryDto(url=" + this.url + ", title=" + this.title + ')';
        }

        @Metadata(d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u000f\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004HÆ\u0001¨\u0006\u0006"}, d2 = {"Leu/kanade/tachiyomi/extension/ar/mangalek/Mangalek$SearchEntryDto$Companion;", "", "()V", "serializer", "Lkotlinx/serialization/KSerializer;", "Leu/kanade/tachiyomi/extension/ar/mangalek/Mangalek$SearchEntryDto;", "tachiyomi-ar.mangalek-v1.4.49_release"}, k = 1, mv = {1, 7, 1}, xi = 48)
        /* compiled from: Mangalek.kt */
        public static final class Companion {
            public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
                this();
            }

            private Companion() {
            }

            public final KSerializer<SearchEntryDto> serializer() {
                return (KSerializer) Mangalek$SearchEntryDto$$serializer.INSTANCE;
            }
        }

        @Deprecated(level = DeprecationLevel.HIDDEN, message = "This synthesized declaration should not be used directly", replaceWith = @ReplaceWith(expression = "", imports = {}))
        public /* synthetic */ SearchEntryDto(int i, String str, String str2, SerializationConstructorMarker serializationConstructorMarker) {
            if ((i & 1) == 0) {
                this.url = "";
            } else {
                this.url = str;
            }
            if ((i & 2) == 0) {
                this.title = "";
            } else {
                this.title = str2;
            }
        }

        public SearchEntryDto(String str, String str2) {
            Intrinsics.checkNotNullParameter(str, "url");
            Intrinsics.checkNotNullParameter(str2, "title");
            this.url = str;
            this.title = str2;
        }

        @JvmStatic
        public static final void write$Self(SearchEntryDto searchEntryDto, CompositeEncoder compositeEncoder, SerialDescriptor serialDescriptor) {
            Intrinsics.checkNotNullParameter(searchEntryDto, "self");
            Intrinsics.checkNotNullParameter(compositeEncoder, "output");
            Intrinsics.checkNotNullParameter(serialDescriptor, "serialDesc");
            if (compositeEncoder.shouldEncodeElementDefault(serialDescriptor, 0) || !Intrinsics.areEqual(searchEntryDto.url, "")) {
                compositeEncoder.encodeStringElement(serialDescriptor, 0, searchEntryDto.url);
            }
            if (compositeEncoder.shouldEncodeElementDefault(serialDescriptor, 1) || !Intrinsics.areEqual(searchEntryDto.title, "")) {
                compositeEncoder.encodeStringElement(serialDescriptor, 1, searchEntryDto.title);
            }
        }

        /* JADX INFO: this call moved to the top of the method (can break code semantics) */
        public /* synthetic */ SearchEntryDto(String str, String str2, int i, DefaultConstructorMarker defaultConstructorMarker) {
            this((i & 1) != 0 ? "" : str, (i & 2) != 0 ? "" : str2);
        }

        public final String getUrl() {
            return this.url;
        }

        public final String getTitle() {
            return this.title;
        }
    }

    /* access modifiers changed from: protected */
    public MangasPage searchMangaParse(Response response) {
        Intrinsics.checkNotNullParameter(response, "response");
        StringFormat json = getJson();
        String string = response.body().string();
        DeserializationStrategy serializer = SerializersKt.serializer(json.getSerializersModule(), Reflection.typeOf(SearchResponseDto.class));
        Intrinsics.checkNotNull(serializer, "null cannot be cast to non-null type kotlinx.serialization.KSerializer<T of kotlinx.serialization.internal.Platform_commonKt.cast>");
        SearchResponseDto searchResponseDto = (SearchResponseDto) json.decodeFromString(serializer, string);
        if (!searchResponseDto.getSuccess()) {
            return new MangasPage(CollectionsKt.emptyList(), false);
        }
        Iterable<SearchEntryDto> data = searchResponseDto.getData();
        Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(data, 10));
        for (SearchEntryDto searchEntryDto : data) {
            SManga create = SManga.Companion.create();
            setUrlWithoutDomain(create, searchEntryDto.getUrl());
            create.setTitle(searchEntryDto.getTitle());
            arrayList.add(create);
        }
        return new MangasPage((List) arrayList, false);
    }
}

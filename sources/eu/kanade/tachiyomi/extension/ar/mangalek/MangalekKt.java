package eu.kanade.tachiyomi.extension.ar.mangalek;

import kotlin.Metadata;

@Metadata(d1 = {"\u0000\u0010\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0011\n\u0002\b\u0007\"\u000e\u0010\u0000\u001a\u00020\u0001X\u0004¢\u0006\u0002\n\u0000\"\u001c\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00010\u0003X\u0004¢\u0006\n\n\u0002\u0010\u0006\u001a\u0004\b\u0004\u0010\u0005\"\u000e\u0010\u0007\u001a\u00020\u0001XT¢\u0006\u0002\n\u0000\"\u000e\u0010\b\u001a\u00020\u0001XT¢\u0006\u0002\n\u0000\"\u000e\u0010\t\u001a\u00020\u0001XT¢\u0006\u0002\n\u0000¨\u0006\n"}, d2 = {"MIRROR_PREF_DEFAULT_VALUE", "", "MIRROR_PREF_ENTRY_VALUES", "", "getMIRROR_PREF_ENTRY_VALUES", "()[Ljava/lang/String;", "[Ljava/lang/String;", "MIRROR_PREF_KEY", "MIRROR_PREF_TITLE", "RESTART_TACHIYOMI", "tachiyomi-ar.mangalek-v1.4.49_release"}, k = 2, mv = {1, 7, 1}, xi = 48)
/* compiled from: Mangalek.kt */
public final class MangalekKt {
    /* access modifiers changed from: private */
    public static final String MIRROR_PREF_DEFAULT_VALUE;
    private static final String[] MIRROR_PREF_ENTRY_VALUES;
    private static final String MIRROR_PREF_KEY = "MIRROR";
    private static final String MIRROR_PREF_TITLE = "تعديل رابط مانجا ليك";
    private static final String RESTART_TACHIYOMI = ".لتطبيق الإعدادات الجديدة Tachiyomi أعد تشغيل";

    public static final String[] getMIRROR_PREF_ENTRY_VALUES() {
        return MIRROR_PREF_ENTRY_VALUES;
    }

    static {
        String[] strArr = {"https://lekmanga.net", "https://lekmanga.org", "https://like-manga.net", "https://lekmanga.com", "https://manga-leko.org"};
        MIRROR_PREF_ENTRY_VALUES = strArr;
        MIRROR_PREF_DEFAULT_VALUE = strArr[0];
    }
}

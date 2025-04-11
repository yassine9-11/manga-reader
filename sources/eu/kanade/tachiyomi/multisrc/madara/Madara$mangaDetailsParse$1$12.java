package eu.kanade.tachiyomi.multisrc.madara;

import java.util.Locale;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import kotlin.text.CharsKt;

@Metadata(d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\r\n\u0000\n\u0002\u0010\u000e\n\u0000\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\n¢\u0006\u0002\b\u0004"}, d2 = {"<anonymous>", "", "genre", "", "invoke"}, k = 3, mv = {1, 7, 1}, xi = 48)
/* compiled from: Madara.kt */
final class Madara$mangaDetailsParse$1$12 extends Lambda implements Function1<String, CharSequence> {
    public static final Madara$mangaDetailsParse$1$12 INSTANCE = new Madara$mangaDetailsParse$1$12();

    Madara$mangaDetailsParse$1$12() {
        super(1);
    }

    public final CharSequence invoke(String str) {
        String str2;
        Intrinsics.checkNotNullParameter(str, "genre");
        if (str.length() > 0) {
            StringBuilder sb = new StringBuilder();
            char charAt = str.charAt(0);
            if (Character.isLowerCase(charAt)) {
                Locale locale = Locale.ROOT;
                Intrinsics.checkNotNullExpressionValue(locale, "ROOT");
                str2 = CharsKt.titlecase(charAt, locale);
            } else {
                str2 = String.valueOf(charAt);
            }
            sb.append(str2);
            String substring = str.substring(1);
            Intrinsics.checkNotNullExpressionValue(substring, "this as java.lang.String).substring(startIndex)");
            sb.append(substring);
            str = sb.toString();
        }
        return str;
    }
}

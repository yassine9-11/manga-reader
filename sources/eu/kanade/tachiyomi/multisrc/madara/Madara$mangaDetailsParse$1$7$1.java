package eu.kanade.tachiyomi.multisrc.madara;

import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import kotlin.text.StringsKt;
import org.jsoup.nodes.Element;

@Metadata(d1 = {"\u0000\u0010\n\u0000\n\u0002\u0010\r\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u00012\u000e\u0010\u0002\u001a\n \u0004*\u0004\u0018\u00010\u00030\u0003H\n¢\u0006\u0002\b\u0005"}, d2 = {"<anonymous>", "", "p", "Lorg/jsoup/nodes/Element;", "kotlin.jvm.PlatformType", "invoke"}, k = 3, mv = {1, 7, 1}, xi = 48)
/* compiled from: Madara.kt */
final class Madara$mangaDetailsParse$1$7$1 extends Lambda implements Function1<Element, CharSequence> {
    public static final Madara$mangaDetailsParse$1$7$1 INSTANCE = new Madara$mangaDetailsParse$1$7$1();

    Madara$mangaDetailsParse$1$7$1() {
        super(1);
    }

    public final CharSequence invoke(Element element) {
        String text = element.text();
        Intrinsics.checkNotNullExpressionValue(text, "p.text()");
        return StringsKt.replace$default(text, "<br>", "\n", false, 4, (Object) null);
    }
}

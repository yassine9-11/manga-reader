package eu.kanade.tachiyomi.multisrc.madara;

import kotlin.Metadata;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;
import okhttp3.Headers;

@Metadata(d1 = {"\u0000\b\n\u0000\n\u0002\u0018\u0002\n\u0000\u0010\u0000\u001a\u00020\u0001H\n¢\u0006\u0002\b\u0002"}, d2 = {"<anonymous>", "Lokhttp3/Headers;", "invoke"}, k = 3, mv = {1, 7, 1}, xi = 48)
/* compiled from: Madara.kt */
final class Madara$xhrHeaders$2 extends Lambda implements Function0<Headers> {
    final /* synthetic */ Madara this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    Madara$xhrHeaders$2(Madara madara) {
        super(0);
        this.this$0 = madara;
    }

    public final Headers invoke() {
        return this.this$0.headersBuilder().set("X-Requested-With", "XMLHttpRequest").build();
    }
}

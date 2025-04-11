package eu.kanade.tachiyomi.multisrc.madara;

import eu.kanade.tachiyomi.source.model.MangasPage;
import eu.kanade.tachiyomi.source.model.SManga;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import okhttp3.HttpUrl;
import okhttp3.Response;

@Metadata(d1 = {"\u0000\u0010\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\u0010\u0000\u001a\n \u0002*\u0004\u0018\u00010\u00010\u00012\u000e\u0010\u0003\u001a\n \u0002*\u0004\u0018\u00010\u00040\u0004H\n¢\u0006\u0002\b\u0005"}, d2 = {"<anonymous>", "Leu/kanade/tachiyomi/source/model/MangasPage;", "kotlin.jvm.PlatformType", "response", "Lokhttp3/Response;", "invoke"}, k = 3, mv = {1, 7, 1}, xi = 48)
/* compiled from: Madara.kt */
final class Madara$fetchSearchManga$1 extends Lambda implements Function1<Response, MangasPage> {
    final /* synthetic */ HttpUrl $mangaUrl;
    final /* synthetic */ Madara this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    Madara$fetchSearchManga$1(Madara madara, HttpUrl httpUrl) {
        super(1);
        this.this$0 = madara;
        this.$mangaUrl = httpUrl;
    }

    public final MangasPage invoke(Response response) {
        Madara madara = this.this$0;
        Intrinsics.checkNotNullExpressionValue(response, "response");
        SManga access$mangaDetailsParse = madara.mangaDetailsParse(response);
        this.this$0.setUrlWithoutDomain(access$mangaDetailsParse, this.$mangaUrl.toString());
        return new MangasPage(CollectionsKt.listOf(access$mangaDetailsParse), false);
    }
}

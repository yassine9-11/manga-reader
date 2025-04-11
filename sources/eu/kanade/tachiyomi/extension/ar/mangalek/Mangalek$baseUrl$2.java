package eu.kanade.tachiyomi.extension.ar.mangalek;

import kotlin.Metadata;
import kotlin.collections.ArraysKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;

@Metadata(d1 = {"\u0000\b\n\u0000\n\u0002\u0010\u000e\n\u0000\u0010\u0000\u001a\u00020\u0001H\n¢\u0006\u0002\b\u0002"}, d2 = {"<anonymous>", "", "invoke"}, k = 3, mv = {1, 7, 1}, xi = 48)
/* compiled from: Mangalek.kt */
final class Mangalek$baseUrl$2 extends Lambda implements Function0<String> {
    final /* synthetic */ Mangalek this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    Mangalek$baseUrl$2(Mangalek mangalek) {
        super(0);
        this.this$0 = mangalek;
    }

    public final String invoke() {
        if (Intrinsics.areEqual(System.getenv("CI"), "true")) {
            return ArraysKt.joinToString$default(MangalekKt.getMIRROR_PREF_ENTRY_VALUES(), "#, ", (CharSequence) null, (CharSequence) null, 0, (CharSequence) null, (Function1) null, 62, (Object) null);
        }
        String string = this.this$0.getPreferences().getString("MIRROR", MangalekKt.MIRROR_PREF_DEFAULT_VALUE);
        Intrinsics.checkNotNull(string);
        return string;
    }
}

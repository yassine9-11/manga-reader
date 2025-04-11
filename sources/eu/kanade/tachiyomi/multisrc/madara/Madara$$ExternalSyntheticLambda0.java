package eu.kanade.tachiyomi.multisrc.madara;

import kotlin.jvm.functions.Function1;
import rx.functions.Func1;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class Madara$$ExternalSyntheticLambda0 implements Func1 {
    public final /* synthetic */ Function1 f$0;

    public /* synthetic */ Madara$$ExternalSyntheticLambda0(Function1 function1) {
        this.f$0 = function1;
    }

    public final Object call(Object obj) {
        return Madara.fetchSearchManga$lambda$7(this.f$0, obj);
    }
}

package eu.kanade.tachiyomi.multisrc.madara;

import kotlin.Metadata;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;
import kotlinx.serialization.json.Json;
import uy.kohesive.injekt.InjektKt;
import uy.kohesive.injekt.api.FullTypeReference;

@Metadata(d1 = {"\u0000\u0004\n\u0002\b\u0004\u0010\u0000\u001a\u00028\u0000H\n¢\u0006\u0004\b\u0001\u0010\u0002¨\u0006\u0003"}, d2 = {"<anonymous>", "invoke", "()Ljava/lang/Object;", "uy/kohesive/injekt/InjektKt$injectLazy$1"}, k = 3, mv = {1, 7, 1})
/* compiled from: Injekt.kt */
public final class Madara$special$$inlined$injectLazy$1 extends Lambda implements Function0<Json> {
    public static final Madara$special$$inlined$injectLazy$1 INSTANCE = new Madara$special$$inlined$injectLazy$1();

    public Madara$special$$inlined$injectLazy$1() {
        super(0);
    }

    /* JADX WARNING: type inference failed for: r0v2, types: [kotlinx.serialization.json.Json, java.lang.Object] */
    public final Json invoke() {
        return InjektKt.getInjekt().getInstance(((FullTypeReference) new FullTypeReference<Json>() {
        }).getType());
    }
}

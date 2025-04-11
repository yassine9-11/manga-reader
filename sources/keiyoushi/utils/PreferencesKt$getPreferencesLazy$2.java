package keiyoushi.utils;

import android.app.Application;
import android.content.SharedPreferences;
import eu.kanade.tachiyomi.source.online.HttpSource;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import uy.kohesive.injekt.InjektKt;

@Metadata(d1 = {"\u0000\b\n\u0000\n\u0002\u0018\u0002\n\u0000\u0010\u0000\u001a\u00020\u0001H\n¢\u0006\u0002\b\u0002"}, d2 = {"<anonymous>", "Landroid/content/SharedPreferences;", "invoke"}, k = 3, mv = {1, 7, 1}, xi = 176)
/* compiled from: Preferences.kt */
public final class PreferencesKt$getPreferencesLazy$2 extends Lambda implements Function0<SharedPreferences> {
    final /* synthetic */ Function1<SharedPreferences, Unit> $migration;
    final /* synthetic */ HttpSource $this_getPreferencesLazy;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public PreferencesKt$getPreferencesLazy$2(HttpSource httpSource, Function1<? super SharedPreferences, Unit> function1) {
        super(0);
        this.$this_getPreferencesLazy = httpSource;
        this.$migration = function1;
    }

    public final SharedPreferences invoke() {
        HttpSource httpSource = this.$this_getPreferencesLazy;
        Function1<SharedPreferences, Unit> function1 = this.$migration;
        long id = httpSource.getId();
        SharedPreferences sharedPreferences = ((Application) InjektKt.getInjekt().getInstance(new PreferencesKt$getPreferences$$inlined$get$1().getType())).getSharedPreferences("source_" + id, 0);
        Intrinsics.checkNotNullExpressionValue(sharedPreferences, "Injekt.get<Application>(…ource_$sourceId\", 0x0000)");
        function1.invoke(sharedPreferences);
        return sharedPreferences;
    }
}

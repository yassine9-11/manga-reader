package keiyoushi.utils;

import android.app.Application;
import android.content.SharedPreferences;
import eu.kanade.tachiyomi.source.online.HttpSource;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import uy.kohesive.injekt.InjektKt;

@Metadata(d1 = {"\u0000&\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u001a\u0011\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\b\u001a+\u0010\u0000\u001a\u00020\u0001*\u00020\u00042\u0019\b\u0002\u0010\u0005\u001a\u0013\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u00020\u00070\u0006¢\u0006\u0002\b\bH\bø\u0001\u0000\u001a1\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00010\n*\u00020\u00042\u0019\b\u0006\u0010\u0005\u001a\u0013\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u00020\u00070\u0006¢\u0006\u0002\b\bH\bø\u0001\u0000\u0002\u0007\n\u0005\b20\u0001¨\u0006\u000b"}, d2 = {"getPreferences", "Landroid/content/SharedPreferences;", "sourceId", "", "Leu/kanade/tachiyomi/source/online/HttpSource;", "migration", "Lkotlin/Function1;", "", "Lkotlin/ExtensionFunctionType;", "getPreferencesLazy", "Lkotlin/Lazy;", "utils_release"}, k = 2, mv = {1, 7, 1}, xi = 48)
/* compiled from: Preferences.kt */
public final class PreferencesKt {
    public static /* synthetic */ SharedPreferences getPreferences$default(HttpSource httpSource, Function1 function1, int i, Object obj) {
        if ((i & 1) != 0) {
            function1 = (Function1) PreferencesKt$getPreferences$1.INSTANCE;
        }
        Intrinsics.checkNotNullParameter(httpSource, "<this>");
        Intrinsics.checkNotNullParameter(function1, "migration");
        long id = httpSource.getId();
        SharedPreferences sharedPreferences = ((Application) InjektKt.getInjekt().getInstance(new PreferencesKt$getPreferences$$inlined$get$1().getType())).getSharedPreferences("source_" + id, 0);
        Intrinsics.checkNotNullExpressionValue(sharedPreferences, "Injekt.get<Application>(…ource_$sourceId\", 0x0000)");
        function1.invoke(sharedPreferences);
        return sharedPreferences;
    }

    public static final SharedPreferences getPreferences(HttpSource httpSource, Function1<? super SharedPreferences, Unit> function1) {
        Intrinsics.checkNotNullParameter(httpSource, "<this>");
        Intrinsics.checkNotNullParameter(function1, "migration");
        long id = httpSource.getId();
        SharedPreferences sharedPreferences = ((Application) InjektKt.getInjekt().getInstance(new PreferencesKt$getPreferences$$inlined$get$1().getType())).getSharedPreferences("source_" + id, 0);
        Intrinsics.checkNotNullExpressionValue(sharedPreferences, "Injekt.get<Application>(…ource_$sourceId\", 0x0000)");
        function1.invoke(sharedPreferences);
        return sharedPreferences;
    }

    public static /* synthetic */ Lazy getPreferencesLazy$default(HttpSource httpSource, Function1 function1, int i, Object obj) {
        if ((i & 1) != 0) {
            function1 = (Function1) PreferencesKt$getPreferencesLazy$1.INSTANCE;
        }
        Intrinsics.checkNotNullParameter(httpSource, "<this>");
        Intrinsics.checkNotNullParameter(function1, "migration");
        return LazyKt.lazy(new PreferencesKt$getPreferencesLazy$2(httpSource, function1));
    }

    public static final Lazy<SharedPreferences> getPreferencesLazy(HttpSource httpSource, Function1<? super SharedPreferences, Unit> function1) {
        Intrinsics.checkNotNullParameter(httpSource, "<this>");
        Intrinsics.checkNotNullParameter(function1, "migration");
        return LazyKt.lazy(new PreferencesKt$getPreferencesLazy$2(httpSource, function1));
    }

    public static final SharedPreferences getPreferences(long j) {
        SharedPreferences sharedPreferences = ((Application) InjektKt.getInjekt().getInstance(new PreferencesKt$getPreferences$$inlined$get$1().getType())).getSharedPreferences("source_" + j, 0);
        Intrinsics.checkNotNullExpressionValue(sharedPreferences, "Injekt.get<Application>(…ource_$sourceId\", 0x0000)");
        return sharedPreferences;
    }
}

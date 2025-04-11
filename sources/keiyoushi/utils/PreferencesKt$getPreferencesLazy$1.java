package keiyoushi.utils;

import android.content.SharedPreferences;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;

@Metadata(d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0000\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\n¢\u0006\u0002\b\u0003"}, d2 = {"<anonymous>", "", "Landroid/content/SharedPreferences;", "invoke"}, k = 3, mv = {1, 7, 1}, xi = 176)
/* compiled from: Preferences.kt */
public final class PreferencesKt$getPreferencesLazy$1 extends Lambda implements Function1<SharedPreferences, Unit> {
    public static final PreferencesKt$getPreferencesLazy$1 INSTANCE = new PreferencesKt$getPreferencesLazy$1();

    public PreferencesKt$getPreferencesLazy$1() {
        super(1);
    }

    public final void invoke(SharedPreferences sharedPreferences) {
        Intrinsics.checkNotNullParameter(sharedPreferences, "$this$null");
    }

    public /* bridge */ /* synthetic */ Object invoke(Object obj) {
        invoke((SharedPreferences) obj);
        return Unit.INSTANCE;
    }
}

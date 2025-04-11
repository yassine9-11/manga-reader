package eu.kanade.tachiyomi.extension.ar.mangalek;

import android.app.Application;
import android.content.SharedPreferences;
import eu.kanade.tachiyomi.source.online.HttpSource;
import kotlin.Metadata;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import uy.kohesive.injekt.InjektKt;
import uy.kohesive.injekt.api.FullTypeReference;

@Metadata(d1 = {"\u0000\n\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u0001H\n¢\u0006\u0002\b\u0002¨\u0006\u0003"}, d2 = {"<anonymous>", "Landroid/content/SharedPreferences;", "invoke", "keiyoushi/utils/PreferencesKt$getPreferencesLazy$2"}, k = 3, mv = {1, 7, 1}, xi = 48)
/* compiled from: Preferences.kt */
public final class Mangalek$special$$inlined$getPreferencesLazy$default$1 extends Lambda implements Function0<SharedPreferences> {
    final /* synthetic */ HttpSource $this_getPreferencesLazy;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public Mangalek$special$$inlined$getPreferencesLazy$default$1(HttpSource httpSource) {
        super(0);
        this.$this_getPreferencesLazy = httpSource;
    }

    public final SharedPreferences invoke() {
        long id = this.$this_getPreferencesLazy.getId();
        SharedPreferences sharedPreferences = ((Application) InjektKt.getInjekt().getInstance(new FullTypeReference<Application>() {
        }.getType())).getSharedPreferences("source_" + id, 0);
        Intrinsics.checkNotNullExpressionValue(sharedPreferences, "Injekt.get<Application>(…ource_$sourceId\", 0x0000)");
        return sharedPreferences;
    }
}

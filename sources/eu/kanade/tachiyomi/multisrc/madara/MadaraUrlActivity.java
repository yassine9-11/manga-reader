package eu.kanade.tachiyomi.multisrc.madara;

import android.app.Activity;
import java.util.List;
import kotlin.Metadata;

@Metadata(d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010!\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u0018\u0010\u0003\u001a\u0004\u0018\u00010\u00042\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00040\u0006H\u0002J\u0012\u0010\u0007\u001a\u00020\b2\b\u0010\t\u001a\u0004\u0018\u00010\nH\u0014¨\u0006\u000b"}, d2 = {"Leu/kanade/tachiyomi/multisrc/madara/MadaraUrlActivity;", "Landroid/app/Activity;", "()V", "getSLUG", "", "pathSegments", "", "onCreate", "", "savedInstanceState", "Landroid/os/Bundle;", "madara_release"}, k = 1, mv = {1, 7, 1}, xi = 48)
/* compiled from: MadaraUrlActivity.kt */
public final class MadaraUrlActivity extends Activity {
    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:2:0x0009, code lost:
        r4 = r4.getData();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onCreate(android.os.Bundle r4) {
        /*
            r3 = this;
            super.onCreate(r4)
            android.content.Intent r4 = r3.getIntent()
            if (r4 == 0) goto L_0x0014
            android.net.Uri r4 = r4.getData()
            if (r4 == 0) goto L_0x0014
            java.util.List r4 = r4.getPathSegments()
            goto L_0x0015
        L_0x0014:
            r4 = 0
        L_0x0015:
            java.lang.String r0 = "MadaraUrl"
            if (r4 == 0) goto L_0x004d
            int r1 = r4.size()
            r2 = 2
            if (r1 < r2) goto L_0x004d
            android.content.Intent r1 = new android.content.Intent
            r1.<init>()
            java.lang.String r2 = "eu.kanade.tachiyomi.SEARCH"
            r1.setAction(r2)
            java.lang.String r4 = r3.getSLUG(r4)
            java.lang.String r4 = java.lang.String.valueOf(r4)
            java.lang.String r2 = "query"
            r1.putExtra(r2, r4)
            java.lang.String r4 = "filter"
            java.lang.String r2 = r3.getPackageName()
            r1.putExtra(r4, r2)
            r3.startActivity(r1)     // Catch:{ ActivityNotFoundException -> 0x0044 }
            goto L_0x0062
        L_0x0044:
            r4 = move-exception
            java.lang.String r4 = r4.toString()
            android.util.Log.e(r0, r4)
            goto L_0x0062
        L_0x004d:
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            java.lang.String r1 = "could not parse uri from intent "
            r4.<init>(r1)
            android.content.Intent r1 = r3.getIntent()
            r4.append(r1)
            java.lang.String r4 = r4.toString()
            android.util.Log.e(r0, r4)
        L_0x0062:
            r3.finish()
            r4 = 0
            java.lang.System.exit(r4)
            java.lang.RuntimeException r4 = new java.lang.RuntimeException
            java.lang.String r0 = "System.exit returned normally, while it was supposed to halt JVM."
            r4.<init>(r0)
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: eu.kanade.tachiyomi.multisrc.madara.MadaraUrlActivity.onCreate(android.os.Bundle):void");
    }

    private final String getSLUG(List<String> list) {
        if (list.size() < 2) {
            return null;
        }
        return Madara.URL_SEARCH_PREFIX + list.get(1);
    }
}

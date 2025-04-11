package keiyoushi.utils;

import java.util.Iterator;
import java.util.NoSuchElementException;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(d1 = {"\u0000\f\n\u0002\b\u0002\n\u0002\u0010\u001c\n\u0002\b\u0003\u001a\u001e\u0010\u0000\u001a\u0002H\u0001\"\u0006\b\u0000\u0010\u0001\u0018\u0001*\u0006\u0012\u0002\b\u00030\u0002H\b¢\u0006\u0002\u0010\u0003\u001a \u0010\u0004\u001a\u0004\u0018\u0001H\u0001\"\u0006\b\u0000\u0010\u0001\u0018\u0001*\u0006\u0012\u0002\b\u00030\u0002H\b¢\u0006\u0002\u0010\u0003¨\u0006\u0005"}, d2 = {"firstInstance", "T", "", "(Ljava/lang/Iterable;)Ljava/lang/Object;", "firstInstanceOrNull", "utils_release"}, k = 2, mv = {1, 7, 1}, xi = 48)
/* compiled from: Collections.kt */
public final class CollectionsKt {
    public static final /* synthetic */ <T> T firstInstance(Iterable<?> iterable) {
        Intrinsics.checkNotNullParameter(iterable, "<this>");
        for (T next : iterable) {
            Intrinsics.reifiedOperationMarker(3, "T");
            if (next instanceof Object) {
                Intrinsics.reifiedOperationMarker(1, "T");
                Object obj = (Object) next;
                return next;
            }
        }
        throw new NoSuchElementException("Collection contains no element matching the predicate.");
    }

    public static final /* synthetic */ <T> T firstInstanceOrNull(Iterable<?> iterable) {
        T t;
        Intrinsics.checkNotNullParameter(iterable, "<this>");
        Iterator<?> it = iterable.iterator();
        while (true) {
            if (!it.hasNext()) {
                t = null;
                break;
            }
            t = it.next();
            Intrinsics.reifiedOperationMarker(3, "T");
            if (t instanceof Object) {
                break;
            }
        }
        Intrinsics.reifiedOperationMarker(2, "T");
        Object obj = (Object) t;
        return t;
    }
}

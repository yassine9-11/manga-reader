package keiyoushi.utils;

import java.io.InputStream;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.reflect.KType;
import kotlinx.serialization.DeserializationStrategy;
import kotlinx.serialization.KSerializer;
import kotlinx.serialization.SerializersKt;
import kotlinx.serialization.StringFormat;
import kotlinx.serialization.json.Json;
import kotlinx.serialization.json.JvmStreamsKt;
import kotlinx.serialization.modules.SerializersModule;
import okhttp3.Response;

@Metadata(d1 = {"\u0000\u001a\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a$\u0010\u0006\u001a\u0002H\u0007\"\u0006\b\u0000\u0010\u0007\u0018\u0001*\u00020\b2\b\b\u0002\u0010\t\u001a\u00020\u0001H\b¢\u0006\u0002\u0010\n\u001a$\u0010\u0006\u001a\u0002H\u0007\"\u0006\b\u0000\u0010\u0007\u0018\u0001*\u00020\u000b2\b\b\u0002\u0010\t\u001a\u00020\u0001H\b¢\u0006\u0002\u0010\f\"\u001b\u0010\u0000\u001a\u00020\u00018FX\u0002¢\u0006\f\n\u0004\b\u0004\u0010\u0005\u001a\u0004\b\u0002\u0010\u0003¨\u0006\r"}, d2 = {"jsonInstance", "Lkotlinx/serialization/json/Json;", "getJsonInstance", "()Lkotlinx/serialization/json/Json;", "jsonInstance$delegate", "Lkotlin/Lazy;", "parseAs", "T", "", "json", "(Ljava/lang/String;Lkotlinx/serialization/json/Json;)Ljava/lang/Object;", "Lokhttp3/Response;", "(Lokhttp3/Response;Lkotlinx/serialization/json/Json;)Ljava/lang/Object;", "utils_release"}, k = 2, mv = {1, 7, 1}, xi = 48)
/* compiled from: Json.kt */
public final class JsonKt {
    private static final Lazy jsonInstance$delegate = LazyKt.lazy(JsonKt$special$$inlined$injectLazy$1.INSTANCE);

    public static final Json getJsonInstance() {
        return (Json) jsonInstance$delegate.getValue();
    }

    public static /* synthetic */ Object parseAs$default(String str, Json json, int i, Object obj) {
        if ((i & 1) != 0) {
            json = getJsonInstance();
        }
        Intrinsics.checkNotNullParameter(str, "<this>");
        Intrinsics.checkNotNullParameter(json, "json");
        StringFormat stringFormat = (StringFormat) json;
        SerializersModule serializersModule = stringFormat.getSerializersModule();
        Intrinsics.reifiedOperationMarker(6, "T");
        DeserializationStrategy serializer = SerializersKt.serializer(serializersModule, (KType) null);
        Intrinsics.checkNotNull(serializer, "null cannot be cast to non-null type kotlinx.serialization.KSerializer<T of kotlinx.serialization.internal.Platform_commonKt.cast>");
        KSerializer kSerializer = (KSerializer) serializer;
        return stringFormat.decodeFromString(serializer, str);
    }

    public static final /* synthetic */ <T> T parseAs(String str, Json json) {
        Intrinsics.checkNotNullParameter(str, "<this>");
        Intrinsics.checkNotNullParameter(json, "json");
        StringFormat stringFormat = (StringFormat) json;
        SerializersModule serializersModule = stringFormat.getSerializersModule();
        Intrinsics.reifiedOperationMarker(6, "T");
        DeserializationStrategy serializer = SerializersKt.serializer(serializersModule, (KType) null);
        Intrinsics.checkNotNull(serializer, "null cannot be cast to non-null type kotlinx.serialization.KSerializer<T of kotlinx.serialization.internal.Platform_commonKt.cast>");
        KSerializer kSerializer = (KSerializer) serializer;
        return stringFormat.decodeFromString(serializer, str);
    }

    public static /* synthetic */ Object parseAs$default(Response response, Json json, int i, Object obj) {
        if ((i & 1) != 0) {
            json = getJsonInstance();
        }
        Intrinsics.checkNotNullParameter(response, "<this>");
        Intrinsics.checkNotNullParameter(json, "json");
        InputStream byteStream = response.body().byteStream();
        SerializersModule serializersModule = json.getSerializersModule();
        Intrinsics.reifiedOperationMarker(6, "T");
        DeserializationStrategy serializer = SerializersKt.serializer(serializersModule, (KType) null);
        Intrinsics.checkNotNull(serializer, "null cannot be cast to non-null type kotlinx.serialization.KSerializer<T of kotlinx.serialization.internal.Platform_commonKt.cast>");
        KSerializer kSerializer = (KSerializer) serializer;
        return JvmStreamsKt.decodeFromStream(json, serializer, byteStream);
    }

    public static final /* synthetic */ <T> T parseAs(Response response, Json json) {
        Intrinsics.checkNotNullParameter(response, "<this>");
        Intrinsics.checkNotNullParameter(json, "json");
        InputStream byteStream = response.body().byteStream();
        SerializersModule serializersModule = json.getSerializersModule();
        Intrinsics.reifiedOperationMarker(6, "T");
        DeserializationStrategy serializer = SerializersKt.serializer(serializersModule, (KType) null);
        Intrinsics.checkNotNull(serializer, "null cannot be cast to non-null type kotlinx.serialization.KSerializer<T of kotlinx.serialization.internal.Platform_commonKt.cast>");
        KSerializer kSerializer = (KSerializer) serializer;
        return JvmStreamsKt.decodeFromStream(json, serializer, byteStream);
    }
}

package eu.kanade.tachiyomi.extension.ar.mangalek;

import eu.kanade.tachiyomi.extension.ar.mangalek.Mangalek;
import java.util.List;
import kotlin.Deprecated;
import kotlin.DeprecationLevel;
import kotlin.Metadata;
import kotlin.ReplaceWith;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.serialization.KSerializer;
import kotlinx.serialization.UnknownFieldException;
import kotlinx.serialization.descriptors.SerialDescriptor;
import kotlinx.serialization.encoding.CompositeDecoder;
import kotlinx.serialization.encoding.CompositeEncoder;
import kotlinx.serialization.encoding.Decoder;
import kotlinx.serialization.encoding.Encoder;
import kotlinx.serialization.internal.ArrayListSerializer;
import kotlinx.serialization.internal.BooleanSerializer;
import kotlinx.serialization.internal.GeneratedSerializer;
import kotlinx.serialization.internal.PluginGeneratedSerialDescriptor;
import kotlinx.serialization.internal.SerializationConstructorMarker;

@Metadata(d1 = {"\u00006\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\bÇ\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0003J\u0018\u0010\b\u001a\f\u0012\b\u0012\u0006\u0012\u0002\b\u00030\n0\tHÖ\u0001¢\u0006\u0002\u0010\u000bJ\u0011\u0010\f\u001a\u00020\u00022\u0006\u0010\r\u001a\u00020\u000eHÖ\u0001J\u0019\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u0002HÖ\u0001R\u0014\u0010\u0004\u001a\u00020\u00058VXÖ\u0005¢\u0006\u0006\u001a\u0004\b\u0006\u0010\u0007¨\u0006\u0014"}, d2 = {"eu/kanade/tachiyomi/extension/ar/mangalek/Mangalek.SearchResponseDto.$serializer", "Lkotlinx/serialization/internal/GeneratedSerializer;", "Leu/kanade/tachiyomi/extension/ar/mangalek/Mangalek$SearchResponseDto;", "()V", "descriptor", "Lkotlinx/serialization/descriptors/SerialDescriptor;", "getDescriptor", "()Lkotlinx/serialization/descriptors/SerialDescriptor;", "childSerializers", "", "Lkotlinx/serialization/KSerializer;", "()[Lkotlinx/serialization/KSerializer;", "deserialize", "decoder", "Lkotlinx/serialization/encoding/Decoder;", "serialize", "", "encoder", "Lkotlinx/serialization/encoding/Encoder;", "value", "tachiyomi-ar.mangalek-v1.4.49_release"}, k = 1, mv = {1, 7, 1}, xi = 48)
@Deprecated(level = DeprecationLevel.HIDDEN, message = "This synthesized declaration should not be used directly", replaceWith = @ReplaceWith(expression = "", imports = {}))
/* compiled from: Mangalek.kt */
public final class Mangalek$SearchResponseDto$$serializer implements GeneratedSerializer<Mangalek.SearchResponseDto> {
    public static final Mangalek$SearchResponseDto$$serializer INSTANCE;
    public static final /* synthetic */ SerialDescriptor descriptor;

    public SerialDescriptor getDescriptor() {
        return descriptor;
    }

    static {
        GeneratedSerializer mangalek$SearchResponseDto$$serializer = new Mangalek$SearchResponseDto$$serializer();
        INSTANCE = mangalek$SearchResponseDto$$serializer;
        SerialDescriptor pluginGeneratedSerialDescriptor = new PluginGeneratedSerialDescriptor("eu.kanade.tachiyomi.extension.ar.mangalek.Mangalek.SearchResponseDto", mangalek$SearchResponseDto$$serializer, 2);
        pluginGeneratedSerialDescriptor.addElement("data", false);
        pluginGeneratedSerialDescriptor.addElement("success", false);
        descriptor = pluginGeneratedSerialDescriptor;
    }

    private Mangalek$SearchResponseDto$$serializer() {
    }

    public KSerializer<?>[] childSerializers() {
        return new KSerializer[]{(KSerializer) new ArrayListSerializer(Mangalek$SearchEntryDto$$serializer.INSTANCE), (KSerializer) BooleanSerializer.INSTANCE};
    }

    public Mangalek.SearchResponseDto deserialize(Decoder decoder) {
        int i;
        boolean z;
        Object obj;
        Intrinsics.checkNotNullParameter(decoder, "decoder");
        SerialDescriptor descriptor2 = getDescriptor();
        CompositeDecoder beginStructure = decoder.beginStructure(descriptor2);
        if (beginStructure.decodeSequentially()) {
            obj = beginStructure.decodeSerializableElement(descriptor2, 0, new ArrayListSerializer(Mangalek$SearchEntryDto$$serializer.INSTANCE), (Object) null);
            z = beginStructure.decodeBooleanElement(descriptor2, 1);
            i = 3;
        } else {
            obj = null;
            boolean z2 = false;
            int i2 = 0;
            boolean z3 = true;
            while (z3) {
                int decodeElementIndex = beginStructure.decodeElementIndex(descriptor2);
                if (decodeElementIndex == -1) {
                    z3 = false;
                } else if (decodeElementIndex == 0) {
                    obj = beginStructure.decodeSerializableElement(descriptor2, 0, new ArrayListSerializer(Mangalek$SearchEntryDto$$serializer.INSTANCE), obj);
                    i2 |= 1;
                } else if (decodeElementIndex == 1) {
                    z2 = beginStructure.decodeBooleanElement(descriptor2, 1);
                    i2 |= 2;
                } else {
                    throw new UnknownFieldException(decodeElementIndex);
                }
            }
            z = z2;
            i = i2;
        }
        beginStructure.endStructure(descriptor2);
        return new Mangalek.SearchResponseDto(i, (List) obj, z, (SerializationConstructorMarker) null);
    }

    public void serialize(Encoder encoder, Mangalek.SearchResponseDto searchResponseDto) {
        Intrinsics.checkNotNullParameter(encoder, "encoder");
        Intrinsics.checkNotNullParameter(searchResponseDto, "value");
        SerialDescriptor descriptor2 = getDescriptor();
        CompositeEncoder beginStructure = encoder.beginStructure(descriptor2);
        Mangalek.SearchResponseDto.write$Self(searchResponseDto, beginStructure, descriptor2);
        beginStructure.endStructure(descriptor2);
    }

    public KSerializer<?>[] typeParametersSerializers() {
        return GeneratedSerializer.DefaultImpls.typeParametersSerializers(this);
    }
}

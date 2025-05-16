package net.space333.enchants.Component;

import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentType;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.Codecs;
import net.space333.enchants.Enchants;

import java.util.function.UnaryOperator;

public class ModDataComponentType {
    public static final ComponentType<Integer> UNBREAKING = register("unbreaking",builder -> builder.codec(Codecs.POSITIVE_INT).packetCodec(PacketCodecs.VAR_INT));
    public static final ComponentType<Integer> EFFICIENCY = register("efficiency",builder -> builder.codec(Codecs.POSITIVE_INT).packetCodec(PacketCodecs.VAR_INT));
    public static final ComponentType<Integer> PROTECTION = register("protection",builder -> builder.codec(Codecs.POSITIVE_INT).packetCodec(PacketCodecs.VAR_INT));
    public static final ComponentType<Integer> SHARPNESS = register("sharpness",builder -> builder.codec(Codecs.POSITIVE_INT).packetCodec(PacketCodecs.VAR_INT));
    public static final ComponentType<Integer> POWER = register("power",builder -> builder.codec(Codecs.POSITIVE_INT).packetCodec(PacketCodecs.VAR_INT));
    public static final ComponentType<Integer> IMPALING = register("impaling",builder -> builder.codec(Codecs.POSITIVE_INT).packetCodec(PacketCodecs.VAR_INT));
    public static final ComponentType<Integer> DENSITY = register("density",builder -> builder.codec(Codecs.POSITIVE_INT).packetCodec(PacketCodecs.VAR_INT));



    private  static <T>ComponentType<T> register(String name, UnaryOperator<ComponentType.Builder<T>> builderOperator) {
        return Registry.register(Registries.DATA_COMPONENT_TYPE, Identifier.of(Enchants.MOD_ID, name),
                builderOperator.apply(ComponentType.builder()).build()
        );
    }

    public static void registerDataComponentTypes() {
        Enchants.LOGGER.info("Registering Data Component Types for" + Enchants.MOD_ID);
    }
}

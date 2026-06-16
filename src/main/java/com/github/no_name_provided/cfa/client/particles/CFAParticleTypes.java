package com.github.no_name_provided.cfa.client.particles;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jspecify.annotations.NonNull;

import static com.github.no_name_provided.cfa.CommunityFluidAPI.MODID;

/**
 * Our ParticleType registry.
 */
public class CFAParticleTypes {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
            DeferredRegister.create(BuiltInRegistries.PARTICLE_TYPE, MODID);
    
    @SuppressWarnings("Convert2Diamond") // We actually need explicit typing for the IDE to properly parse the "this"
    public static final DeferredHolder<ParticleType<?>, ParticleType<ColorParticleOption>> TINTED_SPLASH_PARTICLE = PARTICLE_TYPES.register(
            "tinted_splash_particle",
            () -> new ParticleType<ColorParticleOption>(false) {
                @Override
                public @NonNull MapCodec<ColorParticleOption> codec() {
                    return ColorParticleOption.codec(this);
                }
                
                @Override
                public @NonNull StreamCodec<? super RegistryFriendlyByteBuf, ColorParticleOption> streamCodec() {
                    return ColorParticleOption.streamCodec(this);
                }
            }
    );
    @SuppressWarnings("Convert2Diamond") // We actually need explicit typing for the IDE to properly parse the "this"
    public static final DeferredHolder<ParticleType<?>, ParticleType<ColorParticleOption>> TINTED_WAKE_PARTICLE = PARTICLE_TYPES.register(
            "tinted_wake_particle",
            () -> new ParticleType<ColorParticleOption>(false) {
                @Override
                public @NonNull MapCodec<ColorParticleOption> codec() {
                    return ColorParticleOption.codec(this);
                }
                
                @Override
                public @NonNull StreamCodec<? super RegistryFriendlyByteBuf, ColorParticleOption> streamCodec() {
                    return ColorParticleOption.streamCodec(this);
                }
            }
    );
    
    /**
     * Adds this deferred register to the mod event bus, so it won't be ignored.
     */
    public static void register(IEventBus modBus) {
        PARTICLE_TYPES.register(modBus);
    }
}

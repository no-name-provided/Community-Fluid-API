package com.github.no_name_provided.cfa.client.particles.options;

import com.mojang.logging.annotations.MethodsReturnNonnullByDefault;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.material.Fluid;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Used by particles that require information about a fluid.
 */
@ParametersAreNonnullByDefault @MethodsReturnNonnullByDefault
public class FluidParticleOption implements ParticleOptions {
    private final ParticleType<FluidParticleOption> type;
    private final Fluid fluid;
    
    public FluidParticleOption(ParticleType<FluidParticleOption> type, Fluid fluid) {
        this.type = type;
        this.fluid = fluid;
    }
    
    public static MapCodec<FluidParticleOption> codec(ParticleType<FluidParticleOption> type) {
        return BuiltInRegistries.FLUID.byNameCodec().xmap(
                fluid -> new FluidParticleOption(type, fluid),
                o -> o.fluid
        ).fieldOf("fluid");
    }
    
    /**
     * May want to make this more efficient. There's probably a reason vanilla doesn't use #fromCodec very often. Needs
     * testing.
     */
    public static StreamCodec<RegistryFriendlyByteBuf, FluidParticleOption> streamCodec(ParticleType<FluidParticleOption> type) {
        return ByteBufCodecs.fromCodecWithRegistries(codec(type).codec()).map(
                fluid -> new FluidParticleOption(type, fluid.fluid),
                o -> new FluidParticleOption(type, o.fluid)
        );
    }
    
    public Fluid getFluid() {
        return fluid;
    }
    
    @Override
    public ParticleType<?> getType() {
        return type;
    }
}

package com.github.no_name_provided.cfa.common;

import net.minecraft.client.Minecraft;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.client.fluid.FluidTintSource;

import java.util.Optional;

/**
 * This code exists to allow common code to reference client classes in conditional code without actually loading them.
 */
public class ClientWrappers {
    /**
     * It's no longer possible to get fluid tints from IClientFluidTypeExtension. That part of the Fluid API has been
     * removed. Instead, we crib from FluidContentsTint.
     *
     * @param fluid The Fluid whose tint we should grab.
     * @return An optional containing the tint applied to the fluid's default FluidState. May be empty.
     */
    public static Optional<Integer> getFluidTint(Fluid fluid) {
        FluidTintSource tintSource = Minecraft.getInstance()
                .getModelManager()
                .getFluidStateModelSet()
                .get(fluid.defaultFluidState())
                .fluidTintSource();
        return tintSource != null ? Optional.of(tintSource.color(fluid.defaultFluidState())) : Optional.empty();
    }
}

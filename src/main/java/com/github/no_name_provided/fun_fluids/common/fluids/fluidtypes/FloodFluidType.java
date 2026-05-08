package com.github.no_name_provided.fun_fluids.common.fluids.fluidtypes;

import com.mojang.logging.annotations.MethodsReturnNonnullByDefault;
import net.minecraft.sounds.SoundEvents;
import net.neoforged.neoforge.common.SoundActions;
import net.neoforged.neoforge.fluids.FluidType;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault @MethodsReturnNonnullByDefault
public class FloodFluidType extends FluidType {
    public FloodFluidType() {
        super(Properties.create()
                // Taken from net.neoforged.neoforge.common.NeoForgeMod.WATER_TYPE
                .fallDistanceModifier(0F)
                .canExtinguish(true)
                .canConvertToSource(true)
                .supportsBoating(true)
                .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
                .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)
                .sound(SoundActions.FLUID_VAPORIZE, SoundEvents.FIRE_EXTINGUISH)
                .canHydrate(true)
        );
    }

}

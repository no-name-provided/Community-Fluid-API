package com.github.no_name_provided.fun_fluids.common.fluids.fluidtypes;

import com.github.no_name_provided.fun_fluids.datagen.providers.FFFluidTagsProvider;
import com.mojang.logging.annotations.MethodsReturnNonnullByDefault;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.common.SoundActions;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault @MethodsReturnNonnullByDefault
public class FloodFluidType extends TaggedFluidType {
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
    
    /**
     * Since vanilla has leaned heavily into the use of tags, we're now associating one with each fluid type.
     * This greatly simplifies the mixins required to replace the now-retiring Fluid API.
     */
    @Override
    public TagKey<Fluid> getTag() {
        return FFFluidTagsProvider.FLOOD_FLUID;
    }
    
    /**
     * Is mining speed reduced while immersed in this fluid?
     */
    @Override
    public boolean reducesMiningSpeed() {
        return true;
    }
}

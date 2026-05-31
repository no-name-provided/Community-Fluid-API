package com.github.no_name_provided.fun_fluids.mixin_interfaces;

import com.github.no_name_provided.fun_fluids.datagen.providers.FFFluidTagsProvider;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.fluids.FluidType;

/**
 * Used to inject a tag getter into all FluidType subclasses. This way, we don't need to special case vanilla fluids
 * every time we iterate through the registry.
 * <p>
 * Unfortunately, we can't add this to development environments because Neo's interface injection silently fails when
 * used on Neo's own classes.
 * </p>
 */
public interface TaggedFluidType {
    
    /**
     * If you're making a modded fluid, this should be overwritten. Defaults to net.minecraft.tags.FluidTags#WATER.
     *
     * @return The fluid tag associated with your fluid type. This is used to check if an entity is touching your fluid.
     */
    default TagKey<Fluid> getTag() {
        FluidType thisType = (FluidType) this;
        
        // We prefer vanilla tags here, for easier cross-loader compat.
        // Neo tags will grab entries in their vanilla counterparts
        if (thisType == NeoForgeMod.LAVA_TYPE.value()) {
            return FluidTags.LAVA;
        } else if (thisType == NeoForgeMod.EMPTY_TYPE.value()) {
            return FFFluidTagsProvider.EMPTY;
        } else if (thisType == NeoForgeMod.MILK_TYPE.value()) {
            return Tags.Fluids.MILK;
        } else {
            return FluidTags.WATER;
        }
    }
}

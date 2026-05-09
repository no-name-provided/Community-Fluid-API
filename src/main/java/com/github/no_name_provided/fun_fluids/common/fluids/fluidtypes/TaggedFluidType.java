package com.github.no_name_provided.fun_fluids.common.fluids.fluidtypes;

import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidType;

public abstract class TaggedFluidType extends FluidType {
    public TaggedFluidType(Properties properties) {
        super(properties);
    }
    
    /**
     * Since vanilla has leaned heavily into the use of tags, we're now associating one with each fluid type.
     * This greatly simplifies the mixins required to replace the now-retiring Fluid API.
     */
    abstract public TagKey<Fluid> getTag();
}

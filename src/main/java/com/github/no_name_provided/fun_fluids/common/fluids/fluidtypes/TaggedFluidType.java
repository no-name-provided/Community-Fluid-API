package com.github.no_name_provided.fun_fluids.common.fluids.fluidtypes;

import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidType;

/**
 * This is a convenience class. Since The neo compile time interface injection doesn't work on its own classes, we need
 * a subclass that implements the interfaces methods to trick the compiler into letting us use Override annotation.
 */
@SuppressWarnings("unused")
public abstract class TaggedFluidType extends FluidType {
    public TaggedFluidType(Properties properties) {
        super(properties);
    }
    
    /**
     * Since vanilla has leaned heavily into the use of tags, we're now associating one with each fluid type. This
     * greatly simplifies the mixins required to replace the now-retiring Fluid API.
     * <p>
     * This is required, even with interface injections, because our compiler won't otherwise allow @Override.
     * </p>
     */
    abstract public TagKey<Fluid> getTag();
    
    /**
     * Is mining speed reduced while immersed in this fluid?
     */
    public boolean reducesMiningSpeed() {
        return false;
    }
}

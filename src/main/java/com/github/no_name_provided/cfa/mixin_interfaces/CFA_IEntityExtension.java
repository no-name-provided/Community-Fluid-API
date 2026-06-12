package com.github.no_name_provided.cfa.mixin_interfaces;

import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.fluids.FluidType;

/**
 * Used to inject a more generic version of the `wasTouchingWater` and `wasTouchingLava` fields, for efficiency
 * (theoretically, down the line) and so we can react to state changes for things like splash effects.
 * <p>
 * These are both overridden in the Entity mixin. The default implementation are just provided to prevent compiler
 * complaints from our static interface injection.
 * </p>
 */
public interface CFA_IEntityExtension {
    default FluidType getLastFluid() {
        return NeoForgeMod.EMPTY_TYPE.value();
    }
    
    default void setLastFluid(FluidType lastFluid) {
        // pass
    }
}

package com.github.no_name_provided.cfa.mixin_interfaces;

import net.neoforged.neoforge.fluids.FluidType;

/**
 * Used to inject a more generic version of the `wasTouchingWater` and `wasTouchingLava` fields, for efficiency
 * (theoretically, down the line) and so we can react to state changes for things like splash effects.
 */
public interface CFA_IEntityExtension {
    FluidType getLastFluid();
    
    void setLastFluid(FluidType lastFluid);
}

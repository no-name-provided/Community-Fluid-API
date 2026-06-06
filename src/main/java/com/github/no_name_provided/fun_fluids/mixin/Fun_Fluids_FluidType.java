package com.github.no_name_provided.fun_fluids.mixin;

import com.github.no_name_provided.fun_fluids.mixin_interfaces.IFluidTypeExtension;
import net.neoforged.neoforge.fluids.FluidType;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Injects methods into FluidType, so we can stop special casing the default types and types added by other mods.
 */
@Mixin(FluidType.class)
public class Fun_Fluids_FluidType implements IFluidTypeExtension {

}

package com.github.no_name_provided.fun_fluids.common.fluids;

import com.github.no_name_provided.fun_fluids.common.fluids.registries.BlockRegistry;
import com.github.no_name_provided.fun_fluids.common.fluids.registries.FluidRegistries;
import com.github.no_name_provided.fun_fluids.common.fluids.registries.ItemRegistry;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;

/**
 * Example of a basic fluid implementation, demonstrating the use of convenience classes and dynamically recolored
 * generic assets (provided by vanilla and/or NeoForge) to make a fluid with minimal effort/code and no unique textures.
 * <p>
 *     For an alternative example, see FloodFluid.
 * </p> <p>
 *     Note: In a truly minimal implementation, RiverOfTimeFluidType#motionScale wouldn't be overridden.
 * </p>
 * <p>
 * Note: For consistency, this follows KaupenJoe's pattern. However, I consider directly inheriting from
 * BaseFlowingFluid a bit of an antipattern. An alternative pattern is demonstrated in FloodFluid.
 * </p>
 **/
public abstract class RiverOfTimeFluid extends BaseFlowingFluid {

    public static final Properties PROPERTIES = new Properties(
            FluidRegistries.FunFluidTypes.RIVER_OF_TIME,
            FluidRegistries.FunFluids.RIVER_OF_TIME_FLUID,
            FluidRegistries.FunFluids.FLOWING_RIVER_OF_TIME_FLUID
    )
            .block(BlockRegistry.RIVER_OF_TIME_BLOCK)
            .explosionResistance(120.0f)
            .bucket(ItemRegistry.RIVER_OF_TIME_BUCKET);

    protected RiverOfTimeFluid() {
        super(PROPERTIES);
    }
}

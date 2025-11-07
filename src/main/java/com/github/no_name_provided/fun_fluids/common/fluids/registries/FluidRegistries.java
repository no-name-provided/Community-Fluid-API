package com.github.no_name_provided.fun_fluids.common.fluids.registries;

import com.github.no_name_provided.fun_fluids.common.fluids.ConfigurableFluid;
import com.github.no_name_provided.fun_fluids.common.fluids.CoolLavaFluid;
import com.github.no_name_provided.fun_fluids.common.fluids.RiverOfTimeFluid;
import com.github.no_name_provided.fun_fluids.common.fluids.ThickAirFluid;
import com.github.no_name_provided.fun_fluids.common.fluids.fluidtypes.ConfigurableFluidType;
import com.github.no_name_provided.fun_fluids.common.fluids.fluidtypes.CoolLavaFluidType;
import com.github.no_name_provided.fun_fluids.common.fluids.fluidtypes.RiverOfTimeFluidType;
import com.github.no_name_provided.fun_fluids.common.fluids.fluidtypes.ThickAirFluidType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidType;

import static com.github.no_name_provided.fun_fluids.FunFluids.MODID;

public class FluidRegistries {

    public static class FunFluids {
        public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(BuiltInRegistries.FLUID, MODID);

        public static final DeferredHolder<Fluid, CoolLavaFluid.Flowing> FLOWING_COOL_LAVA = FLUIDS.register(
                "flowing_cool_lava",
                CoolLavaFluid.Flowing::new
        );
        public static final DeferredHolder<Fluid, CoolLavaFluid.Source> COOL_LAVA = FLUIDS.register(
                "cool_lava",
                CoolLavaFluid.Source::new
        );
        // Since this fluid has no flowing variant, we only need to register a source block.
        public static final DeferredHolder<Fluid, ThickAirFluid> THICK_AIR_FLUID = FLUIDS.register(
                "thick_air",
                ThickAirFluid::new
        );
        public static final DeferredHolder<Fluid, ConfigurableFluid.Flowing> FLOWING_CONFIGURABLE_FLUID = FLUIDS.register(
                "flowing_configurable_fluid",
                ConfigurableFluid.Flowing::new
        );
        public static final DeferredHolder<Fluid, ConfigurableFluid.Source> CONFIGURABLE_FLUID = FLUIDS.register(
                "configurable_fluid",
                ConfigurableFluid.Source::new
        );
        public static final DeferredHolder<Fluid, BaseFlowingFluid.Flowing> FLOWING_RIVER_OF_TIME_FLUID = FLUIDS.register(
                "flowing_river_of_time",
                () -> new RiverOfTimeFluid.Flowing(RiverOfTimeFluid.PROPERTIES)
        );
        public static final DeferredHolder<Fluid, BaseFlowingFluid.Source> RIVER_OF_TIME_FLUID = FLUIDS.register(
                "river_of_time",
                () -> new RiverOfTimeFluid.Source(RiverOfTimeFluid.PROPERTIES)
        );
    }

    public static class FunFluidTypes {
        public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(NeoForgeRegistries.FLUID_TYPES, MODID);

        public static final DeferredHolder<FluidType, CoolLavaFluidType> COOL_LAVA = FLUID_TYPES.register(
                "cool_lava",
                CoolLavaFluidType::new
        );
        public static final DeferredHolder<FluidType, ThickAirFluidType> THICK_AIR = FLUID_TYPES.register(
                "thick_air",
                ThickAirFluidType::new
        );
        public static final DeferredHolder<FluidType, ConfigurableFluidType> C_FLUID = FLUID_TYPES.register(
                "c_fluid",
                ConfigurableFluidType::new
        );
        public static final DeferredHolder<FluidType, RiverOfTimeFluidType> RIVER_OF_TIME = FLUID_TYPES.register(
                "river_of_time",
                RiverOfTimeFluidType::new
        );
    }

    public static void register(IEventBus bus) {
        FunFluids.FLUIDS.register(bus);
        FunFluidTypes.FLUID_TYPES.register(bus);
    }

}

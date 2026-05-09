package com.github.no_name_provided.fun_fluids.datagen.providers;

import com.github.no_name_provided.fun_fluids.common.fluids.registries.FluidRegistries.FunFluids;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.FluidTagsProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.concurrent.CompletableFuture;

import static com.github.no_name_provided.fun_fluids.FunFluids.MODID;

@ParametersAreNonnullByDefault
public class FFFluidTagsProvider extends FluidTagsProvider {
    public static final TagKey<Fluid> COOL_LAVA = create("cool_lava");
    public static final TagKey<Fluid> THICK_AIR = create("thick_air");
    public static final TagKey<Fluid> CONFIGURABLE_FLUID = create("configurable_fluid");
    public static final TagKey<Fluid> FLOOD_FLUID = create("flood_fluid");
    public static final TagKey<Fluid> RIVER_OF_TIME = create("river_of_time");

    public FFFluidTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, MODID);
    }
    
    @Override
    protected void addTags(HolderLookup.Provider registries) {
        tag(COOL_LAVA).add(FunFluids.COOL_LAVA.get(), FunFluids.FLOWING_COOL_LAVA.get());
        tag(THICK_AIR).add(FunFluids.THICK_AIR_FLUID.get());
        tag(CONFIGURABLE_FLUID).add(FunFluids.CONFIGURABLE_FLUID.get(), FunFluids.FLOWING_CONFIGURABLE_FLUID.get());
        tag(FLOOD_FLUID).add(FunFluids.FLOOD_FLUID.get(), FunFluids.FLOWING_FLOOD_FLUID.get());
        tag(RIVER_OF_TIME).add(FunFluids.RIVER_OF_TIME_FLUID.get(), FunFluids.FLOWING_RIVER_OF_TIME_FLUID.get());
    }
    
    private static TagKey<Fluid> create(String name) {
        return TagKey.create(Registries.FLUID, Identifier.fromNamespaceAndPath(MODID, name));
    }
}

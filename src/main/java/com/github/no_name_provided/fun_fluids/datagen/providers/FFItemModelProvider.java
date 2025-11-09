package com.github.no_name_provided.fun_fluids.datagen.providers;

import com.github.no_name_provided.fun_fluids.common.fluids.registries.FluidRegistries;
import com.github.no_name_provided.fun_fluids.common.fluids.registries.ItemRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.loaders.DynamicFluidContainerModelBuilder;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class FFItemModelProvider extends ItemModelProvider {
    public FFItemModelProvider(PackOutput output, String modid, ExistingFileHelper existingFileHelper) {
        super(output, modid, existingFileHelper);
    }

    @Override protected void registerModels() {
        withExistingParent(ItemRegistry.COOL_LAVA_BUCKET.getRegisteredName(), mcLoc("item/lava_bucket"));

        withExistingParent(ItemRegistry.THICK_AIR_BUCKET.getRegisteredName(), mcLoc("item/bucket"))
                .texture("layer1", mcLoc("item/firework_star_overlay"));

        // There's a special (Neo)Forge model type for bucket items.
        // More NeoForge convenience models can be found at neoforge-21.1.[XXX]-merged.jar/assets/neoforge/models.
        getBuilder(ItemRegistry.CONFIGURABLE_FLUID_BUCKET.getRegisteredName())
                .parent(getExistingFile(ResourceLocation.fromNamespaceAndPath("neoforge","item/bucket")))
                .customLoader(DynamicFluidContainerModelBuilder::begin)
                .applyFluidLuminosity(true)
                .coverIsMask(false)
                // Change if the density is negative - really should be dynamically determined. #BlameNeoForge
                .flipGas(false)
                .fluid(FluidRegistries.FunFluids.CONFIGURABLE_FLUID.get())
                // Set to false to remind us that this field is ignored. We must
                // register a TintHandler using RegisterColorHandlersEvent.Item
                // or the fluid will be untinted. #BlameThe(Neo)ForgeTeam
                .applyTint(false);
                // If you need a reference to the model file, you can add #end to the end of this call chain.
        getBuilder(ItemRegistry.RIVER_OF_TIME_BUCKET.getRegisteredName())
                .parent(getExistingFile(ResourceLocation.fromNamespaceAndPath("neoforge", "item/bucket")))
                .customLoader(DynamicFluidContainerModelBuilder::begin)
                .applyFluidLuminosity(true)
                .coverIsMask(false)
                .flipGas(false)
                .fluid(FluidRegistries.FunFluids.RIVER_OF_TIME_FLUID.get());

        getBuilder(ItemRegistry.FLOOD_BUCKET.getRegisteredName())
                .parent(getExistingFile(ResourceLocation.fromNamespaceAndPath("neoforge", "item/bucket_drip")))
                .customLoader(DynamicFluidContainerModelBuilder::begin)
                .applyFluidLuminosity(true)
                .coverIsMask(false)
                .flipGas(false)
                .fluid(FluidRegistries.FunFluids.FLOOD_FLUID.get())
                .end()
                .texture("cover", mcLoc("item/barrier"));
    }
}

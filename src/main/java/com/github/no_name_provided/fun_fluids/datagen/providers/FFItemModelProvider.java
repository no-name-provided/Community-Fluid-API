package com.github.no_name_provided.fun_fluids.datagen.providers;

import com.github.no_name_provided.fun_fluids.common.fluids.registries.FluidRegistries;
import com.github.no_name_provided.fun_fluids.common.fluids.registries.ItemRegistry;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Items;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class FFItemModelProvider extends ModelProvider {
    public FFItemModelProvider(PackOutput output, String modid) {
        super(output, modid);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
//        itemModels.generateFlatItem(ItemRegistry.COOL_LAVA_BUCKET.get(), Items.LAVA_BUCKET, ModelTemplates.FLAT_ITEM);
//
//        itemModels.generateLayeredItem(ItemRegistry.THICK_AIR_BUCKET.get(), mcLoc("item/bucket"))
//                .texture("layer1", mcLoc("item/firework_star_overlay"));
//
//        // There's a special (Neo)Forge model type for bucket items.
//        // More NeoForge convenience models can be found at neoforge-21.1.[XXX]-merged.jar/assets/neoforge/models.
//        getBuilder(ItemRegistry.CONFIGURABLE_FLUID_BUCKET.getRegisteredName())
//                .parent(getExistingFile(Identifier.fromNamespaceAndPath("neoforge","item/bucket")))
//                .customLoader(DynamicFluidContainerModelBuilder::begin)
//                .applyFluidLuminosity(true)
//                .coverIsMask(false)
//                // Change if the density is negative - really should be dynamically determined. #BlameNeoForge
//                .flipGas(false)
//                .fluid(FluidRegistries.FunFluids.CONFIGURABLE_FLUID.get())
//                // Set to false to remind us that this field is ignored. We must
//                // register a TintHandler using RegisterColorHandlersEvent.Item
//                // or the fluid will be untinted. #BlameThe(Neo)ForgeTeam
//                .applyTint(false);
//                // If you need a reference to the model file, you can add #end to the end of this call chain.
//        getBuilder(ItemRegistry.RIVER_OF_TIME_BUCKET.getRegisteredName())
//                .parent(getExistingFile(Identifier.fromNamespaceAndPath("neoforge", "item/bucket")))
//                .customLoader(DynamicFluidContainerModelBuilder::begin)
//                .applyFluidLuminosity(true)
//                .coverIsMask(false)
//                .flipGas(false)
//                .fluid(FluidRegistries.FunFluids.RIVER_OF_TIME_FLUID.get());
//
//        getBuilder(ItemRegistry.FLOOD_BUCKET.getRegisteredName())
//                .parent(getExistingFile(Identifier.fromNamespaceAndPath("neoforge", "item/bucket_drip")))
//                .customLoader(DynamicFluidContainerModelBuilder::begin)
//                .applyFluidLuminosity(true)
//                .coverIsMask(false)
//                .flipGas(false)
//                .fluid(FluidRegistries.FunFluids.FLOOD_FLUID.get())
//                .end()
//                .texture("cover", mcLoc("item/barrier"));
    }
}

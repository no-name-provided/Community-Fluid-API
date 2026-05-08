package com.github.no_name_provided.fun_fluids.datagen.providers;

import com.github.no_name_provided.fun_fluids.common.fluids.registries.BlockRegistry;
import com.github.no_name_provided.fun_fluids.common.fluids.registries.FluidRegistries;
import com.github.no_name_provided.fun_fluids.common.fluids.registries.ItemRegistry;
import com.mojang.logging.annotations.MethodsReturnNonnullByDefault;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.Holder;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.client.model.item.DynamicFluidContainerModel;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;
import java.util.stream.Stream;

@MethodsReturnNonnullByDefault @ParametersAreNonnullByDefault
public class FFModelProvider extends ModelProvider {
    public FFModelProvider(PackOutput output, String modid) {
        super(output, modid);
    }

//    /**
//     * Returns a {@link Stream stream} containing all {@link Block blocks} which must have their models/block states
//     * generated or {@link Stream#empty() empty} if none are desired.
//     * <p>
//     * When using providers for specific {@link Block block} usages, it is best to override this method returning the
//     * exact {@link Block blocks} which must be generated, or {@link Stream#empty() empty} if generating only
//     * {@link Item item} models.
//     * <p>
//     * Default implementation generates models for {@link Block blocks} matching the given {@code modId}.
//     *
//     * @see #getKnownItems()
//     */
//    @Override
//    protected Stream<? extends Holder<Block>> getKnownBlocks() {
//        return BlockRegistry.SOLID_BLOCKS.getEntries().stream();
//    }

//    /**
//     * Returns a {@link Stream stream} containing all {@link Item items} which must have their models/client items
//     * generated or {@link Stream#empty() empty} if none are desired.
//     * <p>
//     * When using providers for specific {@link Item item} usages, it is best to override this method returning the
//     * exact {@link Item items} which must be generated, or {@link Stream#empty() empty} if generating only
//     * {@link Block block} models (which have no respective {@link Item item}).
//     * <p>
//     * Default implementation generates models for {@link Item items} matching the given {@code modId}.
//     *
//     * @see #getKnownBlocks()
//     */
//    @Override
//    protected Stream<? extends Holder<Item>> getKnownItems() {
//        return ItemRegistry.ITEMS.getEntries().stream();
//    }
    
    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
//        blockModels.copyModel(Blocks.LAVA_CAULDRON, BlockRegistry.COOL_LAVA_CAULDRON.get());
        blockModels.createTrivialCube(BlockRegistry.COOL_LAVA_CAULDRON.get());
        // Not quite right... wrong parent
//        blockModels.createTrivialCube(BlockRegistry.THICK_AIR_BLOCK.get());
        
        BlockRegistry.FLUID_BLOCKS.getEntries().forEach(fluidBlock -> {
            blockModels.createTrivialCube(fluidBlock.get());
        });
        
        itemModels.generateFlatItem(ItemRegistry.COOL_LAVA_BUCKET.get(), Items.LAVA_BUCKET, ModelTemplates.FLAT_ITEM);
        
        itemModels.itemModelOutput.accept(ItemRegistry.THICK_AIR_BUCKET.get(), ItemModelUtils.plainModel(
                itemModels.generateLayeredItem(
                        ItemRegistry.THICK_AIR_BUCKET.get(),
                        new Material(mcLocation("item/bucket")),
                        new Material(mcLocation("item/firework_star_overlay"))
                )));
        
        // There's a special (Neo)Forge model type for bucket items.
        // More NeoForge convenience models can be found at neoforge-21.1.[XXX]-merged.jar/assets/neoforge/models.
        itemModels.itemModelOutput.accept(
                ItemRegistry.CONFIGURABLE_FLUID_BUCKET.get(),
                new DynamicFluidContainerModel.Unbaked(
                        new DynamicFluidContainerModel.Textures(
                                Optional.of(new Material(mcLocation("item/bucket"))),
                                Optional.of(new Material(mcLocation("item/bucket"))),
                                Optional.of(new Material(Identifier.fromNamespaceAndPath("neoforge", "item/mask/bucket_fluid"))),
                                Optional.of(new Material(Identifier.fromNamespaceAndPath("neoforge", "item/mask/bucket_fluid_cover")))
                        ),
                        FluidRegistries.FunFluids.CONFIGURABLE_FLUID.get(),
                        false,
                        true,
                        true
                )
        );
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
        itemModels.itemModelOutput.accept(
                ItemRegistry.RIVER_OF_TIME_BUCKET.get(),
                new DynamicFluidContainerModel.Unbaked(
                        new DynamicFluidContainerModel.Textures(
                                Optional.of(new Material(mcLocation("item/bucket"))),
                                Optional.of(new Material(mcLocation("item/bucket"))),
                                Optional.of(new Material(Identifier.fromNamespaceAndPath("neoforge", "item/mask/bucket_fluid"))),
                                Optional.of(new Material(Identifier.fromNamespaceAndPath("neoforge", "item/mask/bucket_fluid_cover")))
                        ),
                        FluidRegistries.FunFluids.RIVER_OF_TIME_FLUID.get(),
                        false,
                        true,
                        true
                )
        );
//        getBuilder(ItemRegistry.RIVER_OF_TIME_BUCKET.getRegisteredName())
//                .parent(getExistingFile(Identifier.fromNamespaceAndPath("neoforge", "item/bucket")))
//                .customLoader(DynamicFluidContainerModelBuilder::begin)
//                .applyFluidLuminosity(true)
//                .coverIsMask(false)
//                .flipGas(false)
//                .fluid(FluidRegistries.FunFluids.RIVER_OF_TIME_FLUID.get());
        itemModels.itemModelOutput.accept(
                ItemRegistry.FLOOD_BUCKET.get(),
                new DynamicFluidContainerModel.Unbaked(
                        new DynamicFluidContainerModel.Textures(
                                Optional.of(new Material(mcLocation("item/bucket"))),
                                Optional.of(new Material(mcLocation("item/bucket"))),
                                Optional.of(new Material(Identifier.fromNamespaceAndPath("neoforge", "item/mask/bucket_fluid"))),
                                Optional.of(new Material(Identifier.fromNamespaceAndPath("neoforge", "item/mask/bucket_fluid_cover")))
                        ),
                        FluidRegistries.FunFluids.FLOOD_FLUID.get(),
                        false,
                        true,
                        true
                )
        );
//        getBuilder(ItemRegistry.FLOOD_BUCKET.getRegisteredName())
//                .parent(getExistingFile(Identifier.fromNamespaceAndPath("neoforge", "item/bucket_drip")))
//                .customLoader(DynamicFluidContainerModelBuilder::begin)
//                .applyFluidLuminosity(true)
//                .coverIsMask(false)
//                .flipGas(false)
//                .fluid(FluidRegistries.FunFluids.FLOOD_FLUID.get())
//                .end()
//                .texture("cover", mcLoc("item/barrier"));
        // Actually run the generators
//        super.registerModels(blockModels, itemModels);
    }
}

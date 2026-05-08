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
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.client.model.item.DynamicFluidContainerModel;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;

import static net.minecraft.client.data.models.BlockModelGenerators.createSimpleBlock;
import static net.minecraft.client.data.models.BlockModelGenerators.plainVariant;

/**
 * Not only have our convenience methods generally been removed, or made more verbose, we're now forced to put all our
 * model generation in a single file.
 */
@MethodsReturnNonnullByDefault @ParametersAreNonnullByDefault
public class FFModelProvider extends ModelProvider {
    public FFModelProvider(PackOutput output, String modid) {
        super(output, modid);
    }
    
    /**
     * Whatever you do here, do <i>not</i> call super. That will trigger a check for all the vanilla items.
     *
     * @param blockModels Access to the BlockModelGenerator.
     * @param itemModels  Access to the ItemModelGenerator.
     */
    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        // blockModels#copyModel has problems, so we instead copy the vanilla datagen for lava cauldrons
        blockModels.blockStateOutput
                .accept(
                        createSimpleBlock(
                                BlockRegistry.COOL_LAVA_CAULDRON.get(),
                                plainVariant(
                                        ModelTemplates.CAULDRON_FULL
                                                .create(BlockRegistry.COOL_LAVA_CAULDRON.get(), TextureMapping.cauldron(TextureMapping.getBlockTexture(Blocks.LAVA, "_still")), blockModels.modelOutput)
                                )
                        )
                );
        // These aren't used, so they can be anything. We just need them to trick the datagen safeguards
        // into getting out of our way and suppress vacuous errors in the log. As an alternative, consider
        // overwriting #getKnownBlocks and #getKnownItems.
        BlockRegistry.FLUID_BLOCKS.getEntries().forEach(fluidBlock ->
                blockModels.createTrivialCube(fluidBlock.get())
        );
        // Generate a simple bucket, copying an existing texture
        itemModels.generateFlatItem(ItemRegistry.COOL_LAVA_BUCKET.get(), Items.LAVA_BUCKET, ModelTemplates.FLAT_ITEM);
        // Making dynamic buckets is a bit different in 26.1, and the docs are currently incorrect. This mostly works, but results in a weird outline
        itemModels.itemModelOutput.accept(ItemRegistry.THICK_AIR_BUCKET.get(), ItemModelUtils.plainModel(
                itemModels.generateLayeredItem(
                        ItemRegistry.THICK_AIR_BUCKET.get(),
                        new Material(mcLocation("item/bucket")),
                        new Material(mcLocation("item/firework_star_overlay"))
                )));
        
        // There's a special (Neo)Forge model type for bucket items.
        // More NeoForge convenience assets can be found in your External Libraries folder at net.neoforged\neoforge\26.1.2.42-beta\445b8c102926c24111540bf66f7a2572f7d1638a\neoforge-26.1.2.42-beta-universal.jar!\assets\neoforge\.
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
                        false
                )
        );
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
    }
}

package com.github.no_name_provided.fun_fluids.datagen.providers;

import com.github.no_name_provided.fun_fluids.common.fluids.registries.BlockRegistry;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Blocks;

public class FFBlockModelProvider extends ModelProvider {
    public FFBlockModelProvider(PackOutput output, String modid) {
        super(output, modid);
    }
    
    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        blockModels.createNonTemplateModelBlock(BlockRegistry.COOL_LAVA_CAULDRON.get(), Blocks.LAVA_CAULDRON);
        //        withExistingParent("cool_lava_cauldron", mcLoc("block/lava_cauldron"));
//        withExistingParent("cool_lava_block", mcLoc("block/lava"));

        // Not quite right... wrong parent
        blockModels.createTrivialCube(BlockRegistry.THICK_AIR_BLOCK.get());
//        cubeAll("thick_air_block", modLoc("block/transparent"));
        super.registerModels(blockModels, itemModels);
    }
}

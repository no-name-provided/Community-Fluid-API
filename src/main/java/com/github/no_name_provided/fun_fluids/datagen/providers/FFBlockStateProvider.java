//package com.github.no_name_provided.fun_fluids.datagen.providers;
//
//import net.minecraft.data.PackOutput;
//import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
//import net.neoforged.neoforge.client.model.generators.*;

import static com.github.no_name_provided.fun_fluids.common.fluids.registries.BlockRegistry.*;

///**
// * Note: import the NeoForge BlockStateProvider.
// */
//public class FFBlockStateProvider extends BlockStateProvider {
//    public FFBlockStateProvider(PackOutput output, String modid) {
//        super(output, modid);
//    }
//
//    @Override protected void registerStatesAndModels() {
//        simpleBlock(
//                COOL_LAVA_CAULDRON.get(),
//                models().getExistingFile(mcLoc("lava_cauldron"))
//        );

//        //Not necessary. Eliminates some undefined/missing blockstate warnings.
//        simpleBlock(
//                COOL_LAVA_BLOCK.get(),
//                models().getExistingFile(mcLoc("lava"))
//        );
//        simpleBlock(
//                THICK_AIR_BLOCK.get(),
//                models().cubeAll("thick_air", modLoc("block/transparent"))
//        );
//        simpleBlock(
//                CONFIGURABLE_FLUID_BLOCK.get(),
//                models().getExistingFile(mcLoc("water"))
//        );
//        simpleBlock(
//                RIVER_OF_TIME_BLOCK.get(),
//                models().getExistingFile(mcLoc("water"))
//        );
//        simpleBlock(
//                FLOOD_BLOCK.get(),
//                models().getExistingFile(mcLoc("water"))
//        );
//    }
//}

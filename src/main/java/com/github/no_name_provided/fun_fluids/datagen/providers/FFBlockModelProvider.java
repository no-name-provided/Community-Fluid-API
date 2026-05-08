//package com.github.no_name_provided.fun_fluids.datagen.providers;
//
//import net.minecraft.data.PackOutput;
//import net.neoforged.neoforge.client.model.generators.BlockModelProvider;
//import net.neoforged.neoforge.common.data.ExistingFileHelper;
//
//public class FFBlockModelProvider extends BlockModelProvider {
//    public FFBlockModelProvider(PackOutput output, String modid, ExistingFileHelper existingFileHelper) {
//        super(output, modid, existingFileHelper);
//    }
//
//    @Override
//    protected void registerModels() {
//        withExistingParent("cool_lava_cauldron", mcLoc("block/lava_cauldron"));
//        withExistingParent("cool_lava_block", mcLoc("block/lava"));
//
//        cubeAll("thick_air_block", modLoc("block/transparent"));
//    }
//}

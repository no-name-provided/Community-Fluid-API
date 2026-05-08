package com.github.no_name_provided.fun_fluids.datagen;

import com.github.no_name_provided.fun_fluids.datagen.providers.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.Locale;
import java.util.concurrent.CompletableFuture;

import static com.github.no_name_provided.fun_fluids.FunFluids.MODID;

///**
// * All data (json) generators need to be "registered" here. For convenience,
// * the providers are defined in separate classes.
// * <p>
// * Note: this is the "basic" approach. If your datagen classes need to share information,
// * you'll need to add a few lines or use methods that bypass the ExistingFileHelper (the "helper" bit is a misnomer).
// * */
//@EventBusSubscriber(modid = MODID)
//public class Generators {
//    @SubscribeEvent
//    public static void onGatherData(GatherDataEvent.Client event) {
//        DataGenerator generator = event.getGenerator();
//        PackOutput packOutput = generator.getPackOutput();
//        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
//
//        // If it's a server resource (goes in the data folder) include the server.
//        generator.addProvider(event.includeDev(), new FFRecipeProvider((HolderLookup.Provider) lookupProvider, (RecipeOutput) packOutput));
//
//        // If it's a client resource (goes in the assets folder) include the client.
//        generator.addProvider(event.includeDev(), new FFBlockModelProvider(packOutput, MODID));
//        generator.addProvider(event.includeDev(), new FFItemModelProvider(packOutput, MODID));
//        generator.addProvider(event.includeDev(), new FFBlockStateProvider(packOutput, MODID));
//        generator.addProvider(event.includeDev(), new FFLanguageProvider(packOutput, MODID, Locale.US.toString().toLowerCase()));
//        generator.addProvider(event.includeDev(), new FFParticleDescriptionProvider(packOutput));
//    }
//}

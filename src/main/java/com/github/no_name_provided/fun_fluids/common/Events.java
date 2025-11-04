package com.github.no_name_provided.fun_fluids.common;

import com.github.no_name_provided.fun_fluids.client.particles.MistParticle;
import com.github.no_name_provided.fun_fluids.client.registries.ParticleRegistry;
import com.github.no_name_provided.fun_fluids.common.fluids.registries.BlockRegistry;
import com.github.no_name_provided.fun_fluids.common.fluids.registries.FluidRegistries;
import com.github.no_name_provided.fun_fluids.common.fluids.registries.ItemRegistry;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;
import net.neoforged.neoforge.fluids.FluidInteractionRegistry;
import net.neoforged.neoforge.fluids.RegisterCauldronFluidContentEvent;
import net.neoforged.neoforge.fluids.capability.wrappers.FluidBucketWrapper;

import static com.github.no_name_provided.fun_fluids.FunFluids.MODID;

@EventBusSubscriber(modid = MODID)
public class Events {
    @SubscribeEvent
    static void registerCauldronFluidContent(RegisterCauldronFluidContentEvent event) {
        event.register(
                BlockRegistry.COOL_LAVA_CAULDRON.get(),
                FluidRegistries.FunFluids.COOL_LAVA.get(),
                1000,
                null
        );
    }

    /**
     * Runs during common setup.
     * <p>
     * This is where we register all fluid interactions that don't involve falling. Arbitrary, I know.
     * Suggested by ChiefArug.
     * </p>
     *
     */
    @SubscribeEvent
    static void onCommonSetup(FMLCommonSetupEvent event) {
        // They seem to work if we "register" them here, so we might as well. For whatever reason, the (Neo)Forge team
        // decided to use a bespoke "registry" (not deferred) with a basic synchronized static addition method.
        // Something about registries not handling "arbitrary obj -> thing" mappings.
        // This may cause lag if a bunch of mods try to add fluid interactions at the same time.
        FluidInteractionRegistry.addInteraction(
                FluidRegistries.FunFluidTypes.COOL_LAVA.get(),
                new FluidInteractionRegistry.InteractionInformation(
                        NeoForgeMod.WATER_TYPE.value(),
                        fluidState -> fluidState.isSource() ? Blocks.OBSIDIAN.defaultBlockState() : Blocks.COBBLESTONE.defaultBlockState())
        );
        FluidInteractionRegistry.addInteraction(
                FluidRegistries.FunFluidTypes.COOL_LAVA.get(),
                new FluidInteractionRegistry.InteractionInformation(
                        (level, currentPos, relativePos, currentState) -> level.getBlockState(currentPos.below()).is(Blocks.SOUL_SOIL) && level.getBlockState(relativePos).is(Blocks.BLUE_ICE),
                        Blocks.BASALT.defaultBlockState())
        );
    }

    /**
     * We only need to add this because we aren't using a vanilla BucketItem.
     * It's normally automatically added by NeoForge.
     * */
    @SubscribeEvent
    static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        event.registerItem(
                Capabilities.FluidHandler.ITEM,
                (stack, ctx) -> new FluidBucketWrapper(stack),
                ItemRegistry.COOL_LAVA_BUCKET.get()
        );
    }

    /**
     * Just part of the thick air fancy rendering. Not really fluid code.
     * */
    @SubscribeEvent
    static void registerParticleProviders(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ParticleRegistry.MIST_PARTICLE.get(), MistParticle.Provider::new);
    }
}

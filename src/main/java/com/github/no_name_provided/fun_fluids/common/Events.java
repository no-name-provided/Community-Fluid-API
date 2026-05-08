package com.github.no_name_provided.fun_fluids.common;

import com.github.no_name_provided.fun_fluids.client.particles.MistParticle;
import com.github.no_name_provided.fun_fluids.client.registries.ParticleRegistry;
import com.github.no_name_provided.fun_fluids.common.blocks.CoolLavaCauldronBlock;
import com.github.no_name_provided.fun_fluids.common.fluids.registries.BlockRegistry;
import com.github.no_name_provided.fun_fluids.common.fluids.registries.FluidRegistries;
import com.github.no_name_provided.fun_fluids.common.fluids.registries.ItemRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.event.RegisterCauldronInteractionEvent;
import net.neoforged.neoforge.fluids.FluidInteractionRegistry;
import net.neoforged.neoforge.fluids.RegisterCauldronFluidContentEvent;
import net.neoforged.neoforge.transfer.fluid.BucketResourceHandler;

import static com.github.no_name_provided.fun_fluids.FunFluids.MODID;
import static com.github.no_name_provided.fun_fluids.common.blocks.CoolLavaCauldronBlock.COOL_LAVA_INTERACTION_DISPATCHER;

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
     * This is where we register all fluid interactions that don't involve falling. Arbitrary, I know. Suggested by
     * ChiefArug.
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
//         Needs to be called here, due to an initialization order issue that
//         <i>appears</i> to not be present in vanilla (and doesn't affect
//         interactions with vanilla items) #BlameTheNeoForgeTeam
//        event.enqueueWork(() -> CoolLavaCauldronBlock.addCoolLavaCauldronInteractions(CoolLavaCauldronBlock.COOL_LAVA));
    }
    
    @SubscribeEvent
    static void onRegisterCauldronInteractions(RegisterCauldronInteractionEvent.Dispatcher event) {
        // We need a custom dispatcher because we return a unique bucket of fluid when our custom cualdron is
        // interacted with by a bucket
        event.register(
                Identifier.fromNamespaceAndPath(MODID, "fun_fluids"),
                COOL_LAVA_INTERACTION_DISPATCHER
        );
    }
    
    @SubscribeEvent
    static void onRegisterCauldronInteractions(RegisterCauldronInteractionEvent.Interaction event) {
        event.registerToAll(
                ItemRegistry.COOL_LAVA_BUCKET.get(),
                (BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, ItemStack itemInHand) ->
                        InteractionResult.SUCCESS.heldItemTransformedTo(Items.BUCKET.getDefaultInstance())
        );
        // Needs more work to bring in line with vanilla
        event.register(
                Identifier.fromNamespaceAndPath(MODID, "fun_fluids"),
                Items.BUCKET,
                (BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, ItemStack itemInHand) ->
                        InteractionResult.SUCCESS.heldItemTransformedTo(ItemRegistry.COOL_LAVA_BUCKET.get().getDefaultInstance())
        );
    }
    
    /**
     * We only need to add this because we aren't using a vanilla BucketItem. It's normally automatically added by
     * NeoForge.
     *
     */
    @SubscribeEvent
    static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        event.registerItem(
                Capabilities.Fluid.ITEM,
                (stack, ctx) -> new BucketResourceHandler(ctx.oneByOne()),
                ItemRegistry.COOL_LAVA_BUCKET.get()
        );
    }
    
    /**
     * Just part of the thick air fancy rendering. Not really fluid code.
     *
     */
    @SubscribeEvent
    static void registerParticleProviders(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ParticleRegistry.MIST_PARTICLE.get(), MistParticle.Provider::new);
    }
}

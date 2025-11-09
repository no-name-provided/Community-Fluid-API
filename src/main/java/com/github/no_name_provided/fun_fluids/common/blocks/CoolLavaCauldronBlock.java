package com.github.no_name_provided.fun_fluids.common.blocks;

import com.github.no_name_provided.fun_fluids.common.fluids.registries.FluidRegistries;
import com.github.no_name_provided.fun_fluids.common.fluids.registries.ItemRegistry;
import com.mojang.serialization.MapCodec;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.AbstractCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.SoundActions;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Each type of fluid that can be placed in a cauldron is represented by a separate block. If you want cauldrons to hold
 * your fluids, you'll need to jump through some hoops.
 */
@ParametersAreNonnullByDefault @MethodsReturnNonnullByDefault
public class CoolLavaCauldronBlock extends AbstractCauldronBlock {

    public static final MapCodec<CoolLavaCauldronBlock> CODEC = simpleCodec(CoolLavaCauldronBlock::new);
    public static final CauldronInteraction.InteractionMap COOL_LAVA = CauldronInteraction.newInteractionMap("cool_lava");

    @Override
    public MapCodec<CoolLavaCauldronBlock> codec() {
        return CODEC;
    }

    @Override
    public boolean isFull(BlockState state) {
        return true;
    }

    public CoolLavaCauldronBlock(Properties properties) {
        super(properties, COOL_LAVA);
    }

    /**
     * We can't add capabilities checks to a vanilla bucket (without mixins or shenanigans), so we
     * create an interaction map instead. A similar technique (with default cauldron interactions)
     * could probably be used to replace the fluid capability check in BetterBucketItem.
     */
    public static void addCoolLavaCauldronInteractions(CauldronInteraction.InteractionMap interactionMap) {
        // These are normally added by net.minecraft.core.cauldron.CauldronInteraction#addDefaultInteractions,
        // but that's package-private. These are why you can replace the fluid content of already filled cauldrons
        interactionMap.map().put(Items.LAVA_BUCKET, CauldronInteraction.FILL_LAVA);
        interactionMap.map().put(Items.WATER_BUCKET, CauldronInteraction.FILL_WATER);
        interactionMap.map().put(Items.POWDER_SNOW_BUCKET, CauldronInteraction.FILL_POWDER_SNOW);
        // Fill empty buckets
        interactionMap.map().put(Items.BUCKET,  (state, level, pos, player, hand, stack) ->
                CauldronInteraction.fillBucket(
                        state,
                        level,
                        pos,
                        player,
                        hand,
                        stack,
                        new ItemStack(ItemRegistry.COOL_LAVA_BUCKET),
                        // Accepts the current blockstate and allows you to conditionally skip processing
                        // Vanilla uses this to check the LEVEL of LayerCauldronBlocks and make sure they have enough fluid
                        other_state -> true,
                        SoundEvents.BUCKET_FILL_LAVA
                )
        );
        // Allow our fluid to be replaced with... itself, as done by vanilla.
        SoundEvent sound = FluidRegistries.FunFluidTypes.COOL_LAVA.get().getSound(SoundActions.BUCKET_EMPTY);
        interactionMap.map().put(ItemRegistry.COOL_LAVA_BUCKET.get(),
                (state, level, pos, player, hand, stack) ->
                CauldronInteraction.emptyBucket(
                        level,
                        pos,
                        player,
                        hand,
                        stack,
                        state,
                        // We know this isn't null, but the IDE doesn't, and you might mess up your implementation at some point
                        null == sound ? SoundEvents.BUCKET_EMPTY_LAVA : sound
                )
        );
    }
}

package com.github.no_name_provided.fun_fluids.common.items;

import com.mojang.logging.annotations.MethodsReturnNonnullByDefault;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;

import javax.annotation.ParametersAreNonnullByDefault;

import static net.minecraft.world.item.Items.BUCKET;

/**
 * This is where we add some compatibility code for things like cauldrons. For whatever reason (#Blame the NeoForged
 * team), BucketItems don't check for fluid capabilities and cauldrons use an inflexible interaction scheme that
 * <i>also</i> doesn't check for capabilities.
 * <p>
 * As an alternative, consider enqueuing a call to CauldronInteraction#addDefaultInteractions in common setup.
 * </p>
 * <p>
 * Also sets basic parameters, like stack size and the crafting remainder.
 *
 */
@ParametersAreNonnullByDefault @MethodsReturnNonnullByDefault
public class BetterBucketItem extends BucketItem {
    public BetterBucketItem(Fluid content, Properties properties, Identifier iD) {
        this(content, properties.setId(ResourceKey.create(Registries.ITEM, iD)), true);
    }
    
    public BetterBucketItem(Fluid content, Properties properties, boolean setBasicProperties) {
        super(
                content,
                setBasicProperties ? properties.craftRemainder(BUCKET).stacksTo(1) : properties
        );
    }
    
    /**
     * Special case cauldron interactions and use their fluid handler.
     */
    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
//        BlockHitResult result = getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE);
//        if (result.getType() == HitResult.Type.BLOCK && level.getBlockState(result.getBlockPos()).is(Blocks.CAULDRON)) {
//            //We use the autocloseable here - generally a bad idea in Minecraft, but these transactions are screwy, undocumented sources of instability
//            try {
//                Transaction root = Transaction.openRoot();
//                ResourceHandler<FluidResource> handler = level.getCapability(Capabilities.Fluid.BLOCK, result.getBlockPos(), Blocks.CAULDRON.defaultBlockState(), null, null);
//                if (handler != null) {
//                    handler.insert(FluidResource.of(new FluidStack(content, 1000)), 1000, root);
//                }
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//            player.awardStat(Stats.USE_CAULDRON);
//            player.awardStat(Stats.ITEM_USED.get(player.getItemInHand(hand).getItem()));
//            level.playSound(null, result.getBlockPos(), SoundEvents.BUCKET_FILL_LAVA, SoundSource.BLOCKS, 1.0F, 1.0F);
//            level.gameEvent(null, GameEvent.FLUID_PICKUP, result.getBlockPos());
//            return InteractionResult.CONSUME.heldItemTransformedTo(player.isCreative() ? player.getItemInHand(hand) : new ItemStack(Items.BUCKET));
//        } else {
            return super.use(level, player, hand);
//        }
    }
}

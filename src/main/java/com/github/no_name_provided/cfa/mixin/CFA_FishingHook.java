package com.github.no_name_provided.cfa.mixin;

import com.github.no_name_provided.cfa.mixin_interfaces.IFluidTypeExtension;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.logging.Logger;

import static com.github.no_name_provided.cfa.CommunityFluidAPI.MODID;

@Mixin(FishingHook.class)
abstract class CFA_FishingHook extends Projectile {
    
    protected CFA_FishingHook(EntityType<? extends Projectile> type, Level level) {
        super(type, level);
    }
    
    @ModifyExpressionValue(method = "tick()V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/material/FluidState;is(Lnet/minecraft/tags/TagKey;)Z"))
    private boolean cfa_tick_fixFluidStateCheck(boolean original, @Local(name = "fluidState") FluidState fluidState) {
        return original || ((IFluidTypeExtension) fluidState.getFluidType()).canFish();
    }
    
    @ModifyArg(method = "retrieve(Lnet/minecraft/world/item/ItemStack;)I",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/server/ReloadableServerRegistries$Holder;getLootTable(Lnet/minecraft/resources/ResourceKey;)Lnet/minecraft/world/level/storage/loot/LootTable;"))
    private ResourceKey<LootTable> cfa_retrieve_fixLootTable(ResourceKey<LootTable> id) {
        FishingHook hook = (FishingHook) (Object) this;
        // This should never be null, because fluids are checked beforehand, but the check is cheap and modders mess up
        ResourceKey<LootTable> key = ((IFluidTypeExtension) hook.getInBlockState().getFluidState().getFluidType()).getFishingLootTableKey();
        if (key == null) {
            Logger.getLogger(MODID).warning("Missing loot table key for fluid. Defaulting to BuiltInLootTables.FISHING.");
            return BuiltInLootTables.FISHING;
        } else {
            return key;
        }
    }
    
    @ModifyExpressionValue(method = "catchingFish(Lnet/minecraft/core/BlockPos;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Ljava/lang/Object;)Z"))
    private boolean cfa_catchingFish_fixBlockStateCheck(boolean original, @Local(name = "splashBlockState") BlockState splashBlockState) {
        return original || ((IFluidTypeExtension) splashBlockState.getFluidState().getFluidType()).canFish();
    }
    
    @ModifyExpressionValue(method = "getOpenWaterTypeForBlock(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/entity/projectile/FishingHook$OpenWaterType;",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/material/FluidState;is(Lnet/minecraft/tags/TagKey;)Z"))
    private boolean cfa_getOpenWaterTypeForBlock_fixFluidStateCheck(boolean original, @Local(name = "fluidState") FluidState fluidState) {
        return original || ((IFluidTypeExtension) fluidState.getFluidType()).canFish();
    }
    
    
}

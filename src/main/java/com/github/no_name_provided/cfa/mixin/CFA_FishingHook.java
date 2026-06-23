package com.github.no_name_provided.cfa.mixin;

import com.github.no_name_provided.cfa.mixin_interfaces.IFluidTypeExtension;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
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
import org.spongepowered.asm.mixin.injection.Redirect;

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
        return original || ((IFluidTypeExtension) fluidState.getFluidType()).canFish(level(), blockPosition());
    }
    
    @ModifyArg(method = "retrieve(Lnet/minecraft/world/item/ItemStack;)I",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/server/ReloadableServerRegistries$Holder;getLootTable(Lnet/minecraft/resources/ResourceKey;)Lnet/minecraft/world/level/storage/loot/LootTable;"))
    private ResourceKey<LootTable> cfa_retrieve_fixLootTable(ResourceKey<LootTable> id) {
        FishingHook hook = (FishingHook) (Object) this;
        // This should never be null, because fluids are checked beforehand, but the check is cheap and modders mess up
        ResourceKey<LootTable> key = ((IFluidTypeExtension) hook.getInBlockState().getFluidState().getFluidType()).getFishingLootTableKey(level(), blockPosition());
        if (key == null) {
            Logger.getLogger(MODID).warning("Fluid [" + getInBlockState().getFluidState().getFluidType() + "] is missing loot table key for fluid. Defaulting to BuiltInLootTables.FISHING.");
            return BuiltInLootTables.FISHING;
        } else {
            return key;
        }
    }
    
    @ModifyExpressionValue(method = "catchingFish(Lnet/minecraft/core/BlockPos;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Ljava/lang/Object;)Z"))
    private boolean cfa_catchingFish_fixBlockStateCheck(boolean original, @Local(name = "splashBlockState") BlockState splashBlockState) {
        return original || ((IFluidTypeExtension) splashBlockState.getFluidState().getFluidType()).canFish(level(), blockPosition());
    }
    
    @Redirect(method = "catchingFish(Lnet/minecraft/core/BlockPos;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;sendParticles(Lnet/minecraft/core/particles/ParticleOptions;DDDIDDDD)I", ordinal = 5))
    private int cfa_catchingFish_redirectParticles(ServerLevel level, ParticleOptions particle, double x, double y, double z, int count, double xDist, double yDist, double zDist, double speed, @Local(name = "splashBlockState") BlockState splashBlockState) {
        return ((IFluidTypeExtension) splashBlockState.getFluidState().getFluidType()).createSplashParticleOnServer(
                splashBlockState.getFluidState().getType(),
                level,
                x,
                y,
                z,
                count,
                xDist,
                yDist,
                zDist,
                speed
        );
    }
    
    @Redirect(method = "catchingFish(Lnet/minecraft/core/BlockPos;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;sendParticles(Lnet/minecraft/core/particles/ParticleOptions;DDDIDDDD)I", ordinal = 1))
    private int cfa_catchingFish_redirectWakeParticles1(ServerLevel level, ParticleOptions particle, double x, double y, double z, int count, double xDist, double yDist, double zDist, double speed, @Local(name = "splashBlockState") BlockState splashBlockState) {
        return ((IFluidTypeExtension) splashBlockState.getFluidState().getFluidType()).createWakeParticleOnServer(
                splashBlockState.getFluidState().getType(),
                level,
                x,
                y,
                z,
                count,
                xDist,
                yDist,
                zDist,
                speed
        );
    }
    
    @Redirect(method = "catchingFish(Lnet/minecraft/core/BlockPos;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;sendParticles(Lnet/minecraft/core/particles/ParticleOptions;DDDIDDDD)I", ordinal = 2))
    private int cfa_catchingFish_redirectWakeParticles2(ServerLevel level, ParticleOptions particle, double x, double y, double z, int count, double xDist, double yDist, double zDist, double speed, @Local(name = "splashBlockState") BlockState splashBlockState) {
        return ((IFluidTypeExtension) splashBlockState.getFluidState().getFluidType()).createWakeParticleOnServer(
                splashBlockState.getFluidState().getType(),
                level,
                x,
                y,
                z,
                count,
                xDist,
                yDist,
                zDist,
                speed
        );
    }
    
    @ModifyExpressionValue(method = "getOpenWaterTypeForBlock(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/entity/projectile/FishingHook$OpenWaterType;",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/material/FluidState;is(Lnet/minecraft/tags/TagKey;)Z"))
    private boolean cfa_getOpenWaterTypeForBlock_fixFluidStateCheck(boolean original, @Local(name = "fluidState") FluidState fluidState) {
        return original || ((IFluidTypeExtension) fluidState.getFluidType()).canFish(level(), blockPosition());
    }
}

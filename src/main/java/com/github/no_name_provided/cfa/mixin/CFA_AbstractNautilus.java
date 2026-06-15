package com.github.no_name_provided.cfa.mixin;

import com.github.no_name_provided.cfa.mixin_interfaces.IFluidTypeExtension;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.nautilus.AbstractNautilus;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AbstractNautilus.class)
abstract class CFA_AbstractNautilus extends TamableAnimal implements PlayerRideableJumping, HasCustomInventoryScreen {
    protected CFA_AbstractNautilus(EntityType<? extends TamableAnimal> type, Level level) {
        super(type, level);
    }
    
    /**
     * Every Nautilus check is just for sound. It's probably safe to assume modders want to use underwater sounds for
     * swimmable fluids.
     *
     * @return If the mob is under any swimmable fluid.
     */
    @Override
    public boolean isUnderWater() {
        return super.isUnderWater() ||
                // Vanilla checks both fluid immersion and eye immersion separately...
                getLastFluid().canSwim(this) &&
                        fluidInteraction.isInFluid(((IFluidTypeExtension) getLastFluid()).getTag()) &&
                        fluidInteraction.isEyeInFluid(((IFluidTypeExtension) getLastFluid()).getTag());
    }
    
    @ModifyExpressionValue(method = "checkNautilusSpawnRules(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/LevelAccessor;Lnet/minecraft/world/entity/EntitySpawnReason;Lnet/minecraft/core/BlockPos;Lnet/minecraft/util/RandomSource;)Z",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/material/FluidState;is(Lnet/minecraft/tags/TagKey;)Z"))
    private static boolean cfa_checkNautilusSpawnRules_fixFluidStateCheck(boolean original, EntityType<? extends AbstractNautilus> type, LevelAccessor level, EntitySpawnReason spawnReason, BlockPos pos) {
        return original || ((IFluidTypeExtension)level.getFluidState(pos.below()).getFluidType()).canSpawnAquaticMobs(type);
    }
    
    @ModifyExpressionValue(method = "checkNautilusSpawnRules(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/LevelAccessor;Lnet/minecraft/world/entity/EntitySpawnReason;Lnet/minecraft/core/BlockPos;Lnet/minecraft/util/RandomSource;)Z",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Ljava/lang/Object;)Z"))
    private static boolean cfa_checkNautilusSpawnRules_fixBlockStateCheck(boolean original, EntityType<? extends AbstractNautilus> type, LevelAccessor level, EntitySpawnReason spawnReason, BlockPos pos) {
        // Vanilla uses a BlockState here for some reason...
        return original || ((IFluidTypeExtension)level.getBlockState(pos.above()).getFluidState().getFluidType()).canSpawnAquaticMobs(type);
    }
    
    @ModifyExpressionValue(method = "getRiddenSpeed(Lnet/minecraft/world/entity/player/Player;)F",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/nautilus/AbstractNautilus;isInWater()Z"))
    private boolean cfa_getRiddenSpeed(boolean original) {
        return original || this.canSwimInFluidType(getLastFluid());
    }
    
    @ModifyExpressionValue(method = "tick()V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/nautilus/AbstractNautilus;isInWater()Z"))
    private boolean cfa_tick(boolean original) {
        return original || this.canSwimInFluidType(getLastFluid());
    }
    
    @ModifyExpressionValue(method = "executeRidersJump(FLnet/minecraft/world/entity/player/Player;)V",
    at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/nautilus/AbstractNautilus;isInWater()Z"))
    private boolean cfa_executeRidersJump(boolean original) {
        return original || this.canSwimInFluidType(getLastFluid());
    }
}

package com.github.no_name_provided.cfa.mixin;

import com.github.no_name_provided.cfa.mixin_interfaces.IFluidTypeExtension;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.monster.zombie.Drowned;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Drowned.class)
abstract class CFA_Drowned extends Zombie implements RangedAttackMob {
    
    public CFA_Drowned(Level level) {
        super(level);
    }
    
    @ModifyExpressionValue(method = "checkDrownedSpawnRules(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/ServerLevelAccessor;Lnet/minecraft/world/entity/EntitySpawnReason;Lnet/minecraft/core/BlockPos;Lnet/minecraft/util/RandomSource;)Z",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/material/FluidState;is(Lnet/minecraft/tags/TagKey;)Z", ordinal = 0))
    private static boolean cfa_checkDrownedSpawnRules_fixBelowFluidStateCheck(boolean original, EntityType<Drowned> type, ServerLevelAccessor level, EntitySpawnReason spawnReason, BlockPos pos) {
        return original || ((IFluidTypeExtension) level.getFluidState(pos.below()).getFluidType()).canSpawnAquaticMobs(type);
    }
    
    @ModifyExpressionValue(method = "checkDrownedSpawnRules(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/ServerLevelAccessor;Lnet/minecraft/world/entity/EntitySpawnReason;Lnet/minecraft/core/BlockPos;Lnet/minecraft/util/RandomSource;)Z",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/material/FluidState;is(Lnet/minecraft/tags/TagKey;)Z", ordinal = 1))
    private static boolean cfa_checkDrownedSpawnRules_fixFluidStateCheck(boolean original, EntityType<Drowned> type, ServerLevelAccessor level, EntitySpawnReason spawnReason, BlockPos pos) {
        return original || ((IFluidTypeExtension) level.getFluidState(pos).getFluidType()).canSpawnAquaticMobs(type);
    }
    
    @ModifyExpressionValue(method = "getAmbientSound()Lnet/minecraft/sounds/SoundEvent;",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/monster/zombie/Drowned;isInWater()Z"))
    private boolean cfa_getAmbientSound(boolean original) {
        return original || ((IFluidTypeExtension) getLastFluid()).canSpawnAquaticMobs(getType());
    }
    
    @ModifyExpressionValue(method = "getHurtSound(Lnet/minecraft/world/damagesource/DamageSource;)Lnet/minecraft/sounds/SoundEvent;",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/monster/zombie/Drowned;isInWater()Z"))
    private boolean cfa_getHurtSound(boolean original) {
        return original || ((IFluidTypeExtension) getLastFluid()).canSpawnAquaticMobs(getType());
    }
    
    @ModifyExpressionValue(method = "getDeathSound()Lnet/minecraft/sounds/SoundEvent;",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/monster/zombie/Drowned;isInWater()Z"))
    private boolean cfa_getDeathSound(boolean original) {
        return original || ((IFluidTypeExtension) getLastFluid()).canSpawnAquaticMobs(getType());
    }
    
    @ModifyExpressionValue(method = "okTarget(Lnet/minecraft/world/entity/LivingEntity;)Z",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;isInWater()Z"))
    private boolean cfa_okTarget(boolean original) {
        return original || ((IFluidTypeExtension) getLastFluid()).preventsBurning(this);
    }
    
    @ModifyExpressionValue(method = "wantsToSwim()Z",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;isInWater()Z"))
    private boolean cfa_wantsToSwim(boolean original) {
        return original || ((IFluidTypeExtension) getLastFluid()).preventsBurning(this);
    }
    
    @ModifyExpressionValue(method = "travelInWater(Lnet/minecraft/world/phys/Vec3;DZD)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/monster/zombie/Drowned;isUnderWater()Z"))
    private boolean cfa_travelInWater(boolean original) {
        return original ||
                (
                        canSwimInFluidType(getLastFluid()) &&
                                isEyeInFluid(((IFluidTypeExtension) getLastFluid()).getTag())
                );
    }
    
    @ModifyExpressionValue(method = "updateSwimming()V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/monster/zombie/Drowned;isUnderWater()Z"))
    private boolean cfa_updateSwimming(boolean original) {
        return original ||
                (
                        canSwimInFluidType(getLastFluid()) &&
                                isEyeInFluid(((IFluidTypeExtension) getLastFluid()).getTag())
                );
    }
}

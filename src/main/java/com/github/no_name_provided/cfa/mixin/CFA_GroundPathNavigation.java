package com.github.no_name_provided.cfa.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.NeoForgeMod;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GroundPathNavigation.class)
abstract class CFA_GroundPathNavigation extends PathNavigation {
    
    public CFA_GroundPathNavigation(Mob mob, Level level) {
        super(mob, level);
    }
    
    @ModifyExpressionValue(method = "canUpdatePath()Z",
    at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Mob;isInLiquid()Z"))
    private boolean cfa_canUpdatePath(boolean original) {
        return !mob.getLastFluid().isAir();
    }
    
    @ModifyExpressionValue(method = "getSurfaceY()I",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Mob;isInWater()Z"))
    private boolean cfa_getSurfaceY_fixIsInWater(boolean original) {
        return mob.canSwimInFluidType(mob.getLastFluid()) && mob.getLastFluid() != NeoForgeMod.LAVA_TYPE.value();
    }
    
    @ModifyExpressionValue(method = "getSurfaceY()I",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Ljava/lang/Object;)Z"))
    private boolean cfa_getSurfaceY_fixBlockStateCheck(boolean original) {
        return mob.canSwimInFluidType(mob.getLastFluid()) && mob.getLastFluid() != NeoForgeMod.LAVA_TYPE.value();
    }
    
    
}

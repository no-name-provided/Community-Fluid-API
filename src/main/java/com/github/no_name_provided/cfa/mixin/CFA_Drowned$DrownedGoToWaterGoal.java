package com.github.no_name_provided.cfa.mixin;

import com.github.no_name_provided.cfa.mixin_interfaces.IFluidTypeExtension;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(targets = "net.minecraft.world.entity.monster.zombie.Drowned.DrownedGoToWaterGoal")
abstract class CFA_Drowned$DrownedGoToWaterGoal extends Goal {
    @Shadow @Final
    private PathfinderMob mob;
    
    @Shadow @Final
    private Level level;
    
    /**
     * If it can already swim, and is protected from light, it shouldn't run for cover.
     */
    @ModifyExpressionValue(method = "canUse()Z",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/PathfinderMob;isInWater()Z"))
    private boolean cfa_canUse(boolean original) {
        return original ||
                (
                        mob.canSwimInFluidType(mob.getLastFluid()) &&
                                ((IFluidTypeExtension) mob.getLastFluid()).preventsBurning(mob)
                );
    }
    
    @ModifyExpressionValue(method = "getWaterPos()Lnet/minecraft/world/phys/Vec3;",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Ljava/lang/Object;)Z"))
    private boolean cfa_getWaterPos(boolean original, @Local(name = "randomPos") BlockPos randomPos) {
        FluidType fluid = level.getFluidState(randomPos).getFluidType();
        return original || (
                mob.canSwimInFluidType(fluid) &&
                        ((IFluidTypeExtension) fluid).preventsBurning(mob)
        );
    }
}

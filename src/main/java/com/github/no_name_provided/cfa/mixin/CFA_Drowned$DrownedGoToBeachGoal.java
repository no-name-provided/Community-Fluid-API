package com.github.no_name_provided.cfa.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.monster.zombie.Drowned;
import net.neoforged.neoforge.common.NeoForgeMod;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(targets = "net.minecraft.world.entity.monster.zombie.Drowned.DrownedGoToBeachGoal")
abstract class CFA_Drowned$DrownedGoToBeachGoal extends MoveToBlockGoal {
    @Shadow @Final private Drowned drowned;
    
    public CFA_Drowned$DrownedGoToBeachGoal(PathfinderMob mob, double speedModifier, int searchRange) {
        super(mob, speedModifier, searchRange);
    }
    
    @ModifyExpressionValue(method = "canUse()Z",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/monster/zombie/Drowned;isInWater()Z"))
    private boolean cfa_wantsToSwim(boolean original) {
        return original || drowned.getLastFluid() != NeoForgeMod.EMPTY_TYPE.value();
    }
}

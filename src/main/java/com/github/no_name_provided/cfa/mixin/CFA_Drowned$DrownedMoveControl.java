package com.github.no_name_provided.cfa.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.monster.zombie.Drowned;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(targets = "net.minecraft.world.entity.monster.zombie.Drowned$DrownedMoveControl")
abstract class CFA_Drowned$DrownedMoveControl extends MoveControl {
    @Shadow @Final
    private Drowned drowned;
    
    public CFA_Drowned$DrownedMoveControl(Mob mob) {
        super(mob);
    }
    
    @ModifyExpressionValue(method = "tick()V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/monster/zombie/Drowned;isInWater()Z"))
    private boolean cfa_tick(boolean original) {
        return original || drowned.canSwimInFluidType(drowned.getLastFluid());
    }
    
    
}

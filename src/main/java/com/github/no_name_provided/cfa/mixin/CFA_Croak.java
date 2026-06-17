package com.github.no_name_provided.cfa.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.Croak;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.animal.frog.Frog;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Map;

@Mixin(Croak.class)
abstract class CFA_Croak extends Behavior<Frog> {
    
    public CFA_Croak(Map<MemoryModuleType<?>, MemoryStatus> entryCondition) {
        super(entryCondition);
    }
    
    @ModifyExpressionValue(method = "start(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/animal/frog/Frog;J)V",
    at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/frog/Frog;isInLiquid()Z"))
    private boolean cfa_start(boolean original, @Local(name = "body", argsOnly = true) Frog body) {
        return original || !body.getLastFluid().isAir();
    }
}

package com.github.no_name_provided.cfa.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.entity.animal.axolotl.PlayDead;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Map;

/**
 * Used (only) by Axolotl.
 */
@Mixin(PlayDead.class)
abstract class CFA_PlayDead extends Behavior<Axolotl> {
    public CFA_PlayDead(Map<MemoryModuleType<?>, MemoryStatus> entryCondition) {
        super(entryCondition);
    }
    
    @ModifyExpressionValue(method = "checkExtraStartConditions(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/animal/axolotl/Axolotl;)Z",
    at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/axolotl/Axolotl;isInWater()Z"))
    private boolean cfa_checkExtraStartConditions(boolean original, @Local(argsOnly = true, name = "body") Axolotl body) {
        return original || body.canSwimInFluidType(body.getLastFluid());
    }
    
    @ModifyExpressionValue(method = "canStillUse(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/animal/axolotl/Axolotl;J)Z",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/axolotl/Axolotl;isInWater()Z"))
    private boolean cfa_canStillUse(boolean original, @Local(argsOnly = true, name = "body") Axolotl body) {
        return original || body.canSwimInFluidType(body.getLastFluid());
    }
}

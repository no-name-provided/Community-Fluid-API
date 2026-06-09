package com.github.no_name_provided.cfa.mixin;

import com.github.no_name_provided.cfa.mixin_interfaces.IFluidTypeExtension;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.level.pathfinder.AmphibiousNodeEvaluator;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AmphibiousNodeEvaluator.class)
abstract class CFA_AmphibiousNodeEvaluator extends WalkNodeEvaluator {
    
    @ModifyExpressionValue(method = "getStart()Lnet/minecraft/world/level/pathfinder/Node;",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Mob;isInWater()Z"))
    private boolean cfa_getStart(boolean original) {
        return NeoForgeRegistries.FLUID_TYPES.stream().anyMatch(type ->
                mob.fluidInteraction.isInFluid(((IFluidTypeExtension) type).getTag()) && type.canSwim(mob)
        );
    }
}

package com.github.no_name_provided.cfa.mixin;

import com.github.no_name_provided.cfa.mixin_interfaces.IFluidTypeExtension;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(WaterAvoidingRandomStrollGoal.class)
abstract class CFA_WaterAvoidingRandomStrollGoal extends RandomStrollGoal {
    public CFA_WaterAvoidingRandomStrollGoal(PathfinderMob mob, double speedModifier) {
        super(mob, speedModifier);
    }
    
    @ModifyExpressionValue(method = "getPosition()Lnet/minecraft/world/phys/Vec3;",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/PathfinderMob;isInWater()Z"))
    private boolean cfa_getPosition(boolean original) {
        return original || NeoForgeRegistries.FLUID_TYPES.stream().anyMatch(type ->
                !type.isVanilla() && this.mob.fluidInteraction.isInFluid(((IFluidTypeExtension) type).getTag()));
    }
}

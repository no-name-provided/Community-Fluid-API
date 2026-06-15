package com.github.no_name_provided.cfa.mixin;

import com.github.no_name_provided.cfa.mixin_interfaces.IFluidTypeExtension;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.squid.Squid;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(targets = "net.minecraft.world.entity.animal.squid.Squid$SquidFleeGoal")
abstract class CFA_Squid$SquidFleeGoal extends Goal {
    // https://stackoverflow.com/a/76957829 - this is the usual name of the automatic reference to the containing class
    @Shadow @Final
    Squid this$0;
    
    @ModifyExpressionValue(method = "canUse()Z",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/squid/Squid;isInWater()Z"))
    public boolean cfa_canUse(boolean original) {
        return original || NeoForgeRegistries.FLUID_TYPES.stream().anyMatch(type ->
                !type.isVanilla() &&
                        type.canSwim(this$0) &&
                        this$0.fluidInteraction.isInFluid(((IFluidTypeExtension) type).getTag())
        );
    }
    
    @ModifyExpressionValue(method = "tick()V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/material/FluidState;is(Lnet/minecraft/tags/TagKey;)Z"))
    public boolean cfa_tick(boolean original, @Local(name = "fluidState") FluidState fluidState) {
        return original || fluidState.getFluidType().canSwim(this$0);
    }
}

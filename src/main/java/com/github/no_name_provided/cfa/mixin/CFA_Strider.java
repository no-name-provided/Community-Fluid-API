package com.github.no_name_provided.cfa.mixin;

import com.github.no_name_provided.cfa.mixin_interfaces.IFluidTypeExtension;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Strider;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import javax.annotation.ParametersAreNonnullByDefault;

@Mixin(Strider.class) @ParametersAreNonnullByDefault
abstract class CFA_Strider extends Animal {
    @Shadow
    protected abstract void floatStrider();
    
    @Shadow
    public abstract boolean canStandOnFluid(FluidState fluid);
    
    protected CFA_Strider(EntityType<? extends Animal> type, Level level) {
        super(type, level);
    }
    
    @ModifyReturnValue(method = "canStandOnFluid(Lnet/minecraft/world/level/material/FluidState;)Z",
            at = @At("RETURN"))
    private boolean cfa_canStandOnFluid(boolean original, @Local(name = "fluid", argsOnly = true) FluidState fluid) {
        return ((IFluidTypeExtension) fluid.getFluidType()).entityCanStandOn(this);
    }
    
    @ModifyExpressionValue(method = "floatStrider()V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/monster/Strider;isInLava()Z"))
    private boolean cfa_floatStrider(boolean original) {
        return original || NeoForgeRegistries.FLUID_TYPES.stream().anyMatch(type ->
                !type.isVanilla() &&
                        fluidInteraction.isInFluid(((IFluidTypeExtension) type).getTag()) &&
                        ((IFluidTypeExtension) type).entityCanStandOn(this)
        );
    }
    
    @ModifyExpressionValue(method = "floatStrider()V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/material/FluidState;is(Lnet/minecraft/tags/TagKey;)Z"))
    private boolean cfa_floatStrider_fixTagCheck(boolean original) {
        FluidType fluidType = level().getFluidState(blockPosition().above()).getFluidType();
        return original || !fluidType.isVanilla() &&
                ((IFluidTypeExtension) fluidType).entityCanStandOn(this);
    }
}

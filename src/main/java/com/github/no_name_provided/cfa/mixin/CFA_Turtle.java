package com.github.no_name_provided.cfa.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.turtle.Turtle;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Turtle.class)
abstract class CFA_Turtle extends Animal {
    @Shadow
    public static final TargetingConditions.Selector BABY_ON_LAND_SELECTOR = (target, level) -> target.isBaby() && target.getLastFluid().isAir();
    
    protected CFA_Turtle(EntityType<? extends Animal> type, Level level) {
        super(type, level);
    }
    
    @ModifyExpressionValue(method = "getAmbientSound()Lnet/minecraft/sounds/SoundEvent;",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/turtle/Turtle;isInWater()Z"))
    private boolean cfa_getAmbientSound(boolean original) {
        return original || !getLastFluid().isAir();
    }
    
    @ModifyExpressionValue(method = "getWalkTargetValue(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/LevelReader;)F",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/material/FluidState;is(Lnet/minecraft/tags/TagKey;)Z"))
    private boolean cfa_getWalkTargetValue(boolean original, @Local(argsOnly = true, name = "pos") BlockPos pos, @Local(argsOnly = true, name ="level") LevelReader level) {
        // I seem to remember fluid states not being available with some of the "level" objects that get
        // passed in during worldgen, and this showed up in a crash log, so we go through blockstate here
        return original || canSwimInFluidType(level.getBlockState(pos).getFluidState().getFluidType());
    }
}

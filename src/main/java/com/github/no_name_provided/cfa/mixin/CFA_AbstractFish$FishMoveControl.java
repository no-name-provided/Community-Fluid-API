package com.github.no_name_provided.cfa.mixin;

import com.github.no_name_provided.cfa.mixin_interfaces.IFluidTypeExtension;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(targets = "net.minecraft.world.entity.animal.fish.AbstractFish$FishMoveControl")
abstract class CFA_AbstractFish$FishMoveControl extends MoveControl {
    
    public CFA_AbstractFish$FishMoveControl(Mob mob) {
        super(mob);
    }
    
    /**
     * Allow fish to swim in any fluid they can, well, swim in.
     *
     * @param original Whether the fish is in water.
     * @return Whether the fish is in <i>any</i> fluid it can swim in.
     */
    @ModifyExpressionValue(method = "tick()V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/fish/AbstractFish;isEyeInFluid(Lnet/minecraft/tags/TagKey;)Z"))
    private boolean cfa_tick_fixEyeInFluid(boolean original) {
        return original || NeoForgeRegistries.FLUID_TYPES.stream().anyMatch(type ->
                !type.isVanilla() && type.canSwim(mob) && mob.isEyeInFluid(((IFluidTypeExtension) type).getTag())
        );
    }
}

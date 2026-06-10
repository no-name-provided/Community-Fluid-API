package com.github.no_name_provided.cfa.mixin;

import com.github.no_name_provided.cfa.mixin_interfaces.IFluidTypeExtension;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Zombie.class)
abstract class CFA_Zombie extends Monster {
    protected CFA_Zombie(EntityType<? extends Monster> type, Level level) {
        super(type, level);
    }
    
    @ModifyExpressionValue(method = "tick()V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/monster/zombie/Zombie;isEyeInFluid(Lnet/minecraft/tags/TagKey;)Z"))
    private boolean cfa_tick(boolean original) {
        return original || NeoForgeRegistries.FLUID_TYPES.stream().anyMatch(type ->
                !type.isVanilla() &&
                        type.canDrownIn(this) &&
                        fluidInteraction.isEyeInFluid(((IFluidTypeExtension) type).getTag())
        );
    }
}

package com.github.no_name_provided.cfa.mixin;

import com.github.no_name_provided.cfa.mixin_interfaces.IFluidTypeExtension;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.animal.fish.AbstractFish;
import net.minecraft.world.entity.animal.fish.WaterAnimal;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AbstractFish.class)
abstract class CFA_AbstractFish extends WaterAnimal implements Bucketable {
    protected CFA_AbstractFish(EntityType<? extends WaterAnimal> type, Level level) {
        super(type, level);
    }
    
    @ModifyExpressionValue(method = "aiStep()V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/fish/AbstractFish;isInWater()Z"))
    private boolean cfa_aiStep(boolean original) {
        return original || NeoForgeRegistries.FLUID_TYPES.stream().anyMatch(type ->
                !type.isVanilla() &&
                        type.canSwim(this) &&
                        fluidInteraction.isInFluid(((IFluidTypeExtension) type).getTag())
        );
    }
}

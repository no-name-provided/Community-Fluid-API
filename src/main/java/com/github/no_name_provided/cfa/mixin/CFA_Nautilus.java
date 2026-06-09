package com.github.no_name_provided.cfa.mixin;

import com.github.no_name_provided.cfa.mixin_interfaces.IFluidTypeExtension;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.nautilus.AbstractNautilus;
import net.minecraft.world.entity.animal.nautilus.Nautilus;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Nautilus.class)
abstract class CFA_Nautilus extends AbstractNautilus {
    protected CFA_Nautilus(EntityType<? extends AbstractNautilus> type, Level level) {
        super(type, level);
    }
    
    @ModifyExpressionValue(method = "handleAirSupply(Lnet/minecraft/server/level/ServerLevel;I)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/nautilus/Nautilus;isInWater()Z"))
    private boolean cfa_handleAirSupply(boolean original) {
        return original || NeoForgeRegistries.FLUID_TYPES.stream().anyMatch(type ->
                !type.isVanilla() &&
                        // May be a problem, if anyone gets clever about making fluids a nautilus can't drown in.
                        // Left out for ease of testing
//                        type.canDrownIn(this) &&
                        fluidInteraction.isInFluid(((IFluidTypeExtension) type).getTag())
        );
    }
}

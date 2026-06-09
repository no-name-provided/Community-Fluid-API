package com.github.no_name_provided.cfa.mixin;

import com.github.no_name_provided.cfa.mixin_interfaces.IFluidTypeExtension;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(WaterBoundPathNavigation.class)
abstract class CFA_WaterBoundPathNavigation extends PathNavigation {
    public CFA_WaterBoundPathNavigation(Mob mob, Level level) {
        super(mob, level);
    }
    
    /**
     * Attempt to fix aquatic mob pathing. Specifically, random wandering. appears to have failed.
     */
    @ModifyExpressionValue(method = "canUpdatePath()Z",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Mob;isInLiquid()Z"))
    private boolean cfa_canUpdatePath(boolean original) {
        return original || NeoForgeRegistries.FLUID_TYPES.stream().anyMatch(type ->
                // Vanilla doesn't check to see if the fluid can be swum in, only that it is a fluid (ie, lava counts)
                mob.fluidInteraction.isInFluid(((IFluidTypeExtension) type).getTag())
        );
    }
}

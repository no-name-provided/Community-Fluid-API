package com.github.no_name_provided.cfa.mixin;

import net.minecraft.network.chat.Component;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(FluidStack.class)
public abstract class CFA_FluidStack {
    
    @Shadow public abstract Fluid getFluid();
    @ModifyArg(method = "getTooltipLines(Lnet/minecraft/world/item/Item$TooltipContext;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/item/TooltipFlag;)Ljava/util/List;",
            at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z"), index = 0)
    private Object cfa_getTooltipLines(Object e) {
        FluidStack stack = (FluidStack) (Object) this;
        return Component.empty().append((Component)e).withStyle(stack.getFluidType().getRarity().getStyleModifier());
    }
}

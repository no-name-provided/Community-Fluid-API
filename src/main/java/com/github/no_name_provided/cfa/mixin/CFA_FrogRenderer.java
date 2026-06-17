package com.github.no_name_provided.cfa.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.model.animal.frog.FrogModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.FrogRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.state.FrogRenderState;
import net.minecraft.world.entity.animal.frog.Frog;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FrogRenderer.class)
abstract class CFA_FrogRenderer extends MobRenderer<Frog, FrogRenderState, FrogModel> {
    
    public CFA_FrogRenderer(EntityRendererProvider.Context context, FrogModel model, float shadow) {
        super(context, model, shadow);
    }
    
    @ModifyExpressionValue(method = "extractRenderState(Lnet/minecraft/world/entity/animal/frog/Frog;Lnet/minecraft/client/renderer/entity/state/FrogRenderState;F)V",
    at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/frog/Frog;isInWater()Z"))
    private boolean cfa_extractRenderState(boolean original, @Local(argsOnly = true, name = "entity") Frog entity) {
        return original || entity.canSwimInFluidType(entity.getLastFluid());
    }
}

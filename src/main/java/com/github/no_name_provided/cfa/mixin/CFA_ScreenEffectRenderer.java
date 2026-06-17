package com.github.no_name_provided.cfa.mixin;

import com.github.no_name_provided.cfa.mixin_interfaces.IFluidTypeExtension;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Implements the NeoForge fluid overlay you can register with the RegisterClientExtensionsEvent (so they actually apply
 * to non-water fluids, rather than being ignored or replaced with a hardcoded vanilla default).
 */
@Mixin(ScreenEffectRenderer.class)
public class CFA_ScreenEffectRenderer {
    @Shadow @Final private Minecraft minecraft;
    
    @Shadow public static void renderFluid(Minecraft minecraft, PoseStack poseStack, MultiBufferSource bufferSource, Identifier texture) {
        throw new UnsupportedOperationException("Implemented via mixin");
    }
    
    @ModifyExpressionValue(method = "renderScreenEffect(ZZFLnet/minecraft/client/renderer/SubmitNodeCollector;Z)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;isEyeInFluid(Lnet/minecraft/tags/TagKey;)Z"))
    private boolean cfa_renderScreenEffect_fixIsEyeInFluid(boolean original, @Local(name = "player") Player player) {
        // "Vanilla" uses the current, rather than buffered, value here.
        // Also, some weird stuff goes on with the player variable
        return original || player.isEyeInFluid(((IFluidTypeExtension) (player.getLastFluid())).getTag());
    }
    
    /**
     * Neo hardcodes the underwater overlay here... #BlameTheNeoForgedTeam. Instead of calling renderWater, or even
     * renderFluid with the correct overlay texture, we grab the IClientFluidTypeExtension associated with our fluidtype
     * and call its render method.
     */
    @Redirect(method = "renderScreenEffect(ZZFLnet/minecraft/client/renderer/SubmitNodeCollector;Z)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ScreenEffectRenderer;renderWater(Lnet/minecraft/client/Minecraft;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;)V"))
    private void cfa_renderScreenEffect_fixRenderFluid(Minecraft minecraft, PoseStack poseStack, MultiBufferSource bufferSource, @Local(name = "player") Player player) {
        IClientFluidTypeExtensions.of(player.getLastFluid()).renderOverlay(minecraft, poseStack, bufferSource);
    }
}

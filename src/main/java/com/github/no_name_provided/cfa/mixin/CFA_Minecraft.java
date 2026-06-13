package com.github.no_name_provided.cfa.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.blaze3d.platform.WindowEventHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.thread.ReentrantBlockableEventLoop;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Objects;

@Mixin(Minecraft.class)
abstract class CFA_Minecraft extends ReentrantBlockableEventLoop<Runnable> implements WindowEventHandler, net.neoforged.neoforge.client.extensions.IMinecraftExtension {
    @Shadow
    public @Nullable LocalPlayer player;
    
    public CFA_Minecraft(String name, boolean propagatesCrashes) {
        super(name, propagatesCrashes);
    }
    
    /**
     * Can vanilla underwater music play when the player is submerged in a fluid of this type?
     *
     * @param original True if the player is underwater; false otherwise.
     * @return True if vanilla underwater music can play when submerged in this fluid; false otherwise.
     */
    @ModifyExpressionValue(method = "getSituationalMusic()Lnet/minecraft/sounds/Music;",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;isUnderWater()Z"))
    private boolean cfa_getSituationalMusic_isUnderWater(boolean original) {
        // The player is null-checked before we reach this point, so we can assume it isn't null
        return original || Objects.requireNonNull(player).getWasUnderLastFluid();
    }
}

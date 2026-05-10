package com.github.no_name_provided.fun_fluids.mixin;

import com.github.no_name_provided.fun_fluids.common.fluids.fluidtypes.TaggedFluidType;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LocalPlayer.class)
abstract class Fun_Fluids_LocalPlayer extends AbstractClientPlayer {
    
    public Fun_Fluids_LocalPlayer(ClientLevel level, GameProfile gameProfile) {
        super(level, gameProfile);
    }
    
    /**
     * Trigger sinking while shifting in modded fluids.
     */
    @ModifyExpressionValue(method = "aiStep()V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;isInWater()Z"))
    private boolean isInWater(boolean original) {
        return original || NeoForgeRegistries.FLUID_TYPES.stream()
                .anyMatch(type ->
                        type instanceof TaggedFluidType tagged &&
                                this.fluidInteraction.isInFluid(tagged.getTag())
                );
        
    }
}

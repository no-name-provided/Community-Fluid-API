package com.github.no_name_provided.fun_fluids.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.renderer.block.FluidRenderer;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.fml.util.thread.EffectiveSide;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FluidRenderer.class)
public class Fun_Fluids_FluidRenderer {
    @ModifyReturnValue(method = "shouldRenderFace(Lnet/minecraft/world/level/material/FluidState;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/Direction;Lnet/minecraft/world/level/block/state/BlockState;)Z",
            at = @At("RETURN"))
    private static boolean Fun_Fluids_shouldRenderFace(boolean original, FluidState fluidState, BlockState selfState, Direction direction, BlockState otherState) {
        return original && !selfState.skipRendering(otherState, direction.getOpposite());
    }
    
    /**
     * Force the tesselator to respect Block#skipRendering while handling upper fluid faces.
     * <p>
     * The check for whether the top surface of a fluid should be rendered bypasses #shouldRenderFace, so we need to
     * inject our conditional rendering logic here.
     * </p>
     *
     * @param original     The original return value.
     * @param fluidState   The fluid state being rendered.
     * @param fluidStateUp The fluid state directly above.
     * @return True if the face should be skipped during rendering; otherwise false.
     */
    @ModifyExpressionValue(method = "tesselate(Lnet/minecraft/client/renderer/block/BlockAndTintGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/client/renderer/block/FluidRenderer$Output;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/material/FluidState;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/block/FluidRenderer;isNeighborSameFluid(Lnet/minecraft/world/level/material/FluidState;Lnet/minecraft/world/level/material/FluidState;)Z", ordinal = 0))
    private static boolean Fun_Fluids_tesselate(boolean original, @Local(name = "fluidState", argsOnly = true) FluidState fluidState, @Local(name = "fluidStateUp") FluidState fluidStateUp) {
        if (EffectiveSide.get().isClient()) {
            return original || fluidState.createLegacyBlock().skipRendering(fluidStateUp.createLegacyBlock(), Direction.UP);
        } else {
            return original;
        }
    }
}

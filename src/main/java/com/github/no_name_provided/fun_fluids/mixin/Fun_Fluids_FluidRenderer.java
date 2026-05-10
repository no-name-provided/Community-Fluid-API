package com.github.no_name_provided.fun_fluids.mixin;

import com.github.no_name_provided.fun_fluids.client.ClientConfig;
import com.github.no_name_provided.fun_fluids.datagen.providers.FFFluidTagsProvider;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
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
     * Quick hack to suppress rendering of top surface of invisible fluids.
     */
    @ModifyReturnValue(method = "isNeighborSameFluid(Lnet/minecraft/world/level/material/FluidState;Lnet/minecraft/world/level/material/FluidState;)Z",
            at = @At("RETURN"))
    private static boolean Fun_Fluids_isNeighborSameFluid(boolean original, FluidState fluidState, FluidState neighborFluidState) {
        if (EffectiveSide.get().isClient()) {
            return original || fluidState.is(FFFluidTagsProvider.THICK_AIR) && !ClientConfig.renderThickAir;
        } else {
            return original;
        }
    }
    
    
}

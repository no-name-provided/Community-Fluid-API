package com.github.no_name_provided.fun_fluids.mixin;

import com.github.no_name_provided.fun_fluids.common.fluids.registries.FluidRegistries;
import net.minecraft.client.Camera;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.FogType;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;

/**
 * Forces the lava camera fog to render while the player is in CoolLavaFluid.
 * This is necessary because the fog system is separate from (and not supported by)
 * the overlay in IClientFluidTypeExtensions.
 * <p>
 *     It may be possible to replace this, with some difficulty, with ViewportEvent.RenderFog
 *     or ComputeFogColor. Despite their names, and where they're thrown, it's <i>possible</i>
 *     these events are actually called each render tick, regardless of whether or not a fog is
 *     being rendered.
 * </p>
 **/
@Mixin(Camera.class)
public abstract class Fun_Fluids_Camera {

    @Shadow
    private boolean initialized;
    @Shadow
    public abstract Camera.NearPlane getNearPlane();
    @Shadow
    private Vec3 position;
    @Shadow
    private BlockGetter level;
    @Shadow
    @Final
    private Vector3f forwards;

    @Inject(method = "getFluidInCamera()Lnet/minecraft/world/level/material/FogType;", at = @At("RETURN"), cancellable = true)
    void Fun_Fluids_FogType(CallbackInfoReturnable<FogType> cir) {
        if (this.initialized) {
            Camera.NearPlane nearplane = this.getNearPlane();
            for (Vec3 vec3 : Arrays.asList(
                    new Vec3(this.forwards).scale(0.05F),
                    nearplane.getTopLeft(),
                    nearplane.getTopRight(),
                    nearplane.getBottomLeft(),
                    nearplane.getBottomRight()
            )) {
                Vec3 vec31 = this.position.add(vec3);
                BlockPos blockpos = BlockPos.containing(vec31);
                FluidState fluidstate = this.level.getFluidState(blockpos);

                if (fluidstate.getFluidType() == FluidRegistries.FunFluidTypes.COOL_LAVA.get()) {
                    if (vec31.y <= (double)(fluidstate.getHeight(this.level, blockpos) + (float)blockpos.getY())) {
                        cir.setReturnValue(FogType.LAVA);
                        cir.cancel();
                    }
                }
            }
        }
    }
}

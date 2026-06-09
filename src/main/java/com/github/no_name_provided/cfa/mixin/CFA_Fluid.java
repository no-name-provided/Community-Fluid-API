package com.github.no_name_provided.cfa.mixin;

import com.mojang.logging.annotations.MethodsReturnNonnullByDefault;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.common.SoundActions;
import net.neoforged.neoforge.fluids.FluidType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;

@ParametersAreNonnullByDefault @MethodsReturnNonnullByDefault
@Mixin(Fluid.class)
abstract class CFA_Fluid {
    
    @Shadow
    public abstract FluidType getFluidType();
    
    /**
     * @author No Name Provided.
     * @reason Default value is stupid. Should prefer FluidType property to null. Prevents counterintuitive behavior and
     * reduces boilerplate.
     */
    @Overwrite
    public Optional<SoundEvent> getPickupSound() {
        return Optional.ofNullable(getFluidType().getSound(SoundActions.BUCKET_FILL));
    }
}

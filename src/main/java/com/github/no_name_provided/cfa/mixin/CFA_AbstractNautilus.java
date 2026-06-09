package com.github.no_name_provided.cfa.mixin;

import com.github.no_name_provided.cfa.mixin_interfaces.IFluidTypeExtension;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.HasCustomInventoryScreen;
import net.minecraft.world.entity.PlayerRideableJumping;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.nautilus.AbstractNautilus;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(AbstractNautilus.class)
abstract class CFA_AbstractNautilus extends TamableAnimal implements PlayerRideableJumping, HasCustomInventoryScreen {
    protected CFA_AbstractNautilus(EntityType<? extends TamableAnimal> type, Level level) {
        super(type, level);
    }
    
    /**
     * Every Nautilus check is just for sound. It's probably safe to assume modders want to use underwater sounds for
     * swimmable fluids.
     *
     * @return If the mob is under any swimmable fluid.
     */
    @Override
    public boolean isUnderWater() {
        return super.isUnderWater() ||
                // Vanilla checks both fluid immersion and eye immersion separately...
                NeoForgeRegistries.FLUID_TYPES.stream().anyMatch(type ->
                        type.canSwim(this) &&
                                fluidInteraction.isInFluid(((IFluidTypeExtension) type).getTag()) &&
                                fluidInteraction.isEyeInFluid(((IFluidTypeExtension) type).getTag())
                );
    }
}

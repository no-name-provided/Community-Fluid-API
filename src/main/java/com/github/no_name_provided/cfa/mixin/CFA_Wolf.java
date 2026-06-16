package com.github.no_name_provided.cfa.mixin;

import com.github.no_name_provided.cfa.mixin_interfaces.CFA_IEntityExtension;
import com.github.no_name_provided.cfa.mixin_interfaces.IFluidTypeExtension;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.wolf.Wolf;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Wolf.class)
abstract class CFA_Wolf extends TamableAnimal implements NeutralMob, CFA_IEntityExtension {
    protected CFA_Wolf(EntityType<? extends TamableAnimal> type, Level level) {
        super(type, level);
    }
    
    /**
     * Let modded fluids make wolves wet.
     *
     * @param original The result of the vanilla check for rain or water.
     * @return Whether the wolf should be wet.
     */
    @ModifyExpressionValue(method = "tick()V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/wolf/Wolf;isInWaterOrRain()Z"))
    private boolean cfa_tick_fixWolfWetting(boolean original) {
        return original || ((IFluidTypeExtension) getLastFluid()).makesWet(getType());
    }
    
    // TODO: add an attachment to store the fluid that wet the wolf, so we can use the correct particle color. Low priority.
//    @Redirect(method = "tick()V",
//            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V"))
//    private void cfa_tick_fixWolfShakeParticle(Level instance, ParticleOptions particle, double x, double y, double z, double xd, double yd, double zd) {
//        Fluid fluid = getData(...
//        ((IFluidTypeExtension) getLastFluid()).makesWet(getType());
//    }
}

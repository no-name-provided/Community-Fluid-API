package com.github.no_name_provided.cfa.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.BlockAttachedEntity;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.NeoForgeMod;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * Tiny little mixin to allow blocks logged with a non-water fluid to resist explosions. We ignore empty and lava, to
 * maintain vanilla behavior. We may want to add a FLuidType method for this at some point.
 */
@Mixin(BlockAttachedEntity.class)
abstract class CFA_BlockAttachedEntity extends Entity {
    
    public CFA_BlockAttachedEntity(EntityType<?> type, Level level) {
        super(type, level);
    }
    
    @ModifyExpressionValue(method = "ignoreExplosion(Lnet/minecraft/world/level/Explosion;)Z",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;isInWater()Z"))
    private boolean cfa_ignoreExplosion(boolean original) {
        return original || (getLastFluid() != NeoForgeMod.EMPTY_TYPE.value() && getLastFluid() != NeoForgeMod.LAVA_TYPE.value());
    }
}

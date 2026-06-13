package com.github.no_name_provided.cfa.mixin;

import com.github.no_name_provided.cfa.mixin_interfaces.IFluidTypeExtension;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.NeoForgeMod;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ItemEntity.class)
abstract class CFA_ItemEntity extends Entity {
    public CFA_ItemEntity(EntityType<?> type, Level level) {
        super(type, level);
    }
    
    /**
     * Suppress vanilla water logic.
     *
     * @param original The original return value.
     * @return False.
     */
    @ModifyExpressionValue(method = "tick()V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/item/ItemEntity;isInWater()Z"))
    private boolean cfa_tick_disableVanillaWaterMovement(boolean original) {
        return false;
    }
    
    /**
     * Suppress vanilla lava logic.
     *
     * @param original The original return value.
     * @return False.
     */
    @ModifyExpressionValue(method = "tick()V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/item/ItemEntity;isInLava()Z"))
    private boolean cfa_tick_disableVanillaLavaMovement(boolean original) {
        return false;
    }
    
    /**
     * Since we've guaranteed this branch of the if-else chain will be executed, we inject our all-purpose conditional
     * here (and default to the original else).
     *
     * @param instance This class.
     * @param original The wrapped method.
     */
    @WrapOperation(method = "tick()V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/item/ItemEntity;applyGravity()V"))
    private void cfa_tick_handleAllMovementLogic(ItemEntity instance, Operation<Void> original) {
        if (
                getLastFluid() != NeoForgeMod.EMPTY_TYPE.value() &&
                        // The magic number, 0.1, comes from the vanilla logic.
                        // Likely a compiler optimization of the ItemEntity.FLOAT_HEIGHT field
                        fluidInteraction.getFluidHeight(((IFluidTypeExtension) getLastFluid()).getTag()) > 0.1f
        ) {
            getLastFluid().setItemMovement(instance);
        } else {
            original.call(instance);
        }
    }
}

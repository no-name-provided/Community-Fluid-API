package com.github.no_name_provided.cfa.mixin;

import com.github.no_name_provided.cfa.mixin_interfaces.IFluidTypeExtension;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ConduitBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * Jailbreak conduits.
 */
@Mixin(ConduitBlockEntity.class)
abstract class CFA_ConduitBlockEntity extends BlockEntity {
    
    public CFA_ConduitBlockEntity(BlockEntityType<?> type, BlockPos worldPosition, BlockState blockState) {
        super(type, worldPosition, blockState);
    }
    
    @ModifyExpressionValue(method = "updateShape(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Ljava/util/List;)Z",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;isWaterAt(Lnet/minecraft/core/BlockPos;)Z"))
    private static boolean cfa_updateShape(boolean original, @Local(name = "level", argsOnly = true) Level level, @Local(name = "testPos") BlockPos testPos) {
        FluidType type = level.getFluidState(testPos).getFluidType();
        return original || ((IFluidTypeExtension) type).conduitCompatible();
    }
    
    @ModifyExpressionValue(method = "applyEffects(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Ljava/util/List;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;isInWaterOrRain()Z"))
    private static boolean cfa_applyEffects(boolean original, @Local(name = "player") Player player) {
        return original || ((IFluidTypeExtension) player.getLastFluid()).conduitCompatible();
    }
    
    @ModifyExpressionValue(method = "lambda$selectNewTarget$0(Lnet/minecraft/world/entity/LivingEntity;)Z",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;isInWaterOrRain()Z"))
    private static boolean cfa_selectNewTarget(boolean original, @Local(name = "input", argsOnly = true) LivingEntity input) {
        return original || ((IFluidTypeExtension) input.getLastFluid()).conduitCompatible();
    }
}

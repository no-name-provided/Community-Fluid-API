package com.github.no_name_provided.cfa.mixin;

import com.github.no_name_provided.cfa.mixin_interfaces.IFluidTypeExtension;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.fish.AbstractSchoolingFish;
import net.minecraft.world.entity.animal.fish.TropicalFish;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(TropicalFish.class)
abstract class CFA_TropicalFish extends AbstractSchoolingFish {
    
    public CFA_TropicalFish(EntityType<? extends AbstractSchoolingFish> type, Level level) {
        super(type, level);
    }
    
    @WrapOperation(method = "checkTropicalFishSpawnRules(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/LevelAccessor;Lnet/minecraft/world/entity/EntitySpawnReason;Lnet/minecraft/core/BlockPos;Lnet/minecraft/util/RandomSource;)Z",
    at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/material/FluidState;is(Lnet/minecraft/tags/TagKey;)Z"))
    private static boolean cfa_checkTropicalFishSpawnRules_fixFluidStateCheck(FluidState instance, TagKey<Fluid> tagKey, Operation<Boolean> original, @Local(name = "level", argsOnly = true)LevelAccessor level, @Local(name = "pos", argsOnly = true) BlockPos pos) {
        // below
        return ((IFluidTypeExtension)level.getFluidState(pos.below()).getType()).canSpawnAquaticMobs(EntityType.TROPICAL_FISH);
    }
    
    @WrapOperation(method = "checkTropicalFishSpawnRules(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/LevelAccessor;Lnet/minecraft/world/entity/EntitySpawnReason;Lnet/minecraft/core/BlockPos;Lnet/minecraft/util/RandomSource;)Z",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Ljava/lang/Object;)Z"))
    private static boolean cfa_checkTropicalFishSpawnRules_fixBlockStateCheck(BlockState instance, Object block, Operation<Boolean> original, @Local(name = "level", argsOnly = true)LevelAccessor level, @Local(name = "pos", argsOnly = true) BlockPos pos) {
        // above
        return ((IFluidTypeExtension)level.getFluidState(pos.above()).getType()).canSpawnAquaticMobs(EntityType.TROPICAL_FISH);
    }
}

package com.github.no_name_provided.cfa.mixin;

import com.github.no_name_provided.cfa.mixin_interfaces.IFluidTypeExtension;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.animal.fish.WaterAnimal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(WaterAnimal.class)
abstract class CFA_WaterAnimal extends PathfinderMob {
    protected CFA_WaterAnimal(EntityType<? extends PathfinderMob> type, Level level) {
        super(type, level);
    }
    
    @ModifyExpressionValue(method = "handleAirSupply(Lnet/minecraft/server/level/ServerLevel;I)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/fish/WaterAnimal;isInWater()Z"))
    private boolean cfa_handleAirSupply(boolean original) {
        return original || NeoForgeRegistries.FLUID_TYPES.stream().anyMatch(type ->
                !type.isVanilla() &&
                        this.canDrownInFluidType(type) &&
                        fluidInteraction.isInFluid(((IFluidTypeExtension) type).getTag())
        );
    }
    
    /**
     * Maps fluids that can spawn aquatic mammals to water (which passes the downstream check) and redirects others to
     * Fluids#EMPTY, which fails.
     *
     * @param original The BlockPos being checked.
     * @return Water#defaultFluidState if the fluid can spawn mobs; otherwise EMPTY#defaultFluidState.
     */
    @ModifyExpressionValue(method = "checkSurfaceWaterAnimalSpawnRules(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/LevelAccessor;Lnet/minecraft/world/entity/EntitySpawnReason;Lnet/minecraft/core/BlockPos;Lnet/minecraft/util/RandomSource;)Z",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/LevelAccessor;getFluidState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/material/FluidState;"))
    private static FluidState cfa_checkSurfaceWaterAnimalSpawnRules_fixFluidStateCheck(FluidState original, @Local(argsOnly = true, name = "type") EntityType<? extends WaterAnimal> type) {
        boolean canSpawn = ((IFluidTypeExtension) original.getType()).canSpawnAquaticMobs(type);
        return canSpawn ? Fluids.WATER.defaultFluidState() : Fluids.EMPTY.defaultFluidState();
    }
    
    /**
     * Maps blocks that can spawn aquatic mammals to water (which passes the downstream check) and redirects others to
     * Fluids#EMPTY, which fails.
     *
     * @param original The BlockPos being checked.
     * @return Water#defaultBlockState if the fluid can spawn mobs; otherwise EMPTY#defaultFluidState.
     */
    @ModifyExpressionValue(method = "checkSurfaceWaterAnimalSpawnRules(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/LevelAccessor;Lnet/minecraft/world/entity/EntitySpawnReason;Lnet/minecraft/core/BlockPos;Lnet/minecraft/util/RandomSource;)Z",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/LevelAccessor;getBlockState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;"))
    private static BlockState cfa_checkSurfaceWaterAnimalSpawnRules_fixBlockStateCheck(BlockState original, @Local(argsOnly = true, name = "type") EntityType<? extends WaterAnimal> type) {
        boolean canSpawn = ((IFluidTypeExtension) original.getFluidState().getType()).canSpawnAquaticMobs(type);
        return canSpawn ? Blocks.WATER.defaultBlockState() : Fluids.EMPTY.defaultFluidState().createLegacyBlock();
    }
}

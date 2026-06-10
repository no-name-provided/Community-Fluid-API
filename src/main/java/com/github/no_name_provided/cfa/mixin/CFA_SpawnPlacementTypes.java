package com.github.no_name_provided.cfa.mixin;

import com.github.no_name_provided.cfa.mixin_interfaces.IFluidTypeExtension;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.SpawnPlacementType;
import net.minecraft.world.entity.SpawnPlacementTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(SpawnPlacementTypes.class)
public interface CFA_SpawnPlacementTypes {
    
    @Shadow
    SpawnPlacementType IN_WATER = (level, blockPos, type) -> {
        if (type != null && level.getWorldBorder().isWithinBounds(blockPos)) {
            BlockPos above = blockPos.above();
            return ((IFluidTypeExtension) level.getFluidState(blockPos).getFluidType()).canSpawnAquaticMobs(type) && !level.getBlockState(above).isRedstoneConductor(level, above);
        } else {
            return false;
        }
    };
}

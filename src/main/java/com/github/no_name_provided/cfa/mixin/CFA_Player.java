package com.github.no_name_provided.cfa.mixin;

import com.github.no_name_provided.cfa.mixin_interfaces.CFA_IPlayerExtension;
import com.github.no_name_provided.cfa.mixin_interfaces.IFluidTypeExtension;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.logging.annotations.MethodsReturnNonnullByDefault;
import net.minecraft.world.entity.Avatar;
import net.minecraft.world.entity.ContainerUser;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.extensions.IPlayerExtension;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import javax.annotation.ParametersAreNonnullByDefault;

@Mixin(Player.class) @ParametersAreNonnullByDefault @MethodsReturnNonnullByDefault
abstract class CFA_Player extends Avatar implements ContainerUser, IPlayerExtension, CFA_IPlayerExtension {
    @Unique
    private Boolean cfa$wasUnderLastFluid = false;
    
    protected CFA_Player(EntityType<? extends LivingEntity> type, Level level) {
        super(type, level);
    }
    
    @SuppressWarnings("AddedMixinMembersNamePattern") // appropriate name for injected interface method
    @Override
    public boolean getWasUnderLastFluid() {
        return cfa$wasUnderLastFluid;
    }
    
    @SuppressWarnings("AddedMixinMembersNamePattern") // appropriate name for injected interface method
    @Override
    public void setWasUnderLastFluid(Boolean wasUnder) {
        cfa$wasUnderLastFluid = wasUnder;
    }
    
    /**
     * Don't refresh turtle helm respiration while the game is actively trying to drown you.
     */
    @ModifyExpressionValue(method = "tick()V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;isEyeInFluid(Lnet/minecraft/tags/TagKey;)Z"))
    private boolean fun_fluids_tick(boolean original) {
        return original || NeoForgeRegistries.FLUID_TYPES.stream().anyMatch(this::canDrownInFluidType);
    }
    
    @ModifyExpressionValue(method = "getDestroySpeed(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;)F",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;isEyeInFluid(Lnet/minecraft/tags/TagKey;)Z"))
    private boolean fun_fluids_fix_submersion_check(boolean original) {
        return original || NeoForgeRegistries.FLUID_TYPES.stream().anyMatch(fluidType -> {
            IFluidTypeExtension extension = (IFluidTypeExtension) fluidType;
            return isEyeInFluid(extension.getTag()) && extension.reducesMiningSpeed();
        });
    }
    
}

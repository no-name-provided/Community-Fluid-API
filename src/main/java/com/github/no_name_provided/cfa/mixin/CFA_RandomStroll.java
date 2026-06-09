package com.github.no_name_provided.cfa.mixin;

import com.github.no_name_provided.cfa.mixin_interfaces.IFluidTypeExtension;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.behavior.RandomStroll;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.function.Predicate;

@Mixin(RandomStroll.class)
abstract class CFA_RandomStroll {
    
    @ModifyArg(method = "swim(F)Lnet/minecraft/world/entity/ai/behavior/BehaviorControl;",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/behavior/RandomStroll;strollFlyOrSwim(FLjava/util/function/Function;Ljava/util/function/Predicate;)Lnet/minecraft/world/entity/ai/behavior/OneShot;"),
            index = 2)
    private static Predicate<PathfinderMob> cfa_swim(Predicate<PathfinderMob> canRun) {
        return mob -> NeoForgeRegistries.FLUID_TYPES.stream().anyMatch(type ->
                mob.fluidInteraction.isInFluid(((IFluidTypeExtension) type).getTag()) &&
                        type.canSwim(mob)
        );
    }
}

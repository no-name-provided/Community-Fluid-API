package com.github.no_name_provided.cfa.common;

import com.github.no_name_provided.cfa.mixin_interfaces.IFluidTypeExtension;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingFallEvent;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

@EventBusSubscriber
public class CommonEvents {
    
    /**
     * Since Neo already hooks into the fall damage event, we just modify the effective distance here.
     */
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    static void onLivingFall(LivingFallEvent event) {
        LivingEntity entity = event.getEntity();
        float fallDistanceModifier = entity.getFluidFallDistanceModifier(NeoForgeRegistries.FLUID_TYPES.stream().filter(type ->
                !type.isVanilla() && entity.fluidInteraction.isInFluid(((IFluidTypeExtension) type).getTag())
        ).findFirst().orElse(Fluids.EMPTY.getFluidType()));
        event.setDistance(event.getDistance() * fallDistanceModifier);
    }
}

package com.github.no_name_provided.cfa.datagen;

import com.github.no_name_provided.cfa.datagen.providers.CFAParticleProvider;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import static com.github.no_name_provided.cfa.CommunityFluidAPI.MODID;

@EventBusSubscriber(modid = MODID)
public class Generators {
    /**
     * Queues up datagen.
     */
    @SubscribeEvent
    static void onGatherData(GatherDataEvent.Client event) {
        event.createProvider(CFAParticleProvider::new);
    }
}

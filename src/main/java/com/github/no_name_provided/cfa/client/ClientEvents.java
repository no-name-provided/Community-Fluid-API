package com.github.no_name_provided.cfa.client;

import com.github.no_name_provided.cfa.client.particles.CFAParticleTypes;
import com.github.no_name_provided.cfa.client.particles.TintedSplashParticle;
import com.github.no_name_provided.cfa.client.particles.TintedWakeParticle;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;

import static com.github.no_name_provided.cfa.CommunityFluidAPI.MODID;

@EventBusSubscriber(modid = MODID, value = Dist.CLIENT)
public class ClientEvents {
    
    @SubscribeEvent
    private static void registerParticleProviders(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(CFAParticleTypes.TINTED_SPLASH_PARTICLE.get(), TintedSplashParticle.Provider::new);
        event.registerSpriteSet(CFAParticleTypes.TINTED_WAKE_PARTICLE.get(), TintedWakeParticle.Provider::new);
    }
}

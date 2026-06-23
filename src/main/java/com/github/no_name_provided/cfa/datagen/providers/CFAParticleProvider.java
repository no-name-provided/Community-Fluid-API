package com.github.no_name_provided.cfa.datagen.providers;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.client.data.ParticleDescriptionProvider;

import static com.github.no_name_provided.cfa.CommunityFluidAPI.MODID;
import static com.github.no_name_provided.cfa.client.particles.CFAParticleTypes.*;

public class CFAParticleProvider extends ParticleDescriptionProvider {
    
    public CFAParticleProvider(PackOutput output) {
        super(output);
    }
    
    @Override
    protected void addDescriptions() {
        spriteSet(
                TINTED_SPLASH_PARTICLE.get(),
                // This is the (base) name of the textures to use
                Identifier.fromNamespaceAndPath(MODID, "colorless_splash"),
                4,
                false
        );
        spriteSet(
                TINTED_SPLASH_PARTICLE_FROM_FLUID.get(),
                Identifier.fromNamespaceAndPath(MODID, "colorless_splash"),
                4,
                false
        );
        // This uses the same splash textures, even in vanilla
        spriteSet(
                TINTED_WAKE_PARTICLE.get(),
                Identifier.fromNamespaceAndPath(MODID, "colorless_splash"),
                4,
                false
        );
        spriteSet(
                TINTED_WAKE_PARTICLE_FROM_FLUID.get(),
                Identifier.fromNamespaceAndPath(MODID, "colorless_splash"),
                4,
                false
        );
    }
}

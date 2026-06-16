package com.github.no_name_provided.cfa.client.particles;

import com.mojang.logging.annotations.MethodsReturnNonnullByDefault;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.WakeParticle;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.util.RandomSource;
import org.jspecify.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * A tintable version of the vanilla "Fishing"/WakeParticle, widely used for vanilla fish approaching fishing bobs. This
 * is provided as a convenience to mod devs.
 */
@ParametersAreNonnullByDefault @MethodsReturnNonnullByDefault
public class TintedWakeParticle extends WakeParticle {
    
    public TintedWakeParticle(ClientLevel level, double x, double y, double z, double xa, double ya, double za, SpriteSet sprites, ColorParticleOption option) {
        super(level, x, y, z, xa, ya, za, sprites);
        
        setColor(option.getRed(), option.getGreen(), option.getBlue());
        setAlpha(option.getAlpha());
    }
    
    public static class Provider implements ParticleProvider<ColorParticleOption> {
        private final SpriteSet sprites;
        
        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }
        
        @Override
        public @Nullable Particle createParticle(ColorParticleOption options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
            return new TintedWakeParticle(level, x, y, z, xAux, yAux, zAux, this.sprites, options);
        }
    }
}


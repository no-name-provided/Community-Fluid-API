package com.github.no_name_provided.cfa.client.particles;

import com.github.no_name_provided.cfa.client.particles.options.FluidParticleOption;
import com.github.no_name_provided.cfa.common.ClientWrappers;
import com.mojang.logging.annotations.MethodsReturnNonnullByDefault;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SplashParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.util.RandomSource;
import org.jspecify.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

import static com.github.no_name_provided.cfa.client.particles.CFAParticleTypes.TINTED_SPLASH_PARTICLE;

/**
 * A tintable version of the vanilla net.minecraft.client.particle.SplashParticle, widely used in vanilla fluid effects.
 * This is provided as a convenience to mod devs.
 */
@ParametersAreNonnullByDefault @MethodsReturnNonnullByDefault
public class TintedSplashParticle extends SplashParticle {
    
    public TintedSplashParticle(ClientLevel level, double x, double y, double z, double xa, double ya, double za, TextureAtlasSprite sprite, ColorParticleOption option) {
        super(level, x, y, z, xa, ya, za, sprite);
        
        setColor(option.getRed(), option.getGreen(), option.getBlue());
        setAlpha(option.getAlpha());
    }
    
    public static class ArbitraryTintProvider implements ParticleProvider<ColorParticleOption> {
        private final SpriteSet sprite;
        
        public ArbitraryTintProvider(SpriteSet sprite) {
            this.sprite = sprite;
        }
        
        @Override
        public @Nullable Particle createParticle(
                ColorParticleOption options,
                ClientLevel level,
                double x,
                double y,
                double z,
                double xAux,
                double yAux,
                double zAux,
                RandomSource random
        ) {
            return new TintedSplashParticle(level, x, y, z, xAux, yAux, zAux, this.sprite.get(random), options);
        }
    }
    
    /**
     * Our fluid tints aren't available on the server, so we encode the entire Fluid and use it to look up the tint when
     * we initialize our particle on the client.
     */
    public static class TintFromFluidProvider implements ParticleProvider<FluidParticleOption> {
        private final SpriteSet sprite;
        
        public TintFromFluidProvider(SpriteSet sprite) {
            this.sprite = sprite;
        }
        
        @Override
        public @Nullable Particle createParticle(
                FluidParticleOption options,
                ClientLevel level,
                double x,
                double y,
                double z,
                double xAux,
                double yAux,
                double zAux,
                RandomSource random
        ) {
            return new TintedSplashParticle(level,
                    x,
                    y,
                    z,
                    xAux,
                    yAux,
                    zAux,
                    this.sprite.get(random),
                    ColorParticleOption.create(
                            TINTED_SPLASH_PARTICLE.get(),
                            // We default to the vanilla water color calcs here if no tint was provided
                            ClientWrappers.getFluidTint(options.getFluid()).orElse(level.getBlockTint(BlockPos.containing(x, y, z), BiomeColors.WATER_COLOR_RESOLVER))
                    )
            );
        }
    }
}

package com.github.no_name_provided.fun_fluids.client.particles;

import com.github.no_name_provided.fun_fluids.client.ClientConfig;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.concurrent.ThreadLocalRandom;

/**
 * This class briefly discusses render methods, and demonstrates a particle that
 * can be configured to render behind translucent fluids.
 * */
@ParametersAreNonnullByDefault @MethodsReturnNonnullByDefault
public class MistParticle extends TextureSheetParticle {

    protected MistParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet sprites) {
        super(level, x, y, z);
        this.scale(1.5f);
        this.setSize(0.2F, 0.2f);

        this.gravity = 3.5E-6f;
        this.xd = xSpeed;
        this.yd = ySpeed + (double) (this.random.nextFloat() / 500.0f);
        this.zd = zSpeed;

        this.lifetime = 60;

        // Always set the initial sprite here (or in the provider)
        // since ticking is not guaranteed to set the sprite before
        // the render method is called.
        this.pickSprite(sprites);
    }

    /**
     * Copied from net.minecraft.client.particle.CampfireSmokeParticle.
     * */
    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ < this.lifetime && !(this.alpha <= 0.0F)) {
            this.xd += this.random.nextFloat() / 5000.0F * (float) (this.random.nextBoolean() ? 1 : -1);
            this.zd += this.random.nextFloat() / 5000.0F * (float) (this.random.nextBoolean() ? 1 : -1);
            this.yd -= this.gravity;
            this.move(this.xd, this.yd, this.zd);
            if (this.age >= this.lifetime - 60 && this.alpha > 0.01F) {
                this.alpha -= 0.015F;
            }
        } else {
            this.remove();
        }
    }

    public record Provider(SpriteSet sprites) implements ParticleProvider<SimpleParticleType> {

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            // We use ThreadLocalRandom here for guaranteed multithreaded performance...
            // and because it has a convenience method for doubles.
            ThreadLocalRandom rand = ThreadLocalRandom.current();
            return new MistParticle(
                    level,
                    x - 0.5,
                    y - 0.2,
                    z - 0.5,
                    xSpeed + rand.nextDouble(-.05, .05),
                    ySpeed + rand.nextDouble(0.05),
                    zSpeed + rand.nextDouble(-.05, .05),
                    sprites
            );
        }
    }

    @Override
    public ParticleRenderType getRenderType() {
        // If it's translucent, it generally won't render behind (inside) fluids
        if (ClientConfig.translucentMistParticles) {
            return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
        } else {
            return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
        }
    }

}

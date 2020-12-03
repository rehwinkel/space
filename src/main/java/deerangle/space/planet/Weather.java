package deerangle.space.planet;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class Weather extends ForgeRegistryEntry<Weather> {

    private final ResourceLocation weatherTexture;
    private final ParticleType<?> particle;

    public Weather(ResourceLocation weatherTexture, ParticleType<?> particle) {
        this.weatherTexture = new ResourceLocation(weatherTexture.getNamespace(), "textures/environment/" + weatherTexture.getPath() + ".png");
        this.particle = particle;
    }

    public ParticleType<?> getParticle() {
        return particle;
    }

    public ResourceLocation getWeatherTexture() {
        return weatherTexture;
    }

}

package deerangle.space.registry;

import deerangle.space.world.mars.biomes.MarsBiomeMaker;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;

import java.rmi.registry.Registry;

public class BiomeRegistry extends AbstractRegistry {

    public static RegistryObject<Biome> RUSTY_DESERT = BIOMES.register("rusty_desert", () -> MarsBiomeMaker.makeRustyDesertBiome(0.25F, 0.025F));

    public static void register() {

    }
}

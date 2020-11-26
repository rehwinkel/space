package deerangle.space.planet;

import deerangle.space.data.LanguageGenerator;
import deerangle.space.main.SpaceMod;
import deerangle.space.planet.data.BiomeGenerator;
import deerangle.space.planet.data.DimensionGenerator;
import deerangle.space.planet.feature.Features;
import deerangle.space.planet.planets.mars.MarsRegistry;
import deerangle.space.planet.planets.venus.VenusRegistry;
import deerangle.space.planet.planets.venus.world.VenusBiomeProvider;
import deerangle.space.planet.render.CustomDimensionRenderInfo;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.minecraft.client.world.DimensionRenderInfo;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryManager;

import java.util.Map;

public class PlanetManager {

    static {
        Registry.register(Registry.BIOME_PROVIDER_CODEC, new ResourceLocation(SpaceMod.MOD_ID, "venus_provider"),
                VenusBiomeProvider.CODEC);
    }

    public static final ItemGroup TAB = new ItemGroup(SpaceMod.MOD_ID + ".extraterrestrial") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(VenusRegistry.PULCHERITE.get());
        }
    };

    public static void register() {
        VenusRegistry.register();
        MarsRegistry.register();
        Features.FEATURES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static void registerData(GatherDataEvent event) {
        VenusRegistry.registerData(event);
        MarsRegistry.registerData(event);
        event.getGenerator().addProvider(new DimensionGenerator(event.getGenerator()));
        event.getGenerator().addProvider(new BiomeGenerator(event.getGenerator()));
    }

    public static void registerClient() {
        VenusRegistry.registerClient();
        MarsRegistry.registerClient();

        // Set dimension render info for custom dimensions
        DimensionRenderInfo.field_239208_a_ = Util.make(new Object2ObjectArrayMap<>(), (map) -> {
            DimensionRenderInfo.Overworld overworld = new DimensionRenderInfo.Overworld();
            map.defaultReturnValue(overworld);
            for (ResourceLocation key : DimensionRenderInfo.field_239208_a_.keySet()) {
                map.put(key, DimensionRenderInfo.field_239208_a_.get(key));
            }
            IForgeRegistry<Planet> planetRegistry = RegistryManager.ACTIVE.getRegistry(Planet.class);
            for (Map.Entry<RegistryKey<Planet>, Planet> entry : planetRegistry.getEntries()) {
                map.put(entry.getKey().getLocation(), new CustomDimensionRenderInfo(
                        entry.getValue().getAtmosphereRenderer().apply(entry.getValue())));
            }
        });
    }

    public static void registerLanguage(LanguageGenerator languageGenerator) {
        VenusRegistry.registerLanguage(languageGenerator);
        MarsRegistry.registerLanguage(languageGenerator);
    }

}

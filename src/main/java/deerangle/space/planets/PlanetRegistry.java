package deerangle.space.planets;

import deerangle.space.data.LanguageGenerator;
import deerangle.space.main.SpaceMod;
import deerangle.space.planets.data.DimensionGenerator;
import deerangle.space.planets.feature.Features;
import deerangle.space.planets.mars.MarsRegistry;
import deerangle.space.planets.venus.VenusRegistry;
import deerangle.space.planets.venus.render.CustomDimensionRenderInfo;
import deerangle.space.planets.venus.render.VenusAtmosphereRenderer;
import deerangle.space.planets.venus.world.VenusBiomeProvider;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.minecraft.client.world.DimensionRenderInfo;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class PlanetRegistry {

    static {
        Registry.register(Registry.BIOME_PROVIDER_CODEC, new ResourceLocation(SpaceMod.MOD_ID, "venus_provider"),
                VenusBiomeProvider.CODEC);

        DimensionRenderInfo.field_239208_a_ = Util.make(new Object2ObjectArrayMap<>(), (map) -> {
            DimensionRenderInfo.Overworld overworld = new DimensionRenderInfo.Overworld();
            map.defaultReturnValue(overworld);
            for (ResourceLocation key : DimensionRenderInfo.field_239208_a_.keySet()) {
                map.put(key, DimensionRenderInfo.field_239208_a_.get(key));
            }
            map.put(DimensionMaker.VENUS_DIMENSION_TYPE, new CustomDimensionRenderInfo(new VenusAtmosphereRenderer()));
        });
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
    }

    public static void registerClient() {
        VenusRegistry.registerClient();
        MarsRegistry.registerClient();
    }

    public static void registerLanguage(LanguageGenerator languageGenerator) {
        VenusRegistry.registerLanguage(languageGenerator);
        MarsRegistry.registerLanguage(languageGenerator);
    }

}

package deerangle.space.main;

import deerangle.space.data.*;
import deerangle.space.machine.element.MachineType;
import deerangle.space.main.proxy.ClientProxy;
import deerangle.space.main.proxy.IProxy;
import deerangle.space.main.proxy.ServerProxy;
import deerangle.space.network.PacketHandler;
import deerangle.space.planet.Planet;
import deerangle.space.planet.PlanetManager;
import deerangle.space.registry.AbstractRegistry;
import deerangle.space.registry.FluidRegistry;
import deerangle.space.registry.MachineRegistry;
import deerangle.space.registry.ResourceRegistry;
import deerangle.space.stats.Stats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegistryBuilder;

@Mod(SpaceMod.MOD_ID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class SpaceMod {

    public static final String MOD_ID = "space";

    public static IProxy proxy = DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> ServerProxy::new);

    public SpaceMod() {
        AbstractRegistry.BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        AbstractRegistry.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        AbstractRegistry.TILE_ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
        AbstractRegistry.CONTAINERS.register(FMLJavaModLoadingContext.get().getModEventBus());
        AbstractRegistry.RECIPE_SERIALIZERS.register(FMLJavaModLoadingContext.get().getModEventBus());
        AbstractRegistry.FLUIDS.register(FMLJavaModLoadingContext.get().getModEventBus());
        MachineRegistry.register();
        ResourceRegistry.register();
        FluidRegistry.register();
        Stats.register();
        PlanetManager.register();
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, ConfigData.SERVER_SPEC);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ConfigData.CLIENT_SPEC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
    }

    @SubscribeEvent
    public static void registerModConfig(ModConfig.ModConfigEvent event) {
        ModConfig config = event.getConfig();
        if (config.getSpec() == ConfigData.SERVER_SPEC) {
            ConfigData.refreshServer();
        }
        if (config.getSpec() == ConfigData.CLIENT_SPEC) {
            ConfigData.refreshClient();
        }
    }

    @SubscribeEvent
    public static void registerData(GatherDataEvent event) {
        event.getGenerator().addProvider(new BlockStateGenerator(event.getGenerator(), MOD_ID, event.getExistingFileHelper()));
        event.getGenerator().addProvider(new ItemModelGenerator(event.getGenerator(), MOD_ID, event.getExistingFileHelper()));
        event.getGenerator().addProvider(new FluidTagsGenerator(event.getGenerator(), MOD_ID, event.getExistingFileHelper()));
        event.getGenerator().addProvider(new BlockTagsGenerator(event.getGenerator(), MOD_ID, event.getExistingFileHelper()));
        event.getGenerator().addProvider(new LootTableGenerator(event.getGenerator(), MOD_ID));
        event.getGenerator().addProvider(new LanguageGenerator(event.getGenerator(), MOD_ID, "en_us"));
        event.getGenerator().addProvider(new RecipeGenerator(event.getGenerator()));
        PlanetManager.registerData(event);
    }

    private static <T> Class<T> c(Class<?> cls) {
        return (Class<T>) cls;
    }

    @SubscribeEvent
    public static void newRegistries(RegistryEvent.NewRegistry event) {
        new RegistryBuilder<MachineType<?>>().setType(c(MachineType.class)).setName(new ResourceLocation(MOD_ID, "machine")).setMaxID(Integer.MAX_VALUE - 1).create();
        new RegistryBuilder<Planet>().setType(Planet.class).setName(new ResourceLocation(MOD_ID, "planet")).setMaxID(Integer.MAX_VALUE - 1).create();
    }

    public void clientSetup(FMLClientSetupEvent event) {
        proxy.clientSetup(event);
    }

    public void commonSetup(FMLCommonSetupEvent event) {
        PacketHandler.registerPackets();
    }

}

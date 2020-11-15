package deerangle.space.main;

import deerangle.space.data.BlockStateGenerator;
import deerangle.space.data.LanguageGenerator;
import deerangle.space.data.LootTableGenerator;
import deerangle.space.registry.AbstractRegistry;
import deerangle.space.registry.MachineRegistry;
import deerangle.space.registry.ResourceRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

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
        MachineRegistry.register();
        ResourceRegistry.register();
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, ConfigData.SERVER_SPEC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
    }

    @SubscribeEvent
    public static void registerModConfig(ModConfig.ModConfigEvent event) {
        ModConfig config = event.getConfig();
        if (config.getSpec() == ConfigData.SERVER_SPEC) {
            ConfigData.refreshServer();
        }
    }

    @SubscribeEvent
    public static void registerData(GatherDataEvent event) {
        event.getGenerator()
                .addProvider(new BlockStateGenerator(event.getGenerator(), MOD_ID, event.getExistingFileHelper()));
        event.getGenerator().addProvider(new LootTableGenerator(event.getGenerator(), MOD_ID));
        event.getGenerator().addProvider(new LanguageGenerator(event.getGenerator(), MOD_ID, "en_us"));
    }

    public void clientSetup(FMLClientSetupEvent event) {
        proxy.clientSetup();
    }

}

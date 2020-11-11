package deerangle.space.main;

import deerangle.space.block.BlockRegistry;
import deerangle.space.data.BlockStateGenerator;
import deerangle.space.data.LanguageGenerator;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(SpaceMod.MOD_ID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class SpaceMod {

    public static final String MOD_ID = "space";

    public static final ItemGroup BLOCKS_TAB = new ItemGroup(MOD_ID + ".blocks") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(BlockRegistry.COPPER_ORE.get());
        }
    };

    public SpaceMod() {
        BlockRegistry.BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        BlockRegistry.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, ConfigData.SERVER_SPEC);
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
        event.getGenerator().addProvider(new LanguageGenerator(event.getGenerator(), MOD_ID, "en_us"));
    }

}

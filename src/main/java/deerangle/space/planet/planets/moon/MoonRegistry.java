package deerangle.space.planet.planets.moon;

import deerangle.space.data.LanguageGenerator;
import deerangle.space.main.SpaceMod;
import deerangle.space.planet.PlanetManager;
import deerangle.space.planet.planets.moon.data.BlockStateGenerator;
import deerangle.space.planet.planets.moon.data.LootTableGenerator;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.item.*;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class MoonRegistry {

    private static final DeferredRegister<Block> BLOCKS = DeferredRegister
            .create(ForgeRegistries.BLOCKS, SpaceMod.MOD_ID);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SpaceMod.MOD_ID);

    public static final RegistryObject<Block> DUST = BLOCKS.register("dust", () -> new FallingBlock(
            AbstractBlock.Properties.create(Material.SAND).harvestTool(ToolType.SHOVEL).sound(SoundType.SAND)
            .hardnessAndResistance(1.5F, 6.0F)));

    static {
        ITEMS.register("dust", () -> new BlockItem(DUST.get(), new Item.Properties().group(PlanetManager.TAB)));
    }

    public static void register() {
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static void registerData(GatherDataEvent event) {
        event.getGenerator().addProvider(new LootTableGenerator(event.getGenerator(), SpaceMod.MOD_ID));
        event.getGenerator().addProvider(new BlockStateGenerator(event.getGenerator(), SpaceMod.MOD_ID, event.getExistingFileHelper()));
    }

    public static void registerLanguage(LanguageGenerator gen) {
        gen.add(DUST.get(), "Dust");
    }

    public static void registerClient() {

    }

}

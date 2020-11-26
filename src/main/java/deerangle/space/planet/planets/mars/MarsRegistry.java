package deerangle.space.planet.planets.mars;

import deerangle.space.data.LanguageGenerator;
import deerangle.space.main.SpaceMod;
import deerangle.space.planet.PlanetManager;
import deerangle.space.planet.planets.mars.data.BlockStateGenerator;
import deerangle.space.planet.planets.mars.data.RecipeGenerator;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.FallingBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class MarsRegistry {

    private static final DeferredRegister<Block> BLOCKS = DeferredRegister
            .create(ForgeRegistries.BLOCKS, SpaceMod.MOD_ID);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SpaceMod.MOD_ID);

    public static final RegistryObject<Block> RUSTY_DUST = BLOCKS.register("rusty_dust", () -> new FallingBlock(
            AbstractBlock.Properties.create(Material.SAND).harvestTool(ToolType.SHOVEL).sound(SoundType.SAND)
                    .hardnessAndResistance(1.0F, 1.0F)));
    public static final RegistryObject<Block> REGOLITH = BLOCKS.register("regolith", () -> new Block(
            AbstractBlock.Properties.create(Material.ROCK).setRequiresTool().hardnessAndResistance(1.5F, 4.0F)));

    static {
        ITEMS.register("rusty_dust",
                () -> new BlockItem(RUSTY_DUST.get(), new Item.Properties().group(PlanetManager.TAB)));
        ITEMS.register("regolith",
                () -> new BlockItem(REGOLITH.get(), new Item.Properties().group(PlanetManager.TAB)));
    }

    public static void register() {
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static void registerData(GatherDataEvent event) {
        event.getGenerator().addProvider(
                new BlockStateGenerator(event.getGenerator(), SpaceMod.MOD_ID, event.getExistingFileHelper()));
        event.getGenerator().addProvider(new RecipeGenerator(event.getGenerator()));
    }

    public static void registerClient() {
    }

    public static void registerLanguage(LanguageGenerator gen) {
        gen.add(RUSTY_DUST.get(), "Rusty Dust");
        gen.add(REGOLITH.get(), "Regolith");
    }

}

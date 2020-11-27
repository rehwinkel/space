package deerangle.space.planet.planets.mercury;

import deerangle.space.data.LanguageGenerator;
import deerangle.space.main.SpaceMod;
import deerangle.space.planet.PlanetManager;
import deerangle.space.planet.planets.mercury.data.BlockStateGenerator;
import deerangle.space.planet.planets.mercury.data.LootTableGenerator;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.item.*;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class MercuryRegistry {
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister
            .create(ForgeRegistries.BLOCKS, SpaceMod.MOD_ID);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SpaceMod.MOD_ID);

    public static final RegistryObject<Block> FIRESTONE = BLOCKS.register("firestone", () -> new Block(
            AbstractBlock.Properties.create(Material.ROCK).setRequiresTool().hardnessAndResistance(1.5F, 6.0F)));
    //TODO: bedrock-like
    public static final RegistryObject<Block> PLANET_CORE = BLOCKS.register("planet_core", () -> new Block(
            AbstractBlock.Properties.create(Material.ROCK)));

    static {
        ITEMS.register("firestone", () -> new BlockItem(FIRESTONE.get(), new Item.Properties().group(PlanetManager.TAB)));
        ITEMS.register("planet_core", () -> new BlockItem(PLANET_CORE.get(), new Item.Properties().group(PlanetManager.TAB)));
    }

    public static void register() {
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static void registerData(GatherDataEvent event) {
        //instanciate data generators from mercury/data/
        event.getGenerator().addProvider(new LootTableGenerator(event.getGenerator(), SpaceMod.MOD_ID));
        event.getGenerator().addProvider(new BlockStateGenerator(event.getGenerator(), SpaceMod.MOD_ID, event.getExistingFileHelper()));
    }

    public static void registerLanguage(LanguageGenerator gen) {
        gen.add(FIRESTONE.get(), "Firestone");
        gen.add(PLANET_CORE.get(), "Planet Core");
    }
}

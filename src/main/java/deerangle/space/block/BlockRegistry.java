package deerangle.space.block;

import deerangle.space.main.SpaceMod;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.OreBlock;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BlockRegistry {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister
            .create(ForgeRegistries.BLOCKS, SpaceMod.MOD_ID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SpaceMod.MOD_ID);

    public static final RegistryObject<Block> COPPER_ORE = BLOCKS.register("copper_ore", () -> new OreBlock(
            AbstractBlock.Properties.create(Material.ROCK).setRequiresTool().harvestLevel(1)
                    .hardnessAndResistance(3.0F, 3.0F)));

    public static final RegistryObject<Block> ALUMINIUM_ORE = BLOCKS.register("aluminium_ore", () -> new OreBlock(
            AbstractBlock.Properties.create(Material.ROCK).setRequiresTool().harvestLevel(1)
                    .hardnessAndResistance(3.0F, 3.0F)));

    static {
        ITEMS.register("copper_ore",
                () -> new BlockItem(COPPER_ORE.get(), new Item.Properties().group(SpaceMod.BLOCKS_TAB)));
        ITEMS.register("aluminium_ore",
                () -> new BlockItem(ALUMINIUM_ORE.get(), new Item.Properties().group(SpaceMod.BLOCKS_TAB)));
    }

}

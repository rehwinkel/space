package deerangle.space.registry;

import deerangle.space.main.SpaceMod;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.OreBlock;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.RegistryObject;

public class ResourceRegistry extends AbstractRegistry {

    public static final ItemGroup TAB = new ItemGroup(SpaceMod.MOD_ID + ".resource") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(COPPER_ORE.get());
        }
    };

    public static final RegistryObject<Block> COPPER_ORE = BLOCKS.register("copper_ore", () -> new OreBlock(
            AbstractBlock.Properties.create(Material.ROCK).setRequiresTool().harvestLevel(1)
                    .hardnessAndResistance(3.0F, 3.0F)));

    public static final RegistryObject<Block> ALUMINIUM_ORE = BLOCKS.register("aluminium_ore", () -> new OreBlock(
            AbstractBlock.Properties.create(Material.ROCK).setRequiresTool().harvestLevel(1)
                    .hardnessAndResistance(3.0F, 3.0F)));

    static {
        ITEMS.register("copper_ore", () -> new BlockItem(COPPER_ORE.get(), new Item.Properties().group(TAB)));
        ITEMS.register("aluminium_ore", () -> new BlockItem(ALUMINIUM_ORE.get(), new Item.Properties().group(TAB)));
    }

    public static void register() {
    }

}

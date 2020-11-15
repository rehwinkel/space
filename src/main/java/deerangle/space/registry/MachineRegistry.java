package deerangle.space.registry;

import deerangle.space.block.CoalGeneratorBlock;
import deerangle.space.block.entity.CoalGeneratorTE;
import deerangle.space.container.CoalGeneratorContainer;
import deerangle.space.main.SpaceMod;
import deerangle.space.screen.CoalGeneratorScreen;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.gui.screen.inventory.FurnaceScreen;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;

public class MachineRegistry extends AbstractRegistry {

    public static final ItemGroup TAB = new ItemGroup(SpaceMod.MOD_ID + ".machine") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(COAL_GENERATOR.get());
        }
    };

    public static final RegistryObject<Block> COAL_GENERATOR = BLOCKS
            .register("coal_generator", () -> new CoalGeneratorBlock(AbstractBlock.Properties.create(Material.IRON)));

    public static final RegistryObject<TileEntityType<CoalGeneratorTE>> COAL_GENERATOR_TE = TILE_ENTITIES
            .register("coal_generator",
                    () -> TileEntityType.Builder.create(CoalGeneratorTE::new, COAL_GENERATOR.get()).build(null));

    public static final RegistryObject<ContainerType<CoalGeneratorContainer>> COAL_GENERATOR_CONTAINER = CONTAINERS
            .register("coal_generator", () -> IForgeContainerType.create(CoalGeneratorContainer::new));

    static {
        ITEMS.register("coal_generator", () -> new BlockItem(COAL_GENERATOR.get(), new Item.Properties().group(TAB)));
    }

    public static void register() {

    }

}

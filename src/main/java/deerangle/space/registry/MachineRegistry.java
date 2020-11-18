package deerangle.space.registry;

import deerangle.space.block.MachineBlock;
import deerangle.space.block.entity.MachineTileEntity;
import deerangle.space.container.MachineContainer;
import deerangle.space.main.SpaceMod;
import deerangle.space.stats.Stats;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityType;
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
            .register("coal_generator", () -> new MachineBlock(AbstractBlock.Properties.create(Material.IRON), Stats.INTERACT_WITH_COAL_GENERATOR));

    public static final RegistryObject<TileEntityType<MachineTileEntity>> MACHINE_TE = TILE_ENTITIES
            .register("machine",
                    () -> TileEntityType.Builder.create(MachineTileEntity::new, COAL_GENERATOR.get()).build(null));

    public static final RegistryObject<ContainerType<MachineContainer>> MACHINE_CONTAINER = CONTAINERS
            .register("machine", () -> IForgeContainerType.create(MachineContainer::new));

    static {
        ITEMS.register("coal_generator", () -> new BlockItem(COAL_GENERATOR.get(), new Item.Properties().group(TAB)));
    }

    public static void register() {

    }

}

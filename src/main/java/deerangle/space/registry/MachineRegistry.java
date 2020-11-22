package deerangle.space.registry;

import deerangle.space.block.*;
import deerangle.space.block.entity.MachineTileEntity;
import deerangle.space.container.MachineContainer;
import deerangle.space.item.MachineItem;
import deerangle.space.main.SpaceMod;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;

import java.util.function.ToIntFunction;

public class MachineRegistry extends AbstractRegistry {

    public static final RegistryObject<Block> COAL_GENERATOR = BLOCKS.register("coal_generator",
            () -> new CoalGeneratorBlock(
                    AbstractBlock.Properties.create(Material.IRON).setLightLevel(getRunningLightLevel(13))));
    public static final ItemGroup TAB = new ItemGroup(SpaceMod.MOD_ID + ".machine") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(COAL_GENERATOR.get());
        }
    };
    public static final RegistryObject<Block> BLAST_FURNACE = BLOCKS.register("blast_furnace",
            () -> new BlastFurnaceBlock(
                    AbstractBlock.Properties.create(Material.IRON).setLightLevel(getRunningLightLevel(15))));

    public static final RegistryObject<Block> COMBUSTION_GENERATOR = BLOCKS.register("combustion_generator",
            () -> new CombustionGeneratorBlock(AbstractBlock.Properties.create(Material.IRON)));

    public static final RegistryObject<Block> GAS_TANK = BLOCKS
            .register("gas_tank", () -> new GasTankBlock(AbstractBlock.Properties.create(Material.IRON)));

    public static final RegistryObject<Block> DRUM = BLOCKS
            .register("drum", () -> new DrumBlock(AbstractBlock.Properties.create(Material.IRON)));

    public static final RegistryObject<Block> BATTERY_PACK = BLOCKS
            .register("battery_pack", () -> new BatteryPackBlock(AbstractBlock.Properties.create(Material.IRON)));

    public static final RegistryObject<Block> REFINERY = BLOCKS
            .register("refinery", () -> new RefineryBlock(AbstractBlock.Properties.create(Material.IRON)));
    public static final RegistryObject<TileEntityType<MachineTileEntity>> MACHINE_TE = TILE_ENTITIES.register("machine",
            () -> TileEntityType.Builder.create(MachineTileEntity::new, COAL_GENERATOR.get(), BLAST_FURNACE.get(),
                    COMBUSTION_GENERATOR.get(), GAS_TANK.get(), DRUM.get(), BATTERY_PACK.get(), REFINERY.get())
                    .build(null));
    public static final RegistryObject<ContainerType<MachineContainer>> MACHINE_CONTAINER = CONTAINERS
            .register("machine", () -> IForgeContainerType.create(MachineContainer::new));

    static {
        ITEMS.register("coal_generator", () -> new MachineItem(COAL_GENERATOR.get(), new Item.Properties().group(TAB)));
        ITEMS.register("blast_furnace", () -> new MachineItem(BLAST_FURNACE.get(), new Item.Properties().group(TAB)));
        ITEMS.register("combustion_generator",
                () -> new MachineItem(COMBUSTION_GENERATOR.get(), new Item.Properties().group(TAB)));
        ITEMS.register("gas_tank", () -> new MachineItem(GAS_TANK.get(), new Item.Properties().group(TAB)));
        ITEMS.register("drum", () -> new MachineItem(DRUM.get(), new Item.Properties().group(TAB)));
        ITEMS.register("battery_pack", () -> new MachineItem(BATTERY_PACK.get(), new Item.Properties().group(TAB)));
        ITEMS.register("refinery", () -> new MachineItem(REFINERY.get(), new Item.Properties().group(TAB)));
    }

    private static ToIntFunction<BlockState> getRunningLightLevel(int light) {
        return state -> state.get(MachineBlock.RUNNING) ? light : 0;
    }

    public static void register() {

    }

}

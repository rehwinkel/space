package deerangle.space.registry;

import deerangle.space.fluid.CrudeOilFluid;
import deerangle.space.fluid.KeroseneFluid;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;

public class FluidRegistry extends AbstractRegistry {

    public static final RegistryObject<FlowingFluid> CRUDE_OIL = FLUIDS
            .register("crude_oil", CrudeOilFluid.Source::new);
    public static final RegistryObject<FlowingFluid> CRUDE_OIL_FLOWING = FLUIDS
            .register("crude_oil_flowing", CrudeOilFluid.Flowing::new);
    public static final RegistryObject<BucketItem> CRUDE_OIL_BUCKET = ITEMS.register("crude_oil_bucket",
            () -> new BucketItem(CRUDE_OIL, new Item.Properties().group(ResourceRegistry.TAB).maxStackSize(1)));
    public static final RegistryObject<Block> CRUDE_OIL_BLOCK = BLOCKS.register("crude_oil",
            () -> new FlowingFluidBlock(CRUDE_OIL,
                    AbstractBlock.Properties.create(Material.LAVA, MaterialColor.BLACK).doesNotBlockMovement()
                            .hardnessAndResistance(100.0F)));
    public static final RegistryObject<FlowingFluid> KEROSENE = FLUIDS.register("kerosene", KeroseneFluid.Source::new);
    public static final RegistryObject<FlowingFluid> KEROSENE_FLOWING = FLUIDS
            .register("kerosene_flowing", KeroseneFluid.Flowing::new);
    public static final RegistryObject<BucketItem> KEROSENE_BUCKET = ITEMS.register("kerosene_bucket",
            () -> new BucketItem(KEROSENE, new Item.Properties().group(ResourceRegistry.TAB).maxStackSize(1)));
    public static final RegistryObject<Block> KEROSENE_BLOCK = BLOCKS.register("kerosene",
            () -> new FlowingFluidBlock(KEROSENE,
                    AbstractBlock.Properties.create(Material.LAVA, MaterialColor.BLACK).doesNotBlockMovement()
                            .hardnessAndResistance(100.0F)));

    public static void register() {
    }

}

package deerangle.space.fluid;

import deerangle.space.main.SpaceMod;
import deerangle.space.registry.FluidRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Item;
import net.minecraft.state.StateContainer;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraftforge.common.Tags;
import net.minecraftforge.fluids.FluidAttributes;

public class CrudeOilFluid extends FlowingFluid {

    @Override
    protected FluidAttributes createAttributes() {
        return FluidAttributes.builder(new ResourceLocation(SpaceMod.MOD_ID, "block/crude_oil_still"),
                new ResourceLocation(SpaceMod.MOD_ID, "block/crude_oil_flow"))
                .overlay(new ResourceLocation(SpaceMod.MOD_ID, "block/crude_oil_overlay"))
                .translationKey("block.space.crude_oil").density(870).viscosity(4000)
                .sound(SoundEvents.ITEM_BUCKET_FILL, SoundEvents.ITEM_BUCKET_EMPTY).color(0xFF202020).build(this);
    }

    @Override
    public Fluid getFlowingFluid() {
        return FluidRegistry.CRUDE_OIL_FLOWING.get();
    }

    @Override
    public Fluid getStillFluid() {
        return FluidRegistry.CRUDE_OIL.get();
    }

    @Override
    public Item getFilledBucket() {
        return FluidRegistry.CRUDE_OIL_BUCKET.get();
    }

    @Override
    public boolean isEquivalentTo(Fluid fluidIn) {
        return fluidIn == FluidRegistry.CRUDE_OIL.get() || fluidIn == FluidRegistry.CRUDE_OIL_FLOWING.get();
    }

    @Override
    protected boolean canSourcesMultiply() {
        return false;
    }

    @Override
    protected void beforeReplacingBlock(IWorld worldIn, BlockPos pos, BlockState state) {
    }

    @Override
    protected int getSlopeFindDistance(IWorldReader worldIn) {
        return 2;
    }

    @Override
    protected int getLevelDecreasePerBlock(IWorldReader worldIn) {
        return 2;
    }

    @Override
    protected boolean canDisplace(FluidState fluidState, IBlockReader blockReader, BlockPos pos, Fluid fluid, Direction direction) {
        return fluidState.getActualHeight(blockReader, pos) >= 0.44444445F && fluid.isIn(FluidTags.WATER);
    }

    @Override
    public int getTickRate(IWorldReader p_205569_1_) {
        return 15;
    }

    @Override
    protected float getExplosionResistance() {
        return 100.0F;
    }

    @Override
    protected BlockState getBlockState(FluidState state) {
        return FluidRegistry.CRUDE_OIL_BLOCK.get().getDefaultState()
                .with(FlowingFluidBlock.LEVEL, getLevelFromState(state));
    }

    @Override
    public boolean isSource(FluidState state) {
        return false;
    }

    @Override
    public int getLevel(FluidState state) {
        return 0;
    }

    public static class Flowing extends CrudeOilFluid {
        protected void fillStateContainer(StateContainer.Builder<Fluid, FluidState> builder) {
            super.fillStateContainer(builder);
            builder.add(LEVEL_1_8);
        }

        public int getLevel(FluidState state) {
            return state.get(LEVEL_1_8);
        }

        public boolean isSource(FluidState state) {
            return false;
        }
    }

    public static class Source extends CrudeOilFluid {
        public int getLevel(FluidState state) {
            return 8;
        }

        public boolean isSource(FluidState state) {
            return true;
        }
    }

}

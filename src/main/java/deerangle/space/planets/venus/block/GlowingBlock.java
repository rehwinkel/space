package deerangle.space.planets.venus.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public class GlowingBlock extends Block {

    public GlowingBlock(AbstractBlock.Properties properties) {
        super(properties);
    }

    public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn) {
        if (!entityIn.isImmuneToFire() && entityIn instanceof LivingEntity && !EnchantmentHelper
                .hasFrostWalker((LivingEntity) entityIn)) {
            entityIn.attackEntityFrom(DamageSource.HOT_FLOOR, 1.0F);
        }
        super.onEntityWalk(worldIn, pos, entityIn);
    }

    @Override
    public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
        if (worldIn.isAirBlock(pos.up())) {
            if (rand.nextFloat() < 0.1f) {
                worldIn.setBlockState(pos.up(), Blocks.FIRE.getDefaultState());
            }
        }
    }

}

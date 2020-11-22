package deerangle.space.tags;

import deerangle.space.main.SpaceMod;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;

public class BlockTags {

    public static final Tags.IOptionalNamedTag<Block> MACHINES = tag("machines");

    private static Tags.IOptionalNamedTag<Block> tag(String name) {
        return net.minecraft.tags.BlockTags.createOptional(new ResourceLocation(SpaceMod.MOD_ID, name));
    }

}

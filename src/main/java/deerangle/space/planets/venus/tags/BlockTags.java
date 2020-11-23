package deerangle.space.planets.venus.tags;

import deerangle.space.main.SpaceMod;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;

public class BlockTags {

    public static final Tags.IOptionalNamedTag<Block> VENUS_GROUND = tag("venus_ground");
    public static final Tags.IOptionalNamedTag<Block> VENUS_OVERGROWABLE = tag("venus_overgrowable");

    private static Tags.IOptionalNamedTag<Block> tag(String name) {
        return net.minecraft.tags.BlockTags.createOptional(new ResourceLocation(SpaceMod.MOD_ID, name));
    }

}

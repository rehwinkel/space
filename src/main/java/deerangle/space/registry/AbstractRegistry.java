package deerangle.space.registry;

import deerangle.space.main.SpaceMod;
import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public abstract class AbstractRegistry {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister
            .create(ForgeRegistries.BLOCKS, SpaceMod.MOD_ID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SpaceMod.MOD_ID);
    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = DeferredRegister
            .create(ForgeRegistries.TILE_ENTITIES, SpaceMod.MOD_ID);
    public static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister
            .create(ForgeRegistries.CONTAINERS, SpaceMod.MOD_ID);
    public static final DeferredRegister<IRecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister
            .create(ForgeRegistries.RECIPE_SERIALIZERS, SpaceMod.MOD_ID);
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister
            .create(ForgeRegistries.FLUIDS, SpaceMod.MOD_ID);

}

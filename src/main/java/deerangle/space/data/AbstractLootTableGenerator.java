package deerangle.space.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.advancements.criterion.EnchantmentPredicate;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.advancements.criterion.StatePropertiesPredicate;
import net.minecraft.block.Block;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.SlabBlock;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Items;
import net.minecraft.loot.*;
import net.minecraft.loot.conditions.*;
import net.minecraft.loot.functions.ApplyBonus;
import net.minecraft.loot.functions.ExplosionDecay;
import net.minecraft.loot.functions.SetCount;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.state.properties.SlabType;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;

public abstract class AbstractLootTableGenerator implements IDataProvider {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
    private final String mod_id;
    private final DataGenerator generator;
    private final HashMap<ResourceLocation, LootTable> tables;

    public AbstractLootTableGenerator(DataGenerator generator, String mod_id) {
        this.generator = generator;
        this.mod_id = mod_id;
        this.tables = new HashMap<>();
    }

    private static Path getPath(Path pathIn, ResourceLocation id) {
        return pathIn.resolve("data/" + id.getNamespace() + "/loot_tables/" + id.getPath() + ".json");
    }

    public void normalBlock(Block block) {
        normalBlock(block, block);
    }

    public void normalBlock(Block block, IItemProvider drop) {
        LootTable value = LootTable.builder().addLootPool(LootPool.builder().rolls(ConstantRange.of(1)).addEntry(ItemLootEntry.builder(drop)).acceptCondition(SurvivesExplosion.builder())).build();
        putBlock(block.getRegistryName(), value);
    }

    public void slabBlock(Block block) {
        LootTable value = LootTable.builder().addLootPool(LootPool.builder().rolls(ConstantRange.of(1)).addEntry(ItemLootEntry.builder(block).acceptFunction(ExplosionDecay.builder()).acceptFunction(SetCount.builder(ConstantRange.of(2)).acceptCondition(BlockStateProperty.builder(block).fromProperties(StatePropertiesPredicate.Builder.newBuilder().withProp(SlabBlock.TYPE, SlabType.DOUBLE)))))).build();
        putBlock(block.getRegistryName(), value);
    }

    public void grassBlock(Block block) {
        LootTable value = LootTable.builder().addLootPool(LootPool.builder().rolls(ConstantRange.of(1)).addEntry(AlternativesLootEntry.builder(ItemLootEntry.builder(block).acceptCondition(MatchTool.builder(ItemPredicate.Builder.create().item(Items.SHEARS)))))).build();
        putBlock(block.getRegistryName(), value);
    }

    protected void oreBlock(Block block, IItemProvider result) {
        LootTable value = LootTable.builder().addLootPool(LootPool.builder().rolls(ConstantRange.of(1)).addEntry(AlternativesLootEntry.builder(ItemLootEntry.builder(block).acceptCondition(MatchTool.builder(ItemPredicate.Builder.create().enchantment(new EnchantmentPredicate(Enchantments.SILK_TOUCH, MinMaxBounds.IntBound.atLeast(1))))), ItemLootEntry.builder(result).acceptFunction(ApplyBonus.oreDrops(Enchantments.FORTUNE)).acceptFunction(ExplosionDecay.builder())))).build();
        putBlock(block.getRegistryName(), value);
    }

    protected void doorBlock(Block block, IItemProvider doorItem) {
        LootTable value = LootTable.builder().addLootPool(LootPool.builder().rolls(ConstantRange.of(1)).addEntry(ItemLootEntry.builder(doorItem).acceptCondition(BlockStateProperty.builder(block).fromProperties(StatePropertiesPredicate.Builder.newBuilder().withProp(DoorBlock.HALF, DoubleBlockHalf.LOWER)))).acceptCondition(SurvivesExplosion.builder())).build();
        putBlock(block.getRegistryName(), value);
    }

    protected void leavesBlock(Block block, IItemProvider sapling) {
        LootTable.Builder builder = LootTable.builder().setParameterSet(LootParameterSets.BLOCK).addLootPool(LootPool.builder().rolls(ConstantRange.of(1)).addEntry(AlternativesLootEntry.builder(ItemLootEntry.builder(block).acceptCondition(Alternative.builder(MatchTool.builder(ItemPredicate.Builder.create().item(Items.SHEARS)), MatchTool.builder(ItemPredicate.Builder.create().enchantment(new EnchantmentPredicate(Enchantments.SILK_TOUCH, MinMaxBounds.IntBound.atLeast(1)))))), ItemLootEntry.builder(sapling).acceptCondition(SurvivesExplosion.builder()).acceptCondition(TableBonus.builder(Enchantments.FORTUNE, 1f / 20f, 1f / 16f, 1f / 12f, 1f / 10f)))))
                .addLootPool(LootPool.builder().rolls(ConstantRange.of(1)).addEntry(ItemLootEntry.builder(Items.STICK).acceptCondition(TableBonus.builder(Enchantments.FORTUNE, 1f / 50f, 1f / 45f, 1f / 40f, 1f / 30f, 1f / 10f)).acceptFunction(SetCount.builder(RandomValueRange.of(1.0f, 2.0f))).acceptFunction(ExplosionDecay.builder())).acceptCondition(Inverted.builder(Alternative.builder(MatchTool.builder(ItemPredicate.Builder.create().item(Items.SHEARS)), MatchTool.builder(ItemPredicate.Builder.create().enchantment(new EnchantmentPredicate(Enchantments.SILK_TOUCH, MinMaxBounds.IntBound.atLeast(1))))))));
        putBlock(block.getRegistryName(), builder.build());
    }

    private void putBlock(ResourceLocation registryName, LootTable lootTable) {
        this.tables.put(new ResourceLocation(registryName.getNamespace(), "blocks/" + registryName.getPath()), lootTable);
    }

    protected abstract void populate();

    @Override
    public void act(DirectoryCache cache) throws IOException {
        Path path = this.generator.getOutputFolder();
        this.populate();
        this.tables.forEach((location, lootTable) -> {
            Path path1 = getPath(path, location);

            try {
                IDataProvider.save(GSON, cache, LootTableManager.toJson(lootTable), path1);
            } catch (IOException ioexception) {
                LOGGER.error("Couldn't save loot table {}", path1, ioexception);
            }
        });
    }

    @Override
    public String getName() {
        return "LootTables: " + this.mod_id;
    }

}

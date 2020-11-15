package deerangle.space.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.loot.*;
import net.minecraft.loot.conditions.SurvivesExplosion;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;

public abstract class AbstractLootTableGenerator implements IDataProvider {

    private final String mod_id;
    private final DataGenerator generator;
    private final HashMap<ResourceLocation, LootTable> tables;

    private static final Logger LOGGER = LogManager.getLogger();
    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();

    public AbstractLootTableGenerator(DataGenerator generator, String mod_id) {
        this.generator = generator;
        this.mod_id = mod_id;
        this.tables = new HashMap<>();
    }

    public void normalBlock(Block block) {
        LootTable value = LootTable.builder().addLootPool(
                LootPool.builder().rolls(ConstantRange.of(1)).addEntry(ItemLootEntry.builder(block))
                        .acceptCondition(SurvivesExplosion.builder())).build();
        tables.put(block.getRegistryName(), value);
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

    private static Path getPath(Path pathIn, ResourceLocation id) {
        return pathIn.resolve("data/" + id.getNamespace() + "/loot_tables/" + id.getPath() + ".json");
    }

    @Override
    public String getName() {
        return "LootTables: " + this.mod_id;
    }

}

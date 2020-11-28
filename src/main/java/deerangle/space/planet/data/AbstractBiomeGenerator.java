package deerangle.space.planet.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class AbstractBiomeGenerator implements IDataProvider {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();
    private final DataGenerator generator;

    public AbstractBiomeGenerator(DataGenerator generator) {
        this.generator = generator;
    }

    private static Path getPath(Path path, ResourceLocation biomeLocation) {
        return path.resolve("data/" + biomeLocation.getNamespace() + "/worldgen/biome/" + biomeLocation.getPath() + ".json");
    }

    public void act(DirectoryCache cache) {
        Path path = this.generator.getOutputFolder();

        Map<ResourceLocation, Biome> biomeMap = new HashMap<>();
        addBiomes(biomeMap);

        for (Map.Entry<ResourceLocation, Biome> entry : biomeMap.entrySet()) {
            Path path1 = getPath(path, entry.getKey());
            Biome biome = entry.getValue();
            Function<Supplier<Biome>, DataResult<JsonElement>> function = JsonOps.INSTANCE.withEncoder(Biome.BIOME_CODEC);

            try {
                Optional<JsonElement> optional = function.apply(() -> biome).result();
                if (optional.isPresent()) {
                    IDataProvider.save(GSON, cache, optional.get(), path1);
                } else {
                    LOGGER.error("Couldn't serialize biome {}", path1);
                }
            } catch (IOException ioexception) {
                LOGGER.error("Couldn't save biome {}", path1, ioexception);
            }
        }

    }

    protected abstract void addBiomes(Map<ResourceLocation, Biome> biomeMap);

    public String getName() {
        return "Modded Biomes";
    }

}


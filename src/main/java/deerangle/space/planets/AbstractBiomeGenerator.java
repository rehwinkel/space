package deerangle.space.planets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.fluid.WaterFluid;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.DimensionSettings;
import net.minecraft.world.gen.settings.NoiseSettings;
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

    public void act(DirectoryCache cache) {
        Path path = this.generator.getOutputFolder();

        Map<RegistryKey<Biome>, Biome> biomeMap = new HashMap<>();
        addBiomes(biomeMap);

        for (Map.Entry<RegistryKey<Biome>, Biome> entry : biomeMap.entrySet()) {
            Path path1 = getPath(path, entry.getKey().getLocation());
            Biome biome = entry.getValue();
            Function<Supplier<Biome>, DataResult<JsonElement>> function = JsonOps.INSTANCE
                    .withEncoder(Biome.BIOME_CODEC);

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

    protected abstract void addBiomes(Map<RegistryKey<Biome>, Biome> biomeMap);

    private static Path getPath(Path path, ResourceLocation biomeLocation) {
        return path.resolve("reports/" + biomeLocation.getNamespace() + "/biomes/" + biomeLocation.getPath() + ".json");
    }

    public String getName() {
        return "Modded Biomes";
    }

}


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
import net.minecraft.world.Dimension;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public abstract class AbstractDimensionGenerator implements IDataProvider {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();
    private final DataGenerator generator;

    public AbstractDimensionGenerator(DataGenerator generator) {
        this.generator = generator;
    }

    private static Path getPath(Path path, ResourceLocation dimensionLocation) {
        return path.resolve("reports/" + dimensionLocation.getNamespace() + "/dimension/" + dimensionLocation.getPath() + ".json");
    }

    public void act(DirectoryCache cache) {
        Path path = this.generator.getOutputFolder();

        Map<ResourceLocation, Dimension> biomeMap = new HashMap<>();
        addDimensions(biomeMap);

        for (Map.Entry<ResourceLocation, Dimension> entry : biomeMap.entrySet()) {
            Path path1 = getPath(path, entry.getKey());
            Dimension dimension = entry.getValue();

            Function<Dimension, DataResult<JsonElement>> function = JsonOps.INSTANCE.withEncoder(Dimension.CODEC);

            try {
                Optional<JsonElement> optional = function.apply(dimension).result();
                if (optional.isPresent()) {
                    IDataProvider.save(GSON, cache, optional.get(), path1);
                } else {
                    LOGGER.error("Couldn't serialize dimension {}", path1);
                }
            } catch (IOException ioexception) {
                LOGGER.error("Couldn't save dimension {}", path1, ioexception);
            }
        }

    }

    protected abstract void addDimensions(Map<ResourceLocation, Dimension> dimensionMap);

    public String getName() {
        return "Modded Dimensions";
    }

}

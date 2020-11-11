package deerangle.space.main;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class ConfigData {

    public static final ServerConfig SERVER;
    public static final ForgeConfigSpec SERVER_SPEC;

    static {
        final Pair<ServerConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(ServerConfig::new);
        SERVER = specPair.getLeft();
        SERVER_SPEC = specPair.getRight();
    }

    public static boolean spawnCopper;
    public static boolean spawnAluminium;

    public static class ServerConfig {
        private final ForgeConfigSpec.BooleanValue spawnCopper;
        private final ForgeConfigSpec.BooleanValue spawnAluminium;

        private ServerConfig(ForgeConfigSpec.Builder builder) {
            builder.push("worldgen");
            spawnCopper = builder.comment("Determines whether or not copper ore should spawn.")
                    .define("spawn_copper", true);
            spawnAluminium = builder.comment("Determines whether or not aluminium ore should spawn.")
                    .define("spawn_aluminium", true);
            builder.pop();
        }
    }

    public static void refreshServer() {
        spawnCopper = SERVER.spawnCopper.get();
        spawnAluminium = SERVER.spawnAluminium.get();
    }
}

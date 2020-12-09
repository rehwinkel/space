package deerangle.space.main;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class ConfigData {

    public static final ClientConfig CLIENT;
    public static final ServerConfig SERVER;
    public static final ForgeConfigSpec SERVER_SPEC;
    public static final ForgeConfigSpec CLIENT_SPEC;
    public static boolean spawnCopper;
    public static boolean spawnAluminium;
    public static boolean doMachineParticles;
    //TODO: rework config

    static {
        final Pair<ServerConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(ServerConfig::new);
        SERVER = specPair.getLeft();
        SERVER_SPEC = specPair.getRight();
        final Pair<ClientConfig, ForgeConfigSpec> specPairClient = new ForgeConfigSpec.Builder().configure(ClientConfig::new);
        CLIENT = specPairClient.getLeft();
        CLIENT_SPEC = specPairClient.getRight();
    }

    public static void refreshServer() {
        spawnCopper = SERVER.spawnCopper.get();
        spawnAluminium = SERVER.spawnAluminium.get();
    }

    public static void refreshClient() {
        doMachineParticles = CLIENT.doMachineParticles.get();
    }

    public static class ServerConfig {
        private final ForgeConfigSpec.BooleanValue spawnCopper;
        private final ForgeConfigSpec.BooleanValue spawnAluminium;

        private ServerConfig(ForgeConfigSpec.Builder builder) {
            builder.push("worldgen");
            spawnCopper = builder.comment("Determines whether or not copper ore should spawn.").define("spawn_copper", true);
            spawnAluminium = builder.comment("Determines whether or not aluminium ore should spawn.").define("spawn_aluminium", true);
            builder.pop();
        }
    }

    private static class ClientConfig {
        private final ForgeConfigSpec.BooleanValue doMachineParticles;

        private ClientConfig(ForgeConfigSpec.Builder builder) {
            builder.push("rendering");
            doMachineParticles = builder.comment("Determines whether or not machines should emit particles.").define("do_machine_particles", true);
            builder.pop();
        }
    }

}

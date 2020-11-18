package deerangle.space.network;

import deerangle.space.main.SpaceMod;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class PacketHandler {

    private static final String PROTO_VERSION = "1";

    public static SimpleChannel INSTANCE = NetworkRegistry
            .newSimpleChannel(new ResourceLocation(SpaceMod.MOD_ID, "main"), () -> PROTO_VERSION, PROTO_VERSION::equals,
                    PROTO_VERSION::equals);

    public static void registerPackets() {
        INSTANCE.registerMessage(0, UpdateMachineMsg.class, UpdateMachineMsg::serialize, UpdateMachineMsg::deserialize, UpdateMachineMsg::handle);
        INSTANCE.registerMessage(1, AdvanceSideMsg.class, AdvanceSideMsg::serialize, AdvanceSideMsg::deserialize, AdvanceSideMsg::handle);
    }

}

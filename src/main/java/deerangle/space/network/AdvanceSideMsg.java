package deerangle.space.network;

import deerangle.space.block.entity.MachineTileEntity;
import deerangle.space.machine.util.SideConfig;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class AdvanceSideMsg {

    private BlockPos pos;
    private boolean forward;
    private Face face;

    public AdvanceSideMsg() {
    }

    public AdvanceSideMsg(BlockPos pos, boolean forward, Face face) {
        this.pos = pos;
        this.forward = forward;
        this.face = face;
    }

    public static void serialize(AdvanceSideMsg msg, PacketBuffer packetBuffer) {
        packetBuffer.writeBlockPos(msg.pos);
        packetBuffer.writeBoolean(msg.forward);
        packetBuffer.writeEnumValue(msg.face);
    }

    public static AdvanceSideMsg deserialize(PacketBuffer packetBuffer) {
        return new AdvanceSideMsg(packetBuffer.readBlockPos(), packetBuffer.readBoolean(),
                packetBuffer.readEnumValue(Face.class));
    }

    public static void handle(AdvanceSideMsg msg, Supplier<NetworkEvent.Context> contextSupplier) {
        contextSupplier.get().enqueueWork(() -> {
            SideConfig sides = ((MachineTileEntity) contextSupplier.get().getSender().getServerWorld()
                    .getTileEntity(msg.pos)).getMachine().getSideConfig();
            switch (msg.face) {
                case TOP:
                    sides.setTop(sides.getNext(sides.getTop()));
                    break;
                case BOTTOM:
                    sides.setBottom(sides.getNext(sides.getBottom()));
                    break;
                case FRONT:
                    sides.setFront(sides.getNext(sides.getFront()));
                    break;
                case BACK:
                    sides.setBack(sides.getNext(sides.getBack()));
                    break;
                case LEFT:
                    sides.setLeft(sides.getNext(sides.getLeft()));
                    break;
                case RIGHT:
                    sides.setRight(sides.getNext(sides.getRight()));
                    break;
            }
        });
        contextSupplier.get().setPacketHandled(true);
    }

    public enum Face {
        FRONT, BACK, LEFT, RIGHT, TOP, BOTTOM;
    }

}

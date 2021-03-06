package deerangle.space.network;

import deerangle.space.block.entity.MachineTileEntity;
import deerangle.space.machine.util.SideConfig;
import net.minecraft.block.Block;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
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
        return new AdvanceSideMsg(packetBuffer.readBlockPos(), packetBuffer.readBoolean(), packetBuffer.readEnumValue(Face.class));
    }

    public static void handle(AdvanceSideMsg msg, Supplier<NetworkEvent.Context> contextSupplier) {
        contextSupplier.get().enqueueWork(() -> {
            World world = contextSupplier.get().getSender().getServerWorld();
            SideConfig sides = ((MachineTileEntity) world.getTileEntity(msg.pos)).getMachine().getSideConfig();
            switch (msg.face) {
                case TOP:
                    sides.setTop(sides.getNext(sides.getTop(), msg.forward));
                    break;
                case BOTTOM:
                    sides.setBottom(sides.getNext(sides.getBottom(), msg.forward));
                    break;
                case FRONT:
                    sides.setFront(sides.getNext(sides.getFront(), msg.forward));
                    break;
                case BACK:
                    sides.setBack(sides.getNext(sides.getBack(), msg.forward));
                    break;
                case LEFT:
                    sides.setLeft(sides.getNext(sides.getLeft(), msg.forward));
                    break;
                case RIGHT:
                    sides.setRight(sides.getNext(sides.getRight(), msg.forward));
                    break;
            }
            world.setBlockState(msg.pos, Block.getValidBlockForPosition(world.getBlockState(msg.pos), world, msg.pos)); // updates all sides for connections
        });
        contextSupplier.get().setPacketHandled(true);
    }

    public enum Face {
        FRONT, BACK, LEFT, RIGHT, TOP, BOTTOM
    }

}

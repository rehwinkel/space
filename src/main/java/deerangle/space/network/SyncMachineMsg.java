package deerangle.space.network;

import deerangle.space.block.entity.MachineTileEntity;
import deerangle.space.machine.Machine;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class SyncMachineMsg {

    private BlockPos pos;
    private ByteBuf data;

    public SyncMachineMsg() {

    }

    public SyncMachineMsg(BlockPos pos, ByteBuf data) {
        this.data = data;
        this.pos = pos;
    }

    public SyncMachineMsg(BlockPos pos, Machine machine) {
        PacketBuffer data = new PacketBuffer(Unpooled.buffer());
        machine.writePacket(data);
        this.data = data;
        this.pos = pos;
    }

    public static void serialize(SyncMachineMsg msg, PacketBuffer packetBuffer) {
        packetBuffer.writeBlockPos(msg.pos);
        packetBuffer.writeBytes(msg.data);
    }

    public static SyncMachineMsg deserialize(PacketBuffer packetBuffer) {
        BlockPos pos = packetBuffer.readBlockPos();
        return new SyncMachineMsg(pos, packetBuffer.copy(9, packetBuffer.array().length - 9));
    }

    public static void handle(SyncMachineMsg msg, Supplier<NetworkEvent.Context> contextSupplier) {
        contextSupplier.get().enqueueWork(() -> {
            MachineTileEntity tileEntity = (MachineTileEntity) Minecraft.getInstance().world.getTileEntity(msg.pos);
            tileEntity.getMachine().readPacket(new PacketBuffer(msg.data));
        });
        contextSupplier.get().setPacketHandled(true);
    }

}

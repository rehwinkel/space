package deerangle.space.machine.data;

import deerangle.space.machine.util.Accessor;
import deerangle.space.machine.util.FlowType;
import deerangle.space.machine.util.IColorGetter;
import deerangle.space.machine.util.Restriction;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;

public class EnergyMachineData extends BaseMachineData {

    private final EnergyMachineGate storage;

    public EnergyMachineData(String name, int capacity, int transfer, FlowType flowType, IColorGetter colorGetter, ITextComponent dataName) {
        super(name, flowType, colorGetter, dataName);
        this.storage = new EnergyMachineGate(capacity, transfer, transfer, 0);
    }

    @Override
    public Accessor getInputAccessor() {
        return this.inputAccessor;
    }

    @Override
    public Accessor getOutputAccessor() {
        return this.outputAccessor;
    }

    public LazyOptional<IEnergyStorage> getEnergyStorageOpt(Restriction restriction) {
        return this.storage.getEnergyStorage(restriction);
    }

    public IEnergyStorage getEnergyStorage(Restriction restriction) {
        return this.getEnergyStorageOpt(restriction).orElseThrow(() -> new RuntimeException("Optional wasn't present"));
    }

    public IEnergyStorage getEnergyStorage() {
        return this.getEnergyStorage(Restriction.UNRESTRICTED);
    }

    @Override
    public INBT write() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putInt("Cap", this.storage.getCapacity());
        nbt.putInt("Rec", this.storage.getMaxReceive());
        nbt.putInt("Ext", this.storage.getMaxExtract());
        nbt.putInt("Eng", this.storage.getEnergy());
        return nbt;
    }

    @Override
    public void read(INBT nbt) {
        CompoundNBT compound = (CompoundNBT) nbt;
        this.storage.setCapacity(compound.getInt("Cap"));
        this.storage.setMaxReceive(compound.getInt("Rec"));
        this.storage.setMaxExtract(compound.getInt("Ext"));
        this.storage.setEnergy(compound.getInt("Eng"));
    }

    @Override
    public void writePacket(PacketBuffer buf) {
        buf.writeInt(this.storage.getEnergy());
        buf.writeInt(this.storage.getCapacity());
    }

    @Override
    public void readPacket(PacketBuffer buf) {
        this.storage.setEnergy(buf.readInt());
        this.storage.setCapacity(buf.readInt());
    }

    public int getCapacity() {
        return this.storage.getCapacity();
    }

    public int getEnergy() {
        return this.storage.getEnergy();
    }

}

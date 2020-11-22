package deerangle.space.screen;

import com.mojang.datafixers.util.Pair;
import deerangle.space.machine.Machine;
import deerangle.space.machine.data.*;
import deerangle.space.machine.util.Accessor;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fluids.FluidStack;

public class DisplayValueReader {

    private static final int DEFAULT_EMPTY_COLOR = 0x888888;
    private static final int BLOCKED_COLOR = 0x444444;
    private final Machine machine;

    public DisplayValueReader(Machine machine) {
        this.machine = machine;
    }

    public Pair<Integer, Integer> getEnergyData(int index) {
        IMachineData data = machine.getMachineData(index);
        assert data instanceof EnergyMachineData;
        int cap = ((EnergyMachineData) data).getCapacity();
        int eng = ((EnergyMachineData) data).getEnergy();
        return new Pair<>(eng, cap);
    }

    public Pair<FluidStack, Integer> getFluidData(int index) {
        IMachineData data = machine.getMachineData(index);
        assert data instanceof FluidMachineData;
        int cap = ((FluidMachineData) data).getCapacity();
        FluidStack fluid = ((FluidMachineData) data).getFluidStack();
        return new Pair<>(fluid, cap);
    }

    public float getBurnData(int index) {
        IMachineData data = machine.getMachineData(index);
        assert data instanceof BurnMachineData;
        return ((BurnMachineData) data).getProgress();
    }

    public float getProgressData(int index) {
        IMachineData data = machine.getMachineData(index);
        assert data instanceof ProgressMachineData;
        return ((ProgressMachineData) data).getProgress();
    }

    public int getAccessorColor(Accessor accessor, boolean blocked) {
        if (blocked) {
            return BLOCKED_COLOR;
        }
        if (accessor == null) {
            return DEFAULT_EMPTY_COLOR;
        }
        return accessor.getColor();
    }

    private ITextComponent getAccessorName(Accessor accessor, boolean blocked) {
        if (blocked) {
            return new TranslationTextComponent("info.space.blocked");
        }
        if (accessor == null) {
            return new TranslationTextComponent("info.space.none");
        }
        return accessor.getName();
    }

    public int getFrontColor() {
        return this.getAccessorColor(machine.getSideConfig().getFront(), machine.getSideConfig().isFrontBlocked());
    }

    public int getBackColor() {
        return this.getAccessorColor(machine.getSideConfig().getBack(), machine.getSideConfig().isBackBlocked());
    }

    public int getLeftColor() {
        return this.getAccessorColor(machine.getSideConfig().getLeft(), machine.getSideConfig().isLeftBlocked());
    }

    public int getRightColor() {
        return this.getAccessorColor(machine.getSideConfig().getRight(), machine.getSideConfig().isRightBlocked());
    }

    public int getTopColor() {
        return this.getAccessorColor(machine.getSideConfig().getTop(), machine.getSideConfig().isTopBlocked());
    }

    public int getBottomColor() {
        return this.getAccessorColor(machine.getSideConfig().getBottom(), machine.getSideConfig().isBottomBlocked());
    }

    public ITextComponent getBackName() {
        return this.getAccessorName(machine.getSideConfig().getBack(), machine.getSideConfig().isBackBlocked());
    }

    public ITextComponent getFrontName() {
        return this.getAccessorName(machine.getSideConfig().getFront(), machine.getSideConfig().isFrontBlocked());
    }

    public ITextComponent getBottomName() {
        return this.getAccessorName(machine.getSideConfig().getBottom(), machine.getSideConfig().isBottomBlocked());
    }

    public ITextComponent getTopName() {
        return this.getAccessorName(machine.getSideConfig().getTop(), machine.getSideConfig().isTopBlocked());
    }

    public ITextComponent getRightName() {
        return this.getAccessorName(machine.getSideConfig().getRight(), machine.getSideConfig().isRightBlocked());
    }

    public ITextComponent getLeftName() {
        return this.getAccessorName(machine.getSideConfig().getLeft(), machine.getSideConfig().isLeftBlocked());
    }

    public int getOverlayColor(int index) {
        return machine.getMachineData(index).getColor();
    }
}

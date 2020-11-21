package deerangle.space.screen;

import com.mojang.datafixers.util.Pair;
import deerangle.space.machine.Machine;
import deerangle.space.machine.data.*;
import deerangle.space.machine.element.DataElement;
import deerangle.space.machine.element.Element;
import deerangle.space.machine.element.OverlayedElement;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fluids.FluidStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DisplayValueReader {

    private final Machine machine;
    private final Map<Integer, DataElement> dataElementMap;
    private static final int DEFAULT_EMPTY_COLOR = 0x888888;
    private static final int BLOCKED_COLOR = 0x444444;

    public DisplayValueReader(Machine machine, List<Element> elementList) {
        this.machine = machine;
        dataElementMap = new HashMap<>();
        for (Element el : elementList) {
            if (el instanceof DataElement) {
                dataElementMap.put(((DataElement) el).getIndex(), (DataElement) el);
            }
        }
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

    public int getIndexColor(int index, boolean blocked) {
        if (blocked) {
            return BLOCKED_COLOR;
        }
        if (index == -1) {
            return DEFAULT_EMPTY_COLOR;
        }
        return ((OverlayedElement) this.dataElementMap.get(index)).getOverlayColor();
    }

    private ITextComponent getIndexName(int index, boolean blocked) {
        if (blocked) {
            return new TranslationTextComponent("info.space.blocked");
        }
        if (index == -1) {
            return new TranslationTextComponent("info.space.none");
        }
        return ((OverlayedElement) this.dataElementMap.get(index)).getName();
    }

    public int getFrontColor() {
        return this.getIndexColor(machine.getSideConfig().getFront(), machine.getSideConfig().isFrontBlocked());
    }

    public int getBackColor() {
        return this.getIndexColor(machine.getSideConfig().getBack(), machine.getSideConfig().isBackBlocked());
    }

    public int getLeftColor() {
        return this.getIndexColor(machine.getSideConfig().getLeft(), machine.getSideConfig().isLeftBlocked());
    }

    public int getRightColor() {
        return this.getIndexColor(machine.getSideConfig().getRight(), machine.getSideConfig().isRightBlocked());
    }

    public int getTopColor() {
        return this.getIndexColor(machine.getSideConfig().getTop(), machine.getSideConfig().isTopBlocked());
    }

    public int getBottomColor() {
        return this.getIndexColor(machine.getSideConfig().getBottom(), machine.getSideConfig().isBottomBlocked());
    }

    public ITextComponent getBackName() {
        return this.getIndexName(machine.getSideConfig().getBack(), machine.getSideConfig().isBackBlocked());
    }

    public ITextComponent getFrontName() {
        return this.getIndexName(machine.getSideConfig().getFront(), machine.getSideConfig().isFrontBlocked());
    }

    public ITextComponent getBottomName() {
        return this.getIndexName(machine.getSideConfig().getBottom(), machine.getSideConfig().isBottomBlocked());
    }

    public ITextComponent getTopName() {
        return this.getIndexName(machine.getSideConfig().getTop(), machine.getSideConfig().isTopBlocked());
    }

    public ITextComponent getRightName() {
        return this.getIndexName(machine.getSideConfig().getRight(), machine.getSideConfig().isRightBlocked());
    }

    public ITextComponent getLeftName() {
        return this.getIndexName(machine.getSideConfig().getLeft(), machine.getSideConfig().isLeftBlocked());
    }

}

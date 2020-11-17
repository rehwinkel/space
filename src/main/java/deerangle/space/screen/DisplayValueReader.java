package deerangle.space.screen;

import com.mojang.datafixers.util.Pair;
import deerangle.space.machine.EnergyMachineData;
import deerangle.space.machine.IMachineData;
import deerangle.space.machine.Machine;

public class DisplayValueReader {

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

    public float getBurnData(int index) {
        return 0.5f;
    }
}

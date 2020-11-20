package deerangle.space.machine.element;

import com.google.common.collect.ImmutableList;
import deerangle.space.machine.Machine;
import deerangle.space.machine.util.FlowType;
import net.minecraft.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class MachineType<M extends Machine> extends ForgeRegistryEntry<MachineType<?>> {

    private final ImmutableList<Element> elements;
    private final Supplier<M> constructor;

    private MachineType(List<Element> elements, Supplier<M> constructor) {
        this.elements = ImmutableList.copyOf(elements);
        this.constructor = constructor;
    }

    public static <M extends Machine> MachineType.Builder<M> builder(Supplier<M> constructor) {
        return new Builder<>(constructor);
    }

    public List<Element> getElements() {
        return this.elements;
    }

    public M getNewInstance() {
        return constructor.get();
    }

    public static class Builder<M extends Machine> {
        private final ArrayList<Element> elements;
        private final Supplier<M> constructor;
        private int inputIndex;
        private int outputIndex;
        private int inOutIndex;

        public Builder(Supplier<M> constructor) {
            this.elements = new ArrayList<>();
            this.inputIndex = 0;
            this.outputIndex = 0;
            this.inOutIndex = 0;
            this.constructor = constructor;
        }

        public Builder<M> addItemElement(int x, int y, int index, FlowType flowType, Predicate<ItemStack> validPredicate) {
            int color = this.getColor(flowType);
            this.elements.add(new ItemElement(x, y, index, flowType, color, validPredicate));
            this.incrementColorCounter(flowType);
            return this;
        }

        public Builder<M> addEnergyElement(int x, int y, int index, FlowType flowType) {
            int color = this.getColor(flowType);
            this.elements.add(new EnergyElement(x, y, index, flowType, color));
            this.incrementColorCounter(flowType);
            return this;
        }

        private void incrementColorCounter(FlowType flowType) {
            switch (flowType) {
                case INOUT:
                    this.inOutIndex++;
                    break;
                case INPUT:
                    this.inputIndex++;
                    break;
                case OUTPUT:
                    this.outputIndex++;
                    break;
            }
        }

        private int getColor(FlowType flowType) {
            int colorIndex = -1;
            switch (flowType) {
                case INOUT:
                    colorIndex = this.inOutIndex;
                    break;
                case INPUT:
                    colorIndex = this.inputIndex;
                    break;
                case OUTPUT:
                    colorIndex = this.outputIndex;
                    break;
            }
            return flowType.getColor(colorIndex);
        }

        public Builder<M> addFluidElement(int x, int y, int index, FlowType flowType) {
            int color = this.getColor(flowType);
            this.elements.add(new FluidElement(x, y, index, flowType, color));
            this.incrementColorCounter(flowType);
            return this;
        }

        public Builder<M> add(Element el) {
            this.elements.add(el);
            return this;
        }

        public MachineType<M> build() {
            return new MachineType<>(this.elements, this.constructor);
        }
    }

}

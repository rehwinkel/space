package deerangle.space.machine.element;

import com.google.common.collect.ImmutableList;
import deerangle.space.machine.Machine;
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
        private static final int[] inputColors = new int[]{0x2056d4, 0x682edb, 0x2edb7c};
        private static final int[] outputColors = new int[]{0xd18232, 0xd14f32, 0xe6df27};

        private final ArrayList<Element> elements;
        private final Supplier<M> constructor;
        private int inputIndex;
        private int outputIndex;

        public Builder(Supplier<M> constructor) {
            this.elements = new ArrayList<>();
            this.inputIndex = 0;
            this.outputIndex = 0;
            this.constructor = constructor;
        }

        public Builder<M> addItemSlot(int x, int y, int index, boolean input, Predicate<ItemStack> validPredicate) {
            int color = input ? inputColors[this.inputIndex] : outputColors[this.outputIndex];
            this.elements.add(new ItemElement(x, y, index, input, color, validPredicate));
            if (input) {
                this.inputIndex++;
            } else {
                this.outputIndex++;
            }
            return this;
        }

        public Builder<M> addEnergySlot(int x, int y, int index, boolean input) {
            int color = input ? inputColors[this.inputIndex] : outputColors[this.outputIndex];
            this.elements.add(new EnergyElement(x, y, index, input, color));
            if (input) {
                this.inputIndex++;
            } else {
                this.outputIndex++;
            }
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

package deerangle.space.machine.element;

import com.google.common.collect.ImmutableList;
import deerangle.space.machine.Machine;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
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

        public Builder(Supplier<M> constructor) {
            this.elements = new ArrayList<>();
            this.constructor = constructor;
        }

        public Builder<M> addItemElement(int x, int y, int index, ITextComponent name, Predicate<ItemStack> validPredicate) {
            this.elements.add(new ItemElement(x, y, index, name, validPredicate));
            return this;
        }

        public Builder<M> addEnergyElement(int x, int y, int index, ITextComponent name) {
            this.elements.add(new EnergyElement(x, y, index, name));
            return this;
        }

        public Builder<M> addFluidElement(int x, int y, int index, ITextComponent name) {
            this.elements.add(new FluidElement(x, y, index, name));
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

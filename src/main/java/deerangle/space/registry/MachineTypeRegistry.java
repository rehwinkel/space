package deerangle.space.registry;

import deerangle.space.machine.BlastFurnaceMachine;
import deerangle.space.machine.CoalGeneratorMachine;
import deerangle.space.machine.CombustionGeneratorMachine;
import deerangle.space.machine.Machine;
import deerangle.space.machine.element.BurnElement;
import deerangle.space.machine.element.MachineType;
import deerangle.space.machine.element.ProgressElement;
import net.minecraft.item.Items;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class MachineTypeRegistry {

    public static MachineType<CombustionGeneratorMachine> COMBUSTION_GENERATOR;
    public static MachineType<BlastFurnaceMachine> BLAST_FURNACE;
    public static MachineType<CoalGeneratorMachine> COAL_GENERATOR;

    @SubscribeEvent
    public static void registerMachineTypes(RegistryEvent.Register<MachineType<?>> event) {
        COAL_GENERATOR = register("coal_generator", MachineType.builder(CoalGeneratorMachine::new)
                .addItemElement(79, 47, 0, true, stack -> ForgeHooks.getBurnTime(stack) > 0)
                .addEnergyElement(17, 17, 1, false).add(new BurnElement(79, 29, 2)));
        BLAST_FURNACE = register("blast_furnace", MachineType.builder(BlastFurnaceMachine::new)
                .addItemElement(55, 52, 0, true, stack -> stack.getItem().equals(Items.COAL))
                .addItemElement(55, 16, 1, true, stack -> true).addItemElement(110, 34, 2, false, stack -> false)
                .add(new BurnElement(55, 34, 3)).add(new ProgressElement(79, 33, 4)));
        COMBUSTION_GENERATOR = register("combustion_generator",
                MachineType.builder(CombustionGeneratorMachine::new).addFluidElement(79, 17, 0, true)
                        .addEnergyElement(17, 17, 1, false).addItemElement(79 + 20, 29 + 20, 2, true,
                        stack -> stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).isPresent())
                        .add(new BurnElement(79 + 20, 29, 3)));
        event.getRegistry().registerAll(COAL_GENERATOR, BLAST_FURNACE, COMBUSTION_GENERATOR);
    }

    private static <M extends Machine> MachineType<M> register(String name, MachineType.Builder<M> builder) {
        MachineType<M> machineType = builder.build();
        machineType.setRegistryName(name);
        return machineType;
    }

}

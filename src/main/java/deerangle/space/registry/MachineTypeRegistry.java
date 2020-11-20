package deerangle.space.registry;

import deerangle.space.machine.*;
import deerangle.space.machine.element.BurnElement;
import deerangle.space.machine.element.MachineType;
import deerangle.space.machine.element.ProgressElement;
import deerangle.space.machine.util.FlowType;
import net.minecraft.item.Items;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.common.Mod;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class MachineTypeRegistry {

    public static MachineType<CoalGeneratorMachine> COAL_GENERATOR;
    public static MachineType<BlastFurnaceMachine> BLAST_FURNACE;
    public static MachineType<CombustionGeneratorMachine> COMBUSTION_GENERATOR;
    public static MachineType<GasTankMachine> GAS_TANK;
    public static MachineType<DrumMachine> DRUM;

    @SubscribeEvent
    public static void registerMachineTypes(RegistryEvent.Register<MachineType<?>> event) {
        COAL_GENERATOR = register("coal_generator", MachineType.builder(CoalGeneratorMachine::new)
                .addItemElement(79, 47, 0, FlowType.INPUT, stack -> ForgeHooks.getBurnTime(stack) > 0)
                .addEnergyElement(17, 17, 1, FlowType.OUTPUT).add(new BurnElement(79, 29, 2)));
        BLAST_FURNACE = register("blast_furnace", MachineType.builder(BlastFurnaceMachine::new)
                .addItemElement(55, 52, 0, FlowType.INPUT, stack -> stack.getItem().equals(Items.COAL))
                .addItemElement(55, 16, 1, FlowType.INPUT, stack -> true)
                .addItemElement(110, 34, 2, FlowType.OUTPUT, stack -> false).add(new BurnElement(55, 34, 3))
                .add(new ProgressElement(79, 33, 4)));
        COMBUSTION_GENERATOR = register("combustion_generator",
                MachineType.builder(CombustionGeneratorMachine::new).addFluidElement(89, 17, 0, FlowType.INPUT)
                        .addEnergyElement(17, 17, 1, FlowType.OUTPUT).addItemElement(69, 48, 2, FlowType.INPUT,
                        stack -> stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).isPresent())
                        .add(new BurnElement(69, 29, 3)));
        GAS_TANK = register("gas_tank",
                MachineType.builder(GasTankMachine::new).addFluidElement(79, 17, 0, FlowType.INOUT)
                        .addItemElement(79 - 20, 17, 1, FlowType.INPUT,
                                stack -> stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY)
                                        .isPresent()).addItemElement(79 + 20, 17, 2, FlowType.OUTPUT,
                        stack -> stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY)
                                .isPresent()));
        DRUM = register("drum",
                MachineType.builder(DrumMachine::new).addFluidElement(79, 17, 0, FlowType.INOUT)
                        .addItemElement(79 - 20, 17, 1, FlowType.INPUT,
                                stack -> stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY)
                                        .isPresent()).addItemElement(79 + 20, 17, 2, FlowType.OUTPUT,
                        stack -> stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY)
                                .isPresent()));
        event.getRegistry().registerAll(COAL_GENERATOR, BLAST_FURNACE, COMBUSTION_GENERATOR, GAS_TANK, DRUM);
    }

    private static <M extends Machine> MachineType<M> register(String name, MachineType.Builder<M> builder) {
        MachineType<M> machineType = builder.build();
        machineType.setRegistryName(name);
        return machineType;
    }

}

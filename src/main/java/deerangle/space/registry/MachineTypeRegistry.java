package deerangle.space.registry;

import deerangle.space.machine.*;
import deerangle.space.machine.element.BurnElement;
import deerangle.space.machine.element.MachineType;
import deerangle.space.machine.element.ProgressElement;
import deerangle.space.machine.util.FlowType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class MachineTypeRegistry {

    public static MachineType<CoalGeneratorMachine> COAL_GENERATOR;
    public static MachineType<BlastFurnaceMachine> BLAST_FURNACE;
    public static MachineType<CombustionGeneratorMachine> COMBUSTION_GENERATOR;
    public static MachineType<GasTankMachine> GAS_TANK;
    public static MachineType<DrumMachine> DRUM;
    public static MachineType<BatteryPackMachine> BATTERY_PACK;
    public static MachineType<RefineryMachine> REFINERY;

    public static boolean holdsFluid(ItemStack stack) {
        return stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).isPresent();
    }

    public static boolean holdsEnergy(ItemStack stack) {
        //TODO
        return stack.getCapability(CapabilityEnergy.ENERGY).isPresent();
    }

    public static boolean isFuel(ItemStack stack) {
        return ForgeHooks.getBurnTime(stack) > 0;
    }

    private static final ITextComponent BUCKET_TEXT = new TranslationTextComponent("info.space.element.bucket");
    private static final ITextComponent BATTERY_TEXT = new TranslationTextComponent("info.space.element.battery");
    private static final ITextComponent TANK_TEXT = new TranslationTextComponent("info.space.element.tank");
    private static final ITextComponent FUEL_TEXT = new TranslationTextComponent("info.space.element.fuel");
    private static final ITextComponent ENERGY_TEXT = new TranslationTextComponent("info.space.element.energy");
    private static final ITextComponent INPUT_TEXT = new TranslationTextComponent("info.space.element.input");
    private static final ITextComponent OUTPUT_TEXT = new TranslationTextComponent("info.space.element.output");

    @SubscribeEvent
    public static void registerMachineTypes(RegistryEvent.Register<MachineType<?>> event) {
        COAL_GENERATOR = register("coal_generator", MachineType.builder(CoalGeneratorMachine::new)
                .addItemElement(79, 47, 0, FlowType.INPUT, MachineTypeRegistry::isFuel, FUEL_TEXT)
                .addEnergyElement(17, 17, 1, FlowType.OUTPUT, ENERGY_TEXT).add(new BurnElement(79, 29, 2)));
        BLAST_FURNACE = register("blast_furnace", MachineType.builder(BlastFurnaceMachine::new)
                .addItemElement(55, 52, 0, FlowType.INPUT, stack -> stack.getItem().equals(Items.COAL), FUEL_TEXT)
                .addItemElement(55, 16, 1, FlowType.INPUT, stack -> true, INPUT_TEXT)
                .addItemElement(110, 34, 2, FlowType.OUTPUT, stack -> false, OUTPUT_TEXT)
                .add(new BurnElement(55, 34, 3)).add(new ProgressElement(79, 33, 4)));
        COMBUSTION_GENERATOR = register("combustion_generator", MachineType.builder(CombustionGeneratorMachine::new)
                .addFluidElement(89, 17, 0, FlowType.INPUT, FUEL_TEXT)
                .addEnergyElement(17, 17, 1, FlowType.OUTPUT, ENERGY_TEXT)
                .addItemElement(69, 48, 2, FlowType.INPUT, MachineTypeRegistry::holdsFluid, BUCKET_TEXT)
                .add(new BurnElement(69, 29, 3)));
        GAS_TANK = register("gas_tank",
                MachineType.builder(GasTankMachine::new).addFluidElement(79, 17, 0, FlowType.INOUT, TANK_TEXT)
                        .addItemElement(79 - 20, 17, 1, FlowType.INPUT, MachineTypeRegistry::holdsFluid, BUCKET_TEXT)
                        .addItemElement(79 + 20, 17, 2, FlowType.OUTPUT, MachineTypeRegistry::holdsFluid, BUCKET_TEXT));
        DRUM = register("drum",
                MachineType.builder(DrumMachine::new).addFluidElement(79, 17, 0, FlowType.INOUT, TANK_TEXT)
                        .addItemElement(79 - 20, 17, 1, FlowType.INPUT, MachineTypeRegistry::holdsFluid, BUCKET_TEXT)
                        .addItemElement(79 + 20, 17, 2, FlowType.OUTPUT, MachineTypeRegistry::holdsFluid, BUCKET_TEXT));
        BATTERY_PACK = register("battery_pack", MachineType.builder(BatteryPackMachine::new)
                .addEnergyElement(79 + 4, 17, 0, FlowType.INOUT, ENERGY_TEXT)
                .addItemElement(79 - 20, 17, 1, FlowType.INPUT, MachineTypeRegistry::holdsEnergy, BATTERY_TEXT)
                .addItemElement(79 + 20, 17, 2, FlowType.OUTPUT, MachineTypeRegistry::holdsEnergy, BATTERY_TEXT));
        REFINERY = register("refinery",
                MachineType.builder(RefineryMachine::new).addEnergyElement(17, 17, 0, FlowType.INPUT, ENERGY_TEXT)
                        .addFluidElement(17 + 12, 17, 1, FlowType.INPUT, INPUT_TEXT)
                        .addFluidElement(176 - 17 - 18 - 12, 17, 2, FlowType.OUTPUT, OUTPUT_TEXT)
                        .addItemElement(17 + 12+20, 48, 3, FlowType.INPUT, MachineTypeRegistry::holdsFluid, BUCKET_TEXT)
                        .addItemElement(176 - 17 - 18 - 20 - 12, 48, 4, FlowType.OUTPUT, MachineTypeRegistry::holdsFluid,
                                BUCKET_TEXT).add(new ProgressElement(176/2-12, 30, 5)));
        event.getRegistry()
                .registerAll(COAL_GENERATOR, BLAST_FURNACE, COMBUSTION_GENERATOR, GAS_TANK, DRUM, BATTERY_PACK,
                        REFINERY);
    }

    private static <M extends Machine> MachineType<M> register(String name, MachineType.Builder<M> builder) {
        MachineType<M> machineType = builder.build();
        machineType.setRegistryName(name);
        return machineType;
    }

}

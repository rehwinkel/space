package deerangle.space.registry;

import deerangle.space.machine.*;
import deerangle.space.machine.element.*;
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

    private static final ITextComponent BUCKET_TEXT = new TranslationTextComponent("info.space.element.bucket");
    private static final ITextComponent BATTERY_TEXT = new TranslationTextComponent("info.space.element.battery");
    private static final ITextComponent TANK_TEXT = new TranslationTextComponent("info.space.element.tank");
    private static final ITextComponent FUEL_TEXT = new TranslationTextComponent("info.space.element.fuel");
    private static final ITextComponent ENERGY_TEXT = new TranslationTextComponent("info.space.element.energy");
    private static final ITextComponent INPUT_TEXT = new TranslationTextComponent("info.space.element.input");
    private static final ITextComponent OUTPUT_TEXT = new TranslationTextComponent("info.space.element.output");
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

    public static boolean isCoal(ItemStack stack) {
        return stack.getItem().equals(Items.COAL);
    }

    public static boolean accept(ItemStack stack) {
        return true;
    }

    public static boolean reject(ItemStack stack) {
        return false;
    }

    @SubscribeEvent
    public static void registerMachineTypes(RegistryEvent.Register<MachineType<?>> event) {
        COAL_GENERATOR = register("coal_generator", MachineType.builder(CoalGeneratorMachine::new).add(new ItemElement(79, 47, 0, FUEL_TEXT, MachineTypeRegistry::isFuel)).add(new EnergyElement(17, 17, 1, ENERGY_TEXT)).add(new BurnElement(79, 29, 2)));
        BLAST_FURNACE = register("blast_furnace", MachineType.builder(BlastFurnaceMachine::new).add(new ItemElement(55, 52, 0, FUEL_TEXT, MachineTypeRegistry::isCoal)).add(new ItemElement(55, 16, 1, INPUT_TEXT, MachineTypeRegistry::accept)).add(new ItemElement(110, 34, 2, OUTPUT_TEXT, MachineTypeRegistry::reject)).add(new BurnElement(55, 34, 3)).add(new ProgressElement(79, 33, 4)));
        COMBUSTION_GENERATOR = register("combustion_generator", MachineType.builder(CombustionGeneratorMachine::new).add(new FluidElement(89, 17, 0, FUEL_TEXT)).add(new EnergyElement(17, 17, 1, ENERGY_TEXT)).add(new ItemElement(69, 48, 2, BUCKET_TEXT, MachineTypeRegistry::holdsFluid)).add(new BurnElement(69, 29, 3)));
        GAS_TANK = register("gas_tank", MachineType.builder(GasTankMachine::new).add(new FluidElement(79, 17, 0, TANK_TEXT)).add(new ItemElement(79 - 20, 17, 1, BUCKET_TEXT, MachineTypeRegistry::holdsFluid)).add(new ItemElement(79 + 20, 17, 2, BUCKET_TEXT, MachineTypeRegistry::holdsFluid)));
        DRUM = register("drum", MachineType.builder(DrumMachine::new).add(new FluidElement(79, 17, 0, TANK_TEXT)).add(new ItemElement(79 - 20, 17, 1, BUCKET_TEXT, MachineTypeRegistry::holdsFluid)).add(new ItemElement(79 + 20, 17, 2, BUCKET_TEXT, MachineTypeRegistry::holdsFluid)));
        BATTERY_PACK = register("battery_pack", MachineType.builder(BatteryPackMachine::new).add(new EnergyElement(79 + 4, 17, 0, ENERGY_TEXT)).add(new ItemElement(79 - 20, 17, 1, BATTERY_TEXT, MachineTypeRegistry::holdsEnergy)).add(new ItemElement(79 + 20, 17, 2, BATTERY_TEXT, MachineTypeRegistry::holdsEnergy)));
        REFINERY = register("refinery", MachineType.builder(RefineryMachine::new).add(new EnergyElement(17, 17, 0, ENERGY_TEXT)).add(new FluidElement(29, 17, 1, INPUT_TEXT)).add(new FluidElement(129, 17, 2, OUTPUT_TEXT)).add(new ItemElement(49, 48, 3, BUCKET_TEXT, MachineTypeRegistry::holdsFluid)).add(new ItemElement(109, 48, 4, BUCKET_TEXT, MachineTypeRegistry::holdsFluid)).add(new ProgressElement(76, 30, 5)));
        event.getRegistry().registerAll(COAL_GENERATOR, BLAST_FURNACE, COMBUSTION_GENERATOR, GAS_TANK, DRUM, BATTERY_PACK, REFINERY);
    }

    private static <M extends Machine> MachineType<M> register(String name, MachineType.Builder<M> builder) {
        MachineType<M> machineType = builder.build();
        machineType.setRegistryName(name);
        return machineType;
    }

}

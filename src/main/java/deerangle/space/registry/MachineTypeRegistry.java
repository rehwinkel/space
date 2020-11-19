package deerangle.space.registry;

import deerangle.space.machine.CoalGeneratorMachine;
import deerangle.space.machine.element.BurnElement;
import deerangle.space.machine.element.MachineType;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class MachineTypeRegistry {

    public static MachineType<CoalGeneratorMachine> COAL_GENERATOR;

    @SubscribeEvent
    public static void registerMachineTypes(RegistryEvent.Register<MachineType<?>> event) {
        COAL_GENERATOR = MachineType.builder(CoalGeneratorMachine::new)
                .addItemSlot(79, 47, 0, true, stack -> ForgeHooks.getBurnTime(stack) > 0)
                .addEnergySlot(17, 17, 1, false).add(new BurnElement(79, 29, 2)).build();
        COAL_GENERATOR.setRegistryName("coal_generator");
        event.getRegistry().registerAll(COAL_GENERATOR);
    }

}

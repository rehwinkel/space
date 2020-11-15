package deerangle.space.main;

import deerangle.space.registry.MachineRegistry;
import deerangle.space.screen.CoalGeneratorScreen;
import net.minecraft.client.gui.ScreenManager;

public class ClientProxy implements IProxy {

    @Override
    public void clientSetup() {
        ScreenManager.registerFactory(MachineRegistry.COAL_GENERATOR_CONTAINER.get(), CoalGeneratorScreen::new);
    }

}

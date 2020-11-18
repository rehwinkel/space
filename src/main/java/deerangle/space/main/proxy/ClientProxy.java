package deerangle.space.main.proxy;

import deerangle.space.registry.MachineRegistry;
import deerangle.space.screen.MachineScreen;
import net.minecraft.client.gui.ScreenManager;

public class ClientProxy implements IProxy {

    @Override
    public void clientSetup() {
        ScreenManager.registerFactory(MachineRegistry.MACHINE_CONTAINER.get(), MachineScreen::new);
    }

}

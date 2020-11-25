package deerangle.space.main.proxy;

import deerangle.space.planets.PlanetRegistry;
import deerangle.space.registry.FluidRegistry;
import deerangle.space.registry.MachineRegistry;
import deerangle.space.screen.MachineScreen;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;

public class ClientProxy implements IProxy {

    @Override
    public void clientSetup() {
        ScreenManager.registerFactory(MachineRegistry.MACHINE_CONTAINER.get(), MachineScreen::new);
        RenderTypeLookup.setRenderLayer(FluidRegistry.KEROSENE.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(FluidRegistry.KEROSENE_FLOWING.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(FluidRegistry.ACID.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(FluidRegistry.ACID_FLOWING.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(MachineRegistry.REFINERY.get(), RenderType.getCutoutMipped());
        PlanetRegistry.registerClient();
    }

}

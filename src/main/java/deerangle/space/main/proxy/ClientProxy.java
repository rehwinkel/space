package deerangle.space.main.proxy;

import deerangle.space.planet.PlanetManager;
import deerangle.space.registry.FluidRegistry;
import deerangle.space.registry.MachineRegistry;
import deerangle.space.screen.MachineScreen;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ClientProxy implements IProxy {

    @Override
    public void clientSetup(FMLClientSetupEvent event) {
        ScreenManager.registerFactory(MachineRegistry.MACHINE_CONTAINER.get(), MachineScreen::new);
        RenderTypeLookup.setRenderLayer(FluidRegistry.KEROSENE.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(FluidRegistry.KEROSENE_FLOWING.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(FluidRegistry.ACID.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(FluidRegistry.ACID_FLOWING.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(MachineRegistry.REFINERY.get(), RenderType.getCutoutMipped());
        PlanetManager.registerClient(event);
    }

}

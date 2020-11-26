package deerangle.space.main.proxy;

import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public interface IProxy {

    void clientSetup(FMLClientSetupEvent event);

}

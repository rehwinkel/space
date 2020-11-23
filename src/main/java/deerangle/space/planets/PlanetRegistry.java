package deerangle.space.planets;

import deerangle.space.main.SpaceMod;
import deerangle.space.planets.mars.MarsRegistry;
import deerangle.space.planets.venus.VenusRegistry;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

public class PlanetRegistry {

    public static final ItemGroup TAB = new ItemGroup(SpaceMod.MOD_ID + ".extraterrestrial") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(VenusRegistry.PULCHERITE.get());
        }
    };

    public static void register() {
        VenusRegistry.register();
        MarsRegistry.register();
    }

    public static void registerData(GatherDataEvent event) {
        VenusRegistry.registerData(event);
        MarsRegistry.registerData(event);
    }

    public static void registerClient() {
        VenusRegistry.registerClient();
        MarsRegistry.registerClient();
    }
}

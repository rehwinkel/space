package deerangle.space.stats;

import deerangle.space.main.SpaceMod;
import net.minecraft.stats.IStatFormatter;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

public class Stats {

    public static final ResourceLocation INTERACT_WITH_COAL_GENERATOR = registerCustom("interact_with_coal_generator",
            IStatFormatter.DEFAULT);
    public static final ResourceLocation INTERACT_WITH_BLAST_FURNACE = registerCustom("interact_with_blast_furnace",
            IStatFormatter.DEFAULT);
    public static final ResourceLocation INTERACT_WITH_COMBUSTION_GENERATOR = registerCustom(
            "interact_with_combustion_generator", IStatFormatter.DEFAULT);
    public static final ResourceLocation INTERACT_WITH_GAS_TANK = registerCustom("interact_with_gas_tank",
            IStatFormatter.DEFAULT);
    public static final ResourceLocation INTERACT_WITH_DRUM = registerCustom("interact_with_drum",
            IStatFormatter.DEFAULT);
    public static final ResourceLocation INTERACT_WITH_BATTERY_PACK = registerCustom("interact_with_battery_pack",
            IStatFormatter.DEFAULT);

    public static void register() {
    }

    private static ResourceLocation registerCustom(String key, IStatFormatter formatter) {
        ResourceLocation resourcelocation = new ResourceLocation(SpaceMod.MOD_ID, key);
        Registry.register(Registry.CUSTOM_STAT, SpaceMod.MOD_ID + ":" + key, resourcelocation);
        net.minecraft.stats.Stats.CUSTOM.get(resourcelocation, formatter);
        return resourcelocation;
    }

}

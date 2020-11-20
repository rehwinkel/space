package deerangle.space.data;

import deerangle.space.registry.ResourceRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

public class LanguageGenerator extends LanguageProvider {

    public LanguageGenerator(DataGenerator gen, String modid, String locale) {
        super(gen, modid, locale);
    }

    @Override
    protected void addTranslations() {
        add(ResourceRegistry.COPPER_ORE.get(), "Copper Ore");
        add(ResourceRegistry.ALUMINIUM_ORE.get(), "Aluminium Ore");
        add(ResourceRegistry.ILMENITE_ORE.get(), "Ilmenite Ore");
        add(ResourceRegistry.ALUMINIUM_BLOCK.get(), "Aluminium Block");
        add(ResourceRegistry.COPPER_BLOCK.get(), "Copper Block");
        add(ResourceRegistry.STEEL_BLOCK.get(), "Steel Block");
        add(ResourceRegistry.TITANIUM_BLOCK.get(), "Titanium Block");
        add(ResourceRegistry.QUARTZ_SAND.get(), "Quartz Sand");
        add(ResourceRegistry.RUSTY_DUST.get(), "Rusty Dust");
        add(ResourceRegistry.STEEL_INGOT.get(), "Steel Ingot");
        add(ResourceRegistry.COPPER_INGOT.get(), "Copper Ingot");
        add(ResourceRegistry.TITANIUM_INGOT.get(), "Titanium Ingot");
        add(ResourceRegistry.ALUMINIUM_INGOT.get(), "Aluminium Ingot");
        add(ResourceRegistry.STEEL_NUGGET.get(), "Steel Nugget");
        add(ResourceRegistry.COPPER_NUGGET.get(), "Copper Nugget");
        add(ResourceRegistry.TITANIUM_NUGGET.get(), "Titanium Nugget");
        add(ResourceRegistry.ALUMINIUM_NUGGET.get(), "Aluminium Nugget");
        add(ResourceRegistry.ALUMINIUM_PLATE.get(), "Aluminium Plate");
        add(ResourceRegistry.COPPER_PLATE.get(), "Copper Plate");
        add(ResourceRegistry.IRON_PLATE.get(), "Iron Plate");
        add(ResourceRegistry.STEEL_PLATE.get(), "Steel Plate");
        add(ResourceRegistry.BATTERY.get(), "Battery");
        add(ResourceRegistry.CYLINDER.get(), "Cylinder");
        add(ResourceRegistry.IGNITION_COIL.get(), "Ignition Coil");
        add(ResourceRegistry.INDUSTRIAL_PISTON.get(), "Industrial Piston");
        add(ResourceRegistry.COPPER_PIPE.get(), "Copper Pipe");
        add(ResourceRegistry.IRON_PIPE.get(), "Iron Pipe");
        add(ResourceRegistry.IRON_DUST.get(), "Iron Dust");
        add(ResourceRegistry.QUARTZ_DUST.get(), "Quartz Dust");
        add(ResourceRegistry.STEEL_ROD.get(), "Steel Rod");
        add(ResourceRegistry.HEAT_RESISTENT_GLASS.get(), "Heat-Resistent Glass");
        add(ResourceRegistry.MACHINE_BASE.get(), "Machine Base");
        add(ResourceRegistry.SILICA_TILE.get(), "Silica Tile");
        add(ResourceRegistry.PLATED_ROCKET_BODY.get(), "Plated Rocket Body");
        add(ResourceRegistry.ROCKET_CONE.get(), "Rocket Cone");
        add(ResourceRegistry.ROCKET_FIN.get(), "Rocket Fin");
        add(ResourceRegistry.ROCKET_THRUSTER.get(), "Rocket Thruster");
        add("itemGroup.space.resource", "Space Resources");
        add("itemGroup.space.machine", "Space Machines");
        add("block.space.coal_generator", "Coal Generator");
        add("block.space.combustion_generator", "Combustion Generator");
        add("block.space.blast_furnace", "Blast Furnace");
        add("stat.space.interact_with_coal_generator", "Interactions with Coal Generator");
        add("info.space.energy", "Energy Stored: %s/%s FE");
        add("info.space.fluid", "%s: %s/%s mB");
        add("info.space.missing_fluid", "None");
        add("info.space.side_config", "Side Config");
        add("info.space.top_letter", "U");
        add("info.space.bottom_letter", "D");
        add("info.space.front_letter", "F");
        add("info.space.back_letter", "B");
        add("info.space.left_letter", "L");
        add("info.space.right_letter", "R");
    }

}

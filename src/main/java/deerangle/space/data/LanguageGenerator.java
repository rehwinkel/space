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
        add("itemGroup.space.resource", "Space Resources");
        add("itemGroup.space.machine", "Space Machines");
        add("block.space.coal_generator", "Coal Generator");
        add("stat.space.interact_with_coal_generator", "Interactions with Coal Generator");
        add("info.space.energy", "Energy Stored: %s/%s FE");
        add("info.space.fluid", "%s: %s/%s mB");
        add("info.space.side_config", "Side Config");
        add("info.space.top_letter", "U");
        add("info.space.bottom_letter", "D");
        add("info.space.front_letter", "F");
        add("info.space.back_letter", "B");
        add("info.space.left_letter", "L");
        add("info.space.right_letter", "R");
    }

}

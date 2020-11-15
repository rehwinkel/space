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
    }

}

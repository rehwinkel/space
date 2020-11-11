package deerangle.space.data;

import deerangle.space.block.BlockRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

public class LanguageGenerator extends LanguageProvider {

    public LanguageGenerator(DataGenerator gen, String modid, String locale) {
        super(gen, modid, locale);
    }

    @Override
    protected void addTranslations() {
        add(BlockRegistry.COPPER_ORE.get(), "Copper Ore");
        add(BlockRegistry.ALUMINIUM_ORE.get(), "Aluminium Ore");
        add("itemGroup.space.blocks", "Space Blocks");
    }

}

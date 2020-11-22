package deerangle.space.machine.element;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.datafixers.util.Pair;
import deerangle.space.screen.DisplayValueReader;
import deerangle.space.screen.MachineScreen;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class EnergyElement extends TooltipElement {

    public EnergyElement(int x, int y, int index, ITextComponent name) {
        super(x, y, index, name, 10, 49);
    }

    @Override
    public ITextComponent getTooltipText(DisplayValueReader reader) {
        Pair<Integer, Integer> engAndCap = reader.getEnergyData(this.getIndex());
        int eng = engAndCap.getFirst();
        int cap = engAndCap.getSecond();
        return new TranslationTextComponent("info.space.energy", eng, cap);
    }

    @Override
    public void draw(MachineScreen screen, DisplayValueReader reader, MatrixStack matrixStack, int guiLeft, int guiTop) {
        int x = guiLeft + this.getX();
        int y = guiTop + this.getY();
        Pair<Integer, Integer> engAndCap = reader.getEnergyData(this.getIndex());
        int eng = engAndCap.getFirst();
        int cap = engAndCap.getSecond();
        float amount = eng / (float) cap;
        screen.resetOverlayColor();
        screen.bindMachinesTexture();
        int height = (int) (47 * (1F - amount));
        screen.blit(matrixStack, x, y, 0, 49, 10, 49);
        screen.blit(matrixStack, x + 1, y + 1 + height, 11, 50 + height, 8, 47 - height);
        screen.setOverlayColor(reader.getOverlayColor(this.getIndex()));
        screen.blit(matrixStack, x, y, 20, 49, 10, 49);
    }

}

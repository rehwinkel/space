package deerangle.space.machine.type;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.datafixers.util.Pair;
import deerangle.space.screen.MachineScreen;
import deerangle.space.screen.DisplayValueReader;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class EnergyElement extends OverlayedElement {

    EnergyElement(int x, int y, int index, boolean input, int overlayColor) {
        super(x, y, index, input, overlayColor, 10, 48);
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
        int height = (int) (46 * (1F - amount));
        screen.blit(matrixStack, x, y, 0, 48, 10, 48);
        screen.blit(matrixStack, x + 1, y + 1 + height, 11, 49 + height, 8, 46 - height);
        screen.setOverlayColor(this.getOverlayColor());
        screen.blit(matrixStack, x, y, 20, 48, 10, 48);
    }

}

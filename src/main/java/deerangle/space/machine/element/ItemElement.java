package deerangle.space.machine.element;

import com.mojang.blaze3d.matrix.MatrixStack;
import deerangle.space.screen.DisplayValueReader;
import deerangle.space.screen.MachineScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;

import java.util.function.Predicate;

public class ItemElement extends TooltipElement {

    private final Predicate<ItemStack> validPredicate;

    public ItemElement(int x, int y, int index, ITextComponent name, Predicate<ItemStack> acceptedForSlot) {
        super(x, y, index, name, 18, 18);
        this.validPredicate = acceptedForSlot;
    }

    @Override
    public ITextComponent getTooltipText(DisplayValueReader valueReader) {
        return null;
    }

    @Override
    public void draw(MachineScreen screen, DisplayValueReader reader, MatrixStack matrixStack, int guiLeft, int guiTop) {
        int x = guiLeft + this.getX();
        int y = guiTop + this.getY();
        screen.resetOverlayColor();
        screen.bindMachinesTexture();
        screen.blit(matrixStack, x, y, 0, 98, 18, 18);
        screen.setOverlayColor(reader.getOverlayColor(this.getIndex()));
        screen.blit(matrixStack, x, y, 18, 98, 18, 18);
    }

    public boolean isValid(ItemStack stack) {
        return this.validPredicate.test(stack);
    }

}

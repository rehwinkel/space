package deerangle.space.machine.element;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.datafixers.util.Pair;
import deerangle.space.screen.DisplayValueReader;
import deerangle.space.screen.MachineScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fluids.FluidStack;

public class FluidElement extends TooltipElement {

    public FluidElement(int x, int y, int index, ITextComponent name) {
        super(x, y, index, name, 18, 49);
    }

    @Override
    public void draw(MachineScreen screen, DisplayValueReader reader, MatrixStack matrixStack, int guiLeft, int guiTop) {
        Pair<FluidStack, Integer> fluidAndCap = reader.getFluidData(this.getIndex());
        FluidStack fluid = fluidAndCap.getFirst();
        int cap = fluidAndCap.getSecond();
        drawFluidBar(screen, matrixStack, reader, fluid.getFluid(), guiLeft + this.getX(), guiTop + this.getY(),
                fluid.getAmount() / (float) cap);
    }

    @Override
    public ITextComponent getTooltipText(DisplayValueReader reader) {
        Pair<FluidStack, Integer> fluidAndCap = reader.getFluidData(this.getIndex());
        FluidStack fluid = fluidAndCap.getFirst();
        int cap = fluidAndCap.getSecond();
        ITextComponent fluidName = new TranslationTextComponent(fluid.getTranslationKey());
        if (fluid.isEmpty()) {
            fluidName = new TranslationTextComponent("info.space.none");
        }
        return new TranslationTextComponent("info.space.fluid", fluidName, fluid.getAmount(), cap);
    }


    private void drawFluidBar(MachineScreen screen, MatrixStack matrixStack, DisplayValueReader reader, Fluid fluid, int x, int y, float amount) {
        screen.resetOverlayColor();
        screen.bindMachinesTexture();
        int height = (int) (47 * (1F - amount));
        screen.blit(matrixStack, x, y, 0, 0, 18, 49);
        this.drawFluidColumn(screen, matrixStack, fluid, x + 1, y + 1 + height, 47 - height, true);
        screen.bindMachinesTexture();
        screen.setOverlayColor(reader.getOverlayColor(this.getIndex()));
        screen.blit(matrixStack, x, y, 36, 0, 18, 49);
    }

    private void drawFluidColumn(MachineScreen screen, MatrixStack matrixStack, Fluid fluid, int x, int y, int height, boolean bottomUp) {
        TextureAtlasSprite sprite = screen.getFluidStillTexture(fluid);
        screen.bindTexture(sprite.getAtlasTexture().getTextureLocation());
        int color = fluid.getAttributes().getColor();
        screen.setOverlayColor(color);
        if (bottomUp) {
            for (int i = height; i > 0; i -= 16) {
                int drawHeight = 16 - Math.min(i, 16);
                blitSprite(matrixStack, x, y + i + drawHeight - 16, screen.getBlitOffset(), 0, drawHeight, 16, 16, 16,
                        16, sprite);
            }
        } else {
            for (int i = 0; i < height; i += 16) {
                int drawHeight = Math.min(height - i, 16);
                blitSprite(matrixStack, x, y + i, screen.getBlitOffset(), 0, 0, 16, drawHeight, 16, 16, sprite);
            }
        }
    }

    private void blitSprite(MatrixStack matrixStack, int x, int y, int blitOffset, int offX, int offY, int width, int height, int textureWidth, int textureHeight, TextureAtlasSprite sprite) {
        GlStateManager.disableBlend();
        Matrix4f matrix = matrixStack.getLast().getMatrix();
        BufferBuilder bufferbuilder = Tessellator.getInstance().getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        int x2 = x + width - offX;
        int y2 = y + height - offY;
        float minU = sprite.getMinU();
        float minV = sprite.getMinV();
        float spriteWidth = sprite.getMaxU() - minU;
        float spriteHeight = sprite.getMaxV() - minV;
        minU += (offX / (float) textureWidth) * spriteWidth;
        minV += (offY / (float) textureHeight) * spriteHeight;
        float maxU = minU + ((width - offX) / (float) textureWidth) * spriteWidth;
        float maxV = minV + ((height - offY) / (float) textureHeight) * spriteHeight;
        bufferbuilder.pos(matrix, (float) x, (float) y2, (float) blitOffset).tex(minU, maxV).endVertex();
        bufferbuilder.pos(matrix, (float) x2, (float) y2, (float) blitOffset).tex(maxU, maxV).endVertex();
        bufferbuilder.pos(matrix, (float) x2, (float) y, (float) blitOffset).tex(maxU, minV).endVertex();
        bufferbuilder.pos(matrix, (float) x, (float) y, (float) blitOffset).tex(minU, minV).endVertex();
        bufferbuilder.finishDrawing();
        RenderSystem.enableAlphaTest();
        WorldVertexBufferUploader.draw(bufferbuilder);
        GlStateManager.enableBlend();
    }


}

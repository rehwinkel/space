package deerangle.space.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import deerangle.space.container.CoalGeneratorContainer;
import deerangle.space.machine.Machine;
import deerangle.space.machine.type.Element;
import deerangle.space.machine.type.SlotElement;
import deerangle.space.main.SpaceMod;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import java.util.List;

public class CoalGeneratorScreen extends ContainerScreen<CoalGeneratorContainer> {
    // private static final ScreenTextureWorldReader TEXTURE_WORLD_READER = new ScreenTextureWorldReader();

    private static final ResourceLocation MACHINES_GUI = new ResourceLocation(SpaceMod.MOD_ID,
            "textures/gui/machine/machines.png");
    private static final ResourceLocation GENERIC_GUI = new ResourceLocation(SpaceMod.MOD_ID,
            "textures/gui/machine/generic.png");

    private final List<Element> elementList;
    private final DisplayValueReader valueReader;

    public CoalGeneratorScreen(CoalGeneratorContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        this.elementList = screenContainer.machineType.getElements();
        this.valueReader = new DisplayValueReader(screenContainer.getMachine());
    }

    @Override
    protected void init() {
        super.init();
        this.titleX = (this.xSize - this.font.getStringPropertyWidth(this.title)) / 2;
    }

    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderBarTooltips(matrixStack, mouseX, mouseY);
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
    }

    private void renderBarTooltips(MatrixStack matrixStack, int x, int y) {
        for (Element el : this.elementList) {
            if (el instanceof SlotElement) {
                int barWidth = ((SlotElement) el).getWidth();
                int barHeight = ((SlotElement) el).getHeight();
                int barX = el.getX();
                int barY = el.getY();
                if (x >= guiLeft + barX && x < guiLeft + barX + barWidth) {
                    if (y >= guiTop + barY && y < guiTop + barY + barHeight) {
                        ITextComponent tooltipText = ((SlotElement) el).getTooltipText(this.valueReader);
                        if (tooltipText != null) {
                            this.renderTooltip(matrixStack, tooltipText, x, y);
                        }
                    }
                }
            }
        }
    }

    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
        this.resetOverlayColor();
        this.minecraft.getTextureManager().bindTexture(GENERIC_GUI);
        this.blit(matrixStack, this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);

        for (Element el : this.elementList) {
            el.draw(this, this.valueReader, matrixStack, this.guiLeft, this.guiTop);
        }
    }

    public void bindMachinesTexture() {
        this.minecraft.getTextureManager().bindTexture(MACHINES_GUI);
    }

    public void setOverlayColor(int overlay) {
        RenderSystem
                .color4f(((overlay >> 16) & 0xFF) / 255.0F, ((overlay >> 8) & 0xFF) / 255.0F, (overlay & 0xFF) / 255.0F,
                        1.0F);
    }

    public void resetOverlayColor() {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    /*
    private void drawBurnDisplay(MatrixStack matrixStack, int x, int y, float amount, int overlayColor) {
        this.resetOverlayColor();
        this.bindMachinesTexture();
        this.blit(matrixStack, x, y, 0, 96 + 18, 18, 18);
        if (amount > 0f) {
            int height = (int) (13 * (1 - amount));
            this.blit(matrixStack, x + 2, y + 2 + height, 20, 116 + height, 14, 14);
        }
    }

    private void drawSlot(MatrixStack matrixStack, int x, int y, int overlay) {
        this.resetOverlayColor();
        this.bindMachinesTexture();
        this.blit(matrixStack, x, y, 0, 96, 18, 18);
        this.setOverlayColor(overlay);
        this.blit(matrixStack, x, y, 18, 96, 18, 18);
    }

    private void drawEnergyBar(MatrixStack matrixStack, int x, int y, float amount, int overlay) {
        this.resetOverlayColor();
        this.bindMachinesTexture();
        int height = (int) (46 * (1F - amount));
        this.blit(matrixStack, x, y, 0, 48, 10, 48);
        this.blit(matrixStack, x + 1, y + 1 + height, 11, 49 + height, 8, 46 - height);
        this.setOverlayColor(overlay);
        this.blit(matrixStack, x, y, 20, 48, 10, 48);
    }

    private void drawFluidBar(MatrixStack matrixStack, Fluid fluid, int x, int y, float amount, int overlay) {
        this.resetOverlayColor();
        this.bindMachinesTexture();
        int height = (int) (46 * (1F - amount));
        this.blit(matrixStack, x, y, 0, 0, 18, 48);
        this.drawFluidColumn(matrixStack, fluid, x + 1, y + 1 + height, 46 - height, true);
        this.bindMachinesTexture();
        this.setOverlayColor(overlay);
        this.blit(matrixStack, x, y, 36, 0, 18, 48);
    }

    private void drawFluidColumn(MatrixStack matrixStack, Fluid fluid, int x, int y, int height, boolean bottomUp) {
        BlockState fluidState = fluid.getDefaultState().getBlockState();
        TextureAtlasSprite sprite = this.minecraft.getBlockRendererDispatcher().getModelForState(fluidState)
                .getParticleTexture();
        this.minecraft.getTextureManager().bindTexture(sprite.getAtlasTexture().getTextureLocation());
        int color = this.minecraft.getBlockColors()
                .getColor(fluidState, TEXTURE_WORLD_READER, new BlockPos(0, 0, 0), 0);
        setOverlayColor(color);
        if (bottomUp) {
            for (int i = height; i > 0; i -= 16) {
                int drawHeight = 16 - Math.min(i, 16);
                blitSprite(matrixStack, x, y + i + drawHeight - 16, 0, drawHeight, 16, 16, 16, 16, sprite);
            }
        } else {
            for (int i = 0; i < height; i += 16) {
                int drawHeight = Math.min(height - i, 16);
                blitSprite(matrixStack, x, y + i, 0, 0, 16, drawHeight, 16, 16, sprite);
            }
        }
    }

    private void blitSprite(MatrixStack matrixStack, int x, int y, int offX, int offY, int width, int height, int textureWidth, int textureHeight, TextureAtlasSprite sprite) {
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
        bufferbuilder.pos(matrix, (float) x, (float) y2, (float) this.getBlitOffset()).tex(minU, maxV).endVertex();
        bufferbuilder.pos(matrix, (float) x2, (float) y2, (float) this.getBlitOffset()).tex(maxU, maxV).endVertex();
        bufferbuilder.pos(matrix, (float) x2, (float) y, (float) this.getBlitOffset()).tex(maxU, minV).endVertex();
        bufferbuilder.pos(matrix, (float) x, (float) y, (float) this.getBlitOffset()).tex(minU, minV).endVertex();
        bufferbuilder.finishDrawing();
        RenderSystem.enableAlphaTest();
        WorldVertexBufferUploader.draw(bufferbuilder);
    }
    */

}

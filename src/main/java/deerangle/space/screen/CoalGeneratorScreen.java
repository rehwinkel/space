package deerangle.space.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import deerangle.space.container.CoalGeneratorContainer;
import deerangle.space.main.SpaceMod;
import net.minecraft.block.BlockState;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.text.ITextComponent;

public class CoalGeneratorScreen extends ContainerScreen<CoalGeneratorContainer> {
    private static final ScreenTextureWorldReader TEXTURE_WORLD_READER = new ScreenTextureWorldReader();

    private static final ResourceLocation MACHINES_GUI = new ResourceLocation(SpaceMod.MOD_ID,
            "textures/gui/machine/machines.png");
    private static final ResourceLocation COAL_GENERATOR_GUI = new ResourceLocation(SpaceMod.MOD_ID,
            "textures/gui/machine/coal_generator.png");

    public CoalGeneratorScreen(CoalGeneratorContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
    }

    @Override
    protected void init() {
        super.init();
        this.titleX = (this.xSize - this.font.getStringPropertyWidth(this.title)) / 2;
    }

    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
    }

    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;

        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(COAL_GENERATOR_GUI);
        this.blit(matrixStack, i, j, 0, 0, this.xSize, this.ySize);
        drawEnergyBar(matrixStack, i, j, 0.5f);
        drawFluidBar(matrixStack, Fluids.LAVA, i + 20, j, 0.3f);
        // this.blit(matrixStack, i+1, j+1, 19, 49, 16, 46);
        /*
        if (this.container.isBurning()) {
            int k = this.container.getBurnLeftScaled();
            this.blit(matrixStack, i + 56, j + 36 + 12 - k, 176, 12 - k, 14, k + 1);
        }

        int l = this.container.getCookProgressionScaled();
        this.blit(matrixStack, i + 79, j + 34, 176, 14, l + 1, 16);
        */
    }

    private void drawEnergyBar(MatrixStack matrixStack, int x, int y, float amount) {
        this.minecraft.getTextureManager().bindTexture(MACHINES_GUI);
        int height = (int) (46 * (1F - amount));
        this.blit(matrixStack, x, y, 0, 48, 10, 48);
        this.blit(matrixStack, x + 1, y + 1 + height, 11, 49 + height, 8, 46 - height);
    }

    private void drawFluidBar(MatrixStack matrixStack, Fluid fluid, int x, int y, float amount) {
        this.minecraft.getTextureManager().bindTexture(MACHINES_GUI);
        int height = (int) (46 * (1F - amount));
        this.blit(matrixStack, x, y, 0, 0, 18, 48);
        this.drawFluidColumn(matrixStack, fluid, x + 1, y + 1 + height, 46 - height, true);
    }

    private void drawFluidColumn(MatrixStack matrixStack, Fluid fluid, int x, int y, int height, boolean bottomUp) {
        BlockState fluidState = fluid.getDefaultState().getBlockState();
        TextureAtlasSprite sprite = this.minecraft.getBlockRendererDispatcher().getModelForState(fluidState)
                .getParticleTexture();
        this.minecraft.getTextureManager().bindTexture(sprite.getAtlasTexture().getTextureLocation());
        int color = this.minecraft.getBlockColors()
                .getColor(fluidState, TEXTURE_WORLD_READER, new BlockPos(0, 0, 0), 0);
        RenderSystem.color4f(((color >> 16) & 0xFF) / 255.0F, ((color >> 8) & 0xFF) / 255.0F, (color & 0xFF) / 255.0F,
                1.0F);
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

}

package deerangle.space.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import deerangle.space.container.MachineContainer;
import deerangle.space.machine.element.Element;
import deerangle.space.machine.element.TooltipElement;
import deerangle.space.main.SpaceMod;
import deerangle.space.network.AdvanceSideMsg;
import deerangle.space.network.PacketHandler;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.List;

public class MachineScreen extends ContainerScreen<MachineContainer> {

    public static final ResourceLocation MACHINES_GUI = new ResourceLocation(SpaceMod.MOD_ID,
            "textures/gui/machine/machines.png");
    public static final ResourceLocation GENERIC_GUI = new ResourceLocation(SpaceMod.MOD_ID,
            "textures/gui/machine/generic.png");
    public static final ResourceLocation GENERIC_EMPTY_GUI = new ResourceLocation(SpaceMod.MOD_ID,
            "textures/gui/machine/generic_empty.png");
    private static final ITextComponent sideConfigText = new TranslationTextComponent("info.space.side_config");
    private final List<Element> elementList;
    private final DisplayValueReader valueReader;
    private final BlockPos pos;
    private boolean isMainScreen = true;
    private int sideConfigX;
    private SideColorButton frontButton;
    private SideColorButton backButton;
    private SideColorButton leftButton;
    private SideColorButton rightButton;
    private SideColorButton topButton;
    private SideColorButton bottomButton;

    public MachineScreen(MachineContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        this.elementList = screenContainer.machineType.getElements();
        this.valueReader = new DisplayValueReader(screenContainer.getMachine());
        this.pos = screenContainer.pos;
    }

    @Override
    protected void init() {
        super.init();
        addButton(new SettingsButton(guiLeft + xSize - 16 - 5, guiTop + 5, button -> {
            this.isMainScreen = !this.isMainScreen;
            this.setButtonVisibility(!this.isMainScreen);
            if (!this.isMainScreen) {
                this.container.inventorySlots.forEach(slot -> {
                    slot.xPos -= 1000;
                    slot.yPos -= 1000;
                });
            } else {
                this.container.inventorySlots.forEach(slot -> {
                    slot.xPos += 1000;
                    slot.yPos += 1000;
                });
            }
        }));
        int width = 20;
        int dist = 6;
        int size = dist + width;
        int topOffset = 48;
        this.topButton = addButton(
                new SideColorButton(guiLeft + xSize / 2 - (width / 2), guiTop + topOffset, "info.space.top", button -> {
                    PacketHandler.INSTANCE.send(PacketDistributor.SERVER.noArg(),
                            new AdvanceSideMsg(pos, button == 0, AdvanceSideMsg.Face.TOP));
                }));
        this.frontButton = addButton(
                new SideColorButton(guiLeft + xSize / 2 - (width / 2), guiTop + size + topOffset, "info.space.front",
                        button -> {
                            PacketHandler.INSTANCE.send(PacketDistributor.SERVER.noArg(),
                                    new AdvanceSideMsg(pos, button == 0, AdvanceSideMsg.Face.FRONT));
                        }));
        this.bottomButton = addButton(
                new SideColorButton(guiLeft + xSize / 2 - (width / 2), guiTop + size * 2 + topOffset,
                        "info.space.bottom", button -> {
                    PacketHandler.INSTANCE.send(PacketDistributor.SERVER.noArg(),
                            new AdvanceSideMsg(pos, button == 0, AdvanceSideMsg.Face.BOTTOM));
                }));
        this.leftButton = addButton(
                new SideColorButton(guiLeft + xSize / 2 - (width / 2) - size, guiTop + size + topOffset,
                        "info.space.left", button -> {
                    PacketHandler.INSTANCE.send(PacketDistributor.SERVER.noArg(),
                            new AdvanceSideMsg(pos, button == 0, AdvanceSideMsg.Face.LEFT));
                }));
        this.backButton = addButton(
                new SideColorButton(guiLeft + xSize / 2 - (width / 2) - size, guiTop + size * 2 + topOffset,
                        "info.space.back", button -> {
                    PacketHandler.INSTANCE.send(PacketDistributor.SERVER.noArg(),
                            new AdvanceSideMsg(pos, button == 0, AdvanceSideMsg.Face.BACK));
                }));
        this.rightButton = addButton(
                new SideColorButton(guiLeft + xSize / 2 - (width / 2) + size, guiTop + size + topOffset,
                        "info.space.right", button -> {
                    PacketHandler.INSTANCE.send(PacketDistributor.SERVER.noArg(),
                            new AdvanceSideMsg(pos, button == 0, AdvanceSideMsg.Face.RIGHT));
                }));
        this.setButtonVisibility(!isMainScreen);
        this.titleX = (this.xSize - this.font.getStringPropertyWidth(this.title)) / 2;
        this.sideConfigX = (this.xSize - this.font.getStringPropertyWidth(sideConfigText)) / 2;
    }

    @Override
    public void tick() {
        super.tick();
        this.leftButton.setColor(this.valueReader.getLeftColor());
        this.rightButton.setColor(this.valueReader.getRightColor());
        this.topButton.setColor(this.valueReader.getTopColor());
        this.bottomButton.setColor(this.valueReader.getBottomColor());
        this.frontButton.setColor(this.valueReader.getFrontColor());
        this.backButton.setColor(this.valueReader.getBackColor());
        this.leftButton.setSelectedName(this.valueReader.getLeftName());
        this.rightButton.setSelectedName(this.valueReader.getRightName());
        this.topButton.setSelectedName(this.valueReader.getTopName());
        this.bottomButton.setSelectedName(this.valueReader.getBottomName());
        this.frontButton.setSelectedName(this.valueReader.getFrontName());
        this.backButton.setSelectedName(this.valueReader.getBackName());
    }

    private void setButtonVisibility(boolean visible) {
        this.frontButton.visible = visible;
        this.backButton.visible = visible;
        this.leftButton.visible = visible;
        this.rightButton.visible = visible;
        this.topButton.visible = visible;
        this.bottomButton.visible = visible;
    }

    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        if (isMainScreen) {
            this.renderBarTooltips(matrixStack, mouseX, mouseY);
            this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
        } else {
            this.renderSideButtonTooltips(matrixStack, mouseX, mouseY);
        }
    }

    private void renderSideButtonTooltips(MatrixStack matrixStack, int x, int y) {
        this.topButton.drawTooltip(this, matrixStack, x, y);
        this.bottomButton.drawTooltip(this, matrixStack, x, y);
        this.leftButton.drawTooltip(this, matrixStack, x, y);
        this.rightButton.drawTooltip(this, matrixStack, x, y);
        this.frontButton.drawTooltip(this, matrixStack, x, y);
        this.backButton.drawTooltip(this, matrixStack, x, y);
    }

    private void renderBarTooltips(MatrixStack matrixStack, int x, int y) {
        for (Element el : this.elementList) {
            if (el instanceof TooltipElement) {
                int barWidth = ((TooltipElement) el).getWidth();
                int barHeight = ((TooltipElement) el).getHeight();
                int barX = el.getX();
                int barY = el.getY();
                if (x >= guiLeft + barX && x < guiLeft + barX + barWidth) {
                    if (y >= guiTop + barY && y < guiTop + barY + barHeight) {
                        ITextComponent tooltipText = ((TooltipElement) el).getTooltipText(this.valueReader);
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
        if (isMainScreen) {
            this.minecraft.getTextureManager().bindTexture(GENERIC_GUI);
        } else {
            this.minecraft.getTextureManager().bindTexture(GENERIC_EMPTY_GUI);
        }
        this.blit(matrixStack, this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);

        if (isMainScreen) {
            for (Element el : this.elementList) {
                el.draw(this, this.valueReader, matrixStack, this.guiLeft, this.guiTop);
            }
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int x, int y) {
        if (isMainScreen) {
            this.font.func_243248_b(matrixStack, this.title, (float) this.titleX, (float) this.titleY, 4210752);
            this.font.func_243248_b(matrixStack, this.playerInventory.getDisplayName(),
                    (float) this.playerInventoryTitleX, (float) this.playerInventoryTitleY, 4210752);
        } else {
            this.font
                    .func_243248_b(matrixStack, sideConfigText, (float) this.sideConfigX, (float) this.titleY, 4210752);
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

    public void bindTexture(ResourceLocation textureLocation) {
        this.minecraft.getTextureManager().bindTexture(textureLocation);
    }

    public TextureAtlasSprite getFluidStillTexture(Fluid fluid) {
        return this.minecraft.getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE)
                .apply(fluid.getAttributes().getStillTexture());
    }
}

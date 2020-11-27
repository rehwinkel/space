package deerangle.space.planet.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import deerangle.space.planet.Planet;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.settings.CloudOption;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.IBlockDisplayReader;
import net.minecraft.world.LightType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.Heightmap;

import java.util.List;
import java.util.OptionalDouble;
import java.util.Random;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class AtmosphereRenderer extends AbstractAtmosphereRenderer {

    private static final ResourceLocation CLOUDS_TEXTURES = new ResourceLocation("textures/environment/clouds.png");
    private static final ResourceLocation RAIN_TEXTURES = new ResourceLocation("textures/environment/rain.png");
    private static final ResourceLocation SNOW_TEXTURES = new ResourceLocation("textures/environment/snow.png");

    private final VertexFormat skyVertexFormat = DefaultVertexFormats.POSITION;
    private VertexBuffer skyVBO;
    private VertexBuffer sky2VBO;
    private VertexBuffer starVBO;
    private final float[] sunsetColor = new float[4];
    private final BiConsumer<Float, float[]> sunsetColorSetter;
    private final OptionalDouble fixedSunsetAlpha;

    private final List<Planet> orbitingPlanets;

    private VertexBuffer cloudsVBO;
    private CloudOption cloudOption;
    private boolean cloudsNeedUpdate;
    private Vector3d cloudsCheckColor = Vector3d.ZERO;
    private final BiFunction<Float, Float, Vector3d> cloudColor;

    private final float[] rainSizeX = new float[1024];
    private final float[] rainSizeZ = new float[1024];

    public AtmosphereRenderer(Planet planet) {
        this.orbitingPlanets = planet.getSkyPlanets().stream().map(Supplier::get).collect(Collectors.toList());
        this.sunsetColorSetter = planet.getSunsetColors();
        this.fixedSunsetAlpha = planet.getSunsetAlpha();
        this.cloudColor = planet.getCloudColor();
        this.cloudHeight = planet.getCloudHeight();
        generateSky();
        generateSky2();
        generateStars();

        for (int i = 0; i < 32; ++i) {
            for (int j = 0; j < 32; ++j) {
                float f = (float) (j - 16);
                float f1 = (float) (i - 16);
                float f2 = MathHelper.sqrt(f * f + f1 * f1);
                this.rainSizeX[i << 5 | j] = -f1 / f2;
                this.rainSizeZ[i << 5 | j] = f / f2;
            }
        }
    }

    private void generateSky() {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        if (this.skyVBO != null) {
            this.skyVBO.close();
        }

        this.skyVBO = new VertexBuffer(this.skyVertexFormat);
        this.renderSkyBox(bufferbuilder, 16.0F, false);
        bufferbuilder.finishDrawing();
        this.skyVBO.upload(bufferbuilder);
    }

    private void generateSky2() {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        if (this.sky2VBO != null) {
            this.sky2VBO.close();
        }

        this.sky2VBO = new VertexBuffer(this.skyVertexFormat);
        this.renderSkyBox(bufferbuilder, -16.0F, true);
        bufferbuilder.finishDrawing();
        this.sky2VBO.upload(bufferbuilder);
    }

    private void generateStars() {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        if (this.starVBO != null) {
            this.starVBO.close();
        }

        this.starVBO = new VertexBuffer(this.skyVertexFormat);
        this.renderStars(bufferbuilder);
        bufferbuilder.finishDrawing();
        this.starVBO.upload(bufferbuilder);
    }

    private void renderStars(BufferBuilder bufferBuilderIn) {
        Random random = new Random(10842L);
        bufferBuilderIn.begin(7, DefaultVertexFormats.POSITION);

        for (int i = 0; i < 1500; ++i) {
            double d0 = (random.nextFloat() * 2.0F - 1.0F);
            double d1 = (random.nextFloat() * 2.0F - 1.0F);
            double d2 = (random.nextFloat() * 2.0F - 1.0F);
            double d3 = (0.15F + random.nextFloat() * 0.1F);
            double d4 = d0 * d0 + d1 * d1 + d2 * d2;
            if (d4 < 1.0D && d4 > 0.01D) {
                d4 = 1.0D / Math.sqrt(d4);
                d0 = d0 * d4;
                d1 = d1 * d4;
                d2 = d2 * d4;
                double d5 = d0 * 100.0D;
                double d6 = d1 * 100.0D;
                double d7 = d2 * 100.0D;
                double d8 = Math.atan2(d0, d2);
                double d9 = Math.sin(d8);
                double d10 = Math.cos(d8);
                double d11 = Math.atan2(Math.sqrt(d0 * d0 + d2 * d2), d1);
                double d12 = Math.sin(d11);
                double d13 = Math.cos(d11);
                double d14 = random.nextDouble() * Math.PI * 2.0D;
                double d15 = Math.sin(d14);
                double d16 = Math.cos(d14);

                for (int j = 0; j < 4; ++j) {
                    double d18 = (double) ((j & 2) - 1) * d3;
                    double d19 = (double) ((j + 1 & 2) - 1) * d3;
                    double d21 = d18 * d16 - d19 * d15;
                    double d22 = d19 * d16 + d18 * d15;
                    double d23 = d21 * d12 + 0.0D * d13;
                    double d24 = 0.0D * d12 - d21 * d13;
                    double d25 = d24 * d9 - d22 * d10;
                    double d26 = d22 * d9 + d24 * d10;
                    bufferBuilderIn.pos(d5 + d25, d6 + d23, d7 + d26).endVertex();
                }
            }
        }

    }

    private void renderSkyBox(BufferBuilder bufferBuilderIn, float posY, boolean reverseX) {
        bufferBuilderIn.begin(7, DefaultVertexFormats.POSITION);

        for (int k = -384; k <= 384; k += 64) {
            for (int l = -384; l <= 384; l += 64) {
                float f = (float) k;
                float f1 = (float) (k + 64);
                if (reverseX) {
                    f1 = (float) k;
                    f = (float) (k + 64);
                }

                bufferBuilderIn.pos(f, posY, l).endVertex();
                bufferBuilderIn.pos(f1, posY, l).endVertex();
                bufferBuilderIn.pos(f1, posY, l + 64).endVertex();
                bufferBuilderIn.pos(f, posY, l + 64).endVertex();
            }
        }

    }

    private void drawClouds(BufferBuilder bufferIn, double cloudsX, double cloudsY, double cloudsZ, Vector3d cloudsColor) {
        float f3 = (float) MathHelper.floor(cloudsX) * 0.00390625F;
        float f4 = (float) MathHelper.floor(cloudsZ) * 0.00390625F;
        float f5 = (float) cloudsColor.x;
        float f6 = (float) cloudsColor.y;
        float f7 = (float) cloudsColor.z;
        float f8 = f5 * 0.9F;
        float f9 = f6 * 0.9F;
        float f10 = f7 * 0.9F;
        float f11 = f5 * 0.7F;
        float f12 = f6 * 0.7F;
        float f13 = f7 * 0.7F;
        float f14 = f5 * 0.8F;
        float f15 = f6 * 0.8F;
        float f16 = f7 * 0.8F;
        bufferIn.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
        float f17 = (float) Math.floor(cloudsY / 4.0D) * 4.0F;
        if (this.cloudOption == CloudOption.FANCY) {
            for (int k = -3; k <= 4; ++k) {
                for (int l = -3; l <= 4; ++l) {
                    float f18 = (float) (k * 8);
                    float f19 = (float) (l * 8);
                    if (f17 > -5.0F) {
                        bufferIn.pos((f18 + 0.0F), (f17 + 0.0F), (f19 + 8.0F))
                                .tex((f18 + 0.0F) * 0.00390625F + f3, (f19 + 8.0F) * 0.00390625F + f4)
                                .color(f11, f12, f13, 0.8F).normal(0.0F, -1.0F, 0.0F).endVertex();
                        bufferIn.pos((f18 + 8.0F), (f17 + 0.0F), (f19 + 8.0F))
                                .tex((f18 + 8.0F) * 0.00390625F + f3, (f19 + 8.0F) * 0.00390625F + f4)
                                .color(f11, f12, f13, 0.8F).normal(0.0F, -1.0F, 0.0F).endVertex();
                        bufferIn.pos((f18 + 8.0F), (f17 + 0.0F), (f19 + 0.0F))
                                .tex((f18 + 8.0F) * 0.00390625F + f3, (f19 + 0.0F) * 0.00390625F + f4)
                                .color(f11, f12, f13, 0.8F).normal(0.0F, -1.0F, 0.0F).endVertex();
                        bufferIn.pos((f18 + 0.0F), (f17 + 0.0F), (f19 + 0.0F))
                                .tex((f18 + 0.0F) * 0.00390625F + f3, (f19 + 0.0F) * 0.00390625F + f4)
                                .color(f11, f12, f13, 0.8F).normal(0.0F, -1.0F, 0.0F).endVertex();
                    }

                    if (f17 <= 5.0F) {
                        bufferIn.pos((f18 + 0.0F), (f17 + 4.0F - 9.765625E-4F), (f19 + 8.0F))
                                .tex((f18 + 0.0F) * 0.00390625F + f3, (f19 + 8.0F) * 0.00390625F + f4)
                                .color(f5, f6, f7, 0.8F).normal(0.0F, 1.0F, 0.0F).endVertex();
                        bufferIn.pos((f18 + 8.0F), (f17 + 4.0F - 9.765625E-4F), (f19 + 8.0F))
                                .tex((f18 + 8.0F) * 0.00390625F + f3, (f19 + 8.0F) * 0.00390625F + f4)
                                .color(f5, f6, f7, 0.8F).normal(0.0F, 1.0F, 0.0F).endVertex();
                        bufferIn.pos((f18 + 8.0F), (f17 + 4.0F - 9.765625E-4F), (f19 + 0.0F))
                                .tex((f18 + 8.0F) * 0.00390625F + f3, (f19 + 0.0F) * 0.00390625F + f4)
                                .color(f5, f6, f7, 0.8F).normal(0.0F, 1.0F, 0.0F).endVertex();
                        bufferIn.pos((f18 + 0.0F), (f17 + 4.0F - 9.765625E-4F), (f19 + 0.0F))
                                .tex((f18 + 0.0F) * 0.00390625F + f3, (f19 + 0.0F) * 0.00390625F + f4)
                                .color(f5, f6, f7, 0.8F).normal(0.0F, 1.0F, 0.0F).endVertex();
                    }

                    if (k > -1) {
                        for (int i1 = 0; i1 < 8; ++i1) {
                            bufferIn.pos((f18 + (float) i1 + 0.0F), (f17 + 0.0F), (f19 + 8.0F))
                                    .tex((f18 + (float) i1 + 0.5F) * 0.00390625F + f3, (f19 + 8.0F) * 0.00390625F + f4)
                                    .color(f8, f9, f10, 0.8F).normal(-1.0F, 0.0F, 0.0F).endVertex();
                            bufferIn.pos((f18 + (float) i1 + 0.0F), (f17 + 4.0F), (f19 + 8.0F))
                                    .tex((f18 + (float) i1 + 0.5F) * 0.00390625F + f3, (f19 + 8.0F) * 0.00390625F + f4)
                                    .color(f8, f9, f10, 0.8F).normal(-1.0F, 0.0F, 0.0F).endVertex();
                            bufferIn.pos((f18 + (float) i1 + 0.0F), (f17 + 4.0F), (f19 + 0.0F))
                                    .tex((f18 + (float) i1 + 0.5F) * 0.00390625F + f3, (f19 + 0.0F) * 0.00390625F + f4)
                                    .color(f8, f9, f10, 0.8F).normal(-1.0F, 0.0F, 0.0F).endVertex();
                            bufferIn.pos((f18 + (float) i1 + 0.0F), (f17 + 0.0F), (f19 + 0.0F))
                                    .tex((f18 + (float) i1 + 0.5F) * 0.00390625F + f3, (f19 + 0.0F) * 0.00390625F + f4)
                                    .color(f8, f9, f10, 0.8F).normal(-1.0F, 0.0F, 0.0F).endVertex();
                        }
                    }

                    if (k <= 1) {
                        for (int j2 = 0; j2 < 8; ++j2) {
                            bufferIn.pos((f18 + (float) j2 + 1.0F - 9.765625E-4F), (f17 + 0.0F), (f19 + 8.0F))
                                    .tex((f18 + (float) j2 + 0.5F) * 0.00390625F + f3, (f19 + 8.0F) * 0.00390625F + f4)
                                    .color(f8, f9, f10, 0.8F).normal(1.0F, 0.0F, 0.0F).endVertex();
                            bufferIn.pos((f18 + (float) j2 + 1.0F - 9.765625E-4F), (f17 + 4.0F), (f19 + 8.0F))
                                    .tex((f18 + (float) j2 + 0.5F) * 0.00390625F + f3, (f19 + 8.0F) * 0.00390625F + f4)
                                    .color(f8, f9, f10, 0.8F).normal(1.0F, 0.0F, 0.0F).endVertex();
                            bufferIn.pos((f18 + (float) j2 + 1.0F - 9.765625E-4F), (f17 + 4.0F), (f19 + 0.0F))
                                    .tex((f18 + (float) j2 + 0.5F) * 0.00390625F + f3, (f19 + 0.0F) * 0.00390625F + f4)
                                    .color(f8, f9, f10, 0.8F).normal(1.0F, 0.0F, 0.0F).endVertex();
                            bufferIn.pos((f18 + (float) j2 + 1.0F - 9.765625E-4F), (f17 + 0.0F), (f19 + 0.0F))
                                    .tex((f18 + (float) j2 + 0.5F) * 0.00390625F + f3, (f19 + 0.0F) * 0.00390625F + f4)
                                    .color(f8, f9, f10, 0.8F).normal(1.0F, 0.0F, 0.0F).endVertex();
                        }
                    }

                    if (l > -1) {
                        for (int k2 = 0; k2 < 8; ++k2) {
                            bufferIn.pos((f18 + 0.0F), (f17 + 4.0F), (f19 + (float) k2 + 0.0F))
                                    .tex((f18 + 0.0F) * 0.00390625F + f3, (f19 + (float) k2 + 0.5F) * 0.00390625F + f4)
                                    .color(f14, f15, f16, 0.8F).normal(0.0F, 0.0F, -1.0F).endVertex();
                            bufferIn.pos((f18 + 8.0F), (f17 + 4.0F), (f19 + (float) k2 + 0.0F))
                                    .tex((f18 + 8.0F) * 0.00390625F + f3, (f19 + (float) k2 + 0.5F) * 0.00390625F + f4)
                                    .color(f14, f15, f16, 0.8F).normal(0.0F, 0.0F, -1.0F).endVertex();
                            bufferIn.pos((f18 + 8.0F), (f17 + 0.0F), (f19 + (float) k2 + 0.0F))
                                    .tex((f18 + 8.0F) * 0.00390625F + f3, (f19 + (float) k2 + 0.5F) * 0.00390625F + f4)
                                    .color(f14, f15, f16, 0.8F).normal(0.0F, 0.0F, -1.0F).endVertex();
                            bufferIn.pos((f18 + 0.0F), (f17 + 0.0F), (f19 + (float) k2 + 0.0F))
                                    .tex((f18 + 0.0F) * 0.00390625F + f3, (f19 + (float) k2 + 0.5F) * 0.00390625F + f4)
                                    .color(f14, f15, f16, 0.8F).normal(0.0F, 0.0F, -1.0F).endVertex();
                        }
                    }

                    if (l <= 1) {
                        for (int l2 = 0; l2 < 8; ++l2) {
                            bufferIn.pos((f18 + 0.0F), (f17 + 4.0F), (f19 + (float) l2 + 1.0F - 9.765625E-4F))
                                    .tex((f18 + 0.0F) * 0.00390625F + f3, (f19 + (float) l2 + 0.5F) * 0.00390625F + f4)
                                    .color(f14, f15, f16, 0.8F).normal(0.0F, 0.0F, 1.0F).endVertex();
                            bufferIn.pos((f18 + 8.0F), (f17 + 4.0F), (f19 + (float) l2 + 1.0F - 9.765625E-4F))
                                    .tex((f18 + 8.0F) * 0.00390625F + f3, (f19 + (float) l2 + 0.5F) * 0.00390625F + f4)
                                    .color(f14, f15, f16, 0.8F).normal(0.0F, 0.0F, 1.0F).endVertex();
                            bufferIn.pos((f18 + 8.0F), (f17 + 0.0F), (f19 + (float) l2 + 1.0F - 9.765625E-4F))
                                    .tex((f18 + 8.0F) * 0.00390625F + f3, (f19 + (float) l2 + 0.5F) * 0.00390625F + f4)
                                    .color(f14, f15, f16, 0.8F).normal(0.0F, 0.0F, 1.0F).endVertex();
                            bufferIn.pos((f18 + 0.0F), (f17 + 0.0F), (f19 + (float) l2 + 1.0F - 9.765625E-4F))
                                    .tex((f18 + 0.0F) * 0.00390625F + f3, (f19 + (float) l2 + 0.5F) * 0.00390625F + f4)
                                    .color(f14, f15, f16, 0.8F).normal(0.0F, 0.0F, 1.0F).endVertex();
                        }
                    }
                }
            }
        } else {
            for (int l1 = -32; l1 < 32; l1 += 32) {
                for (int i2 = -32; i2 < 32; i2 += 32) {
                    bufferIn.pos(l1, f17, (i2 + 32))
                            .tex((float) (l1) * 0.00390625F + f3, (float) (i2 + 32) * 0.00390625F + f4)
                            .color(f5, f6, f7, 0.8F).normal(0.0F, -1.0F, 0.0F).endVertex();
                    bufferIn.pos(l1 + 32, f17, (i2 + 32))
                            .tex((float) (l1 + 32) * 0.00390625F + f3, (float) (i2 + 32) * 0.00390625F + f4)
                            .color(f5, f6, f7, 0.8F).normal(0.0F, -1.0F, 0.0F).endVertex();
                    bufferIn.pos(l1 + 32, f17, (i2))
                            .tex((float) (l1 + 32) * 0.00390625F + f3, (float) (i2) * 0.00390625F + f4)
                            .color(f5, f6, f7, 0.8F).normal(0.0F, -1.0F, 0.0F).endVertex();
                    bufferIn.pos(l1, f17, (i2)).tex((float) (l1) * 0.00390625F + f3, (float) (i2) * 0.00390625F + f4)
                            .color(f5, f6, f7, 0.8F).normal(0.0F, -1.0F, 0.0F).endVertex();
                }
            }
        }

    }

    @Override
    protected void renderClouds(int ticks, float partialTicks, MatrixStack matrixStackIn, ClientWorld world, Minecraft mc, double cloudsCheckX, double cloudsCheckY, double cloudsCheckZ) {
        float f = this.cloudHeight;
        if (!Float.isNaN(f)) {
            RenderSystem.disableCull();
            RenderSystem.enableBlend();
            RenderSystem.enableAlphaTest();
            RenderSystem.enableDepthTest();
            RenderSystem.defaultAlphaFunc();
            RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
                    GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE,
                    GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            RenderSystem.enableFog();
            RenderSystem.depthMask(true);
            double d1 = (((float) ticks + partialTicks) * 0.03F);
            double d2 = (cloudsCheckX + d1) / 12.0D;
            double d3 = (f - (float) cloudsCheckY + 0.33F);
            double d4 = cloudsCheckZ / 12.0D + (double) 0.33F;
            d2 = d2 - (double) (MathHelper.floor(d2 / 2048.0D) * 2048);
            d4 = d4 - (double) (MathHelper.floor(d4 / 2048.0D) * 2048);
            float f3 = (float) (d2 - (double) MathHelper.floor(d2));
            float f4 = (float) (d3 / 4.0D - (double) MathHelper.floor(d3 / 4.0D)) * 4.0F;
            float f5 = (float) (d4 - (double) MathHelper.floor(d4));
            Vector3d vector3d = this.cloudColor.apply(partialTicks, world.func_242415_f(partialTicks));
            int i = (int) Math.floor(d2);
            int j = (int) Math.floor(d3 / 4.0D);
            int k = (int) Math.floor(d4);
            if (i != cloudsCheckX || j != cloudsCheckY || k != cloudsCheckZ || mc.gameSettings
                    .getCloudOption() != this.cloudOption || this.cloudsCheckColor
                    .squareDistanceTo(vector3d) > 2.0E-4D) {
                this.cloudsCheckColor = vector3d;
                this.cloudOption = mc.gameSettings.getCloudOption();
                this.cloudsNeedUpdate = true;
            }

            if (this.cloudsNeedUpdate) {
                this.cloudsNeedUpdate = false;
                BufferBuilder bufferbuilder = Tessellator.getInstance().getBuffer();
                if (this.cloudsVBO != null) {
                    this.cloudsVBO.close();
                }

                this.cloudsVBO = new VertexBuffer(DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
                this.drawClouds(bufferbuilder, d2, d3, d4, vector3d);
                bufferbuilder.finishDrawing();
                this.cloudsVBO.upload(bufferbuilder);
            }

            mc.textureManager.bindTexture(CLOUDS_TEXTURES);
            matrixStackIn.push();
            matrixStackIn.scale(12.0F, 1.0F, 12.0F);
            matrixStackIn.translate((-f3), f4, (-f5));
            if (this.cloudsVBO != null) {
                this.cloudsVBO.bindBuffer();
                DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL.setupBufferState(0L);
                int i1 = this.cloudOption == CloudOption.FANCY ? 0 : 1;

                for (int l = i1; l < 2; ++l) {
                    if (l == 0) {
                        RenderSystem.colorMask(false, false, false, false);
                    } else {
                        RenderSystem.colorMask(true, true, true, true);
                    }

                    this.cloudsVBO.draw(matrixStackIn.getLast().getMatrix(), 7);
                }

                VertexBuffer.unbindBuffer();
                DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL.clearBufferState();
            }

            matrixStackIn.pop();
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.disableAlphaTest();
            RenderSystem.enableCull();
            RenderSystem.disableBlend();
            RenderSystem.disableFog();
        }
    }

    @Override
    protected void renderSky(int ticks, float partialTicks, MatrixStack matrixStackIn, ClientWorld world, Minecraft mc) {
        RenderSystem.disableTexture();
        Vector3d vector3d = world.getSkyColor(mc.gameRenderer.getActiveRenderInfo().getBlockPos(), partialTicks);
        float f = (float) vector3d.x;
        float f1 = (float) vector3d.y;
        float f2 = (float) vector3d.z;
        FogRenderer.applyFog();
        BufferBuilder bufferbuilder = Tessellator.getInstance().getBuffer();
        RenderSystem.depthMask(false);
        RenderSystem.enableFog();
        RenderSystem.color3f(f, f1, f2);
        this.skyVBO.bindBuffer();
        this.skyVertexFormat.setupBufferState(0L);
        this.skyVBO.draw(matrixStackIn.getLast().getMatrix(), 7);
        VertexBuffer.unbindBuffer();
        this.skyVertexFormat.clearBufferState();
        RenderSystem.disableFog();
        RenderSystem.disableAlphaTest();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        float[] sunsetColor = world.func_239132_a_().func_230492_a_(world.func_242415_f(partialTicks), partialTicks);
        if (sunsetColor != null) {
            RenderSystem.disableTexture();
            RenderSystem.shadeModel(7425);
            matrixStackIn.push();
            matrixStackIn.rotate(Vector3f.XP.rotationDegrees(90.0F));
            float f3 = MathHelper.sin(world.getCelestialAngleRadians(partialTicks)) < 0.0F ? 180.0F : 0.0F;
            matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(f3));
            matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(90.0F));
            float f4 = sunsetColor[0];
            float f5 = sunsetColor[1];
            float f6 = sunsetColor[2];
            Matrix4f matrix4f = matrixStackIn.getLast().getMatrix();
            bufferbuilder.begin(6, DefaultVertexFormats.POSITION_COLOR);
            bufferbuilder.pos(matrix4f, 0.0F, 100.0F, 0.0F).color(f4, f5, f6, sunsetColor[3]).endVertex();

            for (int j = 0; j <= 16; ++j) {
                float f7 = (float) j * ((float) Math.PI * 2F) / 16.0F;
                float f8 = MathHelper.sin(f7);
                float f9 = MathHelper.cos(f7);
                bufferbuilder.pos(matrix4f, f8 * 120.0F, f9 * 120.0F, -f9 * 40.0F * sunsetColor[3])
                        .color(sunsetColor[0], sunsetColor[1], sunsetColor[2], 0.0F).endVertex();
            }

            bufferbuilder.finishDrawing();
            WorldVertexBufferUploader.draw(bufferbuilder);
            matrixStackIn.pop();
            RenderSystem.shadeModel(7424);
        }

        RenderSystem.enableTexture();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE,
                GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);

        matrixStackIn.push();
        float oneMinusRainStrength = 1.0F - world.getRainStrength(partialTicks);

        for (Planet planet : this.orbitingPlanets) {
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, oneMinusRainStrength);
            matrixStackIn.rotate(Vector3f.YP.rotationDegrees(planet.getOrbitDirection()));
            matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(planet.getOrbitTilt()));
            matrixStackIn.rotate(Vector3f.XP
                    .rotationDegrees(planet.getSkyPosition(world.func_242415_f(partialTicks) * 360.0F)));
            Matrix4f matrix4f1 = matrixStackIn.getLast().getMatrix();
            float planetSize = planet.getSizeInSky();
            mc.textureManager.bindTexture(planet.getSkyTexture());
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
            bufferbuilder.pos(matrix4f1, -planetSize, 100.0F, -planetSize).tex(0.0F, 0.0F).endVertex();
            bufferbuilder.pos(matrix4f1, planetSize, 100.0F, -planetSize).tex(1.0F, 0.0F).endVertex();
            bufferbuilder.pos(matrix4f1, planetSize, 100.0F, planetSize).tex(1.0F, 1.0F).endVertex();
            bufferbuilder.pos(matrix4f1, -planetSize, 100.0F, planetSize).tex(0.0F, 1.0F).endVertex();
            bufferbuilder.finishDrawing();
            WorldVertexBufferUploader.draw(bufferbuilder);
        }

        RenderSystem.disableTexture();
        float f10 = world.getStarBrightness(partialTicks) * oneMinusRainStrength;
        if (f10 > 0.0F) {
            RenderSystem.color4f(f10, f10, f10, f10);
            this.starVBO.bindBuffer();
            this.skyVertexFormat.setupBufferState(0L);
            this.starVBO.draw(matrixStackIn.getLast().getMatrix(), 7);
            VertexBuffer.unbindBuffer();
            this.skyVertexFormat.clearBufferState();
        }

        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.disableBlend();
        RenderSystem.enableAlphaTest();
        RenderSystem.enableFog();
        matrixStackIn.pop();


        RenderSystem.disableTexture();
        RenderSystem.color3f(0.0F, 0.0F, 0.0F);
        double d0 = mc.player.getEyePosition(partialTicks).y - world.getWorldInfo().getVoidFogHeight();
        if (d0 < 0.0D) {
            matrixStackIn.push();
            matrixStackIn.translate(0.0D, 12.0D, 0.0D);
            this.sky2VBO.bindBuffer();
            this.skyVertexFormat.setupBufferState(0L);
            this.sky2VBO.draw(matrixStackIn.getLast().getMatrix(), 7);
            VertexBuffer.unbindBuffer();
            this.skyVertexFormat.clearBufferState();
            matrixStackIn.pop();
        }

        if (world.func_239132_a_().func_239216_b_()) {
            RenderSystem.color3f(f * 0.2F + 0.04F, f1 * 0.2F + 0.04F, f2 * 0.6F + 0.1F);
        } else {
            RenderSystem.color3f(f, f1, f2);
        }

        RenderSystem.enableTexture();
        RenderSystem.depthMask(true);
        RenderSystem.disableFog();
    }

    @Override
    protected void renderWeather(int ticks, float partialTicks, ClientWorld world, Minecraft mc, LightTexture lightmapIn, double xIn, double yIn, double zIn) {
        float f = 0;//world.getRainStrength(partialTicks);
        if (!(f <= 0.0F)) {
            lightmapIn.enableLightmap();
            int i = MathHelper.floor(xIn);
            int j = MathHelper.floor(yIn);
            int k = MathHelper.floor(zIn);
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBuffer();
            RenderSystem.enableAlphaTest();
            RenderSystem.disableCull();
            RenderSystem.normal3f(0.0F, 1.0F, 0.0F);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.defaultAlphaFunc();
            RenderSystem.enableDepthTest();
            int l = 5;
            if (Minecraft.isFancyGraphicsEnabled()) {
                l = 10;
            }

            RenderSystem.depthMask(Minecraft.isFabulousGraphicsEnabled());
            int i1 = -1;
            float f1 = (float) ticks + partialTicks;
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();

            for (int j1 = k - l; j1 <= k + l; ++j1) {
                for (int k1 = i - l; k1 <= i + l; ++k1) {
                    int l1 = (j1 - k + 16) * 32 + k1 - i + 16;
                    double d0 = (double) this.rainSizeX[l1] * 0.5D;
                    double d1 = (double) this.rainSizeZ[l1] * 0.5D;
                    blockpos$mutable.setPos(k1, 0, j1);
                    Biome biome = world.getBiome(blockpos$mutable);
                    if (true) {//biome.getPrecipitation() != Biome.RainType.NONE) {
                        int i2 = world.getHeight(Heightmap.Type.MOTION_BLOCKING, blockpos$mutable).getY();
                        int j2 = j - l;
                        int k2 = j + l;
                        if (j2 < i2) {
                            j2 = i2;
                        }

                        if (k2 < i2) {
                            k2 = i2;
                        }

                        int l2 = i2;
                        if (i2 < j) {
                            l2 = j;
                        }

                        if (j2 != k2) {
                            Random random = new Random(
                                    (k1 * k1 * 3121 + k1 * 45238971 ^ j1 * j1 * 418711 + j1 * 13761));
                            blockpos$mutable.setPos(k1, j2, j1);
                            float f2 = biome.getTemperature(blockpos$mutable);
                            if (f2 >= 0.15F) {
                                if (i1 != 0) {
                                    if (i1 >= 0) {
                                        tessellator.draw();
                                    }

                                    i1 = 0;
                                    mc.getTextureManager().bindTexture(RAIN_TEXTURES);
                                    bufferbuilder.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
                                }

                                int i3 = ticks + k1 * k1 * 3121 + k1 * 45238971 + j1 * j1 * 418711 + j1 * 13761 & 31;
                                float f3 = -((float) i3 + partialTicks) / 32.0F * (3.0F + random.nextFloat());
                                double d2 = (double) ((float) k1 + 0.5F) - xIn;
                                double d4 = (double) ((float) j1 + 0.5F) - zIn;
                                float f4 = MathHelper.sqrt(d2 * d2 + d4 * d4) / (float) l;
                                float f5 = ((1.0F - f4 * f4) * 0.5F + 0.5F) * f;
                                blockpos$mutable.setPos(k1, l2, j1);
                                int j3 = getCombinedLight(world, blockpos$mutable);
                                bufferbuilder.pos((double) k1 - xIn - d0 + 0.5D, (double) k2 - yIn,
                                        (double) j1 - zIn - d1 + 0.5D).tex(0.0F, (float) j2 * 0.25F + f3)
                                        .color(1.0F, 1.0F, 1.0F, f5).lightmap(j3).endVertex();
                                bufferbuilder.pos((double) k1 - xIn + d0 + 0.5D, (double) k2 - yIn,
                                        (double) j1 - zIn + d1 + 0.5D).tex(1.0F, (float) j2 * 0.25F + f3)
                                        .color(1.0F, 1.0F, 1.0F, f5).lightmap(j3).endVertex();
                                bufferbuilder.pos((double) k1 - xIn + d0 + 0.5D, (double) j2 - yIn,
                                        (double) j1 - zIn + d1 + 0.5D).tex(1.0F, (float) k2 * 0.25F + f3)
                                        .color(1.0F, 1.0F, 1.0F, f5).lightmap(j3).endVertex();
                                bufferbuilder.pos((double) k1 - xIn - d0 + 0.5D, (double) j2 - yIn,
                                        (double) j1 - zIn - d1 + 0.5D).tex(0.0F, (float) k2 * 0.25F + f3)
                                        .color(1.0F, 1.0F, 1.0F, f5).lightmap(j3).endVertex();
                            } else {
                                if (i1 != 1) {
                                    if (i1 >= 0) {
                                        tessellator.draw();
                                    }

                                    i1 = 1;
                                    mc.getTextureManager().bindTexture(SNOW_TEXTURES);
                                    bufferbuilder.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
                                }

                                float f6 = -((float) (ticks & 511) + partialTicks) / 512.0F;
                                float f7 = (float) (random.nextDouble() + (double) f1 * 0.01D * (double) ((float) random
                                        .nextGaussian()));
                                float f8 = (float) (random.nextDouble() + (double) (f1 * (float) random
                                        .nextGaussian()) * 0.001D);
                                double d3 = (double) ((float) k1 + 0.5F) - xIn;
                                double d5 = (double) ((float) j1 + 0.5F) - zIn;
                                float f9 = MathHelper.sqrt(d3 * d3 + d5 * d5) / (float) l;
                                float f10 = ((1.0F - f9 * f9) * 0.3F + 0.5F) * f;
                                blockpos$mutable.setPos(k1, l2, j1);
                                int k3 = getCombinedLight(world, blockpos$mutable);
                                int l3 = k3 >> 16 & '\uffff';
                                int i4 = (k3 & '\uffff') * 3;
                                int j4 = (l3 * 3 + 240) / 4;
                                int k4 = (i4 * 3 + 240) / 4;
                                bufferbuilder.pos((double) k1 - xIn - d0 + 0.5D, (double) k2 - yIn,
                                        (double) j1 - zIn - d1 + 0.5D).tex(0.0F + f7, (float) j2 * 0.25F + f6 + f8)
                                        .color(1.0F, 1.0F, 1.0F, f10).lightmap(k4, j4).endVertex();
                                bufferbuilder.pos((double) k1 - xIn + d0 + 0.5D, (double) k2 - yIn,
                                        (double) j1 - zIn + d1 + 0.5D).tex(1.0F + f7, (float) j2 * 0.25F + f6 + f8)
                                        .color(1.0F, 1.0F, 1.0F, f10).lightmap(k4, j4).endVertex();
                                bufferbuilder.pos((double) k1 - xIn + d0 + 0.5D, (double) j2 - yIn,
                                        (double) j1 - zIn + d1 + 0.5D).tex(1.0F + f7, (float) k2 * 0.25F + f6 + f8)
                                        .color(1.0F, 1.0F, 1.0F, f10).lightmap(k4, j4).endVertex();
                                bufferbuilder.pos((double) k1 - xIn - d0 + 0.5D, (double) j2 - yIn,
                                        (double) j1 - zIn - d1 + 0.5D).tex(0.0F + f7, (float) k2 * 0.25F + f6 + f8)
                                        .color(1.0F, 1.0F, 1.0F, f10).lightmap(k4, j4).endVertex();
                            }
                        }
                    }
                }
            }

            if (i1 >= 0) {
                tessellator.draw();
            }

            RenderSystem.enableCull();
            RenderSystem.disableBlend();
            RenderSystem.defaultAlphaFunc();
            RenderSystem.disableAlphaTest();
            lightmapIn.disableLightmap();
        }
    }

    public static int getCombinedLight(IBlockDisplayReader lightReaderIn, BlockPos blockPosIn) {
        return getPackedLightmapCoords(lightReaderIn, lightReaderIn.getBlockState(blockPosIn), blockPosIn);
    }

    public static int getPackedLightmapCoords(IBlockDisplayReader lightReaderIn, BlockState blockStateIn, BlockPos blockPosIn) {
        if (blockStateIn.isEmissiveRendering(lightReaderIn, blockPosIn)) {
            return 15728880;
        } else {
            int i = lightReaderIn.getLightFor(LightType.SKY, blockPosIn);
            int j = lightReaderIn.getLightFor(LightType.BLOCK, blockPosIn);
            int k = blockStateIn.getLightValue(lightReaderIn, blockPosIn);
            if (j < k) {
                j = k;
            }

            return i << 20 | j << 4;
        }
    }

    // TODO: proper sunset colors, proper sky with dynamic moons and suns, lighting for world, day length settings,
    // TODO: beds that work and advance time partially, meteors
    // TODO: acid rain, acid damage, (clouds?)
    // TODO: gravity

    @Override
    public float[] getSunsetColor(float sunAngle, float partialTicks) {
        float cosineOfSunAngle = MathHelper.cos(sunAngle * ((float) Math.PI * 2F));
        if (cosineOfSunAngle >= -0.4F && cosineOfSunAngle <= 0.4F) {
            float factor = cosineOfSunAngle / 0.4F * 0.5F + 0.5F;
            float alpha = 1.0F - (1.0F - MathHelper.sin(factor * (float) Math.PI)) * 0.99F;
            alpha = alpha * alpha;
            alpha = (float) this.fixedSunsetAlpha.orElse(alpha);
            this.sunsetColorSetter.accept(factor, this.sunsetColor);
            this.sunsetColor[3] = alpha; // 0 is for no sunset colors, otherwise "alpha"
            return this.sunsetColor;
        } else {
            return null;
        }
    }

    @Override
    public boolean renderNearFog() {
        return true;
    }

    @Override
    public Vector3d getFogColor(Vector3d color) {
        return color;
    }

}

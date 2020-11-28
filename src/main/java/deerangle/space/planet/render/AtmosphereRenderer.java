package deerangle.space.planet.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import deerangle.space.capability.Capabilities;
import deerangle.space.capability.IWeatherCapability;
import deerangle.space.planet.Planet;
import deerangle.space.planet.Weather;
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
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.common.util.LazyOptional;

import java.util.List;
import java.util.Objects;
import java.util.OptionalDouble;
import java.util.Random;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@SuppressWarnings("deprecation")
public class AtmosphereRenderer extends AbstractAtmosphereRenderer {

    //TODO: do thing about time setting (new command?)
    //TODO: do thing about weather (new command?)

    private static final ResourceLocation CLOUDS_TEXTURES = new ResourceLocation("textures/environment/clouds.png");

    private final VertexFormat skyVertexFormat = DefaultVertexFormats.POSITION;
    private final float[] sunsetColor = new float[4];
    private final BiConsumer<Float, float[]> sunsetColorSetter;
    private final OptionalDouble fixedSunsetAlpha;
    private final List<Planet> orbitingPlanets;
    private final BiFunction<Float, Float, Vector3d> cloudColor;
    private final float[] rainSizeX = new float[1024];
    private final float[] rainSizeZ = new float[1024];
    private VertexBuffer skyVBO;
    private VertexBuffer sky2VBO;
    private VertexBuffer starVBO;
    private VertexBuffer cloudsVBO;
    private CloudOption cloudOption;
    private boolean cloudsNeedUpdate;
    private Vector3d cloudsCheckColor = Vector3d.ZERO;
    private final float dayLength;

    public AtmosphereRenderer(Planet planet) {
        this.orbitingPlanets = planet.getSkyPlanets().stream().map(Supplier::get).collect(Collectors.toList());
        this.sunsetColorSetter = planet.getSunsetColors();
        this.fixedSunsetAlpha = planet.getSunsetAlpha();
        this.cloudColor = planet.getCloudColor();
        this.cloudHeight = planet.getCloudHeight();
        this.dayLength = planet.getDayLength();
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

    public static int getCombinedLight(IBlockDisplayReader lightReaderIn, BlockPos blockPosIn) {
        return getPackedLightMapCoordinates(lightReaderIn, lightReaderIn.getBlockState(blockPosIn), blockPosIn);
    }

    public static int getPackedLightMapCoordinates(IBlockDisplayReader lightReaderIn, BlockState blockStateIn, BlockPos blockPosIn) {
        if (blockStateIn.isEmissiveRendering(lightReaderIn, blockPosIn)) {
            return 0xf000f0;
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
                        bufferIn.pos((f18 + 0.0F), (f17 + 0.0F), (f19 + 8.0F)).tex((f18 + 0.0F) * 0.00390625F + f3, (f19 + 8.0F) * 0.00390625F + f4).color(f11, f12, f13, 0.8F).normal(0.0F, -1.0F, 0.0F).endVertex();
                        bufferIn.pos((f18 + 8.0F), (f17 + 0.0F), (f19 + 8.0F)).tex((f18 + 8.0F) * 0.00390625F + f3, (f19 + 8.0F) * 0.00390625F + f4).color(f11, f12, f13, 0.8F).normal(0.0F, -1.0F, 0.0F).endVertex();
                        bufferIn.pos((f18 + 8.0F), (f17 + 0.0F), (f19 + 0.0F)).tex((f18 + 8.0F) * 0.00390625F + f3, (f19 + 0.0F) * 0.00390625F + f4).color(f11, f12, f13, 0.8F).normal(0.0F, -1.0F, 0.0F).endVertex();
                        bufferIn.pos((f18 + 0.0F), (f17 + 0.0F), (f19 + 0.0F)).tex((f18 + 0.0F) * 0.00390625F + f3, (f19 + 0.0F) * 0.00390625F + f4).color(f11, f12, f13, 0.8F).normal(0.0F, -1.0F, 0.0F).endVertex();
                    }

                    if (f17 <= 5.0F) {
                        bufferIn.pos((f18 + 0.0F), (f17 + 4.0F - 9.765625E-4F), (f19 + 8.0F)).tex((f18 + 0.0F) * 0.00390625F + f3, (f19 + 8.0F) * 0.00390625F + f4).color(f5, f6, f7, 0.8F).normal(0.0F, 1.0F, 0.0F).endVertex();
                        bufferIn.pos((f18 + 8.0F), (f17 + 4.0F - 9.765625E-4F), (f19 + 8.0F)).tex((f18 + 8.0F) * 0.00390625F + f3, (f19 + 8.0F) * 0.00390625F + f4).color(f5, f6, f7, 0.8F).normal(0.0F, 1.0F, 0.0F).endVertex();
                        bufferIn.pos((f18 + 8.0F), (f17 + 4.0F - 9.765625E-4F), (f19 + 0.0F)).tex((f18 + 8.0F) * 0.00390625F + f3, (f19 + 0.0F) * 0.00390625F + f4).color(f5, f6, f7, 0.8F).normal(0.0F, 1.0F, 0.0F).endVertex();
                        bufferIn.pos((f18 + 0.0F), (f17 + 4.0F - 9.765625E-4F), (f19 + 0.0F)).tex((f18 + 0.0F) * 0.00390625F + f3, (f19 + 0.0F) * 0.00390625F + f4).color(f5, f6, f7, 0.8F).normal(0.0F, 1.0F, 0.0F).endVertex();
                    }

                    if (k > -1) {
                        for (int i1 = 0; i1 < 8; ++i1) {
                            bufferIn.pos((f18 + (float) i1 + 0.0F), (f17 + 0.0F), (f19 + 8.0F)).tex((f18 + (float) i1 + 0.5F) * 0.00390625F + f3, (f19 + 8.0F) * 0.00390625F + f4).color(f8, f9, f10, 0.8F).normal(-1.0F, 0.0F, 0.0F).endVertex();
                            bufferIn.pos((f18 + (float) i1 + 0.0F), (f17 + 4.0F), (f19 + 8.0F)).tex((f18 + (float) i1 + 0.5F) * 0.00390625F + f3, (f19 + 8.0F) * 0.00390625F + f4).color(f8, f9, f10, 0.8F).normal(-1.0F, 0.0F, 0.0F).endVertex();
                            bufferIn.pos((f18 + (float) i1 + 0.0F), (f17 + 4.0F), (f19 + 0.0F)).tex((f18 + (float) i1 + 0.5F) * 0.00390625F + f3, (f19 + 0.0F) * 0.00390625F + f4).color(f8, f9, f10, 0.8F).normal(-1.0F, 0.0F, 0.0F).endVertex();
                            bufferIn.pos((f18 + (float) i1 + 0.0F), (f17 + 0.0F), (f19 + 0.0F)).tex((f18 + (float) i1 + 0.5F) * 0.00390625F + f3, (f19 + 0.0F) * 0.00390625F + f4).color(f8, f9, f10, 0.8F).normal(-1.0F, 0.0F, 0.0F).endVertex();
                        }
                    }

                    if (k <= 1) {
                        for (int j2 = 0; j2 < 8; ++j2) {
                            bufferIn.pos((f18 + (float) j2 + 1.0F - 9.765625E-4F), (f17 + 0.0F), (f19 + 8.0F)).tex((f18 + (float) j2 + 0.5F) * 0.00390625F + f3, (f19 + 8.0F) * 0.00390625F + f4).color(f8, f9, f10, 0.8F).normal(1.0F, 0.0F, 0.0F).endVertex();
                            bufferIn.pos((f18 + (float) j2 + 1.0F - 9.765625E-4F), (f17 + 4.0F), (f19 + 8.0F)).tex((f18 + (float) j2 + 0.5F) * 0.00390625F + f3, (f19 + 8.0F) * 0.00390625F + f4).color(f8, f9, f10, 0.8F).normal(1.0F, 0.0F, 0.0F).endVertex();
                            bufferIn.pos((f18 + (float) j2 + 1.0F - 9.765625E-4F), (f17 + 4.0F), (f19 + 0.0F)).tex((f18 + (float) j2 + 0.5F) * 0.00390625F + f3, (f19 + 0.0F) * 0.00390625F + f4).color(f8, f9, f10, 0.8F).normal(1.0F, 0.0F, 0.0F).endVertex();
                            bufferIn.pos((f18 + (float) j2 + 1.0F - 9.765625E-4F), (f17 + 0.0F), (f19 + 0.0F)).tex((f18 + (float) j2 + 0.5F) * 0.00390625F + f3, (f19 + 0.0F) * 0.00390625F + f4).color(f8, f9, f10, 0.8F).normal(1.0F, 0.0F, 0.0F).endVertex();
                        }
                    }

                    if (l > -1) {
                        for (int k2 = 0; k2 < 8; ++k2) {
                            bufferIn.pos((f18 + 0.0F), (f17 + 4.0F), (f19 + (float) k2 + 0.0F)).tex((f18 + 0.0F) * 0.00390625F + f3, (f19 + (float) k2 + 0.5F) * 0.00390625F + f4).color(f14, f15, f16, 0.8F).normal(0.0F, 0.0F, -1.0F).endVertex();
                            bufferIn.pos((f18 + 8.0F), (f17 + 4.0F), (f19 + (float) k2 + 0.0F)).tex((f18 + 8.0F) * 0.00390625F + f3, (f19 + (float) k2 + 0.5F) * 0.00390625F + f4).color(f14, f15, f16, 0.8F).normal(0.0F, 0.0F, -1.0F).endVertex();
                            bufferIn.pos((f18 + 8.0F), (f17 + 0.0F), (f19 + (float) k2 + 0.0F)).tex((f18 + 8.0F) * 0.00390625F + f3, (f19 + (float) k2 + 0.5F) * 0.00390625F + f4).color(f14, f15, f16, 0.8F).normal(0.0F, 0.0F, -1.0F).endVertex();
                            bufferIn.pos((f18 + 0.0F), (f17 + 0.0F), (f19 + (float) k2 + 0.0F)).tex((f18 + 0.0F) * 0.00390625F + f3, (f19 + (float) k2 + 0.5F) * 0.00390625F + f4).color(f14, f15, f16, 0.8F).normal(0.0F, 0.0F, -1.0F).endVertex();
                        }
                    }

                    if (l <= 1) {
                        for (int l2 = 0; l2 < 8; ++l2) {
                            bufferIn.pos((f18 + 0.0F), (f17 + 4.0F), (f19 + (float) l2 + 1.0F - 9.765625E-4F)).tex((f18 + 0.0F) * 0.00390625F + f3, (f19 + (float) l2 + 0.5F) * 0.00390625F + f4).color(f14, f15, f16, 0.8F).normal(0.0F, 0.0F, 1.0F).endVertex();
                            bufferIn.pos((f18 + 8.0F), (f17 + 4.0F), (f19 + (float) l2 + 1.0F - 9.765625E-4F)).tex((f18 + 8.0F) * 0.00390625F + f3, (f19 + (float) l2 + 0.5F) * 0.00390625F + f4).color(f14, f15, f16, 0.8F).normal(0.0F, 0.0F, 1.0F).endVertex();
                            bufferIn.pos((f18 + 8.0F), (f17 + 0.0F), (f19 + (float) l2 + 1.0F - 9.765625E-4F)).tex((f18 + 8.0F) * 0.00390625F + f3, (f19 + (float) l2 + 0.5F) * 0.00390625F + f4).color(f14, f15, f16, 0.8F).normal(0.0F, 0.0F, 1.0F).endVertex();
                            bufferIn.pos((f18 + 0.0F), (f17 + 0.0F), (f19 + (float) l2 + 1.0F - 9.765625E-4F)).tex((f18 + 0.0F) * 0.00390625F + f3, (f19 + (float) l2 + 0.5F) * 0.00390625F + f4).color(f14, f15, f16, 0.8F).normal(0.0F, 0.0F, 1.0F).endVertex();
                        }
                    }
                }
            }
        } else {
            for (int l1 = -32; l1 < 32; l1 += 32) {
                for (int i2 = -32; i2 < 32; i2 += 32) {
                    bufferIn.pos(l1, f17, (i2 + 32)).tex((float) (l1) * 0.00390625F + f3, (float) (i2 + 32) * 0.00390625F + f4).color(f5, f6, f7, 0.8F).normal(0.0F, -1.0F, 0.0F).endVertex();
                    bufferIn.pos(l1 + 32, f17, (i2 + 32)).tex((float) (l1 + 32) * 0.00390625F + f3, (float) (i2 + 32) * 0.00390625F + f4).color(f5, f6, f7, 0.8F).normal(0.0F, -1.0F, 0.0F).endVertex();
                    bufferIn.pos(l1 + 32, f17, (i2)).tex((float) (l1 + 32) * 0.00390625F + f3, (float) (i2) * 0.00390625F + f4).color(f5, f6, f7, 0.8F).normal(0.0F, -1.0F, 0.0F).endVertex();
                    bufferIn.pos(l1, f17, (i2)).tex((float) (l1) * 0.00390625F + f3, (float) (i2) * 0.00390625F + f4).color(f5, f6, f7, 0.8F).normal(0.0F, -1.0F, 0.0F).endVertex();
                }
            }
        }

    }

    private float getCelestialAngleByTime(World world) {
        double d0 = MathHelper.frac((double) world.func_241851_ab() / ((double) this.dayLength) - 0.25D);
        double d1 = 0.5D - Math.cos(d0 * Math.PI) / 2.0D;
        return (float) (d0 * 2.0D + d1) / 3.0F;
    }

    private float getStarBrightness(World world) {
        float f = getCelestialAngleByTime(world);
        float f1 = 1.0F - (MathHelper.cos(f * ((float) Math.PI * 2F)) * 2.0F + 0.25F);
        f1 = MathHelper.clamp(f1, 0.0F, 1.0F);
        return f1 * f1 * 0.5F;
    }

    private float getCelestialAngleRadians(World world) {
        float f = this.getCelestialAngleByTime(world);
        return f * ((float) Math.PI * 2F);
    }

    private Vector3d getSkyColor(World world, BlockPos blockPosIn) {
        float f1 = MathHelper.cos(getCelestialAngleByTime(world) * ((float) Math.PI * 2F)) * 2.0F + 0.5F;
        f1 = MathHelper.clamp(f1, 0.0F, 1.0F);
        Biome biome = world.getBiome(blockPosIn);
        int i = biome.getSkyColor();
        float r = (float) (i >> 16 & 255) / 255.0F;
        float g = (float) (i >> 8 & 255) / 255.0F;
        float b = (float) (i & 255) / 255.0F;
        r = r * f1;
        g = g * f1;
        b = b * f1;
        float f5 = getWeatherStrength(world);
        if (f5 > 0.0F) {
            float f6 = (r * 0.3F + g * 0.59F + b * 0.11F) * 0.6F;
            float mixWithOther = 1.0F - f5;
            r = r * mixWithOther + f6 * (1.0F - mixWithOther);
            g = g * mixWithOther + f6 * (1.0F - mixWithOther);
            b = b * mixWithOther + f6 * (1.0F - mixWithOther);
        }

        return new Vector3d(r, g, b);
    }

    private float getWeatherStrength(World world) {
        Weather weather = getWeather(world);
        if (weather == null) {
            return 0;
        }
        return weather.getStrength(world);
    }

    private Weather getWeather(World world) {
        LazyOptional<IWeatherCapability> weatherCapability = world.getCapability(Capabilities.WEATHER_CAPABILITY);
        Weather thisWeather = null;
        if (weatherCapability.isPresent()) {
            thisWeather = weatherCapability.orElseThrow(() -> new RuntimeException("failed to get weather cap")).getCurrentWeather();
        }
        return thisWeather;
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
            RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
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
            Vector3d vector3d = this.cloudColor.apply(partialTicks, getCelestialAngleByTime(world));
            int i = (int) Math.floor(d2);
            int j = (int) Math.floor(d3 / 4.0D);
            int k = (int) Math.floor(d4);
            if (i != cloudsCheckX || j != cloudsCheckY || k != cloudsCheckZ || mc.gameSettings.getCloudOption() != this.cloudOption || this.cloudsCheckColor.squareDistanceTo(vector3d) > 2.0E-4D) {
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
        Vector3d skyColor = getSkyColor(world, mc.gameRenderer.getActiveRenderInfo().getBlockPos());
        float red = (float) skyColor.x;
        float green = (float) skyColor.y;
        float blue = (float) skyColor.z;
        FogRenderer.applyFog();
        BufferBuilder bufferbuilder = Tessellator.getInstance().getBuffer();
        RenderSystem.depthMask(false);
        RenderSystem.enableFog();
        RenderSystem.color3f(red, green, blue);
        this.skyVBO.bindBuffer();
        this.skyVertexFormat.setupBufferState(0L);
        this.skyVBO.draw(matrixStackIn.getLast().getMatrix(), 7);
        VertexBuffer.unbindBuffer();
        this.skyVertexFormat.clearBufferState();
        RenderSystem.disableFog();
        RenderSystem.disableAlphaTest();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        float[] sunset = this.getSunsetColor(getCelestialAngleByTime(world), partialTicks);
        if (sunset != null) {
            RenderSystem.disableTexture();
            RenderSystem.shadeModel(7425);
            matrixStackIn.push();
            matrixStackIn.rotate(Vector3f.XP.rotationDegrees(90.0F));
            float f3 = MathHelper.sin(getCelestialAngleRadians(world)) < 0.0F ? 180.0F : 0.0F;
            matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(f3));
            matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(90.0F));
            float sunsetRed = sunset[0];
            float sunsetGreen = sunset[1];
            float sunsetBlue = sunset[2];
            Matrix4f matrix4f = matrixStackIn.getLast().getMatrix();
            bufferbuilder.begin(6, DefaultVertexFormats.POSITION_COLOR);
            bufferbuilder.pos(matrix4f, 0.0F, 100.0F, 0.0F).color(sunsetRed, sunsetGreen, sunsetBlue, sunset[3]).endVertex();

            for (int j = 0; j <= 16; ++j) {
                float f7 = (float) j * ((float) Math.PI * 2F) / 16.0F;
                float f8 = MathHelper.sin(f7);
                float f9 = MathHelper.cos(f7);
                bufferbuilder.pos(matrix4f, f8 * 120.0F, f9 * 120.0F, -f9 * 40.0F * sunset[3]).color(sunset[0], sunset[1], sunset[2], 0.0F).endVertex();
            }

            bufferbuilder.finishDrawing();
            WorldVertexBufferUploader.draw(bufferbuilder);
            matrixStackIn.pop();
            RenderSystem.shadeModel(7424);
        }

        RenderSystem.enableTexture();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        matrixStackIn.push();
        float oneMinusRain = (1.0F - getWeatherStrength(world)) * 0.95f + 0.05f;

        RenderSystem.color4f(1.0F, 1.0F, 1.0F, oneMinusRain);
        for (Planet p : this.orbitingPlanets) {
            matrixStackIn.rotate(Vector3f.YP.rotationDegrees(p.getOrbitDirection()));
            matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(p.getOrbitTilt()));
            matrixStackIn.rotate(Vector3f.XP.rotationDegrees(getCelestialAngleByTime(world) * 360.0F));
            Matrix4f matrix4f1 = matrixStackIn.getLast().getMatrix();
            float planetSize = p.getSizeInSky();
            mc.textureManager.bindTexture(p.getSkyTexture());
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
            bufferbuilder.pos(matrix4f1, -planetSize, 100.0F, -planetSize).tex(0.0F, 0.0F).endVertex();
            bufferbuilder.pos(matrix4f1, planetSize, 100.0F, -planetSize).tex(1.0F, 0.0F).endVertex();
            bufferbuilder.pos(matrix4f1, planetSize, 100.0F, planetSize).tex(1.0F, 1.0F).endVertex();
            bufferbuilder.pos(matrix4f1, -planetSize, 100.0F, planetSize).tex(0.0F, 1.0F).endVertex();
            bufferbuilder.finishDrawing();
            WorldVertexBufferUploader.draw(bufferbuilder);
        }

        RenderSystem.disableTexture();
        float f10 = getStarBrightness(world) * oneMinusRain;
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
        double viewerEyeHeight = Objects.requireNonNull(mc.player).getEyePosition(partialTicks).y - world.getWorldInfo().getVoidFogHeight();
        if (viewerEyeHeight < 0.0D) {
            matrixStackIn.push();
            matrixStackIn.translate(0.0D, 12.0D, 0.0D);
            this.sky2VBO.bindBuffer();
            this.skyVertexFormat.setupBufferState(0L);
            this.sky2VBO.draw(matrixStackIn.getLast().getMatrix(), 7);
            VertexBuffer.unbindBuffer();
            this.skyVertexFormat.clearBufferState();
            matrixStackIn.pop();
        }

        RenderSystem.color3f(red * 0.2F + 0.04F, green * 0.2F + 0.04F, blue * 0.6F + 0.1F);

        RenderSystem.enableTexture();
        RenderSystem.depthMask(true);
        RenderSystem.disableFog();
    }

    @Override
    protected void renderWeather(int ticks, float partialTicks, ClientWorld world, Minecraft mc, LightTexture lightMapIn, double xIn, double yIn, double zIn) {
        float f = getWeatherStrength(world);
        if (!(f <= 0.0F)) {
            lightMapIn.enableLightmap();
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
            int rainGridSize = 5;
            if (Minecraft.isFancyGraphicsEnabled()) {
                rainGridSize = 10;
            }

            RenderSystem.depthMask(Minecraft.isFabulousGraphicsEnabled());
            int i1 = -1;
            float f1 = (float) ticks + partialTicks;
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            BlockPos.Mutable blockPos = new BlockPos.Mutable();

            for (int z = k - rainGridSize; z <= k + rainGridSize; ++z) {
                for (int x = i - rainGridSize; x <= i + rainGridSize; ++x) {
                    int rainHeightIndex = (z - k + 16) * 32 + x - i + 16;
                    double d0 = (double) this.rainSizeX[rainHeightIndex] * 0.5D;
                    double d1 = (double) this.rainSizeZ[rainHeightIndex] * 0.5D;
                    blockPos.setPos(x, 0, z);
                    int i2 = world.getHeight(Heightmap.Type.MOTION_BLOCKING, blockPos).getY();
                    int y = j - rainGridSize;
                    int yEnd = j + rainGridSize;
                    if (y < i2) {
                        y = i2;
                    }

                    if (yEnd < i2) {
                        yEnd = i2;
                    }

                    int l2 = i2;
                    if (i2 < j) {
                        l2 = j;
                    }

                    if (y != yEnd) {
                        Random random = new Random(x * x * 3121 + x * 45238971 ^ z * z * 418711 + z * 13761);
                        blockPos.setPos(x, y, z);
                        if (i1 != 0) {
                            i1 = 0;
                            mc.getTextureManager().bindTexture(getWeather(world).getWeatherTexture());
                            bufferbuilder.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
                        }

                        int i3 = ticks + x * x * 3121 + x * 45238971 + z * z * 418711 + z * 13761 & 31;
                        float f3 = -((float) i3 + partialTicks) / 32.0F * (3.0F + random.nextFloat());
                        double d2 = (double) ((float) x + 0.5F) - xIn;
                        double d4 = (double) ((float) z + 0.5F) - zIn;
                        float f4 = MathHelper.sqrt(d2 * d2 + d4 * d4) / (float) rainGridSize;
                        float f5 = ((1.0F - f4 * f4) * 0.5F + 0.5F) * f;
                        blockPos.setPos(x, l2, z);
                        int j3 = getCombinedLight(world, blockPos);
                        bufferbuilder.pos((double) x - xIn - d0 + 0.5D, (double) yEnd - yIn, (double) z - zIn - d1 + 0.5D).tex(0.0F, (float) y * 0.25F + f3).color(1.0F, 1.0F, 1.0F, f5).lightmap(j3).endVertex();
                        bufferbuilder.pos((double) x - xIn + d0 + 0.5D, (double) yEnd - yIn, (double) z - zIn + d1 + 0.5D).tex(1.0F, (float) y * 0.25F + f3).color(1.0F, 1.0F, 1.0F, f5).lightmap(j3).endVertex();
                        bufferbuilder.pos((double) x - xIn + d0 + 0.5D, (double) y - yIn, (double) z - zIn + d1 + 0.5D).tex(1.0F, (float) yEnd * 0.25F + f3).color(1.0F, 1.0F, 1.0F, f5).lightmap(j3).endVertex();
                        bufferbuilder.pos((double) x - xIn - d0 + 0.5D, (double) y - yIn, (double) z - zIn - d1 + 0.5D).tex(0.0F, (float) yEnd * 0.25F + f3).color(1.0F, 1.0F, 1.0F, f5).lightmap(j3).endVertex();
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
            lightMapIn.disableLightmap();
        }
    }

    // TODO: proper sunset colors, proper sky with dynamic moons and suns, lighting for world, day length settings,
    // TODO: beds that work and advance time partially, meteors
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

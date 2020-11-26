package deerangle.space.planet.planets.venus.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import deerangle.space.planet.Planet;
import deerangle.space.planet.render.AtmosphereRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;

import java.util.List;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class VenusAtmosphereRenderer extends AtmosphereRenderer {

    private final float[] sunsetColor = new float[4];

    private final VertexFormat skyVertexFormat = DefaultVertexFormats.POSITION;
    private VertexBuffer skyVBO;
    private VertexBuffer sky2VBO;
    private VertexBuffer starVBO;

    private final float dayLength;
    private final List<Planet> orbitingPlanets;

    public VenusAtmosphereRenderer(Planet planet) {
        this.dayLength = planet.getDayLength();
        this.orbitingPlanets = planet.getSkyPlanets().stream().map(Supplier::get).collect(Collectors.toList());
        generateSky();
        generateSky2();
        generateStars();
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
            double d0 = (double) (random.nextFloat() * 2.0F - 1.0F);
            double d1 = (double) (random.nextFloat() * 2.0F - 1.0F);
            double d2 = (double) (random.nextFloat() * 2.0F - 1.0F);
            double d3 = (double) (0.15F + random.nextFloat() * 0.1F);
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
                    double d17 = 0.0D;
                    double d18 = (double) ((j & 2) - 1) * d3;
                    double d19 = (double) ((j + 1 & 2) - 1) * d3;
                    double d20 = 0.0D;
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

    @Override
    protected void renderClouds(int ticks, float partialTicks, MatrixStack matrixStack, ClientWorld world, Minecraft mc, double viewEntityX, double viewEntityY, double viewEntityZ) {
        return;
    }

    @Override
    protected void renderSky(int ticks, float partialTicks, MatrixStack matrixStackIn, ClientWorld world, Minecraft mc) {
        float daySpeedFactor = 24000f / this.dayLength;

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
            int i = 16;

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
        return;
    }

    // TODO: proper sunset colors, proper sky with dynamic moons and suns, lighting for world, day length settings,
    // TODO: beds that work and advance time partially, meteors
    // TODO: acid rain, acid damage, (clouds?)
    // TODO: gravity

    @Override
    public float[] getSunsetColor(float sunAngle, float partialTicks) {
        float cosineOfSunAngle = MathHelper.cos(sunAngle * ((float) Math.PI * 2F));
        if (cosineOfSunAngle >= -0.4F && cosineOfSunAngle <= 0.4F) {
            float f3 = cosineOfSunAngle / 0.4F * 0.5F + 0.5F;
            // float alpha = 1.0F - (1.0F - MathHelper.sin(f3 * (float) Math.PI)) * 0.99F;
            // alpha = alpha * alpha;
            this.sunsetColor[0] = 1;//f3 * 0.3F + 0.7F;
            this.sunsetColor[1] = 0;//f3 * f3 * 0.7F + 0.2F;
            this.sunsetColor[2] = 1;//f3 * f3 * 0.0F + 0.2F;
            this.sunsetColor[3] = 0; // 0 is for no sunset colors, otherwise "alpha"
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

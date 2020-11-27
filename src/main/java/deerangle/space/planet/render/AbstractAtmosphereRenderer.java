package deerangle.space.planet.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.client.ICloudRenderHandler;
import net.minecraftforge.client.ISkyRenderHandler;
import net.minecraftforge.client.IWeatherRenderHandler;

public abstract class AbstractAtmosphereRenderer implements ICloudRenderHandler, ISkyRenderHandler, IWeatherRenderHandler {

    public float cloudHeight = Float.NaN;

    @Override
    public void render(int ticks, float partialTicks, MatrixStack matrixStack, ClientWorld world, Minecraft mc, double viewEntityX, double viewEntityY, double viewEntityZ) {
        this.renderClouds(ticks, partialTicks, matrixStack, world, mc, viewEntityX, viewEntityY, viewEntityZ);
    }

    @Override
    public void render(int ticks, float partialTicks, MatrixStack matrixStack, ClientWorld world, Minecraft mc) {
        this.renderSky(ticks, partialTicks, matrixStack, world, mc);
    }

    @Override
    public void render(int ticks, float partialTicks, ClientWorld world, Minecraft mc, LightTexture lightmapIn, double xIn, double yIn, double zIn) {
        this.renderWeather(ticks, partialTicks, world, mc, lightmapIn, xIn, yIn, zIn);
    }

    protected abstract void renderClouds(int ticks, float partialTicks, MatrixStack matrixStack, ClientWorld world, Minecraft mc, double viewEntityX, double viewEntityY, double viewEntityZ);

    protected abstract void renderSky(int ticks, float partialTicks, MatrixStack matrixStack, ClientWorld world, Minecraft mc);

    protected abstract void renderWeather(int ticks, float partialTicks, ClientWorld world, Minecraft mc, LightTexture lightmapIn, double xIn, double yIn, double zIn);

    public abstract float[] getSunsetColor(float sunAngle, float partialTicks);

    public abstract boolean renderNearFog();

    public abstract Vector3d getFogColor(Vector3d color);

}

package deerangle.space.planets.venus.render;

import deerangle.space.planets.render.AtmosphereRenderer;
import net.minecraft.util.math.vector.Vector3d;

public class CustomDimensionRenderInfo extends net.minecraft.client.world.DimensionRenderInfo {

    private final AtmosphereRenderer atmosphereRenderer;

    public CustomDimensionRenderInfo(AtmosphereRenderer atmosphereRenderer) {
        super(atmosphereRenderer.cloudHeight, true, FogType.NORMAL, false, false);
        this.atmosphereRenderer = atmosphereRenderer;
        this.setCloudRenderHandler(this.atmosphereRenderer);
        this.setSkyRenderHandler(this.atmosphereRenderer);
        this.setWeatherRenderHandler(this.atmosphereRenderer);
    }

    // getSunsetColor
    @Override
    public float[] func_230492_a_(float sunAngle, float partialTicks) {
        return this.atmosphereRenderer.getSunsetColor(sunAngle, partialTicks);
    }

    // getFogColor
    @Override
    public Vector3d func_230494_a_(Vector3d colorIn, float p_230494_2_) {
        return this.atmosphereRenderer.getFogColor(colorIn);
    }

    // nearFog
    @Override
    public boolean func_230493_a_(int viewX, int viewY) {
        return this.atmosphereRenderer.renderNearFog();
    }

}

package deerangle.space.planet.render;

import net.minecraft.client.world.DimensionRenderInfo;
import net.minecraft.util.math.vector.Vector3d;

public class CustomDimensionRenderInfo extends DimensionRenderInfo {

    private final AbstractAtmosphereRenderer atmosphereRenderer;

    public CustomDimensionRenderInfo(AbstractAtmosphereRenderer atmosphereRenderer) {
        super(atmosphereRenderer.cloudHeight, true, FogType.NORMAL, false, false);
        this.atmosphereRenderer = atmosphereRenderer;
        this.setCloudRenderHandler(this.atmosphereRenderer);
        this.setSkyRenderHandler(this.atmosphereRenderer);
        this.setWeatherRenderHandler(this.atmosphereRenderer);
    }

    //TODO: FogRenderer#updateFogColor

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

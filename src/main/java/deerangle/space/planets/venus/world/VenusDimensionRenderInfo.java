package deerangle.space.planets.venus.world;

import net.minecraft.util.math.vector.Vector3d;

public class VenusDimensionRenderInfo extends net.minecraft.client.world.DimensionRenderInfo {

    public VenusDimensionRenderInfo(float cloudHeight, boolean changeSkyColor, FogType fogType, boolean doLightmap, boolean flatLighting) {
        super(cloudHeight, changeSkyColor, fogType, doLightmap, flatLighting);
    }

    @Override
    public Vector3d func_230494_a_(Vector3d colorIn, float p_230494_2_) {
        return colorIn;
    }

    @Override
    public boolean func_230493_a_(int p_230493_1_, int p_230493_2_) {
        return true;
    }

}

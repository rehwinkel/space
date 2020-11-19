package deerangle.space.util;

import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;

import java.util.Arrays;

public class VoxelShapeUtil {

    public static VoxelShape[] horizontalShape(VoxelShape... shapes) {
        return horizontalShape(0, shapes);
    }

    public static VoxelShape[] horizontalShape(double yOffset, VoxelShape... shapes) {
        VoxelShape north = Arrays.stream(shapes)
                .map(shape -> VoxelShapes.create(offsetY(shape.getBoundingBox(), yOffset)))
                .reduce(VoxelShapes.empty(), VoxelShapes::or);
        VoxelShape east = Arrays.stream(shapes)
                .map(shape -> VoxelShapes.create(reflectAlongX(swapXZ(offsetY(shape.getBoundingBox(), yOffset)))))
                .reduce(VoxelShapes.empty(), VoxelShapes::or);
        VoxelShape south = Arrays.stream(shapes).map(shape -> VoxelShapes
                .create(reflectAlongX(reflectAlongZ(offsetY(shape.getBoundingBox(), yOffset)))))
                .reduce(VoxelShapes.empty(), VoxelShapes::or);
        VoxelShape west = Arrays.stream(shapes)
                .map(shape -> VoxelShapes.create(reflectAlongZ(swapXZ(offsetY(shape.getBoundingBox(), yOffset)))))
                .reduce(VoxelShapes.empty(), VoxelShapes::or);
        return new VoxelShape[]{south, west, north, east};
    }

    private static AxisAlignedBB offsetY(AxisAlignedBB box, double yOffset) {
        return new AxisAlignedBB(box.minX, box.minY + yOffset, box.minZ, box.maxX, box.maxY + yOffset, box.maxZ);
    }

    private static AxisAlignedBB reflectAlongX(AxisAlignedBB box) {
        return new AxisAlignedBB(1 - box.minX, box.minY, box.minZ, 1 - box.maxX, box.maxY, box.maxZ);
    }

    private static AxisAlignedBB reflectAlongY(AxisAlignedBB box) {
        return new AxisAlignedBB(box.minX, 1 - box.minY, box.minZ, box.maxX, 1 - box.maxY, box.maxZ);
    }

    private static AxisAlignedBB reflectAlongZ(AxisAlignedBB box) {
        return new AxisAlignedBB(box.minX, box.minY, 1 - box.minZ, box.maxX, box.maxY, 1 - box.maxZ);
    }

    private static AxisAlignedBB swapXZ(AxisAlignedBB box) {
        return new AxisAlignedBB(box.minZ, box.minY, box.minX, box.maxZ, box.maxY, box.maxX);
    }

}
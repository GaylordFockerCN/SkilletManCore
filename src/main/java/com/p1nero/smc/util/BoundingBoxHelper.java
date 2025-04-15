package com.p1nero.smc.util;

import net.minecraft.world.phys.AABB;

public class BoundingBoxHelper {
    public static AABB scaleAABB(AABB boundingBox, double scale){
        boundingBox.setMaxX(boundingBox.getXsize() * scale + boundingBox.minX);
        boundingBox.setMaxZ(boundingBox.getZsize() * scale + boundingBox.minZ);
        boundingBox.setMaxY(boundingBox.getYsize() * scale + boundingBox.minY);
        return boundingBox;
    }
}

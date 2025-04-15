package com.p1nero.smc.gameasset;

import yesman.epicfight.api.collider.Collider;
import yesman.epicfight.api.collider.MultiOBBCollider;
import yesman.epicfight.api.collider.OBBCollider;

public class SMCColliders {
    public static final Collider LETHAL_SLICING = new OBBCollider(2.0D, 0.25D, 1.5D, 0D, 1.0D, -1.0D);
    public static final Collider LETHAL_SLICING1 = new OBBCollider(2.0D, 0.25D, 1.5D, 0D, 0.5D, -1.0D);
    public static final Collider FATAL_DRAW_DASH = new OBBCollider(0.7, 0.7, 4.0, 0.0, 1.0, -4.0);
    public static final Collider BLADE_RUSH_FINISHER = new OBBCollider(1.2D, 0.8D, 2.0D, 0D, 1.0D, -1.2D);
    public static final Collider YAMATO_P = new MultiOBBCollider(3, 0.4D, 0.4D, 1.5D, 0.0D, 0.0D, -0.5D);

    public SMCColliders() {
    }
}
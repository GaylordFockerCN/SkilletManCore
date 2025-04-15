package com.p1nero.smc.gameasset;

import yesman.epicfight.api.collider.Collider;
import yesman.epicfight.api.collider.MultiOBBCollider;
import yesman.epicfight.api.collider.OBBCollider;

public class SMCColliders {
    public static final Collider SKILLET = new MultiOBBCollider(3, 0.4, 0.4, 0.7, 0.0, 0.0, -0.85);
    public static final Collider SPATULA = new MultiOBBCollider(3, 0.4, 0.4, 0.7, 0.0, 0.0, -0.35);

    public SMCColliders() {
    }
}
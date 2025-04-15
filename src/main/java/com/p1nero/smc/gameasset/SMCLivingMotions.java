package com.p1nero.smc.gameasset;

import yesman.epicfight.api.animation.LivingMotion;

public enum SMCLivingMotions implements LivingMotion {
    TALKING;

    final int id;

    private SMCLivingMotions() {
        this.id = LivingMotion.ENUM_MANAGER.assign(this);
    }

    public int universalOrdinal() {
        return this.id;
    }
}

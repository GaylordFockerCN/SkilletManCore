package com.p1nero.smc.entity.ai.epicfight.api;

import org.jetbrains.annotations.Nullable;
import yesman.epicfight.world.damagesource.StunType;

/**
 * 用于修改硬直类型
 */
public interface IModifyStunTypeEntityPatch {
    void setStunType(@Nullable StunType stunType);
    @Nullable
    StunType getStunType();
}
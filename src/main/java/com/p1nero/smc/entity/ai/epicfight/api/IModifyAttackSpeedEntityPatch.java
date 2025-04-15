package com.p1nero.smc.entity.ai.epicfight.api;

/**
 * 用于修改攻速
 */
public interface IModifyAttackSpeedEntityPatch {
    float getAttackSpeed();
    default void setAttackSpeed(float speed){};
}
package com.p1nero.smc.entity.ai.epicfight.api;

/**
 * 用于修改伤害系数
 */
public interface IModifyAttackDamageEntityPatch {
    void setNewDamage(float damage);
    float getNewDamage();
}
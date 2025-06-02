package com.p1nero.smc.entity.api;

import com.p1nero.smc.SkilletManCoreMod;
import com.p1nero.smc.SMCConfig;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.List;
import java.util.UUID;

/**
 * 多人时血量增加
 */
public interface MultiPlayerBoostEntity {

    int getLastTickPlayerCount();
    void setLastTickPlayerCount(int newCount);
    default void tickCheck(ServerLevel serverLevel) {
        int newCount = serverLevel.players().size();
        if(newCount != getLastTickPlayerCount()) {
            LivingEntity entity = ((LivingEntity) this);
            float originalHealth = entity.getHealth();
            float maxHealthOrdinal = entity.getMaxHealth();
            float finalRate = whenPlayerCountChange(newCount);
            float currentMax = entity.getMaxHealth();
            entity.setHealth(originalHealth * currentMax / maxHealthOrdinal);
            if(finalRate > 1){
                SkilletManCoreMod.LOGGER.info("[Player Count Modify]" + entity.getType().getDescriptionId() + "'s max health has changed from [" + maxHealthOrdinal + "] to : [" + currentMax + "]");
                SkilletManCoreMod.LOGGER.info("[Player Count Modify]" + entity.getType().getDescriptionId() + "'s health has changed from [" + originalHealth + "] to : [" + entity.getHealth() + "]");
                entity.level().players().forEach(player -> player.displayClientMessage(SkilletManCoreMod.getInfo("multy_player_health_boost_to", finalRate), true));
            }
            setLastTickPlayerCount(newCount);
        }
    }

    default float whenPlayerCountChange(int playerCount) {
        LivingEntity entity = ((LivingEntity) this);
        int max = SMCConfig.BOSS_HEALTH_AND_LOOT_MULTIPLE_MAX.get();
        int target = Math.min(playerCount, max);
        float rate;
        float finalRate = 0;
        for (int i = 1; i <= max; i++) {
            rate = (i - 1) * 0.6f;
            AttributeModifier modifier = new AttributeModifier(UUID.fromString("d2d110cc-f22f-11ed-a05b-1212bb11451" + i), "health_modify_in_multi_player", rate, AttributeModifier.Operation.MULTIPLY_TOTAL);
            AttributeInstance instance = entity.getAttribute(Attributes.MAX_HEALTH);
            if (instance != null) {
                if (i == target) {
                    if(!instance.hasModifier(modifier)) {
                        instance.addPermanentModifier(modifier);
                        finalRate =  1 + rate;
                    }
                } else {
                    instance.removeModifier(modifier);
                }
            }
        }
        return finalRate;
    }
}

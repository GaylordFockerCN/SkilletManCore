package com.p1nero.smc.entity.api;

import com.p1nero.smc.SkilletManCoreMod;
import com.p1nero.smc.DOTEConfig;
import com.p1nero.smc.util.EntityUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.UUID;

/**
 * 多人时血量增加
 */
public interface MultiPlayerBoostEntity {
    default void whenPlayerCountChange(){
        LivingEntity entity = ((LivingEntity) this);
        float ordinal = entity.getHealth();
        if(entity.level() instanceof ServerLevel serverLevel){
            float healthOrdinal = entity.getHealth();
            float maxHealthOrdinal = entity.getMaxHealth();
            int playerCount = EntityUtil.getPlayerCount(serverLevel);
            for(int i = 1; i <= DOTEConfig.BOSS_HEALTH_AND_LOOT_MULTIPLE_MAX.get(); i++){
                AttributeModifier modifier = new AttributeModifier(UUID.fromString("d2d110cc-f22f-11ed-a05b-1212bb11451"+i), "health_modify_in_multi_player", i - 1, AttributeModifier.Operation.MULTIPLY_TOTAL);
                AttributeInstance instance = entity.getAttribute(Attributes.MAX_HEALTH);
                if(instance != null){
                    if(i == playerCount){
                        if(instance.hasModifier(modifier)){
                            instance.addPermanentModifier(modifier);
                        }
                    } else {
                        instance.removeModifier(modifier);
                    }
                }
            }
            entity.setHealth(healthOrdinal * entity.getMaxHealth() / maxHealthOrdinal);//别忘了血量也要变
            SkilletManCoreMod.LOGGER.info("[Player Count Modify]" + entity.getType().getDescriptionId() + "'s max health has changed from [" + ordinal + "] to : " + entity.getMaxHealth());
        }
    }
}

package com.p1nero.smc.entity.api;

import com.p1nero.smc.SkilletManCoreMod;
import com.p1nero.smc.DOTEConfig;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.UUID;

/**
 * 用于在升级的时候加血量和攻击力
 */
public interface LevelableEntity {
    default void levelUp(int level){
        if(level > 0 && this instanceof LivingEntity entity && !entity.level().isClientSide){
            float ordinal = entity.getHealth();
            AttributeInstance instance = entity.getAttribute(Attributes.MAX_HEALTH);
            AttributeInstance attackDamage = entity.getAttribute(Attributes.ATTACK_DAMAGE);
            if(instance != null){
                for(int i = 1; i <= level; i++){
                    AttributeModifier levelModifier = new AttributeModifier(UUID.fromString("d2d114cc-f88f-41ed-a05b-2233bb11451"+level),"level"+level, Math.pow(DOTEConfig.MOB_MULTIPLIER_WHEN_WORLD_LEVEL_UP.get(), level) - 1, AttributeModifier.Operation.MULTIPLY_BASE);
                    if(i == level){
                        if(!instance.hasModifier(levelModifier)){
                            instance.addPermanentModifier(levelModifier);
                        }
                    } else {
                        instance.removeModifier(levelModifier);
                    }
                }
            }
            if(attackDamage != null){
                for(int i = 1; i <= level; i++){
                    AttributeModifier levelModifier = new AttributeModifier(UUID.fromString("d2d514cc-f88f-41ed-a05b-2233bb11451"+level),"level"+level, Math.pow(DOTEConfig.MOB_MULTIPLIER_WHEN_WORLD_LEVEL_UP.get(), level) - 1, AttributeModifier.Operation.MULTIPLY_BASE);
                    if(i == level){
                        if(!attackDamage.hasModifier(levelModifier)){
                            attackDamage.addPermanentModifier(levelModifier);
                        }
                    } else {
                        attackDamage.removeModifier(levelModifier);
                    }
                }
            }
            entity.setHealth(entity.getMaxHealth());
            SkilletManCoreMod.LOGGER.info("[World Level Modify]" + entity.getType().getDescriptionId() + "'s max health has changed from [" + ordinal + "] to : " + entity.getMaxHealth());
        }
    }
}
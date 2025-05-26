package com.p1nero.smc.effect;

import com.p1nero.smc.SkilletManCoreMod;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SMCEffects {

    public static final DeferredRegister<MobEffect> REGISTRY = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, SkilletManCoreMod.MOD_ID);
    public static final RegistryObject<MobEffect> BURNT = REGISTRY.register("burnt",() -> new BurntEffect(MobEffectCategory.HARMFUL, 0x6c6a5c));
    public static final RegistryObject<MobEffect> RUMOR = REGISTRY.register("rumor",() -> new SimpleMobEffect(MobEffectCategory.HARMFUL, 0xffffff));
    public static final RegistryObject<MobEffect> BAD_CAT = REGISTRY.register("bad_cat",() -> new BadCatEffect(MobEffectCategory.HARMFUL, 0xffffff));
    public static final RegistryObject<MobEffect> SUPER_CHEF = REGISTRY.register("super_chef",() -> new BadCatEffect(MobEffectCategory.BENEFICIAL, 0xff0000));

    /**
     * 避雷
     */
    public static void onEntityHurt(LivingHurtEvent event) {
        LivingEntity entity = event.getEntity();
    }

    /**
     * 九转还魂
     */
    public static void onEntityDie(LivingDeathEvent event) {
        LivingEntity entity = event.getEntity();
    }

    public static void onEntityUpdate(LivingEvent.LivingTickEvent event) {
        LivingEntity entity = event.getEntity();
    }

}

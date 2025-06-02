package com.p1nero.smc.event;

import com.p1nero.smc.SkilletManCoreMod;
import com.p1nero.smc.archive.DataManager;
import com.p1nero.smc.capability.SMCCapabilityProvider;
import com.p1nero.smc.capability.SMCPlayer;
import com.p1nero.smc.entity.SMCEntities;
import com.p1nero.smc.entity.api.MultiPlayerBoostEntity;
import com.p1nero.smc.entity.custom.boss.SMCBoss;
import com.p1nero.smc.entity.custom.boss.goldenflame.GoldenFlame;
import com.p1nero.smc.entity.custom.npc.me.P1nero;
import com.p1nero.smc.entity.custom.npc.special.virgil.VirgilVillager;
import com.p1nero.smc.network.PacketRelay;
import com.p1nero.smc.network.SMCPacketHandler;
import com.p1nero.smc.network.packet.clientbound.OpenFastKillBossScreenPacket;
import com.p1nero.smc.registrate.SMCRegistrateItems;
import com.p1nero.smc.util.ItemUtil;
import hungteen.htlib.common.event.events.DummyEntityEvent;
import hungteen.htlib.common.event.events.RaidEvent;
import hungteen.htlib.common.world.raid.DefaultRaid;
import net.minecraft.ChatFormatting;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.anti_ad.a.b.a.a.a.E;
import yesman.epicfight.world.item.EpicFightItems;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = SkilletManCoreMod.MOD_ID)
public class LivingEntityListeners {

    public static List<ItemStack> weapons = new ArrayList<>();

    @SubscribeEvent
    public static void onEntityUpdate(LivingEvent.LivingTickEvent event) {
        if(event.getEntity() instanceof MultiPlayerBoostEntity multiPlayerBoostEntity && event.getEntity().level() instanceof ServerLevel serverLevel) {
            multiPlayerBoostEntity.tickCheck(serverLevel);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onEntityHurt(LivingHurtEvent event) {

        //宠物免伤
        if (event.getEntity() instanceof OwnableEntity ownableEntity) {
            if (event.getSource().getEntity() == ownableEntity.getOwner()) {
                event.setAmount(0);
                event.setCanceled(true);
            }
        }

        if (event.getEntity() instanceof Villager villager && !(villager instanceof P1nero) && villager.getVillagerData().getProfession() != VillagerProfession.CLERIC && event.getSource().getEntity() instanceof ServerPlayer player) {
            event.setAmount(0);
            event.setCanceled(true);
            player.displayClientMessage(SkilletManCoreMod.getInfo("customer_is_first").withStyle(ChatFormatting.RED), true);
            SMCPlayer.consumeMoney(10, player);
        }
        if (event.getEntity() instanceof Player) {
            event.setAmount(event.getAmount() * 0.5F);
        }
        if (event.getSource().getEntity() instanceof ServerPlayer serverPlayer) {
            if (serverPlayer.getMainHandItem().hasTag() && serverPlayer.getMainHandItem().getOrCreateTag().getBoolean(SkilletManCoreMod.POISONED_SKILLET) && event.getEntity().isAlive()) {
                event.getEntity().addEffect(new MobEffectInstance(MobEffects.POISON, 200), serverPlayer);
            }
        }
    }

    @SubscribeEvent
    public static void onEntityDie(LivingDeathEvent event) {
        if (event.getEntity() instanceof Enemy && event.getSource().getEntity() instanceof ServerPlayer player) {
            SMCPlayer.addMoney((int) event.getEntity().getMaxHealth(), player);//击杀奖励
        }

        if (event.getEntity() instanceof Villager villager && villager.getVillagerData().getProfession() == VillagerProfession.CLERIC && event.getSource().getEntity() instanceof ServerPlayer serverPlayer) {
            ItemUtil.addItem(serverPlayer, SMCRegistrateItems.END_TELEPORTER.asStack(), true);
        }

        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            serverPlayer.displayClientMessage(SkilletManCoreMod.getInfo("die_tip"), false);
            if (event.getSource().getEntity() instanceof SMCBoss smcBoss) {
                smcBoss.setHealth(smcBoss.getHealth() + smcBoss.getMaxHealth() / 10);
                for (Player player : serverPlayer.serverLevel().players()) {
                    player.displayClientMessage(SkilletManCoreMod.getInfo("boss_will_recover"), false);
                }
                DataManager.lastKilledByBoss.put(serverPlayer, true);//记录上次被boss杀，复活的时候用
            }
        }

    }

    @SubscribeEvent
    public static void onRaidSpawn(DummyEntityEvent.DummyEntitySpawnEvent event) {
        if (event.getDummyEntity() instanceof DefaultRaid defaultRaid) {
            for (Entity entity : defaultRaid.getRaiders()) {
                entity.setGlowingTag(true);
            }
        }
    }

    /**
     * 尝试修多人突破bug
     */
    @SubscribeEvent
    public static void onRaidDefeated(RaidEvent.RaidDefeatedEvent event) {
        for (Entity entity : event.getRaid().getDefenders()) {
            if (entity instanceof ServerPlayer serverPlayer) {
                SMCPlayer smcPlayer = SMCCapabilityProvider.getSMCPlayer(serverPlayer);
                if (smcPlayer.isTrialRequired()) {
                    SMCPlayer.stageUp(serverPlayer);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onLivingExperienceDropEvent(LivingExperienceDropEvent event) {
        if (event.getEntity() instanceof EnderDragon) {
            event.setDroppedExperience(0);
        }
    }

    @SubscribeEvent
    public static void onEntityJoinLevel(EntityJoinLevelEvent event) {

        if (event.getEntity() instanceof AbstractPiglin piglin) {
            piglin.setImmuneToZombification(true);
        }

        if (event.getEntity() instanceof EnderDragon enderDragon) {
            if (event.getLevel() instanceof ServerLevel serverLevel) {
                if (serverLevel.getEntities(SMCEntities.GOLDEN_FLAME.get(), LivingEntity::isAlive).isEmpty()) {
                    GoldenFlame goldenFlame = SMCEntities.GOLDEN_FLAME.get().spawn(serverLevel, enderDragon.getOnPos(), MobSpawnType.SPAWNER);
                    if (goldenFlame != null) {
                        enderDragon.setHealth(0);
                        SkilletManCoreMod.LOGGER.info("replace ender dragon to golden flame.");
                    }
                }
            }
        }

        if (event.getEntity() instanceof ServerPlayer serverPlayer) {

            SMCCapabilityProvider.syncPlayerDataToClient(serverPlayer);
            //重置
            if (DataManager.lastKilledByBoss.get(serverPlayer)) {
                PacketRelay.sendToPlayer(SMCPacketHandler.INSTANCE, new OpenFastKillBossScreenPacket(), serverPlayer);
                DataManager.lastKilledByBoss.put(serverPlayer, false);
            }
        }

        if (weapons.isEmpty()) {
            weapons.add(Items.GOLDEN_SWORD.getDefaultInstance());
            weapons.add(Items.GOLDEN_AXE.getDefaultInstance());
            weapons.add(EpicFightItems.GOLDEN_LONGSWORD.get().getDefaultInstance());
            weapons.add(EpicFightItems.GOLDEN_GREATSWORD.get().getDefaultInstance());
            weapons.add(EpicFightItems.GOLDEN_SPEAR.get().getDefaultInstance());
            weapons.add(EpicFightItems.GOLDEN_TACHI.get().getDefaultInstance());
            weapons.add(EpicFightItems.UCHIGATANA.get().getDefaultInstance());
            weapons.add(EpicFightItems.GOLDEN_DAGGER.get().getDefaultInstance());
        }

        if (event.getEntity() instanceof Monster monster) {
            if (monster instanceof VirgilVillager) {
                monster.setItemInHand(InteractionHand.MAIN_HAND, SMCRegistrateItems.IRON_SKILLET_LEVEL5.asStack());
                return;
            }
            if (monster.getMainHandItem().isEmpty()) {
                monster.setItemInHand(InteractionHand.MAIN_HAND, weapons.get(monster.getRandom().nextInt(weapons.size())));
                if (monster.getRandom().nextInt(5) == 0) {
                    monster.setItemInHand(InteractionHand.OFF_HAND, monster.getMainHandItem());
                }
            }
        }

        if (event.getEntity() instanceof Cat cat && !cat.level().isClientSide) {
            if (!cat.hasCustomName() && cat.getVariant().texture().toString().contains("white") && cat.getRandom().nextBoolean()) {
                cat.setCustomName(SkilletManCoreMod.getInfo("rana_kaname"));
                cat.setCustomNameVisible(true);
            }
        }

    }

}

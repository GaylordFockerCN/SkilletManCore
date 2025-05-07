package com.p1nero.smc.event;

import com.p1nero.smc.SkilletManCoreMod;
import com.p1nero.smc.capability.SMCCapabilityProvider;
import com.p1nero.smc.capability.SMCPlayer;
import com.p1nero.smc.registrate.SMCRegistrateItems;
import com.p1nero.smc.util.ItemUtil;
import com.p1nero.smc.worldgen.dimension.SMCDimension;
import hungteen.htlib.common.event.events.DummyEntityEvent;
import hungteen.htlib.common.world.raid.DefaultRaid;
import net.minecraft.ChatFormatting;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.EntityTravelToDimensionEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;
import yesman.epicfight.world.item.EpicFightItems;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = SkilletManCoreMod.MOD_ID)
public class LivingEntityListeners {

    public static List<ItemStack> weapons = new ArrayList<>();

    /**
     * 穿盔甲加效果
     */
    @SubscribeEvent
    public static void onEntityUpdate(LivingEvent.LivingTickEvent event) {

    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onEntityDimChanged(EntityTravelToDimensionEvent event) {

        //维度内且为观察者则禁止传送
        if(event.getEntity().isSpectator() && event.getEntity().level().dimension() == SMCDimension.P_SKY_ISLAND_LEVEL_KEY){
            event.setCanceled(true);
        }
        //进维度换冒险
        if(event.getDimension() == SMCDimension.P_SKY_ISLAND_LEVEL_KEY){
            if(FMLEnvironment.production) {
                //开发环境不受影响
                return;
            }
            if(event.getEntity() instanceof ServerPlayer serverPlayer){
                serverPlayer.setGameMode(GameType.ADVENTURE);
            }
        }
        //出维度还原为生存
        if(event.getEntity().level().dimension() == SMCDimension.P_SKY_ISLAND_LEVEL_KEY){
            if(event.getEntity() instanceof ServerPlayer serverPlayer){
                serverPlayer.setGameMode(GameType.SURVIVAL);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onEntityHurt(LivingHurtEvent event) {
        if(event.getEntity() instanceof Villager villager && villager.getVillagerData().getProfession() != VillagerProfession.CLERIC && event.getSource().getEntity() instanceof ServerPlayer player) {
            event.setAmount(0);
            event.setCanceled(true);
            player.displayClientMessage(SkilletManCoreMod.getInfo("customer_is_first").withStyle(ChatFormatting.RED), true);
            SMCPlayer.consumeMoney(10, player);
        }
        if(event.getEntity() instanceof Player) {
            event.setAmount(event.getAmount() * 0.5F);
        }
    }

    @SubscribeEvent
    public static void onEntityDie(LivingDeathEvent event) {
        if(event.getEntity() instanceof Enemy && event.getSource().getEntity() instanceof ServerPlayer player) {
            SMCPlayer.addMoney((int) event.getEntity().getMaxHealth(), player);//击杀奖励
        }

        if(event.getEntity() instanceof Villager villager && villager.getVillagerData().getProfession() == VillagerProfession.CLERIC && event.getSource().getEntity() instanceof ServerPlayer serverPlayer) {
            ItemUtil.addItem(serverPlayer, SMCRegistrateItems.END_TELEPORTER.asStack(), true);
        }

        if(event.getEntity() instanceof ServerPlayer serverPlayer){
            serverPlayer.displayClientMessage(SkilletManCoreMod.getInfo("die_tip"), false);
        }

    }

    @SubscribeEvent
    public static void onRaidSpawn(DummyEntityEvent.DummyEntitySpawnEvent event) {
        if(event.getDummyEntity() instanceof DefaultRaid defaultRaid) {
            for(Entity entity : defaultRaid.getRaiders()){
                entity.setGlowingTag(true);
            }
        }
    }

    @SubscribeEvent
    public static void onEntityJoinLevel(EntityJoinLevelEvent event) {

        if(event.getEntity() instanceof ServerPlayer serverPlayer) {
            SMCCapabilityProvider.syncPlayerDataToClient(serverPlayer);
        }

        if(weapons.isEmpty()) {
            weapons.add(Items.GOLDEN_SWORD.getDefaultInstance());
            weapons.add(Items.GOLDEN_AXE.getDefaultInstance());
            weapons.add(EpicFightItems.GOLDEN_LONGSWORD.get().getDefaultInstance());
            weapons.add(EpicFightItems.GOLDEN_GREATSWORD.get().getDefaultInstance());
            weapons.add(EpicFightItems.GOLDEN_SPEAR.get().getDefaultInstance());
            weapons.add(EpicFightItems.GOLDEN_TACHI.get().getDefaultInstance());
            weapons.add(EpicFightItems.UCHIGATANA.get().getDefaultInstance());
            weapons.add(EpicFightItems.GOLDEN_DAGGER.get().getDefaultInstance());
        }

        if(event.getEntity() instanceof Monster monster) {
            if(monster.getMainHandItem().isEmpty()) {
                monster.setItemInHand(InteractionHand.MAIN_HAND, weapons.get(monster.getRandom().nextInt(weapons.size())));
                if(monster.getRandom().nextInt(5) == 0){
                    monster.setItemInHand(InteractionHand.OFF_HAND, monster.getMainHandItem());
                }
            }
        }

        if(event.getEntity() instanceof Cat cat && !cat.level().isClientSide) {
            if(!cat.hasCustomName() && cat.getVariant().texture().toString().contains("white")) {
                cat.setCustomName(SkilletManCoreMod.getInfo("rana_kaname"));
                cat.setCustomNameVisible(true);
            }
        }

    }

}

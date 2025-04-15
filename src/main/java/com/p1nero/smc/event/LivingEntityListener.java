package com.p1nero.smc.event;

import com.p1nero.smc.SkilletManCoreMod;
import com.p1nero.smc.worldgen.dimension.SMCDimension;
import net.minecraft.ChatFormatting;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
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

@Mod.EventBusSubscriber(modid = SkilletManCoreMod.MOD_ID)
public class LivingEntityListener {

    /**
     * 穿盔甲加效果
     */
    @SubscribeEvent
    public static void onEntityUpdate(LivingEvent.LivingTickEvent event) {

    }

    @SubscribeEvent
    public static void onEntityDie(LivingDeathEvent event) {

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
        if(event.getEntity() instanceof Villager && event.getSource().getEntity() instanceof Player player) {
            event.setAmount(0);
            event.setCanceled(true);
            player.displayClientMessage(SkilletManCoreMod.getInfo("customer_is_first").withStyle(ChatFormatting.RED), true);
        }

    }

    @SubscribeEvent
    public static void onEntityJoinLevel(EntityJoinLevelEvent event) {

    }

}

package com.p1nero.smc.entity.custom.npc.customer.customer_data.special;

import com.p1nero.smc.datagen.lang.SMCLangGenerator;
import com.p1nero.smc.entity.custom.npc.customer.Customer;
import com.p1nero.smc.entity.custom.npc.customer.customer_data.SpecialCustomerData;
import com.p1nero.smc.registrate.SMCRegistrateItems;
import com.p1nero.smc.util.ItemUtil;
import net.minecraft.server.level.ServerPlayer;
import yesman.epicfight.gameasset.EpicFightSounds;

public class SpecialCustomerData22 extends SpecialCustomerData {

    public SpecialCustomerData22() {
        super(22);
    }

    @Override
    public void generateTranslation(SMCLangGenerator generator) {
        super.generateTranslation(generator);
        generator.add(nameTranslationKey, "广告招租的村民");
        generator.add(answerPre(-2), "你这不是我想要的呀，有这时间不如多让我介绍两句 XXX 云服务器。");
        generator.add(choicePre(-2), "好好好");
        generator.add(choicePre(-3), "马上做！");
        generator.add(answerPre(-1), "等下，先让我念个台词。咳咳，平底锅侠整合包广告位招租，欢迎各位老板前来打广告哦！广告种类不限！从服务器到洗发水样样都接！");
        generator.add(choicePre(-1), "你这一天能有多少？");
        generator.add(answerPre(0), "咳咳，作者并没有给我钱，我只是个NPC。不过 XXX 云服务器（广告位招租），一年只要19.9！你值得拥有！不说了，先给我来点 %s ！");
        generator.add(choicePre(0), "交之");
        generator.add(answerPre(1), "简直是仙品！质量堪比 XXX 云服务器！");
        generator.add(choicePre(1), "这么硬核的吗...");
        generator.add(answerPre(2), "味道一般般，感觉不如使用 XXX 云！");
        generator.add(choicePre(2), "这么硬核的吗...");
        generator.add(answerPre(3), "这是人吃的吗！如果使用了 XXX 云就不会做出这猪食了！");
        generator.add(choicePre(3), "这做菜还和服务器有关系吗...");
    }

    @Override
    protected void onBest(ServerPlayer serverPlayer, Customer self) {
        super.onBest(serverPlayer, self);
        ItemUtil.addItem(serverPlayer, SMCRegistrateItems.DOLL_RAFFLE_TICKET.asItem(), 5);
        ItemUtil.addItem(serverPlayer, SMCRegistrateItems.PET_RAFFLE_TICKET.asItem(), 3);
        ItemUtil.addItem(serverPlayer, SMCRegistrateItems.DISC_RAFFLE_TICKET.asItem(), 1);
    }

    @Override
    protected void onBad(ServerPlayer serverPlayer, Customer self) {
        super.onBad(serverPlayer, self);
        serverPlayer.setSilent(false);
        serverPlayer.playSound(EpicFightSounds.BLUNT_HIT.get());
    }
}

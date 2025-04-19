package com.p1nero.smc.entity.custom.npc.customer.customer_data.special;

import com.p1nero.smc.client.gui.DialogueComponentBuilder;
import com.p1nero.smc.client.gui.TreeNode;
import com.p1nero.smc.datagen.lang.SMCLangGenerator;
import com.p1nero.smc.entity.custom.npc.customer.Customer;
import com.p1nero.smc.entity.custom.npc.customer.customer_data.SpecialCustomerData;
import com.p1nero.smc.item.SMCItems;
import com.p1nero.smc.util.ItemUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Items;
import yesman.epicfight.gameasset.EpicFightSounds;

public class SpecialCustomerData3 extends SpecialCustomerData {

    public SpecialCustomerData3() {
        super(3);
    }

    public void generateTranslation(SMCLangGenerator generator) {
        super.generateTranslation(generator);
        generator.add(nameTranslationKey, "看起来很豪爽的村民");
        generator.add(answerPre(-2), "（铜铃眼瞪着你）洒家要的是下酒菜，这劳什子能就酒？");
        generator.add(choicePre(-2), "缩脖子退下");
        generator.add(choicePre(-3), "这就去！");
        generator.add(answerPre(-1), "（这位村民看起来凶神恶煞）");
        generator.add(choicePre(-1), "好汉要什么硬菜？");
        generator.add(answerPre(0), "切二斤 %s 来！要嚼着带响的！");
        generator.add(choicePre(0), "呈上");
        generator.add(answerPre(1), "（仰天大笑）痛快！这钻石够买你半间铺子了！（虽然你实在不知道平底锅侠的世界里要钻石有何用）");
        generator.add(choicePre(1), "收下钻石");
        generator.add(answerPre(2), "（剔牙）怎么像娘们吃的玩意儿");
        generator.add(choicePre(2), "好嘛");
        generator.add(answerPre(3), "（大怒）喂狗的东西也敢拿给爷爷！（设定上你是行侠仗义之人，可不能和普通人打起来，于是你只能求饶）");
        generator.add(choicePre(3), "饶命！饶命！");
    }

    @Override
    protected void onBest(ServerPlayer serverPlayer, Customer self) {
        super.onBest(serverPlayer, self);
        ItemUtil.addItem(serverPlayer, Items.DIAMOND, 64);
        ItemUtil.addItem(serverPlayer, SMCItems.WEAPON_RAFFLE_TICKET.get(), 3);
    }

    @Override
    protected void onBad(ServerPlayer serverPlayer, Customer self) {
        super.onBad(serverPlayer, self);
        serverPlayer.playSound(EpicFightSounds.BLADE_HIT.get());
        serverPlayer.hurt(serverPlayer.damageSources().magic(), 0.5F);
    }
}

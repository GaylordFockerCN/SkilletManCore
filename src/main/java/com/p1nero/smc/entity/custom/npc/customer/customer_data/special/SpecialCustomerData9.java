package com.p1nero.smc.entity.custom.npc.customer.customer_data.special;

import com.p1nero.smc.client.gui.DialogueComponentBuilder;
import com.p1nero.smc.client.gui.TreeNode;
import com.p1nero.smc.datagen.lang.SMCLangGenerator;
import com.p1nero.smc.entity.custom.npc.customer.Customer;
import com.p1nero.smc.entity.custom.npc.customer.customer_data.SpecialCustomerData;
import com.p1nero.smc.item.SMCItems;
import com.p1nero.smc.registrate.SMCRegistrateItems;
import com.p1nero.smc.util.ItemUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.gameevent.GameEvent;

public class SpecialCustomerData9 extends SpecialCustomerData {

    public SpecialCustomerData9() {
        super(9);
    }

    public void generateTranslation(SMCLangGenerator generator) {
        generator.add(nameTranslationKey, "冷漠的村民");
        generator.add(choicePre(-3), "马上做！");
        generator.add(answerPre(-2), "（简短而不耐烦地）不是我要的。别浪费时间。");
        generator.add(choicePre(-2), "重新准备");

        generator.add(answerPre(-1), "（这位村民眼神空洞，仿佛对周围一切毫无兴趣）");
        generator.add(choicePre(-1), "需要我为您准备什么吗？");

        generator.add(answerPre(0), "要吃就来点能一眼看出价值的 %s 。");
        generator.add(choicePre(0), "即刻奉上");

        generator.add(answerPre(1), "（微微点头）还算合格。此武器送你罢");
        generator.add(choicePre(1), "收下，多谢");

        generator.add(answerPre(2), "（不屑一顾）勉勉强强。");
        generator.add(choicePre(2), "告退");

        generator.add(answerPre(3), "（冷哼一声）废物。别让我再看见这种东西。");
        generator.add(choicePre(3), "不敢，就此别过。");
    }

    @Override
    protected void onBest(ServerPlayer serverPlayer, Customer self) {
        super.onBest(serverPlayer, self);
        ItemUtil.addItem(serverPlayer, SMCRegistrateItems.WEAPON_RAFFLE_TICKET.get(), 5);
    }

    @Override
    protected void onBad(ServerPlayer serverPlayer, Customer self) {
        super.onBad(serverPlayer, self);
        serverPlayer.hurt(serverPlayer.damageSources().magic(), 0.5F);
    }

}

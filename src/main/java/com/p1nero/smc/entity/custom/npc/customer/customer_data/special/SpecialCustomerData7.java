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
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.item.Items;

import java.util.Objects;
import java.util.UUID;

public class SpecialCustomerData7 extends SpecialCustomerData {

    public SpecialCustomerData7() {
        super(7);
    }

    @Override
    public void onInteract(ServerPlayer player, Customer self) {
        super.onInteract(player, self);
        self.setVillagerData(self.getVillagerData().setProfession(VillagerProfession.NITWIT));
    }

    public void generateTranslation(SMCLangGenerator generator) {
        generator.add(nameTranslationKey, "愚蠢的村民");
        generator.add(choicePre(-3), "马上做！");
        generator.add(answerPre(-2), "（挠头困惑）这个...这个不是我要的...我要的是...");
        generator.add(choicePre(-2), "好的，我再去弄。");

        generator.add(answerPre(-1), "（这位村民眼神迷离，仿佛在注视着遥远的神秘事物）");
        generator.add(choicePre(-1), "这位客官想要啥呢？");

        generator.add(answerPre(0), "我听说有一种超级大大的 %s ？ （或许它的需求会异于常人）");
        generator.add(choicePre(0), "递上食物");


        generator.add(answerPre(1), "（惊恐后退）啊！怪物食物！你当我傻呢！（你赶忙解释安抚）");
        generator.add(choicePre(1), "别怕别怕！");

        generator.add(answerPre(2), "（困惑挠头）嗯...不太对...");
        generator.add(choicePre(2), "对的对的，再见！");

        generator.add(answerPre(3), "（突然欢呼）哇哦！好吃好吃！这个金闪闪之物送你！这是我最宝贵的东西！");
        generator.add(choicePre(3), "微笑着收下");
    }

    @Override
    protected void onBad(ServerPlayer serverPlayer, Customer self) {
        //反过来
        super.onBest(serverPlayer, self);
        ItemUtil.addItem(serverPlayer, Items.STONE.getDefaultInstance());
    }

    @Override
    protected void onBest(ServerPlayer serverPlayer, Customer self) {
        //反过来
        super.onBad(serverPlayer, self);
    }

}

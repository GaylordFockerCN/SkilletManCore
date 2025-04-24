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
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.Objects;
import java.util.UUID;

public class SpecialCustomerData5 extends SpecialCustomerData {

    public SpecialCustomerData5() {
        super(5);
    }

    public void generateTranslation(SMCLangGenerator generator) {
        generator.add(nameTranslationKey, "忧伤的村民");
        generator.add(answerPre(-2), "（低声叹气）这不是我想要的东西...麻烦你再想想看。");
        generator.add(choicePre(-2), "重新准备。");
        generator.add(choicePre(-3), "这就去！");
        generator.add(answerPre(-1), "（这位村民眼神忧郁，仿佛有无尽心事）");
        generator.add(choicePre(-1), "请问客官想吃些什么？");
        generator.add(answerPre(0), "如果有一道能慰藉心灵的 %s ，或许能让我暂时忘却烦恼。");
        generator.add(choicePre(0), "轻声呈上");
        generator.add(answerPre(1), "（眼眸微亮）这味道像极了从前的她...这份唱片送你，愿你也能珍惜这份美好。年轻人，不要等到失去了才懂得珍惜！");
        generator.add(choicePre(1), "轻声道谢  （原来是失恋了）");
        generator.add(answerPre(2), "（轻叹）还不够...还不够...这味道，不及她三分...");
        generator.add(choicePre(2), "告辞，再接再厉。");
        generator.add(answerPre(3), "（身子一颤）这怎么吃呀...（你赶忙道歉安抚）");
        generator.add(choicePre(3), "实在抱歉，请多包涵。");
    }

    @Override
    protected void onBest(ServerPlayer serverPlayer, Customer self) {
        super.onBest(serverPlayer, self);
        ItemUtil.addItem(serverPlayer, SMCRegistrateItems.DISC_RAFFLE_TICKET.get(), 3);
    }

}

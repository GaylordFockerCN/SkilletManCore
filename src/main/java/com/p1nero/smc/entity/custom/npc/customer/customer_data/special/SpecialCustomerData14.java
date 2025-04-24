package com.p1nero.smc.entity.custom.npc.customer.customer_data.special;

import com.github.ysbbbbbb.kaleidoscopedoll.init.ModItems;
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
import net.minecraft.world.item.Items;

public class SpecialCustomerData14 extends SpecialCustomerData {

    public SpecialCustomerData14() {
        super(14);
    }

    public void generateTranslation(SMCLangGenerator generator) {
        generator.add(nameTranslationKey, "爱哭的村民");
        generator.add(choicePre(-3), "马上做！");

        generator.add(answerPre(-2), "（眼眶瞬间泛红）这个不是我要的...（眼泪在眼眶打转）我想要的不是这个...");
        generator.add(choicePre(-2), "（这玻璃心咋办，赶紧选择正确的食材吧！）");

        generator.add(answerPre(-1), "（眼前这位村民眼神水汪汪，随时准备哭出来的样子）");
        generator.add(choicePre(-1), "客官别哭，我这儿有美食可选。");

        generator.add(answerPre(0), "能不能...（快哭出来）给我来点...（抽泣）能让我开心的 %s ...");
        generator.add(choicePre(0), "提交");

        generator.add(answerPre(1), "（破涕为笑）真的吗？！好开心！送你这个，谢谢！（不是爱哭的村民吗，怎么马上就笑起来了...）");
        generator.add(choicePre(1), "（笑着收下）多谢，告辞！");

        generator.add(answerPre(2), "（眼泪汪汪）不够好...我好难过...（抽泣）");
        generator.add(choicePre(2), "下次一定！");

        generator.add(answerPre(3), "（哇的一声大哭）这是什么呀！（你赶忙递纸巾）");
        generator.add(choicePre(3), "（无奈）别哭了，别哭了，我错了！");
    }

    @Override
    protected void onBest(ServerPlayer serverPlayer, Customer self) {
        super.onBest(serverPlayer, self);
        ItemUtil.addItem(serverPlayer, SMCRegistrateItems.WEAPON_RAFFLE_TICKET.get(), 3);
        ItemUtil.addItem(serverPlayer, SMCRegistrateItems.WEAPON_RAFFLE_TICKET.asItem(), 3);
    }

    @Override
    protected void onBad(ServerPlayer serverPlayer, Customer self) {

    }

}

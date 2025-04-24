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
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class SpecialCustomerData15 extends SpecialCustomerData {

    public SpecialCustomerData15() {
        super(15);
    }

    @Override
    public Component getTranslation() {
        return Component.literal("Grumm");
    }

    public void generateTranslation(SMCLangGenerator generator) {
        generator.add(nameTranslationKey, "民村的来过反");
        generator.add(answerPre(-2), "的要我是不这");
        generator.add(choicePre(-2), "！歉抱，歉抱");
        generator.add(choicePre(-3), "！去就这");
        generator.add(answerPre(-1), "（眼前这位村民不知为何上下翻转，连同文案也不知为何，反过来了）");
        generator.add(choicePre(-1), "？么点什吃想官客");
        generator.add(answerPre(0), "？s% 点来");
        generator.add(choicePre(0), "交提");
        generator.add(answerPre(1), "！赏重有重，错不味道这");
        generator.add(choicePre(1), "下收");
        generator.add(answerPre(2), "般一味道这");
        generator.add(choicePre(2), "话应对束结");
        generator.add(answerPre(3), "！差极味道这");
        generator.add(choicePre(3), "话应对束结");
    }

    @Override
    protected void onBest(ServerPlayer serverPlayer, Customer self) {
        super.onBest(serverPlayer, self);
        ItemUtil.addItem(serverPlayer, SMCRegistrateItems.WEAPON_RAFFLE_TICKET.get(), 4);
        ItemUtil.addItem(serverPlayer, SMCRegistrateItems.ARMOR_RAFFLE_TICKET.get(), 4);
        ItemStack nameTag = Items.NAME_TAG.getDefaultInstance();
        nameTag.setHoverName(Component.literal("Dinnerbone"));
        ItemUtil.addItem(serverPlayer, nameTag);
    }

}

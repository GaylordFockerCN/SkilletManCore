package com.p1nero.smc.entity.custom.npc.customer.customer_data.special;

import com.p1nero.smc.client.gui.DialogueComponentBuilder;
import com.p1nero.smc.client.gui.TreeNode;
import com.p1nero.smc.datagen.lang.SMCLangGenerator;
import com.p1nero.smc.entity.custom.npc.customer.customer_data.SpecialCustomerData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;

public class SCustomerData1 extends SpecialCustomerData {

    @Override
    public void generateTranslation(SMCLangGenerator generator) {
        generator.add("special_customer_1_answer_0", "老夫闭关三日，可有补气益元的珍膳 %s ？");
        generator.add("special_customer_1_choice_0", "恭敬呈上");
        generator.add("special_customer_1_answer_1","（捋须长笑）此味通达任督二脉！我看你骨骼惊奇，此秘笈赠与你罢！去也！");
        generator.add("special_customer_1_choice_1", "恭敬收下");
        generator.add("special_customer_1_answer_2","（摇头）火候尚欠一甲子修为");
        generator.add("special_customer_1_choice_2", "（这老登真不识货）");
        generator.add("special_customer_1_answer_3", "（震碎碗碟）岂敢拿猪食欺我！");
        generator.add("special_customer_1_choice_3", "妈妈救我！");
    }

    @Override
    protected void append(TreeNode root, CompoundTag serverData, DialogueComponentBuilder dialogueComponentBuilder) {

    }

    @Override
    public void handle(ServerPlayer serverPlayer, int interactId) {
        super.handle(serverPlayer, interactId);
    }
}

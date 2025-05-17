package com.p1nero.smc.entity.custom.npc.customer.customer_data.special;

import com.p1nero.smc.capability.SMCPlayer;
import com.p1nero.smc.client.gui.DialogueComponentBuilder;
import com.p1nero.smc.client.gui.TreeNode;
import com.p1nero.smc.datagen.lang.SMCLangGenerator;
import com.p1nero.smc.entity.custom.npc.customer.Customer;
import com.p1nero.smc.entity.custom.npc.customer.customer_data.SpecialCustomerData;
import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class SpecialCustomerData18 extends SpecialCustomerData {

    public SpecialCustomerData18() {
        super(18);
    }

    public void generateTranslation(SMCLangGenerator generator) {
        generator.add(nameTranslationKey, "挑剔的村民");
        generator.add(answerPre(-2), "这明显不是我要的");
        generator.add(choicePre(-2), "好的，这就重做");

        generator.add(answerPre(-1), "（眼前这位村民眼神高傲，仿佛在审视艺术品，看来不好对付）");
        generator.add(choicePre(-1), "客官您看要些什么？");

        generator.add(answerPre(0), "来份 %s ，不要加肉也不要加菜，不要加鱼也不要加饭，也不要加意面。");
        generator.add(choicePre(0), "呈上");

        generator.add(answerPre(1), "（摇头晃脑）这是什么垃圾！我不是说了不要加肉也不要加菜，不要加鱼也不要加饭，也不要加意面。");
        generator.add(choicePre(1), "你这是来找茬的吧？");
        generator.add(choicePre(2), "揍他一顿（难得有这个选项，真的不选吗！）");

        generator.add(answerPre(2), "哎哎哎！杀人啦！杀人啦！");

    }

    @Override
    protected void append(TreeNode root, CompoundTag serverData, DialogueComponentBuilder dialogueComponentBuilder, boolean canSubmit, int foodScore) {
        String foodName = "§6" + I18n.get(serverData.getString("food_name")) + "§r";
        if (!canSubmit) {
            root.addChild(new TreeNode(answer(0), choice(-1))
                    .addChild(new TreeNode(answer(-2, foodName), choice(0))
                            .addLeaf(choice(-2), (byte) -3)));
        } else {
            root.addChild(new TreeNode(answer(0), choice(-1))
                    .addExecutable(SUBMIT_FOOD)
                    .addChild(new TreeNode(answer(1), choice(0))
                            .addLeaf(choice(1), BAD)
                            .addLeaf(choice(2), BEST)));
        }
    }

    @Override
    protected void onBest(ServerPlayer serverPlayer, Customer self) {
        super.onBest(serverPlayer, self);
        serverPlayer.displayClientMessage(Component.literal("[").append(this.getTranslation().copy().withStyle(ChatFormatting.YELLOW)).append("] :").append(answer(2)), false);
        self.hurt(serverPlayer.damageSources().playerAttack(serverPlayer), 1145);
        SMCPlayer.updateWorkingState(false, serverPlayer);
    }

}

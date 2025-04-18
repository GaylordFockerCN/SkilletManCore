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

import java.util.Objects;
import java.util.UUID;

public class SpecialCustomerData7 extends SpecialCustomerData {

    public SpecialCustomerData7() {
        super(7);
    }

    public void generateTranslation(SMCLangGenerator generator) {
        generator.add(nameTranslationKey, "修道院的客户");
        generator.add(answerPre(-2), "（念珠突然发烫）迷途的孩子，这不是斋戒日该出现之物");
        generator.add(choicePre(-2), "在胸前画十字");
        generator.add(answerPre(-1), "（粗麻袍袖中隐约露出带血痕的苦修带）");
        generator.add(choicePre(-1), "姊妹需要怎样的虔敬之食？");
        generator.add(answerPre(0), "按《圣本笃训规》第59章，请奉上能使灵魂轻盈的%s");
        generator.add(choicePre(0), "洒圣盐祝福");
        generator.add(answerPre(1), "（浮现圣痕）这纯净度堪比约旦河水！收下这片真十字架碎片！");
        generator.add(choicePre(1), "收下");
        generator.add(answerPre(2), "（烛光变成幽蓝色）里面掺杂了堕落的念头");
        generator.add(choicePre(2), "默念忏悔经");
        generator.add(answerPre(3), "（地面裂开硫磺坑）你被七宗罪侵蚀了！（最大生命-2");
        generator.add(choicePre(3), "？？？");
    }

    @Override
    protected void append(TreeNode root, CompoundTag serverData, DialogueComponentBuilder dialogueComponentBuilder, boolean canSubmit, int foodScore) {
        if (!canSubmit) {
            root.addChild(new TreeNode(answer(0), choice(-1))
                    .addChild(new TreeNode(answer(-2, serverData.get("food_name")), choice(0))
                            .addLeaf(choice(-2), (byte) -3)));
        } else {
            switch (foodScore) {
                case BEST:
                    root.addChild(new TreeNode(answer(0), choice(-1))
                            .execute(SUBMIT_FOOD)
                            .addChild(new TreeNode(answer(1), choice(0))
                                    .addLeaf(choice(1), BEST)));
                    break;
                case MIDDLE:
                    root.addChild(new TreeNode(answer(0), choice(-1))
                            .execute(SUBMIT_FOOD)
                            .addChild(new TreeNode(answer(2), choice(0))
                                    .addLeaf(choice(2), MIDDLE)));
                    break;
                default:
                    root.addChild(new TreeNode(answer(0), choice(-1))
                            .execute(SUBMIT_FOOD)
                            .addChild(new TreeNode(answer(3), choice(0))
                                    .addLeaf(choice(3), BAD)));
            }
        }
    }

    @Override
    protected void onBest(ServerPlayer serverPlayer, Customer self) {
        super.onBest(serverPlayer, self);
        ItemUtil.addItem(serverPlayer, SMCItems.SKILL_BOOK_RAFFLE_TICKET.get().getDefaultInstance());
    }

    @Override
    protected void onBad(ServerPlayer serverPlayer, Customer self) {
        super.onBad(serverPlayer, self);
        serverPlayer.hurt(serverPlayer.damageSources().magic(), 0.5F);
        Objects.requireNonNull(serverPlayer.getAttribute(Attributes.MAX_HEALTH)).addPermanentModifier(new AttributeModifier(UUID.randomUUID(), "触怒圣女，该罚！", -2, AttributeModifier.Operation.ADDITION));
    }

}

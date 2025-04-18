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

public class SpecialCustomerData5 extends SpecialCustomerData {

    public SpecialCustomerData5() {
        super(5);
    }

    public void generateTranslation(SMCLangGenerator generator) {
        super.generateTranslation(generator);
        generator.add(nameTranslationKey, "仙风道骨的客户");
        generator.add(answerPre(-2), "（拂尘轻扫）小道要的是晨露调制的茶点，凡尘俗物且收去 ");
        generator.add(choicePre(-2), "稽首称是");
        generator.add(answerPre(-1), "（鹤氅无风自动，周身似有流云环绕，虽然你不理解怎么这个世界会有仙人）");
        generator.add(choicePre(-1), "仙长要用什么灵肴？");
        generator.add(answerPre(0), "听闻贵处有采自瑶池畔的%s？");
        generator.add(choicePre(0), "试图取出琉璃盏");
        generator.add(answerPre(1), "（周身泛起金光）此物可抵三年吐纳之功，这卷《云笈七签》便予你参悟！");
        generator.add(choicePre(1), "跪接经卷");
        generator.add(answerPre(2), "（叹气）终究沾了烟火浊气");
        generator.add(choicePre(2), "送客");
        generator.add(answerPre(3), "（平地惊雷骤起）妄以浊物乱人道心！应当折寿！ （最大生命上限 -1）");
        generator.add(choicePre(3), "道爷饶命！");
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
        Objects.requireNonNull(serverPlayer.getAttribute(Attributes.MAX_HEALTH)).addPermanentModifier(new AttributeModifier(UUID.randomUUID(), "触怒仙人，该罚！", -1, AttributeModifier.Operation.ADDITION));
    }
}

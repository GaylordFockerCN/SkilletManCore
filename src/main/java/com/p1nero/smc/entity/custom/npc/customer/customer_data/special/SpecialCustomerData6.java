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

public class SpecialCustomerData6 extends SpecialCustomerData {

    public SpecialCustomerData6() {
        super(6);
    }

    public void generateTranslation(SMCLangGenerator generator) {
        generator.add(nameTranslationKey, "落魄的客户");
        generator.add(answerPre(-2), "（剑鞘重重磕在柜台上）真正的骑士只会接受与战马同食的佳肴 §6（...这包要是能火我一定去给村民换个模型）");
        generator.add(choicePre(-2), "请提交正确的物品");
        generator.add(answerPre(-1), "（生锈的锁子甲下露出褪色的家族纹章）");
        generator.add(choicePre(-1), "阁下需要怎样的勇者之宴？");
        generator.add(answerPre(0), "以蔷薇骑士团之名，可有能唤醒战场回忆的%s？");
        generator.add(choicePre(0), "行骑士礼后呈上");
        generator.add(answerPre(1), "（单膝跪地）这滋味让我想起收复圣城那日的庆功宴！收下这神兵！");
        generator.add(choicePre(1), "触摸纹章上的血痕");
        generator.add(answerPre(2), "（擦拭佩剑）城堡厨娘的水平不止如此");
        generator.add(choicePre(2), "送客");
        generator.add(answerPre(3), "这是给叛徒吃的泔水！");
        generator.add(choicePre(3), "息怒！息怒！");
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
        ItemUtil.addItem(serverPlayer, SMCItems.WEAPON_RAFFLE_TICKET.get().getDefaultInstance());
    }

    @Override
    protected void onBad(ServerPlayer serverPlayer, Customer self) {
        super.onBad(serverPlayer, self);
        serverPlayer.hurt(serverPlayer.damageSources().magic(), 0.5F);
    }
}

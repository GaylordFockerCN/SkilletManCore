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

public class SpecialCustomerData10 extends SpecialCustomerData {

    public SpecialCustomerData10() {
        super(10);
    }

    public void generateTranslation(SMCLangGenerator generator) {
        generator.add(nameTranslationKey, "笑里藏刀的客户");
        generator.add(answerPre(-2), "（折扇轻点你手背）小哥~呈错东西会掉手指头的哟~");
        generator.add(choicePre(-2), "冷汗浸透后背");
        generator.add(answerPre(-1), "（绣金扇面隐约露出唐门标记）");
        generator.add(choicePre(-1), "贵客想尝点新奇滋味？");
        generator.add(answerPre(0), "奴家最爱那入口封喉的%s呢~");
        generator.add(choicePre(0), "用银针试毒后奉上");
        generator.add(answerPre(1), "（朱唇勾起）这酥麻感正合适~送你件 神兵 把玩吧");
        generator.add(choicePre(1), "收下");
        generator.add(answerPre(2), "（捏碎瓷勺）连五毒教的药人都毒不倒");
        generator.add(choicePre(2), "息怒！息怒！");
        generator.add(answerPre(3), "竟敢拿糖水糊弄我！");
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

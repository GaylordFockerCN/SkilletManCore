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
import net.minecraft.world.item.Items;

public class SpecialCustomerData14 extends SpecialCustomerData {

    public SpecialCustomerData14() {
        super(14);
    }

    public void generateTranslation(SMCLangGenerator generator) {
        generator.add(nameTranslationKey, "当铺朝奉的客户");
        generator.add(answerPre(-2), "（放大镜映出寒光）赝品也敢拿来充数？");
        generator.add(choicePre(-2), "躬身退下");
        generator.add(answerPre(-1), "（翡翠扳指正在掐算时辰）");
        generator.add(choicePre(-1), "掌柜要鉴些珍奇食材？");
        generator.add(answerPre(0), "上月收的百年%s需验成色，呈来");
        generator.add(choicePre(0), "捧出紫檀食盒");
        generator.add(answerPre(1), "（冷笑）倒是看走眼了，这枚错金铜权予你当彩头");
        generator.add(choicePre(1), "双手托接");
        generator.add(answerPre(2), "（摔账本）市井粗食也敢充贡品！");
        generator.add(choicePre(2), "低头送客");
        generator.add(answerPre(3), "（突然敲响惊堂木）好大的做局胆子！");
        generator.add(choicePre(3), "疾退三步闭柜");
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

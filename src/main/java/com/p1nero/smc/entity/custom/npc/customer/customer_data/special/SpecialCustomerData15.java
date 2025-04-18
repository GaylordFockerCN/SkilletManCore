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

public class SpecialCustomerData15 extends SpecialCustomerData {

    public SpecialCustomerData15() {
        super(15);
    }

    public void generateTranslation(SMCLangGenerator generator) {
        generator.add(nameTranslationKey, "青楼花魁般的客户");
        generator.add(answerPre(-2), "（金步摇骤响）拿这等俗物辱我眼界？");
        generator.add(choicePre(-2), "欠身离去");
        generator.add(answerPre(-1), "（胭脂香染透三重纱帘，看得出是个美人。但是作者现在缺少模型，暂且先想象一下你见过最美丽的女子吧。）");
        generator.add(choicePre(-1), "姑娘要备些助兴佳肴？");
        generator.add(answerPre(0), "今夜状元郎指名要配诗的%s，速备");
        generator.add(choicePre(0), "以银刀分切");
        generator.add(answerPre(1), "（抛来香囊）此味当入《群芳谱》，这 匣子 黛赏你");
        generator.add(choicePre(1), "屈膝谢赏");
        generator.add(answerPre(2), "（撕毁诗笺）尚不及盐商妾室手艺");
        generator.add(choicePre(2), "福身送客");
        generator.add(answerPre(3), "（突然掷碎琉璃盏）当我秦淮河雏妓不成？！");
        generator.add(choicePre(3), "掩面撤退");
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
        ItemUtil.addItem(serverPlayer, SMCItems.DISK_RAFFLE_TICKET.get().getDefaultInstance());
    }

}

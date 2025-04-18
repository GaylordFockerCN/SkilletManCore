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
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.gameevent.GameEvent;

public class SpecialCustomerData9 extends SpecialCustomerData {

    public SpecialCustomerData9() {
        super(9);
    }

    public void generateTranslation(SMCLangGenerator generator) {
        generator.add(nameTranslationKey, "冷若冰霜的客户");
        generator.add(answerPre(-2), "（桌面瞬间结霜）你当本宫会碰这些腌臜之物？");
        generator.add(choicePre(-2), "哈着白雾后退");
        generator.add(answerPre(-1), "（雪狐裘衣领间隐约露出寒玉项链）");
        generator.add(choicePre(-1), "仙子需要冰镇仙露吗？");
        generator.add(answerPre(0), "极北寒渊千年不化的%s，取来");
        generator.add(choicePre(0), "戴上玄冰手套呈递");
        generator.add(answerPre(1), "（周遭飘起雪霰）七成雪魄精元尚存，这卷 秘笈 赏你");
        generator.add(choicePre(1), "收下");
        generator.add(answerPre(2), "（茶盏冻裂）火毒未除尽");
        generator.add(choicePre(2), "下次一定");
        generator.add(answerPre(3), "（冰锥突刺而来）你想让本宫走火入魔？");
        generator.add(choicePre(3), "不敢！不敢！");
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
    }

}

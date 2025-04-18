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
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.gameevent.GameEvent;

import java.util.Objects;
import java.util.UUID;

public class SpecialCustomerData8 extends SpecialCustomerData {

    public SpecialCustomerData8() {
        super(8);
    }

    public void generateTranslation(SMCLangGenerator generator) {
        generator.add(nameTranslationKey, "醉眼朦胧的客户");
        generator.add(answerPre(-2), "（酒葫芦咚地砸在案上）老子要下酒菜...嗝...这玩意能配竹叶青？");
        generator.add(choicePre(-2), "（失算，竟然被识破了）");
        generator.add(answerPre(-1), "（浑身酒气的剑客正用筷子敲击《将进酒》节拍）");
        generator.add(choicePre(-1), "剑仙要来点醒酒汤么？");
        generator.add(answerPre(0), "听闻...你们有能化酒为剑气的%s？");
        generator.add(choicePre(0), "揭开乾坤食盒");
        generator.add(answerPre(1), "（突然舞剑）十步杀一人的剑意都在此中了！这柄利器送你切肉");
        generator.add(choicePre(1), "收下");
        generator.add(answerPre(2), "（醉卧柜台）比起杜康阁还差...三十杯...");
        generator.add(choicePre(2), "送客");
        generator.add(answerPre(3), "（剑气劈开房梁）这是给毛贼吃的断头饭！");
        generator.add(choicePre(3), "（抱头钻到八仙桌下）饶命！饶命！");
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
        PrimedTnt primedtnt = new PrimedTnt(serverPlayer.level(), self.getX() + 0.5D, self.getY(), self.getZ() + 0.5D, self);
        serverPlayer.level().addFreshEntity(primedtnt);
        serverPlayer.level().playSound(null, primedtnt.getX(), primedtnt.getY(), primedtnt.getZ(), SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0F, 1.0F);
        serverPlayer.level().gameEvent(self, GameEvent.PRIME_FUSE, self.getOnPos());
    }

}

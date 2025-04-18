package com.p1nero.smc.entity.custom.npc.customer.customer_data.special;

import com.p1nero.smc.client.gui.DialogueComponentBuilder;
import com.p1nero.smc.client.gui.TreeNode;
import com.p1nero.smc.datagen.lang.SMCLangGenerator;
import com.p1nero.smc.entity.custom.npc.customer.Customer;
import com.p1nero.smc.entity.custom.npc.customer.customer_data.SpecialCustomerData;
import com.p1nero.smc.item.SMCItems;
import com.p1nero.smc.util.ItemUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraftforge.registries.ForgeRegistries;
import yesman.epicfight.gameasset.EpicFightSounds;

import java.util.Objects;

public class SpecialCustomerData2 extends SpecialCustomerData {

    public SpecialCustomerData2() {
        super(2);
    }

    public void generateTranslation(SMCLangGenerator generator) {
        super.generateTranslation(generator);
        generator.add(nameTranslationKey, "娇俏可爱的客户");
        generator.add(answerPre(-2), "（小姑娘踮脚看了看你的托盘）这个不能当点心吃的呀");
        generator.add(choicePre(-2), "吐舌头");
        generator.add(answerPre(-1), "（双髻上的银铃随着她转头叮当作响） §6（有一说一，我很难想象面前这个大鼻子村民是小萝莉...或许以后可以给它换个模型）");
        generator.add(choicePre(-1), "小妹妹要尝尝新点心吗？");
        generator.add(answerPre(0), "听说你们有甜滋滋的%s？要能配桂花蜜的那种！");
        generator.add(choicePre(0), "献宝式端出");
        generator.add(answerPre(1), "（眼睛弯成月牙）这个比娘亲做的蜜饯还香！这个小狐狸送你玩～");
        generator.add(choicePre(1), "（非常开心的收下，谁能拒绝一只可爱的小狐狸呢）");
        generator.add(answerPre(2), "（嘟嘴）糖霜放少了三钱呢");
        generator.add(choicePre(2), "下次再来玩呀~");
        generator.add(answerPre(3), "（突然嚎啕大哭）苦苦的东西最讨厌了！");
        generator.add(choicePre(3), "诶！妹妹别走！（手忙脚乱找糖果）");
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
        ItemUtil.addItem(serverPlayer, Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(new ResourceLocation("kaleidoscope_doll:doll_45"))).getDefaultInstance());

    }

    @Override
    protected void onBad(ServerPlayer serverPlayer, Customer self) {
        super.onBad(serverPlayer, self);
        serverPlayer.playSound(EpicFightSounds.BLUNT_HIT.get());
    }
}

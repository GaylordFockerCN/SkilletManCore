package com.p1nero.smc.entity.custom.npc.customer.customer_data.special;

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
        generator.add(answerPre(-2), "（皱眉，挑剔地）这个？颜色不行，摆盘不行，味道更不行。重做。（突然又补充）您这手艺得好好打磨打磨，像我这种美食家可不好糊弄。（突然又压低声音）您别觉得我苛刻，我只是对美食有追求。");
        generator.add(choicePre(-2), "好的，这就重做");

        generator.add(answerPre(-1), "（眼前这位村民眼神高傲，仿佛在审视艺术品，看来不好对付）");
        generator.add(choicePre(-1), "客官您看要些什么？");

        generator.add(answerPre(0), "要吃就来点...（挑眉）能符合我审美标准的 %s 。");
        generator.add(choicePre(0), "呈上");

        generator.add(answerPre(1), "（摇头晃脑）这是什么垃圾！火候、刀工、调味，全都不行...您不会是故意糊弄我吧？上周在东街碰到个厨子，做的菜比您这个也好不到哪儿去，结果被老板当场解雇...您说这是不是个教训？我有个私人厨师，他做出来的菜比您这个好多了。不过他有个毛病，就是太爱加盐...（突然又警觉）您别误会，我不是在贬低您，只是实话实说。");
        generator.add(choicePre(1), "下次一定改进！");
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
                    .execute(SUBMIT_FOOD)
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
    }

}

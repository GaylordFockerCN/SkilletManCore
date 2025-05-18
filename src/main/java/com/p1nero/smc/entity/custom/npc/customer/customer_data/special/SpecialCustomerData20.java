package com.p1nero.smc.entity.custom.npc.customer.customer_data.special;

import com.p1nero.smc.block.entity.MainCookBlockEntity;
import com.p1nero.smc.client.gui.DialogueComponentBuilder;
import com.p1nero.smc.client.gui.TreeNode;
import com.p1nero.smc.client.gui.screen.LinkListStreamDialogueScreenBuilder;
import com.p1nero.smc.datagen.lang.SMCLangGenerator;
import com.p1nero.smc.entity.custom.npc.customer.Customer;
import com.p1nero.smc.entity.custom.npc.customer.customer_data.SpecialCustomerData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class SpecialCustomerData20 extends SpecialCustomerData {

    public SpecialCustomerData20() {
        super(20);
    }

    public void generateTranslation(SMCLangGenerator generator) {
        generator.add(nameTranslationKey, "不是很友善的村民");
        generator.add(answerPre(0), "（你感到很不安，这村民似乎有点问题）");
        generator.add(choicePre(0), "这位怕不是坏人");
        generator.add(answerPre(1), "没错！你猜中了！老子就是22位特殊顾客中唯一的反派角色！终于出场了！嚯嚯嚯哈哈哈哈哈哈哈！");
        generator.add(choicePre(1), "所以..?你要吃点什么吗反派先生");
        generator.add(answerPre(2), "吃你奶奶！老子今天就要砸了你的店！接招吧！纳命来！嚯嚯嚯哈哈哈！桀桀桀！");
        generator.add(choicePre(2), "嚯嚯嚯哈哈哈！忍了那么久终于遇到个能打的客户了！");
    }


    @Override
    @OnlyIn(Dist.CLIENT)
    public void getDialogScreen(CompoundTag serverData, LinkListStreamDialogueScreenBuilder screenBuilder, DialogueComponentBuilder dialogueComponentBuilder, boolean canSubmit, int foodScore) {
        screenBuilder.start(answer(0))
                .addChoice(choice(0), answer(1))
                .addChoice(choice(1), answer(2))
                .addFinalChoice(choice(2), (byte) 1);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    protected void append(TreeNode root, CompoundTag serverData, DialogueComponentBuilder dialogueComponentBuilder, boolean canSubmit, int foodLevel) {

    }

    @Override
    public void handle(ServerPlayer serverPlayer, Customer self, byte interactId) {
        if(self.level().getBlockEntity(self.getHomePos()) instanceof MainCookBlockEntity cookBlockEntity){
            cookBlockEntity.summonRandomRaidFor(serverPlayer);
        }
    }
}

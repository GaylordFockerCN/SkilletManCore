package com.p1nero.smc.entity.custom.npc.customer.customer_data.special;

import com.p1nero.smc.client.gui.DialogueComponentBuilder;
import com.p1nero.smc.client.gui.TreeNode;
import com.p1nero.smc.datagen.lang.SMCLangGenerator;
import com.p1nero.smc.entity.custom.npc.customer.Customer;
import com.p1nero.smc.entity.custom.npc.customer.customer_data.SpecialCustomerData;
import com.p1nero.smc.item.SMCItems;
import com.p1nero.smc.registrate.SMCRegistrateItems;
import com.p1nero.smc.util.ItemUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.Items;
import yesman.epicfight.gameasset.EpicFightSounds;

public class SpecialCustomerData4 extends SpecialCustomerData {

    public SpecialCustomerData4() {
        super(4);
    }
    
    @Override
    public void onInteract(ServerPlayer player, Customer self) {
        super.onInteract(player, self);
        self.setVillagerData(self.getVillagerData().setProfession(VillagerProfession.NITWIT));
    }

    public void generateTranslation(SMCLangGenerator generator) {
        super.generateTranslation(generator);
        generator.add(nameTranslationKey, "充满智慧的村民");
        generator.add(answerPre(-2), "未完成的答案如同半部残卷。（智慧的村民一下子就识破了你的诡计，看来得认真对待）");
        generator.add(choicePre(-2), "抱歉，抱歉！");
        generator.add(choicePre(-3), "这就去！");
        generator.add(answerPre(-1), "（眼前这位村民眼神深邃，充满了智慧，你感受到对方没有那么好忽悠）");
        generator.add(choicePre(-1), "客官想品尝何物？");
        generator.add(answerPre(0), "老夫苦思冥想多日，可有启迪心智的 %s ？");
        generator.add(choicePre(0), "恭敬献上");
        generator.add(answerPre(1), "妙哉！此食如智慧之光！知识，与你分享！");
        generator.add(choicePre(1), "收下秘籍");
        generator.add(answerPre(2), "（轻叹）似懂非懂，还需细细琢磨。");
        generator.add(choicePre(2), "告退");
        generator.add(answerPre(3), "此等粗鄙之物，怎入得了智者之口？");
        generator.add(choicePre(3), "在下无知，请恕罪！");
    }

    @Override
    protected void onBest(ServerPlayer serverPlayer, Customer self) {
        super.onBest(serverPlayer, self);
        ItemUtil.addItem(serverPlayer, Items.KNOWLEDGE_BOOK.getDefaultInstance());
        ItemUtil.addItem(serverPlayer, SMCRegistrateItems.SKILL_BOOK_RAFFLE_TICKET.get().getDefaultInstance(), true);

    }

}

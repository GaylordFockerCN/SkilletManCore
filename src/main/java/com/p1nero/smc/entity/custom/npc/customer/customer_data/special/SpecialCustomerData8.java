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
        generator.add(nameTranslationKey, "社交恐惧的村民");
        generator.add(choicePre(-3), "马上做！");
        generator.add(answerPre(-2), "（结结巴巴）这、这个...不、不是我要的...（后退两步）");
        generator.add(choicePre(-2), "那我重新准备。");

        generator.add(answerPre(-1), "（这位村民瑟瑟发抖，眼神惊恐四顾，似乎周围随时有僵尸出现）");
        generator.add(choicePre(-1), "小朋友别怕，想吃点什么？");

        generator.add(answerPre(0), "能、能来点看着就、就安心的 %s 吗？（声音颤抖）");
        generator.add(choicePre(0), "安心呈上");

        generator.add(answerPre(1), "（放松下来）这、这个...真、真的不错！（从怀中掏出一本旧书）这、这本送你，谢、谢谢了！");
        generator.add(choicePre(1), "多谢，告辞");

        generator.add(answerPre(2), "不、不够好...我、我先走了...（转身欲逃）");
        generator.add(choicePre(2), "告辞，下次再见。");

        generator.add(answerPre(3), "（惊恐大叫）啊！怪物！（转身就跑）（你无奈摇头）");
        generator.add(choicePre(3), "罢了，送客。");
    }

    @Override
    protected void onBest(ServerPlayer serverPlayer, Customer self) {
        super.onBest(serverPlayer, self);
        ItemUtil.addItem(serverPlayer, SMCRegistrateItems.SKILL_BOOK_RAFFLE_TICKET.get(), 3, true);
    }

}

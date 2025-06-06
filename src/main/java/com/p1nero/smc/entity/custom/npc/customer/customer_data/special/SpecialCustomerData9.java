package com.p1nero.smc.entity.custom.npc.customer.customer_data.special;

import com.p1nero.smc.SkilletManCoreMod;
import com.p1nero.smc.client.gui.DialogueComponentBuilder;
import com.p1nero.smc.client.gui.TreeNode;
import com.p1nero.smc.datagen.lang.SMCLangGenerator;
import com.p1nero.smc.entity.custom.npc.customer.Customer;
import com.p1nero.smc.entity.custom.npc.customer.customer_data.SpecialCustomerData;
import com.p1nero.smc.item.SMCItems;
import com.p1nero.smc.registrate.SMCRegistrateItems;
import com.p1nero.smc.util.ItemUtil;
import dev.xkmc.cuisinedelight.init.registrate.PlateFood;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.gameevent.GameEvent;

public class SpecialCustomerData9 extends SpecialCustomerData {

    public SpecialCustomerData9() {
        super(9);
    }

    @Override
    public void onInteract(ServerPlayer player, Customer self) {
        self.setOrder(PlateFood.MEAT_PASTA.item.asStack());
        super.onInteract(player, self);
    }

    public void generateTranslation(SMCLangGenerator generator) {
        generator.add(nameTranslationKey, "美食家");
        generator.add(choicePre(-3), "我马上去料理~！");
        generator.add(answerPre(-2), "我等不及了，快拿出来! (无忧无虑的笑容)");
        generator.add(choicePre(-2), " 我马上去料理~");

        generator.add(answerPre(-1), "（眼前这位村民意味深长地说道）人类有三大欲望，而在这三大欲望当中，因为食欲是满足人类生存需求的欲望，所以，满足食欲的行为，在这三者中，优先性是第一位的。如果能在进食的过程中，吃下了美味的食物，也能使人类无比愉快，而在现实生活中，存在着对于这种快感执着追求的人，我便是这样的人，通常人们把我称之为美食家。听说本餐厅，专门为那些厌倦世间常见美食的人，量体裁衣，提供符合他们身份的美食？");
        generator.add(choicePre(-1), "欢迎光临！在本餐厅，可以品尝到世间各种各样的美!");

        generator.add(answerPre(0), "那么，听说这里提供的是我们至今都没吃过的最好的料理... 请给我来份 %s 。");
        generator.add(choicePre(0), "请慢慢享受吧");

        generator.add(answerPre(1), "非常に新鲜で、非常に美味しい！");
        generator.add(choicePre(1), "谢谢");

        generator.add(answerPre(2), "嗯，真是太棒了。(讽刺)");
        generator.add(choicePre(2), "是吗，谢谢。");

        generator.add(answerPre(3), "真讨厌，好恶心...");
        generator.add(choicePre(3), "来吧，不要客气！");
    }

    @Override
    protected void onBest(ServerPlayer serverPlayer, Customer self) {
        super.onBest(serverPlayer, self);

        ItemStack honeyHoney = Items.HONEY_BOTTLE.getDefaultInstance();
        honeyHoney.getOrCreateTag().putBoolean(SkilletManCoreMod.MUL, true);
        honeyHoney.setHoverName(SkilletManCoreMod.getInfo("honey_custom_name"));

        ItemUtil.addItem(serverPlayer, honeyHoney, true);
    }

    @Override
    protected void onBad(ServerPlayer serverPlayer, Customer self) {
        super.onBad(serverPlayer, self);
        serverPlayer.hurt(serverPlayer.damageSources().magic(), 0.5F);
    }

}

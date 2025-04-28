package com.p1nero.smc.entity.custom.npc.customer.customer_data.special;

import com.p1nero.smc.datagen.lang.SMCLangGenerator;
import com.p1nero.smc.entity.custom.npc.customer.Customer;
import com.p1nero.smc.entity.custom.npc.customer.customer_data.SpecialCustomerData;
import com.p1nero.smc.registrate.SMCRegistrateItems;
import com.p1nero.smc.util.ItemUtil;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class SpecialCustomerData13 extends SpecialCustomerData {

    public SpecialCustomerData13() {
        super(13);
    }

    public void generateTranslation(SMCLangGenerator generator) {
        generator.add(nameTranslationKey, "阴阳怪气的村民");
        generator.add(answerPre(-2), "（拖长音调）哎哟～咱们大厨今天手滑了不是～");//(嘲讽MAX)
        generator.add(choicePre(-2), "假笑退下");
        generator.add(choicePre(-3), "马上做！");
        generator.add(answerPre(-1), "（来者不是很友善）");
        generator.add(choicePre(-1), "客官有何需求？");
        generator.add(answerPre(0), "劳驾来份%s——（突然轻笑）不会连这都做不好吧？");//(激将法)
        generator.add(choicePre(0), "冷着脸端上");
        generator.add(answerPre(1), "（假意鼓掌）太阳打西边出来啰～ 赏你的～");//(连秘籍都带刺)
        generator.add(choicePre(1), "呵呵");
        generator.add(answerPre(2), "（啧啧摇头）果然和我预想的一样平庸～");//(早有准备式嘲讽)
        generator.add(choicePre(2), "呵呵");
        generator.add(answerPre(3), "（尖笑）这就是传说中的狗都不吃？（甩袖形成小型龙卷风）");//(武力吐槽)
        generator.add(choicePre(3), "呵呵");
    }

    @Override
    protected void onBest(ServerPlayer serverPlayer, Customer self) {
        super.onBest(serverPlayer, self);
        ItemUtil.addItem(serverPlayer, new ItemStack(SMCRegistrateItems.WEAPON_RAFFLE_TICKET.get(), 4), true);
    }

}

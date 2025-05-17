package com.p1nero.smc.entity.custom.npc.customer.customer_data.special;

import com.p1nero.smc.datagen.lang.SMCLangGenerator;
import com.p1nero.smc.entity.custom.npc.customer.Customer;
import com.p1nero.smc.entity.custom.npc.customer.customer_data.SpecialCustomerData;
import com.p1nero.smc.registrate.SMCRegistrateItems;
import com.p1nero.smc.util.ItemUtil;
import com.simibubi.create.AllBlocks;
import net.minecraft.server.level.ServerPlayer;

public class SpecialCustomerData11 extends SpecialCustomerData {

    public SpecialCustomerData11() {
        super(11);
    }

    public void generateTranslation(SMCLangGenerator generator) {
        generator.add(nameTranslationKey, "傲娇的村民");
        generator.add(choicePre(-3), "马上做！");
        generator.add(answerPre(-2), "（别过脸去）哼！这种东西才不是我要的！");
        generator.add(choicePre(-2), "重新准备");

        generator.add(answerPre(-1), "（面无表情，但眼神偷偷打量）");
        generator.add(choicePre(-1), "客官要尝尝新做的点心吗？");

        generator.add(answerPre(0), "（轻哼）要吃就来点...（小声） %s 。");
        generator.add(choicePre(0), "恭敬呈上");

        generator.add(answerPre(1), "（脸红）还、还算可以吧...这玩意儿送你，别让它沾上俗气。");
        generator.add(choicePre(1), "（笑着收下）");

        generator.add(answerPre(2), "（撇嘴）勉勉强强...再努力点吧你。");
        generator.add(choicePre(2), "下次一定！");

        generator.add(answerPre(3), "（假装生气）这种质量也敢拿出来？！（其实真的很失望）");
        generator.add(choicePre(3), "（无奈）下次一定！");
    }

    @Override
    protected void onBest(ServerPlayer serverPlayer, Customer self) {
        super.onBest(serverPlayer, self);
        ItemUtil.addItem(serverPlayer, AllBlocks.CUCKOO_CLOCK.asStack(), true);
    }

}

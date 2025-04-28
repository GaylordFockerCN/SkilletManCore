package com.p1nero.smc.entity.custom.npc.customer.customer_data.special;

import com.p1nero.smc.datagen.lang.SMCLangGenerator;
import com.p1nero.smc.entity.custom.npc.customer.Customer;
import com.p1nero.smc.entity.custom.npc.customer.customer_data.SpecialCustomerData;
import com.p1nero.smc.registrate.SMCRegistrateItems;
import com.p1nero.smc.util.ItemUtil;
import net.minecraft.server.level.ServerPlayer;
import yesman.epicfight.gameasset.EpicFightSounds;

public class SpecialCustomerData21 extends SpecialCustomerData {

    public SpecialCustomerData21() {
        super(21);
    }

    public void generateTranslation(SMCLangGenerator generator) {
        super.generateTranslation(generator);
        generator.add(nameTranslationKey, "(*^▽^*)的村民");
        generator.add(answerPre(-2), "[・_・?]    ヾ(⌐ ■_■)");
        generator.add(choicePre(-2), "看来这并非它想要的");
        generator.add(choicePre(-3), "马上做！");
        generator.add(answerPre(-1), "（眼前这位村民看起来...(◕ᴗ◕✿) 它就这样看着你） ");
        generator.add(choicePre(-1), "要来点什么吗ヽ(•ω•。)ノ？");
        generator.add(answerPre(0), "٩(๑❛ᴗ❛๑)۶ %s ");
        generator.add(choicePre(0), "给你(*´▽｀)ノノ");
        generator.add(answerPre(1), "٩(๑>◡<๑)۶ ");
        generator.add(choicePre(1), "看起来它很喜欢这道菜✧*｡٩(ˊᗜˋ*)و✧*｡");
        generator.add(answerPre(2), "(눈‸눈)");
        generator.add(choicePre(2), "看起来它觉得这道菜一般(￣ω￣;)");
        generator.add(answerPre(3), "(╯°Д°)╯︵┻━┻");
        generator.add(choicePre(3), "看起来它很讨厌这道菜(⊙_⊙)");
    }

    @Override
    protected void onBest(ServerPlayer serverPlayer, Customer self) {
        super.onBest(serverPlayer, self);
        ItemUtil.addItem(serverPlayer, SMCRegistrateItems.DOLL_RAFFLE_TICKET.asItem(), 5, true);
        ItemUtil.addItem(serverPlayer, SMCRegistrateItems.PET_RAFFLE_TICKET.asItem(), 3, true);
        ItemUtil.addItem(serverPlayer, SMCRegistrateItems.DISC_RAFFLE_TICKET.asItem(), 1, true);
    }

    @Override
    protected void onBad(ServerPlayer serverPlayer, Customer self) {
        super.onBad(serverPlayer, self);
        serverPlayer.setSilent(false);
        serverPlayer.playSound(EpicFightSounds.BLUNT_HIT.get());
    }
}

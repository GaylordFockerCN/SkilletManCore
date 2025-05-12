package com.p1nero.smc.entity.custom.npc.customer.customer_data.special;

import com.p1nero.smc.datagen.lang.SMCLangGenerator;
import com.p1nero.smc.entity.custom.npc.customer.Customer;
import com.p1nero.smc.entity.custom.npc.customer.customer_data.SpecialCustomerData;
import com.p1nero.smc.registrate.SMCRegistrateItems;
import com.p1nero.smc.util.ItemUtil;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;

public class SpecialCustomerData1 extends SpecialCustomerData {

    public SpecialCustomerData1() {
        super(1);
    }

    @Override
    public void generateTranslation(SMCLangGenerator generator) {
        super.generateTranslation(generator);
        generator.add(nameTranslationKey, "内力雄厚的村民");//客户名称，统一用XXX的客户，XXX可以代表性格外貌等等
        generator.add(answerPre(-2), "（你的菜还没有做好，如果胡乱呈上的话怕不是惹不起这位高人...还是先做菜吧）");//企图提交其他物品时的对话
        generator.add(choicePre(-2), "好吧");//企图提交其他物品时的对话的回答
        generator.add(choicePre(-3), "这就去！");//企图提交其他物品时的对话的回答
        generator.add(answerPre(-1), "（你感受到面前的这位客人有一股强大的气场）");//对面前顾客的描述
        generator.add(choicePre(-1), "久仰久仰！客官吃些什么？");//对面前顾客的邀请
        generator.add(answerPre(0), "老夫闭关三日，可有补气益元的珍膳 %s ？");//顾客的请求，一定要保留%s，不能带有具体食物，只能带修饰词。
        generator.add(choicePre(0), "恭敬呈上");//玩家的提交选项
        generator.add(answerPre(1), "（捋须长笑）此味通达任督二脉！我看你骨骼惊奇，此秘笈赠与你罢！去也！");//高品质的时候顾客的回答。赠礼可以是秘笈或神兵
        generator.add(choicePre(1), "恭敬收下");//高品质的时候顾客回答后 的玩家选项
        generator.add(answerPre(2), "（摇头）火候尚欠一甲子修为");//中品质的时候顾客的回答
        generator.add(choicePre(2), "（这老登真不识货）");//中品质的时候顾客回答后 的玩家选项
        generator.add(answerPre(3), "（震碎碗碟）岂敢拿猪食欺我！（虽然你是平底锅侠，但设定上你是行侠仗义之人，可不能和普通人打起来，于是你只能求饶）");//低品质的时候顾客的回答
        generator.add(choicePre(3), "妈妈救我！");//低品质的时候顾客回答后 的玩家选项
    }

    @Override
    protected void onBest(ServerPlayer serverPlayer, Customer self) {
        super.onBest(serverPlayer, self);
        ItemUtil.addItem(serverPlayer, SMCRegistrateItems.SKILL_BOOK_RAFFLE_TICKET.get(), 3, true);

    }

    @Override
    protected void onBad(ServerPlayer serverPlayer, Customer self) {
        super.onBad(serverPlayer, self);
        serverPlayer.playSound(SoundEvents.GLASS_BREAK);
        serverPlayer.hurt(serverPlayer.damageSources().magic(), 0.5F);
    }
}

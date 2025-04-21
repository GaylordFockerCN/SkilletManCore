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

public class SpecialCustomerData16 extends SpecialCustomerData {

    public SpecialCustomerData16() {
        super(16);
    }

    public void generateTranslation(SMCLangGenerator generator) {
        generator.add(nameTranslationKey, "多疑的村民");
        generator.add(answerPre(-2), "（眼神飘忽，凑近食物嗅了嗅）这东西...您确定没在里面放奇怪的草药？（突然又后退两步，眼神警惕）我上周在草药师那儿听到，有种草药能让人失忆！您不会想对我做什么吧？");
        generator.add(choicePre(-2), "重新选择正确食物");
        generator.add(choicePre(-3), "这就去！");

        generator.add(answerPre(-1), "（眼前这位村民一直偷偷观察你的动作，仿佛你是个大盗）");
        generator.add(choicePre(-1), "客官要点啥？");

        generator.add(answerPre(0), "有没有...（环顾四周，压低声音）吃了不会被人下毒的 %s ？我上次在酒馆就中了招...（突然警觉地环顾四周）您说这是不是有人在暗中监视我？");
        generator.add(choicePre(0), "呈上");

        generator.add(answerPre(1), "（突然警惕地观察四周）嗯...还行吧...送你这个，保个平安。");
        generator.add(choicePre(1), "（无语）多谢，告辞。");

        generator.add(answerPre(2), "（皱眉）这东西...（又凑近闻了闻）感觉不太对劲...你确定没放奇怪的东西？我上周在东街看到个流浪汉，吃了不知道哪儿来的食物，结果在街边跳了一下午的舞！（突然警觉地环顾四周）您说这是不是有人故意为之？");
        generator.add(choicePre(2), "下次一定改进");

        generator.add(answerPre(3), "（突然暴跳如雷）你这厨子是不是有病？！（突然又警惕地环顾四周）你不会联合我家那位村民来陷害我吧？！走也走也！（夺门而出）");
        generator.add(choicePre(3), "慢走慢走！我不是故意做那么难吃的！");
    }

    @Override
    protected void onBest(ServerPlayer serverPlayer, Customer self) {
        super.onBest(serverPlayer, self);
        ItemUtil.addItem(serverPlayer, SMCRegistrateItems.SKILL_BOOK_RAFFLE_TICKET.get(), 5);
    }

    @Override
    protected void onBad(ServerPlayer serverPlayer, Customer self) {
        super.onBad(serverPlayer, self);
    }
}

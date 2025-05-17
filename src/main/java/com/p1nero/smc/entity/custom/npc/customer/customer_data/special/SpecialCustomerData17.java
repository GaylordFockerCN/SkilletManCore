package com.p1nero.smc.entity.custom.npc.customer.customer_data.special;

import com.p1nero.smc.datagen.lang.SMCLangGenerator;
import com.p1nero.smc.entity.custom.npc.customer.Customer;
import com.p1nero.smc.entity.custom.npc.customer.customer_data.SpecialCustomerData;
import com.p1nero.smc.registrate.SMCRegistrateItems;
import com.p1nero.smc.util.BookManager;
import com.p1nero.smc.util.ItemUtil;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class SpecialCustomerData17 extends SpecialCustomerData {

    public SpecialCustomerData17() {
        super(17);
    }

    public void generateTranslation(SMCLangGenerator generator) {
        generator.add(nameTranslationKey, "富有诗性的村民");
        generator.add(answerPre(-2), "（摇头晃脑）这食物...缺了灵魂，像一朵未绽放就被摘下的玫瑰。");
        generator.add(choicePre(-2), "选择正确食物");
        generator.add(choicePre(-3), "这就去！");

        generator.add(answerPre(-1), "（面前这位村民眼神迷离，仿佛在云端写诗）");
        generator.add(choicePre(-1), "客官要点啥？");

        generator.add(answerPre(0), "能否赐我一道...（突然单膝跪地）能与月光共舞、和诗篇齐飞的 %s ？");
        generator.add(choicePre(0), "呈上！");

        generator.add(answerPre(1), "（突然热泪盈眶）我找到了灵感！纯美女神眷顾于你！（掏出一本诗集）这本《食物之歌》送你，谢你的杰作。");
        generator.add(choicePre(1), "（尴尬收下）");

        generator.add(answerPre(2), "（皱眉）少了灵魂...像被霜打的诗篇，失去了温度...");
        generator.add(choicePre(2), "下次一定");

        generator.add(answerPre(3), "（惊恐）这是...黑暗料理之诗！（突然开始朗诵）哦！灾难的味蕾在舌尖绽放！（夺门而出）");
        generator.add(choicePre(3), "下次一定！");
    }

    @Override
    protected void onBest(ServerPlayer serverPlayer, Customer self) {
        super.onBest(serverPlayer, self);
        ItemUtil.addItem(serverPlayer, SMCRegistrateItems.DISC_RAFFLE_TICKET.get(), 2, true);
        ItemUtil.addItem(serverPlayer, SMCRegistrateItems.SKILL_BOOK_RAFFLE_TICKET.get(), 2, true);

        ItemStack book = BookManager.getDefaultTextBook(1, "富有诗性的村民", "《食物之歌·卷一》"
                , "【豆腐谣】\\n黄豆化玉凝清光\\n白刃千击柔克刚\\n箸尖一点山河震\\n笑问宗师可敢尝？"
                , "【叫花鸡赋】\\n黄泥为甲火为裳\\n腹藏乾坤百味香\\n撕得金甲三千片\\n方知江湖即酒囊"
                , "【东坡肉颂】\\n慢火熬得琥珀光\\n肥而不腻即文章\\n箸下沉浮三十载\\n方悟此味是柔刚"
                , "【青团行】\\n艾草裹住江南春\\n一口咬出剑气横\\n若问清明何处好\\n剑客坟前酒尚温");

        ItemUtil.addItem(serverPlayer, book, true);
    }

}

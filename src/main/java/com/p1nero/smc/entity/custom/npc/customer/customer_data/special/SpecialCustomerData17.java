package com.p1nero.smc.entity.custom.npc.customer.customer_data.special;

import com.p1nero.smc.datagen.lang.SMCLangGenerator;
import com.p1nero.smc.entity.custom.npc.customer.Customer;
import com.p1nero.smc.entity.custom.npc.customer.customer_data.SpecialCustomerData;
import com.p1nero.smc.registrate.SMCRegistrateItems;
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
        generator.add(choicePre(1), "（尴尬笑着收下）多谢，告辞。");

        generator.add(answerPre(2), "（皱眉）少了灵魂...像被霜打的诗篇，失去了温度...");
        generator.add(choicePre(2), "下次一定改进");

        generator.add(answerPre(3), "（惊恐）这是...黑暗料理之诗！（突然开始朗诵）哦！灾难的味蕾在舌尖绽放！（夺门而出）");
        generator.add(choicePre(3), "送客，下次一定！");
    }

    @Override
    protected void onBest(ServerPlayer serverPlayer, Customer self) {
        super.onBest(serverPlayer, self);
        ItemUtil.addItem(serverPlayer, SMCRegistrateItems.DISC_RAFFLE_TICKET.get(), 2, true);
        ItemUtil.addItem(serverPlayer, SMCRegistrateItems.SKILL_BOOK_RAFFLE_TICKET.get(), 2, true);

        ItemStack book = new ItemStack(Items.WRITTEN_BOOK);
        ListTag bookPages = new ListTag();

        bookPages.add(StringTag.valueOf("""
                【豆腐谣】
                黄豆化玉凝清光
                白刃千击柔克刚
                箸尖一点山河震
                笑问宗师可敢尝？
                """));

        bookPages.add(StringTag.valueOf("""
                【叫花鸡赋】
                黄泥为甲火为裳
                腹藏乾坤百味香
                撕得金甲三千片
                方知江湖即酒囊
                """));

        bookPages.add(StringTag.valueOf("""
                【东坡肉颂】
                慢火熬得琥珀光
                肥而不腻即文章
                箸下沉浮三十载
                方悟此味是柔刚
                """));

        bookPages.add(StringTag.valueOf("""
                【青团行】
                艾草裹住江南春
                一口咬出剑气横
                若问清明何处好
                剑客坟前酒尚温
                """));

        book.addTagElement("pages", bookPages);//页数
        book.addTagElement("generation", IntTag.valueOf(1));//破损度
        book.addTagElement("author", StringTag.valueOf("富有诗性的村民"));
        book.addTagElement("title", StringTag.valueOf("《食物之歌·卷一》"));

        ItemUtil.addItem(serverPlayer, book, true);
    }

}

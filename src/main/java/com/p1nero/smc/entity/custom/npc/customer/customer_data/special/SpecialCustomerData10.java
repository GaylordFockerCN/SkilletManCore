package com.p1nero.smc.entity.custom.npc.customer.customer_data.special;

import com.p1nero.smc.client.gui.DialogueComponentBuilder;
import com.p1nero.smc.client.gui.TreeNode;
import com.p1nero.smc.datagen.lang.SMCLangGenerator;
import com.p1nero.smc.entity.custom.npc.customer.Customer;
import com.p1nero.smc.entity.custom.npc.customer.customer_data.SpecialCustomerData;
import com.p1nero.smc.item.SMCItems;
import com.p1nero.smc.util.ItemUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class SpecialCustomerData10 extends SpecialCustomerData {

    public SpecialCustomerData10() {
        super(10);
    }

    public void generateTranslation(SMCLangGenerator generator) {
        generator.add(nameTranslationKey, "守财的村民");
        generator.add(choicePre(-3), "马上做！");

        generator.add(answerPre(-2), "（推推眼镜，精打细算地）这个？不不不，这个太浪费了！要节约，节约...");
        generator.add(choicePre(-2), "（这老头比我还抠）");

        generator.add(answerPre(-1), "（紧紧抱着钱袋，眼神警惕，看起来很怕你的食物夺走了他的财产。）");
        generator.add(choicePre(-1), "客官要看菜单吗？");

        generator.add(answerPre(0), "有没有...（压低声音）能用最少食材做出最多食物的 %s ？");
        generator.add(choicePre(0), "提交");

        generator.add(answerPre(1), "（眼镜闪过精光）嗯，还算可以接受...（从怀里掏出一本破旧的省钱秘籍）送你这个，《节俭之道》，好好学学。");
        generator.add(choicePre(1), "（表面道谢，内心吐槽）多谢，告辞。");

        generator.add(answerPre(2), "（皱眉）浪费，太浪费了...食材没利用好...");
        generator.add(choicePre(2), "下次一定改进");

        generator.add(answerPre(3), "（暴跳如雷）你这是在浪费粮食！你知道这能做多少便宜料理吗？！（你慌忙道歉）");
        generator.add(choicePre(3), "（心虚）下次一定注意，告辞。");
    }

    @Override
    protected void onBest(ServerPlayer serverPlayer, Customer self) {
        super.onBest(serverPlayer, self);
        ItemUtil.addItem(serverPlayer, SMCItems.SKILL_BOOK_RAFFLE_TICKET.get().getDefaultInstance());

        ItemStack book = new ItemStack(Items.WRITTEN_BOOK);
        ListTag bookPages = new ListTag();

        bookPages.add(StringTag.valueOf("节俭的首要在于节约墨水，"));

        book.addTagElement("pages", bookPages);//页数
        book.addTagElement("generation", IntTag.valueOf(3));//破损度
        book.addTagElement("author", StringTag.valueOf("节俭的村民"));
        book.addTagElement("title", StringTag.valueOf("节俭之道"));

        ItemUtil.addItem(serverPlayer, book);
    }

    @Override
    protected void onBad(ServerPlayer serverPlayer, Customer self) {
        super.onBad(serverPlayer, self);
        serverPlayer.hurt(serverPlayer.damageSources().magic(), 0.5F);
    }

}

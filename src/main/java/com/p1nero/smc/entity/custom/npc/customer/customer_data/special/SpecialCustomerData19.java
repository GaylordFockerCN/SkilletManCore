package com.p1nero.smc.entity.custom.npc.customer.customer_data.special;

import com.p1nero.smc.client.gui.DialogueComponentBuilder;
import com.p1nero.smc.client.gui.TreeNode;
import com.p1nero.smc.datagen.lang.SMCLangGenerator;
import com.p1nero.smc.entity.custom.npc.customer.Customer;
import com.p1nero.smc.entity.custom.npc.customer.customer_data.SpecialCustomerData;
import com.p1nero.smc.item.SMCItems;
import com.p1nero.smc.registrate.SMCRegistrateItems;
import com.p1nero.smc.util.BookManager;
import com.p1nero.smc.util.ItemUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import reascer.wom.world.item.WOMItems;

public class SpecialCustomerData19 extends SpecialCustomerData {

    public SpecialCustomerData19() {
        super(19);
    }

    public void generateTranslation(SMCLangGenerator generator) {
        generator.add(nameTranslationKey, "穿着破烂的村民");
        generator.add(choicePre(-3), "马上做！");
        generator.add(answerPre(-2), "叫花子也分得清残羹剩饭！");
        generator.add(choicePre(-2), "替换正确的物品");
        generator.add(answerPre(-1), "（面前这位村民衣冠不整，穿着十分破烂。但似乎有种强大的气场，还是认真对待吧）");
        generator.add(choicePre(-1), "客官要来些什么？");
        generator.add(answerPre(0), "最近练功到紧要处，需带刚猛气的 %s 补补身子！（怎么mc的世界还有习武的村民...）");
        generator.add(choicePre(0), "呈上");
        generator.add(answerPre(1), "（龙吟隐隐）痛快！这秘笈与盔甲予你防身！");
        generator.add(choicePre(1), "小的有眼不识泰山！");
        generator.add(answerPre(2), "比马厩草料还糙！");
        generator.add(choicePre(2), "抱歉抱歉！");
        generator.add(answerPre(3), "欺我帮无人？！做这等狗食！（看来你做的十分难吃，其实狗都不食，不信你可以喂看看）");
        generator.add(choicePre(3), "饶命！饶命！");
    }

    @Override
    protected void onBest(ServerPlayer serverPlayer, Customer self) {
        super.onBest(serverPlayer, self);
        ItemUtil.addItem(serverPlayer, SMCRegistrateItems.SKILL_BOOK_RAFFLE_TICKET.get(), 10, true);
        ItemUtil.addItem(serverPlayer, SMCRegistrateItems.WEAPON_RAFFLE_TICKET.get(), 10, true);

        ItemStack book = BookManager.getDefaultTextBook(3, "洪七", "降龍十八掌"
                , " 第一式：見龍在田\\n" +
                        "    /\\\\_/\\\\\\n" +
                        "    ( o.o )s\\n" +
                        "==>===|~"
                , "第二式: 飛龍在天\\n" +
                        "    ^\\n" +
                        "   / \\\\\\n" +
                        "~O=====>\\n" +
                        "\\\\_/"
                , "第三式: 龍戰於野\\n" +
                        "  <><>s\\n" +
                        "{|==|}  ~~~*\\n" +
                        " </\\\\>"
                , "收勢: 神龍擺尾\\n" +
                        "  \\\\||/\\n" +
                        "  ~@~\\n" +
                        "--<~~*s");

        ItemUtil.addItem(serverPlayer, book, true);

    }

    @Override
    protected void onBad(ServerPlayer serverPlayer, Customer self) {
        super.onBad(serverPlayer, self);
        ItemUtil.addItem(serverPlayer, Items.WOLF_SPAWN_EGG.getDefaultInstance(), true);
    }
}

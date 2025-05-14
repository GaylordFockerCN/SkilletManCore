package com.p1nero.smc.item.custom;

import com.p1nero.smc.SkilletManCoreMod;
import com.p1nero.smc.client.gui.screen.DialogueScreen;
import com.p1nero.smc.client.gui.screen.LinkListStreamDialogueScreenBuilder;
import com.p1nero.smc.datagen.lang.SMCLangGenerator;
import com.p1nero.smc.registrate.SMCRegistrateItems;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class CookGuideBookItem extends SimpleDescriptionFoilItem {
    public CookGuideBookItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player p_41433_, @NotNull InteractionHand p_41434_) {
        if(level.isClientSide){
            LinkListStreamDialogueScreenBuilder builder = new LinkListStreamDialogueScreenBuilder(null, this.getDescription().copy().withStyle(ChatFormatting.LIGHT_PURPLE));
            builder.start(ans(0))
                    .addChoice(opt(1), ans(2))
                    .thenExecute((screen -> screen.setPicture(new ResourceLocation(SkilletManCoreMod.MOD_ID, "textures/gui/cook_guide_book/cook_guide_2.png"))))
                    .addChoice(opt(1), ans(3))
                    .thenExecute((screen -> screen.setPicture(new ResourceLocation(SkilletManCoreMod.MOD_ID, "textures/gui/cook_guide_book/cook_guide_3.png"))))
                    .addChoice(opt(1), ans(4))
                    .thenExecute((screen -> screen.setPicture(new ResourceLocation(SkilletManCoreMod.MOD_ID, "textures/gui/cook_guide_book/cook_guide_4.png"))))
                    .addChoice(opt(1), ans(5))
                    .thenExecute((screen -> screen.setPicture(new ResourceLocation(SkilletManCoreMod.MOD_ID, "textures/gui/cook_guide_book/cook_guide_5.png"))))
                    .addChoice(opt(1), ans(6))
                    .thenExecute((screen -> screen.setPicture(new ResourceLocation(SkilletManCoreMod.MOD_ID, "textures/gui/cook_guide_book/cook_guide_6.png"))))
                    .addChoice(opt(1), ans(7))
                    .thenExecute((screen -> screen.setPicture(new ResourceLocation(SkilletManCoreMod.MOD_ID, "textures/gui/cook_guide_book/cook_guide_7.png"))))
                    .addChoice(opt(1), ans(8))
                    .thenExecute((screen -> screen.setPicture(new ResourceLocation(SkilletManCoreMod.MOD_ID, "textures/gui/cook_guide_book/cook_guide_8.png"))))
                    .addFinalChoice(opt(9));
            DialogueScreen screen = builder.build();
            screen.setPicture(new ResourceLocation(SkilletManCoreMod.MOD_ID, "textures/gui/cook_guide_book/cook_guide_1.png"));
            Minecraft.getInstance().setScreen(builder.build());
        }
        return super.use(level, p_41433_, p_41434_);
    }

    private Component ans(int i) {
        return Component.literal("\n").append(Component.translatable(this.getDescriptionId() + "_guide_" + i)).withStyle(ChatFormatting.WHITE);
    }

    private Component opt(int i) {
        return Component.translatable(this.getDescriptionId() + "_guide_" + i);
    }

    private String key(int i) {
        return this.getDescriptionId() + "_guide_" + i;
    }

    public static void addTranslation(SMCLangGenerator generator){
        generator.add(SMCRegistrateItems.COOK_GUIDE_BOOK_ITEM.get().key(0), "在§aJEI§r（物品栏右下方）中搜索对应菜品，点击即可查看所需食材，注意看食材下方提示的§6§l比例！§r每个种类的食材的份量相加，再除以总份量，即为食材占比。不同占比将烹饪出不同料理。");
        generator.add(SMCRegistrateItems.COOK_GUIDE_BOOK_ITEM.get().key(1), "下一页");
        generator.add(SMCRegistrateItems.COOK_GUIDE_BOOK_ITEM.get().key(2), "一些食材需要切后才能下锅，将食材放在副手，主手拿§6刀§r，对着§6砧板§r右键即可切之");
        generator.add(SMCRegistrateItems.COOK_GUIDE_BOOK_ITEM.get().key(3), "对于左侧的刻度，§a绿色§r表示未煮熟，§e黄色§r表示煮熟刚刚好，§c红色§r表示煮过头 对于右侧的刻度，§a绿色§r表示未烧焦，§c红色§r表示烧焦");
        generator.add(SMCRegistrateItems.COOK_GUIDE_BOOK_ITEM.get().key(4), "对于§6混合菜品§r（由两种以上食材烹饪而成），下锅时应§6先下§r[最短烹饪时间]较长的物品，其次再下[最短烹饪时间]较短的物品，以免烧焦");
        generator.add(SMCRegistrateItems.COOK_GUIDE_BOOK_ITEM.get().key(5), "使用锅铲对着炒锅右键以翻炒食材，翻炒将重置右边的刻度。 使用食材对着炒锅右键或将食材丢入炒锅上方以添加食材。交易结算时的§6份量加成§r不容忽视。");
        generator.add(SMCRegistrateItems.COOK_GUIDE_BOOK_ITEM.get().key(6), "使用盘子对着炒锅右键以盛出");
        generator.add(SMCRegistrateItems.COOK_GUIDE_BOOK_ITEM.get().key(7), "当没有食材时，可直接向§b小助手§r快速订购食材，不过小助手提供的食材有限");
        generator.add(SMCRegistrateItems.COOK_GUIDE_BOOK_ITEM.get().key(8), "将出锅的食物置于主手，对着客户村民右键对话以完成交易。菜品份量，所用的食材丰富度以及品质等级都可以提升报酬！");
        generator.add(SMCRegistrateItems.COOK_GUIDE_BOOK_ITEM.get().key(9), "合上");
    }

}

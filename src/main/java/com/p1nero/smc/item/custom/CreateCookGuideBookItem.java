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

public class CreateCookGuideBookItem extends Item {
    public CreateCookGuideBookItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player p_41433_, @NotNull InteractionHand p_41434_) {
        if(level.isClientSide){
            LinkListStreamDialogueScreenBuilder builder = new LinkListStreamDialogueScreenBuilder(null, this.getDescription().copy().withStyle(ChatFormatting.LIGHT_PURPLE));
            builder.start(ans(0))
                    .addChoice(opt(1), ans(2))
                    .thenExecute((screen -> screen.setPicture(new ResourceLocation(SkilletManCoreMod.MOD_ID, "textures/gui/create_cook_guide_book/2.png"))))
                    .addChoice(opt(1), ans(3))
                    .thenExecute((screen -> screen.setPicture(new ResourceLocation(SkilletManCoreMod.MOD_ID, "textures/gui/create_cook_guide_book/3.png"))))
                    .addChoice(opt(1), ans(4))
                    .thenExecute((screen -> screen.setPicture(new ResourceLocation(SkilletManCoreMod.MOD_ID, "textures/gui/create_cook_guide_book/4.png"))))
                    .addChoice(opt(1), ans(5))
                    .thenExecute((screen -> screen.setPicture(new ResourceLocation(SkilletManCoreMod.MOD_ID, "textures/gui/create_cook_guide_book/5.png"))))
                    .addFinalChoice(opt(6));
            DialogueScreen screen = builder.build();
            screen.setPicture(new ResourceLocation(SkilletManCoreMod.MOD_ID, "textures/gui/create_cook_guide_book/1.png"));
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
        generator.add(SMCRegistrateItems.CREATE_COOK_GUIDE_BOOK_ITEM.get().key(0), "§6机械手§r是机械动力结合料理乐事不可或缺的一环，它将模拟玩家炒菜。将§a锅铲§r置于机械手上，并§c输入动力§r，它便可以模拟我们翻炒！");
        generator.add(SMCRegistrateItems.CREATE_COOK_GUIDE_BOOK_ITEM.get().key(1), "下一页");
        generator.add(SMCRegistrateItems.CREATE_COOK_GUIDE_BOOK_ITEM.get().key(2), "同理我们可以使用§6机械手§r模拟盘子取食物，上下行过滤器设置为盘子即可，如此一来只有盘子能输入机械手，只有非盘子（即烹饪好的料理）能输出机械手。");
        generator.add(SMCRegistrateItems.CREATE_COOK_GUIDE_BOOK_ITEM.get().key(3), "§6机械手§r遇到§c红石信号§r时会暂停工作。利用红石信号控制机械手，来计算最适合的取出时间吧！");
        generator.add(SMCRegistrateItems.CREATE_COOK_GUIDE_BOOK_ITEM.get().key(4), "本整合包使炒锅在工作且未满时可以像漏斗一样接收它上方的物品，使输入方式更加多样化！因此你可以使用传送带传送食物，当然，用机械手模拟玩家摆放食物也是可行的。");
        generator.add(SMCRegistrateItems.CREATE_COOK_GUIDE_BOOK_ITEM.get().key(5), "现在，该动用你的大脑制作一台自动炒菜机啦！ 本整合包自带了由§6JackNeksa§r大大所制作的炒菜机蓝图提供借鉴。");
        generator.add(SMCRegistrateItems.CREATE_COOK_GUIDE_BOOK_ITEM.get().key(6), "合上");
    }

}

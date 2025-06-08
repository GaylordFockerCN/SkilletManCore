package com.p1nero.smc.item.custom;

import com.p1nero.smc.SkilletManCoreMod;
import com.p1nero.smc.client.gui.screen.DialogueScreen;
import com.p1nero.smc.client.gui.screen.LinkListStreamDialogueScreenBuilder;
import com.p1nero.smc.datagen.lang.SMCLangGenerator;
import com.p1nero.smc.network.packet.serverbound.NpcPlayerInteractPacket;
import com.p1nero.smc.registrate.SMCRegistrateItems;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

public class CookGuideBookItem extends SimpleDescriptionFoilItem {
    public CookGuideBookItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand p_41434_) {
        if(level.isClientSide){
            this.showScreen();
        }
        return super.use(level, player, p_41434_);
    }

    @OnlyIn(Dist.CLIENT)
    private void showScreen(){
        LinkListStreamDialogueScreenBuilder builder = new LinkListStreamDialogueScreenBuilder(null, this.getDescription().copy().withStyle(ChatFormatting.LIGHT_PURPLE));
        builder.start(ans(0))
                .addChoice(opt(1, "1/9"), ans(2))
                .thenExecute((screen -> screen.setPicture(ResourceLocation.fromNamespaceAndPath(SkilletManCoreMod.MOD_ID, "textures/gui/cook_guide_book/2.png"))))
                .addChoice(opt(1, "2/9"), ans(3))
                .thenExecute((screen -> screen.setPicture(ResourceLocation.fromNamespaceAndPath(SkilletManCoreMod.MOD_ID, "textures/gui/cook_guide_book/3.png"))))
                .addChoice(opt(1, "3/9"), ans(4))
                .thenExecute((screen -> screen.setPicture(ResourceLocation.fromNamespaceAndPath(SkilletManCoreMod.MOD_ID, "textures/gui/cook_guide_book/4.png"))))
                .addChoice(opt(1, "4/9"), ans(5))
                .addChoice(opt(1, "5/9"), ans(6))
                .thenExecute((screen -> screen.setPicture(ResourceLocation.fromNamespaceAndPath(SkilletManCoreMod.MOD_ID, "textures/gui/cook_guide_book/5.png"))))
                .addChoice(opt(1, "6/9"), ans(7))
                .thenExecute((screen -> screen.setPicture(ResourceLocation.fromNamespaceAndPath(SkilletManCoreMod.MOD_ID, "textures/gui/cook_guide_book/5_2.png"))))
                .addChoice(opt(1, "7/9"), ans(8))
                .thenExecute((screen -> screen.setPicture(ResourceLocation.fromNamespaceAndPath(SkilletManCoreMod.MOD_ID, "textures/gui/cook_guide_book/6.png"))))
                .addChoice(opt(1, "8/9"), ans(9))
                .thenExecute((screen -> screen.setPicture(ResourceLocation.fromNamespaceAndPath(SkilletManCoreMod.MOD_ID, "textures/gui/cook_guide_book/7.png"))))
                .addChoice(opt(1, "9/9"), ans(10))
                .thenExecute((screen -> screen.setPicture(ResourceLocation.fromNamespaceAndPath(SkilletManCoreMod.MOD_ID, "textures/gui/cook_guide_book/8.png"))))
                .addFinalChoice(opt(12), NpcPlayerInteractPacket.SET_HARD_SPATULA)
                .addFinalChoice(opt(13), NpcPlayerInteractPacket.SET_EAZY_SPATULA)
                .addFinalChoice(opt(11));
        DialogueScreen screen = builder.build();
        screen.setPicture(ResourceLocation.fromNamespaceAndPath(SkilletManCoreMod.MOD_ID, "textures/gui/cook_guide_book/1.png"));
        Minecraft.getInstance().setScreen(builder.build());
    }

    private Component ans(int i) {
        return Component.literal("\n").append(Component.translatable(this.getDescriptionId() + "_guide_" + i)).withStyle(ChatFormatting.WHITE);
    }

    private Component opt(int i, Object... params) {
        return Component.translatable(this.getDescriptionId() + "_guide_" + i, params);
    }

    private String key(int i) {
        return this.getDescriptionId() + "_guide_" + i;
    }

    public static void addTranslation(SMCLangGenerator generator){
        generator.add(SMCRegistrateItems.COOK_GUIDE_BOOK_ITEM.get().key(0), "在§aJEI§r（物品栏右下方）中搜索对应菜品，点击即可查看所需食材，注意看食材下方提示的§6§l比例！§r每个种类的食材的§a份量§r相加，再除以总份量，即为食材占比。不同占比将烹饪出不同料理。");
        generator.add(SMCRegistrateItems.COOK_GUIDE_BOOK_ITEM.get().key(1), "下一页 %s");
        generator.add(SMCRegistrateItems.COOK_GUIDE_BOOK_ITEM.get().key(2), "一些食材需要使用§6砧板§r加工后才能下锅。将食材放在副手，主手拿§6刀§r，对着§6砧板§r右键即可切之");
        generator.add(SMCRegistrateItems.COOK_GUIDE_BOOK_ITEM.get().key(3), "对于左侧的刻度，§a绿色§r表示未煮熟，§e黄色§r表示煮熟刚刚好，§c红色§r表示煮过头 对于右侧的刻度，§a绿色§r表示未烧焦，§c红色§r表示烧焦");
        generator.add(SMCRegistrateItems.COOK_GUIDE_BOOK_ITEM.get().key(4), "使用锅铲对着炒锅右键以翻炒食材，翻炒将重置右边仪表盘的刻度。在困难翻炒模式下只有在锅铲位于绿色部分的时候才能进行翻炒，在本书结尾可选择是否启用此模式。");
        generator.add(SMCRegistrateItems.COOK_GUIDE_BOOK_ITEM.get().key(5), "使用食材对着炒锅右键或将食材丢入炒锅上方以添加食材。交易结算时的§6份量加成§r不容忽视。");
        generator.add(SMCRegistrateItems.COOK_GUIDE_BOOK_ITEM.get().key(6), "若锅内所有食材都抵达最长烹饪时间，则食材将碳化。");
        generator.add(SMCRegistrateItems.COOK_GUIDE_BOOK_ITEM.get().key(7), "对于§6混合菜品§r（由两种以上食材烹饪而成），下锅时应§6先下§r[最短烹饪时间]较长的物品，其次再下[最短烹饪时间]较短的物品，以免烧焦");
        generator.add(SMCRegistrateItems.COOK_GUIDE_BOOK_ITEM.get().key(8), "使用盘子对着炒锅右键以盛出。§c盘子不足§r时，使用§6[切石机]§r可以制作盘子，新手福利当中有赠送[切石机]");
        generator.add(SMCRegistrateItems.COOK_GUIDE_BOOK_ITEM.get().key(9), "当没有食材时，可直接向§b小助手§r快速订购食材。不过小助手提供的食材有限，与农民，屠夫，渔夫交易获得的食材种类会更加丰富。");
        generator.add(SMCRegistrateItems.COOK_GUIDE_BOOK_ITEM.get().key(10), "将出锅的食物置于§6主手§r，对着客户村民右键对话以完成交易。菜品份量，所用的食材丰富度以及品质等级都可以提升报酬！§c是否使用困难翻炒模式？");
        generator.add(SMCRegistrateItems.COOK_GUIDE_BOOK_ITEM.get().key(11), "合上");
        generator.add(SMCRegistrateItems.COOK_GUIDE_BOOK_ITEM.get().key(12), "§c切换为困难模式");
        generator.add(SMCRegistrateItems.COOK_GUIDE_BOOK_ITEM.get().key(13), "§a切换为休闲模式");
    }

}

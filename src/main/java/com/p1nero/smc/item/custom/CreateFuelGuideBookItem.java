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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

public class CreateFuelGuideBookItem extends SimpleDescriptionFoilItem {
    public CreateFuelGuideBookItem(Properties properties) {
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
                .addChoice(opt(1), ans(2))
                .thenExecute((screen -> screen.setPicture(ResourceLocation.fromNamespaceAndPath(SkilletManCoreMod.MOD_ID, "textures/gui/create_fuel_guide_book/1.png"))))
                .addChoice(opt(1), ans(3))
                .thenExecute((screen -> screen.setPicture(ResourceLocation.fromNamespaceAndPath(SkilletManCoreMod.MOD_ID, "textures/gui/create_fuel_guide_book/2.png"))))
                .addChoice(opt(1), ans(4))
                .thenExecute((screen -> screen.setPicture(ResourceLocation.fromNamespaceAndPath(SkilletManCoreMod.MOD_ID, "textures/gui/create_fuel_guide_book/3.png"))))
                .addChoice(opt(1), ans(5))
                .thenExecute((screen -> screen.setPicture(ResourceLocation.fromNamespaceAndPath(SkilletManCoreMod.MOD_ID, "textures/gui/create_fuel_guide_book/4.png"))))
                .addFinalChoice(opt(7));
        DialogueScreen screen = builder.build();
        screen.setPicture(ResourceLocation.fromNamespaceAndPath(SkilletManCoreMod.MOD_ID, "textures/gui/create_fuel_guide_book/0.png"));
        screen.setPicHeight(791);
        screen.setPicWidth(1366);
        Minecraft.getInstance().setScreen(builder.build());
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
        generator.add(SMCRegistrateItems.CREATE_FUEL_GUIDE_BOOK.get().key(0), "本整合包为§6[柴油动力]§r模组进行了适配，本书将介绍在本整合包中如何制取生物柴油。生物柴油可以使柴油机产生动力，柴油机是相对适合本整合包的动力，它体积小，燃料容易获得。");
        generator.add(SMCRegistrateItems.CREATE_FUEL_GUIDE_BOOK.get().key(1), "下一页");
        generator.add(SMCRegistrateItems.CREATE_FUEL_GUIDE_BOOK.get().key(2), "我为所有的食物§a[可直接食用且具有饱食度的物品]§r添加了发酵配方，现在你可以使用§c蜘蛛眼§r或§c腐肉§r在§6工作盆§r或§6发酵塔§r里让它们发酵成生物柴油。饱食度越高的食物发酵产量越高，§6盘装料理§r的发酵产量最高，具体可以在§a[JEI]§r查看它们的配方。");
        generator.add(SMCRegistrateItems.CREATE_FUEL_GUIDE_BOOK.get().key(3), "当§6工作盆§r盖上§6工作盆盖§r时，若工作盆内的物品满足§a发酵配方§r，则会进行发酵，一段时间后原料转化为对应物品或液体。可以查看工作盆盖的§6[思索]§r");
        generator.add(SMCRegistrateItems.CREATE_FUEL_GUIDE_BOOK.get().key(4), "发酵塔和工作盆相同，可以接收物品和液体，它就像流体储罐和物品保险柜的合体。其中的物品满足发酵配方时，经过一段时间后将发酵成对应的物品，但是它的容量比工作盆大得多。但它目前还没有[思索]（截至本物品制作时）");
        generator.add(SMCRegistrateItems.CREATE_FUEL_GUIDE_BOOK.get().key(5), "接下来，将燃料用动力泵和导管输入到柴油引擎中，就可以享受动力的轰鸣了！如果觉得太吵，可以使用[引擎消音器]将其静音哦~");
        generator.add(SMCRegistrateItems.CREATE_FUEL_GUIDE_BOOK.get().key(7), "合上");
    }

}

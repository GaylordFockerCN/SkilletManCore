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
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

public class CreateCookGuideBookItem extends SimpleDescriptionFoilItem {
    public CreateCookGuideBookItem(Properties properties) {
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
                .thenExecute((screen -> screen.setPicture(ResourceLocation.fromNamespaceAndPath(SkilletManCoreMod.MOD_ID, "textures/gui/create_cook_guide_book/2.png"))))
                .addChoice(opt(1), ans(3))
                .thenExecute((screen -> screen.setPicture(ResourceLocation.fromNamespaceAndPath(SkilletManCoreMod.MOD_ID, "textures/gui/create_cook_guide_book/3.png"))))
                .addChoice(opt(1), ans(4))
                .thenExecute((screen -> screen.setPicture(ResourceLocation.fromNamespaceAndPath(SkilletManCoreMod.MOD_ID, "textures/gui/create_cook_guide_book/4.png"))))
                .addChoice(opt(1), ans(5))
                .thenExecute((screen -> screen.setPicture(ResourceLocation.fromNamespaceAndPath(SkilletManCoreMod.MOD_ID, "textures/gui/create_cook_guide_book/5.png"))))
                .addChoice(opt(1), ans(6))
                .thenExecute((screen -> screen.setPicture(ResourceLocation.fromNamespaceAndPath(SkilletManCoreMod.MOD_ID, "textures/gui/create_cook_guide_book/6.png"))))
                .addFinalChoice(opt(7));
        DialogueScreen screen = builder.build();
        screen.setPicture(ResourceLocation.fromNamespaceAndPath(SkilletManCoreMod.MOD_ID, "textures/gui/create_cook_guide_book/1.png"));
        screen.setPicHeight(1440);
        screen.setPicWidth(2560);
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
        generator.add(SMCRegistrateItems.CREATE_COOK_GUIDE_BOOK_ITEM.get().key(0), "§6机械手§r或§6动力臂§r是机械动力结合料理乐事不可或缺的一环，它们可以模拟玩家炒菜。机械手原生可以和炒锅交互，而本整合包自制了模组使得机械臂可以与炒锅交互，更加美观。本书可以结合炒锅§c思索§r阅读。");
        generator.add(SMCRegistrateItems.CREATE_COOK_GUIDE_BOOK_ITEM.get().key(1), "下一页");
        generator.add(SMCRegistrateItems.CREATE_COOK_GUIDE_BOOK_ITEM.get().key(2), "机械臂取得锅铲，并且输出端为炒锅时，若炒锅内有食物，则会进行翻炒。它很乖的，没炒到取餐不会停下来。不过这个设计不符合机械臂物流元件的设定，你可以使用机械手来代替。");
        generator.add(SMCRegistrateItems.CREATE_COOK_GUIDE_BOOK_ITEM.get().key(3), "如图摆放传送带，传送带转速为§68RPM§r时，刚好使得传送带上的物品3s前进一格。回想一下你做过的料理，是不是时间都是3的倍数？利用此特性，传送带末尾作为输入点，我们可以实现延时有序下锅。当然，你也可以尝试从多个不同的地方取食材， 但时间和顺序相当难控制。");
        generator.add(SMCRegistrateItems.CREATE_COOK_GUIDE_BOOK_ITEM.get().key(4), "查看炒锅的[思索]，其中提到的取盘机制可以如图摆放，安山漏斗或下方置物台为输入点，炒锅为第一输出点，黄铜漏斗为第二输出点，且黄铜漏斗上使用过滤器滤过空盘子，机械臂即可先取餐后输出。别忘了用红石在料理可出锅前限制机械臂取餐哦~");
        generator.add(SMCRegistrateItems.CREATE_COOK_GUIDE_BOOK_ITEM.get().key(5), "利用高级物流管理系统可以实现远程下单，机械臂和炒锅交互时会自行处理包裹。智能侦测器在检测到包裹后，经中继器延时15秒后，用无线红石信号，解除对取盘机械臂的锁定。");
        generator.add(SMCRegistrateItems.CREATE_COOK_GUIDE_BOOK_ITEM.get().key(6), "利用§6食物可以直接掉入炒锅§r的新特性，使用机械手来完成以上过程将更精简，但是机械手动起来哪有机械臂浪漫呢？§a本教程仅提供作者为观赏性自制的一种炒菜机，具体还得看各位大神发挥啦！");
        generator.add(SMCRegistrateItems.CREATE_COOK_GUIDE_BOOK_ITEM.get().key(7), "合上");
    }

}

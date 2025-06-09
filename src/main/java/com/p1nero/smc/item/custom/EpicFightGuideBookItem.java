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
import yesman.epicfight.client.input.EpicFightKeyMappings;

public class EpicFightGuideBookItem extends SimpleDescriptionFoilItem {
    public EpicFightGuideBookItem(Properties properties) {
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
        builder.start(ans(0, EpicFightKeyMappings.SWITCH_MODE.getTranslatedKeyMessage().copy().withStyle(ChatFormatting.YELLOW)))
                .addChoice(opt(1), ans(2, EpicFightKeyMappings.DODGE.getTranslatedKeyMessage().copy().withStyle(ChatFormatting.YELLOW)))
                .thenExecute((screen -> screen.setPicture(ResourceLocation.fromNamespaceAndPath(SkilletManCoreMod.MOD_ID, "textures/gui/epic_fight_guide_book/1.png"))))
                .addChoice(opt(1), ans(3))
                .thenExecute((screen -> screen.setPicture(ResourceLocation.fromNamespaceAndPath(SkilletManCoreMod.MOD_ID, "textures/gui/epic_fight_guide_book/2.png"))))
                .addChoice(opt(1), ans(4))
                .thenExecute((screen -> screen.setPicture(ResourceLocation.fromNamespaceAndPath(SkilletManCoreMod.MOD_ID, "textures/gui/epic_fight_guide_book/3.png"))))
                .addChoice(opt(1), ans(5))
                .thenExecute((screen -> screen.setPicture(ResourceLocation.fromNamespaceAndPath(SkilletManCoreMod.MOD_ID, "textures/gui/epic_fight_guide_book/4.png"))))
                .addFinalChoice(opt(7));
        DialogueScreen screen = builder.build();
        screen.setPicture(ResourceLocation.fromNamespaceAndPath(SkilletManCoreMod.MOD_ID, "textures/gui/epic_fight_guide_book/0.png"));
        screen.setPicHeight(480);
        screen.setPicWidth(900);
        Minecraft.getInstance().setScreen(builder.build());
    }

    private Component ans(int i, Object ...objects) {
        return Component.literal("\n").append(Component.translatable(this.getDescriptionId() + "_guide_" + i, objects)).withStyle(ChatFormatting.WHITE);
    }

    private Component opt(int i, Object ...objects) {
        return Component.translatable(this.getDescriptionId() + "_guide_" + i, objects);
    }

    private String key(int i) {
        return this.getDescriptionId() + "_guide_" + i;
    }

    public static void addTranslation(SMCLangGenerator generator){
        generator.add(SMCRegistrateItems.EPIC_FIGHT_GUIDE_BOOK.get().key(0), "首先按下§6[%s]§r开启战斗模式。本整合包采用了§6[史诗战斗]§r的战斗系统，但同时§a保留了原版的战斗系统。§r在原版的情况下，若学习了防御技能，您可以使用§6右键§r进行防御，正如Minecraft1.9更新之前一般！注意，您仍然会受到怪物攻击的§c硬直§r影响！如果不稍微学习一下史诗战斗，可能会打得很难受，并且很难击败最终Boss。我们还是建议您多使用史诗战斗的战斗系统。");
        generator.add(SMCRegistrateItems.EPIC_FIGHT_GUIDE_BOOK.get().key(1), "下一页");
        generator.add(SMCRegistrateItems.EPIC_FIGHT_GUIDE_BOOK.get().key(2), "学习闪避类技能后，按下§6[%s]§r可进行§a闪避§r。若学习§6[完美闪避显示]§r，则在敌人攻击的瞬间闪避后，会留下残影和播放音效。§c看准时机闪避很关键，看准敌人出招再闪避！趁敌人攻击的后摇再反击！闪避会消耗§6耐力§c，没有耐力将无法闪避。请管理好你的耐力！");
        generator.add(SMCRegistrateItems.EPIC_FIGHT_GUIDE_BOOK.get().key(3), "学习防御类技能后，按下§6[鼠标右键]§r可进行格挡。若学习§6[招架]§r，则在敌人攻击的瞬间成功招架后，会播放特殊动画和播放音效。§c看准时机格挡很关键，看准敌人出招再格挡！趁敌人攻击的后摇再反击！格挡会消耗§6耐力§c，没有耐力强行格挡将会破防！请管理好你的耐力！");
        generator.add(SMCRegistrateItems.EPIC_FIGHT_GUIDE_BOOK.get().key(4), "本整合包进行了§6群怪优化§r，聚集在一起的怪会轮流攻击。");
        generator.add(SMCRegistrateItems.EPIC_FIGHT_GUIDE_BOOK.get().key(5), "不同的武器会有不同的攻击模板，快去抽取强力武器吧！具体连招可以看看武器的物品说明！传说级武器键位在按键绑定中的§6[无坚不摧按键]§r进行修改。");
        generator.add(SMCRegistrateItems.EPIC_FIGHT_GUIDE_BOOK.get().key(7), "合上");
    }

}

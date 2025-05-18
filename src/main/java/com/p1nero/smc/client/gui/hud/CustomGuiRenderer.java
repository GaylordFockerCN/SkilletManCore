package com.p1nero.smc.client.gui.hud;

import com.mojang.blaze3d.platform.Window;
import com.p1nero.smc.SMCConfig;
import com.p1nero.smc.SkilletManCoreMod;
import com.p1nero.smc.archive.DataManager;
import com.p1nero.smc.capability.SMCCapabilityProvider;
import com.p1nero.smc.capability.SMCPlayer;
import com.p1nero.smc.client.gui.screen.DialogueScreen;
import dev.xkmc.cuisinedelight.init.CuisineDelight;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CustomGuiRenderer {
    public static final ResourceLocation SPATULA_TEXTURE = ResourceLocation.fromNamespaceAndPath(CuisineDelight.MODID, "textures/item/spatula.png");
    public static final ResourceLocation SPATULA_TEXTURE2 = ResourceLocation.fromNamespaceAndPath(SkilletManCoreMod.MOD_ID, "textures/item/golden_spatula.png");
    public static final ResourceLocation SPATULA_TEXTURE3 = ResourceLocation.fromNamespaceAndPath(SkilletManCoreMod.MOD_ID, "textures/item/diamond_spatula.png");
    public static final ResourceLocation MONEY_TEXTURE = ResourceLocation.parse("textures/item/emerald.png");

    public static boolean shouldRender() {
        if (Minecraft.getInstance().screen instanceof DialogueScreen) {
            return true;
        }
        return Minecraft.getInstance().screen == null;
    }

    public static void renderCustomGui(GuiGraphics guiGraphics) {
        if (!shouldRender()) {
            return;
        }

        LocalPlayer localPlayer = Minecraft.getInstance().player;
        if (localPlayer == null) {
            return;
        }
        SMCPlayer smcPlayer = SMCCapabilityProvider.getSMCPlayer(localPlayer);
        Window window = Minecraft.getInstance().getWindow();
        Font font = Minecraft.getInstance().font;
        int x = (int) (SMCConfig.INFO_X.get() * window.getGuiScaledWidth());
        int y = (int) (SMCConfig.INFO_Y.get() * window.getGuiScaledWidth());
        int interval = SMCConfig.INTERVAL.get();
        int offsetX = font.width(": " + smcPlayer.getMoneyCount());

        int stageColor = switch (smcPlayer.getStage()) {
            case 1 -> 0x84fbff;
            case 2 -> 0x40ff5f;
            case 3 -> 0xfb4ee9;
            default -> 16777215;
        };
        ResourceLocation spatulaTexture = switch (smcPlayer.getStage()){
            case 0 -> SPATULA_TEXTURE;
            case 1 -> SPATULA_TEXTURE2;
            default -> SPATULA_TEXTURE3;
        };
        guiGraphics.blit(spatulaTexture, x - offsetX, y - 25, 20, 20, 0.0F, 0.0F, 1, 1, 1, 1);
        guiGraphics.drawString(font, ": " + smcPlayer.getLevel(), x + 20 - offsetX, y - 20, stageColor, true);

        int lineHeight = font.lineHeight + 6;
        if (Minecraft.getInstance().screen == null) {
            renderTutorial(guiGraphics, localPlayer, smcPlayer, font, lineHeight, x, (int) (y + lineHeight * 1.5F));
        }
        guiGraphics.blit(MONEY_TEXTURE, x - offsetX, y, 20, 20, 0.0F, 0.0F, 1, 1, 1, 1);
        guiGraphics.drawString(font, ": " + smcPlayer.getMoneyCount(), x + 20 - offsetX, y + 5, 16777215, true);

        guiGraphics.drawString(font, smcPlayer.isWorking() ? SkilletManCoreMod.getInfo("working").withStyle(ChatFormatting.BOLD, ChatFormatting.GREEN) : SkilletManCoreMod.getInfo("resting").withStyle(ChatFormatting.BOLD, ChatFormatting.GOLD), x - offsetX, y + font.lineHeight + interval, 0x00ff00, true);
    }

    public static void renderTutorial(GuiGraphics guiGraphics, LocalPlayer localPlayer, SMCPlayer smcPlayer, Font font, int lineHeight, int x, int y) {
        if (!DataManager.firstGiftGot.get(localPlayer)) {
            Component info = SkilletManCoreMod.getInfo("find_villager_first").withStyle(ChatFormatting.BOLD, ChatFormatting.GOLD);
            Component info2 = SkilletManCoreMod.getInfo("find_villager_first2").withStyle(ChatFormatting.GRAY);
            Component info3 = SkilletManCoreMod.getInfo("find_villager_first3").withStyle(ChatFormatting.GRAY);
            Component info4 = SkilletManCoreMod.getInfo("find_villager_first4").withStyle(ChatFormatting.GRAY);
            int maxWidth = Math.max(Math.max(font.width(info), font.width(info2)), font.width(info3));
            guiGraphics.fillGradient(8, y + lineHeight * 2 - 2, 8 + maxWidth + 2, y + lineHeight * 6, 0x66000000, 0x66000000);
            guiGraphics.drawString(font, info, 10, y + lineHeight * 2, 0x00ff00, true);
            guiGraphics.drawString(font, info2, 10, y + lineHeight * 3, 0x00ff00, true);
            guiGraphics.drawString(font, info3, 10, y + lineHeight * 4, 0x00ff00, true);
            guiGraphics.drawString(font, info4, 10, y + lineHeight * 5, 0x00ff00, true);
        } else if (!DataManager.firstGachaGot.get(localPlayer)) {
            Component info = SkilletManCoreMod.getInfo("find_villager_gacha").withStyle(ChatFormatting.BOLD, ChatFormatting.AQUA);
            Component info2 = SkilletManCoreMod.getInfo("find_villager_gacha2").withStyle(ChatFormatting.GRAY);
            Component info3 = SkilletManCoreMod.getInfo("find_villager_gacha3").withStyle(ChatFormatting.GRAY);
            int maxWidth = Math.max(Math.max(font.width(info), font.width(info2)), font.width(info3));
            guiGraphics.fillGradient(8, y + lineHeight * 2 - 2, 8 + maxWidth + 2, y + lineHeight * 5, 0x66000000, 0x66000000);
            guiGraphics.drawString(font, info, 10, y + lineHeight * 2, 0x00ff00, true);
            guiGraphics.drawString(font, info2, 10, y + lineHeight * 3, 0x00ff00, true);
            guiGraphics.drawString(font, info3, 10, y + lineHeight * 4, 0x00ff00, true);
        } else if(!DataManager.firstWork.get(localPlayer)) {
            Component info = SkilletManCoreMod.getInfo("first_work").withStyle(ChatFormatting.BOLD, ChatFormatting.GREEN);
            Component info2 = SkilletManCoreMod.getInfo("first_work2").withStyle(ChatFormatting.GRAY);
            int maxWidth = Math.max(font.width(info), font.width(info2));
            guiGraphics.fillGradient(8, y + lineHeight * 2 - 2, 8 + maxWidth + 2, y + lineHeight * 4, 0x66000000, 0x66000000);
            guiGraphics.drawString(font, info, 10, y + lineHeight * 2, 0x00ff00, true);
            guiGraphics.drawString(font, info2, 10, y + lineHeight * 3, 0x00ff00, true);
        } else if (smcPlayer.isTrialRequired() && !DataManager.inRaid.get(localPlayer)) {
            Component info = SkilletManCoreMod.getInfo("trial_required").withStyle(ChatFormatting.BOLD, ChatFormatting.GOLD);
            Component info2 = SkilletManCoreMod.getInfo("trial_required2").withStyle(ChatFormatting.GRAY);
            int maxWidth = Math.max(font.width(info), font.width(info2));
            guiGraphics.fillGradient(8, y + lineHeight * 2 - 2, 8 + maxWidth + 2, y + lineHeight * 4, 0x66000000, 0x66000000);
            guiGraphics.drawString(font, info, 10, y + lineHeight * 2, 0x00ff00, true);
            guiGraphics.drawString(font, info2, 10, y + lineHeight * 3, 0x00ff00, true);
        } else if(DataManager.shouldShowMachineTicketHint.get(localPlayer)) {
            Component info = SkilletManCoreMod.getInfo("should_trade_machine_ticket").withStyle(ChatFormatting.BOLD, ChatFormatting.YELLOW);
            Component info2 = SkilletManCoreMod.getInfo("should_trade_machine_ticket2").withStyle(ChatFormatting.GRAY);
            int maxWidth = Math.max(font.width(info), font.width(info2));
            guiGraphics.fillGradient(8, y + lineHeight * 2 - 2, 8 + maxWidth + 2, y + lineHeight * 4, 0x66000000, 0x66000000);
            guiGraphics.drawString(font, info, 10, y + lineHeight * 2, 0x00ff00, true);
            guiGraphics.drawString(font, info2, 10, y + lineHeight * 3, 0x00ff00, true);
        }
    }

}

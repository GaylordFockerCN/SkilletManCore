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

public class CustomGuiRenderer {
    public static final ResourceLocation SPATULA_TEXTURE = new ResourceLocation(CuisineDelight.MODID, "textures/item/spatula.png");
    public static final ResourceLocation MONEY_TEXTURE = new ResourceLocation("textures/item/emerald.png");

    public static boolean shouldRender(){
        if(Minecraft.getInstance().screen instanceof DialogueScreen) {
            return true;
        }
        return Minecraft.getInstance().screen == null;
    }

    public static void renderCustomGui(GuiGraphics guiGraphics){
        if(!shouldRender()) {
            return;
        }

        LocalPlayer localPlayer = Minecraft.getInstance().player;
        if(localPlayer == null) {
            return;
        }
        SMCPlayer smcPlayer = SMCCapabilityProvider.getSMCPlayer(localPlayer);
        Window window = Minecraft.getInstance().getWindow();
        Font font = Minecraft.getInstance().font;
        int x = (int) (SMCConfig.INFO_X.get() * window.getGuiScaledWidth());
        int y = (int) (SMCConfig.INFO_Y.get() * window.getGuiScaledWidth());
        int interval = SMCConfig.INTERVAL.get();
        int offsetX = font.width(": " + smcPlayer.getMoneyCount());

        if(smcPlayer.isTrialRequired()) {
            Component info = SkilletManCoreMod.getInfo("trial_required");
            int offsetX2 = font.width(info);
            guiGraphics.blit(SPATULA_TEXTURE, x - offsetX2, y - 25, 20, 20, 0.0F, 0.0F, 1, 1, 1, 1);
            guiGraphics.drawString(font, info, x + 20 - offsetX2, y - 20, 16777215, true);
        } else {
            int stageColor = switch (smcPlayer.getStage()) {
                case 1 -> 0x84fbff;
                case 2 -> 0x40ff5f;
                case 3 -> 0xfb4ee9;
                default -> 16777215;
            };
            guiGraphics.blit(SPATULA_TEXTURE, x - offsetX, y - 25, 20, 20, 0.0F, 0.0F, 1, 1, 1, 1);
            guiGraphics.drawString(font, ": " + smcPlayer.getLevel(), x + 20 - offsetX, y - 20, stageColor, true);
        }

        guiGraphics.blit(MONEY_TEXTURE, x - offsetX, y, 20, 20, 0.0F, 0.0F, 1, 1, 1, 1);
        guiGraphics.drawString(font, ": " + smcPlayer.getMoneyCount(), x + 20 - offsetX, y + 5, 16777215, true);
        if(!DataManager.firstGiftGot.get(localPlayer)) {
            Component info = SkilletManCoreMod.getInfo("find_villager_first").withStyle(ChatFormatting.BOLD, ChatFormatting.YELLOW);
            Component info2 = SkilletManCoreMod.getInfo("find_villager_first2").withStyle(ChatFormatting.BOLD, ChatFormatting.YELLOW);
            guiGraphics.drawString(font, info, x - font.width(info), y + font.lineHeight + interval, 0x00ff00, true);
            guiGraphics.drawString(font, info2, x - font.width(info2), y + font.lineHeight + interval, 0x00ff00, true);
        } else {
            guiGraphics.drawString(font, smcPlayer.isWorking() ? SkilletManCoreMod.getInfo("working").withStyle(ChatFormatting.BOLD, ChatFormatting.GREEN) : SkilletManCoreMod.getInfo("resting").withStyle(ChatFormatting.BOLD, ChatFormatting.GOLD), x - offsetX, y + font.lineHeight + interval, 0x00ff00, true);
        }
    }
}

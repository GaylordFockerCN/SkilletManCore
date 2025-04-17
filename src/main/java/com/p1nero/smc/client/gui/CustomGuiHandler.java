package com.p1nero.smc.client.gui;

import com.mojang.blaze3d.platform.Window;
import com.p1nero.smc.SMCConfig;
import com.p1nero.smc.SkilletManCoreMod;
import com.p1nero.smc.capability.SMCCapabilityProvider;
import com.p1nero.smc.capability.SMCPlayer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;

public class CustomGuiHandler {
    public static final ResourceLocation MONEY_TEXTURE = new ResourceLocation("textures/item/emerald.png");

    public static void renderCustomGui(GuiGraphics guiGraphics){
        if(Minecraft.getInstance().screen != null) {
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
        guiGraphics.blit(MONEY_TEXTURE, x - offsetX, y, 20, 20, 0.0F, 0.0F, 1, 1, 1, 1);
        guiGraphics.drawString(font, ": " + smcPlayer.getMoneyCount(), x + 20 - offsetX, y + 5, 0x00ff00, true);
        guiGraphics.drawString(font, smcPlayer.isWorking() ? SkilletManCoreMod.getInfo("working").withStyle(ChatFormatting.BOLD, ChatFormatting.GREEN) : SkilletManCoreMod.getInfo("resting").withStyle(ChatFormatting.BOLD, ChatFormatting.GOLD), x - offsetX, y + font.lineHeight + interval, 0x00ff00, true);

    }
}

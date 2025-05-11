package com.p1nero.smc.client.gui.hud;

import com.mojang.blaze3d.platform.Window;
import com.p1nero.smc.SMCConfig;
import com.p1nero.smc.SkilletManCoreMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffects;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class HealthBarRenderer {

    private static final ResourceLocation HEALTH_BAR_TEXTURE = new ResourceLocation(SkilletManCoreMod.MOD_ID, "textures/gui/hud/health_bar.png");
    private static final ResourceLocation HEALTH_BAR_BG_TEXTURE = new ResourceLocation(SkilletManCoreMod.MOD_ID, "textures/gui/hud/health_bar_bg.png");
    public static void renderHealthBar(GuiGraphics guiGraphics, Window window, float partialTick) {
        LocalPlayer localPlayer = Minecraft.getInstance().player;
        if(localPlayer != null && !localPlayer.isCreative() && !localPlayer.isSpectator()) {
            Font font = Minecraft.getInstance().font;
            float scale = 0.83333F;
            float rate = localPlayer.getHealth() / localPlayer.getMaxHealth();
            double offset = rate < 0.2 ? Math.random() * 2 - 1 : 0;
            int width = (int) (114 * scale);
            int height = (int) (13 * scale);
            int x = (int) (window.getGuiScaledWidth() / 2.0F - width * 0.95);
            int y = (int) (window.getGuiScaledHeight() - height * 3.9);
            if(localPlayer.getHealth() == localPlayer.getMaxHealth()) {
                guiGraphics.blit(HEALTH_BAR_TEXTURE, (int) (x + offset), (int) (y + offset), width, height, 0, 0, 114, 13, 114, 13);
            } else {
                guiGraphics.blit(HEALTH_BAR_BG_TEXTURE, (int) (x + offset), (int) (y + offset), width, height, 0, 0, 114, 13, 114, 13);
                guiGraphics.blit(HEALTH_BAR_TEXTURE, (int) (x + offset), (int) (y + offset),  Mth.lerpInt(rate, 0, width), height, 0, 0,  Mth.lerpInt(rate, 0, 114), 13, 114, 13);
            }

            String info = String.format("%.1f / %.1f", localPlayer.getHealth(), localPlayer.getMaxHealth());
            int infoX = (int) (x + width / 2.0F - font.width(info) / 2.0F);
            int color = 16777215;
            if(rate < 0.1) {
                color = 0xff1717;
                info = "§l" + info;
            }
            if(localPlayer.hasEffect(MobEffects.WITHER)) {
                color = 0x696969;
            }
            if(localPlayer.hasEffect(MobEffects.REGENERATION)) {
                color = 0xfff66d;
            }
            guiGraphics.drawString(font, info, (int) (infoX + offset), (int) (y - 1 - font.lineHeight / 2 + offset), color, true);
        }
    }
}

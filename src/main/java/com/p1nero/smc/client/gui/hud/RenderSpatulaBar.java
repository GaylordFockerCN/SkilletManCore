package com.p1nero.smc.client.gui.hud;

import com.mojang.blaze3d.platform.Window;
import com.p1nero.smc.SMCConfig;
import com.p1nero.smc.SkilletManCoreMod;
import com.p1nero.smc.archive.DataManager;
import com.p1nero.smc.capability.SMCCapabilityProvider;
import com.p1nero.smc.capability.SMCPlayer;
import dev.xkmc.cuisinedelight.content.client.CookingOverlay;
import dev.xkmc.cuisinedelight.content.item.SpatulaItem;
import dev.xkmc.cuisinedelight.content.logic.CookingData;
import dev.xkmc.cuisinedelight.init.CuisineDelight;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RenderSpatulaBar {

    public static final ResourceLocation SPATULA_TEXTURE = ResourceLocation.fromNamespaceAndPath(CuisineDelight.MODID, "textures/item/spatula.png");
    public static final ResourceLocation SPATULA_TEXTURE2 = ResourceLocation.fromNamespaceAndPath(SkilletManCoreMod.MOD_ID, "textures/item/golden_spatula.png");
    public static final ResourceLocation SPATULA_TEXTURE3 = ResourceLocation.fromNamespaceAndPath(SkilletManCoreMod.MOD_ID, "textures/item/diamond_spatula.png");
    private static final ResourceLocation BG_TEXTURE = ResourceLocation.fromNamespaceAndPath(SkilletManCoreMod.MOD_ID, "textures/gui/hud/spatula_bar.png");

    public static void render(GuiGraphics guiGraphics, Window window, float partialTick) {
        LocalPlayer localPlayer = Minecraft.getInstance().player;
        if (localPlayer == null || Minecraft.getInstance().isPaused() || !SMCConfig.HARD_COOK_MODE.get()) {
            return;
        }
        CookingData data = CookingOverlay.getData();
        if (data != null && !data.contents.isEmpty() && localPlayer.getMainHandItem().getItem() instanceof SpatulaItem) {
            SMCPlayer smcPlayer = SMCCapabilityProvider.getSMCPlayer(localPlayer);

            int x = (int) (window.getGuiScaledWidth() / 2.0F - 128);
            int spatulaX = x + Mth.lerpInt((smcPlayer.getCurrentSpatulaIndex(partialTick) / SMCPlayer.MAX_SPATULA_TIME), 0, 250);
            int y = 20;

            int comboCount = DataManager.spatulaCombo.get(localPlayer).intValue();

            ChatFormatting style = ChatFormatting.GRAY;
            ResourceLocation spatulaTexture = SPATULA_TEXTURE;
            if(comboCount >= 10) {
                style = ChatFormatting.WHITE;
            }
            if(comboCount >= 20) {
                style = ChatFormatting.YELLOW;
                spatulaTexture = SPATULA_TEXTURE2;
            }
            if(comboCount >= 30) {
                style = ChatFormatting.RED;
                spatulaTexture = SPATULA_TEXTURE3;
            }
            if(comboCount >= 40) {
                style = ChatFormatting.GOLD;
            }
            guiGraphics.blit(BG_TEXTURE, x, y, 256, 16, 0.0F, 0.0F, 256, 16, 256, 16);
            guiGraphics.blit(spatulaTexture, spatulaX - 4, y - 2, 20, 20, 0.0F, 0.0F, 1, 1, 1, 1);

            Component comboInfo = Component.literal(" × " + comboCount).withStyle(style, ChatFormatting.BOLD);
            int width = Minecraft.getInstance().font.width(comboInfo);
            int comboX = (int) (window.getGuiScaledWidth() / 2.0F - width / 2.0);
            guiGraphics.drawString(Minecraft.getInstance().font, comboInfo, comboX, y - 10, 16777215, true);
        }
    }

}

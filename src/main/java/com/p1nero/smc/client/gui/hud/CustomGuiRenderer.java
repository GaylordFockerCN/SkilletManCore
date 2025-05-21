package com.p1nero.smc.client.gui.hud;

import com.mojang.blaze3d.platform.Window;
import com.p1nero.smc.SMCConfig;
import com.p1nero.smc.SkilletManCoreMod;
import com.p1nero.smc.archive.DataManager;
import com.p1nero.smc.capability.SMCCapabilityProvider;
import com.p1nero.smc.capability.SMCPlayer;
import com.p1nero.smc.client.gui.screen.DialogueScreen;
import com.p1nero.smc.client.keymapping.KeyMappings;
import com.teamtea.eclipticseasons.EclipticSeasons;
import com.teamtea.eclipticseasons.api.constant.solar.SolarTerm;
import com.teamtea.eclipticseasons.api.util.EclipticUtil;
import dev.xkmc.cuisinedelight.init.CuisineDelight;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CustomGuiRenderer {
    public static final ResourceLocation SPATULA_TEXTURE = ResourceLocation.fromNamespaceAndPath(CuisineDelight.MODID, "textures/item/spatula.png");
    public static final ResourceLocation SPATULA_TEXTURE2 = ResourceLocation.fromNamespaceAndPath(SkilletManCoreMod.MOD_ID, "textures/item/golden_spatula.png");
    public static final ResourceLocation SPATULA_TEXTURE3 = ResourceLocation.fromNamespaceAndPath(SkilletManCoreMod.MOD_ID, "textures/item/diamond_spatula.png");
    public static final ResourceLocation MONEY_TEXTURE = ResourceLocation.parse("textures/item/emerald.png");
    public static final ResourceLocation TERM_ICON = SolarTerm.getFontIcon().withPrefix("textures/").withSuffix(".png");

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
        int lineHeight = font.lineHeight + 6;
        int screenW = window.getGuiScaledWidth();
        int screenH = window.getGuiScaledHeight();
        int x = (int) (SMCConfig.INFO_X.get() * window.getGuiScaledWidth());
        int y = (int) (SMCConfig.INFO_Y.get() * window.getGuiScaledWidth()) + (int) (lineHeight * 1.5F);
        int interval = SMCConfig.INTERVAL.get();
        Component moneyCount = Component.literal(": " + smcPlayer.getMoneyCount());
        SolarTerm solarTerm = EclipticUtil.getNowSolarTerm(localPlayer.clientLevel);
        Component term = Component.literal("[").append(solarTerm.getSeason().getTranslation()).append("] ").append(solarTerm.getTranslation()).withStyle(ChatFormatting.BOLD, solarTerm.getSeason().getColor());
        int offsetX = font.width(moneyCount) + 4;

        int stageColor = switch (smcPlayer.getStage()) {
            case 1 -> 0x84fbff;
            case 2 -> 0x40ff5f;
            case 3 -> 0xfb4ee9;
            default -> 16777215;
        };
        ResourceLocation spatulaTexture = switch (smcPlayer.getStage()) {
            case 0 -> SPATULA_TEXTURE;
            case 1 -> SPATULA_TEXTURE2;
            default -> SPATULA_TEXTURE3;
        };

        int termX = solarTerm.getIconPosition().getKey();
        int termY = solarTerm.getIconPosition().getValue();
        guiGraphics.blit(TERM_ICON, 2, y - 73 + 2, 16, 16, termX * 30, termY * 30, 30, 30, 180, 120);
        guiGraphics.drawString(font, term, 24, y - 68 + 2, stageColor, true);


        guiGraphics.blit(spatulaTexture, screenW - offsetX - 22, y - 65, 20, 20, 0.0F, 0.0F, 1, 1, 1, 1);
        guiGraphics.drawString(font, ": " + smcPlayer.getLevel(), screenW - offsetX, y - 65 + 5, stageColor, true);

        guiGraphics.blit(MONEY_TEXTURE, screenW - offsetX - 22, y - 40, 20, 20, 0.0F, 0.0F, 1, 1, 1, 1);
        guiGraphics.drawString(font, moneyCount, screenW - offsetX, y - 40 + 5, 16777215, true);

        guiGraphics.drawString(font, smcPlayer.isWorking() ? SkilletManCoreMod.getInfo("working").withStyle(ChatFormatting.BOLD, ChatFormatting.GREEN) : SkilletManCoreMod.getInfo("resting").withStyle(ChatFormatting.BOLD, ChatFormatting.GOLD), screenW - offsetX - 22, y + font.lineHeight + interval - 40, 0x00ff00, true);

        if (Minecraft.getInstance().screen == null) {
            renderTutorial(guiGraphics, localPlayer, smcPlayer, font, lineHeight, screenW, screenH, x, y);
        }
    }

    public static void renderTutorial(GuiGraphics guiGraphics, LocalPlayer localPlayer, SMCPlayer smcPlayer, Font font, int lineHeight, int screenW, int screenH, int x, int y) {
        boolean hasTodo = true;

        if (!DataManager.firstGiftGot.get(localPlayer)) {
        } else if (!DataManager.firstWork.get(localPlayer)) {
        } else if (!DataManager.firstStopWork.get(localPlayer)) {
        } else if (!DataManager.firstGachaGot.get(localPlayer)) {
        } else if (DataManager.trailRequired.get(localPlayer)) {
        } else if (DataManager.showFirstPlaceWirelessTerminal.get(localPlayer)) {
        } else if (DataManager.shouldShowMachineTicketHint.get(localPlayer)) {
        } else {
            hasTodo = false;
        }

        MutableComponent show = Component.literal("§7§l[§r");
        if(SMCConfig.SHOW_HINT.get()) {
            show = SkilletManCoreMod.getInfo("press_x_to_show_hint", KeyMappings.SHOW_HINT.getTranslatedKeyMessage().copy().withStyle(ChatFormatting.DARK_GREEN)).withStyle(ChatFormatting.BOLD, ChatFormatting.GRAY);
        } else {
            show.append(KeyMappings.SHOW_HINT.getTranslatedKeyMessage().copy().withStyle(ChatFormatting.BOLD, ChatFormatting.DARK_GREEN).append("§7§l]")).withStyle(ChatFormatting.BOLD);
            if(hasTodo) {
                show.append(SkilletManCoreMod.getInfo("task_todo_tip"));
            }
        }
        if(DataManager.hintUpdated.get(localPlayer)) {
            show.append(SkilletManCoreMod.getInfo("hint_update_tip")).append((localPlayer.tickCount / 10) % 2 == 0 ? Component.literal("⭐").withStyle(ChatFormatting.GOLD) : Component.empty());
        }
        guiGraphics.fillGradient(2, y + lineHeight - 2, 8 + font.width(show) + 2, y + lineHeight, 0x66000000, 0x66000000);
        guiGraphics.drawString(font, show, 4, y + lineHeight, 0x00ff00, true);
        if(!SMCConfig.SHOW_HINT.get()) {
            return;
        }
        if (!DataManager.firstGiftGot.get(localPlayer)) {
            Component info = SkilletManCoreMod.getInfo("find_villager_first").withStyle(ChatFormatting.BOLD, ChatFormatting.GOLD);
            Component info2 = SkilletManCoreMod.getInfo("find_villager_first2").withStyle(ChatFormatting.GRAY);
            Component info3 = SkilletManCoreMod.getInfo("find_villager_first3").withStyle(ChatFormatting.GRAY);
            Component info4 = SkilletManCoreMod.getInfo("find_villager_first4").withStyle(ChatFormatting.GRAY);
            int maxWidth = Math.max(Math.max(font.width(info), font.width(info2)), font.width(info3));
            guiGraphics.fillGradient(2, y + lineHeight * 2 - 2, 8 + maxWidth + 2, y + lineHeight * 6, 0x66000000, 0x66000000);
            guiGraphics.drawString(font, info, 4, y + lineHeight * 2, 0x00ff00, true);
            guiGraphics.drawString(font, info2, 4, y + lineHeight * 3, 0x00ff00, true);
            guiGraphics.drawString(font, info3, 4, y + lineHeight * 4, 0x00ff00, true);
            guiGraphics.drawString(font, info4, 4, y + lineHeight * 5, 0x00ff00, true);
        } else if (!DataManager.firstWork.get(localPlayer)) {
            Component info = SkilletManCoreMod.getInfo("first_work").withStyle(ChatFormatting.BOLD, ChatFormatting.GREEN);
            Component info2 = SkilletManCoreMod.getInfo("first_work2").withStyle(ChatFormatting.GRAY);
            int maxWidth = Math.max(font.width(info), font.width(info2));
            guiGraphics.fillGradient(2, y + lineHeight * 2 - 2, 8 + maxWidth + 2, y + lineHeight * 4, 0x66000000, 0x66000000);
            guiGraphics.drawString(font, info, 4, y + lineHeight * 2, 0x00ff00, true);
            guiGraphics.drawString(font, info2, 4, y + lineHeight * 3, 0x00ff00, true);
        } else if (!DataManager.firstStopWork.get(localPlayer)) {
            Component info = SkilletManCoreMod.getInfo("first_stop_work").withStyle(ChatFormatting.BOLD, ChatFormatting.RED);
            Component info2 = SkilletManCoreMod.getInfo("first_stop_work2").withStyle(ChatFormatting.GRAY);
            int maxWidth = Math.max(font.width(info), font.width(info2));
            guiGraphics.fillGradient(2, y + lineHeight * 2 - 2, 8 + maxWidth + 2, y + lineHeight * 4, 0x66000000, 0x66000000);
            guiGraphics.drawString(font, info, 4, y + lineHeight * 2, 0x00ff00, true);
            guiGraphics.drawString(font, info2, 4, y + lineHeight * 3, 0x00ff00, true);
        } else if (!DataManager.firstGachaGot.get(localPlayer)) {
            Component info = SkilletManCoreMod.getInfo("find_villager_gacha").withStyle(ChatFormatting.BOLD, ChatFormatting.AQUA);
            Component info2 = SkilletManCoreMod.getInfo("find_villager_gacha2").withStyle(ChatFormatting.GRAY);
            Component info3 = SkilletManCoreMod.getInfo("find_villager_gacha3").withStyle(ChatFormatting.GRAY);
            int maxWidth = Math.max(Math.max(font.width(info), font.width(info2)), font.width(info3));
            guiGraphics.fillGradient(2, y + lineHeight * 2 - 2, 8 + maxWidth + 2, y + lineHeight * 5, 0x66000000, 0x66000000);
            guiGraphics.drawString(font, info, 4, y + lineHeight * 2, 0x00ff00, true);
            guiGraphics.drawString(font, info2, 4, y + lineHeight * 3, 0x00ff00, true);
            guiGraphics.drawString(font, info3, 4, y + lineHeight * 4, 0x00ff00, true);
        } else if (DataManager.trailRequired.get(localPlayer)) {
            Component info = SkilletManCoreMod.getInfo("trial_required").withStyle(ChatFormatting.BOLD, ChatFormatting.GOLD);
            Component info2 = SkilletManCoreMod.getInfo("trial_required2").withStyle(ChatFormatting.GRAY);
            int maxWidth = Math.max(font.width(info), font.width(info2));
            guiGraphics.fillGradient(2, y + lineHeight * 2 - 2, 8 + maxWidth + 2, y + lineHeight * 4, 0x66000000, 0x66000000);
            guiGraphics.drawString(font, info, 4, y + lineHeight * 2, 0x00ff00, true);
            guiGraphics.drawString(font, info2, 4, y + lineHeight * 3, 0x00ff00, true);
        } else if (DataManager.showFirstPlaceWirelessTerminal.get(localPlayer)) {
            Component info = SkilletManCoreMod.getInfo("first_place_wireless_terminal").withStyle(ChatFormatting.BOLD, ChatFormatting.DARK_GREEN);
            Component info2 = SkilletManCoreMod.getInfo("first_place_wireless_terminal2").withStyle(ChatFormatting.GRAY);
            int maxWidth = Math.max(font.width(info), font.width(info2));
            guiGraphics.fillGradient(2, y + lineHeight * 2 - 2, 8 + maxWidth + 2, y + lineHeight * 4, 0x66000000, 0x66000000);
            guiGraphics.drawString(font, info, 4, y + lineHeight * 2, 0x00ff00, true);
            guiGraphics.drawString(font, info2, 4, y + lineHeight * 3, 0x00ff00, true);
        } else if (DataManager.shouldShowMachineTicketHint.get(localPlayer)) {
            Component info = SkilletManCoreMod.getInfo("should_trade_machine_ticket").withStyle(ChatFormatting.BOLD, ChatFormatting.YELLOW);
            Component info2 = SkilletManCoreMod.getInfo("should_trade_machine_ticket2").withStyle(ChatFormatting.GRAY);
            int maxWidth = Math.max(font.width(info), font.width(info2));
            guiGraphics.fillGradient(2, y + lineHeight * 2 - 2, 8 + maxWidth + 2, y + lineHeight * 4, 0x66000000, 0x66000000);
            guiGraphics.drawString(font, info, 4, y + lineHeight * 2, 0x00ff00, true);
            guiGraphics.drawString(font, info2, 4, y + lineHeight * 3, 0x00ff00, true);
        } else {
            Component info = SkilletManCoreMod.getInfo("no_task").withStyle(ChatFormatting.BOLD, ChatFormatting.DARK_GRAY);
            Component info2 = SkilletManCoreMod.getInfo("no_task2").withStyle(ChatFormatting.GRAY);
            int maxWidth = Math.max(font.width(info), font.width(info2));
            guiGraphics.fillGradient(2, y + lineHeight * 2 - 2, 8 + maxWidth + 2, y + lineHeight * 4, 0x66000000, 0x66000000);
            guiGraphics.drawString(font, info, 4, y + lineHeight * 2, 0x00ff00, true);
            guiGraphics.drawString(font, info2, 4, y + lineHeight * 3, 0x00ff00, true);
        }

    }


}

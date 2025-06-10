package com.p1nero.smc.client.gui.hud;

import com.mojang.blaze3d.platform.Window;
import com.p1nero.smc.SMCConfig;
import com.p1nero.smc.SkilletManCoreMod;
import com.p1nero.smc.archive.DataManager;
import com.p1nero.smc.capability.SMCCapabilityProvider;
import com.p1nero.smc.capability.SMCPlayer;
import com.p1nero.smc.client.gui.screen.DialogueScreen;
import com.p1nero.smc.client.keymapping.KeyMappings;
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

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

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
        int yL = (int) (SMCConfig.INFO_Y_L.get() * window.getGuiScaledWidth()) + (int) (lineHeight * 1.5F);
        int yR = (int) (SMCConfig.INFO_Y_R.get() * window.getGuiScaledWidth()) + (int) (lineHeight * 1.5F);
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

        //日历
        int termX = solarTerm.getIconPosition().getKey();
        int termY = solarTerm.getIconPosition().getValue();
        guiGraphics.blit(TERM_ICON, 2, 2, 16, 16, termX * 30, termY * 30, 30, 30, 180, 120);
        guiGraphics.drawString(font, term, 24, 7, stageColor, true);

        //任务提示
        if (Minecraft.getInstance().screen == null) {
            renderTutorial(guiGraphics, localPlayer, smcPlayer, font, lineHeight, screenW, screenH, 0, yL);
        }

        //声望等级
        guiGraphics.blit(spatulaTexture, screenW - offsetX - 22, yR - 65, 20, 20, 0.0F, 0.0F, 1, 1, 1, 1);
        guiGraphics.drawString(font, ": " + smcPlayer.getLevel(), screenW - offsetX, yR - 65 + 5, stageColor, true);
        //金币
        guiGraphics.blit(MONEY_TEXTURE, screenW - offsetX - 22, yR - 40, 20, 20, 0.0F, 0.0F, 1, 1, 1, 1);
        guiGraphics.drawString(font, moneyCount, screenW - offsetX, yR - 40 + 5, 16777215, true);
        //工作状态
        guiGraphics.drawString(font, smcPlayer.isWorking() ? SkilletManCoreMod.getInfo("working").withStyle(ChatFormatting.BOLD, ChatFormatting.GREEN) : SkilletManCoreMod.getInfo("resting").withStyle(ChatFormatting.BOLD, ChatFormatting.GOLD), screenW - offsetX - 22, yR + font.lineHeight + interval - 40, 0x00ff00, true);
        long time = localPlayer.level().getDayTime();
        String formattedTime = convertMinecraftTime(time);
        guiGraphics.drawString(font, Component.literal(formattedTime).withStyle(smcPlayer.isWorking() ? ChatFormatting.GREEN : ChatFormatting.GOLD), screenW - offsetX - 22, yR + font.lineHeight * 2 + interval - 40, 0x00ff00, true);
    }

    public static String convertMinecraftTime(long time) {
        // 调整时间到一天范围内 [0, 23999]
        long adjustedTime = time % 24000;
        if (adjustedTime < 0) {
            adjustedTime += 24000; // 处理负值
        }

        // 计算总小时数（包括小数），加上6小时的偏移（因为0刻对应6:00）
        double totalHours = (double) adjustedTime / 1000.0 + 6.0;
        totalHours %= 24; // 确保在24小时内

        // 提取小时和分钟
        int hours = (int) totalHours;
        int minutes = (int) Math.round((totalHours - hours) * 60);

        // 处理分钟进位（例如 23.999小时 -> 24:00 应转为 00:00）
        if (minutes >= 60) {
            hours = (hours + 1) % 24;
            minutes = 0;
        }

        // 格式化为两位数
        return String.format("%02d:%02d", hours, minutes);
    }

    public static void renderTutorial(GuiGraphics guiGraphics, LocalPlayer localPlayer, SMCPlayer smcPlayer, Font font, int lineHeight, int screenW, int screenH, int x, int y) {
        List<TutorialCondition> conditions = Arrays.asList(
                new TutorialCondition(
                        () -> !DataManager.firstGiftGot.get(localPlayer),
                        new Component[]{
                                SkilletManCoreMod.getInfo("find_villager_first").withStyle(ChatFormatting.BOLD, ChatFormatting.GOLD),
                                SkilletManCoreMod.getInfo("find_villager_first2").withStyle(ChatFormatting.GRAY),
                                SkilletManCoreMod.getInfo("find_villager_first3").withStyle(ChatFormatting.GRAY),
                                SkilletManCoreMod.getInfo("find_villager_first4").withStyle(ChatFormatting.GRAY)
                        }
                ),
                new TutorialCondition(
                        () -> !DataManager.firstWork.get(localPlayer),
                        new Component[]{
                                SkilletManCoreMod.getInfo("first_work").withStyle(ChatFormatting.BOLD, ChatFormatting.GREEN),
                                SkilletManCoreMod.getInfo("first_work2").withStyle(ChatFormatting.GRAY)
                        }
                ),
                new TutorialCondition(
                        () -> !DataManager.firstStopWork.get(localPlayer),
                        new Component[]{
                                SkilletManCoreMod.getInfo("first_stop_work").withStyle(ChatFormatting.BOLD, ChatFormatting.RED),
                                SkilletManCoreMod.getInfo("first_stop_work2").withStyle(ChatFormatting.GRAY)
                        }
                ),
                new TutorialCondition(
                        () -> !DataManager.firstGachaGot.get(localPlayer),
                        new Component[]{
                                SkilletManCoreMod.getInfo("find_villager_gacha").withStyle(ChatFormatting.BOLD, ChatFormatting.AQUA),
                                SkilletManCoreMod.getInfo("find_villager_gacha2").withStyle(ChatFormatting.GRAY),
                                SkilletManCoreMod.getInfo("find_villager_gacha3").withStyle(ChatFormatting.GRAY)
                        }
                ),
                new TutorialCondition(
                        () -> DataManager.trailRequired.get(localPlayer),
                        new Component[]{
                                SkilletManCoreMod.getInfo("trial_required").withStyle(ChatFormatting.BOLD, ChatFormatting.GOLD),
                                SkilletManCoreMod.getInfo("trial_required2").withStyle(ChatFormatting.GRAY),
                                SkilletManCoreMod.getInfo("trial_required3").withStyle(ChatFormatting.GRAY)
                        }
                ),
                new TutorialCondition(
                        () -> DataManager.showFirstPlaceWirelessTerminal.get(localPlayer),
                        new Component[]{
                                SkilletManCoreMod.getInfo("first_place_wireless_terminal").withStyle(ChatFormatting.BOLD, ChatFormatting.DARK_GREEN),
                                SkilletManCoreMod.getInfo("first_place_wireless_terminal1").withStyle(ChatFormatting.GRAY),
                                SkilletManCoreMod.getInfo("first_place_wireless_terminal2").withStyle(ChatFormatting.GRAY),
                                SkilletManCoreMod.getInfo("first_place_wireless_terminal3").withStyle(ChatFormatting.GRAY),
                                SkilletManCoreMod.getInfo("first_place_wireless_terminal4").withStyle(ChatFormatting.GRAY)
                        }
                ),
                new TutorialCondition(
                        () -> DataManager.shouldShowMachineTicketHint.get(localPlayer),
                        new Component[]{
                                SkilletManCoreMod.getInfo("should_trade_machine_ticket").withStyle(ChatFormatting.BOLD, ChatFormatting.YELLOW),
                                SkilletManCoreMod.getInfo("should_trade_machine_ticket2").withStyle(ChatFormatting.GRAY)
                        }
                ),
                new TutorialCondition(() -> DataManager.findBBQHint.get(localPlayer),
                        new Component[]{
                                SkilletManCoreMod.getInfo("find_bbq").withStyle(ChatFormatting.BOLD, ChatFormatting.GOLD),
                                SkilletManCoreMod.getInfo("find_bbq1").withStyle(ChatFormatting.GRAY)
                        }
                )
        );

        //隐藏提示
        MutableComponent show = Component.literal("§7§l[§r");
        boolean hasTodo = conditions.stream().anyMatch(c -> c.condition.get());
        if (SMCConfig.SHOW_HINT.get()) {
            show = SkilletManCoreMod.getInfo("press_x_to_show_hint",
                            KeyMappings.SHOW_HINT.getTranslatedKeyMessage().copy().withStyle(ChatFormatting.DARK_GREEN))
                    .withStyle(ChatFormatting.BOLD, ChatFormatting.GRAY);
        } else {
            show.append(KeyMappings.SHOW_HINT.getTranslatedKeyMessage().copy()
                            .withStyle(ChatFormatting.BOLD, ChatFormatting.DARK_GREEN)
                            .append("§7§l]"))
                    .withStyle(ChatFormatting.BOLD);
            if (hasTodo) {
                show.append(SkilletManCoreMod.getInfo("task_todo_tip"));
            }
        }

        if (DataManager.hintUpdated.get(localPlayer)) {
            show.append(SkilletManCoreMod.getInfo("hint_update_tip"))
                    .append((localPlayer.tickCount / 10) % 2 == 0 ?
                            Component.literal("⭐").withStyle(ChatFormatting.GOLD) : Component.empty());
        }

        guiGraphics.fillGradient(2, y + lineHeight - 2, 8 + font.width(show) + 2, y + lineHeight, 0x66000000, 0x66000000);
        guiGraphics.drawString(font, show, 4, y + lineHeight, 0x00ff00, true);

        if (!SMCConfig.SHOW_HINT.get()) {
            return;
        }

        boolean found = false;
        for (TutorialCondition condition : conditions) {
            if (condition.condition.get()) {
                renderCondition(guiGraphics, font, lineHeight, y, condition.components);
                found = true;
                break;
            }
        }

        if (!found) {
            Component[] defaultComponents = {
                    SkilletManCoreMod.getInfo("no_task").withStyle(ChatFormatting.BOLD, ChatFormatting.DARK_GRAY),
                    SkilletManCoreMod.getInfo("no_task1").withStyle(ChatFormatting.GRAY),
                    SkilletManCoreMod.getInfo("no_task2").withStyle(ChatFormatting.GRAY)
            };
            renderCondition(guiGraphics, font, lineHeight, y, defaultComponents);
        }
    }

    private static void renderCondition(GuiGraphics guiGraphics, Font font, int lineHeight, int y, Component[] components) {
        int maxWidth = 0;
        for (Component component : components) {
            maxWidth = Math.max(maxWidth, font.width(component));
        }

        int lines = components.length;
        guiGraphics.fillGradient(2, y + lineHeight * 2 - 2,
                8 + maxWidth + 2, y + lineHeight * (2 + lines), 0x66000000, 0x66000000);

        for (int i = 0; i < components.length; i++) {
            guiGraphics.drawString(font, components[i], 4,
                    y + lineHeight * (2 + i), 0x00ff00, true);
        }
    }

    private record TutorialCondition(Supplier<Boolean> condition, Component[] components) {
    }

}

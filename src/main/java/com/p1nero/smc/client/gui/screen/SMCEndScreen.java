package com.p1nero.smc.client.gui.screen;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.platform.GlStateManager.DestFactor;
import com.mojang.blaze3d.platform.GlStateManager.SourceFactor;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.logging.LogUtils;
import com.p1nero.smc.SkilletManCoreMod;
import com.p1nero.smc.client.gui.screen.component.LogoRenderer;
import com.p1nero.smc.client.sound.player.WinMusicPlayer;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import net.minecraft.ChatFormatting;
import net.minecraft.client.GameNarrator;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.Musics;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.RandomSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.stringtemplate.v4.ST;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class SMCEndScreen extends Screen {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final ResourceLocation VIGNETTE_LOCATION = ResourceLocation.parse("textures/misc/vignette.png");
    public static final ResourceLocation BACKGROUND_LOCATION = ResourceLocation.parse("textures/block/bricks.png");
    private static final Component SECTION_HEADING;
    private static final String OBFUSCATE_TOKEN;
    private final boolean poem;
    private final Runnable onFinished;
    private float scroll;
    private List<FormattedCharSequence> lines;
    private IntSet centeredLines;
    private int totalScrollLength;
    private boolean speedupActive;
    private final IntSet speedupModifiers = new IntOpenHashSet();
    private float scrollSpeed;
    private final float unmodifiedScrollSpeed;
    private int direction;
    private final LogoRenderer logoRenderer = new LogoRenderer(false);

    public SMCEndScreen(boolean poem, Runnable onFinished) {
        super(GameNarrator.NO_TITLE);
        this.poem = poem;
        this.onFinished = onFinished;
        if (!poem) {
            this.unmodifiedScrollSpeed = 0.75F;
        } else {
            this.unmodifiedScrollSpeed = 0.5F;
        }

        this.direction = 1;
        this.scrollSpeed = this.unmodifiedScrollSpeed;
    }

    private float calculateScrollSpeed() {
        return this.speedupActive ? this.unmodifiedScrollSpeed * (5.0F + (float) this.speedupModifiers.size() * 15.0F) * (float) this.direction : this.unmodifiedScrollSpeed * (float) this.direction;
    }

    public void tick() {
        if (this.minecraft == null) {
            return;
        }
        this.minecraft.getMusicManager().tick();
        this.minecraft.getSoundManager().tick(false);
        WinMusicPlayer.playWinMusic();
        float $$0 = (float) (this.totalScrollLength + this.height + this.height + 24);
        if (this.scroll > $$0) {
            this.respawn();
        }

    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 265) {
            this.direction = -1;
        } else if (keyCode != 341 && keyCode != 345) {
            if (keyCode == 32) {
                this.speedupActive = true;
            }
        } else {
            this.speedupModifiers.add(keyCode);
        }

        this.scrollSpeed = this.calculateScrollSpeed();
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 265) {
            this.direction = 1;
        }

        if (keyCode == 32) {
            this.speedupActive = false;
        } else if (keyCode == 341 || keyCode == 345) {
            this.speedupModifiers.remove(keyCode);
        }

        this.scrollSpeed = this.calculateScrollSpeed();
        return super.keyReleased(keyCode, scanCode, modifiers);
    }

    public void onClose() {
        this.respawn();
    }

    private void respawn() {
        this.onFinished.run();
    }

    protected void init() {
        if (this.lines == null) {
            this.lines = Lists.newArrayList();
            this.centeredLines = new IntOpenHashSet();
            String currentLang = this.minecraft.getLanguageManager().getSelected();
            if (this.poem && this.minecraft != null) {
                String endTextPath = SkilletManCoreMod.MOD_ID + ":texts/end_" + this.minecraft.getLanguageManager().getSelected() + ".txt";
                if (this.minecraft.getResourceManager().getResource(ResourceLocation.parse(endTextPath)).isPresent()) {
                    this.wrapCreditsIO(endTextPath, this::addPoemFile);
                } else {
                    this.wrapCreditsIO(SkilletManCoreMod.MOD_ID + ":texts/end_zh_cn.txt", this::addPoemFile);
                }
            }

            String creditsTextPath = SkilletManCoreMod.MOD_ID + ":texts/credits_" + currentLang + ".json";
            if (this.minecraft.getResourceManager().getResource(ResourceLocation.parse(creditsTextPath)).isPresent()) {
                this.wrapCreditsIO(SkilletManCoreMod.MOD_ID + ":texts/credits_" + currentLang + ".json", this::addCreditsFile);
            } else {
                this.wrapCreditsIO(SkilletManCoreMod.MOD_ID + ":texts/credits_zh_cn.json", this::addCreditsFile);
            }

            if (this.poem && this.minecraft != null) {
                String postTextPath = SkilletManCoreMod.MOD_ID + ":texts/postcredits_" + this.minecraft.getLanguageManager().getSelected() + ".txt";
                if (this.minecraft.getResourceManager().getResource(ResourceLocation.parse(postTextPath)).isPresent()) {
                    this.wrapCreditsIO(postTextPath, this::addPoemFile);
                } else {
                    this.wrapCreditsIO(SkilletManCoreMod.MOD_ID + ":texts/postcredits_zh_cn.txt", this::addPoemFile);
                }
            }

            this.totalScrollLength = this.lines.size() * 12;
        }
    }

    private void wrapCreditsIO(String creditsLocation, CreditsReader p_reader) {
        try {
            Reader reader = this.minecraft.getResourceManager().openAsReader(ResourceLocation.parse(creditsLocation));
            try {
                p_reader.read(reader);
            } catch (Throwable var7) {
                try {
                    reader.close();
                } catch (Throwable var6) {
                    var7.addSuppressed(var6);
                }

                throw var7;
            }

            reader.close();
        } catch (Exception var8) {
            LOGGER.error("Couldn't load credits", var8);
        }
    }

    private void addPoemFile(Reader reader) throws IOException {
        BufferedReader $$1 = new BufferedReader(reader);
        RandomSource $$2 = RandomSource.create(8124371L);

        String $$3;
        int $$4;
        while (($$3 = $$1.readLine()) != null) {
            String $$5;
            String $$6;
            for ($$3 = $$3.replaceAll("PLAYERNAME", this.minecraft.getUser().getName()); ($$4 = $$3.indexOf(OBFUSCATE_TOKEN)) != -1; $$3 = $$5 + ChatFormatting.WHITE + ChatFormatting.OBFUSCATED + "XXXXXXXX".substring(0, $$2.nextInt(4) + 3) + $$6) {
                $$5 = $$3.substring(0, $$4);
                $$6 = $$3.substring($$4 + OBFUSCATE_TOKEN.length());
            }

            this.addPoemLines($$3);
            this.addEmptyLine();
        }

        for ($$4 = 0; $$4 < 8; ++$$4) {
            this.addEmptyLine();
        }

    }

    private void addCreditsFile(Reader reader) {
        JsonArray $$1 = GsonHelper.parseArray(reader);
        Iterator var3 = $$1.iterator();

        while (var3.hasNext()) {
            JsonElement $$2 = (JsonElement) var3.next();
            JsonObject $$3 = $$2.getAsJsonObject();
            String $$4 = $$3.get("section").getAsString();
            this.addCreditsLine(SECTION_HEADING, true);
            this.addCreditsLine(Component.literal($$4).withStyle(ChatFormatting.YELLOW), true);
            this.addCreditsLine(SECTION_HEADING, true);
            this.addEmptyLine();
            this.addEmptyLine();
            JsonArray $$5 = $$3.getAsJsonArray("disciplines");
            Iterator var8 = $$5.iterator();

            while (var8.hasNext()) {
                JsonElement $$6 = (JsonElement) var8.next();
                JsonObject $$7 = $$6.getAsJsonObject();
                String $$8 = $$7.get("discipline").getAsString();
                if (StringUtils.isNotEmpty($$8)) {
                    this.addCreditsLine(Component.literal($$8).withStyle(ChatFormatting.YELLOW), true);
                    this.addEmptyLine();
                    this.addEmptyLine();
                }

                JsonArray $$9 = $$7.getAsJsonArray("titles");

                for (JsonElement $$10 : $$9) {
                    JsonObject $$11 = $$10.getAsJsonObject();
                    String $$12 = $$11.get("title").getAsString();
                    JsonArray $$13 = $$11.getAsJsonArray("names");
                    this.addCreditsLine(Component.literal($$12).withStyle(ChatFormatting.GRAY), false);

                    for (JsonElement $$14 : $$13) {
                        String name = $$14.getAsString();
                        this.addCreditsLine(Component.literal("           ").append(name.replaceAll("PLAYERNAME", this.minecraft.getUser().getName())).withStyle(ChatFormatting.WHITE), false);
                    }

                    this.addEmptyLine();
                    this.addEmptyLine();
                }
            }
        }

    }

    private void addEmptyLine() {
        this.lines.add(FormattedCharSequence.EMPTY);
    }

    private void addPoemLines(String text) {
        this.lines.addAll(this.minecraft.font.split(Component.literal(text), 256));
    }

    private void addCreditsLine(Component creditsLine, boolean centered) {
        if (centered) {
            this.centeredLines.add(this.lines.size());
        }

        this.lines.add(creditsLine.getVisualOrderText());
    }

    private void renderBg(GuiGraphics guiGraphics) {
        int $$1 = this.width;
        float $$2 = this.scroll * 0.5F;
        float $$4 = this.scroll / this.unmodifiedScrollSpeed;
        float $$5 = $$4 * 0.02F;
        float $$6 = (float) (this.totalScrollLength + this.height + this.height + 24) / this.unmodifiedScrollSpeed;
        float $$7 = ($$6 - 20.0F - $$4) * 0.005F;
        if ($$7 < $$5) {
            $$5 = $$7;
        }

        if ($$5 > 1.0F) {
            $$5 = 1.0F;
        }

        $$5 *= $$5;
        $$5 = $$5 * 96.0F / 255.0F;
        guiGraphics.setColor($$5, $$5, $$5, 1.0F);
        guiGraphics.blit(BACKGROUND_LOCATION, 0, 0, 0, 0.0F, $$2, $$1, this.height, 64, 64);
        guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
    }

    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.scroll = Math.max(0.0F, this.scroll + partialTick * this.scrollSpeed);
        this.renderBg(guiGraphics);
        int $$4 = this.width / 2 - 128;
        int $$5 = this.height + 50;
        float $$6 = -this.scroll;
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(0.0F, $$6, 0.0F);
        this.logoRenderer.renderLogo(guiGraphics, this.width, 1.0F, $$5);
        int $$7 = $$5 + 100;

        for (int $$8 = 0; $$8 < this.lines.size(); ++$$8) {
            if ($$8 == this.lines.size() - 1) {
                float $$9 = (float) $$7 + $$6 - (float) (this.height / 2 - 6);
                if ($$9 < 0.0F) {
                    guiGraphics.pose().translate(0.0F, -$$9, 0.0F);
                }
            }

            if ((float) $$7 + $$6 + 12.0F + 8.0F > 0.0F && (float) $$7 + $$6 < (float) this.height) {
                FormattedCharSequence $$10 = this.lines.get($$8);
                if (this.centeredLines.contains($$8)) {
                    guiGraphics.drawCenteredString(this.font, $$10, $$4 + 128, $$7, 16777215);
                } else {
                    guiGraphics.drawString(this.font, $$10, $$4, $$7, 16777215);
                }
            }

            $$7 += 12;
        }

        guiGraphics.pose().popPose();
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(SourceFactor.ZERO, DestFactor.ONE_MINUS_SRC_COLOR);
        guiGraphics.blit(VIGNETTE_LOCATION, 0, 0, 0, 0.0F, 0.0F, this.width, this.height, this.width, this.height);
        RenderSystem.disableBlend();
        RenderSystem.defaultBlendFunc();
        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    public Music getBackgroundMusic() {
        return null;
    }

    static {
        SECTION_HEADING = Component.literal("============").withStyle(ChatFormatting.WHITE);
        OBFUSCATE_TOKEN = "" + ChatFormatting.WHITE + ChatFormatting.OBFUSCATED + ChatFormatting.GREEN + ChatFormatting.AQUA;
    }

    @FunctionalInterface
    @OnlyIn(Dist.CLIENT)
    interface CreditsReader {
        void read(Reader var1) throws IOException;
    }
}

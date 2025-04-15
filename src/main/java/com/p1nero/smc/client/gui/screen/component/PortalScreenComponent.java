package com.p1nero.smc.client.gui.screen.component;

import com.p1nero.smc.SkilletManCoreMod;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.jetbrains.annotations.NotNull;

/**
 * 分布在四个角以及中心的按钮
 */
public class PortalScreenComponent extends Button {
    private boolean enable;

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public PortalScreenComponent(MutableComponent message, Button.OnPress onPress) {
        super(Button.builder(appendBrackets(message), onPress).pos(0, 0).size(0, 12).createNarration(DEFAULT_NARRATION));
        this.width = Minecraft.getInstance().font.width(this.getMessage()) + 2;
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        guiGraphics.fillGradient(this.getX(), this.getY(), this.getX() + this.width, this.getY() + this.height, 0x66000000, 0x66000000);
        guiGraphics.drawString(Minecraft.getInstance().font, this.getMessage(), this.getX() + 1, this.getY() + 1, this.isHovered() ? 0xFFFF55: 0xFFFFFF);
    }

    public static MutableComponent appendBrackets(MutableComponent component) {
        return Component.literal("[").append(component).append("]");
    }

    /**
     * 初步判断
     */
    @Override
    public void onPress() {
        if(!enable){
            if(Minecraft.getInstance().player != null){
                Minecraft.getInstance().player.displayClientMessage(SkilletManCoreMod.getInfo("teleport_lock"), true);
            }
        } else {
            super.onPress();
        }
    }

    /**
     * 上锁则显示上锁
     */
    @Override
    public @NotNull Component getMessage() {
        if(!enable){
            return SkilletManCoreMod.getInfo("teleport_lock").withStyle(ChatFormatting.BOLD, ChatFormatting.RED);
        }
        return super.getMessage();
    }
}

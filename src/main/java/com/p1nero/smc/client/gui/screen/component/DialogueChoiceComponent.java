package com.p1nero.smc.client.gui.screen.component;

import com.p1nero.smc.network.PacketRelay;
import com.p1nero.smc.network.SMCPacketHandler;
import com.p1nero.smc.network.packet.serverbound.AddDialogPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

/**
 * A button widget that allows the player to select a line of dialogue to say to an NPC.
 */
public class DialogueChoiceComponent extends Button {

    private boolean broadcast;

    public DialogueChoiceComponent(MutableComponent message, Button.OnPress onPress) {
        super(Button.builder(appendBrackets(message), onPress).pos(0, 0).size(0, 12).createNarration(DEFAULT_NARRATION));
        this.width = Minecraft.getInstance().font.width(this.getMessage()) + 2;
        this.broadcast = false;
    }

    public DialogueChoiceComponent(MutableComponent message, Button.OnPress onPress, boolean broadcast) {
        this(message, onPress);
        this.broadcast = broadcast;
    }

    /**
     * 添加到对话记录并全服广播
     */
    @Override
    public void onPress() {
        super.onPress();
        if(Minecraft.getInstance().player != null){
            PacketRelay.sendToServer(SMCPacketHandler.INSTANCE, new AddDialogPacket(Minecraft.getInstance().player.getDisplayName(), getMessage(), broadcast));
        }
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        guiGraphics.fillGradient(this.getX(), this.getY(), this.getX() + this.width, this.getY() + this.height, 0x66000000, 0x66000000);
        guiGraphics.drawString(Minecraft.getInstance().font, this.getMessage(), this.getX() + 1, this.getY() + 1, this.isHovered() ? 0xFFFF55: 0xFFFFFF);
    }

    public static MutableComponent appendBrackets(MutableComponent component) {
        return Component.literal("[").append(component).append("]");
    }
}

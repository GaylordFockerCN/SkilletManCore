package com.p1nero.smc.client.gui.screen;

import com.p1nero.smc.DOTEConfig;
import com.p1nero.smc.SkilletManCoreMod;
import com.p1nero.smc.client.gui.screen.component.DialogueAnswerComponent;
import com.p1nero.smc.client.gui.screen.component.DialogueChoiceComponent;
import com.p1nero.smc.entity.api.NpcDialogue;
import com.p1nero.smc.network.SMCPacketHandler;
import com.p1nero.smc.network.PacketRelay;
import com.p1nero.smc.network.packet.serverbound.AddDialogPacket;
import com.p1nero.smc.network.packet.serverbound.NpcPlayerInteractPacket;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * 改编自theAether 的 ValkyrieQueenDialogueScreen
 * 搬运了相关类
 */
public class DialogueScreen extends Screen {
    public static final ResourceLocation MY_BACKGROUND_LOCATION = new ResourceLocation(SkilletManCoreMod.MOD_ID,"textures/gui/background.png");
    protected final DialogueAnswerComponent dialogueAnswer;
    protected final Entity entity;
    public final int typewriterInterval;
    private int typewriterTimer = 0;
    EntityType<?> entityType;

    public DialogueScreen(Entity entity, EntityType<?> entityType) {
        super(entity.getDisplayName());
        typewriterInterval = DOTEConfig.TYPEWRITER_EFFECT_INTERVAL.get();
        this.dialogueAnswer = new DialogueAnswerComponent(this.buildDialogueAnswerName(entity.getDisplayName().copy().withStyle(ChatFormatting.YELLOW)).append(": "));
        this.entity = entity;
        this.entityType = entityType;
    }

    /**
     * 在这里实现对话逻辑调用
     */
    @Override
    protected void init() {
        positionDialogue();//不填的话用builder创造出来的对话框第一个对话会错误显示
    }

    public void setupDialogueChoices(List<DialogueChoiceComponent> options) {
        this.clearWidgets();
        for (DialogueChoiceComponent option : options) {
            this.addRenderableWidget(option);
        }
        this.positionDialogue();
    }

    /**
     * Repositions the Valkyrie Queen's dialogue answer and the player's dialogue choices based on the amount of choices.
     */
    protected void positionDialogue() {
        // Dialogue answer.
        this.dialogueAnswer.reposition(this.width, this.height * 5 / 4);//相较于天堂的下移了一点
        // Dialogue choices.
        int lineNumber = this.dialogueAnswer.height / 12 + 1;
        for (Renderable renderable : this.renderables) {
            if (renderable instanceof DialogueChoiceComponent option) {
                option.setX(this.width / 2 - option.getWidth() / 2);
                option.setY(this.height / 2 * 5 / 4 + 12 * lineNumber);//调低一点
                lineNumber++;
            }
        }
    }

    /**
     * 顺便发包同步记录，以及全服广播对话
     * Sets what message to display for a dialogue answer.
     * @param component The message {@link Component}.
     */
    protected void setDialogueAnswer(Component component) {
        PacketRelay.sendToServer(SMCPacketHandler.INSTANCE, new AddDialogPacket(entity.getDisplayName(), component, true));
        if(DOTEConfig.ENABLE_TYPEWRITER_EFFECT.get()){
            this.dialogueAnswer.updateTypewriterDialogue(component);
        }else {
            this.dialogueAnswer.updateDialogue(component);
        }

    }

    /**
     * Sets up the formatting for the Valkyrie Queen's name in the {@link DialogueAnswerComponent} widget.
     * @param component The name {@link Component}.
     * @return The formatted {@link MutableComponent}.
     */
    public MutableComponent buildDialogueAnswerName(Component component) {
        return Component.literal("[").append(component.copy().withStyle(ChatFormatting.YELLOW)).append("]");
    }

    /**
     * Sends an NPC interaction to the server, which is sent through a packet to be handled in {@link NpcDialogue#handleNpcInteraction(Player, byte)}.
     * @param interactionID A code for which interaction was performed on the client.<br>
     *                      0 - "What can you tell me about this place?"<br>
     *                      1 - "I wish to fight you!"<br>
     *                      2 - "On second thought, I'd rather not."<br>
     *                      3 - "Nevermind."<br>
     * @see NpcPlayerInteractPacket
     */
    protected void finishChat(byte interactionID) {
        PacketRelay.sendToServer(SMCPacketHandler.INSTANCE, new NpcPlayerInteractPacket(this.entity.getId(), interactionID));
        super.onClose();
    }

    /**
     * 发包但不关闭窗口
    * */
    protected void execute(byte interactionID) {
        PacketRelay.sendToServer(SMCPacketHandler.INSTANCE, new NpcPlayerInteractPacket(this.entity.getId(), interactionID));
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(guiGraphics);
        //guiGraphics.blit(MY_BACKGROUND_LOCATION, this.width/2 - 214/2, this.height/2 - 252/2, 0, 0, 214, 252);

        if(DOTEConfig.ENABLE_TYPEWRITER_EFFECT.get() && typewriterTimer < 0) {
            this.dialogueAnswer.updateTypewriterDialogue();
            positionDialogue();
            typewriterTimer = typewriterInterval;
        } else {
            typewriterTimer--;
        }

        this.dialogueAnswer.render(guiGraphics);

        //如果回答还没显示完则不渲染选项
        for(Renderable renderable : this.renderables) {
            if(renderable instanceof DialogueChoiceComponent && !dialogueAnswer.shouldRenderOption()){
                continue;
            }
            renderable.render(guiGraphics, mouseX, mouseY, partialTicks);
        }
    }

    /**
     * [CODE COPY] - {@link Screen#renderBackground(GuiGraphics)}.<br><br>
     * Remove code for dark gradient and dirt background.
     */
    @Override
    public void renderBackground(@NotNull GuiGraphics guiGraphics) {
        if (this.getMinecraft().level != null) {
//            guiGraphics.blit(MY_BACKGROUND_LOCATION, 0, 0, 0, 0.0F, 0.0F, this.width, this.height, 214, 252);
            MinecraftForge.EVENT_BUS.post(new ScreenEvent.BackgroundRendered(this, guiGraphics));
        }
    }

    @Override
    public void resize(@NotNull Minecraft minecraft, int width, int height) {
        this.width = width;
        this.height = height;
        this.positionDialogue();
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void onClose() {
        this.finishChat((byte) 0);
    }

}
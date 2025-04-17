package com.p1nero.smc.client.gui.screen.component;

import com.p1nero.smc.SMCConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;

import java.util.ArrayList;
import java.util.List;

/**
 * LZY:添加了实现打字机效果
 * A widget to handle an NPC's name and their response inside the dialogue screen.
 * @author LZY
 * @author The Aether
 */
public class DialogueAnswerComponent {
    //分割后的对话
    private final List<NpcDialogueElement> splitLines;
    //完整的分割后的对话
    private final List<NpcDialogueElement> fullSplitLines;
    private Component message;
    private final Component name;
    public int height;
    //打字机效果的下标
    private int index;
    //打字机效果的最大值
    private int max;
    private int maxWidth;

    private boolean shouldRenderOption = false;

    public DialogueAnswerComponent(Component name) {
        this.splitLines = new ArrayList<>();
        this.fullSplitLines = new ArrayList<>();
        this.name = name;
        this.updateDialogue(Component.empty());
    }

    public boolean shouldRenderOption() {
        if(!SMCConfig.ENABLE_TYPEWRITER_EFFECT.get()){
            return true;
        }
        return shouldRenderOption;
    }

    public Component getMessage() {
        return message;
    }

    public void render(GuiGraphics guiGraphics) {
        this.splitLines.forEach(element -> element.render(guiGraphics));
    }

    /**
     * Repositions the dialogue to the center of the screen.
     * 如果启动打字机效果，则所有文本按完整文本第一行出现的文本的最左侧定位。因为这样阅读比较舒服
     * 先保存一份完整文本，在沿用天堂的计算定位，我真是天才
     * @param width The {@link Integer} for the parent screen width.
     * @param height The {@link Integer} for the parent screen height.
     */
    public void reposition(int width, int height) {

        //不用Foreach是为了打字机效果能够跳过boss名
//        int i = 0;
//        for (NpcDialogueElement dialogue : this.splitLines) {
        for (int i = 0, j = 0 ;i < splitLines.size(); i++) {
            NpcDialogueElement dialogue = splitLines.get(i);
            dialogue.width = Minecraft.getInstance().font.width(dialogue.text) + 2;

//            int maxWidth = dialogue.width
//
//            //如果启动打字机效果，则以完整文本的位置来，即字从左到右出现，而不是从中间往两边延展。比较好看
//            if(TCRConfig.ENABLE_TYPEWRITER_EFFECT.get() && fullSplitLines.size() > 1 && i != 0){//因为第一个变量是NPC名字，所以要取下标1。
//                maxWidth = Minecraft.getInstance().font.width(fullSplitLines.get(1).text) + 2;
//            }

            if(SMCConfig.ENABLE_TYPEWRITER_EFFECT.get() && i != 0){//因为第一个变量是NPC名字，所以要取下标1。
                dialogue.x = width / 2 - maxWidth / 2;
            } else {
                dialogue.x = width / 2 - dialogue.width / 2;
            }
            dialogue.y = height / 2 + j * 12;
            j++;
        }
        this.height = this.splitLines.size() * 12;
    }

    public void updateDialogue(Component message) {
        updateSplitLines(splitLines, message);
    }

    private void updateSplitLines(List<NpcDialogueElement> pSplitLine, Component message){
        pSplitLine.clear();
        List<FormattedCharSequence> list = Minecraft.getInstance().font.split(name.copy().append(message), 300);
        this.height = list.size() * 12;
        list.forEach(text -> pSplitLine.add(new NpcDialogueElement(0, 0, 0, text)));
    }

    /**
     * 更新打字机效果的完整文本内容，并且执行一次打印机效果。
     */
    public void updateTypewriterDialogue(Component message) {
        this.message = message;
        updateSplitLines(fullSplitLines,message);

        //以最长那句话的最左边为最左边。
        maxWidth = 0;
        for(NpcDialogueElement element:fullSplitLines){
            maxWidth = Math.max(Minecraft.getInstance().font.width(element.text) + 2, maxWidth);
        }

        shouldRenderOption = false;
//        index = name.getString().length();//名字就不用打字机了
        index = 0;
        max = message.getString().length();
        updateTypewriterDialogue();
    }

    /**
     * 添加打字机效果，一次更新一个字
     * 不知道为什么Component提供根据下标截取String的方法，太感人了
     * @return 字是否全显示完了
     */
    public void updateTypewriterDialogue() {
        Style style = message.getStyle();
        updateDialogue(Component.literal(message.getString(index)).withStyle(style));
        index += SMCConfig.TYPEWRITER_EFFECT_SPEED.get();
        if(index > max){
            index = max;
            shouldRenderOption = true;
        }
    }

    /**
     * This inner class is used to store data for each line of text.
     */
    public static class NpcDialogueElement {
        private final FormattedCharSequence text;
        private int x;
        private int y;
        private int width;

        public NpcDialogueElement(int x, int y, int width, FormattedCharSequence text) {
            this.text = text;
            this.x = x;
            this.y = y;
            this.width = width;
        }

        public void render(GuiGraphics guiGraphics) {
            guiGraphics.fillGradient(this.x, this.y, this.x + width, this.y + 12, 0x66000000, 0x66000000);
            guiGraphics.drawString(Minecraft.getInstance().font, this.text, this.x + 1, this.y + 1, 0xFFFFFF);
        }
    }
}

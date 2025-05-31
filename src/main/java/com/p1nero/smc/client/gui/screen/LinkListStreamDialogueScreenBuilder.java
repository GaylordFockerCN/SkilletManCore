package com.p1nero.smc.client.gui.screen;

import com.p1nero.smc.client.gui.DialogueComponentBuilder;
import com.p1nero.smc.client.gui.TreeNode;
import com.p1nero.smc.client.gui.screen.component.DialogueChoiceComponent;
import com.p1nero.smc.entity.api.NpcDialogue;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * 用多叉树来优化流式对话框（我自己起的名词，就是没有多个分支几乎都是一条直线的对话，不过好像带有分支的也可以用？
 * 如果要构建树状对话就手动设置answerRoot即可
 * 从Command中得到启发{@link net.minecraft.commands.Commands}
 *
 * @author P1nero
 */
public class LinkListStreamDialogueScreenBuilder {

    protected DialogueScreen screen;//封装一下防止出现一堆杂七杂八的方法
    private TreeNode answerRoot;
    private TreeNode answerNode;
    @Nullable
    private EntityType<?> entityType;

    public LinkListStreamDialogueScreenBuilder(@Nullable Entity entity, Component name) {
        screen = new DialogueScreen(name, entity);
        if (entity != null) {
            this.entityType = entity.getType();
        }
        init();
    }

    public LinkListStreamDialogueScreenBuilder(Entity entity) {
        screen = new DialogueScreen(entity);
        this.entityType = entity.getType();
        init();
    }

    public LinkListStreamDialogueScreenBuilder(EntityType<?> entityType) {
        screen = new DialogueScreen(entityType.getDescription(), null);
        this.entityType = entityType;
        init();
    }

    public boolean isEmpty() {
        return answerRoot == null;
    }

    /**
     * 用于构建树状对话
     */
    public void setAnswerRoot(TreeNode root) {
        this.answerRoot = root;
    }

    public void setYOffset(int yOffset) {
        this.screen.setYOffset(yOffset);
    }

    public void setSilent(boolean silent) {
        this.screen.setSilent(silent);
    }

    /**
     * 重写这个是为了让你记得这才是Screen真正被调用的初始化的地方。建议在这里作些判断再调用start。
     */
    public LinkListStreamDialogueScreenBuilder init() {
        return this;
    }

    /**
     * 初始化对话框，得先start才能做后面的操作
     *
     * @param greeting 初始时显示的话
     */
    public LinkListStreamDialogueScreenBuilder start(Component greeting) {
        answerRoot = new TreeNode(greeting);
        answerNode = answerRoot;
        return this;
    }

    /**
     * 初始化对话框，得先start才能做后面的操作
     *
     * @param greeting 初始时显示的话的编号
     */
    public LinkListStreamDialogueScreenBuilder start(int greeting) {
        return start(DialogueComponentBuilder.BUILDER.ans(entityType, greeting));
    }

    /**
     * @param finalOption 最后显示的话
     * @param returnValue 选项的返回值，默认返回0。用于处理 {@link NpcDialogue#handleNpcInteraction(ServerPlayer, byte)}
     */
    public LinkListStreamDialogueScreenBuilder addFinalChoice(Component finalOption, byte returnValue) {
        if (answerNode == null)
            return null;
        answerNode.addChild(new TreeNode.FinalNode(finalOption, returnValue));
        return this;
    }

    public LinkListStreamDialogueScreenBuilder addFinalChoice(Component finalOption) {
        return addFinalChoice(finalOption, (byte) 0);
    }

    /**
     * @param finalOption 最后显示的话
     * @param returnValue 选项的返回值，默认返回0。用于处理 {@link NpcDialogue#handleNpcInteraction(ServerPlayer, byte)}
     */
    public LinkListStreamDialogueScreenBuilder addFinalChoice(int finalOption, byte returnValue) {
        return addFinalChoice(DialogueComponentBuilder.BUILDER.opt(entityType, finalOption), returnValue);
    }

    public LinkListStreamDialogueScreenBuilder addFinalChoice(int finalOption) {
        return addFinalChoice(finalOption, (byte) 0);
    }

    /**
     * 添加选项进树并返回下一个节点
     *
     * @param option 该选项的内容
     * @param answer 选择该选项后的回答内容
     */
    public LinkListStreamDialogueScreenBuilder addChoice(Component option, Component answer) {
        if (answerNode == null)
            return null;
        answerNode.addChild(answer, option);

        //直接下一个
        List<TreeNode> list = answerNode.getChildren();
        if (!(list.size() == 1 && list.get(0) instanceof TreeNode.FinalNode)) {
            answerNode = list.get(0);
        }

        return this;
    }

    /**
     * 添加选项进树并返回当前节点
     *
     * @param option 该选项的内容
     * @param answer 选择该选项后的回答内容
     */
    public LinkListStreamDialogueScreenBuilder addChoiceAndStayCurrent(Component option, Component answer) {
        if (answerNode == null)
            return null;
        answerNode.addChild(answer, option);
        return this;
    }

    /**
     * 使用BUILDER构建
     * 添加选项进树并返回下一个节点
     *
     * @param option 该选项的内容编号
     * @param answer 选择该选项后的回答内容编号
     */
    public LinkListStreamDialogueScreenBuilder addChoice(int option, int answer) {
        return addChoice(DialogueComponentBuilder.BUILDER.opt(entityType, option), DialogueComponentBuilder.BUILDER.ans(entityType, answer));
    }

    /**
     * 按下按钮后执行
     */
    public LinkListStreamDialogueScreenBuilder thenExecute(Consumer<DialogueScreen> consumer) {
        if (answerNode == null)
            return null;
        answerNode.addExecutable(consumer);
        return this;
    }

    /**
     * 按下按钮后执行。记得在handle的时候不要把玩家设置为null，提前返回，否则可能中断对话！
     */
    public LinkListStreamDialogueScreenBuilder thenExecute(byte returnValue) {
        answerNode.addExecutable(returnValue);
        return this;
    }

    /**
     * 根据树来建立套娃按钮
     */
    public DialogueScreen build() {
        if (answerRoot == null)
            return screen;
        screen.setDialogueAnswer(answerRoot.getAnswer());
        if (answerRoot.canExecute()) {
            answerRoot.execute(screen);
        }
        if (answerRoot.canExecuteCode()) {
            if (answerRoot.getExecuteValue() == 0) {
                throw new IllegalArgumentException("The return value '0' is for ESC");
            }
            screen.execute(answerRoot.getExecuteValue());
        }
        List<DialogueChoiceComponent> choiceList = new ArrayList<>();
        for (TreeNode child : answerRoot.getChildren()) {
            choiceList.add(new DialogueChoiceComponent(child.getOption().copy(), createChoiceButton(child)));
        }
        screen.setupDialogueChoices(choiceList);
        return screen;
    }

    /**
     * 递归添加按钮。放心如果遇到没有添加选项的节点会自动帮你添加一个返回空内容返回值为0的FinalNode。
     */
    private Button.OnPress createChoiceButton(TreeNode node) {

        //如果是终止按钮则实现返回效果
        if (node instanceof TreeNode.FinalNode finalNode) {
            return button -> {
                if (finalNode.canExecute()) {
                    finalNode.execute(screen);
                }
                screen.finishChat(finalNode.getReturnValue());
            };
        }

        //否则继续递归创建按钮
        return button -> {
            //防止还没显示的时候点击到
            if(!screen.dialogueAnswer.shouldRenderOption()) {
                return;
            }
            screen.setYOffset(0);
            if (node.canExecute()) {
                node.execute(screen);
            }
            if (node.canExecuteCode()) {
                if (node.getExecuteValue() == 0) {
                    throw new IllegalArgumentException("The return value '0' is for ESC");
                }
                screen.execute(node.getExecuteValue());
            }
            screen.setDialogueAnswer(node.getAnswer());
            screen.playSound();
            List<DialogueChoiceComponent> choiceList = new ArrayList<>();
            List<TreeNode> options = node.getChildren();
            if (options == null) {
                options = new ArrayList<>();
                options.add(new TreeNode.FinalNode(Component.empty(), (byte) 0));
            }
            for (TreeNode child : options) {
                choiceList.add(new DialogueChoiceComponent(child.getOption().copy(), createChoiceButton(child)));
            }
            screen.setupDialogueChoices(choiceList);
        };
    }

}

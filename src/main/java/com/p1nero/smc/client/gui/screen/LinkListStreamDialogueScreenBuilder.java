package com.p1nero.smc.client.gui.screen;

import com.p1nero.smc.client.gui.DialogueComponentBuilder;
import com.p1nero.smc.client.gui.TreeNode;
import com.p1nero.smc.client.gui.screen.component.DialogueChoiceComponent;
import com.p1nero.smc.entity.api.NpcDialogue;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * 用多叉树来优化流式对话框（我自己起的名词，就是没有多个分支几乎都是一条直线的对话，不过好像带有分支的也可以用？
 * 如果要构建树状对话就手动设置answerRoot即可
 * 从Command中得到启发{@link net.minecraft.commands.Commands}
 *
 * @author LZY
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
    }public LinkListStreamDialogueScreenBuilder(EntityType<?> entityType) {
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
        return start(DialogueComponentBuilder.BUILDER.buildDialogueAnswer(entityType, greeting));
    }

    /**
     * @param finalOption 最后显示的话
     * @param returnValue 选项的返回值，默认返回0。用于处理 {@link NpcDialogue#handleNpcInteraction(Player, byte)}
     */
    public LinkListStreamDialogueScreenBuilder addFinalChoice(Component finalOption, byte returnValue) {
        if (answerNode == null)
            return null;
        answerNode.addChild(new TreeNode.FinalNode(finalOption, returnValue));
        return this;
    }

    /**
     * @param finalOption 最后显示的话
     * @param returnValue 选项的返回值，默认返回0。用于处理 {@link NpcDialogue#handleNpcInteraction(Player, byte)}
     */
    public LinkListStreamDialogueScreenBuilder addFinalChoice(int finalOption, byte returnValue) {
        return addFinalChoice(DialogueComponentBuilder.BUILDER.buildDialogueOption(entityType, finalOption), returnValue);
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
     * 使用BUILDER构建
     * 添加选项进树并返回下一个节点
     *
     * @param option 该选项的内容编号
     * @param answer 选择该选项后的回答内容编号
     */
    public LinkListStreamDialogueScreenBuilder addChoice(int option, int answer) {
        return addChoice(DialogueComponentBuilder.BUILDER.buildDialogueOption(entityType, option), DialogueComponentBuilder.BUILDER.buildDialogueAnswer(entityType, answer));
    }

    /**
     * 按下按钮后执行
     */
    public LinkListStreamDialogueScreenBuilder thenExecute(Runnable runnable) {
        if (answerNode == null)
            return null;
        answerNode.execute(runnable);
        return this;
    }

    /**
     * 按下按钮后执行。记得在handle的时候不要把玩家设置为null，提前返回，否则可能中断对话！
     */
    public LinkListStreamDialogueScreenBuilder thenExecute(byte returnValue) {
        answerNode.execute(returnValue);
        return this;
    }

    /**
     * 根据树来建立套娃按钮
     */
    public DialogueScreen build() {
        if (answerRoot == null)
            return screen;
        screen.setDialogueAnswer(answerRoot.getAnswer());
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
                screen.finishChat(finalNode.getReturnValue());
                if (finalNode.canExecute()) {
                    finalNode.execute();
                }
            };
        }

        //否则继续递归创建按钮
        return button -> {
            if (node.canExecute()) {
                node.execute();
            }
            if (node.canExecuteCode()) {
                if (node.getExecuteValue() == 0) {
                    throw new IllegalArgumentException("The return value '0' is for ESC");
                }
                screen.execute(node.getExecuteValue());
            }
            screen.setDialogueAnswer(node.getAnswer());
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

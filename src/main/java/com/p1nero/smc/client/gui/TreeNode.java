package com.p1nero.smc.client.gui;

import com.p1nero.smc.client.gui.screen.DialogueScreen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * 为什么就没有现成的库呢因为太简单了吗
 */
public class TreeNode {

    protected Component answer;
    protected Component option = Component.empty();

    @Nullable
    protected Consumer<DialogueScreen> screenConsumer;//要执行的操作

    public byte getExecuteValue() {
        return executeValue;
    }

    protected byte executeValue = (byte) -114514;//要执行的操作代码 ，114514 代表无操作

    protected List<TreeNode> options = new ArrayList<>();

    /**
     * 根节点不应该有选项。
     * */
    public TreeNode(Component answer) {
        this.answer = answer;
    }

    public TreeNode(Component answer, Component option) {
        this.answer = answer;
        this.option = option;
    }

    public TreeNode addLeaf(Component option, byte returnValue) {
        options.add(new TreeNode.FinalNode(option, returnValue));
        return this;
    }

    /**
     * 默认的情况。负数不会被处理
     */
    public TreeNode addLeaf(Component option) {
        options.add(new TreeNode.FinalNode(option, (byte) -1));
        return this;
    }

    public TreeNode addChild(Component answer, Component option) {
        options.add(new TreeNode(answer, option));
        return this;
    }

    public TreeNode addChild(TreeNode node) {
        options.add(node);
        return this;
    }

    public TreeNode addExecutable(Consumer<DialogueScreen> runnable) {
        this.screenConsumer = runnable;
        return this;
    }

    public TreeNode addExecutable(byte executeValue) {
        this.executeValue = executeValue;
        return this;
    }

    public void execute(DialogueScreen screen) {
        if(screenConsumer != null){
            screenConsumer.accept(screen);
        }
    }

    public boolean canExecute(){
        return screenConsumer !=null;
    }

    public boolean canExecuteCode(){
        return executeValue != (byte)-114514;
    }

    public Component getAnswer() {
        return answer;
    }

    public Component getOption() {
        return option;
    }

    public List<TreeNode> getChildren(){
        return options;
    }

    public static class FinalNode extends TreeNode{
        private final byte returnValue;
        public FinalNode(Component finalOption, byte returnValue) {
            super(Component.empty());//最终节点不需要回答
            this.option = finalOption;
            this.returnValue = returnValue;
        }

        public byte getReturnValue(){
            return returnValue;
        }

    }

}


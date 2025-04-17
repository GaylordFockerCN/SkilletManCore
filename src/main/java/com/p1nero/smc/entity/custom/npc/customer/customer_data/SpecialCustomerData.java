package com.p1nero.smc.entity.custom.npc.customer.customer_data;

import com.p1nero.smc.client.gui.DialogueComponentBuilder;
import com.p1nero.smc.client.gui.TreeNode;
import com.p1nero.smc.client.gui.screen.LinkListStreamDialogueScreenBuilder;
import com.p1nero.smc.datagen.lang.SMCLangGenerator;
import com.p1nero.smc.entity.custom.npc.customer.Customer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;

public abstract class SpecialCustomerData extends Customer.CustomerData {

    @Override
    public void generateTranslation(SMCLangGenerator generator) {

    }

    @Override
    public void getDialogScreen(CompoundTag serverData, LinkListStreamDialogueScreenBuilder screenBuilder, DialogueComponentBuilder dialogueComponentBuilder) {
        TreeNode root = new TreeNode(dialogueComponentBuilder.buildDialogueAnswer(-1));
        this.append(root, serverData, dialogueComponentBuilder);
        screenBuilder.setAnswerRoot(root);
    }

    protected abstract void append(TreeNode root, CompoundTag serverData, DialogueComponentBuilder dialogueComponentBuilder);

    @Override
    public void handle(ServerPlayer serverPlayer, int interactId) {

    }
}

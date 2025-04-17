package com.p1nero.smc.entity.custom.npc.customer.customer_data;

import com.p1nero.smc.client.gui.DialogueComponentBuilder;
import com.p1nero.smc.client.gui.screen.LinkListStreamDialogueScreenBuilder;
import com.p1nero.smc.datagen.lang.SMCLangGenerator;
import com.p1nero.smc.entity.custom.npc.customer.Customer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;

public class SimpleCustomerData extends Customer.CustomerData {
    @Override
    public void generateTranslation(SMCLangGenerator generator) {

    }

    @Override
    public void getDialogScreen(CompoundTag serverData, LinkListStreamDialogueScreenBuilder screenBuilder, DialogueComponentBuilder dialogueComponentBuilder) {

    }

    @Override
    public void handle(ServerPlayer serverPlayer, int interactId) {

    }
}

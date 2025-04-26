package com.p1nero.smc.client.gui.screen.entity_dialog.animal;

import com.p1nero.smc.client.gui.screen.LinkListStreamDialogueScreenBuilder;
import com.p1nero.smc.datagen.lang.SMCLangGenerator;
import com.p1nero.smc.network.packet.serverbound.NpcPlayerInteractPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class WolfDialogScreenHandler {

    @OnlyIn(Dist.CLIENT)
    public static void addDialogScreen(Wolf wolf) {
        LinkListStreamDialogueScreenBuilder screenBuilder = new LinkListStreamDialogueScreenBuilder(wolf);
        screenBuilder.start(0).addFinalChoice(0, (byte) NpcPlayerInteractPacket.DO_NOTHING);
        Minecraft.getInstance().setScreen(screenBuilder.build());
    }

    public static void onGenerateLang(SMCLangGenerator generator) {
        generator.addDialogEntityName(EntityType.WOLF, "看起来很痛苦的狼");
        generator.addDialog(EntityType.WOLF, 0, "汪！你是要毒死我吗！（哈吉汪发出愤怒的声音）");
        generator.addDialogChoice(EntityType.WOLF, 0, "端走");
    }
}

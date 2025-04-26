package com.p1nero.smc.client.gui.screen.entity_dialog.animal;

import com.p1nero.smc.client.gui.screen.LinkListStreamDialogueScreenBuilder;
import com.p1nero.smc.datagen.lang.SMCLangGenerator;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Cow;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class CowDialogScreenHandler {
    @OnlyIn(Dist.CLIENT)
    public static void addDialogScreen(Cow sheep) {
        LinkListStreamDialogueScreenBuilder screenBuilder = new LinkListStreamDialogueScreenBuilder(sheep);
        screenBuilder.start(0)
                        .addChoice(0, 1)
                                .addFinalChoice(1, (byte) -1);
        Minecraft.getInstance().setScreen(screenBuilder.build());
    }

    public static void onGenerateLang(SMCLangGenerator generator) {
        generator.addDialog(EntityType.COW, 0, "哞~");
        generator.addDialogChoice(EntityType.COW, 0, "尝试对着它哞");
        generator.addDialog(EntityType.COW, 1, "哞？（看来作者没有给他添加有价值的对话，还是离开吧）");
        generator.addDialogChoice(EntityType.COW, 1, "离去");
    }

}

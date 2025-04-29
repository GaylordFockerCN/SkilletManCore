package com.p1nero.smc.client.gui.screen.entity_dialog.animal;

import com.p1nero.smc.client.gui.screen.LinkListStreamDialogueScreenBuilder;
import com.p1nero.smc.datagen.lang.SMCLangGenerator;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ChickenDialogScreenHandler {
    @OnlyIn(Dist.CLIENT)
    public static void addDialogScreen(Chicken chicken) {
        LinkListStreamDialogueScreenBuilder screenBuilder = new LinkListStreamDialogueScreenBuilder(chicken);
        screenBuilder.start(0)
                                .addFinalChoice(0, (byte) -1);
        Minecraft.getInstance().setScreen(screenBuilder.build());
    }

    public static void onGenerateLang(SMCLangGenerator generator) {
        generator.addDialog(EntityType.CHICKEN, 0, "你干嘛~哈哈哎哟");
        generator.addDialogChoice(EntityType.CHICKEN, 0, "作者你露出鸡脚了");
    }

}

package com.p1nero.smc.datagen.sound;

import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;

public class SMCSoundGenerator extends SMCSoundProvider {

    public SMCSoundGenerator(PackOutput output, ExistingFileHelper helper) {
        super(output, helper);
    }

    @Override
    public void registerSounds() {
    }
}

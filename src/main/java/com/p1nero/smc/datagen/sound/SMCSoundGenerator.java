package com.p1nero.smc.datagen.sound;

import com.p1nero.smc.client.sound.SMCSounds;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;

public class SMCSoundGenerator extends SMCSoundProvider {

    public SMCSoundGenerator(PackOutput output, ExistingFileHelper helper) {
        super(output, helper);
    }

    @Override
    public void registerSounds() {
        generateNewSoundWithSubtitle(SMCSounds.WORKING_BGM, "working_bgm", 2);
    }
}

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
        generateNewSoundWithSubtitle(SMCSounds.VILLAGER_YES, "villager_yes", 1);
        generateNewSoundWithSubtitle(SMCSounds.EARN_MONEY, "earn_money", 1);
        generateNewSoundWithSubtitle(SMCSounds.DRUMBEAT_1, "drumbeat1", 1);
        generateNewSoundWithSubtitle(SMCSounds.DRUMBEAT_2, "drumbeat2", 1);
        generateNewSoundWithSubtitle(SMCSounds.DRUMBEAT_3, "drumbeat3", 1);
        generateNewSoundWithSubtitle(SMCSounds.WORKING_BGM, "bgm/working_bgm", 2);
        generateNewSoundWithSubtitle(SMCSounds.RAID_BGM, "bgm/raid_bgm", 1);
        generateNewSoundWithSubtitle(SMCSounds.WIN_BGM, "bgm/win_bgm", 1);
        generateNewSoundWithSubtitle(SMCSounds.TALKING, "talking", 2);
    }
}

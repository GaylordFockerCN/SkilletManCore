package com.p1nero.smc.datagen.sound;

import com.p1nero.smc.SkilletManCoreMod;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.SoundDefinition;
import net.minecraftforge.common.data.SoundDefinitionsProvider;
import net.minecraftforge.registries.RegistryObject;

public abstract class SMCSoundProvider extends SoundDefinitionsProvider {

    protected SMCSoundProvider(PackOutput output, ExistingFileHelper helper) {
        super(output, SkilletManCoreMod.MOD_ID, helper);
    }

    public void generateNewSoundWithSubtitle(RegistryObject<SoundEvent> event, String baseSoundDirectory, int numberOfSounds) {
        generateNewSound(event, baseSoundDirectory, numberOfSounds, true);
    }

    public void generateNewSound(RegistryObject<SoundEvent> event, String baseSoundDirectory, int numberOfSounds, boolean subtitle) {
        SoundDefinition definition = SoundDefinition.definition();
        if (subtitle) {
            String[] splitSoundName = event.getId().getPath().split("\\.", 3);
            definition.subtitle("subtitles." + SkilletManCoreMod.MOD_ID + "." + splitSoundName[0] + "." + splitSoundName[2]);
        }
        for (int i = 1; i <= numberOfSounds; i++) {
            definition.with(SoundDefinition.Sound.sound(ResourceLocation.fromNamespaceAndPath(SkilletManCoreMod.MOD_ID, baseSoundDirectory + (numberOfSounds > 1 ? "_" + i : "")), SoundDefinition.SoundType.SOUND));
        }
        this.add(event, definition);
    }

    public void generateNewSoundMC(RegistryObject<SoundEvent> event, String baseSoundDirectory, int numberOfSounds, boolean subtitle) {
        SoundDefinition definition = SoundDefinition.definition();
        if (subtitle) {
            String[] splitSoundName = event.getId().getPath().split("\\.", 3);
            definition.subtitle("subtitles." + SkilletManCoreMod.MOD_ID + "." + splitSoundName[0] + "." + splitSoundName[2]);
        }
        for (int i = 1; i <= numberOfSounds; i++) {
            definition.with(SoundDefinition.Sound.sound(ResourceLocation.parse(baseSoundDirectory + (numberOfSounds > 1 ? i : "")), SoundDefinition.SoundType.SOUND));
        }
        this.add(event, definition);
    }

    public void generateExistingSoundWithSubtitle(RegistryObject<SoundEvent> event, SoundEvent referencedSound) {
        this.generateExistingSound(event, referencedSound, true);
    }

    public void generateSoundWithCustomSubtitle(RegistryObject<SoundEvent> event, SoundEvent referencedSound, String subtitle) {
        this.add(event, SoundDefinition.definition()
                .subtitle(subtitle)
                .with(SoundDefinition.Sound.sound(referencedSound.getLocation(), SoundDefinition.SoundType.EVENT)));
    }

    public void generateExistingSound(RegistryObject<SoundEvent> event, SoundEvent referencedSound, boolean subtitle) {
        SoundDefinition definition = SoundDefinition.definition();
        if (subtitle) {
            String[] splitSoundName = event.getId().getPath().split("\\.", 3);
            definition.subtitle("subtitles." + SkilletManCoreMod.MOD_ID + "." + splitSoundName[0] + "." + splitSoundName[2]);
        }
        this.add(event, definition
                .with(SoundDefinition.Sound.sound(referencedSound.getLocation(), SoundDefinition.SoundType.EVENT)));
    }

    public void makeStepSound(RegistryObject<SoundEvent> event, SoundEvent referencedSound) {
        this.add(event, SoundDefinition.definition()
                .subtitle("subtitles.block.generic.footsteps")
                .with(SoundDefinition.Sound.sound(referencedSound.getLocation(), SoundDefinition.SoundType.EVENT)));
    }

    public void makeMusicDisc(RegistryObject<SoundEvent> event, String discName) {
        this.add(event, SoundDefinition.definition()
                .with(SoundDefinition.Sound.sound(ResourceLocation.fromNamespaceAndPath(SkilletManCoreMod.MOD_ID, "music/" + discName), SoundDefinition.SoundType.SOUND)
                        .stream()));
    }

    public void generateParrotSound(RegistryObject<SoundEvent> event, SoundEvent referencedSound) {
        SoundDefinition definition = SoundDefinition.definition();
        String[] splitSoundName = event.getId().getPath().split("\\.", 3);
        definition.subtitle("subtitles." + SkilletManCoreMod.MOD_ID + "." + splitSoundName[0] + "." + splitSoundName[2]);

        this.add(event, definition
                .with(SoundDefinition.Sound.sound(referencedSound.getLocation(), SoundDefinition.SoundType.EVENT).pitch(1.8F).volume(0.6F)));
    }
}

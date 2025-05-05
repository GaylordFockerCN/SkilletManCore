package com.p1nero.smc.client.sound;

import com.p1nero.smc.SkilletManCoreMod;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class SMCSounds {

	public static final DeferredRegister<SoundEvent> REGISTRY = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, SkilletManCoreMod.MOD_ID);

	public static final RegistryObject<SoundEvent> VILLAGER_YES = createEvent("sound.smc.villager_yes");
	public static final RegistryObject<SoundEvent> EARN_MONEY = createEvent("sound.smc.earn_money");
	public static final RegistryObject<SoundEvent> WORKING_BGM = createEvent("bgm.smc.working_bgm");
	public static final RegistryObject<SoundEvent> DRUMBEAT_1 = createEvent("sound.smc.drumbeat1");
	public static final RegistryObject<SoundEvent> DRUMBEAT_2 = createEvent("sound.smc.drumbeat2");
	public static final RegistryObject<SoundEvent> DRUMBEAT_3 = createEvent("sound.smc.drumbeat3");
	private static RegistryObject<SoundEvent> createEvent(String sound) {
		return REGISTRY.register(sound, () -> SoundEvent.createVariableRangeEvent(SkilletManCoreMod.prefix(sound)));
	}
}

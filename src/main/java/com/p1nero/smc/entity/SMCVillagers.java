package com.p1nero.smc.entity;

import com.google.common.collect.ImmutableSet;
import com.p1nero.smc.SkilletManCoreMod;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * 仅提供皮肤
 */
public class SMCVillagers {
    public static final DeferredRegister<PoiType> POI_TYPES =
            DeferredRegister.create(ForgeRegistries.POI_TYPES, SkilletManCoreMod.MOD_ID);
    public static final DeferredRegister<VillagerProfession> VILLAGER_PROFESSIONS =
            DeferredRegister.create(ForgeRegistries.VILLAGER_PROFESSIONS, SkilletManCoreMod.MOD_ID);

    public static final RegistryObject<VillagerProfession> HE_SHEN =
            VILLAGER_PROFESSIONS.register("he_shen", () -> new VillagerProfession("he_shen", holder -> false, holder -> false,
                    ImmutableSet.of(), ImmutableSet.of(), null));

    public static final RegistryObject<VillagerProfession> KID =
            VILLAGER_PROFESSIONS.register("kid", () -> new VillagerProfession("kid", holder -> false, holder -> false,
                    ImmutableSet.of(), ImmutableSet.of(), null));
    public static final RegistryObject<VillagerProfession> THIEF1 =
            VILLAGER_PROFESSIONS.register("thief1", () -> new VillagerProfession("thief1", holder -> false, holder -> false,
                    ImmutableSet.of(), ImmutableSet.of(), null));
    public static final RegistryObject<VillagerProfession> THIEF2 =
            VILLAGER_PROFESSIONS.register("thief2", () -> new VillagerProfession("thief2", holder -> false, holder -> false,
                    ImmutableSet.of(), ImmutableSet.of(), null));
    public static void register(IEventBus eventBus) {
        POI_TYPES.register(eventBus);
        VILLAGER_PROFESSIONS.register(eventBus);
    }
}
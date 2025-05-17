package com.p1nero.smc.condition;

import com.p1nero.smc.SkilletManCoreMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import yesman.epicfight.data.conditions.Condition;

import java.util.function.Supplier;

public class SMCConditions {
    public static final DeferredRegister<Supplier<Condition<?>>> CONDITIONS = DeferredRegister.create(ResourceLocation.fromNamespaceAndPath("epicfight", "conditions"), SkilletManCoreMod.MOD_ID);
    private static final RegistryObject<Supplier<Condition<?>>> WORLD_LEVEL_CHECK_CONDITION = CONDITIONS.register((ResourceLocation.fromNamespaceAndPath(SkilletManCoreMod.MOD_ID, "world_level_check")).getPath(), () -> WorldLevelCheckCondition::new);
    private static final RegistryObject<Supplier<Condition<?>>> PLAYER_COUNT_CHECK_CONDITION = CONDITIONS.register((ResourceLocation.fromNamespaceAndPath(SkilletManCoreMod.MOD_ID, "player_count_check")).getPath(), () -> PlayerCountCheckCondition::new);

}

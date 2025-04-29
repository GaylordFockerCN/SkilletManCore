package com.p1nero.smc.item;

import com.p1nero.smc.SkilletManCoreMod;
import com.p1nero.smc.entity.SMCEntities;
import com.p1nero.smc.item.custom.LeftSkilletRightSpatula;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SMCItems {
	public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, SkilletManCoreMod.MOD_ID);
	public static final RegistryObject<Item> LEFT_SKILLET_RIGHT_SPATULA = REGISTRY.register("left_skillet_right_spatula", () -> new LeftSkilletRightSpatula(Tiers.NETHERITE, 4, -1.6F, new Item.Properties().fireResistant().rarity(Rarity.EPIC)));

	public static final RegistryObject<Item> GOOD_SUPER_GOLEM_SPAWN_EGG = REGISTRY.register("super_golem_spawn_egg",
			() -> new ForgeSpawnEggItem(SMCEntities.SUPER_GOOD_GOLEM, 14405058, 7643954,
					new Item.Properties()));
	public static final RegistryObject<Item> NO_BRAIN_VILLAGER_SPAWN_EGG = REGISTRY.register("no_brain_villager_spawn_egg",
			() -> new ForgeSpawnEggItem(SMCEntities.VILLAGER_NO_BRAIN, 5651507, 12422002,
					new Item.Properties()));


}

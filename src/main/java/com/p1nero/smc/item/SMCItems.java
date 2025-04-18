package com.p1nero.smc.item;

import com.p1nero.smc.SkilletManCoreMod;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SMCItems {
	public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, SkilletManCoreMod.MOD_ID);

	public static final RegistryObject<Item> SKILL_BOOK_RAFFLE_TICKET = REGISTRY.register("skill_book_raffle_ticket", () ->
			new Item(new Item.Properties().rarity(Rarity.EPIC).fireResistant().food(Foods.APPLE)));
	public static final RegistryObject<Item> WEAPON_RAFFLE_TICKET = REGISTRY.register("weapon_raffle_ticket", () ->
			new Item(new Item.Properties().rarity(Rarity.EPIC).fireResistant().food(Foods.APPLE)));
	public static final RegistryObject<Item> DISK_RAFFLE_TICKET = REGISTRY.register("disk_raffle_ticket", () ->
			new Item(new Item.Properties().rarity(Rarity.EPIC).fireResistant().food(Foods.APPLE)));

}

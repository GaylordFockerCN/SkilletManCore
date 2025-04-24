package com.p1nero.smc.item;

import com.p1nero.smc.SkilletManCoreMod;
import com.p1nero.smc.item.custom.LeftSkilletRightSpatula;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SMCItems {
	public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, SkilletManCoreMod.MOD_ID);
	public static final RegistryObject<Item> LEFT_SKILLET_RIGHT_SPATULA = REGISTRY.register("left_skillet_right_spatula", () -> new LeftSkilletRightSpatula(Tiers.NETHERITE, 4, -1.6F, new Item.Properties().fireResistant().rarity(Rarity.EPIC)));

}

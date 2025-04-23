package com.p1nero.smc.entity;

import com.p1nero.smc.SkilletManCoreMod;
import com.p1nero.smc.entity.custom.CustomColorItemEntity;
import com.p1nero.smc.entity.custom.boss.goldenflame.GoldenFlamePatch;
import com.p1nero.smc.entity.custom.boss.goldenflame.BlackHoleEntity;
import com.p1nero.smc.entity.custom.boss.goldenflame.FlameCircleEntity;
import com.p1nero.smc.entity.custom.boss.goldenflame.GoldenFlame;
import com.p1nero.smc.entity.custom.npc.customer.Customer;
import com.p1nero.smc.entity.custom.npc.start_npc.StartNPC;
import com.p1nero.smc.event.ClientModEvents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import yesman.epicfight.api.client.forgeevent.PatchedRenderersEvent;
import yesman.epicfight.api.forgeevent.EntityPatchRegistryEvent;
import yesman.epicfight.api.forgeevent.ModelBuildEvent;
import yesman.epicfight.gameasset.Armatures;


@Mod.EventBusSubscriber(modid = SkilletManCoreMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SMCEntities {
	public static final DeferredRegister<EntityType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, SkilletManCoreMod.MOD_ID);

	public static final RegistryObject<EntityType<StartNPC>> START_NPC = register("start_npc",
			EntityType.Builder.<StartNPC>of(StartNPC::new, MobCategory.CREATURE).sized(0.6f, 1.9f).fireImmune());

	public static final RegistryObject<EntityType<Customer>> CUSTOMER = register("customer",
			EntityType.Builder.<Customer>of(Customer::new, MobCategory.CREATURE).sized(0.6f, 1.9f).noSave().fireImmune());
	public static final RegistryObject<EntityType<BlackHoleEntity>> BLACK_HOLE = register("black_hole",
			EntityType.Builder.of(BlackHoleEntity::new, MobCategory.MISC).sized(1.0f, 1.0f));
	public static final RegistryObject<EntityType<FlameCircleEntity>> FLAME_CIRCLE = register("flame_circle",
			EntityType.Builder.<FlameCircleEntity>of(FlameCircleEntity::new, MobCategory.AMBIENT).sized(1.0f, 1.0f));
	public static final RegistryObject<EntityType<GoldenFlame>> GOLDEN_FLAME = register("golden_flame",
			EntityType.Builder.of(GoldenFlame::new, MobCategory.MONSTER).sized(0.8f, 2.5f));

	public static final RegistryObject<EntityType<CustomColorItemEntity>> CUSTOM_COLOR_ITEM = register("custom_color_item",
			EntityType.Builder.<CustomColorItemEntity>of(CustomColorItemEntity::new, MobCategory.MISC).sized(0.25F, 0.25F).clientTrackingRange(6).updateInterval(20));

	private static <T extends Entity> RegistryObject<EntityType<T>> register(String name, EntityType.Builder<T> entityTypeBuilder) {
		return REGISTRY.register(name, () -> entityTypeBuilder.build(new ResourceLocation(SkilletManCoreMod.MOD_ID, name).toString()));
	}

	/**
	 * setPatch完还需要去绑定Renderer {@link ClientModEvents#onRenderPatched(PatchedRenderersEvent.Add)}
	 */
	@SubscribeEvent
	public static void setPatch(EntityPatchRegistryEvent event) {
		//BOSS
		event.getTypeEntry().put(GOLDEN_FLAME.get(), (entity) -> GoldenFlamePatch::new);
	}

	/**
	 * setArmature完还需要去绑定Renderer {@link ClientModEvents#onRenderPatched(PatchedRenderersEvent.Add)}
	 */
	@SubscribeEvent
	public static void setArmature(ModelBuildEvent.ArmatureBuild event) {
		//Boss
		Armatures.registerEntityTypeArmature(GOLDEN_FLAME.get(), Armatures.SKELETON);
	}

	@SubscribeEvent
	public static void entityAttributeEvent(EntityAttributeCreationEvent event) {
		//BOSS
		event.put(GOLDEN_FLAME.get(), GoldenFlame.setAttributes());

		//NPC
		event.put(START_NPC.get(), StartNPC.setAttributes());
		event.put(CUSTOMER.get(), StartNPC.setAttributes());
	}

	@SubscribeEvent
	public static void entitySpawnRestriction(SpawnPlacementRegisterEvent event) {
		event.register(START_NPC.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
				StartNPC::checkMobSpawnRules, SpawnPlacementRegisterEvent.Operation.REPLACE);
		event.register(CUSTOMER.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
				StartNPC::checkMobSpawnRules, SpawnPlacementRegisterEvent.Operation.REPLACE);
	}

}

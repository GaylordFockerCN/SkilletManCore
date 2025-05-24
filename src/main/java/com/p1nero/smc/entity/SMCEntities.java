package com.p1nero.smc.entity;

import com.merlin204.supergolem.gameassets.SuperGolemArmature;
import com.p1nero.smc.SkilletManCoreMod;
import com.p1nero.smc.capability.epicfight.NPCPatch;
import com.p1nero.smc.entity.custom.CustomColorItemEntity;
import com.p1nero.smc.entity.custom.npc.me.P1nero;
import com.p1nero.smc.entity.custom.npc.special.HeShen;
import com.p1nero.smc.entity.custom.npc.VillagerWithoutBrain;
import com.p1nero.smc.entity.custom.npc.customer.FakeCustomer;
import com.p1nero.smc.entity.custom.npc.special.Thief1;
import com.p1nero.smc.entity.custom.npc.special.Thief2;
import com.p1nero.smc.entity.custom.npc.special.TwoKid;
import com.p1nero.smc.entity.custom.npc.special.virgil.VirgilVillager;
import com.p1nero.smc.entity.custom.npc.special.virgil.VirgilVillagerPatch;
import com.p1nero.smc.entity.custom.npc.special.zombie_man.ZombieMan;
import com.p1nero.smc.entity.custom.npc.start_npc.StartNPCBBQ;
import com.p1nero.smc.entity.custom.npc.start_npc.StartNPCPlus;
import com.p1nero.smc.entity.custom.super_golem.SuperBadIronGolem;
import com.p1nero.smc.entity.custom.boss.goldenflame.GoldenFlamePatch;
import com.p1nero.smc.entity.custom.boss.goldenflame.BlackHoleEntity;
import com.p1nero.smc.entity.custom.boss.goldenflame.FlameCircleEntity;
import com.p1nero.smc.entity.custom.boss.goldenflame.GoldenFlame;
import com.p1nero.smc.entity.custom.npc.customer.Customer;
import com.p1nero.smc.entity.custom.npc.start_npc.StartNPC;
import com.p1nero.smc.entity.custom.super_golem.SuperBadIronGolemPatch;
import com.p1nero.smc.entity.custom.super_golem.SuperGoodIronGolem;
import com.p1nero.smc.event.ClientModEvents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
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
import yesman.epicfight.world.capabilities.entitypatch.HumanoidMobPatch;


@Mod.EventBusSubscriber(modid = SkilletManCoreMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SMCEntities {
    public static final DeferredRegister<EntityType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, SkilletManCoreMod.MOD_ID);

    public static final RegistryObject<EntityType<VillagerWithoutBrain>> VILLAGER_NO_BRAIN = register("villager_no_brain",
            EntityType.Builder.of(VillagerWithoutBrain::new, MobCategory.CREATURE).sized(0.6f, 1.9f).fireImmune());
    public static final RegistryObject<EntityType<StartNPC>> START_NPC = register("start_npc",
            EntityType.Builder.<StartNPC>of(StartNPC::new, MobCategory.CREATURE).sized(0.6f, 1.9f).fireImmune());
    public static final RegistryObject<EntityType<P1nero>> P1NERO = register("p1nero",
            EntityType.Builder.<P1nero>of(P1nero::new, MobCategory.CREATURE).sized(0.6f, 1.9f).fireImmune());
    public static final RegistryObject<EntityType<StartNPCPlus>> START_NPC_PLUS = register("start_npc_plus",
            EntityType.Builder.<StartNPCPlus>of(StartNPCPlus::new, MobCategory.CREATURE).sized(0.6f, 1.9f).fireImmune());
    public static final RegistryObject<EntityType<StartNPCBBQ>> START_NPC_BBQ = register("start_npc_bbq",
            EntityType.Builder.<StartNPCBBQ>of(StartNPCBBQ::new, MobCategory.CREATURE).sized(0.6f, 1.9f).fireImmune());
    public static final RegistryObject<EntityType<ZombieMan>> ZOMBIE_MAN = register("zombie_man",
            EntityType.Builder.<ZombieMan>of(ZombieMan::new, MobCategory.CREATURE).sized(0.6f, 1.9f).fireImmune());

    public static final RegistryObject<EntityType<Customer>> CUSTOMER = register("customer",
            EntityType.Builder.<Customer>of(Customer::new, MobCategory.CREATURE).sized(0.6f, 1.9f).noSave().fireImmune());
    public static final RegistryObject<EntityType<FakeCustomer>> FAKE_CUSTOMER = register("fake_customer",
            EntityType.Builder.<FakeCustomer>of(FakeCustomer::new, MobCategory.CREATURE).sized(0.6f, 1.9f).noSave().fireImmune());
    public static final RegistryObject<EntityType<HeShen>> HE_SHEN = register("he_shen",
            EntityType.Builder.<HeShen>of(HeShen::new, MobCategory.CREATURE).sized(0.6f, 1.9f).fireImmune());
    public static final RegistryObject<EntityType<TwoKid>> TWO_KID = register("two_kid",
            EntityType.Builder.<TwoKid>of(TwoKid::new, MobCategory.CREATURE).sized(0.6f, 1.9f).fireImmune());
    public static final RegistryObject<EntityType<Thief1>> THIEF1 = register("thief1",
            EntityType.Builder.<Thief1>of(Thief1::new, MobCategory.CREATURE).sized(0.6f, 1.9f).fireImmune());
    public static final RegistryObject<EntityType<Thief2>> THIEF2 = register("thief2",
            EntityType.Builder.<Thief2>of(Thief2::new, MobCategory.CREATURE).sized(0.6f, 1.9f).fireImmune());
    public static final RegistryObject<EntityType<VirgilVillager>> VIRGIL_VILLAGER = register("virgil_villager",
            EntityType.Builder.<VirgilVillager>of(VirgilVillager::new, MobCategory.CREATURE).sized(0.6f, 1.9f).fireImmune());
    public static final RegistryObject<EntityType<BlackHoleEntity>> BLACK_HOLE = register("black_hole",
            EntityType.Builder.of(BlackHoleEntity::new, MobCategory.MISC).sized(1.0f, 1.0f));
    public static final RegistryObject<EntityType<FlameCircleEntity>> FLAME_CIRCLE = register("flame_circle",
            EntityType.Builder.<FlameCircleEntity>of(FlameCircleEntity::new, MobCategory.AMBIENT).sized(1.0f, 1.0f));
    public static final RegistryObject<EntityType<GoldenFlame>> GOLDEN_FLAME = register("golden_flame",
            EntityType.Builder.of(GoldenFlame::new, MobCategory.MONSTER).sized(0.8f, 2.5f));
    public static final RegistryObject<EntityType<SuperBadIronGolem>> SUPER_GOLEM = register("super_golem",
            EntityType.Builder.of(SuperBadIronGolem::new, MobCategory.MISC).sized(1.4F, 2.7f));
    public static final RegistryObject<EntityType<SuperGoodIronGolem>> SUPER_GOOD_GOLEM = register("super_good_golem",
            EntityType.Builder.of(SuperGoodIronGolem::new, MobCategory.MISC).sized(1.4F, 2.7f));

    public static final RegistryObject<EntityType<CustomColorItemEntity>> CUSTOM_COLOR_ITEM = register("custom_color_item",
            EntityType.Builder.<CustomColorItemEntity>of(CustomColorItemEntity::new, MobCategory.MISC).sized(0.25F, 0.25F).clientTrackingRange(6).updateInterval(20));

    private static <T extends Entity> RegistryObject<EntityType<T>> register(String name, EntityType.Builder<T> entityTypeBuilder) {
        return REGISTRY.register(name, () -> entityTypeBuilder.build(ResourceLocation.fromNamespaceAndPath(SkilletManCoreMod.MOD_ID, name).toString()));
    }

    /**
     * setPatch完还需要去绑定Renderer {@link ClientModEvents#onRenderPatched(PatchedRenderersEvent.Add)}
     */
    @SubscribeEvent
    public static void setPatch(EntityPatchRegistryEvent event) {
        //BOSS
        event.getTypeEntry().put(GOLDEN_FLAME.get(), (entity) -> GoldenFlamePatch::new);
        event.getTypeEntry().put(SUPER_GOLEM.get(), (entity) -> SuperBadIronGolemPatch::new);
        event.getTypeEntry().put(SUPER_GOOD_GOLEM.get(), (entity) -> SuperBadIronGolemPatch::new);
        event.getTypeEntry().put(VIRGIL_VILLAGER.get(), (entity) -> VirgilVillagerPatch::new);
        event.getTypeEntry().put(P1NERO.get(), (entity) -> NPCPatch::new);
        event.getTypeEntry().put(ZOMBIE_MAN.get(), (entity) -> NPCPatch::new);
    }

    /**
     * setArmature完还需要去绑定Renderer {@link ClientModEvents#onRenderPatched(PatchedRenderersEvent.Add)}
     */
    @SubscribeEvent
    public static void setArmature(ModelBuildEvent.ArmatureBuild event) {
        //Boss
        Armatures.registerEntityTypeArmature(GOLDEN_FLAME.get(), Armatures.SKELETON);
        SuperGolemArmature superGolemArmature = event.get(SkilletManCoreMod.MOD_ID, "entity/super_golem", SuperGolemArmature::new);
        Armatures.registerEntityTypeArmature(SUPER_GOLEM.get(), superGolemArmature);
        Armatures.registerEntityTypeArmature(SUPER_GOOD_GOLEM.get(), superGolemArmature);
        Armatures.registerEntityTypeArmature(VIRGIL_VILLAGER.get(), Armatures.BIPED);
        Armatures.registerEntityTypeArmature(P1NERO.get(), Armatures.BIPED);
        Armatures.registerEntityTypeArmature(ZOMBIE_MAN.get(), Armatures.BIPED);
    }

    @SubscribeEvent
    public static void entityAttributeEvent(EntityAttributeCreationEvent event) {
        //BOSS
        event.put(GOLDEN_FLAME.get(), GoldenFlame.setAttributes());
        event.put(SUPER_GOLEM.get(), SuperBadIronGolem.setAttributes());
        event.put(SUPER_GOOD_GOLEM.get(), SuperGoodIronGolem.setAttributes());

        //NPC
        event.put(VILLAGER_NO_BRAIN.get(), StartNPC.setAttributes());
        event.put(START_NPC.get(), StartNPC.setAttributes());
        event.put(START_NPC_PLUS.get(), StartNPC.setAttributes());
        event.put(START_NPC_BBQ.get(), StartNPC.setAttributes());
        event.put(ZOMBIE_MAN.get(), StartNPC.setAttributes());
        event.put(CUSTOMER.get(), StartNPC.setAttributes());
        event.put(FAKE_CUSTOMER.get(), StartNPC.setAttributes());
        event.put(HE_SHEN.get(), StartNPC.setAttributes());
        event.put(TWO_KID.get(), StartNPC.setAttributes());
        event.put(THIEF1.get(), Thief1.setAttributes());
        event.put(THIEF2.get(), Thief1.setAttributes());
        event.put(VIRGIL_VILLAGER.get(), VirgilVillager.createAttributes().build());
        event.put(P1NERO.get(), StartNPC.setAttributes());
    }

    @SubscribeEvent
    public static void entitySpawnRestriction(SpawnPlacementRegisterEvent event) {
        event.register(VILLAGER_NO_BRAIN.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                StartNPC::checkMobSpawnRules, SpawnPlacementRegisterEvent.Operation.REPLACE);
        event.register(START_NPC.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                StartNPC::checkMobSpawnRules, SpawnPlacementRegisterEvent.Operation.REPLACE);
        event.register(START_NPC_PLUS.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                StartNPC::checkMobSpawnRules, SpawnPlacementRegisterEvent.Operation.REPLACE);
        event.register(START_NPC_BBQ.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                StartNPC::checkMobSpawnRules, SpawnPlacementRegisterEvent.Operation.REPLACE);
        event.register(ZOMBIE_MAN.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                StartNPC::checkMobSpawnRules, SpawnPlacementRegisterEvent.Operation.REPLACE);
        event.register(CUSTOMER.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                StartNPC::checkMobSpawnRules, SpawnPlacementRegisterEvent.Operation.REPLACE);
        event.register(FAKE_CUSTOMER.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                StartNPC::checkMobSpawnRules, SpawnPlacementRegisterEvent.Operation.REPLACE);
        event.register(HE_SHEN.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                StartNPC::checkMobSpawnRules, SpawnPlacementRegisterEvent.Operation.REPLACE);
        event.register(TWO_KID.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                StartNPC::checkMobSpawnRules, SpawnPlacementRegisterEvent.Operation.REPLACE);
        event.register(THIEF1.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                StartNPC::checkMobSpawnRules, SpawnPlacementRegisterEvent.Operation.REPLACE);
        event.register(THIEF2.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                StartNPC::checkMobSpawnRules, SpawnPlacementRegisterEvent.Operation.REPLACE);
        event.register(VIRGIL_VILLAGER.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                StartNPC::checkMobSpawnRules, SpawnPlacementRegisterEvent.Operation.REPLACE);
    }

}

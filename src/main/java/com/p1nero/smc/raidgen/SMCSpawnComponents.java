//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.p1nero.smc.raidgen;

import com.mojang.datafixers.util.Pair;
import com.p1nero.smc.SkilletManCoreMod;
import hungteen.htlib.api.interfaces.raid.IPositionComponent;
import hungteen.htlib.api.interfaces.raid.ISpawnComponent;
import hungteen.htlib.common.impl.position.HTPositionComponents;
import hungteen.htlib.common.impl.spawn.DurationSpawn;
import hungteen.htlib.common.impl.spawn.HTSpawnComponents;
import hungteen.htlib.common.impl.spawn.OnceSpawn;
import hungteen.htlib.util.helper.NBTHelper;

import java.util.ArrayList;
import java.util.List;

import net.kenddie.fantasyarmor.item.FAItems;
import net.minecraft.core.HolderGetter;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public interface SMCSpawnComponents {
    List<ResourceKey<ISpawnComponent>> ZOMBIES = new ArrayList<>();
    List<ResourceKey<ISpawnComponent>> SKELETONS = new ArrayList<>();
    List<ResourceKey<ISpawnComponent>> SPIDERS = new ArrayList<>();
    List<ResourceKey<ISpawnComponent>> CREEPERS = new ArrayList<>();
    List<ResourceKey<ISpawnComponent>> WITCHES = new ArrayList<>();
    List<ResourceKey<ISpawnComponent>> PILLAGERS = new ArrayList<>();
    List<ResourceKey<ISpawnComponent>> VINDICATORS = new ArrayList<>();
    List<ResourceKey<ISpawnComponent>> EVOKERS = new ArrayList<>();
    List<ResourceKey<ISpawnComponent>> WITHER_SKELETONS = new ArrayList<>();
    List<ResourceKey<ISpawnComponent>> BLAZES = new ArrayList<>();
    List<ResourceKey<ISpawnComponent>> ENDER_MANS = new ArrayList<>();
    ResourceKey<ISpawnComponent> WITHER = create("wither");//突破试炼和袭击都可以用
    ResourceKey<ISpawnComponent> SUPER_GOLEM = create("super_golem");//突破试炼用

    static void register(BootstapContext<ISpawnComponent> context) {

        HolderGetter<IPositionComponent> positions = HTPositionComponents.registry().helper().lookup(context);
        CompoundTag glowing = new CompoundTag();
        glowing.putBoolean("glowing", true);//TODO 给实体都添加发光
        //预计30day
        for (int i = 0; i <= 30; i++) {
            ZOMBIES.add(create("zombie" + i));
            SKELETONS.add(create("skeleton" + i));
            SPIDERS.add(create("spider" + i));
            CREEPERS.add(create("creeper" + i));
            WITCHES.add(create("witch" + i));
            PILLAGERS.add(create("pillager" + i));
            VINDICATORS.add(create("vindicator" + i));
            EVOKERS.add(create("evoker" + i));
            WITHER_SKELETONS.add(create("wither_skeleton" + i));
            BLAZES.add(create("blaze" + i));
            ENDER_MANS.add(create("ender_man" + i));

            context.register(ZOMBIES.get(i), new DurationSpawn(HTSpawnComponents.builder()
                    .entityType(EntityType.ZOMBIE)
                    .placement(positions.getOrThrow(SMCPositionComponents.RAID))
                    .nbt(NBTHelper
                            .merge(
                                    NBTHelper.armorTag(List.of(ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, new ItemStack(FAItems.TWINNED_HELMET.get()))),
                                    NBTHelper.attributeTags(List.of(Pair.of(Attributes.MAX_HEALTH, 10.0 + i * 2))),
                                    NBTHelper.healthTag(10.0F + i * 2))).build()
                    , 40 * (2 + i / 3), 40, 1, 0));

            context.register(SKELETONS.get(i), new DurationSpawn(HTSpawnComponents.builder()
                    .entityType(EntityType.SKELETON)
                    .placement(positions.getOrThrow(SMCPositionComponents.RAID))
                    .nbt(NBTHelper
                            .merge(
                                    NBTHelper.armorTag(List.of(ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, new ItemStack(FAItems.CRUCIBLE_KNIGHT_HELMET.get()))),
                                    NBTHelper.attributeTags(List.of(Pair.of(Attributes.MAX_HEALTH, 10.0 + i * 2))),
                                    NBTHelper.healthTag(10.0F + i * 2))).build()
                    , 40 * (2 + i / 3), 40, 1, 0));

            //1天后开始出现蜘蛛苦力怕
            if(i > 1) {
                context.register(SPIDERS.get(i), new DurationSpawn(HTSpawnComponents.builder()
                        .entityType(EntityType.SPIDER)
                        .placement(positions.getOrThrow(SMCPositionComponents.RAID))
                        .nbt(NBTHelper
                                .merge(NBTHelper.attributeTags(List.of(Pair.of(Attributes.MAX_HEALTH, 18.0 + i * 2))),
                                        NBTHelper.healthTag(18.0F + i * 2))).build()
                        , 40 * (1 + i / 3), 40, 1, 0));
                context.register(CREEPERS.get(i), new DurationSpawn(HTSpawnComponents.builder()
                        .entityType(EntityType.CREEPER)
                        .placement(positions.getOrThrow(SMCPositionComponents.RAID))
                        .nbt(NBTHelper
                                .merge(NBTHelper.attributeTags(List.of(Pair.of(Attributes.MAX_HEALTH, 18.0 + i * 2))),
                                        NBTHelper.healthTag(18.0F + i * 2))).build()
                        , 40 * (1 + i / 3), 40, 1, 0));
            }
            //3天后开始出现女巫，灾厄村民
            if(i > 3) {
                context.register(WITCHES.get(i), new DurationSpawn(HTSpawnComponents.builder()
                        .entityType(EntityType.WITCH)
                        .placement(positions.getOrThrow(SMCPositionComponents.RAID))
                        .nbt(NBTHelper
                                .merge(NBTHelper.attributeTags(List.of(Pair.of(Attributes.MAX_HEALTH, 18.0 + i * 2))),
                                        NBTHelper.healthTag(18.0F + i * 2))).build()
                        , 40 * (1 + i / 5), 40, 1, 0));
                context.register(PILLAGERS.get(i), new DurationSpawn(HTSpawnComponents.builder()
                        .entityType(EntityType.PILLAGER)
                        .placement(positions.getOrThrow(SMCPositionComponents.RAID))
                        .nbt(NBTHelper
                                .merge(NBTHelper.attributeTags(List.of(Pair.of(Attributes.MAX_HEALTH, 18.0 + i * 2))),
                                        NBTHelper.healthTag(18.0F + i * 2))).build()
                        , 40 * (1 + i / 5), 40, 1, 0));
            }
            //5天后开始出现卫道士唤魔者
            if(i > 5) {
                context.register(VINDICATORS.get(i), new DurationSpawn(HTSpawnComponents.builder()
                        .entityType(EntityType.VINDICATOR)
                        .placement(positions.getOrThrow(SMCPositionComponents.RAID))
                        .nbt(NBTHelper
                                .merge(NBTHelper.attributeTags(List.of(Pair.of(Attributes.MAX_HEALTH, 18.0 + i * 2))),
                                        NBTHelper.healthTag(18.0F + i * 2))).build()
                        , 40 * (1 + i / 10), 40, 1, 0));
                context.register(EVOKERS.get(i), new DurationSpawn(HTSpawnComponents.builder()
                        .entityType(EntityType.EVOKER)
                        .placement(positions.getOrThrow(SMCPositionComponents.RAID))
                        .nbt(NBTHelper
                                .merge(NBTHelper.attributeTags(List.of(Pair.of(Attributes.MAX_HEALTH, 18.0 + i * 2))),
                                        NBTHelper.healthTag(18.0F + i * 2))).build()
                        , 40 * (1 + i / 10), 40, 1, 0));
            }
            //7天后开始凋零骷髅，烈焰人
            if(i >= 7) {
                context.register(WITHER_SKELETONS.get(i), new DurationSpawn(HTSpawnComponents.builder()
                        .entityType(EntityType.WITHER_SKELETON)
                        .placement(positions.getOrThrow(SMCPositionComponents.RAID))
                        .nbt(NBTHelper
                                .merge(NBTHelper.attributeTags(List.of(Pair.of(Attributes.MAX_HEALTH, 18.0 + i * 2))),
                                        NBTHelper.healthTag(18.0F + i * 2))).build()
                        , 40 * (1 + i / 10), 40, 1, 0));
                context.register(BLAZES.get(i), new DurationSpawn(HTSpawnComponents.builder()
                        .entityType(EntityType.BLAZE)
                        .placement(positions.getOrThrow(SMCPositionComponents.RAID))
                        .nbt(NBTHelper
                                .merge(NBTHelper.attributeTags(List.of(Pair.of(Attributes.MAX_HEALTH, 18.0 + i * 2))),
                                        NBTHelper.healthTag(18.0F + i * 2))).build()
                        , 40 * (1 + i / 10), 40, 1, 0));
            }
            //9天后开始生成末影人
            if(i > 9) {
                context.register(ENDER_MANS.get(i), new DurationSpawn(HTSpawnComponents.builder()
                        .entityType(EntityType.ENDERMAN)
                        .placement(positions.getOrThrow(SMCPositionComponents.RAID))
                        .nbt(NBTHelper
                                .merge(NBTHelper.attributeTags(List.of(Pair.of(Attributes.MAX_HEALTH, 18.0 + i * 2))),
                                        NBTHelper.healthTag(18.0F + i * 2))).build()
                        , 40 * (1 + i / 10), 40, 1, 0));
            }
        }

        context.register(WITHER, new OnceSpawn(HTSpawnComponents.builder()
                .entityType(EntityType.WITHER)
                .placement(positions.getOrThrow(SMCPositionComponents.RAID))
                .nbt(NBTHelper
                        .merge(NBTHelper.attributeTags(List.of(Pair.of(Attributes.MAX_HEALTH, 400.0))),
                                NBTHelper.healthTag(400))).build()
                , ConstantInt.of(1)));
    }

    static ResourceKey<ISpawnComponent> create(String name) {
        return HTSpawnComponents.registry().createKey(SkilletManCoreMod.prefix(name));
    }

}

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
import hungteen.htlib.common.impl.spawn.HTSpawnComponents;
import hungteen.htlib.common.impl.spawn.OnceSpawn;
import hungteen.htlib.util.helper.NBTHelper;
import java.util.List;

import net.minecraft.core.HolderGetter;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attributes;

public interface SMCSpawnComponents {
    ResourceKey<ISpawnComponent> WITHER_SKELETON = create("wither_skeleton");

    static void register(BootstapContext<ISpawnComponent> context) {
        HolderGetter<IPositionComponent> positions = HTPositionComponents.registry().helper().lookup(context);
        context.register(WITHER_SKELETON, new OnceSpawn(HTSpawnComponents.builder().entityType(EntityType.WITHER_SKELETON).placement(positions.getOrThrow(SMCPositionComponents.TRIAL)).nbt(NBTHelper.merge(NBTHelper.attributeTags(List.of(Pair.of(Attributes.MAX_HEALTH, 50.0))), NBTHelper.healthTag(50.0F))).build(), ConstantInt.of(1)));
    }
    static ResourceKey<ISpawnComponent> create(String name) {
        return HTSpawnComponents.registry().createKey(SkilletManCoreMod.prefix(name));
    }

}

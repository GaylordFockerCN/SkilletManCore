//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.p1nero.smc.raidgen;

import com.p1nero.smc.SkilletManCoreMod;
import hungteen.htlib.api.interfaces.raid.IResultComponent;
import hungteen.htlib.common.impl.result.*;
import java.util.Optional;

import hungteen.htlib.util.helper.HTLibHelper;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;

public interface SMCResultComponents {
    ResourceKey<IResultComponent> STAGE_UP = create("stage_up");
    ResourceKey<IResultComponent> DEFEND_SUCCESS = create("defend_success");
    ResourceKey<IResultComponent> DEFEND_FAILED = create("defend_failed");

    static void register(BootstapContext<IResultComponent> context) {
        context.register(STAGE_UP, new CommandResult(Optional.empty(), Optional.of("smc stageUp @s"), Optional.empty()));
        context.register(DEFEND_SUCCESS, new CommandResult(Optional.empty(), Optional.of("smc defendSuccess @s"), Optional.empty()));
        context.register(DEFEND_FAILED, new CommandResult(Optional.empty(), Optional.of("smc defendFailed @s"), Optional.empty()));
    }
    static ResourceKey<IResultComponent> create(String name) {
        return HTResultComponents.registry().createKey(SkilletManCoreMod.prefix(name));
    }

}

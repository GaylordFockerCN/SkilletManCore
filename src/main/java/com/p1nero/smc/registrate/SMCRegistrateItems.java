package com.p1nero.smc.registrate;

import com.p1nero.smc.SkilletManCoreMod;
import com.p1nero.smc.item.custom.skillets.SkilletV2;
import com.p1nero.smc.item.custom.skillets.SkilletV3;
import com.p1nero.smc.item.custom.skillets.SkilletV4;
import com.p1nero.smc.item.custom.skillets.SkilletV5;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import dev.xkmc.cuisinedelight.init.registrate.CDBlocks;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraftforge.client.model.generators.ModelFile;

@MethodsReturnNonnullByDefault
public class SMCRegistrateItems {

    public static final ItemEntry<SkilletV2> IRON_SKILLET_LEVEL2 = SkilletManCoreMod.REGISTRATE.item("cuisine_skillet_v2", p -> new SkilletV2(CDBlocks.SKILLET.get(), p.stacksTo(1)))
            .model((ctx, pvd) -> pvd.getBuilder(ctx.getName()).parent(new ModelFile.UncheckedModelFile("cuisinedelight:item/cuisine_skillet_base")))
            .setData(ProviderType.LANG, NonNullBiConsumer.noop()).register();
    public static final ItemEntry<SkilletV3> IRON_SKILLET_LEVEL3 = SkilletManCoreMod.REGISTRATE.item("cuisine_skillet_v3", p -> new SkilletV3(CDBlocks.SKILLET.get(), p.stacksTo(1)))
            .model((ctx, pvd) -> pvd.getBuilder(ctx.getName()).parent(new ModelFile.UncheckedModelFile("cuisinedelight:item/cuisine_skillet_base")))
            .setData(ProviderType.LANG, NonNullBiConsumer.noop()).register();
    public static final ItemEntry<SkilletV4> IRON_SKILLET_LEVEL4 = SkilletManCoreMod.REGISTRATE.item("cuisine_skillet_v4", p -> new SkilletV4(CDBlocks.SKILLET.get(), p.stacksTo(1)))
            .model((ctx, pvd) -> pvd.getBuilder(ctx.getName()).parent(new ModelFile.UncheckedModelFile("cuisinedelight:item/cuisine_skillet_base")))
            .setData(ProviderType.LANG, NonNullBiConsumer.noop()).register();
    public static final ItemEntry<SkilletV5> IRON_SKILLET_LEVEL5 = SkilletManCoreMod.REGISTRATE.item("cuisine_skillet_v5", p -> new SkilletV5(CDBlocks.SKILLET.get(), p.stacksTo(1)))
            .model((ctx, pvd) -> pvd.getBuilder(ctx.getName()).parent(new ModelFile.UncheckedModelFile("cuisinedelight:item/cuisine_skillet_base")))
            .setData(ProviderType.LANG, NonNullBiConsumer.noop()).register();

    public static void register() {
    }

}

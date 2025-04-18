package com.p1nero.smc.registrate;

import com.p1nero.smc.SkilletManCoreMod;
import com.p1nero.smc.item.custom.skillets.SkilletV2;
import com.p1nero.smc.item.custom.skillets.SkilletV3;
import com.p1nero.smc.item.custom.skillets.SkilletV4;
import com.p1nero.smc.item.custom.skillets.SkilletV5;
import com.p1nero.smc.item.custom.spatulas.SpatulaV2;
import com.p1nero.smc.item.custom.spatulas.SpatulaV3;
import com.p1nero.smc.item.custom.spatulas.SpatulaV4;
import com.p1nero.smc.item.custom.spatulas.SpatulaV5;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import dev.xkmc.cuisinedelight.content.item.PlateItem;
import dev.xkmc.cuisinedelight.content.item.SpatulaItem;
import dev.xkmc.cuisinedelight.init.CuisineDelight;
import dev.xkmc.cuisinedelight.init.data.TagGen;
import dev.xkmc.cuisinedelight.init.registrate.CDBlocks;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.tags.TagKey;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.Tags;

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

    public static final ItemEntry<SpatulaV2> SPATULA_V2 = SkilletManCoreMod.REGISTRATE.item("spatula_v2", p -> new SpatulaV2(p.stacksTo(1)))
            .tag(TagGen.UTENSILS)
            .model((ctx, pvd) -> pvd.getBuilder(ctx.getName()).parent(new ModelFile.UncheckedModelFile("cuisinedelight:item/spatula")))
            .defaultLang().register();
    public static final ItemEntry<SpatulaV3> SPATULA_V3 = SkilletManCoreMod.REGISTRATE.item("spatula_v3", p -> new SpatulaV3(p.stacksTo(1)))
            .tag(TagGen.UTENSILS)
            .model((ctx, pvd) -> pvd.getBuilder(ctx.getName()).parent(new ModelFile.UncheckedModelFile("cuisinedelight:item/spatula")))
            .defaultLang().register();
    public static final ItemEntry<SpatulaV4> SPATULA_V4 = SkilletManCoreMod.REGISTRATE.item("spatula_v4", p -> new SpatulaV4(p.stacksTo(1)))
            .tag(TagGen.UTENSILS)
            .model((ctx, pvd) -> pvd.getBuilder(ctx.getName()).parent(new ModelFile.UncheckedModelFile("cuisinedelight:item/spatula")))
            .defaultLang().register();
    public static final ItemEntry<SpatulaV5> SPATULA_V5 = SkilletManCoreMod.REGISTRATE.item("spatula_v5", p -> new SpatulaV5(p.stacksTo(1)))
            .tag(TagGen.UTENSILS)
            .model((ctx, pvd) -> pvd.getBuilder(ctx.getName()).parent(new ModelFile.UncheckedModelFile("cuisinedelight:item/spatula")))
            .defaultLang().register();
    //TODO 脏盘子，可重复利用，遇水变干净，弹成就
    public static final ItemEntry<PlateItem> DIRT_PLATE = SkilletManCoreMod.REGISTRATE.item("spatula_v5", p -> new PlateItem(p.stacksTo(1)))
            .tag(TagGen.UTENSILS)
            .model((ctx, pvd) -> pvd.getBuilder(ctx.getName()).parent(new ModelFile.UncheckedModelFile("cuisinedelight:item/spatula")))
            .defaultLang().register();

    public static void register() {
    }

}

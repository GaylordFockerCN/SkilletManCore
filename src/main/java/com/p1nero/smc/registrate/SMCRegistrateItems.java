package com.p1nero.smc.registrate;

import com.p1nero.smc.SkilletManCoreMod;
import com.p1nero.smc.item.custom.DirtPlateItem;
import com.p1nero.smc.item.custom.SimpleDescriptionFoilItem;
import com.p1nero.smc.item.custom.skillets.*;
import com.p1nero.smc.item.custom.spatulas.*;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import dev.xkmc.cuisinedelight.init.data.TagGen;
import dev.xkmc.cuisinedelight.init.registrate.CDBlocks;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
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

    public static final ItemEntry<GoldenSkilletItem> GOLDEN_SKILLET = SkilletManCoreMod.REGISTRATE.item("golden_cuisine_skillet", p -> new GoldenSkilletItem(SMCRegistrateBlocks.GOLDEN_SKILLET.get(), p.stacksTo(1)))
            .model((ctx, pvd) -> pvd.getBuilder(ctx.getName()).parent(new ModelFile.UncheckedModelFile("builtin/entity")))
            .setData(ProviderType.LANG, NonNullBiConsumer.noop()).register();
    public static final ItemEntry<GoldenSkilletItem> GOLDEN_SKILLET_V2 = SkilletManCoreMod.REGISTRATE.item("golden_cuisine_skillet_v2", p -> new GoldenSkilletItem(SMCRegistrateBlocks.GOLDEN_SKILLET.get(), p.stacksTo(1), 2))
            .model((ctx, pvd) -> pvd.getBuilder(ctx.getName()).parent(new ModelFile.UncheckedModelFile("builtin/entity")))
            .setData(ProviderType.LANG, NonNullBiConsumer.noop()).register();
    public static final ItemEntry<GoldenSkilletItem> GOLDEN_SKILLET_V3 = SkilletManCoreMod.REGISTRATE.item("golden_cuisine_skillet_v3", p -> new GoldenSkilletItem(SMCRegistrateBlocks.GOLDEN_SKILLET.get(), p.stacksTo(1), 3))
            .model((ctx, pvd) -> pvd.getBuilder(ctx.getName()).parent(new ModelFile.UncheckedModelFile("builtin/entity")))
            .setData(ProviderType.LANG, NonNullBiConsumer.noop()).register();
    public static final ItemEntry<GoldenSkilletItem> GOLDEN_SKILLET_V4 = SkilletManCoreMod.REGISTRATE.item("golden_cuisine_skillet_v4", p -> new GoldenSkilletItem(SMCRegistrateBlocks.GOLDEN_SKILLET.get(), p.stacksTo(1), 4))
            .model((ctx, pvd) -> pvd.getBuilder(ctx.getName()).parent(new ModelFile.UncheckedModelFile("builtin/entity")))
            .setData(ProviderType.LANG, NonNullBiConsumer.noop()).register();
    public static final ItemEntry<GoldenSkilletItem> GOLDEN_SKILLET_V5 = SkilletManCoreMod.REGISTRATE.item("golden_cuisine_skillet_v5", p -> new GoldenSkilletItem(SMCRegistrateBlocks.GOLDEN_SKILLET.get(), p.stacksTo(1), 5))
            .model((ctx, pvd) -> pvd.getBuilder(ctx.getName()).parent(new ModelFile.UncheckedModelFile("builtin/entity")))
            .setData(ProviderType.LANG, NonNullBiConsumer.noop()).register();
    public static final ItemEntry<DiamondSkilletItem> DIAMOND_SKILLET = SkilletManCoreMod.REGISTRATE.item("diamond_cuisine_skillet", p -> new DiamondSkilletItem(SMCRegistrateBlocks.DIAMOND_SKILLET.get(), p.stacksTo(1)))
            .model((ctx, pvd) -> pvd.getBuilder(ctx.getName()).parent(new ModelFile.UncheckedModelFile("builtin/entity")))
            .setData(ProviderType.LANG, NonNullBiConsumer.noop()).register();
    public static final ItemEntry<DiamondSkilletItem> DIAMOND_SKILLET_V2 = SkilletManCoreMod.REGISTRATE.item("diamond_cuisine_skillet_v2", p -> new DiamondSkilletItem(SMCRegistrateBlocks.DIAMOND_SKILLET.get(), p.stacksTo(1), 2))
            .model((ctx, pvd) -> pvd.getBuilder(ctx.getName()).parent(new ModelFile.UncheckedModelFile("builtin/entity")))
            .setData(ProviderType.LANG, NonNullBiConsumer.noop()).register();
    public static final ItemEntry<DiamondSkilletItem> DIAMOND_SKILLET_V3 = SkilletManCoreMod.REGISTRATE.item("diamond_cuisine_skillet_v3", p -> new DiamondSkilletItem(SMCRegistrateBlocks.DIAMOND_SKILLET.get(), p.stacksTo(1), 3))
            .model((ctx, pvd) -> pvd.getBuilder(ctx.getName()).parent(new ModelFile.UncheckedModelFile("builtin/entity")))
            .setData(ProviderType.LANG, NonNullBiConsumer.noop()).register();

    public static final ItemEntry<DiamondSkilletItem> DIAMOND_SKILLET_V4 = SkilletManCoreMod.REGISTRATE.item("diamond_cuisine_skillet_v4", p -> new DiamondSkilletItem(SMCRegistrateBlocks.DIAMOND_SKILLET.get(), p.stacksTo(1), 4))
            .model((ctx, pvd) -> pvd.getBuilder(ctx.getName()).parent(new ModelFile.UncheckedModelFile("builtin/entity")))
            .setData(ProviderType.LANG, NonNullBiConsumer.noop()).register();

    public static final ItemEntry<DiamondSkilletItem> DIAMOND_SKILLET_V5 = SkilletManCoreMod.REGISTRATE.item("diamond_cuisine_skillet_v5", p -> new DiamondSkilletItem(SMCRegistrateBlocks.DIAMOND_SKILLET.get(), p.stacksTo(1), 5))
            .model((ctx, pvd) -> pvd.getBuilder(ctx.getName()).parent(new ModelFile.UncheckedModelFile("builtin/entity")))
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
    public static final ItemEntry<GoldenSpatulaBase> GOLDEN_SPATULA = SkilletManCoreMod.REGISTRATE.item("golden_spatula", p -> new GoldenSpatulaBase(p.stacksTo(1)))
            .tag(TagGen.UTENSILS)
            .model((ctx, pvd) -> pvd.handheld(ctx))
            .defaultLang().register();
    public static final ItemEntry<GoldenSpatulaV2> GOLDEN_SPATULA_V2 = SkilletManCoreMod.REGISTRATE.item("golden_spatula_v2", p -> new GoldenSpatulaV2(p.stacksTo(1)))
            .tag(TagGen.UTENSILS)
            .model((ctx, pvd) -> pvd.getBuilder(ctx.getName()).parent(new ModelFile.UncheckedModelFile(SkilletManCoreMod.MOD_ID + ":item/golden_spatula")))
            .defaultLang().register();
    public static final ItemEntry<GoldenSpatulaV3> GOLDEN_SPATULA_V3 = SkilletManCoreMod.REGISTRATE.item("golden_spatula_v3", p -> new GoldenSpatulaV3(p.stacksTo(1)))
            .tag(TagGen.UTENSILS)
            .model((ctx, pvd) -> pvd.getBuilder(ctx.getName()).parent(new ModelFile.UncheckedModelFile(SkilletManCoreMod.MOD_ID + ":item/golden_spatula")))
            .defaultLang().register();
    public static final ItemEntry<GoldenSpatulaV4> GOLDEN_SPATULA_V4 = SkilletManCoreMod.REGISTRATE.item("golden_spatula_v4", p -> new GoldenSpatulaV4(p.stacksTo(1)))
            .tag(TagGen.UTENSILS)
            .model((ctx, pvd) -> pvd.getBuilder(ctx.getName()).parent(new ModelFile.UncheckedModelFile(SkilletManCoreMod.MOD_ID + ":item/golden_spatula")))
            .defaultLang().register();
    public static final ItemEntry<GoldenSpatulaV5> GOLDEN_SPATULA_V5 = SkilletManCoreMod.REGISTRATE.item("golden_spatula_v5", p -> new GoldenSpatulaV5(p.stacksTo(1)))
            .tag(TagGen.UTENSILS)
            .model((ctx, pvd) -> pvd.getBuilder(ctx.getName()).parent(new ModelFile.UncheckedModelFile(SkilletManCoreMod.MOD_ID + ":item/golden_spatula")))
            .defaultLang().register();
    public static final ItemEntry<DiamondSpatulaBase> DIAMOND_SPATULA = SkilletManCoreMod.REGISTRATE.item("diamond_spatula", p -> new DiamondSpatulaBase(p.stacksTo(1)))
            .tag(TagGen.UTENSILS)
            .model((ctx, pvd) -> pvd.handheld(ctx))
            .defaultLang().register();
    public static final ItemEntry<DiamondSpatulaV2> DIAMOND_SPATULA_V2 = SkilletManCoreMod.REGISTRATE.item("diamond_spatula_v2", p -> new DiamondSpatulaV2(p.stacksTo(1)))
            .tag(TagGen.UTENSILS)
            .model((ctx, pvd) -> pvd.getBuilder(ctx.getName()).parent(new ModelFile.UncheckedModelFile(SkilletManCoreMod.MOD_ID + ":item/diamond_spatula")))
            .defaultLang().register();
    public static final ItemEntry<DiamondSpatulaV3> DIAMOND_SPATULA_V3 = SkilletManCoreMod.REGISTRATE.item("diamond_spatula_v3", p -> new DiamondSpatulaV3(p.stacksTo(1)))
            .tag(TagGen.UTENSILS)
            .model((ctx, pvd) -> pvd.getBuilder(ctx.getName()).parent(new ModelFile.UncheckedModelFile(SkilletManCoreMod.MOD_ID + ":item/diamond_spatula")))
            .defaultLang().register();
    public static final ItemEntry<DiamondSpatulaV4> DIAMOND_SPATULA_V4 = SkilletManCoreMod.REGISTRATE.item("diamond_spatula_v4", p -> new DiamondSpatulaV4(p.stacksTo(1)))
            .tag(TagGen.UTENSILS)
            .model((ctx, pvd) -> pvd.getBuilder(ctx.getName()).parent(new ModelFile.UncheckedModelFile(SkilletManCoreMod.MOD_ID + ":item/diamond_spatula")))
            .defaultLang().register();
    public static final ItemEntry<DiamondSpatulaV5> DIAMOND_SPATULA_V5 = SkilletManCoreMod.REGISTRATE.item("diamond_spatula_v5", p -> new DiamondSpatulaV5(p.stacksTo(1)))
            .tag(TagGen.UTENSILS)
            .model((ctx, pvd) -> pvd.getBuilder(ctx.getName()).parent(new ModelFile.UncheckedModelFile(SkilletManCoreMod.MOD_ID + ":item/diamond_spatula")))
            .defaultLang().register();
    public static final ItemEntry<DirtPlateItem> DIRT_PLATE = SkilletManCoreMod.REGISTRATE.item("dirt_plate", DirtPlateItem::new)
            .defaultModel()
            .tag(TagGen.UTENSILS)
            .defaultLang().register();

    public static final ItemEntry<Item> DODGE_ICON = SkilletManCoreMod.REGISTRATE.item("dodge_icon", Item::new)
            .defaultModel()
            .defaultLang().register();
    public static final ItemEntry<Item> PARRY_ICON = SkilletManCoreMod.REGISTRATE.item("parry_icon", Item::new)
            .defaultModel()
            .defaultLang().register();
    public static final ItemEntry<SimpleDescriptionFoilItem> WEAPON_RAFFLE_TICKET = SkilletManCoreMod.REGISTRATE.item("weapon_raffle_ticket", properties -> new SimpleDescriptionFoilItem(properties.fireResistant().rarity(Rarity.EPIC)))
            .defaultModel()
            .defaultLang().register();
    public static final ItemEntry<SimpleDescriptionFoilItem> ARMOR_RAFFLE_TICKET = SkilletManCoreMod.REGISTRATE.item("armor_raffle_ticket", properties -> new SimpleDescriptionFoilItem(properties.fireResistant().rarity(Rarity.EPIC)))
            .defaultModel()
            .defaultLang().register();

    public static final ItemEntry<SimpleDescriptionFoilItem> SKILL_BOOK_RAFFLE_TICKET = SkilletManCoreMod.REGISTRATE.item("skill_book_raffle_ticket", properties -> new SimpleDescriptionFoilItem(properties.fireResistant().rarity(Rarity.EPIC)))
            .defaultModel()
            .defaultLang().register();

    public static final ItemEntry<SimpleDescriptionFoilItem> DISC_RAFFLE_TICKET = SkilletManCoreMod.REGISTRATE.item("disk_raffle_ticket", properties -> new SimpleDescriptionFoilItem(properties.fireResistant().rarity(Rarity.EPIC)))
            .defaultModel()
            .defaultLang().register();
    public static final ItemEntry<SimpleDescriptionFoilItem> PET_RAFFLE_TICKET = SkilletManCoreMod.REGISTRATE.item("pet_raffle_ticket", properties -> new SimpleDescriptionFoilItem(properties.fireResistant().rarity(Rarity.EPIC)))
            .defaultModel()
            .defaultLang().register();
    public static final ItemEntry<SimpleDescriptionFoilItem> DOLL_RAFFLE_TICKET = SkilletManCoreMod.REGISTRATE.item("doll_raffle_ticket", properties -> new SimpleDescriptionFoilItem(properties.fireResistant().rarity(Rarity.EPIC)))
            .defaultModel()
            .defaultLang().register();
    public static void register() {
    }

}

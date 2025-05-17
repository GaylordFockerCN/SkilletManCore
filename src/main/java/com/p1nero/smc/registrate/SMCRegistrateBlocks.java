package com.p1nero.smc.registrate;

import com.p1nero.smc.SkilletManCoreMod;
import com.p1nero.smc.block.custom.ChairBlock;
import com.p1nero.smc.block.custom.DiamondCuisineSkilletBlock;
import com.p1nero.smc.block.custom.GoldenCuisineSkilletBlock;
import com.p1nero.smc.block.entity.ChairBlockEntity;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.tterrag.registrate.util.entry.BlockEntry;
import dev.xkmc.cuisinedelight.content.block.CuisineSkilletBlock;
import dev.xkmc.cuisinedelight.content.block.CuisineSkilletBlockEntity;
import dev.xkmc.cuisinedelight.content.client.CuisineSkilletRenderer;
import dev.xkmc.cuisinedelight.init.data.CopySkilletFunction;
import dev.xkmc.cuisinedelight.init.registrate.CDItems;
import dev.xkmc.l2library.util.data.LootTableTemplate;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import vectorwing.farmersdelight.common.block.SkilletBlock;

public class SMCRegistrateBlocks {
    public static final BlockEntry<GoldenCuisineSkilletBlock> GOLDEN_SKILLET;

    public static final BlockEntityEntry<CuisineSkilletBlockEntity> GOLDEN_BE_SKILLET;
    public static final BlockEntry<DiamondCuisineSkilletBlock> DIAMOND_SKILLET;

    public static final BlockEntityEntry<CuisineSkilletBlockEntity> DIAMOND_BE_SKILLET;
    public static final BlockEntry<ChairBlock> CHAIR;

    public static final BlockEntityEntry<ChairBlockEntity> CHAIR_ENTITY;

    static {
        GOLDEN_SKILLET = SkilletManCoreMod.REGISTRATE.block("golden_cuisine_skillet", p -> new GoldenCuisineSkilletBlock(
                        BlockBehaviour.Properties.of().mapColor(MapColor.METAL)
                                .strength(0.5F, 6.0F).sound(SoundType.LANTERN)))
                .blockstate((ctx, pvd) -> pvd.getVariantBuilder(ctx.getEntry()).forAllStates(e ->
                        ConfiguredModel.builder().modelFile(new ModelFile.UncheckedModelFile(
                                        e.getValue(SkilletBlock.SUPPORT) ?
                                                ResourceLocation.fromNamespaceAndPath(SkilletManCoreMod.MOD_ID, "block/golden_cuisine_skillet_tray") :
                                                ResourceLocation.fromNamespaceAndPath(SkilletManCoreMod.MOD_ID, "block/golden_cuisine_skillet"))
                                ).rotationY(((int) e.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot()) % 360)
                                .build()))
                .loot((loot, block) -> loot.add(block,
                        LootTable.lootTable().withPool(
                                LootTableTemplate.getPool(1, 0).add(
                                        LootItem.lootTableItem(SMCRegistrateItems.GOLDEN_SKILLET.get())
                                                .apply(CopySkilletFunction.builder())
                                ))))
                .register();

        GOLDEN_BE_SKILLET = SkilletManCoreMod.REGISTRATE.blockEntity("golden_cuisine_skillet", CuisineSkilletBlockEntity::new)
                .validBlock(GOLDEN_SKILLET).renderer(() -> CuisineSkilletRenderer::new).register();

        DIAMOND_SKILLET = SkilletManCoreMod.REGISTRATE.block("diamond_cuisine_skillet", p -> new DiamondCuisineSkilletBlock(
                        BlockBehaviour.Properties.of().mapColor(MapColor.METAL)
                                .strength(0.5F, 6.0F).sound(SoundType.LANTERN)))
                .blockstate((ctx, pvd) -> pvd.getVariantBuilder(ctx.getEntry()).forAllStates(e ->
                        ConfiguredModel.builder().modelFile(new ModelFile.UncheckedModelFile(
                                        e.getValue(SkilletBlock.SUPPORT) ?
                                                ResourceLocation.fromNamespaceAndPath(SkilletManCoreMod.MOD_ID, "block/diamond_cuisine_skillet_tray") :
                                                ResourceLocation.fromNamespaceAndPath(SkilletManCoreMod.MOD_ID, "block/diamond_cuisine_skillet"))
                                ).rotationY(((int) e.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot() + 180) % 360)
                                .build()))
                .loot((loot, block) -> loot.add(block,
                        LootTable.lootTable().withPool(
                                LootTableTemplate.getPool(1, 0).add(
                                        LootItem.lootTableItem(SMCRegistrateItems.DIAMOND_SKILLET.get())
                                                .apply(CopySkilletFunction.builder())
                                ))))
                .register();

        DIAMOND_BE_SKILLET = SkilletManCoreMod.REGISTRATE.blockEntity("diamond_cuisine_skillet", CuisineSkilletBlockEntity::new)
                .validBlock(DIAMOND_SKILLET).renderer(() -> CuisineSkilletRenderer::new).register();

        CHAIR = SkilletManCoreMod.REGISTRATE.block("power_chair", p -> new ChairBlock(
                        BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_WHITE)
                                .noOcclusion()
                                .strength(0.5F, 6.0F).sound(SoundType.WOOD)))
                .blockstate((ctx, pvd) -> pvd.getVariantBuilder(ctx.getEntry()).forAllStates(e ->
                        ConfiguredModel.builder().modelFile(new ModelFile.UncheckedModelFile(ResourceLocation.fromNamespaceAndPath(SkilletManCoreMod.MOD_ID, "block/chair")))
                                .rotationY(((int) e.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot() + 180) % 360)
                                .build()))
                .defaultLoot()
                .register();

        CHAIR_ENTITY = SkilletManCoreMod.REGISTRATE.blockEntity("power_chair", ChairBlockEntity::new)
                .validBlock(CHAIR).register();
    }

    public static void register() {
    }

}

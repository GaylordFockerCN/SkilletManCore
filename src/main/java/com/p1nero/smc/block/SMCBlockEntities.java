package com.p1nero.smc.block;

import com.p1nero.smc.SkilletManCoreMod;
import com.p1nero.smc.block.entity.BetterStructureBlockEntity;
import com.p1nero.smc.block.entity.MainCookBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SMCBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> REGISTRY =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, SkilletManCoreMod.MOD_ID);

    public static final RegistryObject<BlockEntityType<BetterStructureBlockEntity>> BETTER_STRUCTURE_BLOCK_ENTITY =
            REGISTRY.register("better_structure_block_entity", () ->
                    BlockEntityType.Builder.of(BetterStructureBlockEntity::new,
                            SMCBlocks.BETTER_STRUCTURE_BLOCK.get()).build(null));

    public static final RegistryObject<BlockEntityType<MainCookBlockEntity>> MAIN_COOK_BLOCK_ENTITY =
            REGISTRY.register("main_cook_block_entity", () ->
                    BlockEntityType.Builder.of(MainCookBlockEntity::new,
                            SMCBlocks.MAIN_COOK_BLOCK.get()).build(null));

}

package com.p1nero.smc.block;

import com.p1nero.smc.SkilletManCoreMod;
import com.p1nero.smc.block.custom.*;
import com.p1nero.smc.item.SMCItems;
import com.p1nero.smc.item.custom.SimpleDescriptionBlockItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class SMCBlocks {
    public static final DeferredRegister<Block> REGISTRY =
            DeferredRegister.create(ForgeRegistries.BLOCKS, SkilletManCoreMod.MOD_ID);

    public static final RegistryObject<Block> BETTER_STRUCTURE_BLOCK = registerBlock("better_structure_block",
            () -> new BetterStructureBlock(BlockBehaviour.Properties.copy(Blocks.STRUCTURE_BLOCK).noOcclusion()));
    public static final RegistryObject<Block> MAIN_COOK_BLOCK = registerBlock("main_cook_block",
            () -> new MainCookBlock(BlockBehaviour.Properties.copy(Blocks.STRUCTURE_BLOCK).noLootTable()));

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = REGISTRY.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
        return SMCItems.REGISTRY.register(name, () -> new SimpleDescriptionBlockItem(block.get(), new Item.Properties()));
    }

}

package com.p1nero.smc.item;

import com.p1nero.smc.SkilletManCoreMod;
import com.p1nero.smc.block.SMCBlocks;
import com.p1nero.smc.registrate.SMCRegistrateBlocks;
import com.p1nero.smc.registrate.SMCRegistrateItems;
import dev.xkmc.cuisinedelight.init.registrate.CDItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import yesman.epicfight.world.item.EpicFightItems;

public class SMCItemTabs {
    public static final DeferredRegister<CreativeModeTab> REGISTRY = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, SkilletManCoreMod.MOD_ID);

    public static final RegistryObject<CreativeModeTab> ITEMS = REGISTRY.register("items",
            () -> CreativeModeTab.builder().title(Component.translatable("itemGroup.smc.items")).icon(() -> {
                return new ItemStack(SMCRegistrateItems.DIAMOND_SPATULA_V5.get());
            }).withTabsBefore(CDItems.TAB.getKey()).displayItems((params, output) -> {
                output.accept(SMCBlocks.MAIN_COOK_BLOCK.get());
                output.accept(SMCRegistrateBlocks.MAIN_COOK_BLOCK2.get().asItem().getDefaultInstance());
                output.accept(SMCRegistrateItems.END_TELEPORTER.get());
                output.accept(SMCItems.LEFT_SKILLET_RIGHT_SPATULA.get());
                output.accept(SMCRegistrateItems.COOK_GUIDE_BOOK_ITEM.get());
                output.accept(SMCRegistrateItems.CREATE_COOK_GUIDE_BOOK_ITEM.get());

				output.accept(SMCRegistrateItems.IRON_SKILLET_LEVEL2);
                output.accept(SMCRegistrateItems.IRON_SKILLET_LEVEL3);
                output.accept(SMCRegistrateItems.IRON_SKILLET_LEVEL4);
                output.accept(SMCRegistrateItems.IRON_SKILLET_LEVEL5);
                output.accept(SMCRegistrateItems.GOLDEN_SKILLET);
                output.accept(SMCRegistrateItems.GOLDEN_SKILLET_V2);
                output.accept(SMCRegistrateItems.GOLDEN_SKILLET_V3);
                output.accept(SMCRegistrateItems.GOLDEN_SKILLET_V4);
                output.accept(SMCRegistrateItems.GOLDEN_SKILLET_V5);
                output.accept(SMCRegistrateItems.DIAMOND_SKILLET);
                output.accept(SMCRegistrateItems.DIAMOND_SKILLET_V2);
                output.accept(SMCRegistrateItems.DIAMOND_SKILLET_V3);
                output.accept(SMCRegistrateItems.DIAMOND_SKILLET_V4);
                output.accept(SMCRegistrateItems.DIAMOND_SKILLET_V5);

                output.accept(SMCRegistrateItems.SPATULA_V2);
                output.accept(SMCRegistrateItems.SPATULA_V3);
                output.accept(SMCRegistrateItems.SPATULA_V4);
                output.accept(SMCRegistrateItems.SPATULA_V5);
                output.accept(SMCRegistrateItems.GOLDEN_SPATULA);
                output.accept(SMCRegistrateItems.GOLDEN_SPATULA_V2);
                output.accept(SMCRegistrateItems.GOLDEN_SPATULA_V3);
                output.accept(SMCRegistrateItems.GOLDEN_SPATULA_V4);
                output.accept(SMCRegistrateItems.GOLDEN_SPATULA_V5);
                output.accept(SMCRegistrateItems.DIAMOND_SPATULA);
                output.accept(SMCRegistrateItems.DIAMOND_SPATULA_V2);
                output.accept(SMCRegistrateItems.DIAMOND_SPATULA_V3);
                output.accept(SMCRegistrateItems.DIAMOND_SPATULA_V4);
                output.accept(SMCRegistrateItems.DIAMOND_SPATULA_V5);

                output.accept(SMCRegistrateItems.WEAPON_RAFFLE_TICKET);
                output.accept(SMCRegistrateItems.ARMOR_RAFFLE_TICKET);
                output.accept(SMCRegistrateItems.SKILL_BOOK_RAFFLE_TICKET);
                output.accept(SMCRegistrateItems.DISC_RAFFLE_TICKET);
                output.accept(SMCRegistrateItems.PET_RAFFLE_TICKET);
                output.accept(SMCItems.NO_BRAIN_VILLAGER_SPAWN_EGG.get());
                output.accept(SMCItems.GOOD_SUPER_GOLEM_SPAWN_EGG.get());
            }).build());
}

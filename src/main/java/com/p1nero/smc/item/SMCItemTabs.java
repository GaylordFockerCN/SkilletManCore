package com.p1nero.smc.item;

import com.p1nero.smc.SkilletManCoreMod;
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
                return new ItemStack(SMCRegistrateItems.IRON_SKILLET_LEVEL5.get());//TODO 换别的锅或眼罩
            }).withTabsBefore(CDItems.TAB.getKey()).displayItems((params, output) -> {
				output.accept(SMCRegistrateItems.IRON_SKILLET_LEVEL2);
                output.accept(SMCRegistrateItems.IRON_SKILLET_LEVEL3);
                output.accept(SMCRegistrateItems.IRON_SKILLET_LEVEL4);
                output.accept(SMCRegistrateItems.IRON_SKILLET_LEVEL5);
            }).build());
}

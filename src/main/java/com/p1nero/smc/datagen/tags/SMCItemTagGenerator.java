package com.p1nero.smc.datagen.tags;

import com.p1nero.smc.SkilletManCoreMod;
import dev.xkmc.cuisinedelight.init.CuisineDelight;
import dev.xkmc.cuisinedelight.init.registrate.PlateFood;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class SMCItemTagGenerator extends ItemTagsProvider {
    public SMCItemTagGenerator(PackOutput p_275343_, CompletableFuture<HolderLookup.Provider> p_275729_,
                               CompletableFuture<TagLookup<Block>> p_275322_, @Nullable ExistingFileHelper existingFileHelper) {
        super(p_275343_, p_275729_, p_275322_, SkilletManCoreMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider pProvider) {
        TagKey<Item> tagKey = ItemTags.create(ResourceLocation.fromNamespaceAndPath(SkilletManCoreMod.MOD_ID, "plate_food"));
        IntrinsicHolderTagsProvider.IntrinsicTagAppender<Item> tagAppender = this.tag(tagKey).replace(false);
        for (PlateFood plateFood : PlateFood.values()) {
            tagAppender.add(plateFood.item.asItem());
        }

        TagKey<Item> fermentable1 = ItemTags.create(ResourceLocation.fromNamespaceAndPath(SkilletManCoreMod.MOD_ID, "fermentable1"));
        IntrinsicHolderTagsProvider.IntrinsicTagAppender<Item> tagAppender1 = this.tag(fermentable1).replace(false);
        TagKey<Item> fermentable2 = ItemTags.create(ResourceLocation.fromNamespaceAndPath(SkilletManCoreMod.MOD_ID, "fermentable2"));
        IntrinsicHolderTagsProvider.IntrinsicTagAppender<Item> tagAppender2 = this.tag(fermentable2).replace(false);
        TagKey<Item> fermentable3 = ItemTags.create(ResourceLocation.fromNamespaceAndPath(SkilletManCoreMod.MOD_ID, "fermentable3"));
        IntrinsicHolderTagsProvider.IntrinsicTagAppender<Item> tagAppender3 = this.tag(fermentable3).replace(false);
        ForgeRegistries.ITEMS.getValues().forEach(item -> {
            if(item.getDescriptionId().contains(CuisineDelight.MODID)){
                return;
            }
            FoodProperties foodProperties = item.getFoodProperties(item.getDefaultInstance(), null);
            if(foodProperties != null) {
                int nutrition = foodProperties.getNutrition();
                if(nutrition < 5) {
                    tagAppender1.add(item);
                } else if(nutrition < 10) {
                    tagAppender2.add(item);
                } else {
                    tagAppender3.add(item);
                }
            }
        });

    }
}
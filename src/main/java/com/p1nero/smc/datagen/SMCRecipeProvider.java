package com.p1nero.smc.datagen;

import com.p1nero.smc.SkilletManCoreMod;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class SMCRecipeProvider extends RecipeProvider {
    public SMCRecipeProvider(PackOutput output) {
        super(output);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> consumer) {

    }

    protected static void nineBlockStorageRecipes(Consumer<FinishedRecipe> consumer, RecipeCategory category, ItemLike itemLike, RecipeCategory category1, ItemLike itemLike1) {
        nineBlockStorageRecipes(consumer, category, itemLike, category1, itemLike1, getSimpleRecipeName(itemLike1), (String)null, getSimpleRecipeName(itemLike), (String)null);
    }

    protected static void nineBlockStorageRecipes(Consumer<FinishedRecipe> consumer, RecipeCategory category, ItemLike itemLike,
                                                  RecipeCategory category1, ItemLike itemLike1, String p_250475_, @Nullable String p_248641_,
                                                  String p_252237_, @Nullable String p_250414_) {
        ShapelessRecipeBuilder.shapeless(category, itemLike, 9).requires(itemLike1).group(p_250414_).unlockedBy(getHasName(itemLike1), has(itemLike1))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(SkilletManCoreMod.MOD_ID, p_252237_+"shapeless"));//加后缀防止重名
        ShapedRecipeBuilder.shaped(category1, itemLike1).define('#', itemLike).pattern("###").pattern("###").pattern("###").group(p_248641_)
                .unlockedBy(getHasName(itemLike), has(itemLike)).save(consumer, ResourceLocation.fromNamespaceAndPath(SkilletManCoreMod.MOD_ID, p_250475_+"shaped"));
    }

    protected static void woodBlockStorageRecipes(Consumer<FinishedRecipe> consumer, RecipeCategory category, ItemLike itemLike, ItemLike itemLike1) {
        woodBlockStorageRecipes(consumer, category, itemLike, itemLike1, getSimpleRecipeName(itemLike), (String)null);
    }

    protected static void woodBlockStorageRecipes(Consumer<FinishedRecipe> consumer, RecipeCategory category, ItemLike itemLike,
                                                   ItemLike itemLike1,String p_252237_, @Nullable String p_250414_) {
        ShapelessRecipeBuilder.shapeless(category, itemLike, 4).requires(itemLike1).group(p_250414_).unlockedBy(getHasName(itemLike1), has(itemLike1))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(SkilletManCoreMod.MOD_ID, p_252237_));
    }

    protected static void oreSmelting(ItemLike result, ItemLike ingredient, float experience, int cookingTime, Consumer<FinishedRecipe> consume) {
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(ingredient), RecipeCategory.MISC, result, experience, cookingTime).unlockedBy(getHasName(ingredient), has(ingredient))
                .save(consume, ResourceLocation.fromNamespaceAndPath(SkilletManCoreMod.MOD_ID, getItemName(result)) + "_from_smelting" + "_" + getItemName(ingredient));
    }

    protected static void smithing(Consumer<FinishedRecipe> consumer, ItemLike ingredient1, ItemLike ingredient2, ItemLike ingredient3, RecipeCategory category, Item output) {
        SmithingTransformRecipeBuilder.smithing(Ingredient.of(ingredient1), Ingredient.of(ingredient2), Ingredient.of(ingredient3), category, output).unlocks(getHasName(ingredient1), has(ingredient1)).save(consumer, "dote_" + getItemName(output) + "_smithing");
    }

}

package com.p1nero.smc.datagen;

import com.p1nero.smc.registrate.SMCRegistrateItems;
import dev.xkmc.cuisinedelight.init.registrate.CDItems;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;

import java.util.function.Consumer;

public class SMCRecipeGenerator extends SMCRecipeProvider implements IConditionBuilder {
    public SMCRecipeGenerator(PackOutput output) {
        super(output);
    }
    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, SMCRegistrateItems.IRON_SKILLET_LEVEL2.get(), 1)
                .requires(CDItems.SKILLET.get(), 2)
                .unlockedBy(getHasName(CDItems.SKILLET.get()), has(CDItems.SKILLET.get()))
                .save(consumer);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, SMCRegistrateItems.IRON_SKILLET_LEVEL3.get(), 1)
                .requires(SMCRegistrateItems.IRON_SKILLET_LEVEL2.get(), 2)
                .unlockedBy(getHasName(SMCRegistrateItems.IRON_SKILLET_LEVEL2.get()), has(SMCRegistrateItems.IRON_SKILLET_LEVEL2.get()))
                .save(consumer);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, SMCRegistrateItems.IRON_SKILLET_LEVEL4.get(), 1)
                .requires(SMCRegistrateItems.IRON_SKILLET_LEVEL3.get(), 2)
                .unlockedBy(getHasName(SMCRegistrateItems.IRON_SKILLET_LEVEL3.get()), has(SMCRegistrateItems.IRON_SKILLET_LEVEL3.get()))
                .save(consumer);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, SMCRegistrateItems.IRON_SKILLET_LEVEL5.get(), 1)
                .requires(SMCRegistrateItems.IRON_SKILLET_LEVEL4.get(), 2)
                .unlockedBy(getHasName(SMCRegistrateItems.IRON_SKILLET_LEVEL4.get()), has(SMCRegistrateItems.IRON_SKILLET_LEVEL4.get()))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, SMCRegistrateItems.GOLDEN_SKILLET_V2.get(), 1)
                .requires(SMCRegistrateItems.GOLDEN_SKILLET.get(), 2)
                .unlockedBy(getHasName(SMCRegistrateItems.GOLDEN_SKILLET.get()), has(SMCRegistrateItems.GOLDEN_SKILLET.get()))
                .save(consumer);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, SMCRegistrateItems.GOLDEN_SKILLET_V3.get(), 1)
                .requires(SMCRegistrateItems.GOLDEN_SKILLET_V2.get(), 2)
                .unlockedBy(getHasName(SMCRegistrateItems.GOLDEN_SKILLET_V2.get()), has(SMCRegistrateItems.GOLDEN_SKILLET_V2.get()))
                .save(consumer);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, SMCRegistrateItems.GOLDEN_SKILLET_V4.get(), 1)
                .requires(SMCRegistrateItems.GOLDEN_SKILLET_V3.get(), 2)
                .unlockedBy(getHasName(SMCRegistrateItems.GOLDEN_SKILLET_V3.get()), has(SMCRegistrateItems.GOLDEN_SKILLET_V3.get()))
                .save(consumer);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, SMCRegistrateItems.GOLDEN_SKILLET_V5.get(), 1)
                .requires(SMCRegistrateItems.GOLDEN_SKILLET_V4.get(), 2)
                .unlockedBy(getHasName(SMCRegistrateItems.GOLDEN_SKILLET_V4.get()), has(SMCRegistrateItems.GOLDEN_SKILLET_V4.get()))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, SMCRegistrateItems.DIAMOND_SKILLET_V2.get(), 1)
                .requires(SMCRegistrateItems.DIAMOND_SKILLET.get(), 2)
                .unlockedBy(getHasName(SMCRegistrateItems.DIAMOND_SKILLET.get()), has(SMCRegistrateItems.DIAMOND_SKILLET.get()))
                .save(consumer);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, SMCRegistrateItems.DIAMOND_SKILLET_V3.get(), 1)
                .requires(SMCRegistrateItems.DIAMOND_SKILLET_V2.get(), 2)
                .unlockedBy(getHasName(SMCRegistrateItems.DIAMOND_SKILLET_V2.get()), has(SMCRegistrateItems.DIAMOND_SKILLET_V2.get()))
                .save(consumer);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, SMCRegistrateItems.DIAMOND_SKILLET_V4.get(), 1)
                .requires(SMCRegistrateItems.DIAMOND_SKILLET_V3.get(), 2)
                .unlockedBy(getHasName(SMCRegistrateItems.DIAMOND_SKILLET_V3.get()), has(SMCRegistrateItems.DIAMOND_SKILLET_V3.get()))
                .save(consumer);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, SMCRegistrateItems.DIAMOND_SKILLET_V5.get(), 1)
                .requires(SMCRegistrateItems.DIAMOND_SKILLET_V4.get(), 2)
                .unlockedBy(getHasName(SMCRegistrateItems.DIAMOND_SKILLET_V4.get()), has(SMCRegistrateItems.DIAMOND_SKILLET_V4.get()))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, SMCRegistrateItems.SPATULA_V2.get(), 1)
                .requires(CDItems.SPATULA.get(), 2)
                .unlockedBy(getHasName(CDItems.SPATULA.get()), has(CDItems.SPATULA.get()))
                .save(consumer);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, SMCRegistrateItems.SPATULA_V3.get(), 1)
                .requires(SMCRegistrateItems.SPATULA_V2.get(), 2)
                .unlockedBy(getHasName(SMCRegistrateItems.SPATULA_V2.get()), has(SMCRegistrateItems.SPATULA_V2.get()))
                .save(consumer);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, SMCRegistrateItems.SPATULA_V4.get(), 1)
                .requires(SMCRegistrateItems.SPATULA_V3.get(), 2)
                .unlockedBy(getHasName(SMCRegistrateItems.SPATULA_V3.get()), has(SMCRegistrateItems.SPATULA_V3.get()))
                .save(consumer);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, SMCRegistrateItems.SPATULA_V5.get(), 1)
                .requires(SMCRegistrateItems.SPATULA_V4.get(), 2)
                .unlockedBy(getHasName(SMCRegistrateItems.SPATULA_V4.get()), has(SMCRegistrateItems.SPATULA_V4.get()))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, SMCRegistrateItems.GOLDEN_SPATULA_V2.get(), 1)
                .requires(SMCRegistrateItems.GOLDEN_SPATULA.get(), 2)
                .unlockedBy(getHasName(SMCRegistrateItems.GOLDEN_SPATULA.get()), has(SMCRegistrateItems.GOLDEN_SPATULA.get()))
                .save(consumer);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, SMCRegistrateItems.GOLDEN_SPATULA_V3.get(), 1)
                .requires(SMCRegistrateItems.GOLDEN_SPATULA_V2.get(), 2)
                .unlockedBy(getHasName(SMCRegistrateItems.GOLDEN_SPATULA_V2.get()), has(SMCRegistrateItems.GOLDEN_SPATULA_V2.get()))
                .save(consumer);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, SMCRegistrateItems.GOLDEN_SPATULA_V4.get(), 1)
                .requires(SMCRegistrateItems.GOLDEN_SPATULA_V3.get(), 2)
                .unlockedBy(getHasName(SMCRegistrateItems.GOLDEN_SPATULA_V3.get()), has(SMCRegistrateItems.GOLDEN_SPATULA_V3.get()))
                .save(consumer);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, SMCRegistrateItems.GOLDEN_SPATULA_V5.get(), 1)
                .requires(SMCRegistrateItems.GOLDEN_SPATULA_V4.get(), 2)
                .unlockedBy(getHasName(SMCRegistrateItems.GOLDEN_SPATULA_V4.get()), has(SMCRegistrateItems.GOLDEN_SPATULA_V4.get()))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, SMCRegistrateItems.DIAMOND_SPATULA_V2.get(), 1)
                .requires(SMCRegistrateItems.DIAMOND_SPATULA.get(), 2)
                .unlockedBy(getHasName(SMCRegistrateItems.DIAMOND_SPATULA.get()), has(SMCRegistrateItems.DIAMOND_SPATULA.get()))
                .save(consumer);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, SMCRegistrateItems.DIAMOND_SPATULA_V3.get(), 1)
                .requires(SMCRegistrateItems.DIAMOND_SPATULA_V2.get(), 2)
                .unlockedBy(getHasName(SMCRegistrateItems.DIAMOND_SPATULA_V2.get()), has(SMCRegistrateItems.DIAMOND_SPATULA_V2.get()))
                .save(consumer);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, SMCRegistrateItems.DIAMOND_SPATULA_V4.get(), 1)
                .requires(SMCRegistrateItems.DIAMOND_SPATULA_V3.get(), 2)
                .unlockedBy(getHasName(SMCRegistrateItems.DIAMOND_SPATULA_V3.get()), has(SMCRegistrateItems.DIAMOND_SPATULA_V3.get()))
                .save(consumer);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, SMCRegistrateItems.DIAMOND_SPATULA_V5.get(), 1)
                .requires(SMCRegistrateItems.DIAMOND_SPATULA_V4.get(), 2)
                .unlockedBy(getHasName(SMCRegistrateItems.DIAMOND_SPATULA_V4.get()), has(SMCRegistrateItems.DIAMOND_SPATULA_V4.get()))
                .save(consumer);

    }

}

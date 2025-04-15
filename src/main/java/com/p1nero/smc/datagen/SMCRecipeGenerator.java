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
                .requires(CDItems.SKILLET.get(), 9)
                .unlockedBy(getHasName(CDItems.SKILLET.get()), has(CDItems.SKILLET.get()))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, SMCRegistrateItems.IRON_SKILLET_LEVEL3.get(), 1)
                .requires(SMCRegistrateItems.IRON_SKILLET_LEVEL2.get(), 9)
                .unlockedBy(getHasName(CDItems.SKILLET.get()), has(CDItems.SKILLET.get()))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, SMCRegistrateItems.IRON_SKILLET_LEVEL4.get(), 1)
                .requires(SMCRegistrateItems.IRON_SKILLET_LEVEL3.get(), 9)
                .unlockedBy(getHasName(CDItems.SKILLET.get()), has(CDItems.SKILLET.get()))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, SMCRegistrateItems.IRON_SKILLET_LEVEL5.get(), 1)
                .requires(SMCRegistrateItems.IRON_SKILLET_LEVEL4.get(), 9)
                .unlockedBy(getHasName(CDItems.SKILLET.get()), has(CDItems.SKILLET.get()))
                .save(consumer);
    }

}

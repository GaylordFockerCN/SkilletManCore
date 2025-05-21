package com.p1nero.smc.mixin;

import com.p1nero.smc.SMCConfig;
import dev.xkmc.cuisinedelight.content.client.CookingOverlay;
import dev.xkmc.cuisinedelight.content.client.PieRenderer;
import dev.xkmc.cuisinedelight.content.logic.CookTransformConfig;
import dev.xkmc.cuisinedelight.content.logic.CookingData;
import dev.xkmc.cuisinedelight.content.logic.IngredientConfig;
import dev.xkmc.cuisinedelight.content.logic.transform.CookTransform;
import dev.xkmc.cuisinedelight.init.data.CDConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Iterator;

@Mixin(value = CookingOverlay.class, remap = false)
public abstract class CookingOverlayMixin {

    @Shadow
    public static @Nullable CookingData getData() {
        return null;
    }

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void smc$render(ForgeGui gui, GuiGraphics g, float partialTick, int screenWidth, int screenHeight, CallbackInfo ci) {
        if (Minecraft.getInstance().level != null) {
            CookingData data = getData();
            if (data != null && !data.contents.isEmpty()) {
                float scale = CDConfig.CLIENT.uiScale.get().floatValue();
                screenHeight = Math.round((float)screenHeight / scale);
                g.pose().pushPose();
                g.pose().scale(scale, scale, scale);
                data.update(Minecraft.getInstance().level.getGameTime());
                int y = screenHeight / 2 - data.contents.size() * 10;
                int x = (int) (screenWidth / 2 - SMCConfig.CUISINE_UI_X.get() * scale);
                Font font = Minecraft.getInstance().font;

                CookingData.CookingEntry entry;
                ItemStack food;
                Iterator<CookingData.CookingEntry> var11;
                for(var11 = data.contents.iterator(); var11.hasNext(); y += 20) {
                    entry = var11.next();
                    food = entry.getItem();
                    CookTransform handle = CookTransformConfig.get(food);
                    ItemStack render = handle.renderStack(entry.getStage(data), food);
                    g.renderItem(render, x, y + 2);
                    g.renderItemDecorations(font, render, x, y + 2);
                }

                x += 20;
                y = screenHeight / 2 - data.contents.size() * 10;

                for(var11 = data.contents.iterator(); var11.hasNext(); y += 20) {
                    entry = var11.next();
                    food = entry.getItem();
                    IngredientConfig.IngredientEntry config = IngredientConfig.get().getEntry(food);
                    if (config != null) {
                        PieRenderer cook = new PieRenderer(g, x + 8, y + 12);
                        float min = (float)config.min_time / 400.0F;
                        float max = (float)config.max_time / 400.0F;
                        cook.fillPie(0.0F, min, PieRenderer.Texture.PIE_GREEN);
                        cook.fillPie(min, max, PieRenderer.Texture.PIE_YELLOW);
                        cook.fillPie(max, 1.0F, PieRenderer.Texture.PIE_RED);
                        float cook_needle = Mth.clamp(entry.getDuration(data, partialTick) / 400.0F, 0.0F, 1.0F);
                        cook.drawNeedle(PieRenderer.Texture.NEEDLE_BLACK, cook_needle);
                        cook.drawIcon(PieRenderer.Texture.COOK);
                        PieRenderer flip = new PieRenderer(g, x + 28, y + 12);
                        float thr = (float)config.stir_time / 100.0F;
                        flip.fillPie(0.0F, thr, PieRenderer.Texture.PIE_GREEN);
                        flip.fillPie(thr, 1.0F, PieRenderer.Texture.PIE_RED);
                        float stir_current = Mth.clamp(entry.timeSinceStir(data, partialTick) / 100.0F, 0.0F, 1.0F);
                        float stir_max = Mth.clamp(Math.max(stir_current, entry.getMaxStirTime(data) / 100.0F), 0.0F, 1.0F);
                        flip.drawNeedle(PieRenderer.Texture.NEEDLE_BLACK, stir_current);
                        flip.drawNeedle(PieRenderer.Texture.NEEDLE_RED, stir_max + 0.5F);
                        flip.drawIcon(PieRenderer.Texture.FLIP);
                    }
                }

                g.pose().popPose();
            }
        }
        ci.cancel();
    }

}

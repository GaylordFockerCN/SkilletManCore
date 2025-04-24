package com.p1nero.smc.item.model;

import com.google.common.base.Suppliers;
import com.mojang.blaze3d.vertex.PoseStack;
import com.p1nero.smc.event.ClientModEvents;
import com.p1nero.smc.item.custom.SMCCuisineSkilletItem;
import com.p1nero.smc.item.custom.skillets.DiamondSkilletItem;
import com.p1nero.smc.item.custom.skillets.GoldenSkilletItem;
import dev.xkmc.cuisinedelight.content.client.CuisineSkilletRenderer;
import dev.xkmc.cuisinedelight.content.item.CuisineSkilletItem;
import dev.xkmc.cuisinedelight.content.logic.CookingData;
import dev.xkmc.cuisinedelight.init.CuisineDelightClient;
import dev.xkmc.l2library.util.Proxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class SMCSkilletBEWLR extends BlockEntityWithoutLevelRenderer {

    public static final Supplier<BlockEntityWithoutLevelRenderer> INSTANCE = Suppliers.memoize(() ->
            new SMCSkilletBEWLR(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels()));

    public static final IClientItemExtensions EXTENSIONS = new IClientItemExtensions() {

        @Override
        public BlockEntityWithoutLevelRenderer getCustomRenderer() {
            return INSTANCE.get();
        }

    };

    public SMCSkilletBEWLR(BlockEntityRenderDispatcher dispatcher, EntityModelSet set) {
        super(dispatcher, set);
    }

    public void onResourceManagerReload(@NotNull ResourceManager manager) {
    }

    @Override
    public void renderByItem(ItemStack stack, @NotNull ItemDisplayContext type, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int light, int overlay) {
        if (stack.isEmpty() || !(stack.getItem() instanceof SMCCuisineSkilletItem)) return;
        poseStack.popPose();
        poseStack.pushPose();
        ItemRenderer renderer = Minecraft.getInstance().getItemRenderer();
        ModelResourceLocation modelResourceLocation;
        if (stack.getItem() instanceof DiamondSkilletItem) {
            modelResourceLocation = ClientModEvents.DIAMOND_SKILLET_MODEL;
        } else if (stack.getItem() instanceof GoldenSkilletItem) {
            modelResourceLocation = ClientModEvents.GOLDEN_SKILLET_MODEL;
        } else {
            modelResourceLocation = CuisineDelightClient.SKILLET_MODEL;
        }
        BakedModel model = Minecraft.getInstance().getModelManager().getModel(modelResourceLocation);
        renderer.render(stack, type, false, poseStack, bufferSource, light, overlay, model);
        CookingData data = CuisineSkilletItem.getData(stack);
        if (data != null && !data.contents.isEmpty()) {
            data.update(Proxy.getClientWorld().getGameTime());
            poseStack.pushPose();
            model.applyTransform(type, poseStack, false);
            float time = 0;
            LocalPlayer player = Proxy.getClientPlayer();
            if (player.getMainHandItem() == stack || player.getOffhandItem() == stack) {
                time = player.getCooldowns().getCooldownPercent(stack.getItem(), Minecraft.getInstance().getPartialTick());
            }
            CuisineSkilletRenderer.renderItem(time, data, poseStack, bufferSource, light, overlay);
            poseStack.popPose();
        }
    }

}
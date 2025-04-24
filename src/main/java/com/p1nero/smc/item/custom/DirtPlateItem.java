package com.p1nero.smc.item.custom;

import com.p1nero.smc.SkilletManCoreMod;
import com.p1nero.smc.datagen.SMCAdvancementData;
import com.p1nero.smc.util.ItemUtil;
import dev.xkmc.cuisinedelight.content.block.CuisineSkilletBlockEntity;
import dev.xkmc.cuisinedelight.content.item.PlateItem;
import dev.xkmc.cuisinedelight.content.logic.CookedFoodData;
import dev.xkmc.cuisinedelight.content.logic.CookingData;
import dev.xkmc.cuisinedelight.content.recipe.BaseCuisineRecipe;
import dev.xkmc.cuisinedelight.init.registrate.CDItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DirtPlateItem extends PlateItem {
    public DirtPlateItem(Properties pProperties) {
        super(pProperties);
    }

    public static void giveScoreEffect(ServerPlayer player, int score) {
        if(score > 95) {
            player.displayClientMessage(SkilletManCoreMod.getInfo("full_score"), true);
            player.serverLevel().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_LEVELUP, player.getSoundSource(), 1.0F, 1.0F);
        } else if(score < 60){
            player.displayClientMessage(SkilletManCoreMod.getInfo("bad_score"), true);
            player.serverLevel().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.GLASS_BREAK, player.getSoundSource(), 1.0F, 1.0F);
        } else {
            player.displayClientMessage(SkilletManCoreMod.getInfo("middle_score"), true);
            player.serverLevel().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.EXPERIENCE_ORB_PICKUP, player.getSoundSource(), 1.0F, 1.0F);
        }
    }

    private void giveBack(ItemStack foodStack, CookedFoodData food, ReturnTarget target) {
        target.addItem(foodStack);
        target.addExp(food.score * food.size / 100);
        if(target instanceof PlateItem.PlayerTarget playerTarget && playerTarget.player() instanceof ServerPlayer serverPlayer) {
            if(serverPlayer.serverLevel().isNight()) {
                SMCAdvancementData.finishAdvancement("pre_cook", serverPlayer);
            }
            giveScoreEffect(serverPlayer, food.score);
        }
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {

        ItemStack itemstack = player.getItemInHand(hand);
        BlockHitResult blockhitresult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);
        if (blockhitresult.getType() != HitResult.Type.BLOCK) {
            return super.use(level, player, hand);
        } else {
            BlockPos blockpos = blockhitresult.getBlockPos();
            Direction direction = blockhitresult.getDirection();
            BlockPos blockPos1 = blockpos.relative(direction);
            BlockState blockState = level.getBlockState(blockpos);
            if (player.mayUseItemAt(blockPos1, direction, itemstack) && blockState.getBlock() instanceof LiquidBlock liquidBlock && liquidBlock.getFluid().isSame(Fluids.WATER)) {
                int count = player.getItemInHand(hand).getCount();
                player.setItemInHand(hand, ItemStack.EMPTY);
                ItemUtil.addItem(player, CDItems.PLATE.asStack(count));
                level.playSound(null, player.getX(), player.getY() + 0.75, player.getZ(), SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 4.0F, 1.5F);
                return InteractionResultHolder.success(itemstack);
            } else {
                return super.use(level, player, hand);
            }
        }
    }

    @Override
    public @NotNull InteractionResult useOn(@NotNull UseOnContext ctx) {

        Level level = ctx.getLevel();
        Player player = ctx.getPlayer();
        BlockEntity var5 = level.getBlockEntity(ctx.getClickedPos());
        if (var5 instanceof CuisineSkilletBlockEntity be) {
            if (be.cookingData.contents.isEmpty()) {
                return InteractionResult.PASS;
            } else {
                if (!level.isClientSide()) {
                    CookingData data = be.cookingData;
                    data.stir(level.getGameTime(), 0);
                    CookedFoodData food = new CookedFoodData(data);
                    ItemStack foodStack = BaseCuisineRecipe.findBestMatch(level, food);
                    ctx.getItemInHand().shrink(1);
                    if (player != null) {
                        this.giveBack(foodStack, food, new PlayerTarget(player));
                    } else {
                        this.giveBack(foodStack, food, new BlockTarget(ctx));
                    }
                    SMCAdvancementData.finishAdvancement("dirt_plate", ((ServerPlayer) player));
                    be.cookingData = new CookingData();
                    be.sync();
                }

                return InteractionResult.SUCCESS;
            }
        } else {
            return InteractionResult.PASS;
        }
    }

    @Override
    public void appendHoverText(@NotNull ItemStack itemStack, @Nullable Level level, @NotNull List<Component> list, @NotNull TooltipFlag flag) {
        super.appendHoverText(itemStack, level, list, flag);
        list.add(Component.translatable(this.getDescriptionId() + ".disc"));
    }
}

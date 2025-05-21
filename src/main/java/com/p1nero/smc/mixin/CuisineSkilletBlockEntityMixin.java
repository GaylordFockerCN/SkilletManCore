package com.p1nero.smc.mixin;

import com.mao.barbequesdelight.init.registrate.BBQDItems;
import com.p1nero.smc.block.custom.DiamondCuisineSkilletBlock;
import com.p1nero.smc.block.custom.GoldenCuisineSkilletBlock;
import dev.xkmc.cuisinedelight.content.block.CuisineSkilletBlockEntity;
import dev.xkmc.cuisinedelight.content.logic.CookingData;
import dev.xkmc.cuisinedelight.content.logic.IngredientConfig;
import dev.xkmc.cuisinedelight.content.logic.transform.Stage;
import dev.xkmc.cuisinedelight.init.data.CDConfig;
import dev.xkmc.l2library.base.tile.BaseBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vectorwing.farmersdelight.common.registry.ModSounds;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Mixin(value = CuisineSkilletBlockEntity.class)
public abstract class CuisineSkilletBlockEntityMixin extends BaseBlockEntity {

    public CuisineSkilletBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Shadow(remap = false)
    public abstract boolean canCook();

    @Shadow(remap = false)
    @Nonnull
    public CookingData cookingData;

    @Shadow(remap = false)
    public ItemStack baseItem;

    @Shadow(remap = false)
    public abstract boolean slowCook();

    /**
     * 自动接收上方的物品
     */
    @Inject(method = "tick", at = @At("TAIL"), remap = false)
    private void smc$tick(Level pLevel, BlockPos pPos, BlockState pState, CallbackInfo ci) {
        for (ItemEntity itemEntity : smc$getItemsAtAndAbove(pLevel, pPos)) {
            ItemStack heldStack = itemEntity.getItem();
            IngredientConfig.IngredientEntry config = IngredientConfig.get().getEntry(heldStack);
            if (config != null) {
                if (!this.canCook()) {
                    return;
                }

                if (this.cookingData.contents.size() >= CDConfig.COMMON.maxIngredient.get()) {
                    return;
                }

                if (!pLevel.isClientSide) {
                    int count = 1 + this.baseItem.getEnchantmentLevel(Enchantments.BLOCK_EFFICIENCY);
                    if (this.slowCook()) {
                        this.cookingData.setSpeed(0.5F);
                    }
                    ItemStack add = heldStack.split(count);
                    this.cookingData.addItem(add, pLevel.getGameTime());
                    ItemStack remain = add.getCraftingRemainingItem();
                    remain.setCount(add.getCount());
                    itemEntity.setItem(remain);
                    if (remain.isEmpty()) {
                        itemEntity.discard();
                    }
                    this.sync();
                    pLevel.playSound(null, pPos.getX(), pPos.getY(), pPos.getZ(), ModSounds.BLOCK_SKILLET_ADD_FOOD.get(), SoundSource.BLOCKS, 1.0F, pLevel.random.nextFloat() * 0.2F + 0.9F);
                }
            }
        }

        //碳化
        if(level != null && !level.isClientSide && !this.cookingData.contents.isEmpty()) {
            float becomeTimer = 400;
            if(pState.getBlock() instanceof GoldenCuisineSkilletBlock){
                becomeTimer = becomeTimer * 3 / 2;
            }
            if(pState.getBlock() instanceof DiamondCuisineSkilletBlock) {
                becomeTimer *= 3;
            }
            AtomicBoolean becomeCoal = new AtomicBoolean(true);
            float finalBecomeTimer = becomeTimer;
            this.cookingData.contents.forEach(cookingEntry -> {
                IngredientConfig.IngredientEntry config = IngredientConfig.get().getEntry(cookingEntry.getItem());
                assert config != null;
                if(level.getGameTime() - ((CookingEntryAccessor)cookingEntry).getStartTime() < finalBecomeTimer) {
                    becomeCoal.set(false);
                }
            });
            if(becomeCoal.get()) {
                int count = 1 + this.cookingData.contents.size() / 3;
                this.cookingData = new CookingData();
                this.sync();
                Vec3 center = pPos.getCenter();
                ItemEntity itemEntity = new ItemEntity(level, center.x, center.y, center.z, BBQDItems.BURNT_FOOD.asStack(count));
                level.addFreshEntity(itemEntity);
            }
        }

    }

    @Unique
    private static final VoxelShape smc$ALL_SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 8.0, 16.0);

    @Unique
    private static List<ItemEntity> smc$getItemsAtAndAbove(Level level, BlockPos pos) {
        return smc$ALL_SHAPE.toAabbs().stream().flatMap((aabb) -> level.getEntitiesOfClass(ItemEntity.class, aabb.move(pos.getX(), pos.getY(), pos.getZ()), EntitySelector.ENTITY_STILL_ALIVE).stream()).collect(Collectors.toList());
    }
}

package com.p1nero.smc.item.custom;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.p1nero.smc.SMCConfig;
import com.p1nero.smc.archive.DataManager;
import com.p1nero.smc.capability.SMCCapabilityProvider;
import com.p1nero.smc.capability.SMCPlayer;
import com.p1nero.smc.client.sound.SMCSounds;
import dev.xkmc.cuisinedelight.content.block.CuisineSkilletBlockEntity;
import dev.xkmc.cuisinedelight.content.item.CuisineSkilletItem;
import dev.xkmc.cuisinedelight.content.item.SpatulaItem;
import dev.xkmc.cuisinedelight.init.registrate.CDItems;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Unique;
import vectorwing.farmersdelight.common.registry.ModSounds;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;

import java.util.Objects;

public class SMCSpatulaItem extends SpatulaItem {

    private final Multimap<Attribute, AttributeModifier> toolAttributes;
    protected int cooldown = 20;

    public SMCSpatulaItem(Properties pProperties) {
        super(pProperties);
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier",0, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier", 0, AttributeModifier.Operation.ADDITION));
        toolAttributes = builder.build();
    }

    @Override
    public void onCraftedBy(@NotNull ItemStack itemStack, @NotNull Level level, @NotNull Player player) {
        super.onCraftedBy(itemStack, level, player);
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_LEVELUP, SoundSource.BLOCKS, 1.0F, 1.0F);
    }

    /**
     * 为了让它显示
     */
    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        return slot == EquipmentSlot.MAINHAND ? toolAttributes : super.getAttributeModifiers(slot, stack);
    }

    private static int getReduction(ItemStack stack) {
        return stack.getEnchantmentLevel(Enchantments.SILK_TOUCH) > 0 ? 20 : 0;
    }

    @Override
    public @NotNull InteractionResult useOn(@NotNull UseOnContext ctx) {
        if(Objects.requireNonNull(EpicFightCapabilities.getEntityPatch(ctx.getPlayer(), PlayerPatch.class)).isBattleMode()) {
            return InteractionResult.PASS;
        }
        Level level = ctx.getLevel();
        Player player = ctx.getPlayer();
        BlockEntity var5 = level.getBlockEntity(ctx.getClickedPos());
        if (var5 instanceof CuisineSkilletBlockEntity be) {
            if (player != null && !level.isClientSide) {
                player.getCooldowns().addCooldown(this, cooldown);
            }
            if (!be.cookingData.contents.isEmpty() && checkInCorrectTime(player)) {
                if (!level.isClientSide()) {
                    be.stir(level.getGameTime(), getReduction(ctx.getItemInHand()));
                } else if (player != null) {
                    CuisineSkilletItem.playSound(player, level, ModSounds.BLOCK_SKILLET_SIZZLE.get());
                }
            }
        }

        return InteractionResult.SUCCESS;
    }

    public static boolean checkInCorrectTime(Player player) {
        if(!SMCConfig.HARD_COOK_MODE.get()) {
            return true;
        }
        SMCPlayer smcPlayer = SMCCapabilityProvider.getSMCPlayer(player);
        boolean toReturn = smcPlayer.inCorrectStirTime();
        if(toReturn) {
            if(!player.level().isClientSide) {
                long lastInteractTime = smcPlayer.getLastSpatulaInteractTime();
                if(player.level().getGameTime() - lastInteractTime > 100 && lastInteractTime != 0) {
                    DataManager.spatulaCombo.put(player, 0.0);
                } else {
                    DataManager.spatulaCombo.put(player, DataManager.spatulaCombo.get(player) + 1);
                }
                smcPlayer.setLastSpatulaInteractTime(player.level().getGameTime());
            }

        } else {
            if(!player.level().isClientSide) {
                DataManager.spatulaCombo.put(player, 0.0);
            }
        }
        return toReturn;
    }

}

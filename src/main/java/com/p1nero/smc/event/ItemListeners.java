package com.p1nero.smc.event;

import com.p1nero.smc.SkilletManCoreMod;
import com.p1nero.smc.item.SMCItems;
import dev.xkmc.cuisinedelight.content.item.CuisineSkilletItem;
import dev.xkmc.cuisinedelight.content.item.SpatulaItem;
import dev.xkmc.cuisinedelight.content.logic.IngredientConfig;
import dev.xkmc.cuisinedelight.init.data.LangData;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.ItemAttributeModifierEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Iterator;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = SkilletManCoreMod.MOD_ID)
public class ItemListeners {
    public static final UUID LEVEL_MODIFIER_UUID = UUID.fromString("d1d111cc-f45f-00ed-a66b-1919ac114514");
    @SubscribeEvent
    public static void onTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        if (!stack.isEmpty()) {
            if(stack.getItem() instanceof SpatulaItem || stack.getItem() instanceof CuisineSkilletItem) {
                event.getToolTip().add(1, SkilletManCoreMod.getInfo("two_craft_tip").withStyle(ChatFormatting.YELLOW));
            }
            if(stack.is(SMCItems.NO_BRAIN_VILLAGER_SPAWN_EGG.get())) {
                event.getToolTip().add(SkilletManCoreMod.getInfo("no_brain_villager_spawn_egg_tip").withStyle(ChatFormatting.GRAY));
            }
            IngredientConfig.IngredientEntry config = IngredientConfig.get().getEntry(stack);
            if (config != null) {
                event.getToolTip().add(config.type.get().withStyle(ChatFormatting.GOLD));
                event.getToolTip().add(LangData.INFO_SIZE.get(config.size));
                event.getToolTip().add(LangData.INFO_NUTRITION.get(config.nutrition));
                event.getToolTip().add(LangData.INFO_MIN_TIME.get(config.min_time / 20));
                event.getToolTip().add(LangData.INFO_MAX_TIME.get(config.max_time / 20));
                event.getToolTip().add(LangData.INFO_STIR_TIME.get(config.stir_time / 20));
                event.getToolTip().add(LangData.INFO_RAW_PENALTY.get(Math.round(config.raw_penalty * 100.0F)));
                event.getToolTip().add(LangData.INFO_OVERCOOK_PENALTY.get(Math.round(config.overcook_penalty * 100.0F)));

                MutableComponent mutablecomponent;
                MobEffect mobeffect;
                for(Iterator<IngredientConfig.EffectEntry> var3 = config.effects.iterator(); var3.hasNext(); event.getToolTip().add(mutablecomponent.withStyle(mobeffect.getCategory().getTooltipFormatting()))) {
                    IngredientConfig.EffectEntry e = var3.next();
                    MobEffectInstance mobeffectinstance = new MobEffectInstance(e.effect(), e.time());
                    mutablecomponent = Component.translatable(mobeffectinstance.getDescriptionId());
                    mobeffect = mobeffectinstance.getEffect();
                    if (mobeffectinstance.getDuration() > 20) {
                        mutablecomponent = Component.translatable("potion.withDuration", mutablecomponent, MobEffectUtil.formatDuration(mobeffectinstance, 1.0F));
                    }
                }
            }
        }
        if(stack.hasTag() && !stack.getAttributeModifiers(EquipmentSlot.MAINHAND).get(Attributes.ATTACK_DAMAGE).isEmpty()){
            int level = stack.getOrCreateTag().getInt(SkilletManCoreMod.WEAPON_LEVEL_KEY);
            event.getToolTip().add(1, SkilletManCoreMod.getInfo("weapon_level").append(String.valueOf(level)).withStyle(ChatFormatting.AQUA));
        }
    }

    @SubscribeEvent
    public static void onItemAttributeModifier(ItemAttributeModifierEvent event){
        ItemStack itemStack = event.getItemStack();
        if(itemStack.hasTag() && event.getSlotType() == EquipmentSlot.MAINHAND){
            int level = itemStack.getOrCreateTag().getInt(SkilletManCoreMod.WEAPON_LEVEL_KEY);
            if(level > 0){
                event.addModifier(Attributes.ATTACK_DAMAGE, new AttributeModifier(LEVEL_MODIFIER_UUID, "SMC Level", 0.8 * level, AttributeModifier.Operation.ADDITION));
            }
        }
    }

}

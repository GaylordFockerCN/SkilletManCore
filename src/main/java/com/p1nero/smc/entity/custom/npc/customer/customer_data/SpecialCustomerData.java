package com.p1nero.smc.entity.custom.npc.customer.customer_data;

import com.p1nero.smc.SkilletManCoreMod;
import com.p1nero.smc.capability.SMCCapabilityProvider;
import com.p1nero.smc.capability.SMCPlayer;
import com.p1nero.smc.client.gui.DialogueComponentBuilder;
import com.p1nero.smc.client.gui.TreeNode;
import com.p1nero.smc.client.gui.screen.LinkListStreamDialogueScreenBuilder;
import com.p1nero.smc.client.sound.SMCSounds;
import com.p1nero.smc.datagen.SMCAdvancementData;
import com.p1nero.smc.datagen.lang.SMCLangGenerator;
import com.p1nero.smc.entity.custom.npc.customer.Customer;
import dev.xkmc.cuisinedelight.content.item.BaseFoodItem;
import dev.xkmc.cuisinedelight.content.logic.CookedFoodData;
import dev.xkmc.cuisinedelight.content.logic.FoodType;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public abstract class SpecialCustomerData extends Customer.CustomerData {
    protected int smcId;
    protected final String answerPre = "special_customer_answer_";
    protected final String choicePre = "special_customer_choice_";

    protected final String nameTranslationKey;

    public SpecialCustomerData(int smcId) {
        this.smcId = smcId;
        this.nameTranslationKey = "special_customer_" + smcId;
    }

    @Override
    public void onInteract(ServerPlayer player, Customer self) {
    }

    @Override
    public void generateTranslation(SMCLangGenerator generator) {
    }

    @Override
    public Component getTranslation() {
        return Component.translatable(this.nameTranslationKey);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void getDialogScreen(CompoundTag serverData, LinkListStreamDialogueScreenBuilder screenBuilder, DialogueComponentBuilder dialogueComponentBuilder, boolean canSubmit, int foodScore) {
        TreeNode root = new TreeNode(answer(-1));
        this.append(root, serverData, dialogueComponentBuilder, canSubmit, foodScore);
        screenBuilder.setAnswerRoot(root);
    }

    protected String answerPre(int id) {
        return answerPre + smcId + "_" + id;
    }

    protected String choicePre(int id) {
        return choicePre + smcId + "_" + id;
    }

    protected Component answer(int id, Object... objects) {
        Component component = Component.translatable(answerPre(id), objects);
        return Component.literal("\n").append(component);
    }

    protected Component choice(int id, Object... objects) {
        return Component.translatable(choicePre(id), objects);
    }

    protected void append(TreeNode root, CompoundTag serverData, DialogueComponentBuilder dialogueComponentBuilder, boolean canSubmit, int foodScore) {
        String foodName = "§6" + I18n.get(serverData.getString("food_name")) + "§r";
        if (!canSubmit) {
            root.addChild(new TreeNode(answer(0, foodName), choice(-1))
                    .addChild(new TreeNode(answer(-2), choice(0))
                            .addLeaf(choice(-2), (byte) -3))
                    .addLeaf(choice(-3), (byte) -3)
            );
        } else {
            switch (foodScore) {
                case BEST:
                    root.addChild(new TreeNode(answer(0, foodName), choice(-1))
                            .addChild(new TreeNode(answer(1), choice(0))
                                    .addExecutable(SUBMIT_FOOD)
                                    .addLeaf(choice(1), BEST))
                            .addLeaf(choice(-3), (byte) -3));
                    break;
                case MIDDLE:
                    root.addChild(new TreeNode(answer(0, foodName), choice(-1))
                            .addChild(new TreeNode(answer(2), choice(0))
                                    .addExecutable(SUBMIT_FOOD)
                                    .addLeaf(choice(2), MIDDLE))
                            .addLeaf(choice(-3), (byte) -3));
                    break;
                default:
                    root.addChild(new TreeNode(answer(0, foodName), choice(-1))
                            .addChild(new TreeNode(answer(3), choice(0))
                                    .addExecutable(SUBMIT_FOOD)
                                    .addLeaf(choice(3), BAD))
                            .addLeaf(choice(-3), (byte) -3));
            }
        }
    }

    @Override
    public void handle(ServerPlayer serverPlayer, Customer self, byte interactId) {
        if(interactId != 0){
            serverPlayer.displayClientMessage(SkilletManCoreMod.getInfo("special_customer"), false);
        }
        switch (interactId) {
            case BEST:
                onBest(serverPlayer, self);
                break;
            case MIDDLE:
                onMiddle(serverPlayer, self);
                break;
            case BAD:
                onBad(serverPlayer, self);
        }
    }

    protected void onBest(ServerPlayer serverPlayer, Customer self) {
        CookedFoodData cookedFoodData = BaseFoodItem.getData(self.getOrder());
        SMCPlayer smcPlayer = SMCCapabilityProvider.getSMCPlayer(serverPlayer);
        smcPlayer.increaseSpecialCustomerMet();
        if(smcPlayer.getSpecialCustomerMet() >= 50) {
            SMCAdvancementData.finishAdvancement("special_customer_1", serverPlayer);
        } else if(smcPlayer.getSpecialCustomerMet() >= 20) {
            SMCAdvancementData.finishAdvancement("special_customer_2", serverPlayer);
        } else if(smcPlayer.getSpecialCustomerMet() >= 10) {
            SMCAdvancementData.finishAdvancement("special_customer_3", serverPlayer);
        }


        float mul = 1.0F + smcPlayer.getLevel();
        serverPlayer.displayClientMessage(SkilletManCoreMod.getInfo("level_mul", smcPlayer.getLevel() + 1), false);
        if (cookedFoodData != null) {
            mul *= cookedFoodData.types.size();
            serverPlayer.displayClientMessage(SkilletManCoreMod.getInfo("type_mul", cookedFoodData.types.size()), false);
            if (cookedFoodData.types.contains(FoodType.MEAT)) {
                mul *= 2.0F;
                serverPlayer.displayClientMessage(SkilletManCoreMod.getInfo("meat_mul", 2.0F), false);
            }
            if (cookedFoodData.types.contains(FoodType.SEAFOOD)) {
                mul *= 5.0F;
                serverPlayer.displayClientMessage(SkilletManCoreMod.getInfo("seafood_mul", 5.0F), false);
            }
            if(cookedFoodData.size > 0){
                mul *= cookedFoodData.size;
                serverPlayer.displayClientMessage(SkilletManCoreMod.getInfo("size_mul", cookedFoodData.size), false);
            }
        }
        SMCPlayer.addMoney(((int) (100 * mul)), serverPlayer);
        serverPlayer.serverLevel().playSound(null, serverPlayer.getX(), serverPlayer.getY(), serverPlayer.getZ(), SMCSounds.VILLAGER_YES.get(), serverPlayer.getSoundSource(), 1.0F, 1.0F);
        self.level().broadcastEntityEvent(self, (byte) 14);//播放开心的粒子
    }


    protected void onMiddle(ServerPlayer serverPlayer, Customer self) {
        CookedFoodData cookedFoodData = BaseFoodItem.getData(self.getOrder());
        SMCPlayer smcPlayer = SMCCapabilityProvider.getSMCPlayer(serverPlayer);
        float mul = 1.0F + smcPlayer.getLevel();
        serverPlayer.displayClientMessage(SkilletManCoreMod.getInfo("level_mul", smcPlayer.getLevel() + 1), false);
        if (cookedFoodData != null) {
            mul += cookedFoodData.types.size();
            serverPlayer.displayClientMessage(SkilletManCoreMod.getInfo("type_mul", cookedFoodData.types.size()), false);
            if (cookedFoodData.types.contains(FoodType.MEAT)) {
                mul += 1.2F;
                serverPlayer.displayClientMessage(SkilletManCoreMod.getInfo("meat_mul", 1.2F), false);
            }
            if (cookedFoodData.types.contains(FoodType.SEAFOOD)) {
                mul += 1.4F;
                serverPlayer.displayClientMessage(SkilletManCoreMod.getInfo("seafood_mul", 1.4F), false);
            }
            if(cookedFoodData.size > 0){
                mul *= cookedFoodData.size;
                serverPlayer.displayClientMessage(SkilletManCoreMod.getInfo("size_mul", cookedFoodData.size), false);
            }
        }
        SMCPlayer.addMoney((int) (50 * mul), serverPlayer);
        serverPlayer.serverLevel().playSound(null, serverPlayer.getX(), serverPlayer.getY(), serverPlayer.getZ(), SoundEvents.VILLAGER_TRADE, serverPlayer.getSoundSource(), 1.0F, 1.0F);
    }


    protected void onBad(ServerPlayer serverPlayer, Customer self) {
        SMCPlayer.consumeMoney(400, serverPlayer);
        serverPlayer.serverLevel().playSound(null, serverPlayer.getX(), serverPlayer.getY(), serverPlayer.getZ(), SoundEvents.VILLAGER_NO, serverPlayer.getSoundSource(), 1.0F, 1.0F);
        self.setUnhappyCounter(40);
    }

}

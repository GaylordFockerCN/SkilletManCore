package com.p1nero.smc.entity.custom.npc.customer.customer_data;

import com.p1nero.smc.capability.SMCPlayer;
import com.p1nero.smc.client.gui.DialogueComponentBuilder;
import com.p1nero.smc.client.gui.TreeNode;
import com.p1nero.smc.client.gui.screen.LinkListStreamDialogueScreenBuilder;
import com.p1nero.smc.client.sound.SMCSounds;
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

public abstract class NormalCustomerData extends Customer.CustomerData {
    protected int smcId;
    protected final String answerPre = "normal_customer_answer_";
    protected final String choicePre = "normal_customer_choice_";

    protected final String nameTranslationKey;

    public NormalCustomerData(int smcId){
        this.smcId = smcId;
        this.nameTranslationKey = "normal_customer_" + smcId;
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

    @Override
    public void getDialogScreen(CompoundTag serverData, LinkListStreamDialogueScreenBuilder screenBuilder, DialogueComponentBuilder dialogueComponentBuilder, boolean canSubmit, int foodLevel) {
        TreeNode root;

        String foodName = "§6" + I18n.get(serverData.getString("food_name")) + "§r";

        if (!canSubmit) {
            root = new TreeNode(answer(0, foodName))
                    .addChild(new TreeNode(answer(-1), choice(0))
                            .addLeaf(choice(-1), (byte) -3))
                    .addLeaf(choice(-2), (byte) -3);
        } else {
            root = switch (foodLevel) {
                case BEST -> new TreeNode(answer(0, foodName))
                        .addChild(new TreeNode(answer(1), choice(0))
                                .execute(SUBMIT_FOOD)
                                .addLeaf(choice(1), BEST))
                        .addLeaf(choice(-2), (byte) -3);
                case MIDDLE -> new TreeNode(answer(0, foodName))
                        .addChild(new TreeNode(answer(2), choice(0))
                                .execute(SUBMIT_FOOD)
                                .addLeaf(choice(2), MIDDLE))
                        .addLeaf(choice(-2), (byte) -3);
                default -> new TreeNode(answer(0, foodName))
                        .addChild(new TreeNode(answer(3), choice(0))
                                .execute(SUBMIT_FOOD)
                                .addLeaf(choice(3), BAD))
                        .addLeaf(choice(-2), (byte) -3);
            };
        }
        screenBuilder.setAnswerRoot(root);
    }

    @Override
    public void handle(ServerPlayer serverPlayer, Customer self, byte interactId) {
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

    protected void onBest(ServerPlayer serverPlayer, Customer self){
        CookedFoodData cookedFoodData = BaseFoodItem.getData(self.getOrder());
        float mul = 1.0F;
        if(cookedFoodData != null) {
            mul *= cookedFoodData.types.size();
            if(cookedFoodData.types.contains(FoodType.MEAT)) {
                mul *= 2.0F;
            }
            if(cookedFoodData.types.contains(FoodType.SEAFOOD)) {
                mul *= 5.0F;
            }
        }
        SMCPlayer.addMoney((int) (50 * mul), serverPlayer);
        serverPlayer.serverLevel().playSound(null, serverPlayer.getX(), serverPlayer.getY(), serverPlayer.getZ(), SMCSounds.VILLAGER_YES.get(), serverPlayer.getSoundSource(), 1.0F, 1.0F);
        self.level().broadcastEntityEvent(self, (byte)14);//播放开心的粒子
    }


    protected void onMiddle(ServerPlayer serverPlayer, Customer self){
        SMCPlayer.addMoney(20, serverPlayer);
        serverPlayer.serverLevel().playSound(null, serverPlayer.getX(), serverPlayer.getY(), serverPlayer.getZ(), SoundEvents.VILLAGER_TRADE, serverPlayer.getSoundSource(), 1.0F, 1.0F);
    }


    protected void onBad(ServerPlayer serverPlayer, Customer self){
        SMCPlayer.consumeMoney(20, serverPlayer);
        serverPlayer.serverLevel().playSound(null, serverPlayer.getX(), serverPlayer.getY(), serverPlayer.getZ(), SoundEvents.VILLAGER_NO, serverPlayer.getSoundSource(), 1.0F, 1.0F);
        self.setUnhappyCounter(40);
    }
}

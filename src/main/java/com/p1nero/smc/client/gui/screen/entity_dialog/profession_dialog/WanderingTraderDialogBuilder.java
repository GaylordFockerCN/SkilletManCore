package com.p1nero.smc.client.gui.screen.entity_dialog.profession_dialog;

import com.p1nero.smc.capability.SMCCapabilityProvider;
import com.p1nero.smc.capability.SMCPlayer;
import com.p1nero.smc.client.gui.TreeNode;
import com.p1nero.smc.client.gui.screen.LinkListStreamDialogueScreenBuilder;
import com.p1nero.smc.client.gui.screen.entity_dialog.VillagerDialogScreenHandler;
import com.p1nero.smc.datagen.lang.SMCLangGenerator;
import com.p1nero.smc.util.SMCRaidManager;
import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class WanderingTraderDialogBuilder extends VillagerDialogScreenHandler.VillagerDialogBuilder {
    private static final WanderingTraderDialogBuilder INSTANCE = new WanderingTraderDialogBuilder();
    public static WanderingTraderDialogBuilder getInstance() {
        return INSTANCE;
    }

    public WanderingTraderDialogBuilder() {
        super(VillagerProfession.NONE);
    }

    @Override
    protected String getTranslationKey() {
        return "wandering_trader";
    }

    @Override
    public void onGenerateLang(SMCLangGenerator generator) {
        generator.addVillagerName(this.getTranslationKey(), " §b流浪商人§r ");
        generator.addVillagerAns(this.getTranslationKey(), 0, "（流浪商人总是时不时蹦出来，并且卖一些没用的东西，你已经忍不住把它杀了取缰绳了）");
        generator.addVillagerOpt(this.getTranslationKey(), 0, "杀之");
        generator.addVillagerOpt(this.getTranslationKey(), 1, "劫而杀之");
        generator.addVillagerOpt(this.getTranslationKey(), 2, "放他一马");
        generator.addVillagerAns(this.getTranslationKey(), 1, "（你崇高的道德在阻止你做这件事，你确定要对抗你的道德吗？）");
        generator.addVillagerOpt(this.getTranslationKey(), 3, "确定");
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void createDialog(LinkListStreamDialogueScreenBuilder builder, Villager self) {
    }

    public void handle(ServerPlayer serverPlayer, WanderingTrader self, byte interactId){
        SMCPlayer smcPlayer = SMCCapabilityProvider.getSMCPlayer(serverPlayer);
        if(interactId == 1) {
            self.setHealth(0);
            smcPlayer.consumeMorality();
            self.playSound(SoundEvents.WANDERING_TRADER_DEATH);
            SMCRaidManager.startRandomRaid(serverPlayer);
        }
        if(interactId == 2) {
            SMCPlayer.addMoney((int) (200 * smcPlayer.getLevelMoneyRate()), serverPlayer);
            self.setHealth(0);
            smcPlayer.consumeMorality();
            smcPlayer.consumeMorality();
            self.playSound(SoundEvents.WANDERING_TRADER_DEATH);
            SMCRaidManager.startRandomRaid(serverPlayer);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void buildDialog(WanderingTrader wanderingTrader) {
        LinkListStreamDialogueScreenBuilder screenBuilder = new LinkListStreamDialogueScreenBuilder(wanderingTrader, getName());
        screenBuilder.setAnswerRoot(new TreeNode(answer(0))
                .addChild(new TreeNode(answer(1), choice(0))
                        .addLeaf(choice(3), (byte) 1)
                        .addLeaf(choice(2)))
                .addChild(new TreeNode(answer(1), choice(1))
                        .addLeaf(choice(3), (byte) 2)
                        .addLeaf(choice(2)))
                .addLeaf(choice(2)));

        Minecraft.getInstance().setScreen(screenBuilder.build());
    }
}

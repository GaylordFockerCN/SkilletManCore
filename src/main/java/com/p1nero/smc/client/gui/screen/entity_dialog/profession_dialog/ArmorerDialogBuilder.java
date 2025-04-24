package com.p1nero.smc.client.gui.screen.entity_dialog.profession_dialog;

import com.p1nero.smc.capability.SMCCapabilityProvider;
import com.p1nero.smc.capability.SMCPlayer;
import com.p1nero.smc.client.gui.TreeNode;
import com.p1nero.smc.client.gui.screen.LinkListStreamDialogueScreenBuilder;
import com.p1nero.smc.client.gui.screen.entity_dialog.VillagerDialogScreenHandler;
import com.p1nero.smc.client.sound.SMCSounds;
import com.p1nero.smc.datagen.lang.SMCLangGenerator;
import com.p1nero.smc.registrate.SMCRegistrateItems;
import com.p1nero.smc.util.ItemUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * 盔甲抽卡
 */
public class ArmorerDialogBuilder extends VillagerDialogScreenHandler.VillagerDialogBuilder {
    public ArmorerDialogBuilder() {
        super(VillagerProfession.ARMORER);
    }

    @Override
    public void handle(ServerPlayer serverPlayer, Villager villager, byte interactionID) {
        super.handle(serverPlayer, villager, interactionID);
        SMCPlayer smcPlayer = SMCCapabilityProvider.getSMCPlayer(serverPlayer);
        if (interactionID == 1) {
            int ticketCount = ItemUtil.searchAndConsumeItem(serverPlayer, SMCRegistrateItems.ARMOR_RAFFLE_TICKET.asItem(), 1);
            if (ticketCount == 0) {
                if(SMCPlayer.hasMoney(serverPlayer, 160, true)) {
                    SMCPlayer.consumeMoney(160, serverPlayer);
                    smcPlayer.setArmorGachaingCount(1);
                }
            } else {
                serverPlayer.serverLevel().playSound(null, serverPlayer.getX(), serverPlayer.getY(), serverPlayer.getZ(), SMCSounds.VILLAGER_YES.get(), serverPlayer.getSoundSource(), 1.0F, 1.0F);
                smcPlayer.setArmorGachaingCount(1);
            }
        }
        if (interactionID == 2) {
            int ticketCount = ItemUtil.searchAndConsumeItem(serverPlayer, SMCRegistrateItems.ARMOR_RAFFLE_TICKET.asItem(), 10);
            if(ticketCount < 10) {
                int need = 10 - ticketCount;
                if(SMCPlayer.hasMoney(serverPlayer, 160 * need, true)) {
                    SMCPlayer.consumeMoney(160 * need, serverPlayer);
                    smcPlayer.setArmorGachaingCount(10);
                }
            } else {
                serverPlayer.serverLevel().playSound(null, serverPlayer.getX(), serverPlayer.getY(), serverPlayer.getZ(), SMCSounds.VILLAGER_YES.get(), serverPlayer.getSoundSource(), 1.0F, 1.0F);
                smcPlayer.setArmorGachaingCount(10);
            }
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void createDialog(LinkListStreamDialogueScreenBuilder builder, Villager self) {
        LocalPlayer localPlayer = Minecraft.getInstance().player;
        if (localPlayer != null) {
            int ticketCount = localPlayer.getInventory().countItem(SMCRegistrateItems.ARMOR_RAFFLE_TICKET.asItem());

            TreeNode pull = new TreeNode(answer(1), choice(0));

            if (ticketCount < 1) {
                pull.addChild(new TreeNode(answer(3, 160), choice(2))
                                .addLeaf(choice(4), (byte) 1)
                                .addLeaf(choice(5))
                        .addChild(new TreeNode(answer(3, 1600), choice(3))
                                .addLeaf(choice(4), (byte) 2)
                                .addLeaf(choice(5))));
            } else if (ticketCount < 10) {
                int needTicket = 10 - ticketCount;
                pull.addLeaf(choice(2), (byte) 1)
                        .addChild(new TreeNode(answer(4, 160 * needTicket), choice(3))
                                .addLeaf(choice(4), (byte) 2)
                                .addLeaf(choice(5)));
            } else {
                pull.addLeaf(choice(2), (byte) 1);
                pull.addLeaf(choice(3), (byte) 2);
            }

            builder.setAnswerRoot(new TreeNode(answer(0))
                    .addChild(pull)
                    .addLeaf(choice(1)));
        }
    }

    @Override
    public void onGenerateLang(SMCLangGenerator generator) {
        generator.addVillagerName(this.profession, " §e传奇的盔甲匠§r ");
        generator.addVillagerAns(this.profession, 0, "（这位盔甲匠脸上写满了传奇，他能给予你充满传奇故事的盔甲）");
        generator.addVillagerOpt(this.profession, 0, "盔甲祈愿");
        generator.addVillagerOpt(this.profession, 1, "离开");
        generator.addVillagerAns(this.profession, 1, "10次祈愿必得四星盔甲，90次祈愿必得五星盔甲，五星盔甲 §6[Spark of Dawn]§r 概率UP！");
        generator.addVillagerOpt(this.profession, 2, "祈愿一次");
        generator.addVillagerOpt(this.profession, 3, "祈愿十次");
        generator.addVillagerAns(this.profession, 2, "盔甲抽奖券不足，是否用 %d 绿宝石替代？");
        generator.addVillagerAns(this.profession, 3, "盔甲抽奖券不足，是否用 %d 绿宝石补全？");
        generator.addVillagerOpt(this.profession, 4, "确定");
        generator.addVillagerOpt(this.profession, 5, "取消");
    }


}

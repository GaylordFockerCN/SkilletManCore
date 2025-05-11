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
import net.kenddie.fantasyarmor.item.FAItems;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
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
                if (SMCPlayer.hasMoney(serverPlayer, 160, true)) {
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
            if (ticketCount < 10) {
                int need = 10 - ticketCount;
                if (SMCPlayer.hasMoney(serverPlayer, 160 * need, true)) {
                    SMCPlayer.consumeMoney(160 * need, serverPlayer);
                    smcPlayer.setArmorGachaingCount(10);
                }
            } else {
                serverPlayer.serverLevel().playSound(null, serverPlayer.getX(), serverPlayer.getY(), serverPlayer.getZ(), SMCSounds.VILLAGER_YES.get(), serverPlayer.getSoundSource(), 1.0F, 1.0F);
                smcPlayer.setArmorGachaingCount(10);
            }
        }
        if (interactionID == 3) {
            this.startTrade(serverPlayer, villager);
        }
    }

    public void startTrade(ServerPlayer serverPlayer, Villager villager) {
        villager.setTradingPlayer(serverPlayer);
        MerchantOffers merchantOffers = new MerchantOffers();

        merchantOffers.add(new MerchantOffer(
                new ItemStack(FAItems.HERO_HELMET.get(), 1),
                new ItemStack(FAItems.HERO_CHESTPLATE.get(), 1),
                142857, 0, 0));
        merchantOffers.add(new MerchantOffer(
                new ItemStack(FAItems.HERO_CHESTPLATE.get(), 1),
                new ItemStack(FAItems.HERO_LEGGINGS.get(), 1),
                142857, 0, 0));
        merchantOffers.add(new MerchantOffer(
                new ItemStack(FAItems.HERO_LEGGINGS.get(), 1),
                new ItemStack(FAItems.HERO_BOOTS.get(), 1),
                142857, 0, 0));
        merchantOffers.add(new MerchantOffer(
                new ItemStack(FAItems.HERO_BOOTS.get(), 1),
                new ItemStack(FAItems.HERO_HELMET.get(), 1),
                142857, 0, 0));

        merchantOffers.add(new MerchantOffer(
                new ItemStack(FAItems.DARK_COVER_HELMET.get(), 1),
                new ItemStack(FAItems.DARK_COVER_CHESTPLATE.get(), 1),
                142857, 0, 0));
        merchantOffers.add(new MerchantOffer(
                new ItemStack(FAItems.DARK_COVER_CHESTPLATE.get(), 1),
                new ItemStack(FAItems.DARK_COVER_LEGGINGS.get(), 1),
                142857, 0, 0));
        merchantOffers.add(new MerchantOffer(
                new ItemStack(FAItems.DARK_COVER_LEGGINGS.get(), 1),
                new ItemStack(FAItems.DARK_COVER_BOOTS.get(), 1),
                142857, 0, 0));
        merchantOffers.add(new MerchantOffer(
                new ItemStack(FAItems.DARK_COVER_BOOTS.get(), 1),
                new ItemStack(FAItems.DARK_COVER_HELMET.get(), 1),
                142857, 0, 0));

        merchantOffers.add(new MerchantOffer(
                new ItemStack(FAItems.SPARK_OF_DAWN_HELMET.get(), 1),
                new ItemStack(FAItems.SPARK_OF_DAWN_CHESTPLATE.get(), 1),
                142857, 0, 0));
        merchantOffers.add(new MerchantOffer(
                new ItemStack(FAItems.SPARK_OF_DAWN_CHESTPLATE.get(), 1),
                new ItemStack(FAItems.SPARK_OF_DAWN_LEGGINGS.get(), 1),
                142857, 0, 0));
        merchantOffers.add(new MerchantOffer(
                new ItemStack(FAItems.SPARK_OF_DAWN_LEGGINGS.get(), 1),
                new ItemStack(FAItems.SPARK_OF_DAWN_BOOTS.get(), 1),
                142857, 0, 0));
        merchantOffers.add(new MerchantOffer(
                new ItemStack(FAItems.SPARK_OF_DAWN_BOOTS.get(), 1),
                new ItemStack(FAItems.SPARK_OF_DAWN_HELMET.get(), 1),
                142857, 0, 0));

        merchantOffers.add(new MerchantOffer(
                new ItemStack(FAItems.GILDED_HUNT_HELMET.get(), 1),
                new ItemStack(FAItems.GILDED_HUNT_CHESTPLATE.get(), 1),
                142857, 0, 0));
        merchantOffers.add(new MerchantOffer(
                new ItemStack(FAItems.GILDED_HUNT_CHESTPLATE.get(), 1),
                new ItemStack(FAItems.GILDED_HUNT_LEGGINGS.get(), 1),
                142857, 0, 0));
        merchantOffers.add(new MerchantOffer(
                new ItemStack(FAItems.GILDED_HUNT_LEGGINGS.get(), 1),
                new ItemStack(FAItems.GILDED_HUNT_BOOTS.get(), 1),
                142857, 0, 0));
        merchantOffers.add(new MerchantOffer(
                new ItemStack(FAItems.GILDED_HUNT_BOOTS.get(), 1),
                new ItemStack(FAItems.GILDED_HUNT_HELMET.get(), 1),
                142857, 0, 0));

        merchantOffers.add(new MerchantOffer(
                new ItemStack(FAItems.LADY_MARIA_HELMET.get(), 1),
                new ItemStack(FAItems.LADY_MARIA_CHESTPLATE.get(), 1),
                142857, 0, 0));
        merchantOffers.add(new MerchantOffer(
                new ItemStack(FAItems.LADY_MARIA_CHESTPLATE.get(), 1),
                new ItemStack(FAItems.LADY_MARIA_HELMET.get(), 1),
                142857, 0, 0));
        merchantOffers.add(new MerchantOffer(
                new ItemStack(FAItems.LADY_MARIA_LEGGINGS.get(), 1),
                new ItemStack(FAItems.LADY_MARIA_BOOTS.get(), 1),
                142857, 0, 0));
        merchantOffers.add(new MerchantOffer(
                new ItemStack(FAItems.LADY_MARIA_BOOTS.get(), 1),
                new ItemStack(FAItems.LADY_MARIA_LEGGINGS.get(), 1),
                142857, 0, 0));

        merchantOffers.add(new MerchantOffer(
                new ItemStack(FAItems.GOLDEN_EXECUTION_HELMET.get(), 1),
                new ItemStack(FAItems.GOLDEN_EXECUTION_CHESTPLATE.get(), 1),
                142857, 0, 0));
        merchantOffers.add(new MerchantOffer(
                new ItemStack(FAItems.GOLDEN_EXECUTION_CHESTPLATE.get(), 1),
                new ItemStack(FAItems.GOLDEN_EXECUTION_HELMET.get(), 1),
                142857, 0, 0));
        merchantOffers.add(new MerchantOffer(
                new ItemStack(FAItems.GOLDEN_EXECUTION_LEGGINGS.get(), 1),
                new ItemStack(FAItems.GOLDEN_EXECUTION_BOOTS.get(), 1),
                142857, 0, 0));
        merchantOffers.add(new MerchantOffer(
                new ItemStack(FAItems.GOLDEN_EXECUTION_BOOTS.get(), 1),
                new ItemStack(FAItems.GOLDEN_EXECUTION_LEGGINGS.get(), 1),
                142857, 0, 0));

        merchantOffers.add(new MerchantOffer(
                new ItemStack(FAItems.ECLIPSE_SOLDIER_HELMET.get(), 1),
                new ItemStack(FAItems.ECLIPSE_SOLDIER_CHESTPLATE.get(), 1),
                142857, 0, 0));
        merchantOffers.add(new MerchantOffer(
                new ItemStack(FAItems.ECLIPSE_SOLDIER_CHESTPLATE.get(), 1),
                new ItemStack(FAItems.ECLIPSE_SOLDIER_HELMET.get(), 1),
                142857, 0, 0));
        merchantOffers.add(new MerchantOffer(
                new ItemStack(FAItems.ECLIPSE_SOLDIER_LEGGINGS.get(), 1),
                new ItemStack(FAItems.ECLIPSE_SOLDIER_BOOTS.get(), 1),
                142857, 0, 0));
        merchantOffers.add(new MerchantOffer(
                new ItemStack(FAItems.ECLIPSE_SOLDIER_BOOTS.get(), 1),
                new ItemStack(FAItems.ECLIPSE_SOLDIER_LEGGINGS.get(), 1),
                142857, 0, 0));

        merchantOffers.add(new MerchantOffer(
                new ItemStack(FAItems.DRAGONSLAYER_HELMET.get(), 1),
                new ItemStack(FAItems.DRAGONSLAYER_CHESTPLATE.get(), 1),
                142857, 0, 0));
        merchantOffers.add(new MerchantOffer(
                new ItemStack(FAItems.DRAGONSLAYER_CHESTPLATE.get(), 1),
                new ItemStack(FAItems.DRAGONSLAYER_HELMET.get(), 1),
                142857, 0, 0));
        merchantOffers.add(new MerchantOffer(
                new ItemStack(FAItems.DRAGONSLAYER_LEGGINGS.get(), 1),
                new ItemStack(FAItems.DRAGONSLAYER_BOOTS.get(), 1),
                142857, 0, 0));
        merchantOffers.add(new MerchantOffer(
                new ItemStack(FAItems.DRAGONSLAYER_BOOTS.get(), 1),
                new ItemStack(FAItems.DRAGONSLAYER_LEGGINGS.get(), 1),
                142857, 0, 0));

        merchantOffers.add(new MerchantOffer(
                new ItemStack(FAItems.CHESS_BOARD_KNIGHT_HELMET.get(), 1),
                new ItemStack(FAItems.CHESS_BOARD_KNIGHT_CHESTPLATE.get(), 1),
                142857, 0, 0));
        merchantOffers.add(new MerchantOffer(
                new ItemStack(FAItems.CHESS_BOARD_KNIGHT_CHESTPLATE.get(), 1),
                new ItemStack(FAItems.CHESS_BOARD_KNIGHT_HELMET.get(), 1),
                142857, 0, 0));
        merchantOffers.add(new MerchantOffer(
                new ItemStack(FAItems.CHESS_BOARD_KNIGHT_LEGGINGS.get(), 1),
                new ItemStack(FAItems.CHESS_BOARD_KNIGHT_BOOTS.get(), 1),
                142857, 0, 0));
        merchantOffers.add(new MerchantOffer(
                new ItemStack(FAItems.CHESS_BOARD_KNIGHT_BOOTS.get(), 1),
                new ItemStack(FAItems.CHESS_BOARD_KNIGHT_LEGGINGS.get(), 1),
                142857, 0, 0));

        merchantOffers.add(new MerchantOffer(
                new ItemStack(FAItems.SUNSET_WINGS_HELMET.get(), 1),
                new ItemStack(FAItems.SUNSET_WINGS_CHESTPLATE.get(), 1),
                142857, 0, 0));
        merchantOffers.add(new MerchantOffer(
                new ItemStack(FAItems.SUNSET_WINGS_CHESTPLATE.get(), 1),
                new ItemStack(FAItems.SUNSET_WINGS_HELMET.get(), 1),
                142857, 0, 0));
        merchantOffers.add(new MerchantOffer(
                new ItemStack(FAItems.SUNSET_WINGS_LEGGINGS.get(), 1),
                new ItemStack(FAItems.SUNSET_WINGS_BOOTS.get(), 1),
                142857, 0, 0));
        merchantOffers.add(new MerchantOffer(
                new ItemStack(FAItems.SUNSET_WINGS_BOOTS.get(), 1),
                new ItemStack(FAItems.SUNSET_WINGS_LEGGINGS.get(), 1),
                142857, 0, 0));
        villager.setOffers(merchantOffers);
        villager.openTradingScreen(serverPlayer, getName(), 5);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void createDialog(LinkListStreamDialogueScreenBuilder builder, Villager self) {
        LocalPlayer localPlayer = Minecraft.getInstance().player;
        if (localPlayer != null) {
            int ticketCount = localPlayer.getInventory().countItem(SMCRegistrateItems.ARMOR_RAFFLE_TICKET.asItem());

            TreeNode pull = new TreeNode(answer(1, FAItems.SPARK_OF_DAWN_HELMET.get().getDefaultInstance().getDisplayName().copy().withStyle(ChatFormatting.GOLD)), choice(0));

            if (ticketCount < 1) {
                pull.addChild(new TreeNode(answer(2, 160), choice(2))
                                .addLeaf(choice(4), (byte) 1)
                                .addLeaf(choice(5)))
                        .addChild(new TreeNode(answer(2, 1600), choice(3))
                                .addLeaf(choice(4), (byte) 2)
                                .addLeaf(choice(5)));
            } else if (ticketCount < 10) {
                int needTicket = 10 - ticketCount;
                pull.addLeaf(choice(2), (byte) 1)
                        .addChild(new TreeNode(answer(3, 160 * needTicket), choice(3))
                                .addLeaf(choice(4), (byte) 2)
                                .addLeaf(choice(5)));
            } else {
                pull.addLeaf(choice(2), (byte) 1);
                pull.addLeaf(choice(3), (byte) 2);
            }

            pull.addLeaf(choice(6), (byte) 3);

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
        generator.addVillagerAns(this.profession, 1, "10次祈愿必得四星盔甲，90次祈愿必得五星盔甲，重复的盔甲可以进行汰换。五星盔甲 %s 概率UP！");
        generator.addVillagerOpt(this.profession, 2, "祈愿一次");
        generator.addVillagerOpt(this.profession, 3, "祈愿十次");
        generator.addVillagerAns(this.profession, 2, "盔甲抽奖券不足，是否用 %d 绿宝石替代？");
        generator.addVillagerAns(this.profession, 3, "盔甲抽奖券不足，是否用 %d 绿宝石补全？");
        generator.addVillagerOpt(this.profession, 4, "确定");
        generator.addVillagerOpt(this.profession, 5, "取消");
        generator.addVillagerOpt(this.profession, 6, "§a盔甲汰换");
    }


}

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
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * 盔甲抽卡
 */
public class ToolSmithDialogBuilder extends VillagerDialogScreenHandler.VillagerDialogBuilder {
    public ToolSmithDialogBuilder() {
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
        villager.openTradingScreen(serverPlayer, answer(3), 1);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void createDialog(LinkListStreamDialogueScreenBuilder builder, Villager self) {
        LocalPlayer localPlayer = Minecraft.getInstance().player;
        if (localPlayer != null) {
            SMCPlayer smcPlayer = SMCCapabilityProvider.getSMCPlayer(localPlayer);
            int playerLevel = smcPlayer.getLevel();

            TreeNode root = new TreeNode(answer(0))
                    .addChild(new TreeNode(answer(1), choice(0))
                            .addChild(new TreeNode(answer(2), choice(3))
                                    .addLeaf(choice(5), (byte) 1)//兑换原版通票 x 10
                                    .addLeaf(choice(6), (byte) 2)//兑换原版通票 x 100
                            )
                            .addChild(new TreeNode(answer(2), choice(4)))
                                    .addLeaf(choice(5), (byte) 3)//兑换机械通票 x 10
                                    .addLeaf(choice(6), (byte) 4)//兑换机械通票 x 100
                            );

            MutableComponent opt5 = choice(5).copy();
            MutableComponent opt6 = choice(6).copy();
            if(playerLevel < 6) {
                Style style = opt5.getStyle();
                opt5.setStyle(style.applyFormat(ChatFormatting.RED).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, choice(8))));
                Style style2 = opt6.getStyle();
                opt6.setStyle(style2.applyFormat(ChatFormatting.RED).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, choice(8))));
                root.addLeaf(opt5);
                root.addLeaf(opt6);
            } else {
                root.addLeaf(opt5, (byte) 5);//打开原版交易表
                root.addLeaf(opt6, (byte) 6);//打开机械动力交易表
            }

            builder.setAnswerRoot(new TreeNode(answer(0))
                    .addLeaf(choice(1)));
        }
    }

    @Override
    public void onGenerateLang(SMCLangGenerator generator) {
        generator.addVillagerName(this.profession, " §e传奇的机械师§r ");
        generator.addVillagerAns(this.profession, 0, "（这位机械师脸上写满了传奇，他能给予你妙妙机械）年轻人，什么时候开始学习机械动力都不算晚！要学会解放双手，自动化炒菜！");
        generator.addVillagerOpt(this.profession, 0, "兑换通票");
        generator.addVillagerAns(this.profession, 1, "要兑换哪种通票呢？");
        generator.addVillagerOpt(this.profession, 1, "兑换原版材料");
        generator.addVillagerOpt(this.profession, 2, "兑换机械动力材料");
        generator.addVillagerOpt(this.profession, 3, "兑换原版材料通票");
        generator.addVillagerOpt(this.profession, 4, "兑换机械动力材料通票");
        generator.addVillagerAns(this.profession, 2, "要兑换多少呢？");
        generator.addVillagerOpt(this.profession, 5, "兑换 10 张 §a16000绿宝石");
        generator.addVillagerOpt(this.profession, 6, "兑换 100 张 §a160000绿宝石");
        generator.addVillagerOpt(this.profession, 7, "离开");
        generator.addVillagerOpt(this.profession, 8, "§a声望等级达到6级解锁");
        generator.addVillagerAns(this.profession, 3, "§6货物将随声望等级提高而增多！");
    }


}

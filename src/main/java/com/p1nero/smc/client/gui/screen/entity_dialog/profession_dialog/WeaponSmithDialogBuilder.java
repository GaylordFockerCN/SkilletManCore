package com.p1nero.smc.client.gui.screen.entity_dialog.profession_dialog;

import com.p1nero.smc.SkilletManCoreMod;
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
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import reascer.wom.world.item.WOMItems;

/**
 * 武器抽卡
 */
public class WeaponSmithDialogBuilder extends VillagerDialogScreenHandler.VillagerDialogBuilder {
    public WeaponSmithDialogBuilder() {
        super(VillagerProfession.WEAPONSMITH);
    }

    @Override
    public void handle(ServerPlayer serverPlayer, Villager villager, byte interactionID) {
        super.handle(serverPlayer, villager, interactionID);
        SMCPlayer smcPlayer = SMCCapabilityProvider.getSMCPlayer(serverPlayer);
        if (interactionID == 1) {
            int ticketCount = ItemUtil.searchAndConsumeItem(serverPlayer, SMCRegistrateItems.WEAPON_RAFFLE_TICKET.asItem(), 1);
            if (ticketCount == 0) {
                if (SMCPlayer.hasMoney(serverPlayer, 160, true)) {
                    SMCPlayer.consumeMoney(160, serverPlayer);
                    smcPlayer.setWeaponGachaingCount(1);
                }
            } else {
                serverPlayer.serverLevel().playSound(null, serverPlayer.getX(), serverPlayer.getY(), serverPlayer.getZ(), SMCSounds.VILLAGER_YES.get(), serverPlayer.getSoundSource(), 1.0F, 1.0F);
                smcPlayer.setWeaponGachaingCount(1);
            }
        }
        if (interactionID == 2) {
            int ticketCount = ItemUtil.searchAndConsumeItem(serverPlayer, SMCRegistrateItems.WEAPON_RAFFLE_TICKET.asItem(), 10);
            if (ticketCount < 10) {
                int need = 10 - ticketCount;
                if (SMCPlayer.hasMoney(serverPlayer, 160 * need, true)) {
                    SMCPlayer.consumeMoney(160 * need, serverPlayer);
                    smcPlayer.setWeaponGachaingCount(10);
                }
            } else {
                serverPlayer.serverLevel().playSound(null, serverPlayer.getX(), serverPlayer.getY(), serverPlayer.getZ(), SMCSounds.VILLAGER_YES.get(), serverPlayer.getSoundSource(), 1.0F, 1.0F);
                smcPlayer.setWeaponGachaingCount(10);
            }
        }

        if (interactionID == 3) {
            ItemStack itemStack = serverPlayer.getMainHandItem();
            int level = 0;
            if (itemStack.hasTag()) {
                level = itemStack.getOrCreateTag().getInt(SkilletManCoreMod.WEAPON_LEVEL_KEY);
            }
            int need = (int) (Math.pow(1.145, (level)) * 1600);
            if (SMCPlayer.hasMoney(serverPlayer, need, true)) {
                SMCPlayer.consumeMoney(need, serverPlayer);
                itemStack.getOrCreateTag().putInt(SkilletManCoreMod.WEAPON_LEVEL_KEY, level + 1);
            }
        }

        if(interactionID == 4){
            this.startTrade(serverPlayer, villager);
        }

    }

    public void startTrade(ServerPlayer serverPlayer, Villager villager) {
        villager.setTradingPlayer(serverPlayer);
        MerchantOffers merchantOffers = new MerchantOffers();

        merchantOffers.add(new MerchantOffer(
                new ItemStack(SMCRegistrateItems.WEAPON_RAFFLE_TICKET, 35),
                new ItemStack(SMCRegistrateItems.WEAPON_RAFFLE_TICKET, 35),
                new ItemStack(WOMItems.AGONY.get(), 1),
                142857, 0, 0));
        merchantOffers.add(new MerchantOffer(
                new ItemStack(SMCRegistrateItems.WEAPON_RAFFLE_TICKET, 35),
                new ItemStack(SMCRegistrateItems.WEAPON_RAFFLE_TICKET, 35),
                new ItemStack(WOMItems.MOONLESS.get(), 1),
                142857, 0, 0));
        merchantOffers.add(new MerchantOffer(
                new ItemStack(SMCRegistrateItems.WEAPON_RAFFLE_TICKET, 35),
                new ItemStack(SMCRegistrateItems.WEAPON_RAFFLE_TICKET, 35),
                new ItemStack(WOMItems.SATSUJIN.get(), 1),
                142857, 0, 0));
        merchantOffers.add(new MerchantOffer(
                new ItemStack(SMCRegistrateItems.WEAPON_RAFFLE_TICKET, 35),
                new ItemStack(SMCRegistrateItems.WEAPON_RAFFLE_TICKET, 35),
                new ItemStack(WOMItems.SOLAR.get(), 1),
                142857, 0, 0));
        merchantOffers.add(new MerchantOffer(
                new ItemStack(SMCRegistrateItems.WEAPON_RAFFLE_TICKET, 35),
                new ItemStack(SMCRegistrateItems.WEAPON_RAFFLE_TICKET, 35),
                new ItemStack(WOMItems.RUINE.get(), 1),
                142857, 0, 0));
        merchantOffers.add(new MerchantOffer(
                new ItemStack(SMCRegistrateItems.WEAPON_RAFFLE_TICKET, 35),
                new ItemStack(SMCRegistrateItems.WEAPON_RAFFLE_TICKET, 35),
                new ItemStack(WOMItems.TORMENTED_MIND.get(), 1),
                142857, 0, 0));
        merchantOffers.add(new MerchantOffer(
                new ItemStack(SMCRegistrateItems.WEAPON_RAFFLE_TICKET, 35),
                new ItemStack(SMCRegistrateItems.WEAPON_RAFFLE_TICKET, 35),
                new ItemStack(WOMItems.ANTITHEUS.get(), 1),
                142857, 0, 0));
        merchantOffers.add(new MerchantOffer(
                new ItemStack(SMCRegistrateItems.WEAPON_RAFFLE_TICKET, 40),
                new ItemStack(WOMItems.ENDER_BLASTER.get(), 1),
                142857, 0, 0));
        merchantOffers.add(new MerchantOffer(
                new ItemStack(SMCRegistrateItems.WEAPON_RAFFLE_TICKET, 35),
                new ItemStack(WOMItems.HERRSCHER.get(), 1),
                142857, 0, 0));
        merchantOffers.add(new MerchantOffer(
                new ItemStack(SMCRegistrateItems.WEAPON_RAFFLE_TICKET, 35),
                new ItemStack(WOMItems.GESETZ.get(), 1),
                142857, 0, 0));

        villager.setOffers(merchantOffers);
        villager.openTradingScreen(serverPlayer, choice(8), 5);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void createDialog(LinkListStreamDialogueScreenBuilder builder, Villager self) {
        LocalPlayer localPlayer = Minecraft.getInstance().player;
        if (localPlayer != null) {
            int ticketCount = localPlayer.getInventory().countItem(SMCRegistrateItems.ARMOR_RAFFLE_TICKET.asItem());

            TreeNode pull = new TreeNode(answer(1), choice(0));

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

            ItemStack itemStack = localPlayer.getMainHandItem();
            int level = 0;
            if (itemStack.hasTag()) {
                level = itemStack.getOrCreateTag().getInt(SkilletManCoreMod.WEAPON_LEVEL_KEY);
            }
            TreeNode weaponUpdate = new TreeNode(answer(5, (int) (Math.pow(1.145, (level)) * 1600)), choice(6))
                    .addLeaf(choice(4), (byte) 3)
                    .addLeaf(choice(5));

            builder.setAnswerRoot(new TreeNode(answer(0))
                    .addChild(pull)
                    .addChild(weaponUpdate)
                    .addLeaf(choice(7), (byte) 4)
                    .addLeaf(choice(1)));
        }
    }

    @Override
    public void onGenerateLang(SMCLangGenerator generator) {
        generator.addVillagerName(this.profession, " §c魁梧的武器匠§r");
        generator.addVillagerAns(this.profession, 0, "（这位武器匠长得凶神恶煞，他可以为你锻造趁手的武器。）");
        generator.addVillagerOpt(this.profession, 0, "武器祈愿");
        generator.addVillagerOpt(this.profession, 1, "离开");
        generator.addVillagerAns(this.profession, 1, "10次祈愿必得四星武器，90次祈愿必得五星武器！五星武器 §6[左锅右铲]§r 概率UP！");
        generator.addVillagerOpt(this.profession, 2, "祈愿一次");
        generator.addVillagerOpt(this.profession, 3, "祈愿十次");
        generator.addVillagerAns(this.profession, 2, "武器抽奖券不足，是否用 %d 绿宝石替代？");
        generator.addVillagerAns(this.profession, 3, "武器抽奖券不足，是否用 %d 绿宝石补全？");
        generator.addVillagerOpt(this.profession, 4, "确定");
        generator.addVillagerOpt(this.profession, 5, "取消");
        generator.addVillagerOpt(this.profession, 6, "§a武器升级");
        generator.addVillagerOpt(this.profession, 7, "武器购买");
        generator.addVillagerAns(this.profession, 5, "是否花费 %d 绿宝石 对当前§a主手§r物品进行升级？");
        generator.addVillagerOpt(this.profession, 8, "§c阶段2后才可使用非锅类武器！");
    }


}

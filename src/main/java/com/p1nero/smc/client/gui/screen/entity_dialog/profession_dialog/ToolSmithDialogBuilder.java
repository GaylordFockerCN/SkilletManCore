package com.p1nero.smc.client.gui.screen.entity_dialog.profession_dialog;

import com.hlysine.create_connected.CCItems;
import com.jesz.createdieselgenerators.CDGItems;
import com.p1nero.smc.archive.DataManager;
import com.p1nero.smc.capability.SMCCapabilityProvider;
import com.p1nero.smc.capability.SMCPlayer;
import com.p1nero.smc.client.gui.TreeNode;
import com.p1nero.smc.client.gui.screen.LinkListStreamDialogueScreenBuilder;
import com.p1nero.smc.client.gui.screen.entity_dialog.VillagerDialogScreenHandler;
import com.p1nero.smc.datagen.lang.SMCLangGenerator;
import com.p1nero.smc.registrate.SMCRegistrateItems;
import com.p1nero.smc.util.ItemUtil;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
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
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * 盔甲抽卡
 */
public class ToolSmithDialogBuilder extends VillagerDialogScreenHandler.VillagerDialogBuilder {
    public ToolSmithDialogBuilder() {
        super(VillagerProfession.TOOLSMITH);
    }

    @Override
    public void handle(ServerPlayer serverPlayer, Villager villager, byte interactionID) {
        super.handle(serverPlayer, villager, interactionID);
        SMCPlayer smcPlayer = SMCCapabilityProvider.getSMCPlayer(serverPlayer);
        double moneyRate = smcPlayer.getLevelMoneyRate();
        int moneyBase = (int) (1600 * moneyRate);
        if (interactionID == 1) {
            if (SMCPlayer.hasMoney(serverPlayer, moneyBase, true)) {
                SMCPlayer.consumeMoney(moneyBase, serverPlayer);
                ItemUtil.addItem(serverPlayer, SMCRegistrateItems.REDSTONE_RAFFLE.asStack(10), true);
            }
        }
        if (interactionID == 2) {
            if (SMCPlayer.hasMoney(serverPlayer, moneyBase * 10, true)) {
                SMCPlayer.consumeMoney(moneyBase * 10, serverPlayer);
                ItemUtil.addItem(serverPlayer, SMCRegistrateItems.REDSTONE_RAFFLE.asStack(100), true);
            }
        }
        if (interactionID == 3) {
            if (SMCPlayer.hasMoney(serverPlayer, moneyBase, true)) {
                SMCPlayer.consumeMoney(moneyBase, serverPlayer);
                ItemUtil.addItem(serverPlayer, SMCRegistrateItems.CREATE_RAFFLE.asStack(10), true);
                DataManager.shouldShowMachineTicketHint.put(serverPlayer, false);
            }
        }
        if (interactionID == 4) {
            if (SMCPlayer.hasMoney(serverPlayer, moneyBase * 10, true)) {
                SMCPlayer.consumeMoney(moneyBase * 10, serverPlayer);
                ItemUtil.addItem(serverPlayer, SMCRegistrateItems.CREATE_RAFFLE.asStack(100), true);
                DataManager.shouldShowMachineTicketHint.put(serverPlayer, false);
            }
        }
        if (interactionID == 5) {
            this.startVanillaTrade(serverPlayer, villager);
        }
        if(interactionID == 6) {
            this.startCreateTrade(serverPlayer, villager, smcPlayer);
        }
    }

    public void startVanillaTrade(ServerPlayer serverPlayer, Villager villager) {
        villager.setTradingPlayer(serverPlayer);
        MerchantOffers merchantOffers = new MerchantOffers();

        merchantOffers.add(new MerchantOffer(
                new ItemStack(SMCRegistrateItems.REDSTONE_RAFFLE, 1),
                new ItemStack(Items.SAND, 16),
                142857, 0, 0));
        merchantOffers.add(new MerchantOffer(
                new ItemStack(SMCRegistrateItems.REDSTONE_RAFFLE, 1),
                new ItemStack(Items.CLAY, 16),
                142857, 0, 0));
        merchantOffers.add(new MerchantOffer(
                new ItemStack(SMCRegistrateItems.REDSTONE_RAFFLE, 1),
                new ItemStack(Items.OAK_LOG, 64),
                142857, 0, 0));
        merchantOffers.add(new MerchantOffer(
                new ItemStack(SMCRegistrateItems.REDSTONE_RAFFLE, 1),
                new ItemStack(Items.BAMBOO, 64),
                142857, 0, 0));
        merchantOffers.add(new MerchantOffer(
                new ItemStack(SMCRegistrateItems.REDSTONE_RAFFLE, 1),
                new ItemStack(Items.STONE, 32),
                142857, 0, 0));
        merchantOffers.add(new MerchantOffer(
                new ItemStack(SMCRegistrateItems.REDSTONE_RAFFLE, 1),
                new ItemStack(Items.COBBLESTONE, 64),
                142857, 0, 0));
        merchantOffers.add(new MerchantOffer(
                new ItemStack(SMCRegistrateItems.REDSTONE_RAFFLE, 1),
                new ItemStack(Items.WHITE_WOOL, 32),
                142857, 0, 0));
        merchantOffers.add(new MerchantOffer(
                new ItemStack(SMCRegistrateItems.REDSTONE_RAFFLE, 4),
                new ItemStack(Items.GLASS, 64),
                142857, 0, 0));
        merchantOffers.add(new MerchantOffer(
                new ItemStack(SMCRegistrateItems.REDSTONE_RAFFLE, 4),
                new ItemStack(Items.COAL_BLOCK, 64),
                142857, 0, 0));
        merchantOffers.add(new MerchantOffer(
                new ItemStack(SMCRegistrateItems.REDSTONE_RAFFLE, 4),
                new ItemStack(Items.STRING, 64),
                142857, 0, 0));
        merchantOffers.add(new MerchantOffer(
                new ItemStack(SMCRegistrateItems.REDSTONE_RAFFLE, 4),
                new ItemStack(Items.DRIED_KELP, 64),
                142857, 0, 0));
        merchantOffers.add(new MerchantOffer(
                new ItemStack(SMCRegistrateItems.REDSTONE_RAFFLE, 4),
                new ItemStack(Items.PAPER, 64),
                142857, 0, 0));
        merchantOffers.add(new MerchantOffer(
                new ItemStack(SMCRegistrateItems.REDSTONE_RAFFLE, 8),
                new ItemStack(Items.HONEY_BLOCK, 64),
                142857, 0, 0));
        merchantOffers.add(new MerchantOffer(
                new ItemStack(SMCRegistrateItems.REDSTONE_RAFFLE, 8),
                new ItemStack(Items.SLIME_BLOCK, 64),
                142857, 0, 0));
        merchantOffers.add(new MerchantOffer(
                new ItemStack(SMCRegistrateItems.REDSTONE_RAFFLE, 4),
                new ItemStack(Items.IRON_BLOCK, 16),
                142857, 0, 0));
        merchantOffers.add(new MerchantOffer(
                new ItemStack(SMCRegistrateItems.REDSTONE_RAFFLE, 4),
                new ItemStack(Items.COPPER_BLOCK, 16),
                142857, 0, 0));
        merchantOffers.add(new MerchantOffer(
                new ItemStack(SMCRegistrateItems.REDSTONE_RAFFLE, 16),
                new ItemStack(Items.GOLD_BLOCK, 32),
                142857, 0, 0));
        merchantOffers.add(new MerchantOffer(
                new ItemStack(SMCRegistrateItems.REDSTONE_RAFFLE, 16),
                new ItemStack(Items.REDSTONE_BLOCK, 32),
                142857, 0, 0));
        merchantOffers.add(new MerchantOffer(
                new ItemStack(SMCRegistrateItems.REDSTONE_RAFFLE, 16),
                new ItemStack(Items.QUARTZ, 64),
                142857, 0, 0));
        merchantOffers.add(new MerchantOffer(
                new ItemStack(SMCRegistrateItems.REDSTONE_RAFFLE, 16),
                new ItemStack(Items.AMETHYST_SHARD, 64),
                142857, 0, 0));
        merchantOffers.add(new MerchantOffer(
                new ItemStack(SMCRegistrateItems.REDSTONE_RAFFLE, 2),
                new ItemStack(Items.LAVA_BUCKET, 5),
                142857, 0, 0));
        villager.setOffers(merchantOffers);
        villager.openTradingScreen(serverPlayer, getName(), 5);
    }

    public void startCreateTrade(ServerPlayer serverPlayer, Villager villager, SMCPlayer smcPlayer) {
        villager.setTradingPlayer(serverPlayer);
        MerchantOffers merchantOffers = new MerchantOffers();
        MutableComponent desc = choice(9);

        merchantOffers.add(new MerchantOffer(
                new ItemStack(SMCRegistrateItems.CREATE_RAFFLE, 1),
                new ItemStack(SMCRegistrateItems.CREATE_COOK_GUIDE_BOOK_ITEM, 1),
                142857, 0, 0));
        merchantOffers.add(new MerchantOffer(
                new ItemStack(SMCRegistrateItems.CREATE_RAFFLE, 1),
                new ItemStack(SMCRegistrateItems.CREATE_FUEL_GUIDE_BOOK, 1),
                142857, 0, 0));
        merchantOffers.add(new MerchantOffer(
                new ItemStack(SMCRegistrateItems.CREATE_RAFFLE, 1),
                new ItemStack(AllItems.WRENCH, 1),
                142857, 0, 0));
        merchantOffers.add(new MerchantOffer(
                new ItemStack(SMCRegistrateItems.CREATE_RAFFLE, 1),
                new ItemStack(AllItems.GOGGLES, 1),
                142857, 0, 0));

        merchantOffers.add(new MerchantOffer(
                new ItemStack(SMCRegistrateItems.CREATE_RAFFLE, 1),
                new ItemStack(AllItems.EMPTY_SCHEMATIC, 1),
                142857, 0, 0));
        merchantOffers.add(new MerchantOffer(
                new ItemStack(SMCRegistrateItems.CREATE_RAFFLE, 2),
                new ItemStack(AllBlocks.SCHEMATIC_TABLE, 1),
                142857, 0, 0));
        merchantOffers.add(new MerchantOffer(
                new ItemStack(SMCRegistrateItems.CREATE_RAFFLE, 5),
                new ItemStack(AllBlocks.SCHEMATICANNON, 1),
                142857, 0, 0));

        merchantOffers.add(new MerchantOffer(
                new ItemStack(SMCRegistrateItems.CREATE_RAFFLE, 1),
                new ItemStack(CDGItems.ENGINE_SILENCER, 1),
                142857, 0, 0));

        merchantOffers.add(new MerchantOffer(
                new ItemStack(SMCRegistrateItems.CREATE_RAFFLE, 10),
                new ItemStack(AllItems.IRON_SHEET, 30),
                142857, 0, 0));
        merchantOffers.add(new MerchantOffer(
                new ItemStack(SMCRegistrateItems.CREATE_RAFFLE, 10),
                new ItemStack(AllItems.COPPER_SHEET, 30),
                142857, 0, 0));
        merchantOffers.add(new MerchantOffer(
                new ItemStack(SMCRegistrateItems.CREATE_RAFFLE, 20),
                new ItemStack(AllItems.GOLDEN_SHEET, 30),
                142857, 0, 0));
        merchantOffers.add(new MerchantOffer(
                new ItemStack(SMCRegistrateItems.CREATE_RAFFLE, 10),
                new ItemStack(AllItems.ZINC_INGOT, 30),
                142857, 0, 0));
        merchantOffers.add(new MerchantOffer(
                new ItemStack(SMCRegistrateItems.CREATE_RAFFLE, 10),
                new ItemStack(AllItems.ANDESITE_ALLOY, 40),
                142857, 0, 0));

        if(smcPlayer.getLevel() > SMCPlayer.STAGE2_REQUIRE){

            merchantOffers.add(new MerchantOffer(
                    new ItemStack(SMCRegistrateItems.CREATE_RAFFLE, 12),
                    new ItemStack(AllBlocks.ANDESITE_CASING, 60),
                    142857, 0, 0));

            merchantOffers.add(new MerchantOffer(
                    new ItemStack(SMCRegistrateItems.CREATE_RAFFLE, 12),
                    new ItemStack(AllBlocks.COPPER_CASING, 20),
                    142857, 0, 0));

            merchantOffers.add(new MerchantOffer(
                    new ItemStack(SMCRegistrateItems.CREATE_RAFFLE, 2),
                    new ItemStack(AllItems.BELT_CONNECTOR, 40),
                    142857, 0, 0));
            merchantOffers.add(new MerchantOffer(
                    new ItemStack(SMCRegistrateItems.CREATE_RAFFLE, 2),
                    new ItemStack(AllBlocks.SHAFT, 40),
                    142857, 0, 0));
            merchantOffers.add(new MerchantOffer(
                    new ItemStack(SMCRegistrateItems.CREATE_RAFFLE, 2),
                    new ItemStack(AllBlocks.COGWHEEL, 20),
                    142857, 0, 0));
            merchantOffers.add(new MerchantOffer(
                    new ItemStack(SMCRegistrateItems.CREATE_RAFFLE, 2),
                    new ItemStack(AllBlocks.LARGE_COGWHEEL, 40),
                    142857, 0, 0));

            merchantOffers.add(new MerchantOffer(
                    new ItemStack(SMCRegistrateItems.CREATE_RAFFLE, 12),
                    new ItemStack(AllBlocks.BRASS_CASING, 12),
                    142857, 0, 0));
            merchantOffers.add(new MerchantOffer(
                    new ItemStack(SMCRegistrateItems.CREATE_RAFFLE, 20),
                    new ItemStack(AllItems.BRASS_SHEET, 32),
                    142857, 0, 0));
            merchantOffers.add(new MerchantOffer(
                    new ItemStack(SMCRegistrateItems.CREATE_RAFFLE, 20),
                    new ItemStack(AllItems.BRASS_INGOT, 40),
                    142857, 0, 0));
            merchantOffers.add(new MerchantOffer(
                    new ItemStack(SMCRegistrateItems.CREATE_RAFFLE, 40),
                    new ItemStack(AllItems.PRECISION_MECHANISM, 8),
                    142857, 0, 0));
        }

        if(smcPlayer.getLevel() > SMCPlayer.STAGE3_REQUIRE){
            desc = choice(10);

            merchantOffers.add(new MerchantOffer(
                    new ItemStack(SMCRegistrateItems.CREATE_RAFFLE, 20),
                    new ItemStack(Items.BLAZE_ROD, 2),
                    new ItemStack(AllBlocks.BLAZE_BURNER, 2),
                    142857, 0, 0));
            merchantOffers.add(new MerchantOffer(
                    new ItemStack(SMCRegistrateItems.CREATE_RAFFLE, 20),
                    new ItemStack(AllItems.ELECTRON_TUBE, 4),
                    142857, 0, 0));
            merchantOffers.add(new MerchantOffer(
                    new ItemStack(SMCRegistrateItems.CREATE_RAFFLE, 20),
                    new ItemStack(CCItems.CONTROL_CHIP, 4),
                    142857, 0, 0));
            merchantOffers.add(new MerchantOffer(
                    new ItemStack(SMCRegistrateItems.CREATE_RAFFLE, 20),
                    new ItemStack(AllItems.STURDY_SHEET, 4),
                    142857, 0, 0));
            merchantOffers.add(new MerchantOffer(
                    new ItemStack(SMCRegistrateItems.CREATE_RAFFLE, 20),
                    new ItemStack(AllBlocks.ROSE_QUARTZ_LAMP, 8),
                    142857, 0, 0));
            merchantOffers.add(new MerchantOffer(
                    new ItemStack(SMCRegistrateItems.CREATE_RAFFLE, 20),
                    new ItemStack(AllItems.TRANSMITTER, 4),
                    142857, 0, 0));
            merchantOffers.add(new MerchantOffer(
                    new ItemStack(SMCRegistrateItems.CREATE_RAFFLE, 10),
                    new ItemStack(AllItems.POTATO_CANNON, 1),
                    142857, 0, 0));
        }

        villager.setOffers(merchantOffers);
        villager.openTradingScreen(serverPlayer, desc, 5);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void createDialog(LinkListStreamDialogueScreenBuilder builder, Villager self) {
        LocalPlayer localPlayer = Minecraft.getInstance().player;
        if (localPlayer != null) {
            SMCPlayer smcPlayer = SMCCapabilityProvider.getSMCPlayer(localPlayer);
            int playerLevel = smcPlayer.getLevel();
            int moneyBase = (int) (1600 * smcPlayer.getLevelMoneyRate());

            TreeNode root = new TreeNode(answer(0))
                    .addChild(new TreeNode(answer(1), choice(0))
                            .addChild(new TreeNode(answer(2), choice(3))
                                    .addLeaf(choice(5, moneyBase), (byte) 1)//兑换原版通票 x 10
                                    .addLeaf(choice(6, moneyBase * 10), (byte) 2)//兑换原版通票 x 100
                            )
                            .addChild(new TreeNode(answer(2), choice(4))
                                    .addLeaf(choice(5, moneyBase), (byte) 3)//兑换机械通票 x 10
                                    .addLeaf(choice(6, moneyBase * 10), (byte) 4)//兑换机械通票 x 100
                            )
                    );

            MutableComponent opt5 = choice(1).append(playerLevel > 5 ? Component.empty() : choice(8));
            MutableComponent opt6 = choice(2).append(playerLevel > 5 ? Component.empty() : choice(8));
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

            builder.setAnswerRoot(root);
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
        generator.addVillagerOpt(this.profession, 5, "兑换 10 张 %d绿宝石");
        generator.addVillagerOpt(this.profession, 6, "兑换 100 张 %d绿宝石");
        generator.addVillagerOpt(this.profession, 7, "离开");
        generator.addVillagerOpt(this.profession, 8, "§c[声望等级达到§l6级§r§c解锁]");
        generator.addVillagerOpt(this.profession, 9, "§c当前还有货物未解锁");
        generator.addVillagerOpt(this.profession, 10, "§6已全部解锁！");
    }


}

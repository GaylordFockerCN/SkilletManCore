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
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;

public class LibrarianDialogBuilder extends VillagerDialogScreenHandler.VillagerDialogBuilder {
    public static final List<ItemStack> COMMON_SKILL_BOOKS = new ArrayList<>();
    public static final List<ItemStack> WOM_SKILL_BOOKS = new ArrayList<>();
    public LibrarianDialogBuilder() {
        super(VillagerProfession.LIBRARIAN);
    }

    @Override
    public void handle(ServerPlayer serverPlayer, Villager villager, byte interactionID) {
        super.handle(serverPlayer, villager, interactionID);

        SMCPlayer smcPlayer = SMCCapabilityProvider.getSMCPlayer(serverPlayer);
        if (interactionID == 1) {
            int ticketCount = ItemUtil.searchAndConsumeItem(serverPlayer, SMCRegistrateItems.SKILL_BOOK_RAFFLE_TICKET.asItem(), 1);
            if (ticketCount == 0) {
                if(SMCPlayer.hasMoney(serverPlayer, 1600, true)) {
                    SMCPlayer.consumeMoney(1600, serverPlayer);
                    smcPlayer.setSkillBookGachaingCount(1);
                }
            } else {
                serverPlayer.serverLevel().playSound(null, serverPlayer.getX(), serverPlayer.getY(), serverPlayer.getZ(), SMCSounds.VILLAGER_YES.get(), serverPlayer.getSoundSource(), 1.0F, 1.0F);
                smcPlayer.setSkillBookGachaingCount(1);
            }
        }
        if (interactionID == 2) {
            int ticketCount = ItemUtil.searchAndConsumeItem(serverPlayer, SMCRegistrateItems.SKILL_BOOK_RAFFLE_TICKET.asItem(), 10);
            if(ticketCount < 10) {
                int need = 10 - ticketCount;
                if(SMCPlayer.hasMoney(serverPlayer, 1600 * need, true)) {
                    SMCPlayer.consumeMoney(1600 * need, serverPlayer);
                    smcPlayer.setSkillBookGachaingCount(10);
                }
            } else {
                serverPlayer.serverLevel().playSound(null, serverPlayer.getX(), serverPlayer.getY(), serverPlayer.getZ(), SMCSounds.VILLAGER_YES.get(), serverPlayer.getSoundSource(), 1.0F, 1.0F);
                smcPlayer.setSkillBookGachaingCount(10);
            }
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void createDialog(LinkListStreamDialogueScreenBuilder builder, Villager self) {

        LocalPlayer localPlayer = Minecraft.getInstance().player;
        if (localPlayer != null) {
            int ticketCount = localPlayer.getInventory().countItem(SMCRegistrateItems.SKILL_BOOK_RAFFLE_TICKET.asItem());

            TreeNode pull = new TreeNode(answer(2), choice(2));

            if (ticketCount < 1) {
                pull.addChild(new TreeNode(answer(3, 1600), choice(3))
                                .addLeaf(choice(5), (byte) 1)
                                .addLeaf(choice(6), (byte) -1))
                        .addChild(new TreeNode(answer(3, 16000), choice(4))
                                .addLeaf(choice(5), (byte) 2)
                                .addLeaf(choice(6), (byte) -1));
            } else if (ticketCount < 10) {
                int needTicket = 10 - ticketCount;
                pull.addLeaf(choice(3), (byte) 1)
                        .addChild(new TreeNode(answer(4, 1600 * needTicket), choice(4))
                                .addLeaf(choice(5), (byte) 2)
                                .addLeaf(choice(6), (byte) -1));
            } else {
                pull.addLeaf(choice(3), (byte) 1);
                pull.addLeaf(choice(4), (byte) 2);
            }

            builder.setAnswerRoot(new TreeNode(answer(0))
                    .addChild(new TreeNode(answer(1), choice(0))
                            .addLeaf(choice(1), (byte) -1))
                    .addChild(pull)
                    .addLeaf(choice(-1), (byte) -1));
        }

    }

    @Override
    public void onGenerateLang(SMCLangGenerator generator) {
        generator.addVillagerName(this.profession, "智慧的图书管理员");
        generator.addVillagerOpt(this.profession, -1, "离去");
        generator.addVillagerAns(this.profession, 0, "（图书管理员用它充满智慧的眼神看着你，这种眼神不同于绿袍尊者的那种智慧。  要不找他问问有没有卖附魔书吧，刚好给我的锅和铲升升级）");
        generator.addVillagerOpt(this.profession, 0, "购买附魔书");
        generator.addVillagerAns(this.profession, 1, "大胆！你不会以为我真的会卖给你附魔书吧？你是不是对作者设计的五合一升级系统和抽卡系统有意见？还企图通过附魔这种旁门左道提升实力？！");
        generator.addVillagerOpt(this.profession, 1, "好嘛");
        generator.addVillagerOpt(this.profession, 2, "抽取技能书");
        generator.addVillagerAns(this.profession, 2, "所有技能书概率均等，童叟无欺！抽10次必出奇迹武器技能书！");
        generator.addVillagerOpt(this.profession, 3, "抽 1 次");
        generator.addVillagerOpt(this.profession, 4, "抽 10 次");
        generator.addVillagerAns(this.profession, 3, "技能书抽奖券不足，是否用 %d 绿宝石替代？");
        generator.addVillagerAns(this.profession, 4, "技能书抽奖券不足，是否用 %d 绿宝石补全？");
        generator.addVillagerOpt(this.profession, 5, "确定");
        generator.addVillagerOpt(this.profession, 6, "取消");
    }


}

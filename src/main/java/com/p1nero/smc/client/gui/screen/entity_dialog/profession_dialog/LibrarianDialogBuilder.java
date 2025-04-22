package com.p1nero.smc.client.gui.screen.entity_dialog.profession_dialog;

import com.p1nero.smc.client.gui.TreeNode;
import com.p1nero.smc.client.gui.screen.LinkListStreamDialogueScreenBuilder;
import com.p1nero.smc.client.gui.screen.entity_dialog.VillagerDialogScreenHandler;
import com.p1nero.smc.datagen.lang.SMCLangGenerator;
import com.p1nero.smc.registrate.SMCRegistrateItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class LibrarianDialogBuilder extends VillagerDialogScreenHandler.VillagerDialogBuilder {
    public LibrarianDialogBuilder() {
        super(VillagerProfession.LIBRARIAN);
    }

    @Override
    public void handle(ServerPlayer serverPlayer, Villager villager, byte interactionID) {
        super.handle(serverPlayer, villager, interactionID);
        if(interactionID == 3) {

        }
        if(interactionID == 4) {

        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void createDialog(LinkListStreamDialogueScreenBuilder builder, Villager self) {

        LocalPlayer localPlayer = Minecraft.getInstance().player;
        if(localPlayer != null) {
            int ticketCount = localPlayer.getInventory().countItem(SMCRegistrateItems.SKILL_BOOK_RAFFLE_TICKET.asItem());

            TreeNode pull = new TreeNode(answer(2), choice(2));

            if(ticketCount < 1) {

            } else if(ticketCount < 10) {

            } else {

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
    }



}

package com.p1nero.smc.client.gui.screen.entity_dialog.profession_dialog;

import com.p1nero.smc.client.gui.TreeNode;
import com.p1nero.smc.client.gui.screen.LinkListStreamDialogueScreenBuilder;
import com.p1nero.smc.client.gui.screen.entity_dialog.VillagerDialogScreenHandler;
import com.p1nero.smc.datagen.lang.SMCLangGenerator;
import com.p1nero.smc.registrate.SMCRegistrateItems;
import com.p1nero.smc.util.ItemUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.List;

/**
 * 碟片抽卡
 */
public class CartographerDialogBuilder extends VillagerDialogScreenHandler.VillagerDialogBuilder {
    public static final List<ItemStack> DISCS = List.of(Items.MUSIC_DISC_11.getDefaultInstance(), Items.MUSIC_DISC_WARD.getDefaultInstance(), Items.MUSIC_DISC_13.getDefaultInstance(),
            Items.MUSIC_DISC_5.getDefaultInstance(), Items.MUSIC_DISC_OTHERSIDE.getDefaultInstance(), Items.MUSIC_DISC_BLOCKS.getDefaultInstance(), Items.MUSIC_DISC_CAT.getDefaultInstance(),
            Items.MUSIC_DISC_CHIRP.getDefaultInstance(), Items.MUSIC_DISC_FAR.getDefaultInstance(), Items.MUSIC_DISC_MALL.getDefaultInstance(), Items.MUSIC_DISC_MELLOHI.getDefaultInstance(),
            Items.MUSIC_DISC_PIGSTEP.getDefaultInstance(), Items.MUSIC_DISC_RELIC.getDefaultInstance(), Items.MUSIC_DISC_STRAD.getDefaultInstance(), Items.MUSIC_DISC_WAIT.getDefaultInstance(),
            Items.MUSIC_DISC_STAL.getDefaultInstance());

    public CartographerDialogBuilder() {
        super(VillagerProfession.CARTOGRAPHER);
    }

    @Override
    public void handle(ServerPlayer serverPlayer, Villager villager, byte interactionID) {
        super.handle(serverPlayer, villager, interactionID);
        if (interactionID == 1) {
            int dollTicketCnt = ItemUtil.searchAndConsumeItem(serverPlayer, SMCRegistrateItems.DISC_RAFFLE_TICKET.asItem(), 1);
            if (dollTicketCnt == 0) {
                ItemUtil.tryAddRandomItem(serverPlayer, DISCS, 1600, 1);
                serverPlayer.serverLevel().sendParticles(ParticleTypes.TOTEM_OF_UNDYING, serverPlayer.getX(), serverPlayer.getY(), serverPlayer.getZ(), 50, 1.0, 1.0, 1.0, 0.2);
                serverPlayer.serverLevel().playSound(null, serverPlayer.getOnPos(), SoundEvents.FIREWORK_ROCKET_BLAST, SoundSource.BLOCKS, 0.5F, 2.0F);

            } else {
                BlockPos spawnPos = serverPlayer.getOnPos().above(4);
                ItemUtil.addItemEntity(serverPlayer.serverLevel(), spawnPos, DISCS.get(serverPlayer.getRandom().nextInt(DISCS.size())));
                serverPlayer.serverLevel().sendParticles(ParticleTypes.TOTEM_OF_UNDYING, spawnPos.getX(), spawnPos.getY(), spawnPos.getZ(), 50, 1.0, 1.0, 1.0, 0.2);
                serverPlayer.serverLevel().playSound(null, serverPlayer.getOnPos(), SoundEvents.FIREWORK_ROCKET_BLAST, SoundSource.BLOCKS, 0.5F, 2.0F);
            }
        }
    }

    @Override
    public void createDialog(LinkListStreamDialogueScreenBuilder builder, Villager self) {
        LocalPlayer localPlayer = Minecraft.getInstance().player;
        if (localPlayer != null) {

            TreeNode root = new TreeNode(answer(0));

            int discTicketCnt = localPlayer.getInventory().countItem(SMCRegistrateItems.DISC_RAFFLE_TICKET.asItem());
            if (discTicketCnt < 1) {
                root.addChild(new TreeNode(answer(1), choice(0))
                        .addLeaf(choice(2), (byte) 1)
                        .addLeaf(choice(3)));
            } else {
                root.addLeaf(choice(0), (byte) 1);
            }
            root.addLeaf(choice(1));

            builder.setAnswerRoot(root);
        }
    }

    @Override
    public void onGenerateLang(SMCLangGenerator generator) {
        generator.addVillagerName(this.profession, " §d时尚的制图师§r");
        generator.addVillagerAns(this.profession, 0, "嘿宝贝，来点碟吗？这儿可都是大宝贝！（你愿意花费辛苦赚来的绿宝石，  为枯燥的营业生活添加点乐趣吗？  传说级碟片 §l§8[13号唱片]§r 概率UP！）");
        generator.addVillagerOpt(this.profession, 0, "抽取唱片");
        generator.addVillagerOpt(this.profession, 1, "离去");
        generator.addVillagerAns(this.profession, 1, "唱片抽奖券不足，是否用 §a1600 绿宝石§r代替？");
        generator.addVillagerOpt(this.profession, 2, "确定");
        generator.addVillagerOpt(this.profession, 3, "取消");
    }


}

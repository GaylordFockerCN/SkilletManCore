package com.p1nero.smc.client.gui.screen.entity_dialog.profession_dialog;

import com.github.ysbbbbbb.kaleidoscopedoll.init.ModItems;
import com.p1nero.smc.SkilletManCoreMod;
import com.p1nero.smc.capability.SMCCapabilityProvider;
import com.p1nero.smc.capability.SMCPlayer;
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
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import reascer.wom.world.entity.WOMEntities;

import java.util.List;

/**
 * 玩偶和宠物抽奖
 */
public class ShepherdDialogBuilder extends VillagerDialogScreenHandler.VillagerDialogBuilder {
    public static final List<EntityType<? extends Animal>> ENTITY_TYPE_LIST = List.of(EntityType.SKELETON_HORSE, EntityType.CAMEL, EntityType.HORSE, EntityType.CAT, EntityType.WOLF, EntityType.PARROT);

    public ShepherdDialogBuilder() {
        super(VillagerProfession.SHEPHERD);
    }

    @Override
    public void handle(ServerPlayer serverPlayer, Villager villager, byte interactionID) {
        super.handle(serverPlayer, villager, interactionID);
        SMCPlayer smcPlayer = SMCCapabilityProvider.getSMCPlayer(serverPlayer);
        double moneyRate = smcPlayer.getLevelMoneyRate();
        if (interactionID == 1) {
            int petTicketCnt = ItemUtil.searchAndConsumeItem(serverPlayer, SMCRegistrateItems.PET_RAFFLE_TICKET.asItem(), 1);
            if (petTicketCnt == 0) {
                if (ItemUtil.tryAddRandomItem(serverPlayer, List.of(Items.LEAD.getDefaultInstance()), (int)(5000 * moneyRate), 1)) {
                    getPet(serverPlayer, villager);
                }
            } else {
                getPet(serverPlayer, villager);
            }
        }

        if (interactionID == 2) {
            int dollTicketCnt = ItemUtil.searchAndConsumeItem(serverPlayer, SMCRegistrateItems.DOLL_RAFFLE_TICKET.asItem(), 1);
            if (dollTicketCnt == 0) {
                ItemUtil.tryAddRandomItem(serverPlayer, List.of(ModItems.PURPLE_DOLL_GIFT_BOX.get().getDefaultInstance()), (int)(1600 * moneyRate), 1);
                serverPlayer.serverLevel().sendParticles(ParticleTypes.TOTEM_OF_UNDYING, serverPlayer.getX(), serverPlayer.getY(), serverPlayer.getZ(), 50, 1.0, 1.0, 1.0, 0.2);
                serverPlayer.serverLevel().playSound(null, serverPlayer.getOnPos(), SoundEvents.FIREWORK_ROCKET_BLAST, SoundSource.BLOCKS, 1.0F, 2.0F);
            } else {
                BlockPos spawnPos = serverPlayer.getOnPos().above(4);
                ItemUtil.addItemEntity(serverPlayer.serverLevel(), spawnPos, ModItems.PURPLE_DOLL_GIFT_BOX.get().getDefaultInstance());
                serverPlayer.serverLevel().sendParticles(ParticleTypes.TOTEM_OF_UNDYING, spawnPos.getX(), spawnPos.getY(), spawnPos.getZ(), 50, 1.0, 1.0, 1.0, 0.2);
                serverPlayer.serverLevel().playSound(null, serverPlayer.getOnPos(), SoundEvents.FIREWORK_ROCKET_BLAST, SoundSource.BLOCKS, 1.0F, 2.0F);
            }
        }

    }

    public void getPet(ServerPlayer serverPlayer, Villager villager) {
        EntityType<? extends Animal> entityType = ENTITY_TYPE_LIST.get(serverPlayer.getRandom().nextInt(ENTITY_TYPE_LIST.size()));
        int randomInt = serverPlayer.getRandom().nextInt(10);
        if (randomInt == 1) {
            entityType = WOMEntities.LUPUS_REX.get();
        }
        BlockPos spawnPos = villager.getOnPos().above(3);
        Animal animal = entityType.spawn(serverPlayer.serverLevel(), spawnPos, MobSpawnType.MOB_SUMMONED);
        if (animal != null) {
            animal.playAmbientSound();
            animal.setInLove(serverPlayer);
            if (animal instanceof TamableAnimal tamableAnimal) {
                tamableAnimal.tame(serverPlayer);
            }
            if (animal instanceof AbstractHorse abstractHorse) {
                abstractHorse.tameWithName(serverPlayer);
                ItemUtil.addItem(serverPlayer, Items.SADDLE.getDefaultInstance(), true);
            }
            ItemUtil.addItem(serverPlayer, Items.NAME_TAG.getDefaultInstance(), true);
            animal.setCustomName(serverPlayer.getDisplayName().copy().append(SkilletManCoreMod.getInfo("someone_s_pet")).append(animal.getDisplayName()));
            animal.setCustomNameVisible(true);

            int x = spawnPos.getX();
            int y = spawnPos.getY();
            int z = spawnPos.getZ();
            serverPlayer.serverLevel().sendParticles(ParticleTypes.TOTEM_OF_UNDYING, x, y, z, 50, 1.0, 1.0, 1.0, 0.2);
            serverPlayer.serverLevel().playSound(null, spawnPos, SoundEvents.FIREWORK_ROCKET_BLAST, SoundSource.BLOCKS, 1.0F, 2.0F);
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void createDialog(LinkListStreamDialogueScreenBuilder builder, Villager self) {
        LocalPlayer localPlayer = Minecraft.getInstance().player;
        if (localPlayer != null) {
            SMCPlayer smcPlayer = SMCCapabilityProvider.getSMCPlayer(localPlayer);
            double moneyRate = smcPlayer.getLevelMoneyRate();

            TreeNode root = new TreeNode(answer(0));

            int petTicketCnt = localPlayer.getInventory().countItem(SMCRegistrateItems.DOLL_RAFFLE_TICKET.asItem());
            int dollTicketCnt = localPlayer.getInventory().countItem(SMCRegistrateItems.PET_RAFFLE_TICKET.asItem());
            if (petTicketCnt < 1) {
                root.addChild(new TreeNode(answer(1, (int)(5000 * moneyRate)), choice(0))
                        .addLeaf(choice(2), (byte) 1)
                        .addLeaf(choice(3)));
            } else {
                root.addLeaf(choice(0), (byte) 1);
            }

            if (dollTicketCnt < 1) {
                root.addChild(new TreeNode(answer(2, (int)(1600 * moneyRate)), choice(1))
                        .addLeaf(choice(2), (byte) 2)
                        .addLeaf(choice(3)));
            } else {
                root.addLeaf(choice(1), (byte) 2);
            }
            root.addLeaf(choice(4));

            builder.setAnswerRoot(root);
        }
    }

    @Override
    public void onGenerateLang(SMCLangGenerator generator) {
        generator.addVillagerName(this.profession, " §3和蔼的牧羊人§r ");
        generator.addVillagerAns(this.profession, 0, "（你愿意花费辛苦赚来的绿宝石，为枯燥的营业生活添加点乐趣吗？  传说级坐骑 §e[Lupus Rex]§r 概率UP！）");
        generator.addVillagerOpt(this.profession, 0, "抽宠物 一次 ");
        generator.addVillagerOpt(this.profession, 1, "抽玩偶 一次");
        generator.addVillagerOpt(this.profession, 4, "为什么牧羊人会有Lupus Rex...");
        generator.addVillagerAns(this.profession, 1, "宠物抽奖券不足，是否用 %d 绿宝石代替？");
        generator.addVillagerAns(this.profession, 2, "玩偶抽奖券不足，是否用 %d 绿宝石代替？");
        generator.addVillagerOpt(this.profession, 2, "确定");
        generator.addVillagerOpt(this.profession, 3, "取消");
    }


}

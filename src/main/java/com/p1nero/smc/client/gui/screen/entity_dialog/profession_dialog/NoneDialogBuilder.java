package com.p1nero.smc.client.gui.screen.entity_dialog.profession_dialog;

import com.p1nero.smc.archive.DataManager;
import com.p1nero.smc.client.gui.TreeNode;
import com.p1nero.smc.client.gui.screen.LinkListStreamDialogueScreenBuilder;
import com.p1nero.smc.client.gui.screen.entity_dialog.VillagerDialogScreenHandler;
import com.p1nero.smc.client.sound.SMCSounds;
import com.p1nero.smc.datagen.SMCAdvancementData;
import com.p1nero.smc.datagen.lang.SMCLangGenerator;
import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public class NoneDialogBuilder extends VillagerDialogScreenHandler.VillagerDialogBuilder {
    public NoneDialogBuilder() {
        super(VillagerProfession.NONE);
    }

    /**
     * 概率太高了，于是多点随机性
     */
    @Override
    public void createDialog(LinkListStreamDialogueScreenBuilder builder, Villager self) {
        int id = self.getRandom().nextInt(3);
        Player player = Minecraft.getInstance().player;
        if(!DataManager.firstChangeVillager.get(player)){
            id = 2;
        }
        if (player == null) {
            return;
        }
        String playerName = player.getGameProfile().getName();
        switch (id) {
            case 0 -> builder.setAnswerRoot(new TreeNode(answer(10))
                    .addChild(new TreeNode(answer(11), choice(10))
                            .addLeaf(choice(2), (byte) -1))
                    .addLeaf(choice(2), (byte) -1));
            case 1 -> builder.setAnswerRoot(new TreeNode(answer(20, playerName, playerName, playerName))
                    .addChild(new TreeNode(answer(21), choice(20))
                            .addLeaf(choice(2), (byte) -1))
                    .addLeaf(choice(2), (byte) -1));
            default -> builder.setAnswerRoot(new TreeNode(answer(0))
                    .addChild(new TreeNode(answer(1), choice(1))
                            .addChild(new TreeNode(answer(2), choice(3))
                                    .addChild(new TreeNode(answer(3), choice(3))
                                            .addChild(new TreeNode(answer(4), choice(3))
                                                    .addLeaf(choice(4), (byte) 4)
                                                    .addLeaf(choice(5), (byte) 5)
                                                    .addLeaf(choice(6), (byte) 6)
                                                    .addLeaf(choice(7), (byte) 7)
                                                    .addLeaf(choice(8), (byte) 8)
                                                    .addLeaf(choice(9), (byte) 9))
                                            .addLeaf(choice(2)))
                                    .addLeaf(choice(2)))
                            .addLeaf(choice(2)))
                    .addLeaf(choice(2)));
        }
    }

    @Override
    public void handle(ServerPlayer serverPlayer, Villager villager, byte interactionID) {
        super.handle(serverPlayer, villager, interactionID);
        if(interactionID >= 4 && interactionID <= 9) {
            if(!DataManager.firstChangeVillager.get(serverPlayer)){
                DataManager.firstChangeVillager.put(serverPlayer, true);
            }
            SMCAdvancementData.finishAdvancement("change_villager", serverPlayer);
            villager.playSound(SMCSounds.VILLAGER_YES.get(), 1.0F, 1.0F);
        }
        switch (interactionID) {
            case 4 -> villager.setVillagerData(villager.getVillagerData().setProfession(VillagerProfession.NITWIT));
            case 5 -> villager.setVillagerData(villager.getVillagerData().setProfession(VillagerProfession.WEAPONSMITH).setLevel(2));//setLevel锁职业
            case 6 -> villager.setVillagerData(villager.getVillagerData().setProfession(VillagerProfession.ARMORER).setLevel(2));
            case 7 -> villager.setVillagerData(villager.getVillagerData().setProfession(VillagerProfession.CLERIC).setLevel(2));
            case 8 -> {
                Pig pig = new Pig(EntityType.PIG, serverPlayer.level());
                pig.setPos(villager.position());
                pig.setCustomName(villager.getCustomName());
                pig.setCustomNameVisible(true);
                villager.discard();
                serverPlayer.level().addFreshEntity(pig);
            }
            case 9 -> {
                List<VillagerProfession> villagerProfessions = ForgeRegistries.VILLAGER_PROFESSIONS.getValues().stream().filter(villagerProfession -> villagerProfession.workSound() != null).toList();
                //备用
//                List<VillagerProfession> villagerProfessions = List.of(VillagerProfession.FARMER, VillagerProfession.BUTCHER, VillagerProfession.FISHERMAN,
//                        VillagerProfession.CLERIC, VillagerProfession.LIBRARIAN, VillagerProfession.FLETCHER,
//                        VillagerProfession.LEATHERWORKER, VillagerProfession.NITWIT, VillagerProfession.ARMORER,
//                        VillagerProfession.MASON, VillagerProfession.SHEPHERD, VillagerProfession.CARTOGRAPHER,
//                        VillagerProfession.TOOLSMITH, VillagerProfession.WEAPONSMITH);
                VillagerProfession profession1 = villagerProfessions.get(serverPlayer.getRandom().nextInt(villagerProfessions.size()));
                villager.setVillagerData(villager.getVillagerData().setProfession(profession1).setLevel(2));
                villager.setVillagerXp(1);
            }
        }
    }

    @Override
    public void onGenerateLang(SMCLangGenerator generator) {
        generator.addVillagerName(this.profession, " §6无业游民§r ");
        generator.addVillagerAns(this.profession, 0, "（别看它叫无业游民，其实找不找得到工作取决于你给不给他工作方块）");
        generator.addVillagerOpt(this.profession, 1, "赋予工作");
        generator.addVillagerOpt(this.profession, 2, "结束对话");
        generator.addVillagerAns(this.profession, 1, "（你确定要给予他工作吗？你确定要在没有工作方块的情况下给予他工作吗？）");
        generator.addVillagerOpt(this.profession, 3, "我确定");
        generator.addVillagerAns(this.profession, 2, "（你确定不再考虑一下吗？你就不担心村民脱离了工作方块会导致游戏崩溃吗？）");
        generator.addVillagerAns(this.profession, 3, "（你就不考虑村民的自己的意愿吗？）");
        generator.addVillagerAns(this.profession, 4, "（你想将它变成什么职业？）");
        generator.addVillagerOpt(this.profession, 4, "绿袍尊者");
        generator.addVillagerOpt(this.profession, 5, "武器匠§a（武器祈愿）");
        generator.addVillagerOpt(this.profession, 6, "盔甲匠§a（盔甲祈愿）");
        generator.addVillagerOpt(this.profession, 7, "牧师§a（主线）");
        generator.addVillagerOpt(this.profession, 8, "猪");
        generator.addVillagerOpt(this.profession, 9, "随机职业");


        generator.addVillagerAns(this.profession, 10, "你知道吗，不是我不想找工作，而是这年头工作太卷了，工作方块又太稀缺，没有玩家给我们方块，我们只能整天在这里闲逛。");
        generator.addVillagerOpt(this.profession, 10, "给我19.9绿宝石，我将赐予你工作");
        generator.addVillagerAns(this.profession, 11, "那还是算了...");

        generator.addVillagerAns(this.profession, 20, "我要采一朵花，送给 %s， %s %s，你看，有花！｡◕◡◕｡)ﾉ ❀");
        generator.addVillagerOpt(this.profession, 20, "工作找到没有！");
        generator.addVillagerAns(this.profession, 21, "呜呜哇哇哇哇哇哇哇哇哇哇");
    }


}

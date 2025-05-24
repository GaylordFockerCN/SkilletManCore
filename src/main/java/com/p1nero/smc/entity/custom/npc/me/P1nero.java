package com.p1nero.smc.entity.custom.npc.me;

import com.p1nero.smc.archive.DataManager;
import com.p1nero.smc.client.gui.TreeNode;
import com.p1nero.smc.client.gui.screen.LinkListStreamDialogueScreenBuilder;
import com.p1nero.smc.client.sound.SMCSounds;
import com.p1nero.smc.entity.SMCEntities;
import com.p1nero.smc.entity.custom.npc.SMCNpc;
import com.p1nero.smc.network.PacketRelay;
import com.p1nero.smc.network.SMCPacketHandler;
import com.p1nero.smc.network.packet.clientbound.NPCDialoguePacket;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

/**
 * 打完BOSS结束后出对话
 */
public class P1nero extends SMCNpc {
    private static final byte STAR0 = 1;
    private static final byte STAR1 = 2;
    private static final byte STAR2 = 3;
    private static final byte STAR3 = 4;
    private static final byte STAR4 = 5;
    private static final byte STAR5 = 6;
    private static final byte END = 7;
    public P1nero(EntityType<? extends Villager> entityType, Level level) {
        super(entityType, level);
    }

    public P1nero(Player owner, Vec3 pos) {
        this(SMCEntities.P1NERO.get(), owner.level());
        this.setPos(pos);
        this.setOwner(owner);
        this.setItemInHand(InteractionHand.MAIN_HAND, Items.WRITABLE_BOOK.getDefaultInstance());
    }

    @Override
    public @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
        if (player instanceof ServerPlayer serverPlayer) {
            //补对话声音
            this.lookAt(player, 180.0F, 180.0F);
            if (this.getConversingPlayer() == null && player == this.getOwner()) {
                CompoundTag compoundTag = new CompoundTag();
                PacketRelay.sendToPlayer(SMCPacketHandler.INSTANCE, new NPCDialoguePacket(this.getId(), getDialogData(compoundTag, serverPlayer)), serverPlayer);
                this.setConversingPlayer(serverPlayer);
            }
        }
        return InteractionResult.sidedSuccess(level().isClientSide);
    }

    @Override
    public boolean hurt(@NotNull DamageSource source, float value) {
        if(source.getEntity() instanceof ServerPlayer serverPlayer) {
            //彩蛋对话
            if (this.getConversingPlayer() == null){
                CompoundTag compoundTag = new CompoundTag();
                compoundTag.putBoolean("from_hurt", true);
                PacketRelay.sendToPlayer(SMCPacketHandler.INSTANCE, new NPCDialoguePacket(this.getId(), compoundTag), serverPlayer);
                this.setConversingPlayer(serverPlayer);
            } else {
                return false;
            }
            source.getEntity().hurt(this.damageSources().indirectMagic(this, this), value * 0.5F);
            EntityType.LIGHTNING_BOLT.spawn(serverPlayer.serverLevel(), serverPlayer.getOnPos(), MobSpawnType.MOB_SUMMONED);
        }
        return super.hurt(source, 0);
    }

    @Override
    public void thunderHit(@NotNull ServerLevel serverLevel, @NotNull LightningBolt lightningBolt) {
    }

    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource source) {
        return null;
    }

    @Override
    public SoundEvent getAmbientSound() {
        return SMCSounds.TALKING.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return null;
    }

    @Override
    public void playAmbientSound() {

    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void openDialogueScreen(CompoundTag senderData) {
        LinkListStreamDialogueScreenBuilder builder = new LinkListStreamDialogueScreenBuilder(this);
        if(getAmbientSound() != null) {
            level().playLocalSound(getX(), getY(), getZ(), getAmbientSound(), SoundSource.BLOCKS, 1.0F, 1.0F, false);
        }
        if(senderData.getBoolean("from_hurt")){
            builder.start(-1)
                    .addFinalChoice(-1)
                    .addFinalChoice(-2);
            Minecraft.getInstance().setScreen(builder.build());
            return;
        }

        TreeNode star5_1 = new TreeNode(ans(1), opt(0));
        TreeNode star5_2 = new TreeNode(ans(2), opt(0));
        TreeNode star5_3 = new TreeNode(ans(3), opt(0));
        TreeNode star5_4 = new TreeNode(ans(4), opt(0));
        TreeNode star5_5 = new TreeNode(ans(5), opt(0));
        TreeNode star5_6 = new TreeNode(ans(6), opt(0));
        TreeNode star5_7 = new TreeNode(ans(7), opt(0));
        TreeNode star5_8 = new TreeNode(ans(8), opt(0));

        TreeNode star4_1 = new TreeNode(ans(1), opt(1));
        TreeNode star4_2 = new TreeNode(ans(2), opt(1));
        TreeNode star4_3 = new TreeNode(ans(3), opt(1));
        TreeNode star4_4 = new TreeNode(ans(4), opt(1));
        TreeNode star4_5 = new TreeNode(ans(5), opt(1));
        TreeNode star4_6 = new TreeNode(ans(6), opt(1));
        TreeNode star4_7 = new TreeNode(ans(7), opt(1));
        TreeNode star4_8 = new TreeNode(ans(8), opt(1));

        TreeNode star3_1 = new TreeNode(ans(1), opt(2));
        TreeNode star3_2 = new TreeNode(ans(2), opt(2));
        TreeNode star3_3 = new TreeNode(ans(3), opt(2));
        TreeNode star3_4 = new TreeNode(ans(4), opt(2));
        TreeNode star3_5 = new TreeNode(ans(5), opt(2));
        TreeNode star3_6 = new TreeNode(ans(6), opt(2));
        TreeNode star3_7 = new TreeNode(ans(7), opt(2));
        TreeNode star3_8 = new TreeNode(ans(8), opt(2));

        TreeNode star2_1 = new TreeNode(ans(1), opt(3));
        TreeNode star2_2 = new TreeNode(ans(2), opt(3));
        TreeNode star2_3 = new TreeNode(ans(3), opt(3));
        TreeNode star2_4 = new TreeNode(ans(4), opt(3));
        TreeNode star2_5 = new TreeNode(ans(5), opt(3));
        TreeNode star2_6 = new TreeNode(ans(6), opt(3));
        TreeNode star2_7 = new TreeNode(ans(7), opt(3));
        TreeNode star2_8 = new TreeNode(ans(8), opt(3));

        TreeNode star1_1 = new TreeNode(ans(1), opt(4));
        TreeNode star1_2 = new TreeNode(ans(2), opt(4));
        TreeNode star1_3 = new TreeNode(ans(3), opt(4));
        TreeNode star1_4 = new TreeNode(ans(4), opt(4));
        TreeNode star1_5 = new TreeNode(ans(5), opt(4));
        TreeNode star1_6 = new TreeNode(ans(6), opt(4));
        TreeNode star1_7 = new TreeNode(ans(7), opt(4));
        TreeNode star1_8 = new TreeNode(ans(8), opt(4));

        TreeNode star0_1 = new TreeNode(ans(1), opt(5));
        TreeNode star0_2 = new TreeNode(ans(2), opt(5));
        TreeNode star0_3 = new TreeNode(ans(3), opt(5));
        TreeNode star0_4 = new TreeNode(ans(4), opt(5));
        TreeNode star0_5 = new TreeNode(ans(5), opt(5));
        TreeNode star0_6 = new TreeNode(ans(6), opt(5));
        TreeNode star0_7 = new TreeNode(ans(7), opt(5));
        TreeNode star0_8 = new TreeNode(ans(8), opt(5));

        TreeNode root = new TreeNode(ans(0));
        root.addChild(star5_1);
        root.addChild(star4_1);
        root.addChild(star3_1);
        root.addChild(star2_1);
        root.addChild(star1_1);
        root.addChild(star0_1);

        for (TreeNode layer1 : root.getChildren()) {
            if(!layer1.getChildren().isEmpty()){
                break;
            }
            layer1.addChild(star5_2);
            layer1.addChild(star4_2);
            layer1.addChild(star3_2);
            layer1.addChild(star2_2);
            layer1.addChild(star1_2);
            layer1.addChild(star0_2);
            for (TreeNode layer2 : layer1.getChildren()) {
                if(!layer2.getChildren().isEmpty()){
                    break;
                }
                layer2.addChild(star5_3);
                layer2.addChild(star4_3);
                layer2.addChild(star3_3);
                layer2.addChild(star2_3);
                layer2.addChild(star1_3);
                layer2.addChild(star0_3);
                for (TreeNode layer3 : layer2.getChildren()) {
                    if(!layer3.getChildren().isEmpty()){
                        break;
                    }
                    layer3.addChild(star5_4);
                    layer3.addChild(star4_4);
                    layer3.addChild(star3_4);
                    layer3.addChild(star2_4);
                    layer3.addChild(star1_4);
                    layer3.addChild(star0_4);
                    for (TreeNode layer4 : layer3.getChildren()) {
                        if(!layer4.getChildren().isEmpty()){
                            break;
                        }
                        layer4.addChild(star5_5);
                        layer4.addChild(star4_5);
                        layer4.addChild(star3_5);
                        layer4.addChild(star2_5);
                        layer4.addChild(star1_5);
                        layer4.addChild(star0_5);
                        for (TreeNode layer5 : layer4.getChildren()) {
                            if(!layer5.getChildren().isEmpty()){
                                break;
                            }
                            layer5.addChild(star5_6);
                            layer5.addChild(star4_6);
                            layer5.addChild(star3_6);
                            layer5.addChild(star2_6);
                            layer5.addChild(star1_6);
                            layer5.addChild(star0_6);
                            for (TreeNode layer6 : layer5.getChildren()) {
                                if(!layer6.getChildren().isEmpty()){
                                    break;
                                }
                                layer6.addChild(star5_7);
                                layer6.addChild(star4_7);
                                layer6.addChild(star3_7);
                                layer6.addChild(star2_7);
                                layer6.addChild(star1_7);
                                layer6.addChild(star0_7);
                                for (TreeNode layer7 : layer6.getChildren()) {
                                    if(!layer7.getChildren().isEmpty()){
                                        break;
                                    }
                                    layer7.addChild(star5_8);
                                    layer7.addChild(star4_8);
                                    layer7.addChild(star3_8);
                                    layer7.addChild(star2_8);
                                    layer7.addChild(star1_8);
                                    layer7.addChild(star0_8);
                                    for(TreeNode layer8 : layer7.getChildren()) {
                                        if(layer8.getChildren().isEmpty()) {
                                            layer8.addLeaf(opt(6), END);
                                            layer8.addLeaf(opt(7), END);
                                            layer8.addLeaf(opt(8), END);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        builder.setAnswerRoot(root);
        builder.setYOffset(-30);
        Minecraft.getInstance().setScreen(builder.build());
    }

    /**
     * 处理评分，发包给服务器统计。
     */
    @Override
    public void handleNpcInteraction(ServerPlayer player, byte interactionID) {
        if(interactionID == END) {
            this.explodeAndDiscard();//TODO 补个帅气的离场
            DataManager.findBBQHint.put(player, true);
        }
        this.setConversingPlayer(null);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable(this.getType().getDescriptionId());
    }
}

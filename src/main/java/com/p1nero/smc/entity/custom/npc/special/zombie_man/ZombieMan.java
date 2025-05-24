package com.p1nero.smc.entity.custom.npc.special.zombie_man;

import com.p1nero.smc.client.gui.TreeNode;
import com.p1nero.smc.client.gui.screen.LinkListStreamDialogueScreenBuilder;
import com.p1nero.smc.entity.SMCEntities;
import com.p1nero.smc.entity.custom.npc.SMCNpc;
import com.p1nero.smc.network.PacketRelay;
import com.p1nero.smc.network.SMCPacketHandler;
import com.p1nero.smc.network.packet.clientbound.NPCDialoguePacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ZombieMan extends SMCNpc {
    private static final byte END = 1;

    public ZombieMan(EntityType<? extends Villager> entityType, Level level) {
        super(entityType, level);
    }

    public ZombieMan(Player player, Vec3 pos){
        super(SMCEntities.ZOMBIE_MAN.get(), player.level());
        this.setPos(pos);
        this.setOwner(player);
    }

    @Override
    public void playAmbientSound() {
    }

    @Nullable
    @Override
    public SoundEvent getAmbientSound() {
        return SoundEvents.ZOMBIE_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource source) {
        return SoundEvents.ZOMBIE_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ZOMBIE_DEATH;
    }

    @Override
    public @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
        this.playSound(SoundEvents.ZOMBIE_AMBIENT);
        if (player instanceof ServerPlayer serverPlayer && this.getOwner() == serverPlayer) {
            this.lookAt(player, 180.0F, 180.0F);
            if (this.getConversingPlayer() == null) {
                CompoundTag compoundTag = new CompoundTag();
                PacketRelay.sendToPlayer(SMCPacketHandler.INSTANCE, new NPCDialoguePacket(this.getId(), getDialogData(compoundTag, serverPlayer)), serverPlayer);
                this.setConversingPlayer(serverPlayer);
            }
        }
        return InteractionResult.sidedSuccess(level().isClientSide);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void openDialogueScreen(CompoundTag senderData) {
        LinkListStreamDialogueScreenBuilder builder = new LinkListStreamDialogueScreenBuilder(this);
        LocalPlayer localPlayer = Minecraft.getInstance().player;
        if (localPlayer == null) {
            return;
        }
        TreeNode root = new TreeNode(ans(0, localPlayer.getDisplayName()));

        TreeNode part3 = new TreeNode(ans(6), opt(7))
                .addLeaf(opt(8), END);

        TreeNode part2 = new TreeNode(ans(2), opt(2))
                .addChild(new TreeNode(ans(3), opt(3))
                        .addChild(new TreeNode(ans(4), opt(4))
                                .addChild(new TreeNode(ans(5), opt(5))
                                        .addChild(part3))
                                .addChild(new TreeNode(ans(5), opt(6))
                                        .addChild(part3))
                        )
                );

        root.addChild(new TreeNode(ans(1), opt(0))
                        .addChild(part2))
                .addChild(new TreeNode(ans(1), opt(1))
                        .addChild(part2));

        builder.setAnswerRoot(root);
        Minecraft.getInstance().setScreen(builder.build());
    }

    @Override
    public void handleNpcInteraction(ServerPlayer player, byte interactionID) {
        if (interactionID == END) {
            this.explodeAndDiscard();
        }
        this.setConversingPlayer(null);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable(this.getType().getDescriptionId());
    }

}

package com.p1nero.smc.network.packet.serverbound;

import com.p1nero.smc.SkilletManCoreMod;
import com.p1nero.smc.archive.DataManager;
import com.p1nero.smc.capability.SMCPlayer;
import com.p1nero.smc.client.gui.screen.entity_dialog.VillagerDialogScreenHandler;
import com.p1nero.smc.client.gui.screen.entity_dialog.animal.CatDialogScreenHandler;
import com.p1nero.smc.client.gui.screen.entity_dialog.golem.IronGolemDialogScreenHandler;
import com.p1nero.smc.client.gui.screen.entity_dialog.profession_dialog.WanderingTraderDialogBuilder;
import com.p1nero.smc.client.sound.SMCSounds;
import com.p1nero.smc.entity.api.NpcDialogue;
import com.p1nero.smc.network.packet.BasePacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;

/**
 * This packet is sent to the server whenever the player chooses an important action in the NPC dialogue.
 */
public record NpcPlayerInteractPacket(int entityID, byte interactionID) implements BasePacket {
    public static final int DO_NOTHING = -1;
    public static final int NO_ENTITY = -1;
    public static final byte SET_HARD_SPATULA = 1;
    public static final byte SET_EAZY_SPATULA= 2;
    public static final byte FAST_KILL_BOSS= 3;
    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(this.entityID());
        buf.writeByte(this.interactionID());
    }

    public static NpcPlayerInteractPacket decode(FriendlyByteBuf buf) {
        return new NpcPlayerInteractPacket(buf.readInt(), buf.readByte());
    }

    @Override
    public void execute(@Nullable Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            Entity entity = player.level().getEntity(this.entityID());
            if (entity instanceof NpcDialogue npc){
                npc.handleNpcInteraction(serverPlayer, this.interactionID());
            } else {
                //普通村民的对话统一在此处理
                if(entity instanceof Villager villager){
                    VillagerDialogScreenHandler.handle(serverPlayer, villager, interactionID);
                }

                if(entity instanceof WanderingTrader wanderingTrader) {
                    WanderingTraderDialogBuilder.getInstance().handle(serverPlayer, wanderingTrader, interactionID);
                }

                if(entity instanceof Cat cat) {
                    CatDialogScreenHandler.handle(serverPlayer, cat, interactionID);
                }

                if(entity instanceof IronGolem ironGolem) {
                    IronGolemDialogScreenHandler.handle(serverPlayer, ironGolem, interactionID);
                }

            }

            if(this.entityID == NO_ENTITY) {
                if(interactionID == SET_HARD_SPATULA) {
                    DataManager.hardSpatulaMode.put(player, true);
                    player.displayClientMessage(SkilletManCoreMod.getInfo("set_to_hard_spatula"), true);
                    player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SMCSounds.VILLAGER_YES.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
                }
                if(interactionID == SET_EAZY_SPATULA) {
                    DataManager.hardSpatulaMode.put(player, false);
                    player.displayClientMessage(SkilletManCoreMod.getInfo("set_to_easy_spatula"), true);
                    player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SMCSounds.VILLAGER_YES.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
                }
                if(interactionID == FAST_KILL_BOSS) {
                    if(SMCPlayer.hasMoney(serverPlayer, 100000, true)) {
                        SMCPlayer.consumeMoney(100000, serverPlayer);
                        DataManager.fastKillBoss.put(serverPlayer, true);
                        serverPlayer.displayClientMessage(SkilletManCoreMod.getInfo("fast_kill_enable"), true);
                    }
                }
            }
        }
    }
}

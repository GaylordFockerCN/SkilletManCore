package com.p1nero.smc.network.packet.clientbound.helper;

import com.p1nero.smc.archive.DataManager;
import com.p1nero.smc.capability.SMCCapabilityProvider;
import com.p1nero.smc.client.gui.screen.SMCEndScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ServerboundClientCommandPacket;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SMCClientHandler {

    public static void syncBoolData(String key, boolean isLocked, boolean value) {
        if(Minecraft.getInstance().player != null && Minecraft.getInstance().level != null) {
            LocalPlayer player = Minecraft.getInstance().player;
            if (isLocked) {
                DataManager.putData(player, key + "isLocked", true);
                return;
            }
            DataManager.putData(player, key, value);
            DataManager.putData(player, key + "isLocked", false);
        }
    }

    public static void syncDoubleData(String key, boolean isLocked, double value) {
        if (Minecraft.getInstance().player != null && Minecraft.getInstance().level != null) {
            LocalPlayer player = Minecraft.getInstance().player;
            if (isLocked) {
                DataManager.putData(player, key + "isLocked", true);
                return;
            }
            DataManager.putData(player, key, value);
            DataManager.putData(player, key + "isLocked", false);
        }
    }

    public static void syncStringData(String key, boolean isLocked, String value){
        if (Minecraft.getInstance().player != null && Minecraft.getInstance().level != null) {
            LocalPlayer player = Minecraft.getInstance().player;
            if (isLocked) {
                DataManager.putData(player, key + "isLocked", true);
                return;
            }
            DataManager.putData(player, key, value);
            DataManager.putData(player, key + "isLocked", false);
        }
    }

    public static void syncSMCPlayer(CompoundTag compoundTag) {
        if(Minecraft.getInstance().player != null && Minecraft.getInstance().level != null) {
            SMCCapabilityProvider.getSMCPlayer(Minecraft.getInstance().player).loadNBTData(compoundTag);
        }
    }

    public static void openEndScreen() {
        if (Minecraft.getInstance().player != null && Minecraft.getInstance().level != null) {
            Minecraft.getInstance().setScreen(new SMCEndScreen(true, () -> {
                Minecraft.getInstance().player.connection.send(new ServerboundClientCommandPacket(ServerboundClientCommandPacket.Action.PERFORM_RESPAWN));
                Minecraft.getInstance().setScreen(null);
            }));
        }
    }
}

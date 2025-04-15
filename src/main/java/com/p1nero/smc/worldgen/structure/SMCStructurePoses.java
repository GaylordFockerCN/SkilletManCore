package com.p1nero.smc.worldgen.structure;

import com.p1nero.smc.util.EntityUtil;
import com.p1nero.smc.worldgen.dimension.SMCChunkGenerator;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;

import java.awt.*;
import java.util.List;

/**
 * 有新的建筑加进来只要在这里添加枚举类型即可，BiomeForceLandmark类和ChunkGenerator类会遍历这个枚举类型（顶级优化哈哈）
 * 以后看看能不能优化成json里面直接写类型而不是写数字（EnumCodec还不会）
 *
 * @author LZY
 * @see PositionPlacement
 * @see SMCChunkGenerator
 */
public enum SMCStructurePoses {

    //offset应为偏移的方块数量除以四。举例：FINAL偏移61格，应填15 。y填数据包里
    START_POINT(20, 0, 0, 0, 10, 0, 4);

    final int size;
    final int offsetX;
    final int offsetZ;
    final Point point;
    final Vec3i pos;
    final int maxPlayerCnt;

    SMCStructurePoses(int size, int offsetX, int offsetZ) {
        this(size, offsetX, offsetZ, 0, 0, 0, 4);
    }

    SMCStructurePoses(int size, int offsetX, int offsetZ, int x, int y, int z, int maxPlayerCnt) {
        this.size = size;
        this.offsetX = offsetX;
        this.offsetZ = offsetZ;
        this.point = new Point(x, z);
        this.pos = new Vec3i(x, y, z);
        this.maxPlayerCnt = maxPlayerCnt;
    }

    public int getSize() {
        return size;
    }

    public int getOffsetX() {
        return offsetX;
    }

    public int getOffsetZ() {
        return offsetZ;
    }

    public Vec3i getPos() {
        return pos;
    }

    public void teleportTo(ServerPlayer serverPlayer){
        serverPlayer.teleportTo(pos.getX(), pos.getY(), pos.getZ());
        List<Player> currentPlayers = EntityUtil.getNearByPlayers(serverPlayer, Math.max(offsetX, offsetZ));
        if(currentPlayers.size() > maxPlayerCnt) {
            serverPlayer.setGameMode(GameType.SPECTATOR);
            serverPlayer.displayClientMessage(Component.translatable("tip.dote.press_enter_to_exit"), false);
        } else {
            serverPlayer.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 60, 5));
        }
    }

}

package com.p1nero.smc.archive;

import com.p1nero.smc.SkilletManCoreMod;
import com.p1nero.smc.network.PacketRelay;
import com.p1nero.smc.network.SMCPacketHandler;
import com.p1nero.smc.network.packet.clientbound.BroadcastMessagePacket;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

import java.util.HashSet;

/**
 * 任务集合
 */
public class TaskSet extends HashSet<Task> {


    /**
     * 服务端添加就要发包同步一下
     */
    @Override
    public boolean add(Task task) {
        if(SMCArchiveManager.isNoPlotMode()){
            return false;
        }
        if (super.add(task)) {
            SMCArchiveManager.syncToClient();
            return true;
        }
        return false;
    }

    /**
     * @param sync 是否同步到客户端或服务端
     */
    public boolean add(Task task, boolean sync) {
        if(!sync){
            return super.add(task);
        }
        return this.add(task);
    }

    /**
     * 同步数据，并广播任务完成
     */
    @Override
    public boolean remove(Object o) {
        if (super.remove(o)) {
            Component message = SkilletManCoreMod.getInfo("task_finish0").append(((Task) o).getName().copy().withStyle(ChatFormatting.RED)).append(SkilletManCoreMod.getInfo("task_finish1"));
            PacketRelay.sendToAll(SMCPacketHandler.INSTANCE, new BroadcastMessagePacket(message, false));
            SMCArchiveManager.syncToClient();
            return true;
        }
        return false;
    }
}

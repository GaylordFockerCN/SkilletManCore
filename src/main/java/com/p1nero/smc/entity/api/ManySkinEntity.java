package com.p1nero.smc.entity.api;

/**
 * 本来是为了自己写的skinID服务的，结果后来才知道原版提供了一个可以同步服务端和客户端的实体数据。。。{@link net.minecraft.network.syncher.EntityDataAccessor}
 */
public interface ManySkinEntity {
    public void setSkinID(int id);

    public int getSkinID();
}

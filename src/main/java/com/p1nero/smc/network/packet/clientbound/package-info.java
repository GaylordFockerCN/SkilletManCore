/**
 * 从天境源码搬运过来延伸的，服务端发向客户端的数据。注意，在execute()中需要使用Minecraft.getInstance().player来获得客户端数据。
 * {@link com.p1nero.smc.network.packet.clientbound.EntityChangeSkinIDPacket} 和 {@link com.p1nero.smc.network.packet.clientbound.NPCDialoguePacketWithSkinID}不再被需要了...
 * @author LZY
 */
package com.p1nero.smc.network.packet.clientbound;
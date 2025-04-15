package com.p1nero.smc.mixin;

import com.p1nero.smc.SkilletManCoreMod;
import com.p1nero.smc.block.entity.BetterStructureBlockEntity;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ServerboundSetStructureBlockPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.StructureBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

/**
 * 通过mixin实现不显示客户端消息，使DisplayClientMessage的第一个参数为空。
 * 启用立即激活时默认关闭输出信息显示。
 * {@link ServerGamePacketListenerImpl#handleSetStructureBlock(ServerboundSetStructureBlockPacket)}
 * @author LZY
 */
@Mixin(ServerGamePacketListenerImpl.class)
public abstract class ServerGamePacketListenerImplMixin {

    @Shadow
    public ServerPlayer player;

    @Shadow public abstract ServerPlayer getPlayer();

    @Inject(method = "handleSetStructureBlock(Lnet/minecraft/network/protocol/game/ServerboundSetStructureBlockPacket;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/protocol/PacketUtils;ensureRunningOnSameThread(Lnet/minecraft/network/protocol/Packet;Lnet/minecraft/network/PacketListener;Lnet/minecraft/server/level/ServerLevel;)V", shift = At.Shift.AFTER), cancellable = true)
    private void injected(ServerboundSetStructureBlockPacket packet, CallbackInfo ci) {
        BlockPos blockpos = packet.getPos();
        BlockState blockstate = this.player.level().getBlockState(blockpos);
        BlockEntity blockentity = this.player.level().getBlockEntity(blockpos);
        if (blockentity instanceof BetterStructureBlockEntity blockEntity) {
            blockEntity.setMode(packet.getMode());
            blockEntity.setStructureName(packet.getName());
            blockEntity.setStructurePos(packet.getOffset());
            blockEntity.setStructureSize(packet.getSize());
            blockEntity.setMirror(packet.getMirror());
            blockEntity.setRotation(packet.getRotation());
            blockEntity.setMetaData(packet.getData());
            blockEntity.setIgnoreEntities(packet.isIgnoreEntities());
            blockEntity.setShowAir(packet.isShowAir());
            blockEntity.setShowBoundingBox(packet.isShowBoundingBox());
            blockEntity.setIntegrity(packet.getIntegrity());
            blockEntity.setSeed(packet.getSeed());
            if (blockEntity.hasStructureName()) {
                String s = blockEntity.getStructureName();
                if (packet.getUpdateType() == StructureBlockEntity.UpdateType.SAVE_AREA) {
                    if (blockEntity.saveStructure()) {
                        this.player.displayClientMessage(Component.translatable("structure_block.save_success", s), false);
                    } else {
                        this.player.displayClientMessage(Component.translatable("structure_block.save_failure", s), false);
                    }
                } else if (packet.getUpdateType() == StructureBlockEntity.UpdateType.LOAD_AREA) {
                    SkilletManCoreMod.LOGGER.info("try to load custom structure block on server: {}", blockEntity.getStructureName());
                    if (!blockEntity.isStructureLoadable()) {
                        this.player.displayClientMessage(Component.translatable("structure_block.load_not_found", s), false);
                    } else if (blockEntity.loadStructure(this.player.serverLevel())) {
//                        this.player.displayClientMessage(Component.translatable("structure_block.load_success", s), false);//取消输出信息
                        Level server = blockEntity.getLevel();
                        Objects.requireNonNull(server).destroyBlock(blockpos, false);//自毁
                        CommandSourceStack css = getPlayer().createCommandSourceStack().withPermission(2).withSuppressedOutput();
                        Objects.requireNonNull(server.getServer()).getCommands().performPrefixedCommand(css, "kill @e[type=item]");//清理物品
                    } else {
//                        this.player.displayClientMessage(Component.translatable("structure_block.load_prepare", s), false);//取消输出信息
                        blockEntity.loadStructure(this.player.serverLevel());
                        SkilletManCoreMod.LOGGER.info("try to load custom structure block AGAIN on server: {}", blockEntity.getStructureName());
                    }
                } else if (packet.getUpdateType() == StructureBlockEntity.UpdateType.SCAN_AREA) {
                    if (blockEntity.detectSize()) {
                        this.player.displayClientMessage(Component.translatable("structure_block.size_success", s), false);
                    } else {
                        this.player.displayClientMessage(Component.translatable("structure_block.size_failure"), false);
                    }
                }
            } else {
                this.player.displayClientMessage(Component.translatable("structure_block.invalid_structure_name", new Object[]{packet.getName()}), false);
            }

            blockEntity.setChanged();
            this.player.level().sendBlockUpdated(blockpos, blockstate, blockstate, 3);
            ci.cancel();
        }

    }

}

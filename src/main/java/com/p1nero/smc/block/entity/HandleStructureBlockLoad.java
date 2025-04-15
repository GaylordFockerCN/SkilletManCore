package com.p1nero.smc.block.entity;

import com.p1nero.smc.SkilletManCoreMod;
import net.minecraft.client.Minecraft;
import net.minecraft.network.protocol.game.ServerboundSetStructureBlockPacket;
import net.minecraft.world.level.block.entity.StructureBlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Objects;

@OnlyIn(Dist.CLIENT)
public class HandleStructureBlockLoad {
    public static void load(StructureBlockEntity entity){
        if(Minecraft.getInstance().level != null && Minecraft.getInstance().player != null){
            Objects.requireNonNull(Minecraft.getInstance().getConnection()).send(new ServerboundSetStructureBlockPacket(entity.getBlockPos(), StructureBlockEntity.UpdateType.LOAD_AREA, entity.getMode(), entity.getStructureName(), entity.getStructurePos(), entity.getStructureSize(), entity.getMirror(), entity.getRotation(), entity.getMetaData(), entity.isIgnoreEntities(), entity.getShowAir(), entity.getShowBoundingBox(), entity.getIntegrity(), entity.getSeed()));
            SkilletManCoreMod.LOGGER.info("post load request : {} ", entity.getStructureName());
        }
    }
}

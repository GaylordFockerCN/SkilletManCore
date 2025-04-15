package com.p1nero.smc.entity.custom.npc.start_npc;

import com.p1nero.smc.client.gui.screen.LinkListStreamDialogueScreenBuilder;
import com.p1nero.smc.entity.custom.npc.SMCNpc;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;

/**
 * 凝渊人，引导的npc
 */
public class StartNPC extends SMCNpc {

    public StartNPC(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public void openDialogueScreen(CompoundTag senderData) {
        LinkListStreamDialogueScreenBuilder builder = new LinkListStreamDialogueScreenBuilder(this);

        builder.start(0)
                .addChoice(0, 1)
                .addChoice(1, 2)
                .addChoice(2, 3)
                .addFinalChoice(9, (byte) 1);

        if(!builder.isEmpty()){
            Minecraft.getInstance().setScreen(builder.build());
        }
    }

    /**
     * 推不动
     */
    @Override
    public boolean isPushable() {
        return false;
    }

}

package com.p1nero.smc.capability;

import com.p1nero.smc.SkilletManCoreMod;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Mod.EventBusSubscriber(modid = SkilletManCoreMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SMCCapabilityProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {

    public static Capability<SMCPlayer> DOTE_PLAYER = CapabilityManager.get(new CapabilityToken<>() {});

    private SMCPlayer SMCPlayer = null;
    
    private final LazyOptional<SMCPlayer> optional = LazyOptional.of(this::createTCRPlayer);

    private SMCPlayer createTCRPlayer() {
        if(this.SMCPlayer == null){
            this.SMCPlayer = new SMCPlayer();
        }

        return this.SMCPlayer;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction direction) {
        if(capability == DOTE_PLAYER){
            return optional.cast();
        }

        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        createTCRPlayer().saveNBTData(tag);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        createTCRPlayer().loadNBTData(tag);
    }

    @Mod.EventBusSubscriber(modid = SkilletManCoreMod.MOD_ID)
    public static class Registration {
        @SubscribeEvent
        public static void attachEntityCapabilities(AttachCapabilitiesEvent<Entity> event) {
            if (event.getObject() instanceof Player) {
               if(!event.getObject().getCapability(SMCCapabilityProvider.DOTE_PLAYER).isPresent()){
                   event.addCapability(new ResourceLocation(SkilletManCoreMod.MOD_ID, "tcr_player"), new SMCCapabilityProvider());
               }
            }
        }

        @SubscribeEvent
        public static void onPlayerCloned(PlayerEvent.Clone event) {
            event.getOriginal().reviveCaps();//。。。怎么之前没加这个也可以，现在没加不行
            if(event.isWasDeath()) {
                event.getOriginal().getCapability(SMCCapabilityProvider.DOTE_PLAYER).ifPresent(oldStore -> {
                    event.getEntity().getCapability(SMCCapabilityProvider.DOTE_PLAYER).ifPresent(newStore -> {
                        newStore.copyFrom(oldStore);
                    });
                });
            }
        }

        @SubscribeEvent
        public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
            event.register(SMCPlayer.class);
        }

    }


}

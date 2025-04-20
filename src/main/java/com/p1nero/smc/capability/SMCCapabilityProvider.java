package com.p1nero.smc.capability;

import com.p1nero.smc.SkilletManCoreMod;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Mod.EventBusSubscriber(modid = SkilletManCoreMod.MOD_ID)
public class SMCCapabilityProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {

    public static Capability<SMCPlayer> SMC_PLAYER = CapabilityManager.get(new CapabilityToken<>() {});

    private SMCPlayer SMCPlayer = null;
    
    private final LazyOptional<SMCPlayer> optional = LazyOptional.of(this::createSMCPlayer);

    private SMCPlayer createSMCPlayer() {
        if(this.SMCPlayer == null){
            this.SMCPlayer = new SMCPlayer();
        }

        return this.SMCPlayer;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction direction) {
        if(capability == SMC_PLAYER){
            return optional.cast();
        }

        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        createSMCPlayer().saveNBTData(tag);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        createSMCPlayer().loadNBTData(tag);
    }

    @SubscribeEvent
    public static void attachEntityCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player player) {
            if(!player.getCapability(SMCCapabilityProvider.SMC_PLAYER).isPresent()){
                event.addCapability(new ResourceLocation(SkilletManCoreMod.MOD_ID, "smc_player"), new SMCCapabilityProvider());
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event) {
        event.getOriginal().reviveCaps();
        if(event.isWasDeath()) {
            event.getOriginal().getCapability(SMCCapabilityProvider.SMC_PLAYER).ifPresent(oldStore -> {
                event.getEntity().getCapability(SMCCapabilityProvider.SMC_PLAYER).ifPresent(newStore -> {
                    newStore.copyFrom(oldStore);
                    newStore.syncToClient(((ServerPlayer) event.getEntity()));
                });
            });
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        getSMCPlayer(event.player).tick(event.player);
    }

    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        event.register(SMCPlayer.class);
    }

    public static SMCPlayer getSMCPlayer(Player player) {
        return player.getCapability(SMCCapabilityProvider.SMC_PLAYER).orElse(new SMCPlayer());
    }

    public static void syncPlayerDataToClient(ServerPlayer serverPlayer) {
        getSMCPlayer(serverPlayer).syncToClient(serverPlayer);
    }

}

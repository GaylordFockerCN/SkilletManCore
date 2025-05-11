package com.p1nero.smc.mixin;

import hungteen.htlib.api.interfaces.raid.IRaidComponent;
import hungteen.htlib.api.interfaces.raid.IWaveComponent;
import hungteen.htlib.common.world.raid.AbstractRaid;
import hungteen.htlib.util.helper.PlayerHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static hungteen.htlib.common.world.raid.AbstractRaid.RAID_WARN;

@Mixin(value = AbstractRaid.class, remap = false)
public abstract class AbstractRaidMixin {

    @Shadow(remap = false) protected abstract void onLoss();

    @Shadow protected int tick;

    @Shadow protected int stopTick;

    @Shadow protected abstract void updatePlayers();

    @Shadow protected abstract void updateRaiders();

    @Shadow protected abstract void tickProgressBar();

    @Shadow protected abstract boolean needStop();

    @Shadow public abstract List<Entity> getDefenders();

    @Shadow public abstract void workTick(@NotNull IRaidComponent raid, @NotNull IWaveComponent wave);

    @Inject(method = "joinRaid", at = @At("HEAD"))
    private void smc$joinRaid(int wave, Entity raider, CallbackInfo ci) {
        raider.setGlowingTag(true);
    }

    @Inject(method = "validTick", at = @At("HEAD"), cancellable = true)
    private void smc$validTick(IRaidComponent raid, IWaveComponent wave, CallbackInfo ci){
        if (this.tick % 20 == 0 || this.stopTick % 10 == 5) {
            this.updatePlayers();
            this.updateRaiders();
        }

        this.tickProgressBar();
        if (this.needStop()) {
            if (this.stopTick == 1 && raid.sendRaidWarn()) {
                Stream<?> defenders = this.getDefenders().stream();
                Objects.requireNonNull(Player.class);
                defenders = defenders.filter(Player.class::isInstance);
                Objects.requireNonNull(Player.class);
                defenders.map(Player.class::cast).forEach((player) -> {
                    PlayerHelper.sendMsgTo(player, RAID_WARN);
                });
            }

            if (++this.stopTick >= 200) {
                this.onLoss();
            }

        } else {
            if (this.stopTick > 0) {
                this.stopTick = 0;
            }

            this.workTick(raid, wave);
        }
        ci.cancel();
    }

}

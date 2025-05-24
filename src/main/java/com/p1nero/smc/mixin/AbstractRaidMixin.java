package com.p1nero.smc.mixin;

import hungteen.htlib.api.interfaces.raid.IRaidComponent;
import hungteen.htlib.api.interfaces.raid.IWaveComponent;
import hungteen.htlib.common.world.entity.DummyEntity;
import hungteen.htlib.common.world.entity.DummyEntityType;
import hungteen.htlib.common.world.raid.AbstractRaid;
import hungteen.htlib.util.helper.PlayerHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
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
public abstract class AbstractRaidMixin extends DummyEntity {

    public AbstractRaidMixin(DummyEntityType<?> entityType, ServerLevel level, Vec3 position) {
        super(entityType, level, position);
    }

    public AbstractRaidMixin(DummyEntityType<?> entityType, Level level, CompoundTag tag) {
        super(entityType, level, tag);
    }

    @Shadow(remap = false) protected abstract void onLoss();

    @Shadow protected int tick;

    @Shadow protected int stopTick;

    @Shadow protected abstract void updatePlayers();

    @Shadow protected abstract void updateRaiders();

    @Shadow protected abstract void tickProgressBar();

    @Shadow protected abstract boolean needStop();

    @Shadow public abstract List<Entity> getDefenders();

    @Shadow public abstract void workTick(@NotNull IRaidComponent raid, @NotNull IWaveComponent wave);

    @Shadow public abstract List<Entity> getRaiders();

    @Inject(method = "joinRaid", at = @At("HEAD"))
    private void smc$joinRaid(int wave, Entity raider, CallbackInfo ci) {
        raider.setGlowingTag(true);
    }

    @Inject(method = "validTick", at = @At("HEAD"), cancellable = true)
    private void smc$validTick(IRaidComponent raid, IWaveComponent wave, CallbackInfo ci){

        //把袭击者抓回来
        for(Entity raider : this.getRaiders()) {
            if(raider.position().distanceTo(this.getPosition()) > raid.getRaidRange()) {
                raider.setDeltaMovement(this.getPosition().subtract(raider.position()).normalize());
            }
        }

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

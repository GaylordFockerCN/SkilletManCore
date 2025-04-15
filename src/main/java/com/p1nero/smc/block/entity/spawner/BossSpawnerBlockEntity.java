package com.p1nero.smc.block.entity.spawner;

import com.p1nero.smc.SkilletManCoreMod;
import com.p1nero.smc.entity.api.HomePointEntity;
import com.p1nero.smc.entity.custom.boss.SMCBoss;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import yesman.epicfight.world.entity.WitherGhostClone;

public abstract class BossSpawnerBlockEntity<T extends SMCBoss> extends EntitySpawnerBlockEntity<T> {
	protected BossSpawnerBlockEntity(BlockEntityType<?> type, EntityType<T> entityType, BlockPos pos, BlockState state) {
		super(type, entityType, pos, state);
	}

	public static void tick(Level pLevel, BlockPos pPos, BlockState state, BlockEntity blockEntity) {
		if(blockEntity instanceof BossSpawnerBlockEntity<?> spawnerBlockEntity){
			if(pLevel instanceof ServerLevel serverLevel){
				if(spawnerBlockEntity.getSpawnerParticle() != null){
					double rx = pPos.getX() + pLevel.getRandom().nextFloat();
					double ry = pPos.getY() + 1 + pLevel.getRandom().nextFloat();
					double rz = pPos.getZ() + pLevel.getRandom().nextFloat();
					serverLevel.sendParticles(spawnerBlockEntity.getSpawnerParticle(), rx, ry, rz ,1, 0.0D, 0.0D, 0.0D, 0);
				}
				if(spawnerBlockEntity.myEntity instanceof HomePointEntity homePointEntity){
					if(spawnerBlockEntity.getBorderParticle() != null && spawnerBlockEntity.myEntity != null){
						for (int angle = 0; angle < 360; angle += 2) {
							double radians = Math.toRadians(angle);
							int xOffset = (int) Math.round(homePointEntity.getHomeRadius() * Math.cos(radians));
							int zOffset = (int) Math.round(homePointEntity.getHomeRadius() * Math.sin(radians));
							serverLevel.sendParticles(spawnerBlockEntity.getBorderParticle(), pPos.getX() + xOffset, pPos.getY() + 1, pPos.getZ() + zOffset, 1, 0.0D, 0.1D, 0.0D, 0.01);
						}
					}

					int r = (int) (homePointEntity.getHomeRadius() + 3);//要比实际的大一点点，防止在边缘偷刀
					//弹开怪物和多余玩家
					for(LivingEntity livingEntity : pLevel.getEntitiesOfClass(LivingEntity.class, new AABB(pPos.offset(-r, -r, -r), pPos.offset(r, r, r)))){
						if(spawnerBlockEntity.currentPlayer != null && livingEntity.getUUID().equals(spawnerBlockEntity.currentPlayer)){
							continue;
						}
						//跳过神王远程攻击物
						if(livingEntity instanceof WitherGhostClone){
							continue;
						}
						//同步boss，以防死后boss对象丢失
						if((spawnerBlockEntity.entityType.equals(livingEntity.getType())) && livingEntity instanceof SMCBoss){
							spawnerBlockEntity.myEntity = livingEntity;
							continue;
						}
						livingEntity.setDeltaMovement(livingEntity.position().subtract(pPos.getCenter()).normalize());
						if(livingEntity instanceof Player player){
							player.setHealth(player.getHealth() - 0.25F);//setDeltaMovement对玩家无效、？
							player.displayClientMessage(SkilletManCoreMod.getInfo("tip9"), false);
						}
					}
					//防止玩家丢失
					if(spawnerBlockEntity.getCurrentPlayer() == null || serverLevel.getPlayerByUUID(spawnerBlockEntity.getCurrentPlayer()) == null){
						serverLevel.sendParticles(ParticleTypes.EXPLOSION, spawnerBlockEntity.myEntity.getX(), spawnerBlockEntity.myEntity.getY(), spawnerBlockEntity.myEntity.getZ(), 1, 0.0D, 0.1D, 0.0D, 0.01);
						spawnerBlockEntity.myEntity.discard();
						spawnerBlockEntity.myEntity = null;
					} else {
						//玩家在圈上则扣血惩罚
						Player player = serverLevel.getPlayerByUUID(spawnerBlockEntity.getCurrentPlayer());
						if(player != null && player.position().distanceTo(pPos.getCenter()) > homePointEntity.getHomeRadius()
								&& player.position().distanceTo(pPos.getCenter()) < homePointEntity.getHomeRadius() + 3){
							player.hurt(spawnerBlockEntity.myEntity.damageSources().magic(), 1);
							player.displayClientMessage(SkilletManCoreMod.getInfo("tip9"), true);
						}
					}
				}

				//击败boss清空状态
				if(spawnerBlockEntity.myEntity == null || !spawnerBlockEntity.myEntity.isAlive()){
					spawnerBlockEntity.myEntity = null;
					spawnerBlockEntity.currentPlayer = null;
                }
			}
		}

	}

}

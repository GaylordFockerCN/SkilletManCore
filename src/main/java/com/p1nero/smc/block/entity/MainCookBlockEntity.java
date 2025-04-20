package com.p1nero.smc.block.entity;

import com.p1nero.smc.SkilletManCoreMod;
import com.p1nero.smc.block.SMCBlockEntities;
import com.p1nero.smc.block.custom.INpcDialogueBlock;
import com.p1nero.smc.capability.SMCCapabilityProvider;
import com.p1nero.smc.capability.SMCPlayer;
import com.p1nero.smc.client.gui.screen.LinkListStreamDialogueScreenBuilder;
import com.p1nero.smc.client.sound.SMCSounds;
import com.p1nero.smc.entity.custom.npc.customer.Customer;
import com.p1nero.smc.entity.custom.npc.start_npc.StartNPC;
import com.p1nero.smc.event.ServerEvents;
import com.p1nero.smc.network.PacketRelay;
import com.p1nero.smc.network.SMCPacketHandler;
import com.p1nero.smc.network.packet.clientbound.NPCBlockDialoguePacket;
import dev.xkmc.cuisinedelight.content.item.CuisineSkilletItem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.common.registry.ModBlocks;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class MainCookBlockEntity extends BlockEntity implements INpcDialogueBlock {
    @Nullable
    private StartNPC startNPC;
    private boolean isWorking;
    public static final int WORKING_RADIUS = 8;
    private final List<Customer> customers = new ArrayList<>();
    private static final List<VillagerProfession> PROFESSION_LIST = ForgeRegistries.VILLAGER_PROFESSIONS.getValues().stream().toList();

    public MainCookBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(SMCBlockEntities.MAIN_COOK_BLOCK_ENTITY.get(), blockPos, blockState);
    }

    public @Nullable StartNPC getStartNPC() {
        return startNPC;
    }

    public boolean isWorking() {
        return isWorking;
    }

    public static <T extends BlockEntity> void tick(Level level, BlockPos pos, BlockState state, T t) {
        if(level.isClientSide) {
            return;
        }
        if (t instanceof MainCookBlockEntity mainCookBlockEntity) {
            if (mainCookBlockEntity.startNPC == null) {
                //优先附近找，找不到再生一只。
                int offset = 3;
                StartNPC startNPC = level.getNearestEntity(StartNPC.class, TargetingConditions.DEFAULT, null, pos.getX(), pos.getY(), pos.getZ(), new AABB(pos.offset(offset, offset, offset), pos.offset(-offset, -offset, -offset)));
                if (startNPC != null) {
                    //互相通知
                    mainCookBlockEntity.startNPC = startNPC;
                    startNPC.setHomePos(pos);
                } else {
                    StartNPC startNPC1 = new StartNPC(((ServerLevel) level), pos.above(3));
                    //互相通知
                    mainCookBlockEntity.startNPC = startNPC1;
                    startNPC1.setHomePos(pos);
                    startNPC1.setVillagerData(startNPC1.getVillagerData().setType(VillagerType.byBiome(startNPC1.level().getBiome(pos))));
                    level.addFreshEntity(startNPC1);
                }
            }

            if (mainCookBlockEntity.startNPC.isGuider()) {
                LivingEntity owner = mainCookBlockEntity.startNPC.getOwner();
                if (owner instanceof ServerPlayer serverPlayer && owner.isAlive()) {
                    if(mainCookBlockEntity.isWorking) {
                        mainCookBlockEntity.workingTick(serverPlayer);
                        mainCookBlockEntity.isWorking = mainCookBlockEntity.checkWorkingTime();
                        mainCookBlockEntity.updateWorkingState(serverPlayer);
                    } else {
                        //检查上班时间
                        if(mainCookBlockEntity.checkWorkingTime() && owner.level().getBlockState(pos.above(1)).is(ModBlocks.STOVE.get()) && owner.level().getBlockState(pos.above(2)).getBlock().asItem() instanceof CuisineSkilletItem) {
                            mainCookBlockEntity.isWorking = true;
                            mainCookBlockEntity.updateWorkingState(serverPlayer);
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag compoundTag) {
        super.saveAdditional(compoundTag);
        compoundTag.putBoolean("isWorking", isWorking);
    }

    @Override
    public void load(@NotNull CompoundTag compoundTag) {
        super.load(compoundTag);
        isWorking = compoundTag.getBoolean("isWorking");
    }

    public void updateWorkingState(ServerPlayer serverPlayer) {
        if (this.isWorking) {
            SMCPlayer.updateWorkingState(true, serverPlayer);
        } else {
            SMCPlayer.updateWorkingState(false, serverPlayer);
            this.clearCustomers();
        }
    }

    public void workingTick(ServerPlayer owner){
        //抓回来上班
        if(this.getBlockPos().getCenter().distanceTo(owner.position()) > WORKING_RADIUS && !this.canPlayerLeave(owner) && this.startNPC != null) {
            Vec3 targetPos = startNPC.getSpawnPos().getCenter();
            owner.teleportTo(targetPos.x, targetPos.y, targetPos.z);
            owner.playSound(SoundEvents.VILLAGER_NO);
            CompoundTag tag = new CompoundTag();
            tag.putBoolean("is_catching_escaping_player", true);
            PacketRelay.sendToPlayer(SMCPacketHandler.INSTANCE, new NPCBlockDialoguePacket(this.getBlockPos(), tag), owner);
            owner.serverLevel().playSound(null, owner.getX(), owner.getY(), owner.getZ(), SoundEvents.VILLAGER_NO, owner.getSoundSource(), 1.0F, 1.0F);
        }

        //生成顾客，20s一只，最多6只
        this.customers.removeIf(customer -> customer == null || customer.isRemoved() || !customer.isAlive());
        if(this.customers.size() < 6 && owner.tickCount % 300 == 0) {
            BlockPos centerPos = this.getBlockPos();
            double centerX = centerPos.getX() + 0.5;
            double centerZ = centerPos.getZ() + 0.5;

            double angle = Math.random() * 2 * Math.PI;
            double radius = owner.getRandom().nextInt(15, 20);

            double spawnX = centerX + Math.cos(angle) * radius;
            double spawnZ = centerZ + Math.sin(angle) * radius;

            BlockPos spawnPos = ServerEvents.getSurfaceBlockPos(owner.serverLevel(), (int) spawnX, (int) spawnZ);
            Customer customer = new Customer(owner, spawnPos.getCenter());
            customer.setHomePos(this.getBlockPos());
            customer.setSpawnPos(spawnPos);
            customer.getNavigation().moveTo(customer.getNavigation().createPath(this.getBlockPos(), 3), 1.0);
            VillagerProfession profession = PROFESSION_LIST.get(customer.getRandom().nextInt(PROFESSION_LIST.size()));//随机抽个职业，换皮肤好看
            customer.setVillagerData(customer.getVillagerData().setType(VillagerType.byBiome(owner.serverLevel().getBiome(this.getBlockPos()))).setProfession(profession));
            this.customers.add(customer);
            owner.serverLevel().addFreshEntity(customer);
        }
    }

    public void clearCustomers(){
        Iterator<Customer> iterator = customers.iterator();
        while (iterator.hasNext()) {
            iterator.next().setTraded(true);//遣散
            iterator.remove();
        }
    }

    public void summonRaidFor(ServerPlayer serverPlayer){
        clearCustomers();
        CommandSourceStack commandSourceStack = serverPlayer.createCommandSourceStack();
        Objects.requireNonNull(serverPlayer.getServer()).getCommands().performPrefixedCommand(commandSourceStack, "s");

    }

    /**
     * 点击顶上的炉子的时候
     * 对话，发起进阶挑战以继续迎客
     */
    public void onClickStove(ServerPlayer serverPlayer, BlockPos below, Direction face, InteractionHand hand) {
        SMCPlayer smcPlayer = SMCCapabilityProvider.getSMCPlayer(serverPlayer);
        int stage = smcPlayer.getStage();

    }

    public void onNPCFinishTrade(Customer customer){
        this.customers.remove(customer);
    }

    public void onSkilletPlace(ServerPlayer serverPlayer) {
        if(this.startNPC == null) {
            return;
        }
        if(!serverPlayer.getUUID().equals(this.startNPC.getOwnerUUID())) {
            SMCPlayer.updateWorkingState(false, serverPlayer);
            this.clearCustomers();
            serverPlayer.displayClientMessage(SkilletManCoreMod.getInfo("already_has_owner"), true);
            return;
        }
        this.isWorking = true;
    }

    public boolean tryBreakSkillet(ServerPlayer serverPlayer) {
        if(this.startNPC == null) {
            return true;
        }
        if(!serverPlayer.getUUID().equals(this.startNPC.getOwnerUUID())) {
            SMCPlayer.updateWorkingState(false, serverPlayer);
            this.clearCustomers();
            serverPlayer.displayClientMessage(SkilletManCoreMod.getInfo("already_has_owner"), true);
            return false;
        }
        this.isWorking = false;
        this.updateWorkingState(serverPlayer);
        return true;
    }

    /**
     * 判断是否开始营业
     */
    public boolean checkWorkingTime() {
        if (this.level == null) {
            return false;
        }
        long currentTime = this.level.getDayTime();
        return currentTime > 0 && currentTime < 12700;
    }

    public boolean canPlayerLeave(ServerPlayer serverPlayer) {
        //TODO 在试炼则可以离开
        return false;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void openDialogueScreen(CompoundTag senderData) {
        LinkListStreamDialogueScreenBuilder builder = new LinkListStreamDialogueScreenBuilder(null, ModBlocks.STOVE.get().getName().withStyle(ChatFormatting.AQUA));

        if(senderData.getBoolean("is_catching_escaping_player")) {
            builder.start(SkilletManCoreMod.getInfo("cannot_left_customers"))
                    .addFinalChoice(SkilletManCoreMod.getInfo("alr"), (byte) 0)
                    .addFinalChoice(SkilletManCoreMod.getInfo("god_stove_talk"), (byte) 0);
        }

        if(senderData.getBoolean("is_first_food_bad")) {
            //第一次炒糊了
            builder.start(SkilletManCoreMod.getInfo("first_food_bad"))
                    .addFinalChoice(SkilletManCoreMod.getInfo("sorry"), (byte) 0)
                    .addFinalChoice(SkilletManCoreMod.getInfo("give_me_another_chance"), (byte) 0);
        }

        if(!builder.isEmpty()){
            Minecraft.getInstance().setScreen(builder.build());
        }
    }

    @Override
    public void handleNpcInteraction(Player player, byte interactionID) {

    }
}

package com.p1nero.smc.event;

import com.p1nero.smc.SkilletManCoreMod;
import com.p1nero.smc.archive.SMCArchiveManager;
import com.p1nero.smc.worldgen.biome.SMCBiomeProvider;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.StringUtil;
import net.minecraft.world.level.BaseCommandBlock;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.WorldOptions;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.storage.ServerLevelData;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 控制服务端SaveUtil的读写
 */
@Mod.EventBusSubscriber(modid = SkilletManCoreMod.MOD_ID)
public class ServerEvents {

    public static List<MapColor> surfacematerials = Arrays.asList(MapColor.WATER, MapColor.ICE);

    /**
     * 获取存档名字，用于二次读取地图时用。
     * 仅限服务器用，如果是单人玩则需要在选择窗口或者创建游戏窗口获取。因为LevelName是可重复的，LevelID才是唯一的...
     * @see com.p1nero.smc.mixin.WorldListEntryMixin#injectedLoadWorld(CallbackInfo ci)
     * @see com.p1nero.smc.mixin.CreateWorldScreenMixin#injected(CallbackInfoReturnable)
     */
    @SubscribeEvent
    public static void onServerAboutToStart(ServerAboutToStartEvent event){
        //服务端读取，客户端从Mixin读
        if(event.getServer().isDedicatedServer()){
            if(SMCBiomeProvider.worldName.isEmpty()){
                String levelName = event.getServer().getWorldData().getLevelName();
                SMCBiomeProvider.worldName = levelName;
//                DOTEBiomeProvider.updateBiomeMap(levelName);
                SMCArchiveManager.read(levelName);
            }
        }
    }

    /**
     * stop的时候TCRBiomeProvider.worldName已经初始化了，无需处理
     */
    @SubscribeEvent
    public static void onServerStop(ServerStoppedEvent event){
        SMCArchiveManager.save(SMCBiomeProvider.worldName);
        SMCArchiveManager.clear();
    }

    @SubscribeEvent
    public static void onWorldLoad(LevelEvent.CreateSpawnPosition e) {
        Level level = getWorldIfInstanceOfAndNotRemote(e.getLevel());
        if (level == null) {
            return;
        }

        ServerLevel serverLevel = (ServerLevel)level;
        if (onWorldLoad(serverLevel)) {
            e.setCanceled(true);
        }
    }

    public static boolean onWorldLoad(ServerLevel serverLevel) {
        if (ModList.get().isLoaded("biomespawnpoint")) {
            return false;
        }

        WorldOptions worldGeneratorOptions = serverLevel.getServer().getWorldData().worldGenOptions();

        if (!worldGeneratorOptions.generateStructures()) {
            return false;
        }

        BlockPos spawnpos = getCenterNearbyVillage(serverLevel);
        if (spawnpos == null) {
            return false;
        }

        serverLevel.setDefaultSpawnPos(spawnpos, 1.0f);

        return true;
    }

    public static Level getWorldIfInstanceOfAndNotRemote(LevelAccessor levelAccessor) {
        if (levelAccessor.isClientSide()) {
            return null;
        }
        if (levelAccessor instanceof Level) {
            return ((Level)levelAccessor);
        }
        return null;
    }

    public static BlockPos getCenterNearbyVillage(ServerLevel serverLevel) {
        return getNearbyVillage(serverLevel, new BlockPos(0, 0, 0));
    }
    public static BlockPos getNearbyVillage(ServerLevel serverLevel, BlockPos nearPos) {
        BlockPos closestvillage = null;
        if (!serverLevel.getServer().getWorldData().worldGenOptions().generateStructures()) {
            return null;
        }

        String rawOutput = getRawCommandOutput(serverLevel, Vec3.atBottomCenterOf(nearPos), "/locate structure #minecraft:village");

        if (rawOutput.contains("[") && rawOutput.contains("]") && rawOutput.contains(", ")) {
            String[] coords;
            try {
                if (rawOutput.contains(":")) {
                    rawOutput = rawOutput.split(":", 2)[1];
                }

                String rawcoords = rawOutput.split("\\[")[1].split("]")[0];
                coords = rawcoords.split(", ");
            }
            catch (IndexOutOfBoundsException ex) {
                return null;
            }

            if (coords.length == 3) {
                String sx = coords[0];
                String sz = coords[2];
                if (isNumeric(sx) && isNumeric(sz)) {
                    return getSurfaceBlockPos(serverLevel, Integer.parseInt(sx), Integer.parseInt(sz));
                }
            }
        }

        return closestvillage;
    }

    public static boolean isNumeric(String string) {
        if (string == null) {
            return false;
        }

        return string.matches("-?\\d+(\\.\\d+)?");
    }
    public static BlockPos getSurfaceBlockPos(ServerLevel serverLevel, int x, int z) {
        int height = serverLevel.getHeight();
        int lowestY = serverLevel.getMinBuildHeight();

        BlockPos returnPos = new BlockPos(x, height-1, z);
        if (!isNether(serverLevel)) {
            BlockPos pos = new BlockPos(x, height, z);
            for (int y = height; y > lowestY; y--) {
                boolean continueCycle = false;

                BlockState blockState = serverLevel.getBlockState(pos);

                if (!continueCycle) {
                    MapColor material = blockState.getMapColor(serverLevel, pos);
                    if (blockState.getLightBlock(serverLevel, pos) >= 15 || surfacematerials.contains(material)) {
                        returnPos = pos.above().immutable();
                        break;
                    }
                }

                pos = pos.below();
            }
        }
        else {
            int maxHeight = 128;
            BlockPos pos = new BlockPos(x, lowestY, z);
            for (int y = lowestY; y < maxHeight; y++) {
                BlockState blockState = serverLevel.getBlockState(pos);
                if (blockState.getBlock().equals(Blocks.AIR)) {
                    BlockState upstate = serverLevel.getBlockState(pos.above());
                    if (upstate.getBlock().equals(Blocks.AIR)) {
                        returnPos = pos.immutable();
                        break;
                    }
                }

                pos = pos.above();
            }
        }

        return returnPos;
    }

    public static boolean isNether(Level level) {
        return getWorldDimensionName(level).toLowerCase().endsWith("nether");
    }

    public static String getWorldDimensionName(Level level) {
        return level.dimension().location().toString();
    }

    public static String getRawCommandOutput(ServerLevel serverLevel, @Nullable Vec3 vec, String command) {
        BaseCommandBlock bcb = new BaseCommandBlock() {
            @Override
            public @NotNull ServerLevel getLevel() {
                return serverLevel;
            }

            @Override
            public void onUpdated() { }

            @Override
            public @NotNull Vec3 getPosition() {
                return Objects.requireNonNullElseGet(vec, () -> new Vec3(0, 0, 0));
            }

            @Override
            public @NotNull CommandSourceStack createCommandSourceStack() {
                return new CommandSourceStack(this, getPosition(), Vec2.ZERO, serverLevel, 2, "dev", Component.literal("dev"), serverLevel.getServer(), null);
            }

            @Override
            public boolean isValid() {
                return true;
            }

            @Override
            public boolean performCommand(Level pLevel) {
                if (!pLevel.isClientSide) {
                    if ("Searge".equalsIgnoreCase(this.getCommand())) {
                        this.setLastOutput(Component.literal("#itzlipofutzli"));
                        this.setSuccessCount(1);
                    }
                    else {
                        this.setSuccessCount(0);
                        MinecraftServer minecraftserver = this.getLevel().getServer();
                        if (!StringUtil.isNullOrEmpty(this.getCommand())) {
                            try {
                                this.setLastOutput(null);
                                CommandSourceStack commandsourcestack = this.createCommandSourceStack().withCallback((p_45417_, p_45418_, p_45419_) -> {
                                    if (p_45418_) {
                                        this.setSuccessCount(this.getSuccessCount()+1);
                                    }

                                });
                                minecraftserver.getCommands().performPrefixedCommand(commandsourcestack, this.getCommand());
                            }
                            catch (Throwable throwable) {
                                CrashReport crashreport = CrashReport.forThrowable(throwable, "Executing command block");
                                CrashReportCategory crashreportcategory = crashreport.addCategory("Command to be executed");
                                crashreportcategory.setDetail("Command", this::getCommand);
                                crashreportcategory.setDetail("Name", () -> this.getName().getString());
                                throw new ReportedException(crashreport);
                            }
                        }
                    }
                    return true;
                }
                else {
                    return false;
                }
            }
        };

        bcb.setCommand(command);
        bcb.setTrackOutput(true);
        bcb.performCommand(serverLevel);

        return bcb.getLastOutput().getString();
    }

}

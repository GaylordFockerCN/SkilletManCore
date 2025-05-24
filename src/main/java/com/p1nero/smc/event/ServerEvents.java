package com.p1nero.smc.event;

import com.p1nero.smc.SkilletManCoreMod;
import com.p1nero.smc.archive.DataManager;
import com.p1nero.smc.archive.SMCArchiveManager;
import com.p1nero.smc.capability.SMCCapabilityProvider;
import com.p1nero.smc.capability.SMCPlayer;
import com.p1nero.smc.entity.custom.npc.start_npc.StartNPC;
import com.p1nero.smc.mixin.CreateWorldScreenMixin;
import com.p1nero.smc.mixin.WorldListEntryMixin;
import com.p1nero.smc.util.ItemUtil;
import com.p1nero.smc.util.SMCRaidManager;
import com.p1nero.smc.worldgen.biome.SMCBiomeProvider;
import com.teamtea.eclipticseasons.api.constant.solar.Season;
import com.teamtea.eclipticseasons.api.constant.solar.SolarTerm;
import com.teamtea.eclipticseasons.api.util.EclipticUtil;
import com.teamtea.eclipticseasons.common.registry.BlockRegistry;
import hungteen.htlib.common.world.entity.DummyEntityManager;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BaseCommandBlock;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.*;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vectorwing.farmersdelight.common.world.VillageStructures;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 控制服务端SaveUtil的读写
 */
@Mod.EventBusSubscriber(modid = SkilletManCoreMod.MOD_ID)
public class ServerEvents {

    public static List<MapColor> surfaceMaterials = Arrays.asList(MapColor.WATER, MapColor.ICE);

    private static final Pattern LOCATE_PATTERN = Pattern.compile(".*?\\[\\s*(-?\\d+)\\s*,\\s*~\\s*,\\s*(-?\\d+)\\s*\\].*");
    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase.equals(TickEvent.Phase.START)) {
            ServerLevel overworld = event.getServer().getLevel(Level.OVERWORLD);
            if (overworld != null) {

                int dayTime = (int) (overworld.getDayTime() / 24000);
                int dayTick = (int) (overworld.getDayTime() % 24000);

                //夜晚生成袭击
                if (overworld.isNight() && dayTick > 10000) {
                    //2天后每两天来一次袭击，10天后每天都将生成袭击
                    if (dayTime > 2 && (dayTime % 2 == 0 || dayTime > 10)) {
                        for (ServerPlayer serverPlayer : event.getServer().getPlayerList().getPlayers()) {
                            SMCPlayer smcPlayer = SMCCapabilityProvider.getSMCPlayer(serverPlayer);
                            if (!smcPlayer.isTodayInRaid()) {
                                if (!DataManager.bossKilled.get(serverPlayer)) {
                                    SMCRaidManager.startNightRaid(serverPlayer, smcPlayer);
                                }
                                DataManager.specialSolvedToday.put(serverPlayer, false);
                            }
                        }
                    }
                }

                if (overworld.isDay() && dayTick > 200) {
                    for (ServerPlayer serverPlayer : event.getServer().getPlayerList().getPlayers()) {
                        SMCPlayer smcPlayer = SMCCapabilityProvider.getSMCPlayer(serverPlayer);
                        if (!smcPlayer.isTodayInRaid()) {
                            if (DataManager.bossKilled.get(serverPlayer)) {
                                SMCRaidManager.startDayNetherRaid(serverPlayer, smcPlayer);
                            }
                            DataManager.specialSolvedToday.put(serverPlayer, false);
                        }
                    }
                }

                //播报
                if (!event.getServer().isSingleplayer() && dayTick == 13000 && DummyEntityManager.getDummyEntities(overworld).isEmpty()) {
                    broadcastRankingList(event.getServer());
                }

                //颁奖
                if (dayTime > 0 && dayTime % 6 == 0 && dayTick == 100) {
                    SolarTerm solarTerm = EclipticUtil.getNowSolarTerm(overworld);
                    ArrayList<ServerPlayer> players = getRankedList(event.getServer());
                    ServerPlayer bestPlayer = players.get(0);
                    Season season = solarTerm.getSeason();
                    players.forEach(serverPlayer -> serverPlayer.displayClientMessage(SkilletManCoreMod.getInfo("best_for_season", bestPlayer.getDisplayName(), season.getTranslation()).withStyle(season.getColor()), false));
                    ItemStack itemStack = switch (season) {
                        case SPRING -> BlockRegistry.spring_greenhouse_core.get().asItem().getDefaultInstance();
                        case SUMMER -> BlockRegistry.summer_greenhouse_core.get().asItem().getDefaultInstance();
                        case AUTUMN -> BlockRegistry.autumn_greenhouse_core.get().asItem().getDefaultInstance();
                        case WINTER -> BlockRegistry.winter_greenhouse_core.get().asItem().getDefaultInstance();
                        case NONE -> ItemStack.EMPTY;
                    };
                    itemStack.setHoverName(SkilletManCoreMod.getInfo("best_season_win", season.getTranslation(), bestPlayer.getDisplayName()).withStyle(season.getColor()));
                    ItemUtil.addItem(bestPlayer, itemStack, true);
                }

            }
        }
    }

    public static ArrayList<ServerPlayer> getRankedList(MinecraftServer server) {
        ArrayList<ServerPlayer> players = new ArrayList<>(server.getPlayerList().getPlayers());
        players.sort(Comparator.comparingInt((player) -> {
            SMCPlayer smcPlayer = SMCCapabilityProvider.getSMCPlayer((Player) player);
            return smcPlayer.getMoneyInSeason();
        }).reversed());
        return players;
    }

    public static void broadcastRankingList(MinecraftServer server) {
        List<ServerPlayer> serverPlayers = getRankedList(server);
        serverPlayers.forEach(serverPlayer -> displayRankingListFor(serverPlayer, serverPlayers));
    }

    public static void displayRankingListFor(ServerPlayer serverPlayer, List<ServerPlayer> sortedPlayers) {
        Player first = sortedPlayers.get(0);
        if (DataManager.inRaid.get(serverPlayer)) {
            return;
        }
        serverPlayer.displayClientMessage(SkilletManCoreMod.getInfo("best_seller", first.getName()), false);
        serverPlayer.displayClientMessage(SkilletManCoreMod.getInfo("current_money_list_pre"), false);
        sortedPlayers.forEach((player1 -> {
            SMCPlayer smcPlayer = SMCCapabilityProvider.getSMCPlayer(player1);
            serverPlayer.displayClientMessage(SkilletManCoreMod.getInfo("current_money_count", player1.getName().copy().append("§a[" + smcPlayer.getLevel() + "]§r"), smcPlayer.getMoneyInSeason()), false);
        }));
        serverPlayer.displayClientMessage(SkilletManCoreMod.getInfo("current_money_list_end"), false);
    }

    public static void displayRankingListFor(ServerPlayer serverPlayer) {
        MinecraftServer server = serverPlayer.server;
        ArrayList<ServerPlayer> players = getRankedList(server);
        displayRankingListFor(serverPlayer, players);
    }


    /**
     * 获取存档名字，用于二次读取地图时用。
     * 仅限服务器用，如果是单人玩则需要在选择窗口或者创建游戏窗口获取。因为LevelName是可重复的，LevelID才是唯一的...
     *
     * @see WorldListEntryMixin#injectedLoadWorld(CallbackInfo ci)
     * @see CreateWorldScreenMixin#injected(CallbackInfoReturnable)
     */
    @SubscribeEvent
    public static void onServerAboutToStart(ServerAboutToStartEvent event) {
        StartNPC.initIngredients();
//        addNewVillageBuilding(event);
        //服务端读取，客户端从Mixin读
        if (event.getServer().isDedicatedServer()) {
            if (SMCBiomeProvider.worldName.isEmpty()) {
                String levelName = event.getServer().getWorldData().getLevelName();
                SMCBiomeProvider.worldName = levelName;
//                DOTEBiomeProvider.updateBiomeMap(levelName);
                SMCArchiveManager.read(levelName);
            }
        }
    }

    /**
     * 换用lithostitched
     */
    @Deprecated
    public static void addNewVillageBuilding(final ServerAboutToStartEvent event) {
        Registry<StructureTemplatePool> templatePools = event.getServer().registryAccess().registry(Registries.TEMPLATE_POOL).get();
        Registry<StructureProcessorList> processorLists = event.getServer().registryAccess().registry(Registries.PROCESSOR_LIST).get();

        VillageStructures.addBuildingToPool(templatePools, processorLists, ResourceLocation.parse("minecraft:village/plains/houses"), SkilletManCoreMod.MOD_ID + ":village/plains/houses/plains_butcher_shop_lv1", 30);
        VillageStructures.addBuildingToPool(templatePools, processorLists, ResourceLocation.parse("minecraft:village/snowy/houses"), SkilletManCoreMod.MOD_ID + ":village/snowy/houses/snowy_butcher_shop_lv1", 20);
        VillageStructures.addBuildingToPool(templatePools, processorLists, ResourceLocation.parse("minecraft:village/savanna/houses"), SkilletManCoreMod.MOD_ID + ":village/savanna/houses/savanna_butcher_shop_lv1", 30);
        VillageStructures.addBuildingToPool(templatePools, processorLists, ResourceLocation.parse("minecraft:village/desert/houses"), SkilletManCoreMod.MOD_ID + ":village/desert/houses/desert_butcher_shop_lv1", 40);
        VillageStructures.addBuildingToPool(templatePools, processorLists, ResourceLocation.parse("minecraft:village/taiga/houses"), SkilletManCoreMod.MOD_ID + ":village/taiga/houses/taiga_butcher_shop_lv1", 20);

    }

    /**
     * stop的时候TCRBiomeProvider.worldName已经初始化了，无需处理
     */
    @SubscribeEvent
    public static void onServerStop(ServerStoppedEvent event) {
        SMCArchiveManager.save(SMCBiomeProvider.worldName);
        SMCArchiveManager.clear();
    }

    @SubscribeEvent
    public static void onWorldLoad(LevelEvent.CreateSpawnPosition e) {
        if (e.getLevel() instanceof ServerLevel serverLevel) {
            if (spawnAtVillage(serverLevel)) {
                e.setCanceled(true);
            }
        }
    }

    public static boolean spawnAtVillage(ServerLevel serverLevel) {
        if (!serverLevel.getServer().getWorldData().worldGenOptions().generateStructures()) {
            return false;
        }
        BlockPos spawnPos = getNearbyVillage(serverLevel, BlockPos.ZERO);
        if(spawnPos != BlockPos.ZERO){
            serverLevel.setDefaultSpawnPos(spawnPos, 1.0f);
            return true;
        }
        return false;
    }

    public static BlockPos getNearbyVillage(ServerLevel serverLevel, BlockPos nearPos) {
        String output = getCommandOutput(serverLevel, Vec3.atBottomCenterOf(nearPos), "/locate structure #minecraft:village");
        Matcher matcher = LOCATE_PATTERN.matcher(output);
        if(matcher.find()) {
            try {
                String xStr = matcher.group(1).trim();
                String zStr = matcher.group(2).trim();
                return getSurfaceBlockPos(serverLevel, Integer.parseInt(xStr), Integer.parseInt(zStr));
            } catch (NumberFormatException ignored) {
            }
        }
        return nearPos;
    }

    public static BlockPos getSurfaceBlockPos(ServerLevel serverLevel, int x, int z) {
        int height = serverLevel.getHeight();
        int minY = serverLevel.getMinBuildHeight();
        BlockPos pos = new BlockPos(x, height, z);
        for (int y = height; y > minY; y--) {
            BlockState blockState = serverLevel.getBlockState(pos);
            MapColor mapColor = blockState.getMapColor(serverLevel, pos);
            if (blockState.getLightBlock(serverLevel, pos) >= 15 || surfaceMaterials.contains(mapColor)) {
                return pos.above().immutable();
            }
            pos = pos.below();
        }

        return new BlockPos(x, height - 1, z);
    }

    public static String getCommandOutput(ServerLevel serverLevel, @Nullable Vec3 vec, String command) {
        BaseCommandBlock baseCommandBlock = new BaseCommandBlock() {
            @Override
            public @NotNull ServerLevel getLevel() {
                return serverLevel;
            }

            @Override
            public void onUpdated() {
            }

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
                    this.setSuccessCount(0);
                    MinecraftServer server = this.getLevel().getServer();
                    try {
                        this.setLastOutput(null);
                        CommandSourceStack commandSourceStack = this.createCommandSourceStack().withCallback((context, success, i) -> {
                            if (success) {
                                this.setSuccessCount(this.getSuccessCount() + 1);
                            }
                        });
                        server.getCommands().performPrefixedCommand(commandSourceStack, this.getCommand());
                    } catch (Throwable ignored) {
                    }
                }
                return true;
            }
        };

        baseCommandBlock.setCommand(command);
        baseCommandBlock.setTrackOutput(true);
        baseCommandBlock.performCommand(serverLevel);

        return baseCommandBlock.getLastOutput().getString();
    }

}

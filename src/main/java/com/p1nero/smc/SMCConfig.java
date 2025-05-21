package com.p1nero.smc;

import net.minecraftforge.common.ForgeConfigSpec;

public class SMCConfig {
    public static final ForgeConfigSpec.IntValue MIN_CHUNK_BETWEEN_STRUCTURE;
    public static final ForgeConfigSpec.BooleanValue BROADCAST_DIALOG;
    public static final ForgeConfigSpec.DoubleValue BROADCAST_DISTANCE;
    public static final ForgeConfigSpec.BooleanValue ENABLE_BETTER_STRUCTURE_BLOCK_LOAD;
    public static final ForgeConfigSpec.DoubleValue MOB_MULTIPLIER_WHEN_WORLD_LEVEL_UP;
    public static final ForgeConfigSpec.IntValue BOSS_HEALTH_AND_LOOT_MULTIPLE_MAX;
    public static final ForgeConfigSpec.DoubleValue TEST_X, TEST_Y, TEST_Z;
    public static final ForgeConfigSpec.BooleanValue FAST_BOSS_FIGHT;
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.BooleanValue ENABLE_WORKING_BGM;
    public static final ForgeConfigSpec.BooleanValue ENABLE_RAID_BGM;
    public static final ForgeConfigSpec.BooleanValue ENABLE_TYPEWRITER_EFFECT;
    public static final ForgeConfigSpec.IntValue TYPEWRITER_EFFECT_SPEED;
    public static final ForgeConfigSpec.IntValue TYPEWRITER_EFFECT_INTERVAL;
    public static final ForgeConfigSpec.BooleanValue SHOW_BOSS_HEALTH;
    public static final ForgeConfigSpec.BooleanValue SHOW_HINT;
    public static final ForgeConfigSpec.DoubleValue INFO_X, INFO_Y, TASK_SIZE, CUISINE_UI_X;
    public static final ForgeConfigSpec.IntValue INTERVAL;
    public static final ForgeConfigSpec CLIENT_SPEC;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        builder.push("Game Setting");
        MIN_CHUNK_BETWEEN_STRUCTURE = createInt(builder, "min_chunk_between_structure", 3, 0, "小建筑和主建筑的最小区块间距（过时）");
//        NO_PLOT_MODE = createBool(builder, "no_plot_mode", false, "！！在此config下启动的存档将无法重新开启主线剧情！！", "无剧情模式：设为true后将简化流程，没有剧情及任务。但仍可获得任务奖励。");
        BROADCAST_DIALOG = createBool(builder, "broadcast_dialog", false, "是否全局广播剧情对话（慎用，可能很烦）");
        BROADCAST_DISTANCE = createDouble(builder, "broadcast_distance", 5, 1, "广播范围");
        builder.pop();

        builder.push("Attribute Value");

        builder.pop();
        builder.push("Monster Setting");
        MOB_MULTIPLIER_WHEN_WORLD_LEVEL_UP = createDouble(builder, "mob_multiplier_when_world_level_up", 1.414, 1.0, "世界等级提升时怪物的属性提升倍数（过时）");
        BOSS_HEALTH_AND_LOOT_MULTIPLE_MAX = createInt(builder, "boss_health_and_loot_multiple_max", 5, 1, "多人模式下boss血量增加的最大倍数");
        builder.pop();

        builder.push("Test");
        TEST_X = createDouble(builder, "test_x", 1.0, -Double.MIN_VALUE, "测试用x， 方便实时调某个数值");
        TEST_Y = createDouble(builder, "test_y", 1.0, -Double.MIN_VALUE, "测试用y， 方便实时调某个数值");
        TEST_Z = createDouble(builder, "test_z", 1.0, -Double.MIN_VALUE, "测试用z， 方便实时调某个数值");
        ENABLE_BETTER_STRUCTURE_BLOCK_LOAD = createBool(builder, "enable_better_structure_block_load", true, "结构方块是否立即加载（开发人员用）");
        FAST_BOSS_FIGHT = createBool(builder, "fast_boss_fight", false, "速杀boss模式（开发人员用）");
        builder.pop();
        SPEC = builder.build();

        ForgeConfigSpec.Builder clientBuilder = new ForgeConfigSpec.Builder();
        ENABLE_WORKING_BGM = createBool(clientBuilder, "enable_working_bgm", true, "是否启用经营时的bgm，不会被唱片覆盖");
        ENABLE_RAID_BGM = createBool(clientBuilder, "enable_raid_bgm", true, "是否启用袭击时的bgm，不会被唱片覆盖");
        ENABLE_TYPEWRITER_EFFECT = createBool(clientBuilder, "enable_typewriter_effect", true, "剧情对话是否使用打字机效果");
        TYPEWRITER_EFFECT_SPEED = createInt(clientBuilder, "typewriter_effect_speed", 2, 1, "打字机效果打字速度");
        TYPEWRITER_EFFECT_INTERVAL = createInt(clientBuilder, "typewriter_effect_interval", 2, 1, "打字机效果打字间隔");
        SHOW_BOSS_HEALTH = createBool(clientBuilder, "show_boss_health", true, "是否显示Boss血量");
        SHOW_HINT = createBool(clientBuilder, "show_hint", true, "是否显示主线提示");
        INFO_X = createDouble(clientBuilder, "info_x", 0.95, 0, "任务提示框的x屏幕位置占比");
        CUISINE_UI_X = createDouble(clientBuilder, "cuisine_ui_x", 20, 0, "料理乐事UI的X位置偏移");
        INFO_Y = createDouble(clientBuilder, "info_y", 0.1, 0, "任务提示框的y屏幕位置占比");
        INTERVAL = createInt(clientBuilder, "task_interval", 12, 1, "任务提示框的各任务间隔");
        TASK_SIZE = createDouble(clientBuilder, "task_size", 100, 0, "占据宽度");

        CLIENT_SPEC = clientBuilder.build();
    }

    private static ForgeConfigSpec.BooleanValue createBool(ForgeConfigSpec.Builder builder, String key, boolean defaultValue, String... comment) {
        return builder
                .translation("config."+ SkilletManCoreMod.MOD_ID+".common."+key)
                .comment(comment)
                .define(key, defaultValue);
    }

    private static ForgeConfigSpec.IntValue createInt(ForgeConfigSpec.Builder builder, String key, int defaultValue, int min, String... comment) {
        return builder
                .translation("config."+ SkilletManCoreMod.MOD_ID+".common."+key)
                .comment(comment)
                .defineInRange(key, defaultValue, min, Integer.MAX_VALUE);
    }

    private static ForgeConfigSpec.DoubleValue createDouble(ForgeConfigSpec.Builder builder, String key, double defaultValue, double min, String... comment) {
        return builder
                .translation("config."+ SkilletManCoreMod.MOD_ID+".common."+key)
                .comment(comment)
                .defineInRange(key, defaultValue, min, Double.MAX_VALUE);
    }

}

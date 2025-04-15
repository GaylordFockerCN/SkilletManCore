package com.p1nero.smc;

import net.minecraftforge.common.ForgeConfigSpec;

public class DOTEConfig {
    public static final ForgeConfigSpec.IntValue MIN_CHUNK_BETWEEN_STRUCTURE;
//    public static final ForgeConfigSpec.BooleanValue NO_PLOT_MODE;
    public static final ForgeConfigSpec.BooleanValue ALLOW_BVB;
    public static final ForgeConfigSpec.DoubleValue HEALTH_CHECK;
    public static final ForgeConfigSpec.BooleanValue GIVE_M_KEY;
    public static final ForgeConfigSpec.BooleanValue BROADCAST_DIALOG;
    public static final ForgeConfigSpec.DoubleValue BROADCAST_DISTANCE;
    public static final ForgeConfigSpec.BooleanValue ENABLE_BETTER_STRUCTURE_BLOCK_LOAD;
    public static final ForgeConfigSpec.DoubleValue MOB_MULTIPLIER_WHEN_WORLD_LEVEL_UP;
    public static final ForgeConfigSpec.DoubleValue MOB_SPAWN_DISTANCE;
    public static final ForgeConfigSpec.DoubleValue ELITE_MOB_HEALTH_MULTIPLIER;
    public static final ForgeConfigSpec.DoubleValue ELITE_MOB_DAMAGE_MULTIPLIER;
    public static final ForgeConfigSpec.BooleanValue BOSS_HEALTH_AND_LOOT_MULTIPLE;
    public static final ForgeConfigSpec.IntValue BOSS_HEALTH_AND_LOOT_MULTIPLE_MAX;
    public static final ForgeConfigSpec.IntValue SPAWNER_BLOCK_PROTECT_RADIUS;
    public static final ForgeConfigSpec.DoubleValue TEST_X, TEST_Y, TEST_Z;
    public static final ForgeConfigSpec.BooleanValue FAST_BOSS_FIGHT;
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.BooleanValue ENABLE_TYPEWRITER_EFFECT;
    public static final ForgeConfigSpec.IntValue TYPEWRITER_EFFECT_SPEED;
    public static final ForgeConfigSpec.IntValue TYPEWRITER_EFFECT_INTERVAL;
    public static final ForgeConfigSpec.BooleanValue SHOW_BOSS_HEALTH;
    public static final ForgeConfigSpec.BooleanValue RENDER_CUSTOM_GUI;
    public static final ForgeConfigSpec.DoubleValue TASK_X, TASK_Y, TASK_SIZE;
    public static final ForgeConfigSpec.IntValue INTERVAL;
    public static final ForgeConfigSpec CLIENT_SPEC;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        builder.push("Game Setting");
        MIN_CHUNK_BETWEEN_STRUCTURE = createInt(builder, "min_chunk_between_structure", 3, 0, "小建筑和主建筑的最小区块间距");
        GIVE_M_KEY = createBool(builder, "give_m_key", true, "是否在进入游戏时直接给予玩家维度传送钥匙以及进入维度后给新手奖励");
        HEALTH_CHECK = createDouble(builder, "health_check", 25, 1, "允许进入维度的玩家血量范围");
        ALLOW_BVB = createBool(builder, "allow_bvb", false,  "是否允许bvb 开启后boss可离开祭坛活动");
//        NO_PLOT_MODE = createBool(builder, "no_plot_mode", false, "！！在此config下启动的存档将无法重新开启主线剧情！！", "无剧情模式：设为true后将简化流程，没有剧情及任务。但仍可获得任务奖励。");
        BROADCAST_DIALOG = createBool(builder, "broadcast_dialog", true, "是否全局广播剧情对话");
        BROADCAST_DISTANCE = createDouble(builder, "broadcast_distance", 50, 1, "广播范围");
        builder.pop();

        builder.push("Attribute Value");

        builder.pop();
        builder.push("Monster Setting");
        MOB_MULTIPLIER_WHEN_WORLD_LEVEL_UP = createDouble(builder, "mob_multiplier_when_world_level_up", 1.414, 1.0, "世界等级提升时怪物的属性提升倍数");
        MOB_SPAWN_DISTANCE = createDouble(builder, "mob_spawn_distance", 20, 1.0, "怪物生成间距");
        ELITE_MOB_HEALTH_MULTIPLIER = createDouble(builder, "elite_mob_health_multiplier", 3.0, 1.0, "精英怪的血量加倍");
        ELITE_MOB_DAMAGE_MULTIPLIER = createDouble(builder, "elite_mob_damage_multiplier", 1.5, 1.0, "精英怪的伤害加倍");
        BOSS_HEALTH_AND_LOOT_MULTIPLE = createBool(builder, "boss_health_and_loot_multiple", true, "是否在多人模式下boss血量增加");
        BOSS_HEALTH_AND_LOOT_MULTIPLE_MAX = createInt(builder, "boss_health_and_loot_multiple_max", 5, 1, "多人模式下boss血量增加的最大倍数");
        SPAWNER_BLOCK_PROTECT_RADIUS = createInt(builder, "spawner_block_protect_radius", 20, 1, "默认决斗范围");
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
        ENABLE_TYPEWRITER_EFFECT = createBool(clientBuilder, "enable_typewriter_effect", true, "剧情对话是否使用打字机效果");
        TYPEWRITER_EFFECT_SPEED = createInt(clientBuilder, "typewriter_effect_speed", 2, 1, "打字机效果打字速度");
        TYPEWRITER_EFFECT_INTERVAL = createInt(clientBuilder, "typewriter_effect_interval", 2, 1, "打字机效果打字间隔");
        SHOW_BOSS_HEALTH = createBool(clientBuilder, "show_boss_health", true, "是否显示Boss血量");
        RENDER_CUSTOM_GUI = createBool(clientBuilder, "render_custom_gui", false, "是否显示自定义GUI（可通过快捷键设置，具体看按键绑定）");
        TASK_X = createDouble(clientBuilder, "task_x", 0.8, 0, "任务提示框的x屏幕位置占比");
        TASK_Y = createDouble(clientBuilder, "task_y", 0.1, 0, "任务提示框的y屏幕位置占比");
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

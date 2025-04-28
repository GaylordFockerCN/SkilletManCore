package com.p1nero.smc.datagen.lang;

import com.p1nero.smc.SkilletManCoreMod;
import com.p1nero.smc.block.SMCBlocks;
import com.p1nero.smc.client.gui.screen.entity_dialog.VillagerDialogScreenHandler;
import com.p1nero.smc.client.gui.screen.entity_dialog.animal.*;
import com.p1nero.smc.client.gui.screen.entity_dialog.golem.IronGolemDialogScreenHandler;
import com.p1nero.smc.client.gui.screen.entity_dialog.golem.SnowGolemDialogScreenHandler;
import com.p1nero.smc.client.gui.screen.info_screen.BanPortalScreenHandler;
import com.p1nero.smc.client.gui.screen.info_screen.StartGuideScreenHandler;
import com.p1nero.smc.effect.SMCEffects;
import com.p1nero.smc.entity.SMCEntities;
import com.p1nero.smc.entity.custom.npc.customer.Customer;
import com.p1nero.smc.registrate.SMCRegistrateItems;
import com.p1nero.smc.worldgen.biome.SMCBiomes;
import net.minecraft.data.PackOutput;
import net.minecraft.world.entity.EntityType;

public class SMCLangGenerator extends SMCLangProvider {
    public SMCLangGenerator(PackOutput output) {
        super(output);
    }

    @Override
    protected void addTranslations() {

        this.addInfo("set_time_not_allowed", "§c \"/time set\"命令将影响游戏进度！请使用 \"/time add\"代替！");

        this.addInfo("start_work", "§a上班！");
        this.addInfo("end_work", "§c下班！");

        this.addInfo("full_score", "§e满分！！");
        this.addInfo("middle_score", "§a优秀！");
        this.addInfo("bad_score", "§c不合格！！");

        this.addInfo("first_food_bad", "\n §e胡来！胡来！这样的菜怎么能给客人吃！速速找NPC再去学习一番怎么做菜！！！第一道菜就这么烂，怎么行！下菜也要有个先后！");
        this.addInfo("sorry", "呜呜呜我错了灶王爷我下次再也不敢了！");
        this.addInfo("give_me_another_chance", "伟大的炉神啊！再赐予我一次机会吧！");
        this.addInfo("cannot_left_customers", "\n §e嘿小子，你不能丢下你的顾客不管！");
        this.addInfo("alr", "好好好");
        this.addInfo("god_stove_talk", "我去！灶王公说话了！");

        this.addInfo("golden_skillet_tip", "§e翻炒冷却降低至 15tick");
        this.addInfo("diamond_weapon_tip", "§b每段攻击都应该尝试一下 [KEY2] （默认右键），摸索出最合适的连招！");
        this.addInfo("golden_spatula_tip", "§e翻炒冷却降低至 15tick");
        this.addInfo("diamond_spatula_tip", "§b翻炒冷却降低至 10tick");

        this.addInfo("raid_title", "袭击");
        this.addInfo("raid_success_for_day", "成功抵御了第 %d 天的袭击！");
        this.addInfo("raid_victory", "成功抵御了袭击！");
        this.addInfo("raid_loss", "抵御失败！损失惨重！");
        this.addInfo("raid_loss_tip", "§6寻找§6盔甲匠§r，§6武器匠§r和§6图书管理员§r抽取更强力的盔甲、武器和技能书！也别忘了练习招架和格挡！");
        this.addInfo("trail_title", "突破试炼");
        this.addInfo("trail_success", "进阶成功！");
        this.addInfo("trail_failed", " 很遗憾，你没能通过试炼");

        this.addInfo("rana_kaname", "要 乐奈");//Rāna Kaname
        this.addInfo("level_up_left", " 距离升级还需完成 %d 次交易");
        this.addInfo("gold_item_got", " §e刚刚 获得传说级物品：%s §e！");
        this.addInfo("rare_item_got", " §d刚刚 获得史诗级物品：%s §d！");
        this.addInfo("someone_s_pet", " 的 ");
        this.addInfo("trial_required", ": §e§l寻找牧师通过试炼以突破世界等级限制！");
        this.addInfo("game_time_no_enough", "§4游戏时长不足！禁止挑战最终boss！");
        this.addInfo("add_item_tip", "§a获得新物品：");
        this.addInfo("advancement_look_tip", "§a不知道做什么时，可以查看一下进度（默认按 L ）");
        this.addInfo("unlock_new_order", "§a客户解锁了新的需求！ 当前可能的请求");
        this.addInfo("customer_left", "§c时间太久，顾客离开了一位。。");
        this.addInfo("already_has_owner", "§c本店铺已经有主人了！");
        this.addInfo("please_in_battle_mode", "§c请打开战斗模式！！");
        this.addInfo("no_enough_money", "§c余额不足！！");
        this.addInfo("shop_upgrade", "§a店铺已升至 %d §a级！");
        this.addInfo("next_grade_left", "§6距下一游戏阶段还差 %d §6级！");
        this.addInfo("unlock_stage2_info", "村民订购解锁 [§a海鲜大礼包§r]！ 现在可以使用全部武器了！");
        this.addInfo("meat_available", "村民订购解锁 [§a肉类大礼包§r]！");
        this.addInfo("unlock_game_stage", "§6游戏阶段提升！现在等级上限已提升至： %d §6！§r解锁新的订购列表，并且客户需求种类增加！");
        this.addInfo("working", "营业中");
        this.addInfo("resting", "休息中");
        this.addInfo("raid_no_work", "§c§l世界某处正在遭遇袭击！紧急下班！");
        this.addInfo("find_villager_first", "在村庄找到店铺并领取新手福利");
        this.addInfo("find_villager_first2", "最好找个好位置，让客人能找到你");
        this.addInfo("find_villager_first3", "本包不需要撸树挖矿，不要乱跑哦");
        this.addInfo("find_villager_gacha", "进行任意 武器/盔甲/技能书 祈愿");
        this.addInfo("find_villager_gacha2", "最好把村民框起来方便再次祈愿");
        this.addInfo("special_customer", "§6特殊顾客！");
        this.addInfo("level_mul", "等级加成！× %.1f");
        this.addInfo("seafood_mul", "海鲜加成！× %.1f");
        this.addInfo("meat_mul", "肉类加成！× %.1f");
        this.addInfo("type_mul", "品种加成！× %.1f");
        this.addInfo("all_taken", "已全部取出！");
        this.addInfo("customer_is_first", "§c顾客就是上帝！");
        this.addInfo("no_your_power", "Oh 不, 这份力量并不属于你...设定上你只能使用平底锅");
        this.addInfo("no_your_power2", "关闭§6战斗模式§r以拾取被丢出的物品，部分物品阶段提升后可在战斗模式下使用。");
        this.addInfo("two_craft_tip", "每两把同星级物品可以合成更高星级的物品，五星级除外。");

        StartGuideScreenHandler.onGenerate(this);
        BanPortalScreenHandler.onGenerate(this);

        this.addEffect(SMCEffects.BURNT, "灼伤");

        this.addSkill("better_dodge_display", "完美闪避显示", "成功闪避将留下残影和播放音效，并额外恢复一点耐力");

        this.addAdvancement(SkilletManCoreMod.MOD_ID, "临村怀梦", "平底锅侠传奇的一生即将开始");
        this.addAdvancement("dirt_plate", "节俭之星", "使用脏盘子盛菜，节俭是一种美德，但是你的顾客可能不会这么想。");
        this.addAdvancement("start_work", "新的开始", "接手一家店铺并开始经营");
        this.addAdvancement("money1000", "一千富翁", "持有 1,000 绿宝石");
        this.addAdvancement("money1000000", "百万富翁", "持有 1,000,000 绿宝石");
        this.addAdvancement("money1000000000", "一个小目标", "持有 1,000,000,000 绿宝石（你真的没有开挂吗）");
        this.addAdvancement("level1", "崭露头角", "经营等级达到一级");
        this.addAdvancement("level2", "信手拈来", "经营等级达到二级");
        this.addAdvancement("level3", "厨神", "经营等级达到三级。那么喜欢做菜，还不去打最终boss？");

        this.addAdvancement("change_villager", "给我上班！", "和无业游民对话转换其职业");
        this.addAdvancement("talk_to_cleric", "神的指示", "和牧师对话，了解终界的事");

        this.addAdvancement("first_gacha", "第一抽！", "抽取任意 武器/盔甲/技能书");
        this.addAdvancement("first_5star_skillet", "第一个五星锅！", "将一把平底锅升到五星");
        this.addAdvancement("first_5star_item", "一发入魂！", "抽出第一个五星 武器/盔甲");


        this.addAdvancement("end", "结束了？", "击败最终boss");
        this.addAdvancement("true_end", "真结局", "和牧师对话解锁真相");

        this.addAdvancement("start_fight", "战斗之路", "第一次成功抵御来自终界的袭击");
        this.addAdvancement("dodge_master", "闪避大师", "完美闪避10次");
        this.addAdvancement("dodge_master2", "闪避大师2", "完美闪避100次");
        this.addAdvancement("dodge_master3", "闪避大师3", "完美闪避1000次");
        this.addAdvancement("parry_master", "招架大师", "完美招架10次");
        this.addAdvancement("parry_master2", "招架大师2", "完美招架100次");
        this.addAdvancement("parry_master3", "招架大师3", "完美招架1000次");

        this.addAdvancement("hijack_customer", "拐走！", "抢走其他玩家的顾客");
        this.addAdvancement("no_your_power", "忠于平底锅", "企图使用其他武器的力量");
        this.addAdvancement("fake_sleep", "睡觉时间到！", "企图唤醒你的员工，但你永远无法唤醒一个正在装睡的人。");
        this.addAdvancement("try_push", "碰碰车", "企图推开核心NPC，太可恶了！");
        this.addAdvancement("no_money", "亿万负翁", "钱达到负数（到底是怎么做到的。。。）");
        this.addAdvancement("self_eat", "自产自销", "吃下自己做的食物");
        this.addAdvancement("too_many_mouth", "工伤请求", "接待话很多的村民");
        this.addAdvancement("pre_cook", "预制菜", "在大晚上的做菜卖给谁呢？");
        this.addAdvancement("dog_no_eat", "狗都不吃", "企图给狗吃做坏的食材。");

        this.add(SMCRegistrateItems.SPATULA_V2.get(), "锅铲");
        this.add(SMCRegistrateItems.SPATULA_V3.get(), "锅铲");
        this.add(SMCRegistrateItems.SPATULA_V4.get(), "锅铲");
        this.add(SMCRegistrateItems.SPATULA_V5.get(), "锅铲");
        this.add(SMCRegistrateItems.GOLDEN_SPATULA.get(), "黄金锅铲");
        this.add(SMCRegistrateItems.GOLDEN_SPATULA_V2.get(), "黄金锅铲");
        this.add(SMCRegistrateItems.GOLDEN_SPATULA_V3.get(), "黄金锅铲");
        this.add(SMCRegistrateItems.GOLDEN_SPATULA_V4.get(), "黄金锅铲");
        this.add(SMCRegistrateItems.GOLDEN_SPATULA_V5.get(), "黄金锅铲");
        this.add(SMCRegistrateItems.DIAMOND_SPATULA.get(), "钻石锅铲");
        this.add(SMCRegistrateItems.DIAMOND_SPATULA_V2.get(), "钻石锅铲");
        this.add(SMCRegistrateItems.DIAMOND_SPATULA_V3.get(), "钻石锅铲");
        this.add(SMCRegistrateItems.DIAMOND_SPATULA_V4.get(), "钻石锅铲");
        this.add(SMCRegistrateItems.DIAMOND_SPATULA_V5.get(), "钻石锅铲");
        this.add("item.smc.diamond_spatula_skill1", "%s %s %s %s %s 开启强化");
        this.add("item.smc.diamond_spatula_skill2", "%s %s 开启无月连段");
        this.add("item.smc.diamond_spatula_skill3", "存在 冲刺 跳跃 攻击分支");
        this.add(SMCRegistrateItems.GOLDEN_SKILLET.get(), "黄金平底锅");
        this.add(SMCRegistrateItems.DIAMOND_SKILLET.get(), "钻石平底锅");
        this.add("item.smc.diamond_skillet_skill1", "%s %s %s %s %s %s %s 开启强化");
        this.add("item.smc.diamond_skillet_skill2", "冲刺时 %s %s/%s %s 释放 CREMATORIO");
        this.add("item.smc.diamond_skillet_skill3", "%s %s %s %s %s 释放 SOLAR POLVORA");
        this.add(SMCRegistrateItems.DIRT_PLATE.get(), "脏盘子");
        this.add(SMCRegistrateItems.DIRT_PLATE.get().getDescriptionId() + ".disc", "上面充满了油渍，对着水右键可以洗干净。或许也可以重复使用？");
        this.add(SMCBlocks.MAIN_COOK_BLOCK.get(), "核心方块");

        this.add(SMCRegistrateItems.SKILL_BOOK_RAFFLE_TICKET.get(), "技能书抽奖券");
        this.addItemUsageInfo(SMCRegistrateItems.SKILL_BOOK_RAFFLE_TICKET.asItem(), "可以在§6图书管理员§r处抽取技能书。");
        this.add(SMCRegistrateItems.WEAPON_RAFFLE_TICKET.get(), "武器抽奖券");
        this.addItemUsageInfo(SMCRegistrateItems.WEAPON_RAFFLE_TICKET.asItem(), "可以在§6武器匠§r处抽取武器。找不到武器匠时可以尝试通过对话转化普通村民");
        this.add(SMCRegistrateItems.ARMOR_RAFFLE_TICKET.get(), "盔甲抽奖券");
        this.addItemUsageInfo(SMCRegistrateItems.ARMOR_RAFFLE_TICKET.asItem(), "可以在§6盔甲匠§r处抽取盔甲。找不到盔甲匠时可以尝试通过对话转化普通村民");
        this.add(SMCRegistrateItems.DOLL_RAFFLE_TICKET.get(), "玩偶抽奖券");
        this.addItemUsageInfo(SMCRegistrateItems.DOLL_RAFFLE_TICKET.asItem(), "可以在§6牧羊人§r处兑换玩偶盲盒。找不到牧羊人时可以尝试通过对话转化普通村民");
        this.add(SMCRegistrateItems.PET_RAFFLE_TICKET.get(), "宠物抽奖券");
        this.addItemUsageInfo(SMCRegistrateItems.PET_RAFFLE_TICKET.asItem(), "可以在§6牧羊人§r处抽取宠物。找不到牧羊人时可以尝试通过对话转化普通村民。§b图标很可爱对吧(〃'▽'〃)");
        this.add(SMCRegistrateItems.DISC_RAFFLE_TICKET.get(), "唱片抽奖券");
        this.addItemUsageInfo(SMCRegistrateItems.DISC_RAFFLE_TICKET.asItem(), "可以在§6制图师§r处抽取唱片。找不到6制图师时可以尝试通过对话转化普通村民。");

        this.addBiome(SMCBiomes.AIR, "虚空");

        this.add("itemGroup.smc.items", "平底锅侠 - 核心");

        this.add(SMCEntities.GOLDEN_FLAME.get(), "终界领主");
        this.add(SMCEntities.SUPER_GOLEM.get(), "超雄铁哥");

        this.add(SMCEntities.START_NPC.get(), "§e人畜无害的村民§r");
        this.add(SMCEntities.START_NPC.get() + "_empty", "§e⬇对话以开始经营⬇");
        this.add(SMCEntities.START_NPC.get().getDescriptionId() + "_hired", "§a收入：%d §e| §a速度 %s");
        this.add(SMCEntities.START_NPC.get().getDescriptionId() + "_guider", "§b人畜无害的村民");
        this.addDialog(SMCEntities.START_NPC, 0, "准备好开始游戏了吗？");
        this.addDialogChoice(SMCEntities.START_NPC, 0, "入职 §a花费 %d 绿宝石");
        this.addDialogChoice(SMCEntities.START_NPC, 1, "雇佣 §a花费 %d 绿宝石");
        this.addDialogChoice(SMCEntities.START_NPC, 2, "告辞");
        this.addDialogChoice(SMCEntities.START_NPC, 3, "领取新手福利");
        this.addDialogChoice(SMCEntities.START_NPC, 4, "§a新手帮助");
        this.addDialogChoice(SMCEntities.START_NPC, 5, "领取全部收入");
        this.addDialogChoice(SMCEntities.START_NPC, 6, "升级店铺 §a花费 %d 绿宝石");
        this.addDialogChoice(SMCEntities.START_NPC, 7, "返回");
        this.addDialogChoice(SMCEntities.START_NPC, 8, "继续");
        this.addDialogChoice(SMCEntities.START_NPC, 9, "还是不要打扰它比较好...");
        this.addDialogChoice(SMCEntities.START_NPC, 10, "醒醒！Go work！");

        this.addDialogChoice(SMCEntities.START_NPC, 11, "订购");
        this.addDialogChoice(SMCEntities.START_NPC, 12, "订购 主食大礼包 §a花费 100 绿宝石");
        this.addDialogChoice(SMCEntities.START_NPC, 13, "订购 果蔬大礼包 §a花费 100 绿宝石");
        this.addDialogChoice(SMCEntities.START_NPC, 14, "订购 肉类大礼包 §a花费 2000 绿宝石");
        this.addDialogChoice(SMCEntities.START_NPC, 15, "订购 海鲜大礼包 §a花费 5000 绿宝石");

        this.addDialogChoice(SMCEntities.START_NPC, 16, "兑换抽奖券");
        this.addDialogChoice(SMCEntities.START_NPC, 17, "武器抽奖券");
        this.addDialogChoice(SMCEntities.START_NPC, 18, "一张 需 %d 绿宝石");
        this.addDialogChoice(SMCEntities.START_NPC, 19, "十张 需 %d 绿宝石");
        this.addDialogChoice(SMCEntities.START_NPC, 20, "技能书抽奖券");
        this.addDialogChoice(SMCEntities.START_NPC, 21, "宠物抽奖券");
        this.addDialogChoice(SMCEntities.START_NPC, 22, "碟片抽奖券");
        this.addDialogChoice(SMCEntities.START_NPC, 23, "玩偶抽奖券");
        this.addDialogChoice(SMCEntities.START_NPC, 24, "盔甲抽奖券");

        this.addDialog(SMCEntities.START_NPC, 1, "今天要做些什么呢？");
        this.addDialog(SMCEntities.START_NPC, 2, "本店亏损已久，承蒙大侠相助！");
        this.addDialog(SMCEntities.START_NPC, 3, "§a已全部取出！");
        this.addDialog(SMCEntities.START_NPC, 4, "§a已升级！");
        this.addDialog(SMCEntities.START_NPC, 5, "准备好开业了吗！§6（由于作者没有建筑水平，因此还特地补偿了一些建筑方块，快去装饰你的店铺吧！§r§c建筑时记得给村民留交易窗口！村民将从四面八方生成！）");
        this.addDialog(SMCEntities.START_NPC, 6, "建议优先寻找§6盔甲匠§r和§6武器匠§r抽取盔甲和武器！以获得更好的游玩体验！ 将§6炒锅§r摆上§6炉灶§r以开始营业，长按拆下以下班。右键顾客以查看所需食材，烹饪完成后将食材放至§6主手§r，再次对话以交付食材。根据食材的品质将获得不同奖励。而夜晚可能会有§c袭击事件§r，拿起平底锅保卫村庄！");
        this.addDialog(SMCEntities.START_NPC, 7, "炒菜时，拿着食材右键§6炒锅§r以把对应的食材丢入锅中，用§6锅铲§r右键即可翻炒。左边的仪表盘提示食物是否§c烧焦§r，请在合适的时候用盘子将其取出！ 使用§a[JEI]§r可查看料理配方。在物品栏对着食材按shift可查看食材烹饪时间区间，做好计算再下锅吧！用到的食材越高级，店铺等级越高，奖励越丰富！如果手忙脚乱，§6[预制菜]§r 是一个不错的选择。");
        this.addDialog(SMCEntities.START_NPC, 8, "接待足够多的村民或成功抵御一次袭击，将提升一次店铺等级.随着等级提升将解锁新的资源和玩法。到一定等级时将开启§6突破试炼§r，若试炼成功则可获得大量奖励并且进入下一游戏阶段。");
        this.addDialog(SMCEntities.START_NPC, 9, "Zzz...Zzz...Zzz...(忙碌了一天的员工睡得正香，  此刻也许你会好奇它为什么能够站着睡着。平底锅侠的世界就是如此奇妙，无需那么多为什么。)");

        this.addDialog(SMCEntities.START_NPC, 10, "这是目前可以订购的食材大礼包的列表，本列表将随着游戏阶段的提升而增加。");
        this.addDialog(SMCEntities.START_NPC, 11, "要兑换哪种抽奖券呢？");
        this.addDialog(SMCEntities.START_NPC, 12, "兑换几张呢？");

        this.add(SMCEntities.CUSTOMER.get(), "§e人畜无害的村民顾客§r");
        this.add(SMCEntities.FAKE_CUSTOMER.get(), "§e一位路过的村民§r");

        Customer.CUSTOMERS.forEach(customerData -> customerData.generateTranslation(this));
        Customer.SPECIAL_CUSTOMERS.forEach(customerData -> customerData.generateTranslation(this));

        VillagerDialogScreenHandler.onLanguageGen(this);

        this.addDialog(EntityType.VILLAGER, 0, "（村民看着你，似乎要说些什么的样子。很明显作者目前没有给眼前这种职业的村民添加对话，因为他认为他的交易项在这整合包里没有意义。他甚至不愿意加点闲聊增加沉浸感）");
        this.addDialogChoice(EntityType.VILLAGER, 0, "离去");

        PigDialogScreenHandler.onGenerateLang(this);
        CatDialogScreenHandler.onGenerateLang(this);
        WolfDialogScreenHandler.onGenerateLang(this);
        SheepDialogScreenHandler.onGenerateLang(this);
        CowDialogScreenHandler.onGenerateLang(this);
        IronGolemDialogScreenHandler.onGenerateLang(this);
        SnowGolemDialogScreenHandler.onGenerateLang(this);
    }
}

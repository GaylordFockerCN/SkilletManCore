package com.p1nero.smc.datagen.lang;

import com.p1nero.smc.SkilletManCoreMod;
import com.p1nero.smc.block.SMCBlocks;
import com.p1nero.smc.client.gui.screen.entity_dialog.VillagerDialogScreenHandler;
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

        this.addInfo("start_work", "§a上班！");
        this.addInfo("end_work", "§c下班！");

        this.addInfo("first_food_bad", "\n §e胡来！胡来！这样的菜怎么能给客人吃！速速找NPC再去学习一番怎么做菜！！！第一道菜就这么烂，怎么行！");
        this.addInfo("sorry", "呜呜呜我错了灶王爷我下次再也不敢了！");
        this.addInfo("give_me_another_chance", "伟大的炉神啊！再赐予我一次机会吧！");
        this.addInfo("cannot_left_customers", "\n §e嘿小子，你不能丢下你的顾客不管！");
        this.addInfo("alr", "好好好");
        this.addInfo("god_stove_talk", "我去！灶王公说话了！");

        this.addInfo("someone_s_pet", " 的 ");
        this.addInfo("trial_required", ": §e§l寻找牧师通过试炼以突破世界等级限制！");
        this.addInfo("add_item_tip", "§a获得新物品：");
        this.addInfo("advancement_look_tip", "§a不知道做什么时，可以查看一下进度（默认按 L ）");
        this.addInfo("unlock_new_order", "§a客户解锁了新的需求！ 当前可能的请求");
        this.addInfo("customer_left", "§c时间太久，顾客离开了一位。。");
        this.addInfo("already_has_owner", "§c本店铺已经有主人了！");
        this.addInfo("please_in_battle_mode", "§c请打开战斗模式！！");
        this.addInfo("no_enough_money", "§c余额不足！！");
        this.addInfo("shop_upgrade", "§a店铺已升至 %d §a级！");
        this.addInfo("seafood_available", "村民订购解锁 §a海鲜大礼包§r ！");
        this.addInfo("meat_available", "村民订购解锁 §a肉类大礼包§r ！");
        this.addInfo("unlock_game_stage", "§6游戏阶段提升！现在等级上限已提升至： %d §6！§r解锁新的订购列表，并且客户需求种类增加！");
        this.addInfo("working", "营业中");
        this.addInfo("resting", "休息中");
        this.addInfo("seafood_mul", "海鲜加成！× %.1f");
        this.addInfo("meat_mul", "肉类加成！× %.1f");
        this.addInfo("type_mul", "品种加成！× %.1f");
        this.addInfo("all_taken", "已全部取出！");
        this.addInfo("customer_is_first", "§c顾客就是上帝！");
        this.addInfo("no_your_power", "Oh 不, 这份力量并不属于你...设定上你只能使用平底锅");

        this.addScreenName("start_guide", "背景故事");
        this.addScreenAns("start_guide", 0, "你是一个来自天外的旅人，受§c『终界』§r侵扰，你失去了力量。因此你暂居此处，白天做饭维生，夜里抵御来自§c『终界』§r的袭击，保卫村庄。你在做菜的时候，不断从翻炒中领悟武学之道，同时可能邂逅神秘村民，传授你§e『秘笈』§r或§b『神兵』§r。");
        this.addScreenOpt("start_guide", 0, "继续");
        this.addScreenOpt("start_guide", 1, "跳过");
        this.addScreenAns("start_guide", 1, "有朝一日，你觉得力量渐渐回来了，准备进军§c『终界』§r，击败恶龙，夺回属于你的力量，换得主世界永恒的安宁。最终将成为一代宗师，村民们因此称你为——§e平 底 锅 侠§r。");
        this.addScreenOpt("start_guide", 2, "平底锅侠...好俗的名字...");
        this.addScreenOpt("start_guide", 3, "这像是作者一拍大腿就想出来的整合包");
        this.addScreenAns("start_guide", 2, "没错。这个整合包就是作者一拍大腿想出来的。但这一拍大腿就换来了一个月的爆肝...建议给作者三连。废话少说，快去村庄里寻找落脚点吧！");
        this.addScreenOpt("start_guide", 4, "我去，还能吐槽");
        this.addScreenAns("start_guide", 3, "你确定要跳过这么精彩的对话吗？一旦跳过或按ECS退出后，将只有重建存档才会弹出本对话哦~");
        this.addScreenOpt("start_guide", 5, "确定");
        this.addScreenOpt("start_guide", 6, "取消");
        this.addScreenAns("start_guide", 4, "哈哈，还想再看一遍？想得美，自己在村子里逛逛吧。");
        this.addScreenOpt("start_guide", 7, "...");

        this.addEffect(SMCEffects.BURNT, "灼伤");

        this.addSkill("better_dodge_display", "完美闪避显示", "成功闪避将留下残影和播放音效，并额外恢复一点耐力");

        this.addAdvancement(SkilletManCoreMod.MOD_ID, "临村怀梦", "平底锅侠传奇的一生即将开始");
        this.addAdvancement("dirt_plate", "节俭之星", "使用脏盘子盛菜，节俭是一种美德，但是你的顾客可能不会这么想。");
        this.addAdvancement("start_work", "新的开始", "接手一家店铺并开始经营");
        this.addAdvancement("money1000", "一千富翁", "持有 1,000 绿宝石");
        this.addAdvancement("money1000000", "百万富翁", "持有 1,000,000 绿宝石");
        this.addAdvancement("money1000000000", "一个小目标", "持有 1,000,000,000 绿宝石（你真的没有开挂吗）");

        this.addAdvancement("start_fight", "战斗之路", "第一次成功抵御袭击");
        this.addAdvancement("dodge_master", "闪避大师", "完美闪避10次");
        this.addAdvancement("dodge_master2", "闪避大师2", "完美闪避100次");
        this.addAdvancement("dodge_master3", "闪避大师3", "完美闪避1000次");
        this.addAdvancement("parry_master", "招架大师", "完美招架10次");
        this.addAdvancement("parry_master2", "招架大师2", "完美招架100次");
        this.addAdvancement("parry_master3", "招架大师3", "完美招架1000次");

        this.addAdvancement("hijack_customer", "拐走！", "抢走其他玩家的顾客");
        this.addAdvancement("no_your_power", "忠于平底锅", "企图使用其他武器的力量");
        this.addAdvancement("first_5star_skillet", "第一个五星锅！", "将一把平底锅升到五星");
        this.addAdvancement("fake_sleep", "睡觉时间到！", "企图唤醒你的员工，但你永远无法唤醒一个正在装睡的人。");
        this.addAdvancement("try_push", "碰碰车", "企图推开核心NPC，太可恶了！");
        this.addAdvancement("no_money", "亿万负翁", "钱达到负数（到底是怎么做到的。。。）");
        this.addAdvancement("self_eat", "自产自销", "吃下自己做的食物");
        this.addAdvancement("too_many_mouth", "工伤请求", "接待话很多的村民");
        this.addAdvancement("pre_cook", "预制菜", "在大晚上的做菜卖给谁呢？");
        this.addAdvancement("dog_no_eat", "狗都不吃", "企图给狗吃做坏的食材。");
        this.addAdvancement("end", "结束了？", "击败最终boss");

        this.add(SMCRegistrateItems.SPATULA_V2.get(), "锅铲");
        this.add(SMCRegistrateItems.SPATULA_V3.get(), "锅铲");
        this.add(SMCRegistrateItems.SPATULA_V4.get(), "锅铲");
        this.add(SMCRegistrateItems.SPATULA_V5.get(), "锅铲");
        this.add(SMCRegistrateItems.DIRT_PLATE.get(), "脏盘子");
        this.add(SMCRegistrateItems.DIRT_PLATE.get().getDescriptionId() + ".disc", "上面充满了油渍，对着水右键可以洗干净。或许也可以重复使用？");
        this.add(SMCBlocks.MAIN_COOK_BLOCK.get(), "核心方块");

        this.add(SMCRegistrateItems.SKILL_BOOK_RAFFLE_TICKET.get(), "技能书抽奖券");
        this.addItemUsageInfo(SMCRegistrateItems.SKILL_BOOK_RAFFLE_TICKET.asItem(), "可以在图书管理员处抽取技能书。");
        this.add(SMCRegistrateItems.WEAPON_RAFFLE_TICKET.get(), "武器抽奖券");
        this.addItemUsageInfo(SMCRegistrateItems.WEAPON_RAFFLE_TICKET.asItem(), "可以在武器匠处抽取武器。");
        this.add(SMCRegistrateItems.DOLL_RAFFLE_TICKET.get(), "玩偶抽奖券");
        this.addItemUsageInfo(SMCRegistrateItems.DOLL_RAFFLE_TICKET.asItem(), "可以在牧羊人处兑换玩偶盲盒。");
        this.add(SMCRegistrateItems.PET_RAFFLE_TICKET.get(), "宠物抽奖券");
        this.addItemUsageInfo(SMCRegistrateItems.PET_RAFFLE_TICKET.asItem(), "可以在牧羊人处抽取宠物。");
        this.add(SMCRegistrateItems.DISC_RAFFLE_TICKET.get(), "唱片抽奖券");
        this.addItemUsageInfo(SMCRegistrateItems.DISC_RAFFLE_TICKET.asItem(), "可以在工具匠处抽取唱片。");

        this.addBiome(SMCBiomes.AIR, "虚空");

        this.add("itemGroup.smc.items", "平底锅侠 - 核心");

        this.add(SMCEntities.GOLDEN_FLAME.get(), "金焰神王");

        this.add(SMCEntities.START_NPC.get(), "§e人畜无害的村民§r");
        this.add(SMCEntities.START_NPC.get() + "_empty", "§e⬇对话以开始经营⬇");
        this.add(SMCEntities.START_NPC.get().getDescriptionId() + "_hired", "§a收入：%d §e| §a速度 %s");
        this. add(SMCEntities.START_NPC.get().getDescriptionId() + "_guider", "§b人畜无害的村民");
        this.addDialog(SMCEntities.START_NPC, 0, "准备好开始游戏了吗？");
        this.addDialogChoice(SMCEntities.START_NPC, 0, "入职 §a花费 %d 绿宝石");
        this.addDialogChoice(SMCEntities.START_NPC, 1, "雇佣 §a花费 %d 绿宝石");
        this.addDialogChoice(SMCEntities.START_NPC, 2, "告辞");
        this.addDialogChoice(SMCEntities.START_NPC, 3, "领取新手福利");
        this.addDialogChoice(SMCEntities.START_NPC, 4, "新手帮助");
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

        this.addDialog(SMCEntities.START_NPC, 1, "今天要做些什么呢？");
        this.addDialog(SMCEntities.START_NPC, 2, "本店亏损已久，承蒙大侠相助！");
        this.addDialog(SMCEntities.START_NPC, 3, "§a已全部取出！");
        this.addDialog(SMCEntities.START_NPC, 4, "§a已升级！");
        this.addDialog(SMCEntities.START_NPC, 5, "准备好开业了吗！§6（由于作者没有建筑水平，因此还特地补偿了一些建筑方块，快去装饰你的店铺吧！§r§c建筑时记得给村民留交易窗口！村民将从四面八方生成！）");
        this.addDialog(SMCEntities.START_NPC, 6, "将§6炒锅§r摆上§6炉灶§r以开始营业，右键顾客以查看所需食材，烹饪完成后将食材放至§6主手§r，再次对话以交付食材。根据食材的品质将获得不同奖励。而夜晚可能会有§c袭击事件§r，拿起平底锅保卫村庄！");
        this.addDialog(SMCEntities.START_NPC, 7, "炒菜时，把对应的食材丢入锅中，用§6锅铲§r即可翻炒。左边的仪表盘提示食物是否烧焦，请在合适的时候将其取出！使用§aJEI§r可查看料理配方。用到的§6食材越高级，奖励越丰富！§r如果手忙脚乱，§6预制菜§r是一个不错的选择。");
        this.addDialog(SMCEntities.START_NPC, 8, "接待足够多的村民将提升一次店铺等级，随着等级提升将解锁新的资源和玩法。到一定等级时将开启§6突破试炼§r，若试炼成功则可获得大量奖励并且进入下一游戏阶段。");
        this.addDialog(SMCEntities.START_NPC, 9, "Zzz...Zzz...Zzz...(忙碌了一天的员工睡得正香，  此刻也许你会好奇它为什么能够站着睡着。平底锅侠的世界就是如此奇妙，无需那么多为什么。)");

        this.addDialog(SMCEntities.START_NPC, 10, "这是目前可以订购的食材大礼包的列表，本列表将随着游戏阶段的提升而增加。");

        this.add(SMCEntities.CUSTOMER.get(), "§e人畜无害的村民§r");

        Customer.CUSTOMERS.forEach(customerData -> customerData.generateTranslation(this));
        Customer.SPECIAL_CUSTOMERS.forEach(customerData -> customerData.generateTranslation(this));

        VillagerDialogScreenHandler.onLanguageGen(this);

        this.addDialog(EntityType.VILLAGER, 0, "（村民看着你，似乎要说些什么的样子。很明显作者目前没有给眼前这种职业的村民添加对话，因为他认为他的交易项在这整合包里没有意义。他甚至不愿意加点闲聊增加沉浸感）");
        this.addDialogChoice(EntityType.VILLAGER, 0, "离去");

        this.addDialogEntityName(EntityType.PIG, "一只平凡的猪");
        this.addDialog(EntityType.PIG, -1, "哼哼哼，哼哼（猪叫，看来作者真的没有给猪做对话...）");
        this.addDialog(EntityType.PIG, 0, "（我大抵是疯了，竟然想和一头猪对话）");
        this.addDialogChoice(EntityType.PIG, 0, "离开");
        this.addDialog(EntityType.PIG, 1, "哼，等等！哼，为什么你觉得在这个整合包里猪不会说话？难道你不想和我说话吗？（猪猪君看透了你的心思）");
        this.addDialogChoice(EntityType.PIG, 1, "你会突然站起来变成苦力怕吗");
        this.addDialogChoice(EntityType.PIG, 2, "尝试 \\TOT/\\TOT/\\TOT/");
        this.addDialogChoice(EntityType.PIG, 3, "对着它唱歌");

    }
}

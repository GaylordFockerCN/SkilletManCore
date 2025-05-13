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
import com.p1nero.smc.event.ClientForgeEvents;
import com.p1nero.smc.item.SMCItems;
import com.p1nero.smc.item.custom.CookGuideBookItem;
import com.p1nero.smc.item.custom.CreateCookGuideBookItem;
import com.p1nero.smc.registrate.SMCRegistrateItems;
import com.p1nero.smc.util.BookManager;
import com.p1nero.smc.worldgen.biome.SMCBiomes;
import dev.xkmc.cuisinedelight.init.registrate.CDItems;
import net.minecraft.data.PackOutput;
import net.minecraft.world.entity.EntityType;

public class SMCLangGenerator extends SMCLangProvider {
    public SMCLangGenerator(PackOutput output) {
        super(output);
    }

    @Override
    protected void addTranslations() {

        for(int i = 0; i < ClientForgeEvents.TIPS.size(); i++){
            this.add("screen_tips.smc.tip" + i, ClientForgeEvents.TIPS.get(i));
        }

        this.addInfo("no_owner_shop", "无主的店铺");
        this.addInfo("my_new_shop", "我的新店铺");
        this.addInfo("error_when_try_to_upgrade_shop", "§c升级店铺外观时出现意外！");
        this.addInfo("second_after_boss_die_left", "将在 %d 秒后返回主世界");
        this.addInfo("need_shift_see_combo", "按下shift以查看武器连招");

        this.addInfo("wear_effect", "§e套装效果：§r");
        this.addInfo("set_time_not_allowed", "§c \"/time set\"命令将影响游戏进度！请使用 \"/time add\"代替！");

        this.addInfo("start_work", "§a上班！");
        this.addInfo("end_work", "§c下班！");
        this.add("key.smc.new_shop", "一家新店铺");

        this.addInfo("die_tip", "§6建议多练习招架和闪避\n金币充足时，建议寻找特定村民抽取强力武器，盔甲，技能书！\n（本包所有伤害都已经降低1/2了哦~）");

        this.addInfo("full_score", "§e满分！！");
        this.addInfo("middle_score", "§a优秀！");
        this.addInfo("bad_score", "§c不合格！！");

        this.addInfo("first_food_bad", "\n §e胡来！胡来！这样的菜怎么能给客人吃！速速再去多翻几遍烹饪宝典学习一番怎么做菜！！！第一道菜就这么烂，怎么行！");
        this.addInfo("sorry", "呜呜呜我错了灶王爷我下次再也不敢了！");
        this.addInfo("give_me_another_chance", "伟大的炉神啊！再赐予我一次机会吧！");
        this.addInfo("cannot_left_customers", "\n §e嘿小子，你不能丢下你的顾客不管！");
        this.addInfo("alr", "好好好");
        this.addInfo("god_stove_talk", "我去！灶王公说话了！");
        this.addInfo("special_event_ans", "\n §e嘿小子！大事不妙！店里失窃了！快去看看怎么回事再回来继续营业吧！");
        this.addInfo("special_event_opt1", "大胆！何方小贼！");
        this.addInfo("special_event_opt2", "灶爷，我去去就回！");

        this.addInfo("golden_skillet_tip", "§e烹饪速度降低至0.66");
        this.addInfo("diamond_skillet_tip", "§e烹饪速度降低至0.33");
        this.addInfo("diamond_weapon_tip", "§b每段攻击都应该尝试一下 [KEY2] （默认右键），摸索出最合适的连招！");
        this.addInfo("golden_spatula_tip", "§e翻炒冷却降低至 15tick");
        this.addInfo("diamond_spatula_tip", "§b翻炒冷却降低至 10tick");

        this.addInfo("weapon_level", "武器等级：");
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
        this.addInfo("trial_required", "寻找牧师通过试炼以突破声望等级限制！");
        this.addInfo("trial_required2", "提示：可通过§a[酿造台]§r或对话转化无业村民");
        this.addInfo("game_time_no_enough", "§4游戏时长不足！禁止挑战最终boss！");
        this.addInfo("add_item_tip", "§a获得新物品：%s × %d");
        this.addInfo("advancement_look_tip", "不知道做什么时，可以按§a[L]§r查看进度");
        this.addInfo("unlock_new_order", "§a客户解锁了新的需求！ 当前可能的请求");
        this.addInfo("customer_left", "§c时间太久，顾客离开了一位。。");
        this.addInfo("already_has_owner", "§c您不是本店铺的主人！");
        this.addInfo("please_in_battle_mode", "§c按 §e[%s§e] §c键打开战斗模式！！");
        this.addInfo("no_enough_money", "§c余额不足！！");
        this.addInfo("shop_upgrade", "§a店铺已升至 %d §a级！");

        this.addInfo("next_grade_left", "§6距下一游戏阶段还差 %d §6级！");
        this.addInfo("unlock_stage2_info", "村民订购解锁 §a[海鲜大礼包]§r！ 现在可以使用全部武器了！");
        this.addInfo("meat_available", "村民订购解锁 §a[肉类大礼包]§r！\n §6接下来村民需求将出现更多混合食物！注意把控下锅时间哦~");
        this.addInfo("unlock_game_stage", "§6游戏阶段提升！现在声望等级上限已提升至：%d§6！ 解锁新的订购列表，并且客户需求种类增加！");
        this.addInfo("working", "营业中");
        this.addInfo("resting", "休息中");
        this.addInfo("foods_need_cut", "§6该食材需砧板加工处理！");
        this.addInfo("raid_no_work", "§c§l世界某处正在遭遇袭击！紧急下班！");
        this.addInfo("find_villager_first", "在村庄找到店铺并领取新手福利");
        this.addInfo("find_villager_first2", "可在小地图查看空店铺，建议找较为平缓的位置");
        this.addInfo("find_villager_first3", "本包不需要撸树挖矿，不要乱跑哦");
        this.addInfo("find_villager_gacha", "进行任意 武器/盔甲/技能书 祈愿");
        this.addInfo("find_villager_gacha2", "最好把村民框起来，方便再次祈愿");
        this.addInfo("find_villager_gacha3", "建议使用§a[不会乱动的村民刷怪蛋]§r哦");
        this.addInfo("first_work", "往炉灶上摆上炒锅开始工作！");
        this.addInfo("first_work2", "记得趁白天哦~");
        this.addInfo("special_customer", "§6特殊顾客！");
        this.addInfo("level_mul", "声望等级加成！× %.1f");
        this.addInfo("seafood_mul", "海鲜加成！× %.1f");
        this.addInfo("meat_mul", "肉类加成！× %.1f");
        this.addInfo("type_mul", "品种加成！× %.1f");
        this.addInfo("size_mul", "份量加成！× %.1f");
        this.addInfo("all_taken", "已全部取出！");
        this.addInfo("customer_is_first", "§c顾客就是上帝！");
        this.addInfo("no_your_power", "Oh 不, 这份力量并不属于你...设定上你只能使用平底锅和锅铲");
        this.addInfo("no_your_power2", "关闭§6战斗模式§r以拾取被丢出的物品，部分物品阶段提升后可在战斗模式下使用。");
        this.addInfo("two_craft_tip", "每两把同星级物品可以合成更高星级的物品，五星级除外。");

        StartGuideScreenHandler.onGenerate(this);
        BanPortalScreenHandler.onGenerate(this);

        this.addEffect(SMCEffects.BURNT, "灼伤");

        this.addSkill("better_dodge_display", "完美闪避显示", "成功闪避将留下残影和播放音效，并额外恢复一点耐力");

        this.addInfo("food_adv_unlock_pre", "首次烹饪出： ");
        this.addInfo("advancement_food_unlock_tip", "解锁了新的图鉴！默认按§a[L]§r查看");
        this.addAdvancement(SkilletManCoreMod.MOD_ID, "临村怀梦", "平底锅侠传奇的一生即将开始");
        this.addAdvancement(SkilletManCoreMod.MOD_ID + "_food", "料理图鉴", "制作过的食物将在这里展示出来");
        this.addAdvancement(SkilletManCoreMod.MOD_ID + "_weapon", "武器图鉴", "在武器匠处可能抽取到的武器，获得过的武器将被点亮");
        this.addItemAdvancementDesc(CDItems.SPATULA, "很普通的锅铲");
        this.addItemAdvancementDesc(SMCRegistrateItems.SPATULA_V2, "很普通的锅铲，伤害提高");
        this.addItemAdvancementDesc(SMCRegistrateItems.SPATULA_V3, "很普通的锅铲，新增一段普通攻击，伤害提高");
        this.addItemAdvancementDesc(SMCRegistrateItems.SPATULA_V4, "很普通的锅铲，伤害提高");
        this.addItemAdvancementDesc(SMCRegistrateItems.SPATULA_V5, "很普通的锅铲，新增一段普通攻击，伤害提高");
        this.addItemAdvancementDesc(CDItems.SKILLET, "很普通的平底锅");
        this.addItemAdvancementDesc(SMCRegistrateItems.IRON_SKILLET_LEVEL2, "很普通的平底锅，伤害提高");
        this.addItemAdvancementDesc(SMCRegistrateItems.IRON_SKILLET_LEVEL3, "很普通的平底锅，新增一段普攻，伤害提高");
        this.addItemAdvancementDesc(SMCRegistrateItems.IRON_SKILLET_LEVEL4, "很普通的平底锅，伤害提高");
        this.addItemAdvancementDesc(SMCRegistrateItems.IRON_SKILLET_LEVEL5, "很普通的平底锅，新增技能【钢铁旋风】，伤害提高");
        this.addItemAdvancementDesc(SMCRegistrateItems.GOLDEN_SPATULA, "金闪闪的锅铲");
        this.addItemAdvancementDesc(SMCRegistrateItems.GOLDEN_SPATULA_V2, "金闪闪的锅铲，伤害提高");
        this.addItemAdvancementDesc(SMCRegistrateItems.GOLDEN_SPATULA_V3, "金闪闪的锅铲，普通攻击新增一段强力的击飞，改变冲刺攻击和跳跃攻击，伤害提高");
        this.addItemAdvancementDesc(SMCRegistrateItems.GOLDEN_SPATULA_V4, "金闪闪的锅铲，伤害提高");
        this.addItemAdvancementDesc(SMCRegistrateItems.GOLDEN_SPATULA_V5, "金闪闪的锅铲，新增强力武器技能，可震慑周边敌人。伤害提高");
        this.addItemAdvancementDesc(SMCRegistrateItems.GOLDEN_SKILLET, "金闪闪的平底锅");
        this.addItemAdvancementDesc(SMCRegistrateItems.GOLDEN_SKILLET_V2, "金闪闪的平底锅，伤害提高");
        this.addItemAdvancementDesc(SMCRegistrateItems.GOLDEN_SKILLET_V3, "金闪闪的平底锅，改变冲刺攻击和跳跃攻击，伤害提高");
        this.addItemAdvancementDesc(SMCRegistrateItems.GOLDEN_SKILLET_V4, "金闪闪的平底锅，伤害提高");
        this.addItemAdvancementDesc(SMCRegistrateItems.GOLDEN_SKILLET_V5, "金闪闪的平底锅，新增强力武器技能，可震慑周边敌人，短时间内霸体。伤害提高");
        this.addItemAdvancementDesc(SMCRegistrateItems.DIAMOND_SPATULA, "钻石闪闪的锅铲，有着独特的搓招方式。");
        this.addItemAdvancementDesc(SMCRegistrateItems.DIAMOND_SPATULA_V2, "钻石闪闪的锅铲，攻速、伤害提高");
        this.addItemAdvancementDesc(SMCRegistrateItems.DIAMOND_SPATULA_V3, "钻石闪闪的锅铲，攻速、伤害提高");
        this.addItemAdvancementDesc(SMCRegistrateItems.DIAMOND_SPATULA_V4, "钻石闪闪的锅铲，攻速、伤害提高");
        this.addItemAdvancementDesc(SMCRegistrateItems.DIAMOND_SPATULA_V5, "钻石闪闪的锅铲，攻速、伤害提高");
        this.addItemAdvancementDesc(SMCRegistrateItems.DIAMOND_SKILLET, "钻石闪闪的平底锅，有着独特的搓招方式。");
        this.addItemAdvancementDesc(SMCRegistrateItems.DIAMOND_SKILLET_V2, "钻石闪闪的平底锅，攻速、伤害提高");
        this.addItemAdvancementDesc(SMCRegistrateItems.DIAMOND_SKILLET_V3, "钻石闪闪的平底锅，攻速、伤害提高");
        this.addItemAdvancementDesc(SMCRegistrateItems.DIAMOND_SKILLET_V4, "钻石闪闪的平底锅，攻速、伤害提高");
        this.addItemAdvancementDesc(SMCRegistrateItems.DIAMOND_SKILLET_V5, "钻石闪闪的平底锅，攻速、伤害提高");
        this.addItemAdvancementDesc(SMCItems.LEFT_SKILLET_RIGHT_SPATULA.get(), "左锅右铲！全新定制！独一无二的攻击模板！");
        this.addAdvancement(SkilletManCoreMod.MOD_ID + "_armor", "盔甲图鉴", "在盔甲匠处可能抽取到的盔甲，获得的盔甲将被点亮");
        this.addAdvancement(SkilletManCoreMod.MOD_ID + "_skill", "技能书图鉴", "在图书管理员处可能抽取到的技能书，学过的技能将被点亮");
        this.addAdvancement(SkilletManCoreMod.MOD_ID + "_level", "声望等级奖励", "随着声望等级提升，将获得不同的奖励");
        this.addAdvancement("level5", "声望等级5 奖励", "炒锅 §6⭐⭐⭐⭐⭐： §a其实五星的普通炒锅可能比一星的黄金武器还好用...");
        this.addAdvancement("level5_1", "突破奖励", "§6喂食升级：§a解放双手，专注于工作和战斗！");
        this.addAdvancement("level5_2", "突破奖励", "§6无脑村民刷怪蛋 × 5§a：在家里也能抽卡！");
        this.addAdvancement("level10", "声望等级10 奖励", "§e黄金锅铲 §6⭐⭐⭐⭐⭐");
        this.addAdvancement("level10_1", "突破奖励", "§6高级喂食升级：§a背包可自动喂食，更多配置项");
        this.addAdvancement("level10_2", "突破奖励", "§6工程师护目镜：§a提醒你一下本包有机械动力。差不多该步入机械时代了，快去找机械师（工具匠）搞点材料吧~");
        this.addAdvancement("level15", "声望等级15 奖励", "§e黄金平底锅 §6⭐⭐⭐⭐⭐");
        this.addAdvancement("level20", "声望等级20 奖励", "§6土豆加农炮：§a大人，时代变了");
        this.addAdvancement("level20_1", "突破奖励", "§d屠龙套装");
        this.addAdvancement("level20_2", "突破奖励", "§6大机械时代！\n§a机械师处解锁全部机械动力交易");
        this.addAdvancement("level25", "声望等级25 奖励", "§b钻石平底锅 §5⭐⭐⭐⭐");
        this.addAdvancement("level30", "声望等级30 奖励", "§6左锅右铲§r + §6英雄套装");

        this.addAdvancement("dirt_plate", "节俭之星", "使用脏盘子盛菜，节俭是一种美德，但是你的顾客可能不会这么想。");
        this.addAdvancement("start_work", "新的开始", "接手一家店铺并开始经营");
        this.addAdvancement("money1000", "一千富翁", "持有 1,000 绿宝石");
        this.addAdvancement("money1000000", "百万富翁", "持有 1,000,000 绿宝石");
        this.addAdvancement("money1000000000", "一个小目标", "持有 1,000,000,000 绿宝石（你真的没有开挂吗）");
        this.addAdvancement("special_customer_1", "大亨", "接待10位特殊客户");
        this.addAdvancement("special_customer_2", "超级大亨", "接待20位特殊客户");
        this.addAdvancement("special_customer_3", "超级超级大亨", "接待50位特殊客户");
        this.addAdvancement("stage1", "崭露头角", "经营等级达到一级");
        this.addAdvancement("stage2", "信手拈来", "经营等级达到二级");
        this.addAdvancement("stage3", "厨神", "经营等级达到三级。那么喜欢做菜，还不去打最终boss？");

        this.addAdvancement("change_villager", "给我上班！", "和无业游民对话转换其职业");
        this.addAdvancement("talk_to_cleric", "神的指示", "和牧师对话，了解终界的事");

        this.addAdvancement("first_gacha", "第一抽！", "抽取任意 武器/盔甲/技能书");
        this.addAdvancement("first_5star_skillet", "第一个五星锅！", "将一把平底锅升到五星");
        this.addAdvancement("first_5star_item", "一发入魂！", "抽出第一个五星 武器/盔甲");

        this.addAdvancement("he_shen", "真诚", "完成河神事件");
        this.addAdvancement("two_kid", "两小儿辩锅", "完成两小儿事件");
        this.addAdvancement("thief", "小偷", "完成小偷事件");
        this.addAdvancement("virgil", "抛瓦！", "完成抛瓦事件");


        this.addAdvancement("end", "结束了？", "击败最终boss");
        this.addAdvancement("true_end", "真结局", "和牧师对话解锁真相");

        this.addAdvancement("start_fight", "战斗之路", "第一次成功抵御来自终界的袭击");
        this.addAdvancement("raid10d", "村庄卫士", "抵御十天的袭击");
        this.addAdvancement("raid20d", "村庄英雄", "抵御二十天的袭击");
        this.addAdvancement("raid30d", "你怎么还没去打最终boss？", "抵御三十天的袭击");
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

        this.add(SMCRegistrateItems.COOK_GUIDE_BOOK_ITEM.get(), "烹饪宝典");
        CookGuideBookItem.addTranslation(this);
        this.add(SMCRegistrateItems.CREATE_COOK_GUIDE_BOOK_ITEM.get(), "论机械动力与料理乐事相结合（新手向）");
        CreateCookGuideBookItem.addTranslation(this);

        this.add(SMCItems.LEFT_SKILLET_RIGHT_SPATULA.get(), "左锅右铲");
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
        this.add(SMCRegistrateItems.DIRT_PLATE.get().getDescriptionId() + ".disc", "上面充满了油渍，对着水右键可以洗干净。");
        this.add(SMCBlocks.MAIN_COOK_BLOCK.get(), "核心方块");

        this.add(SMCRegistrateItems.SKILL_BOOK_RAFFLE_TICKET.get(), "技能书抽奖券");
        this.addItemUsageInfo(SMCRegistrateItems.SKILL_BOOK_RAFFLE_TICKET.asItem(), "可以在§6图书管理员§r处抽取技能书。找不到武器匠时可以尝试 在无业村民附近摆放§a[讲台]§r 或 通过对话 转化普通村民");
        this.add(SMCRegistrateItems.WEAPON_RAFFLE_TICKET.get(), "武器抽奖券");
        this.addItemUsageInfo(SMCRegistrateItems.WEAPON_RAFFLE_TICKET.asItem(), "可以在§6武器匠§r处抽取武器。找不到武器匠时可以尝试 在无业村民附近摆放§a[砂轮]§r 或 通过对话 转化普通村民");
        this.add(SMCRegistrateItems.ARMOR_RAFFLE_TICKET.get(), "盔甲抽奖券");
        this.addItemUsageInfo(SMCRegistrateItems.ARMOR_RAFFLE_TICKET.asItem(), "可以在§6盔甲匠§r处抽取盔甲。找不到盔甲匠时可以尝试 在无业村民附近摆放§a[高炉]§r 或 通过对话 转化普通村民");
        this.add(SMCRegistrateItems.DOLL_RAFFLE_TICKET.get(), "玩偶抽奖券");
        this.addItemUsageInfo(SMCRegistrateItems.DOLL_RAFFLE_TICKET.asItem(), "可以在§6牧羊人§r处兑换玩偶盲盒。找不到牧羊人时可以尝试 在无业村民附近摆放§a[织布机]§r 或 通过对话 转化普通村民");
        this.add(SMCRegistrateItems.PET_RAFFLE_TICKET.get(), "宠物抽奖券");
        this.addItemUsageInfo(SMCRegistrateItems.PET_RAFFLE_TICKET.asItem(), "可以在§6牧羊人§r处抽取宠物。找不到牧羊人时可以尝试 在无业村民附近摆放§a[织布机]§r 或 通过对话 转化普通村民。 §b图标很可爱对吧(〃'▽'〃)");
        this.add(SMCRegistrateItems.DISC_RAFFLE_TICKET.get(), "唱片抽奖券");
        this.addItemUsageInfo(SMCRegistrateItems.DISC_RAFFLE_TICKET.asItem(), "可以在§6制图师§r处抽取唱片。找不到制图师时可以尝试 在无业村民附近摆放§a[制图台]§r 或 通过对话 转化普通村民。");
        this.add(SMCRegistrateItems.CREATE_RAFFLE.get(), "机械动力通票");
        this.addItemUsageInfo(SMCRegistrateItems.CREATE_RAFFLE.asItem(), "可以在§6机械师§r处兑换机械动力材料，可交易内容将随等级提高而提高。找不到机械师时可以尝试 在无业村民附近摆放§a[锻造台]§r 或 通过对话 转化普通村民。");
        this.add(SMCRegistrateItems.REDSTONE_RAFFLE.get(), "雷石东通票");
        this.addItemUsageInfo(SMCRegistrateItems.REDSTONE_RAFFLE.asItem(), "可以在§6机械师§r处兑换来自原版的材料，找不到机械师时可以尝试 在无业村民附近摆放§a[锻造台]§r 或 通过对话 转化普通村民。");

        this.add(SMCRegistrateItems.END_TELEPORTER.get(), "末地传送石");
        this.addItemUsageInfo(SMCRegistrateItems.END_TELEPORTER.asItem(), "击杀牧师获得，潜行时右键以传送至末地主岛。为何牧师身上会有这玩意儿？");

        this.addBiome(SMCBiomes.AIR, "虚空");

        this.add("itemGroup.smc.items", "平底锅侠 - 核心");

        this.add(SMCEntities.GOLDEN_FLAME.get(), "终界领主");
        this.add(SMCEntities.SUPER_GOLEM.get(), "超雄铁哥");
        this.add(SMCEntities.SUPER_GOOD_GOLEM.get(), "超雄铁哥");
        this.add(SMCItems.GOOD_SUPER_GOLEM_SPAWN_EGG.get(), "超雄铁哥刷怪蛋");

        this.add(SMCEntities.VILLAGER_NO_BRAIN.get(), "§e不会乱动的村民§r");
        this.add(SMCItems.NO_BRAIN_VILLAGER_SPAWN_EGG.get(), "不会乱动的村民刷怪蛋");
        this.addInfo("no_brain_villager_spawn_egg_tip", "或许适合用来转换成对应的祈愿工具人");

        this.add(SMCEntities.START_NPC.get(), "§e人畜无害的村民§r");
        this.add(SMCEntities.START_NPC.get() + "_empty", "§e⬇对话以开始经营⬇");
        this.add(SMCEntities.START_NPC.get().getDescriptionId() + "_hired", "§a收入：%d §e| §a速度 %s");
        this.add(SMCEntities.START_NPC.get().getDescriptionId() + "_guider", "§b美好生活小助手");
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

        this.addDialogChoice(SMCEntities.START_NPC, 11, "订购食材");
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

        this.addDialogChoice(SMCEntities.START_NPC, 25, "§f修缮外观§r %d绿宝石");

        this.addDialog(SMCEntities.START_NPC, 1, "今天要做些什么呢？");
        this.addDialog(SMCEntities.START_NPC, 2, "本店亏损已久，承蒙大侠相助！");
        this.addDialog(SMCEntities.START_NPC, 3, "§a已全部取出！");
        this.addDialog(SMCEntities.START_NPC, 4, "§a已升级！");
        this.addDialog(SMCEntities.START_NPC, 5, "准备好开业了吗！记得留意四周哦，村民将从四面八方生成！");
        this.addDialog(SMCEntities.START_NPC, 6, "建议优先寻找§6盔甲匠§r和§6武器匠§r抽取盔甲和武器！以获得更好的游玩体验！ 将§6炒锅§r摆上§6炉灶§r以开始营业，长按拆下以下班。右键顾客以查看所需食材，烹饪完成后将食材放至§6主手§r，再次对话以交付食材。根据食材的品质将获得不同奖励。而§c第二天开始夜晚会有袭击事件§r，拿起平底锅保卫村庄！");
        this.addDialog(SMCEntities.START_NPC, 7, "炒菜时，拿着食材右键§6炒锅§r以把对应的食材丢入锅中，用§6锅铲§r右键即可翻炒。左边的仪表盘提示食物是否§c烧焦§r，请在合适的时候用盘子将其取出！ 使用§a[JEI]§r可查看料理配方。在物品栏可查看食材§6烹饪时间区间§r，做好计算再下锅吧！用到的食材越高级，声望等级越高，奖励越丰富！如果手忙脚乱，§6[预制菜]§r 是一个不错的选择。");
        this.addDialog(SMCEntities.START_NPC, 8, "接待足够多的村民或成功抵御一次袭击，将提升一次店铺声望等级.随着声望等级提升将解锁新的资源和玩法。到一定声望等级时将开启§6突破试炼§r，若试炼成功则可获得大量奖励并且进入下一游戏阶段。");
        this.addDialog(SMCEntities.START_NPC, 9, "Zzz...Zzz...Zzz...(忙碌了一天的员工睡得正香，  此刻也许你会好奇它为什么能够站着睡着。平底锅侠的世界就是如此奇妙，无需那么多为什么。)");

        this.addDialog(SMCEntities.START_NPC, 10, "这是目前可以订购的食材大礼包的列表，本列表将随着游戏阶段的提升而增加。");
        this.addDialog(SMCEntities.START_NPC, 11, "要兑换哪种抽奖券呢？");
        this.addDialog(SMCEntities.START_NPC, 12, "兑换几张呢？");

        this.add(SMCEntities.HE_SHEN.get(), "一位路过的神明");
        this.addDialog(SMCEntities.HE_SHEN, 0, "年轻人，你丢的，是这把金平底锅呢？还是这把钻石平底锅呢？还是这把普通的平底锅呢？");
        this.addDialog(SMCEntities.HE_SHEN, 1, "诚实的孩子哟，这是你应得的！");
        this.addDialog(SMCEntities.HE_SHEN, 2, "快哉快哉！成交愉快！");
        this.addDialog(SMCEntities.HE_SHEN, 3, "§c贪婪的人类哟，这是对你的惩罚！我将收走你的锅，你们永世不得再见！");
        this.addDialogChoice(SMCEntities.HE_SHEN, 0, "普通的平底锅");
        this.addDialogChoice(SMCEntities.HE_SHEN, 1, "金的平底锅");
        this.addDialogChoice(SMCEntities.HE_SHEN, 2, "钻石的平底锅");
        this.addDialogChoice(SMCEntities.HE_SHEN, 3, "都是我丢的");
        this.addDialogChoice(SMCEntities.HE_SHEN, -1, "拜拜~");
        this.addDialogChoice(SMCEntities.HE_SHEN, -2, "怎么感觉有点强买强卖...");
        this.addDialogChoice(SMCEntities.HE_SHEN, -3, "？？？");

        this.add(SMCEntities.TWO_KID.get(), "两小儿");
        this.addDialog(SMCEntities.TWO_KID, 0, "（%s 东游，见两小儿辩锅，一儿曰：）我认为这锅是香的。");
        this.addDialogChoice(SMCEntities.TWO_KID, 0, "继续");
        this.addDialogChoice(SMCEntities.TWO_KID, -1, "好了小朋友");
        this.addDialogChoice(SMCEntities.TWO_KID, -2, "玩够了该把锅还我了");
        this.addDialog(SMCEntities.TWO_KID, 1, "（一儿曰：）我认为这锅是臭的。");
        this.addDialog(SMCEntities.TWO_KID, 2, "（一儿曰：）这锅日间做菜，岂不充满美食之味？");
        this.addDialog(SMCEntities.TWO_KID, 3, "（一儿曰：）这锅夜间杀怪，岂不充满血腥之味？");
        this.addDialog(SMCEntities.TWO_KID, 4, "（他们突然一起看向你，似乎是希望你做出回答。）");
        this.addDialogChoice(SMCEntities.TWO_KID, 1, "我白天都没怎么上班，臭的");
        this.addDialogChoice(SMCEntities.TWO_KID, 2, "我夜里都用锅铲，香的");
        this.addDialogChoice(SMCEntities.TWO_KID, 3, "原来作者知道这厨具杀人不合理啊..");
        this.addDialogChoice(SMCEntities.TWO_KID, 4, "我不到啊");
        this.addDialog(SMCEntities.TWO_KID, 5, "（两小儿笑曰：）哈哈哈哈谁说您十分有智慧呢");

        this.add(SMCEntities.THIEF1.get(), "倒卖村民");
        this.addDialog(SMCEntities.THIEF1, 0, "走过路过不要错过！只要19.9便可加入美食社区！食材都是经过我精心加工的，质量无需担心！（眼前这位村民似曾相识，你一眼便认出它在倒卖你做的菜）");
        this.addDialog(SMCEntities.THIEF1, 1, "你做的又如何？你知道我加调料有多辛苦吗？我收个辛苦费不行吗？与其在这和我争论不如先去把隔壁那个偷你锅卖的搞了！");
        this.addDialog(SMCEntities.THIEF1, 2, "饶，饶命！大侠饶命！小的再也不敢了！（灰溜溜地逃跑了）");
        this.addDialogChoice(SMCEntities.THIEF1, 0, "这不是我做的菜吗？");
        this.addDialogChoice(SMCEntities.THIEF1, 1, "先去看看隔壁怎么个事");
        this.addDialogChoice(SMCEntities.THIEF1, 2, "大胆！你问过我的锅铲了吗！（掏出武器）");
        this.addDialogChoice(SMCEntities.THIEF1, 3, "算你识相");

        this.add(SMCEntities.THIEF2.get(), "偷锅村民");
        this.addDialog(SMCEntities.THIEF2, 0, "走过路过不要错过！只要19.9便可获得平底锅侠同款平底锅！货真价实，质量无需担心！还有我精心喷漆！（眼前这位村民手里的锅极具辨识度，你一看便知是他偷走了你的锅）");
        this.addDialog(SMCEntities.THIEF2, 1, "谁说这是窃取…等等，竟然是本尊！大侠饶命！我只是收个喷漆的辛苦钱，没有倒卖！喷漆很辛苦的！您要不先管管隔壁倒卖的那家伙？");
        this.addDialog(SMCEntities.THIEF2, 2, "饶，饶命！大侠饶命！小的再也不敢了！（灰溜溜地逃跑了）");
        this.addDialogChoice(SMCEntities.THIEF2, 0, "大胆毛贼！何故窃取我锅！");
        this.addDialogChoice(SMCEntities.THIEF2, 1, "先去看看隔壁怎么个事");
        this.addDialogChoice(SMCEntities.THIEF2, 2, "得先问我锅铲同不同意！");
        this.addDialogChoice(SMCEntities.THIEF2, 3, "算你识相");

        this.add(SMCEntities.VIRGIL_VILLAGER.get(), "充满抛瓦的村民");
        this.addDialog(SMCEntities.VIRGIL_VILLAGER, 0, "（眼前这位村民似乎不愿以正脸示人，不过很明显就是它偷了你的锅）");
        this.addDialog(SMCEntities.VIRGIL_VILLAGER, 1, "（它还是坐在那里，不为所动）");
        this.addDialog(SMCEntities.VIRGIL_VILLAGER, 2, "如果你想要，你得自己来拿。");
        this.addDialog(SMCEntities.VIRGIL_VILLAGER, 3, " 这规矩你早就懂的。");
        this.addDialog(SMCEntities.VIRGIL_VILLAGER, 4, "我们之间打过多少次架了。");
        this.addDialog(SMCEntities.VIRGIL_VILLAGER, 5, "（邪魅一笑）");
        this.addDialogChoice(SMCEntities.VIRGIL_VILLAGER, 0, "嘿，刌民，你动不动就袭击村庄的日子结束了！");
        this.addDialogChoice(SMCEntities.VIRGIL_VILLAGER, 1, "（叹气）把平底锅给我");
        this.addDialogChoice(SMCEntities.VIRGIL_VILLAGER, 2, "继续");
        this.addDialogChoice(SMCEntities.VIRGIL_VILLAGER, 3, "我就知道你会这么说（掏出武器）");
        this.addDialogChoice(SMCEntities.VIRGIL_VILLAGER, 4, "说不好。每次路过都会顺手清理刌民营地");
        this.addDialogChoice(SMCEntities.VIRGIL_VILLAGER, 5, " 该做个了解了！一了百了！");

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
        ChickenDialogScreenHandler.onGenerateLang(this);
        IronGolemDialogScreenHandler.onGenerateLang(this);
        SnowGolemDialogScreenHandler.onGenerateLang(this);
    }
}

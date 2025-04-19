package com.p1nero.smc.datagen.lang;

import com.p1nero.smc.SkilletManCoreMod;
import com.p1nero.smc.block.SMCBlocks;
import com.p1nero.smc.effect.SMCEffects;
import com.p1nero.smc.entity.SMCEntities;
import com.p1nero.smc.entity.custom.npc.customer.Customer;
import com.p1nero.smc.registrate.SMCRegistrateItems;
import com.p1nero.smc.worldgen.biome.SMCBiomes;
import net.minecraft.data.PackOutput;

public class SMCLangGenerator extends SMCLangProvider {
    public SMCLangGenerator(PackOutput output) {
        super(output);
    }

    @Override
    protected void addTranslations() {

        this.addInfo("start_work", "§a上班！");
        this.addInfo("end_work", "§c下班！");

        this.addInfo("first_food_bad", "\n §e胡来！胡来！这样的菜怎么能给客人吃！速速找NPC再去学习一番怎么做菜！！再有下次，一次扣你100！");
        this.addInfo("sorry", "呜呜呜我错了灶王爷我下次再也不敢了！");
        this.addInfo("give_me_another_chance", "伟大的炉神啊！再赐予我一次机会吧！");
        this.addInfo("cannot_left_customers", "\n §e嘿小子，你不能丢下你的顾客不管！");
        this.addInfo("alr", "好好好");
        this.addInfo("god_stove_talk", "我去！灶王公说话了！");

        this.addInfo("unlock_new_order", "§a客户解锁了新的需求！ 当前可能的请求");
        this.addInfo("customer_left", "§c时间太久，顾客离开了一位。。");
        this.addInfo("already_has_owner", "§c本店铺已经有主人了！");
        this.addInfo("please_in_battle_mode", "§c请打开战斗模式！！");
        this.addInfo("no_enough_money", "§c余额不足！！");
        this.addInfo("shop_upgrade", "§a店铺已升至 %d §a级！");
        this.addInfo("unlock_game_stage", "§a游戏阶段提升！现在等级上限已提升至： %d §a！解锁新的订购列表，并且客户需求种类增加！");
        this.addInfo("working", "营业中");
        this.addInfo("resting", "休息中");
        this.addInfo("all_taken", "已全部取出！");
        this.addInfo("customer_is_first", "§c顾客就是上帝！");
        this.addInfo("no_your_power", "Oh 不, 这份力量并不属于你...设定上你只能使用平底锅");

        this.addEffect(SMCEffects.BURNT, "灼伤");

        this.addSkill("better_dodge_display", "完美闪避显示", "成功闪避将留下残影和播放音效，并额外恢复一点耐力");

        this.addAdvancement(SkilletManCoreMod.MOD_ID, "临村怀梦", "平底锅侠传奇的一生即将开始");
        this.addAdvancement("no_your_power", "忠于平底锅", "企图使用其他武器的力量");
        this.addAdvancement("first_5star_skillet", "第一个五星锅！", "将一把平底锅升到五星");
        this.addAdvancement("fake_sleep", "睡觉时间到！", "企图唤醒你的员工，但你永远无法唤醒一个正在装睡的人。");
        this.addAdvancement("try_push", "碰碰车", "企图推开核心NPC，太可恶了！");
        this.addAdvancement("no_money", "亿万负翁", "钱达到负数（到底是怎么做到的。。。）");
        this.addAdvancement("self_eat", "自产自销", "吃下自己做的食物");
        this.addAdvancement("too_many_mouth", "工伤请求", "接待话很多的村民");
        this.addAdvancement("got_fox", "小狐狸", "从村民处获得可爱的狐狸玩偶");

        this.add(SMCRegistrateItems.SPATULA_V2.get(), "锅铲");
        this.add(SMCRegistrateItems.SPATULA_V3.get(), "锅铲");
        this.add(SMCRegistrateItems.SPATULA_V4.get(), "锅铲");
        this.add(SMCRegistrateItems.SPATULA_V5.get(), "锅铲");
        this.add(SMCBlocks.MAIN_COOK_BLOCK.get(), "核心方块");

        this.addBiome(SMCBiomes.AIR, "虚空");

        this.add("itemGroup.smc.items", "平底锅侠 - 核心");

        this.add(SMCEntities.GOLDEN_FLAME.get(), "金焰神王");

        this.add(SMCEntities.START_NPC.get(), "§e人畜无害的村民§r");
        this.add(SMCEntities.START_NPC.get() + "_empty", "§e⬇对话以开始经营⬇");
        this.add(SMCEntities.START_NPC.get().getDescriptionId() + "_hired", "§a收入：%d §e| §a速度 %s");
        this. add(SMCEntities.START_NPC.get().getDescriptionId() + "_guider", "§b人畜无害的村民");
        this.addDialog(SMCEntities.START_NPC, 0, "客官可是要在此地歇一歇因果？");
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
        this.addDialogChoice(SMCEntities.START_NPC, 12, "订购 米面大礼包 §a花费 50 绿宝石");
        this.addDialogChoice(SMCEntities.START_NPC, 13, "订购 蘑菇大礼包 §a花费 10 绿宝石");
        this.addDialogChoice(SMCEntities.START_NPC, 14, "订购 果蔬大礼包 §a花费 50 绿宝石");
        this.addDialogChoice(SMCEntities.START_NPC, 15, "订购 肉类大礼包 §a花费 200 绿宝石");
        this.addDialogChoice(SMCEntities.START_NPC, 16, "订购 海鲜大礼包 §a花费 500 绿宝石");

        this.addDialog(SMCEntities.START_NPC, 1, "恩公有何贵干？");
        this.addDialog(SMCEntities.START_NPC, 2, "本店亏损已久，承蒙恩公相助！");
        this.addDialog(SMCEntities.START_NPC, 3, "§a已全部取出！");
        this.addDialog(SMCEntities.START_NPC, 4, "§a已升级！");
        this.addDialog(SMCEntities.START_NPC, 5, "准备好开业了吗！§6（由于作者没有建筑水平，因此还特地补偿了一些建筑方块。至于这些盘子，你用到通关了也花不完。就算花完了拿木板就可以做。）");
        this.addDialog(SMCEntities.START_NPC, 6, "将§6炒锅§r摆上§6炉灶§r以开始营业，右键顾客以查看所需食材，烹饪完成后§6将食材放至主手，再次对话以交付食材。§r根据食材的品质将获得不同奖励。而夜晚可能会有§4袭击事件§r，拿起平底锅保卫村庄！");
        this.addDialog(SMCEntities.START_NPC, 7, "炒菜时，把对应的食材丢入锅中，用锅铲即可翻炒。左边的仪表盘提示食物是否烧焦，请在合适的时候将其取出！使用JEI可查看料理配方。食材不足可找村民购买，等级达到一定程度时候，订购机将解锁新的食材。");
        this.addDialog(SMCEntities.START_NPC, 8, "每接待一位顾客将提升一次店铺等级，随着等级提升将解锁新的资源和玩法。到一定等级时将开启§6突破试炼§r，若试炼成功则可获得大量奖励并且进入下一游戏阶段。不用担心，§a流程不肝不长！§r");
        this.addDialog(SMCEntities.START_NPC, 9, "Zzz...Zzz...Zzz... (忙碌了一天的员工睡得正香，此刻也许你会好奇它为什么能够站着睡着。平底锅侠的世界就是如此奇妙，无需那么多为什么。)");

        this.addDialog(SMCEntities.START_NPC, 10, "这是目前可以订购的食材大礼包的列表，本列表将随着游戏阶段的提升而增加。");

        this.add(SMCEntities.CUSTOMER.get(), "§e人畜无害的村民§r");

        Customer.customers.forEach(customerData -> customerData.generateTranslation(this));
        Customer.specialCustomers.forEach(customerData -> customerData.generateTranslation(this));

    }
}

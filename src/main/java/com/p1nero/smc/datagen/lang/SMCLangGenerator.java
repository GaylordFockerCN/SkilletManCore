package com.p1nero.smc.datagen.lang;

import com.p1nero.smc.SkilletManCoreMod;
import com.p1nero.smc.block.SMCBlocks;
import com.p1nero.smc.client.SMCSounds;
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

        this.addInfo("cannot_left_customers", "\n §e嘿小子，你不能丢下你的顾客不管！");
        this.addInfo("alr", "好好好");
        this.addInfo("god_stove_talk", "我去！灶王公说话了！");

        this.addInfo("already_has_owner", "§c本店铺已经有主人了！");
        this.addInfo("please_in_battle_mode", "§c请打开战斗模式！！");
        this.addInfo("no_enough_money", "§c余额不足！！");
        this.addInfo("shop_upgrade", "§a店铺已升至 %d §a级！");
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

        this.add(SMCRegistrateItems.SPATULA_V2.get(), "锅铲");
        this.add(SMCRegistrateItems.SPATULA_V3.get(), "锅铲");
        this.add(SMCRegistrateItems.SPATULA_V4.get(), "锅铲");
        this.add(SMCRegistrateItems.SPATULA_V5.get(), "锅铲");
        this.add(SMCBlocks.MAIN_COOK_BLOCK.get(), "核心方块");

        addBiome(SMCBiomes.AIR, "虚空");
        
        add("itemGroup.smc.items", "平底锅侠 - 核心");

        add(SMCEntities.GOLDEN_FLAME.get(), "金焰神王");

        add(SMCEntities.START_NPC.get(), "§e人畜无害的村民§r");
        add(SMCEntities.START_NPC.get() + "_empty", "§e⬇对话以开始经营⬇");
        add(SMCEntities.START_NPC.get().getDescriptionId() + "_hired", "§a收入：%d §e| §a速度 %s");
        add(SMCEntities.START_NPC.get().getDescriptionId() + "_guider", "§b人畜无害的村民");
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
        this.addDialog(SMCEntities.START_NPC, 1, "恩公有何贵干？");
        this.addDialog(SMCEntities.START_NPC, 2, "本店亏损已久，承蒙恩公相助！");
        this.addDialog(SMCEntities.START_NPC, 3, "§a已全部取出！");
        this.addDialog(SMCEntities.START_NPC, 4, "§a已升级！");
        this.addDialog(SMCEntities.START_NPC, 5, "准备好开业了吗！§6[由于作者没有建筑水平，因此还特地补偿了一些建筑方块]");
        this.addDialog(SMCEntities.START_NPC, 6, "将§6炒锅§r摆上§6炉灶§r以开始营业，右键顾客以查看所需食材，烹饪完成后§6将食材放至主手§r，再次对话以交付食材。根据食材的品质将获得不同奖励。而夜晚可能会有袭击事件，拿起平底锅保卫村庄！");
        this.addDialog(SMCEntities.START_NPC, 7, "每接待一位顾客将提升一次店铺等级，随着等级提升将解锁新的资源和玩法。到一定等级时将开启§6突破试炼§r，若试炼成功则可获得大量奖励并且进入下一游戏阶段。不用担心，§a流程不肝不长！§r");
        this.addDialog(SMCEntities.START_NPC, 8, "Zzz...Zzz...Zzz... (忙碌了一天的员工睡得正香，此刻也许你会好奇它为什么能够站着睡着。平底锅侠的世界就是如此奇妙，无需那么多为什么。)");

        add(SMCEntities.CUSTOMER.get(), "§e人畜无害的顾客§r");
        this.addDialog(SMCEntities.CUSTOMER, -1, "（你感受到面前的这位客人有一股强大的气场）");

        Customer.customers.forEach(customerData -> customerData.generateTranslation(this));
        Customer.specialCustomers.forEach(customerData -> customerData.generateTranslation(this));

    }
}

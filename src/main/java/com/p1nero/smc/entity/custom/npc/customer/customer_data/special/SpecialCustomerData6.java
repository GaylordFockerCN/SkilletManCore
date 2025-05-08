package com.p1nero.smc.entity.custom.npc.customer.customer_data.special;

import com.p1nero.smc.client.gui.DialogueComponentBuilder;
import com.p1nero.smc.client.gui.TreeNode;
import com.p1nero.smc.client.gui.screen.LinkListStreamDialogueScreenBuilder;
import com.p1nero.smc.datagen.SMCAdvancementData;
import com.p1nero.smc.datagen.lang.SMCLangGenerator;
import com.p1nero.smc.entity.custom.npc.customer.Customer;
import com.p1nero.smc.entity.custom.npc.customer.customer_data.SpecialCustomerData;
import com.p1nero.smc.registrate.SMCRegistrateItems;
import com.p1nero.smc.util.ItemUtil;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class SpecialCustomerData6 extends SpecialCustomerData {

    public SpecialCustomerData6() {
        super(6);
    }

    public void generateTranslation(SMCLangGenerator generator) {
        generator.add(nameTranslationKey, "话很多的村民");
        generator.add(choicePre(-3), "这就去！");
        generator.add(answerPre(-2), "（指着你手中的物品，开启了滔滔不绝模式）哦！这个这个！这不是我要的啦！我跟你说啊，我真正想要的是...（突然压低声音，神秘兮兮）其实我今天本来想买把伞的，但路过你们店的时候闻到了一股特别香的味道！哎呀我这记性，来之前还想着列个购物清单，结果一兴奋就全忘了！（突然提高音量）所以啊！你手里的这个绝对不是我要的！（摆出送客手势）");
        generator.add(choicePre(-2), "好嘞，这就去准备您要的！");

        generator.add(answerPre(-1), "（这位村民刚见到你就像点燃的烟花，噼里啪啦炸开了，你感到一丝不安。）哎呀这位师傅！今天天气真是好得不得了啊！（观察天空）看这云朵，像不像我家隔壁王大爷养的那只大白鹅？（突然掩嘴）啊呀我这嘴！跑题了跑题了！（正色道）我来你们店之前啊，");
        generator.add(choicePre(-1), "继续");
        generator.add(answerPre(0), "在巷口遇到个算命先生，他说我今天有口福之缘！（摊开手掌）看，他还给我画了个饼！（摊开的掌心有根麦秸）我就想啊，既然是口福，那肯定得来你这儿！（压低声音）实不相瞒，我从上周日就开始盘算今天吃什么了！一开始想来个烤乳猪，后来又觉得太油腻；");
        generator.add(choicePre(0), "继续");
        generator.add(answerPre(1), "周二改主意想吃糖醋排骨，结果周三看到有人吃凉皮馋得不行；周四去参加个邻居家的喜宴，多了两碗米饭，周五消化不良一天没进食...（突然惊觉）哎呀我这又要跑题！重点是！（突然拉住你的衣袖）您这儿有没有那种...（东张西望）别人都没吃过的...（突然憋红了脸）");
        generator.add(choicePre(1), "继续");
        generator.add(answerPre(2), "就是那种...（突然拍大腿）对了！我小舅子上次在这吃了个什么...（蹲下假装翻找记忆）啊！想起来了！他说是有个能让人边吃边回忆童年味道的神奇食物！（突然凑近你耳边）悄悄说，我小舅子其实是个大胃王，上次参加吃西瓜大赛，一口气吃了...");
        generator.add(choicePre(2), "（在对方的狂轰滥炸下找准机会插嘴）客官且慢，您先别急着讲故事，不妨说说您要些什么？");
        generator.add(answerPre(3), "哦哦，（突然做憧憬状）我今天不要这个！我要的是...（突然凑近你耳边）吃完能让人忘记烦恼的那种！（突然又想起什么）不过我其实没烦恼啦！（突然大笑）我这人天生乐观！（突然又正色）总之！您这有没有能让人一听名字就忍不住咽口水的 %s ？");
        generator.add(choicePre(3), "交付");

        generator.add(answerPre(4), "（接过食物的瞬间，眼睛瞬间变成爱心形状）哇哦！师傅您太厉害了！（突然凑近食物猛吸香气）这香气！这摆盘！这色泽！（突然想起什么）我上周在美食杂志看到过类似的菜品，但您这明显更胜一筹！（突然东张西望）您看这周围，是不是能感受到食物在发光？");
        generator.add(choicePre(4), "继续");
        generator.add(answerPre(5), "（突然压低声音）我刚才闻着味道，感觉自己的味蕾都站起来了！（突然大口吃掉）唔！（含糊不清地说）这味道简直就像...就像...（突然陷入沉思）就像小时候奶奶给我煮的热汤！（突然又想起什么）不过奶奶煮的汤没这么精致！");
        generator.add(choicePre(5), "继续");
        generator.add(answerPre(6), "（突然又大口吃）这口感！这味道！这层次！（突然拍大腿）我跟您说！我上周在朋友家吃到一道菜，当时我就说“这菜也就七八十分”，但跟您这比起来...（突然做夸张的比较手势）您这是一百分！");
        generator.add(choicePre(6), "（微笑着点头）多谢客官喜欢，祝您生活像这美食一样甜蜜。");

        generator.add(answerPre(7), "（接过食物，五官挤成一团）嗯...（开始挑刺）这个...这个颜色好像有点暗？（突然又想起什么）是不是没炒熟？（突然又想起）我上周看 cooking show，说绿色的蔬菜得保持脆亮才好！（突然又开始分析）您看这切法...是不是可以再均匀点？");
        generator.add(choicePre(7), "继续");
        generator.add(answerPre(8), "（突然又想起）我有个朋友，他是厨师，他说刀工讲究“薄如纸，厚如指”，您这...（突然又拿起食物观察）这形状有点不规则啊！（突然又想起）对了！我上次在邻居家吃饭，他做的红烧肉方方正正，特别好看！（突然又尝试吃一小口）唔...（含糊地说）这味道...好像少了点什么...");
        generator.add(choicePre(8), "继续");
        generator.add(answerPre(9), "（突然又想起）是不是盐放少了？（突然又想起）我刚才路过调味铺，看到他们新进的海盐特别香！（突然又分析）要不就是糖放多了？（突然又想起）我上周在甜品店，店长说糖和酸的比例得是 3:1 才正好！");
        generator.add(choicePre(9), "（趁对方换气的空档赶紧说）客官说得对，我一定改进，下周欢迎您再来品尝升级版！");

        generator.add(answerPre(10), "（刚看到食物瞬间变脸）哎呀！（突然放开音量大喊）这是什么玩意儿！（突然又想起什么）您是不是误解我的需求了？（突然又压低声音）我本来想说...（突然挠头）其实我也不太确定想要什么...（突然又提高音量）但这绝对是错的！（突然又想起什么）我上周在这买到个包子，虽说没期望它能跳舞，但至少得是圆的吧！");
        generator.add(choicePre(10), "继续");
        generator.add(answerPre(11), "（突然又对食物指指点点）您看看您看看！这形状！这颜色！（突然又想起什么）我今天早上在菜市场看到的胡萝卜都比这好看！（突然又开始分析）您是不是用错了食材？（突然又想起）或者这是某种新式创意料理？（突然又想起）我上周看到个视频，说有人把巧克力放进火锅里！");
        generator.add(choicePre(11), "继续");
        generator.add(answerPre(12), "（突然又对食物摇头）但这绝对不行！（突然又想起什么）我有个朋友，他说美食最重要的就是让人有食欲！（突然又对食物撇嘴）这哪有食欲啊！（突然又想起什么）您是不是没用心做？（突然又想起）我刚才看到隔壁店的小哥在玩手机！（突然又对食物摇头）这绝对不行！（突然又想起什么）我本来想留个好评的！");
        generator.add(choicePre(12), "（赶紧点头哈腰）师父别念了！实在抱歉让您失望了！");
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void getDialogScreen(CompoundTag serverData, LinkListStreamDialogueScreenBuilder screenBuilder, DialogueComponentBuilder dialogueComponentBuilder, boolean canSubmit, int foodScore) {
        TreeNode root;

        String foodName = "§6" + I18n.get(serverData.getString("food_name")) + "§r";

        if (!canSubmit) {
            root = new TreeNode(answer(-1))
                    .addChild(new TreeNode(answer(0), choice(-1))
                            .addChild(new TreeNode(answer(1), choice(0))
                                    .addChild(new TreeNode(answer(2), choice(1))
                                            .addChild(new TreeNode(answer(3, foodName), choice(2))
                                                    .addChild(new TreeNode(answer(-2), choice(3))
                                                            .addLeaf(choice(-2), (byte) -3))
                                                    .addLeaf(choice(-3), (byte) -3)))));
        } else {
            root = switch (foodScore) {
                case BEST -> new TreeNode(answer(-1))
                        .addChild(new TreeNode(answer(0), choice(-1))
                                .addChild(new TreeNode(answer(1), choice(0))
                                        .addChild(new TreeNode(answer(2), choice(1))
                                                .addChild(new TreeNode(answer(3, foodName), choice(2))
                                                        .addChild(new TreeNode(answer(4), choice(3))
                                                                .addExecutable(SUBMIT_FOOD)
                                                                .addChild(new TreeNode(answer(5), choice(4))
                                                                        .addChild(new TreeNode(answer(6), choice(5))
                                                                                .addLeaf(choice(6), BEST))))
                                                        .addLeaf(choice(-3), (byte) -3)))));
                case MIDDLE -> new TreeNode(answer(-1))
                        .addChild(new TreeNode(answer(0), choice(-1))
                                .addChild(new TreeNode(answer(1), choice(0))
                                        .addChild(new TreeNode(answer(2), choice(1))
                                                .addChild(new TreeNode(answer(3, foodName), choice(2))
                                                        .addChild(new TreeNode(answer(7), choice(3))
                                                                .addExecutable(SUBMIT_FOOD)
                                                                .addChild(new TreeNode(answer(8), choice(7))
                                                                        .addChild(new TreeNode(answer(9), choice(8))
                                                                                .addLeaf(choice(9), MIDDLE))))
                                                        .addLeaf(choice(-3), (byte) -3)))));
                default -> new TreeNode(answer(-1))
                        .addChild(new TreeNode(answer(0), choice(-1))
                                .addChild(new TreeNode(answer(1), choice(0))
                                        .addChild(new TreeNode(answer(2), choice(1))
                                                .addChild(new TreeNode(answer(3, foodName), choice(2))
                                                        .addChild(new TreeNode(answer(10), choice(3))
                                                                .addExecutable(SUBMIT_FOOD)
                                                                .addChild(new TreeNode(answer(11), choice(10))
                                                                        .addChild(new TreeNode(answer(12), choice(11))
                                                                                .addLeaf(choice(12), BAD))))
                                                        .addLeaf(choice(-3), (byte) -3)))));
            };

        }
        if (root != null) {
            screenBuilder.setAnswerRoot(root);
        }
    }

    @Override
    protected void onBest(ServerPlayer serverPlayer, Customer self) {
        super.onBest(serverPlayer, self);
        ItemUtil.addItem(serverPlayer, SMCRegistrateItems.PET_RAFFLE_TICKET.get(), 1, true);
        ItemUtil.addItem(serverPlayer, SMCRegistrateItems.ARMOR_RAFFLE_TICKET.get(), 3, true);
        SMCAdvancementData.finishAdvancement("too_many_mouth", serverPlayer);
    }

    @Override
    protected void onBad(ServerPlayer serverPlayer, Customer self) {
        super.onBad(serverPlayer, self);
        serverPlayer.hurt(serverPlayer.damageSources().magic(), 0.5F);
    }
}

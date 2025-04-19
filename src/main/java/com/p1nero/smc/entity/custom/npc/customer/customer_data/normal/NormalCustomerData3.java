package com.p1nero.smc.entity.custom.npc.customer.customer_data.normal;

import com.p1nero.smc.datagen.lang.SMCLangGenerator;
import com.p1nero.smc.entity.custom.npc.customer.customer_data.NormalCustomerData;
public class NormalCustomerData3 extends NormalCustomerData {

    public NormalCustomerData3() {
        super(3);
    }

    @Override
    public void generateTranslation(SMCLangGenerator generator) {
        super.generateTranslation(generator);
        generator.add(nameTranslationKey, "非常普通的村民");
        generator.add(answerPre(-1), "（村民看着你，投来疑惑的眼神。看来不是所有村民都是绿袍尊者，请提交正确的菜品）");
        generator.add(choicePre(-1), "好吧");
        generator.add(choicePre(-2), "离开");
        generator.add(answerPre(0), "来一份 %s ");//顾客的请求，一定要保留%s，不能带有具体食物，只能带修饰词。
        generator.add(choicePre(0), "交之");//玩家的提交选项
        generator.add(answerPre(1), "上品");//高品质的时候顾客的回答。赠礼可以是秘笈或神兵
        generator.add(choicePre(1), "谢之");//高品质的时候顾客回答后 的玩家选项
        generator.add(answerPre(2), "凡品");//中品质的时候顾客的回答
        generator.add(choicePre(2), "谢之");//中品质的时候顾客回答后 的玩家选项
        generator.add(answerPre(3), "我知道接待普通的村民很无趣，但请你认真对待，好歹是可以升级的！");//低品质的时候顾客的回答
        generator.add(choicePre(3), "布豪，乱做菜被发现了！");//低品质的时候顾客回答后 的玩家选项
    }

}

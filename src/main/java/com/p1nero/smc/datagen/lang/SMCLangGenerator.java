package com.p1nero.smc.datagen.lang;

import com.p1nero.smc.SkilletManCoreMod;
import com.p1nero.smc.block.SMCBlocks;
import com.p1nero.smc.client.SMCSounds;
import com.p1nero.smc.effect.SMCEffects;
import com.p1nero.smc.entity.SMCEntities;
import com.p1nero.smc.registrate.SMCRegistrateItems;
import com.p1nero.smc.worldgen.biome.SMCBiomes;
import net.minecraft.data.PackOutput;

public class SMCLangGenerator extends SMCLangProvider {
    public SMCLangGenerator(PackOutput output) {
        super(output);
    }

    @Override
    protected void addTranslations() {

        this.addInfo("customer_is_first", "§c顾客就是上帝！");
        this.addInfo("no_your_power", "这份力量不属于你...设定上你只能用平底锅");

        this.addEffect(SMCEffects.BURNT, "灼伤");

        this.addSkill("better_dodge_display", "完美闪避显示", "成功闪避将留下残影和播放音效，并额外恢复一点耐力");

        this.addAdvancement(SkilletManCoreMod.MOD_ID, "临村怀梦", "平底锅侠传奇的一生即将开始");
        this.addAdvancement("no_your_power", "忠于平底锅", "企图使用其他武器的力量");
        this.addAdvancement("first_5star_skillet", "第一个五星锅！", "将一把平底锅升到五星");

        this.add(SMCRegistrateItems.SPATULA_V2.get(), "锅铲");
        this.add(SMCRegistrateItems.SPATULA_V3.get(), "锅铲");
        this.add(SMCRegistrateItems.SPATULA_V4.get(), "锅铲");
        this.add(SMCRegistrateItems.SPATULA_V5.get(), "锅铲");
        this.add(SMCBlocks.MAIN_COOK_BLOCK.get(), "核心方块");

        addBiome(SMCBiomes.AIR, "虚空");
        
        add("itemGroup.smc.items", "平底锅侠 - 核心");

        add(SMCEntities.GOLDEN_FLAME.get(), "金焰神王");

        add(SMCEntities.START_NPC.get(), "NPC");

    }
}

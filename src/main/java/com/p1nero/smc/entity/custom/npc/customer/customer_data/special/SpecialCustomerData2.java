package com.p1nero.smc.entity.custom.npc.customer.customer_data.special;

import com.p1nero.smc.client.gui.DialogueComponentBuilder;
import com.p1nero.smc.client.gui.TreeNode;
import com.p1nero.smc.datagen.lang.SMCLangGenerator;
import com.p1nero.smc.entity.custom.npc.customer.Customer;
import com.p1nero.smc.entity.custom.npc.customer.customer_data.SpecialCustomerData;
import com.p1nero.smc.item.SMCItems;
import com.p1nero.smc.registrate.SMCRegistrateItems;
import com.p1nero.smc.util.ItemUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraftforge.registries.ForgeRegistries;
import yesman.epicfight.gameasset.EpicFightSounds;

import java.util.Objects;

public class SpecialCustomerData2 extends SpecialCustomerData {

    public SpecialCustomerData2() {
        super(2);
    }

    @Override
    public void onInteract(ServerPlayer player, Customer self) {
        super.onInteract(player, self);
        self.setAge(-114514);
        self.setBaby(true);
    }

    public void generateTranslation(SMCLangGenerator generator) {
        super.generateTranslation(generator);
        generator.add(nameTranslationKey, "娇俏可爱的村民");
        generator.add(answerPre(-2), "（小姑娘踮脚看了看你的托盘）这个不能当点心吃的呀");
        generator.add(choicePre(-2), "吐舌头");
        generator.add(choicePre(-3), "这就去！");
        generator.add(answerPre(-1), "（眼前这位村民看起来很娇俏可爱，虽然是突然变小的）§6（有一说一，很难想象面前  这个大鼻子村民是小萝莉...或许以后可以给它换个模型） ");
        generator.add(choicePre(-1), "小妹妹要尝尝新点心吗？");
        generator.add(answerPre(0), "听说你们有甜滋滋的 %s ？");
        generator.add(choicePre(0), "端上");
        generator.add(answerPre(1), "（眼睛弯成月牙）这个比娘亲做的蜜饯还香！这个小狐狸送你玩～");
        generator.add(choicePre(1), "（非常开心的收下，谁能拒绝一只可爱的小狐狸呢）");
        generator.add(answerPre(2), "（嘟嘴）像是糖霜放少了三钱呢");
        generator.add(choicePre(2), "下次再来玩呀~");
        generator.add(answerPre(3), "（突然嚎啕大哭）苦苦的东西最讨厌了！");
        generator.add(choicePre(3), "诶！妹妹别走！");
    }

    @Override
    protected void onBest(ServerPlayer serverPlayer, Customer self) {
        super.onBest(serverPlayer, self);
        ItemUtil.addItem(serverPlayer, Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(new ResourceLocation("kaleidoscope_doll:doll_45"))).getDefaultInstance());
        ItemUtil.addItem(serverPlayer, SMCRegistrateItems.DOLL_RAFFLE_TICKET.get(), 3, true);
    }

    @Override
    protected void onBad(ServerPlayer serverPlayer, Customer self) {
        super.onBad(serverPlayer, self);
        serverPlayer.playSound(EpicFightSounds.BLUNT_HIT.get());
    }
}

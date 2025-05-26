package com.p1nero.smc.item.custom;

import com.p1nero.smc.SkilletManCoreMod;
import com.p1nero.smc.datagen.SMCAdvancementData;
import com.p1nero.smc.entity.custom.npc.customer.Customer;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import static com.p1nero.smc.block.entity.MainCookBlockEntity.getRandomPos;
import static com.p1nero.smc.entity.custom.npc.start_npc.StartNPCPlus.PROFESSION_LIST;

/**
 * 豪猫，使用后生成5个客户
 */
public class LuckyCat extends SimpleDescriptionFoilItem{
    public LuckyCat(Properties properties) {
        super(properties);
    }

    public static void summonCustomers(ItemStack itemStack, Player player, Cat cat) {
        if(player instanceof ServerPlayer serverPlayer && cat.getOwner() == serverPlayer) {
            if(!serverPlayer.level().isDay()) {
                player.displayClientMessage(SkilletManCoreMod.getInfo("npc_plus_need_rest"), false);
                return;
            }
            for(int i = 0; i < 5; i++){
                BlockPos spawnPos = getRandomPos(serverPlayer, 15, 20, serverPlayer.getOnPos());
                Customer customer = new Customer(serverPlayer, spawnPos.getCenter());
                customer.setHomePos(serverPlayer.getOnPos());
                customer.setSpawnPos(spawnPos);
                customer.getNavigation().moveTo(customer.getNavigation().createPath(serverPlayer.getOnPos(), 5), 1.0);
                VillagerProfession profession = PROFESSION_LIST.get(customer.getRandom().nextInt(PROFESSION_LIST.size()));//随机抽个职业，换皮肤好看
                customer.setVillagerData(customer.getVillagerData().setType(VillagerType.byBiome(serverPlayer.serverLevel().getBiome(serverPlayer.getOnPos()))).setProfession(profession));
                serverPlayer.serverLevel().addFreshEntity(customer);
            }
            player.displayClientMessage(SkilletManCoreMod.getInfo("cat_go").withStyle(ChatFormatting.GREEN), false);
            SMCAdvancementData.finishAdvancement("cat_group", serverPlayer);
            itemStack.shrink(1);
        }
    }

}

package com.p1nero.smc.effect;

import com.p1nero.smc.entity.custom.npc.customer.Customer;
import com.p1nero.smc.util.EntityUtil;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Cat;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.List;

public class BadCatEffect extends SimpleMobEffect{
    public BadCatEffect(MobEffectCategory category, int level) {
        super(category, level);
    }

    @Override
    public void applyEffectTick(@NotNull LivingEntity livingEntity, int p_19468_) {
        if(!(livingEntity instanceof Cat cat)) {
            livingEntity.removeEffect(this);
            return;
        }
        //换Mixin里写
//        cat.setLying(false);
//        cat.setInSittingPose(false);
//        List<Customer> customers = EntityUtil.getNearByEntities(Customer.class, cat, 16);
//        if(!customers.isEmpty()) {
//            customers.sort(Comparator.comparingDouble(cat::distanceTo));
//            customers.forEach(customer -> {
//                if(!customer.isTraded() && cat.getOwner() != customer.getOwner()) {
//                    cat.getNavigation().moveTo(cat.getNavigation().createPath(customers.get(0).getOnPos(), 1), 1.0);//直接moveTo实体不行
//                    if(cat.distanceTo(customer) < 2) {
//                        cat.playAmbientSound();
//                        customer.setTraded(true);
//                    }
//                }
//            });
//        }
    }
}

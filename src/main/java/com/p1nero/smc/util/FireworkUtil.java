package com.p1nero.smc.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

/**
 * copy from <a href="https://github.com/dfdyz/epicacg-1.18/blob/main/src/main/java/com/dfdyz/epicacg/utils/FireworkUtils.java">EpicACG</a>
 */
public class FireworkUtil {
    public static CompoundTag getFireworks(CompoundTag ...tags){
        ListTag listTag = new ListTag();

        for (CompoundTag t:tags) {
            listTag.addTag(0, t);
        }

        CompoundTag tag = new CompoundTag();
        tag.put("Explosions", listTag);
        return tag;
    }
    public static CompoundTag getFirework(boolean trail, boolean flicker, byte type, int[] colors, int[] fadeColors){
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("Trail", trail);
        tag.putBoolean("Flicker", flicker);
        tag.putByte("Type", type);
        tag.putIntArray("Colors", colors);
        tag.putIntArray("FadeColors", fadeColors);
        return tag;
    }

    public static final CompoundTag Explode = getFireworks(
            getFirework(true, true, (byte) 1,new int[]{16738665}, new int[]{62463})
    );

    public static final CompoundTag Red = getFireworks(
            getFirework(true, true, (byte) 0, new int[]{16738665}, new int[]{13865361})
    );

    public static final CompoundTag Green = getFireworks(
            getFirework(true, true, (byte) 1, new int[]{65290}, new int[]{16741632})
    );

    public static final CompoundTag Yellow = getFireworks(
            getFirework(true, true, (byte) 2, new int[]{62463}, new int[]{13865361})
    );

    public static final CompoundTag Blue = getFireworks(
            getFirework(true, true, (byte) 3, new int[]{16252672}, new int[]{16741632})
    );

    public static final CompoundTag Purple = getFireworks(
            getFirework(true, true, (byte) 2, new int[]{8806321}, new int[]{10181046})
    );

    public static final CompoundTag Orange = getFireworks(
            getFirework(true, true, (byte) 1, new int[]{16753920}, new int[]{16724416})
    );

    public static final CompoundTag Pink = getFireworks(
            getFirework(true, true, (byte) 0, new int[]{16711935}, new int[]{16777215})
    );

    public static final CompoundTag Cyan = getFireworks(
            getFirework(true, true, (byte) 1, new int[]{5592575}, new int[]{16777215})
    );

    public static final CompoundTag Magenta = getFireworks(
            getFirework(true, true, (byte) 0, new int[]{16711935}, new int[]{16750899})
    );

    public static final CompoundTag Lime = getFireworks(
            getFirework(true, true, (byte) 0, new int[]{10145074}, new int[]{16736768})
    );

    public static final CompoundTag Teal = getFireworks(
            getFirework(true, true, (byte) 0, new int[]{3573035}, new int[]{10974051})
    );

    public static final CompoundTag Brown = getFireworks(
            getFirework(true, true, (byte) 0, new int[]{11184810}, new int[]{16761021})
    );

    public static final CompoundTag Gold = getFireworks(
            getFirework(true, true, (byte) 0, new int[]{16766720}, new int[]{16711935})
    );

    public static final CompoundTag[] RANDOM = {
            Green, Red, Yellow, Blue, Purple, Orange, Pink, Cyan, Magenta, Lime, Teal, Brown, Gold
    };

}

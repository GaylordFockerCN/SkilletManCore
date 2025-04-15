package com.p1nero.smc.worldgen.biome;

import com.p1nero.smc.SkilletManCoreMod;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.io.Serializable;

/**
 * 统一在这里调参数，共有默认地图和噪声生成地图两种形式
 */

public class BiomeMap implements Serializable {

    /**
     *世界的seed，为了使得不同seed生成的分布一致。
     * 在{@link com.p1nero.smc.mixin.WorldOptionsMixin}中实现
     */
    public static long seed;

    //方便全局调用
    private static BiomeMap INSTANCE;
    private double[][] map;

    public double[][] getMap() {
        return map;
    }

    public Point getCenter1() {
        return new Point(map.length / 2, map.length / 6);
    }

    public Point getCenter2() {
        return new Point(map.length / 2, map.length * 5 / 6);
    }
    public Point getAltar1() {
        return new Point(map.length / 6, map.length / 2);
    }
    public Point getAltar2() {
        return new Point(map.length * 5 / 6, map.length / 2);
    }

    public Point getCenter() {
        return new Point(map.length / 2, map.length / 2);
    }

    public int getMapLength(){
        return map.length;
    }

    public int getMapWidth(){
        return map[0].length;
    }

    public int getR() {
        return map.length / 2;
    }
    public Point getBlockPos(Point biomePos0) {
        Point biomePos = (Point) biomePos0.clone();
        biomePos.x = (biomePos0.x - getR()) * 4;
        biomePos.y = (biomePos0.y - getR()) * 4;
        return biomePos;
    }

    public BlockPos getBlockPos(Point biomePos0,int y){
        Point pos = getBlockPos(biomePos0);
        return new BlockPos(pos.x, y, pos.y);
    }

    public static final int SIZE = 320;

    public static BiomeMap getInstance(){
        return INSTANCE;
    }

    public static double[][] createImageMap(){

        try {
            BufferedImage image;
            InputStream inputStream = getInputStream();
            image = ImageIO.read(inputStream);
            SMCBiomeProvider.LOGGER.info("reading default_map");
            SMCBiomeProvider.mapName = "default_map";
            int height = image.getHeight();
            int width = image.getWidth();
            double[][] map = new double[height][width];

            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    int rgb = image.getRGB(j, i);  // 注意，getRGB的参数是 (x, y)，而不是 (i, j)
                    int alpha = (rgb >> 24) & 0xff;
                    int red = (rgb >> 16) & 0xff;
                    int green = (rgb >> 8) & 0xff;
                    int blue = rgb & 0xff;

                    if (alpha < 50) {
                        map[i][j] = 0;  // 透明区域
                    } else {
                        // 判断是否接近蓝色或红色
                        if (blue > 230 && red < 20 && green < 20) {
                            map[i][j] = 3;  // 接近蓝色
                        } else if (red > 230 && blue < 20 && green < 20) {
                            map[i][j] = 4;  // 接近红色
                        } else {
                            // 计算颜色的亮度，接近黑色为 2，接近白色为 1
                            int brightness = (red + green + blue) / 3;
                            if (brightness < 20) {
                                map[i][j] = 2;  // 接近黑色
                            } else {
                                map[i][j] = 1;  // 接近白色
                            }
                        }
                    }
                }
            }

            return map;

        } catch (Exception e) {
            SMCBiomeProvider.LOGGER.error("Dimension map image file exception! Map will be generated with default noise.",e);
            return null;
        }
    }

    /**
     * 从类路径中获取图片资源的输入流
     */
    @NotNull
    private static InputStream getInputStream() {
        ClassLoader classLoader = SkilletManCoreMod.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream("/default_map.png");
        assert inputStream != null;
        return inputStream;
    }

    public static void init(){
        INSTANCE = new BiomeMap();
        INSTANCE.map = createImageMap();
    }

    public static void load(BiomeMap biomeMap){
        INSTANCE = biomeMap;
    }

    /**
     * 把位置信息导入nbt方便客户端读
     */
    public static CompoundTag toNBT(CompoundTag tag){
        if(INSTANCE == null){
            return tag;
        }
        BlockPos biome1Center = INSTANCE.getBlockPos(INSTANCE.getCenter1(), 0);
        BlockPos biome2Center = INSTANCE.getBlockPos(INSTANCE.getCenter2(), 0);
        BlockPos altar1 = INSTANCE.getBlockPos(INSTANCE.getAltar1(), 0);
        BlockPos altar2 = INSTANCE.getBlockPos(INSTANCE.getAltar2(), 0);
        BlockPos biomeCenter = INSTANCE.getBlockPos(INSTANCE.getCenter(), 0);
        String center1 = "(" + biome1Center.getX()+", " + biome1Center.getZ() + ")";
        String center2 = "(" + biome2Center.getX()+", " + biome2Center.getZ() + ")";
        String altar1pos = "(" + altar1.getX()+", " + altar1.getZ() + ")";
        String altar2pos = "(" + altar2.getX()+", " + altar2.getZ() + ")";
        String center = "(" + biomeCenter.getX()+", " + biomeCenter.getZ() + ")";
        tag.putString("center1", center1);
        tag.putString("center2", center2);
        tag.putString("altar1pos", altar1pos);
        tag.putString("altar2pos", altar2pos);
        tag.putString("center", center);
        return tag;
    }

}

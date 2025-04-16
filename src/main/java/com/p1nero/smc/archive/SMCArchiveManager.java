package com.p1nero.smc.archive;

import com.p1nero.smc.SkilletManCoreMod;
import com.p1nero.smc.network.SMCPacketHandler;
import com.p1nero.smc.network.PacketRelay;
import com.p1nero.smc.network.packet.SyncArchivePacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.network.chat.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * 保存游戏进度，这玩意儿应该所有人统一，所以用了自己的数据管理。
 */
public class SMCArchiveManager {

    private static boolean alreadyInit = false;

    /**
     * 以服务端数据为准，如果已经被服务端同步过了，则不能读取客户端的数据，用于{@link SMCArchiveManager#read(String)}
     */
    public static void setAlreadyInit() {
        SMCArchiveManager.alreadyInit = true;
    }

    public static boolean isAlreadyInit() {
        return alreadyInit;
    }

    private static int worldLevel = 0;
    public static final ProgressData BIOME_PROGRESS_DATA = new ProgressData();
    private static boolean noPlotMode = false;

    public static void setNoPlotMode() {
        noPlotMode = true;
    }

    public static boolean isNoPlotMode() {
        return noPlotMode;
    }

    public static int getWorldLevel(){
        return worldLevel;
    }

    public static boolean setWorldLevel(int worldLevel) {
        if(worldLevel >= 0 && worldLevel <= 3){
            SMCArchiveManager.worldLevel = worldLevel;
            return true;
        } else {
            SkilletManCoreMod.LOGGER.info("failed to set world level. world level should between [1, 3]");
            return false;
        }
    }

    public static String getWorldLevelName(){
        return switch (worldLevel){
            case 0 -> "Ⅰ";
            case 1 -> "Ⅱ";
            case 2 -> "Ⅲ";
            case 3 -> "Ⅳ";
            default -> throw new IllegalStateException("Unexpected worldLevel value: " + worldLevel);
        };
    }

    public static boolean isFinished(){
        return worldLevel >= 3;
    }

    public static final List<Task> DIALOG_LIST = new ArrayList<>();
    public static final HashSet<Task> DIALOG_SET = new HashSet<>();//优化用的，但是不知道能优化多少（
    public static final TaskSet TASK_SET = new TaskSet();
    public static void addTask(Task task){
        TASK_SET.add(task);
    }
    public static void finishTask(Task task){
        TASK_SET.remove(task);
    }

    /**
     * 把对话添加到列表里
     */
    public static void addDialog(Component name, Component content){
        if(name == null || content == null){
            return;
        }
        Task task = new Task(name, content);
        if(!DIALOG_SET.contains(task)){
            DIALOG_LIST.add(task);
            DIALOG_SET.add(task);
        }
    }

    public static List<Task> getDialogList() {
        return DIALOG_LIST;
    }

    public static CompoundTag getDialogListNbt(){
        CompoundTag dialogListNbt = new CompoundTag();
        for(int i = 0; i < DIALOG_LIST.size(); i++){
            dialogListNbt.put("dialog"+i, DIALOG_LIST.get(i).toNbt());
        }
        return dialogListNbt;
    }

    public static void setDialogListFromNbt(CompoundTag DialogListTag, int size){
        DIALOG_LIST.clear();
        for(int i = 0; i < size; i++){
            Task task = Task.fromNbt(DialogListTag.getCompound("dialog"+i));
            DIALOG_LIST.add(task);
            DIALOG_SET.add(task);
        }
    }

    public static CompoundTag getTaskListNbt(){
        CompoundTag dialogListNbt = new CompoundTag();
        List<Task> tasks = TASK_SET.stream().toList();
        for(int i = 0; i < tasks.size(); i++){
            dialogListNbt.put("task" + i, tasks.get(i).toNbt());
        }
        return dialogListNbt;
    }

    public static void setTaskListFromNbt(CompoundTag taskListTag, int size){
        TASK_SET.clear();
        for(int i = 0; i < size; i++){
            TASK_SET.add(Task.fromNbt(taskListTag.getCompound("task"+i)), false);
        }
    }

    public static class ProgressData {

        private boolean guideSummoned;
        private boolean boss1fought;
        private boolean boss2fought;
        private boolean boss3fought;
        private boolean senbaiFought;
        private boolean goldenFlameFought;
        private boolean choice1, choice2, choice3;

        public boolean isGuideSummoned() {
            return guideSummoned;
        }

        public boolean isBoss1fought() {
            return boss1fought;
        }

        public boolean isBoss2fought() {
            return boss2fought;
        }

        public boolean isSenbaiFought() {
            return senbaiFought;
        }

        public boolean isGoldenFlameFought() {
            return goldenFlameFought;
        }

        public void setGuideSummoned(boolean guideSummoned) {
            this.guideSummoned = guideSummoned;
        }

        public void setBoss1fought(boolean boss1fought) {
            this.boss1fought = boss1fought;
        }

        public void setBoss2fought(boolean boss2fought) {
            this.boss2fought = boss2fought;
        }
        public void setBoss3fought(boolean boss2fought) {
            this.boss3fought = boss2fought;
        }

        public void setSenbaiFought(boolean senbaiFought) {
            this.senbaiFought = senbaiFought;
        }

        public void setGoldenFlameFought(boolean goldenFlameFought) {
            this.goldenFlameFought = goldenFlameFought;
        }

        /**
         * 结局1： 忠诚，全选骑士
         */
        public boolean isEnd1(){
            return isFinished() && choice1 && choice2 && choice3;
        }

        /**
         * 结局3：碎星者，最终必选final，且前面有一次选final
         */
        public boolean isEnd3(){
            return isFinished() && (!choice1 || !choice2) && !choice3;
        }

        public boolean isChoice1() {
            return choice1;
        }

        public void setChoice1(boolean choice1) {
            this.choice1 = choice1;
        }

        public boolean isChoice2() {
            return choice2;
        }

        public void setChoice2(boolean choice2) {
            this.choice2 = choice2;
        }

        public boolean isChoice3() {
            return choice3;
        }

        public void setChoice3(boolean choice3) {
            this.choice3 = choice3;
        }

        public ProgressData(){

        }

        /**
         * 轮回的时候用
         */
        public void clearData(){
            boss1fought = false;
            boss2fought = false;
            boss3fought = false;
            senbaiFought = false;
            goldenFlameFought = false;
        }

        public CompoundTag toNbt(CompoundTag tag) {
            tag.putBoolean("guideSummoned", guideSummoned);
            tag.putBoolean("boss1fought", boss1fought);
            tag.putBoolean("boss2fought", boss2fought);
            tag.putBoolean("boss3fought", boss3fought);
            tag.putBoolean("senbaiFought", senbaiFought);
            tag.putBoolean("goldenFlameFought", goldenFlameFought);
            tag.putBoolean("choice1", choice1);
            tag.putBoolean("choice2", choice2);
            tag.putBoolean("choice3", choice3);
            return tag;
        }

        public void fromNbt(CompoundTag serverData){
            guideSummoned = serverData.getBoolean("guideSummoned");
            boss1fought = serverData.getBoolean("boss1fought");
            boss2fought = serverData.getBoolean("boss2fought");
            boss3fought = serverData.getBoolean("boss3fought");
            senbaiFought =  serverData.getBoolean("senbaiFought");
            goldenFlameFought = serverData.getBoolean("goldenFlameFought");
            choice1 = serverData.getBoolean("choice1");
            choice2 = serverData.getBoolean("choice2");
            choice3 = serverData.getBoolean("choice3");
        }

    }

    public static Task buildTask(String task){
        return new Task(Component.translatable("task." + SkilletManCoreMod.MOD_ID + "." + task), Component.translatable("task_content." + SkilletManCoreMod.MOD_ID + "." + task));
    }

    public static Task buildTask(String task, Object... objects){
        return new Task(Component.translatable("task." + SkilletManCoreMod.MOD_ID + "." + task), Component.translatable("task_content." + SkilletManCoreMod.MOD_ID + "." + task, objects));
    }


    public static File getFile(String worldName){
        return new File("config/"+ SkilletManCoreMod.MOD_ID+"/"+ worldName +".nbt");
    }

    public static void save(String worldName){

        try {
            CompoundTag saveData = toNbt();
            File file = getFile(worldName);
            SkilletManCoreMod.LOGGER.info("saving data to {} :\n"+saveData, file.getAbsolutePath());
            if(!file.exists()){
                file.createNewFile();
            }
            NbtIo.write(saveData, file);
            SkilletManCoreMod.LOGGER.info("over.");
        } catch (Exception e) {
            SkilletManCoreMod.LOGGER.error("Can't save serverData", e);
        }

    }

    public static void read(String worldName){
        if(alreadyInit){
            return;
        }
        try {
            File save = getFile(worldName);
            CompoundTag saveData = NbtIo.read(save);
            if(saveData == null){
                SkilletManCoreMod.LOGGER.info("save data not found. created new save data: {}" + save.createNewFile(), save.getAbsolutePath());
            } else {
                SkilletManCoreMod.LOGGER.info("reading data {} :\n"+saveData, save.getAbsolutePath());
                fromNbt(saveData);
                SkilletManCoreMod.LOGGER.info("over.");
            }
        } catch (Exception e) {
            SkilletManCoreMod.LOGGER.error("Can't read save serverData", e);
        }
    }

    public static boolean deleteCache(String fileName){
        fromNbt(new CompoundTag());
        return getFile(fileName).delete();
    }

    /**
     * 清空数据
     */
    public static void clear(){
        fromNbt(new CompoundTag());
        alreadyInit = false;
        syncToClient();
    }

    /**
     * 同步数据
     * 请在服务端使用
     */
    public static void syncToClient(){
        PacketRelay.sendToAll(SMCPacketHandler.INSTANCE, new SyncArchivePacket(toNbt()));
    }

    /**
     * 把服务端的所有数据转成NBT方便发给客户端
     * @return 所有数据狠狠塞进NBT里
     */
    public static CompoundTag toNbt(){
        CompoundTag serverData = new CompoundTag();
        serverData.putInt("worldLevel", worldLevel);
        serverData.putBoolean("noPlotMode", noPlotMode);
//        serverData.putInt("dialogLength", DIALOG_LIST.size());
//        serverData.put("dialogList", getDialogListNbt());
//        serverData.putInt("taskLength", TASK_SET.size());
//        serverData.put("taskList", getTaskListNbt());
        serverData.put("biome_progress_data", BIOME_PROGRESS_DATA.toNbt(new CompoundTag()));
        return serverData;
    }

    /**
     * 把服务端发来的nbt转成客户端的SaveUtil调用
     * @param serverData 从服务端发来的nbt
     */
    public static void fromNbt(CompoundTag serverData){
        SkilletManCoreMod.LOGGER.info("reading from: \n" + serverData);
        alreadyInit = true;
        worldLevel = serverData.getInt("worldLevel");
        noPlotMode = serverData.getBoolean("noPlotMode");
//        setDialogListFromNbt(serverData.getCompound("dialogList"), serverData.getInt("dialogLength"));
//        setTaskListFromNbt(serverData.getCompound("taskList"), serverData.getInt("taskLength"));
        BIOME_PROGRESS_DATA.fromNbt(serverData.getCompound("biome_progress_data"));
    }

}

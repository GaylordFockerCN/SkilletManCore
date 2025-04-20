package com.p1nero.smc.datagen.lang;

import com.p1nero.smc.SkilletManCoreMod;
import net.minecraft.client.KeyMapping;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.registries.RegistryObject;
import org.apache.commons.lang3.text.WordUtils;

import java.util.Locale;
import java.util.function.Supplier;

public abstract class SMCLangProvider extends LanguageProvider {
    public SMCLangProvider(PackOutput output) {
        super(output, SkilletManCoreMod.MOD_ID, "zh_cn");
    }

    public void addKeyMapping(KeyMapping key, String name){
        this.add(key.getName(), name);
    }

    public void addInfo(String key, String content){
        this.add("info.smc." + key, content);
    }

    public void addTask(String key, String task, String content){
        this.add("task." + SkilletManCoreMod.MOD_ID + "." + key, task);
        this.add("task_content." + SkilletManCoreMod.MOD_ID + "." + key, content);
    }

    public void addBiome(ResourceKey<Biome> biome, String name) {
        this.add("biome." + SkilletManCoreMod.MOD_ID + "." + biome.location().getPath(), name);
    }

    /**
     * Skill 此时未注册不能run
     */
    public void addSkill(String skill, String name, String tooltip) {
        this.add("skill." + SkilletManCoreMod.MOD_ID + "." + skill, name);
        this.add("skill." + SkilletManCoreMod.MOD_ID + "." + skill + ".tooltip", tooltip);
    }

    public void addSapling(String woodPrefix, String saplingName) {
        this.add("block." + SkilletManCoreMod.MOD_ID + woodPrefix + "_sapling", saplingName);
        this.add("block." + SkilletManCoreMod.MOD_ID + "potted_" + woodPrefix + "_sapling", "Potted " + saplingName);
    }

    public void createLogs(String woodPrefix, String woodName) {
        this.add("block." + SkilletManCoreMod.MOD_ID + "." + woodPrefix + "_log", woodName + " Log");
        this.add("block." + SkilletManCoreMod.MOD_ID + "." + woodPrefix + "_wood", woodName + " Wood");
        this.add("block." + SkilletManCoreMod.MOD_ID + ".stripped_" + woodPrefix + "_log", "Stripped " + woodName + " Log");
        this.add("block." + SkilletManCoreMod.MOD_ID + ".stripped_" + woodPrefix + "_wood", "Stripped " + woodName + " Wood");
        this.createHollowLogs(woodPrefix, woodName, false);
    }

    public void createHollowLogs(String woodPrefix, String woodName, boolean stem) {
        this.add("block." + SkilletManCoreMod.MOD_ID + ".hollow_" + woodPrefix + (stem ? "_stem" : "_log") + "_horizontal", "Hollow " + woodName + (stem ? " Stem" : " Log"));
        this.add("block." + SkilletManCoreMod.MOD_ID + ".hollow_" + woodPrefix + (stem ? "_stem" : "_log") + "_vertical", "Hollow " + woodName + (stem ? " Stem" : " Log"));
        this.add("block." + SkilletManCoreMod.MOD_ID + ".hollow_" + woodPrefix + (stem ? "_stem" : "_log") + "_climbable", "Hollow " + woodName + (stem ? " Stem" : " Log"));
    }

    public void createWoodSet(String woodPrefix, String woodName) {
        this.add("block." + SkilletManCoreMod.MOD_ID + "." + woodPrefix + "_planks", woodName + " Planks");
        this.add("block." + SkilletManCoreMod.MOD_ID + "." + woodPrefix + "_slab", woodName + " Slab");
        this.add("block." + SkilletManCoreMod.MOD_ID + "." + woodPrefix + "_stairs", woodName + " Stairs");
        this.add("block." + SkilletManCoreMod.MOD_ID + "." + woodPrefix + "_button", woodName + " Button");
        this.add("block." + SkilletManCoreMod.MOD_ID + "." + woodPrefix + "_fence", woodName + " Fence");
        this.add("block." + SkilletManCoreMod.MOD_ID + "." + woodPrefix + "_fence_gate", woodName + " Fence Gate");
        this.add("block." + SkilletManCoreMod.MOD_ID + "." + woodPrefix + "_pressure_plate", woodName + " Pressure Plate");
        this.add("block." + SkilletManCoreMod.MOD_ID + "." + woodPrefix + "_trapdoor", woodName + " Trapdoor");
        this.add("block." + SkilletManCoreMod.MOD_ID + "." + woodPrefix + "_door", woodName + " Door");
        this.add("block." + SkilletManCoreMod.MOD_ID + "." + woodPrefix + "_sign", woodName + " Sign");
        this.add("block." + SkilletManCoreMod.MOD_ID + "." + woodPrefix + "_wall_sign", woodName + " Wall Sign");
        this.add("block." + SkilletManCoreMod.MOD_ID + "." + woodPrefix + "_banister", woodName + " Banister");
        this.add("block." + SkilletManCoreMod.MOD_ID + "." + woodPrefix + "_chest", woodName + " Chest");
        this.add("item." + SkilletManCoreMod.MOD_ID + "." + woodPrefix + "_boat", woodName + " Boat");
        this.add("item." + SkilletManCoreMod.MOD_ID + "." + woodPrefix + "_chest_boat", woodName + " Chest Boat");
        this.add("block." + SkilletManCoreMod.MOD_ID + "." + woodPrefix + "_hanging_sign", woodName + " Hanging Sign");
        this.add("block." + SkilletManCoreMod.MOD_ID + "." + woodPrefix + "_wall_hanging_sign", woodName + " Wall Hanging Sign");
    }

    public void addBannerPattern(String patternPrefix, String patternName) {
        this.add("item." + SkilletManCoreMod.MOD_ID + "." + patternPrefix + "_banner_pattern", "Banner Pattern");
        this.add("item." + SkilletManCoreMod.MOD_ID + "." + patternPrefix + "_banner_pattern.desc", patternName);
        for (DyeColor color : DyeColor.values()) {
            this.add("block.minecraft.banner." + SkilletManCoreMod.MOD_ID + "." + patternPrefix + "." + color.getName(), WordUtils.capitalize(color.getName().replace('_', ' ')) + " " + patternName);
        }
    }

    public void addStoneVariants(String blockKey, String blockName) {
        this.add("block." + SkilletManCoreMod.MOD_ID + "." + blockKey, blockName);
        this.add("block." + SkilletManCoreMod.MOD_ID + ".cracked_" + blockKey, "Cracked " + blockName);
        this.add("block." + SkilletManCoreMod.MOD_ID + ".mossy_" + blockKey, "Mossy " + blockName);
    }

    public void addArmor(String itemKey, String item) {
        this.add("item." + SkilletManCoreMod.MOD_ID + "." + itemKey + "_helmet", item + " Helmet");
        this.add("item." + SkilletManCoreMod.MOD_ID + "." + itemKey + "_chestplate", item + " Chestplate");
        this.add("item." + SkilletManCoreMod.MOD_ID + "." + itemKey + "_leggings", item + " Leggings");
        this.add("item." + SkilletManCoreMod.MOD_ID + "." + itemKey + "_boots", item + " Boots");
    }

    public void addTools(String itemKey, String item) {
        this.add("item." + SkilletManCoreMod.MOD_ID + "." + itemKey + "_sword", item + " Sword");
        this.add("item." + SkilletManCoreMod.MOD_ID + "." + itemKey + "_pickaxe", item + " Pickaxe");
        this.add("item." + SkilletManCoreMod.MOD_ID + "." + itemKey + "_axe", item + " Axe");
        this.add("item." + SkilletManCoreMod.MOD_ID + "." + itemKey + "_shovel", item + " Shovel");
        this.add("item." + SkilletManCoreMod.MOD_ID + "." + itemKey + "_hoe", item + " Hoe");
    }

    public void addMusicDisc(Supplier<Item> disc, String description) {
        this.addItem(disc, "Music Disc");
        this.add(disc.get().getDescriptionId() + ".desc", description);
    }

    public void addStructure(ResourceKey<Structure> biome, String name) {
        this.add("structure."+ SkilletManCoreMod.MOD_ID + "." + biome.location().getPath(), name);
    }

    public void addAdvancement(String key, String title, String desc) {
        this.add("advancement."+ SkilletManCoreMod.MOD_ID + "." + key, title);
        this.add("advancement."+ SkilletManCoreMod.MOD_ID + "." + key + ".desc", desc);
    }

    public void addEnchantment(String key, String title, String desc) {
        this.add("enchantment."+ SkilletManCoreMod.MOD_ID + "." + key, title);
        this.add("enchantment."+ SkilletManCoreMod.MOD_ID + "." + key + ".desc", desc);
    }

    public void addEntityAndEgg(RegistryObject<? extends EntityType<?>> entity, String name) {
        this.addEntityType(entity, name);
        this.add("item." + SkilletManCoreMod.MOD_ID + "." + entity.getId().getPath() + "_spawn_egg", name + "刷怪蛋");
    }

    public void addEntityShaderName(RegistryObject<? extends EntityType<?>> entity, String name) {
        this.add(entity.get().getDescriptionId()+"_shader", name);
    }

    public void addEntityType(Supplier<? extends EntityType<?>> key, int i, String name) {
        add(key.get().getDescriptionId()+i, name);
    }

    public void addScreenName(String key, String name) {
        this.add("screen.smc." + key, name);
    }

    public void addScreenAns(String key, int id, String text) {
        this.add("screen.smc.ans." + key + "_" + id, text);
    }

    public void addScreenOpt(String key, int id, String text) {
        this.add("screen.smc.opt." + key + "_" + id, text);
    }
    public void addVillagerName(VillagerProfession profession, String text) {
        this.add("villager.smc." + profession.name().toLowerCase(Locale.ROOT) + ".name", text);
    }
    public void addVillagerAns(VillagerProfession profession, int id, String text) {
        this.add("villager.smc." + profession.name().toLowerCase(Locale.ROOT) + ".ans." + id, text);
    }

    public void addVillagerOpt(VillagerProfession profession, int id, String text) {
        this.add("villager.smc." + profession.name().toLowerCase(Locale.ROOT) + ".opt." + id, text);
    }

    public void addDialog(RegistryObject<? extends EntityType<?>> entity, int i, String text) {
        this.add(entity.get()+".dialog" + i, text);
    }

    public void addDialog(RegistryObject<? extends EntityType<?>> entity, String answer, String text) {
        this.add(entity.get()+".dialog" + answer, text);
    }

    public void addDialogChoice(RegistryObject<? extends EntityType<?>> entity, int i, String text) {
        this.add(entity.get()+".choice" + i, text);
    }

    public void addDialogChoice(RegistryObject<? extends EntityType<?>> entity, String choice, String text) {
        this.add(entity.get()+".choice." + choice, text);
    }

    public void addSubtitle(RegistryObject<SoundEvent> sound, String name) {
        String[] splitSoundName  = sound.getId().getPath().split("\\.", 3);
        this.add("subtitles."+ SkilletManCoreMod.MOD_ID + "." + splitSoundName[0] + "." + splitSoundName[2], name);
    }

    public void addDeathMessage(String key, String name) {
        this.add("death.attack."+ SkilletManCoreMod.MOD_ID + "." + key, name);
    }

    public void addStat(String key, String name) {
        this.add("stat."+ SkilletManCoreMod.MOD_ID + "." + key, name);
    }

    public void addMessage(String key, String name) {
        this.add("misc."+ SkilletManCoreMod.MOD_ID + "." + key, name);
    }

    public void addCommand(String key, String name) {
        this.add("commands.tcrfeature." + key, name);
    }

    public void addTrim(String key, String name) {
        this.add("trim_material."+ SkilletManCoreMod.MOD_ID + "." + key, name + " Material");
    }

    public void addBookAndContents(String bookKey, String bookTitle, String... pages) {
        this.add(SkilletManCoreMod.MOD_ID+".book.author." + bookKey, "无名氏");
        this.add(SkilletManCoreMod.MOD_ID+".book." + bookKey, bookTitle);
        int pageCount = 0;
        for (String page : pages) {
            pageCount++;
            this.add(SkilletManCoreMod.MOD_ID+".book." + bookKey + "." + pageCount, page);
        }
    }

    public void addBookAndAuthorAndContents(String bookKey, String author, String bookTitle, String... pages) {
        this.add(SkilletManCoreMod.MOD_ID+".book.author." + bookKey, author);
        this.add(SkilletManCoreMod.MOD_ID+".book." + bookKey, bookTitle);
        int pageCount = 0;
        for (String page : pages) {
            pageCount++;
            this.add(SkilletManCoreMod.MOD_ID+".book." + bookKey + "." + pageCount, page);
        }
    }

    public void addScreenMessage(String key, String name) {
        this.add("gui."+ SkilletManCoreMod.MOD_ID + "." + key, name);
    }

    public void addConfig(String key, String name) {
        this.add("config."+ SkilletManCoreMod.MOD_ID+".common." + key, name);
    }

    public void addDrinkName(Item item, String name) {
        this.add(item.getDescriptionId()+".effect.empty", name);
    }
    public void addDrinkName(Item item, String effect,String name) {
        this.add(item.getDescriptionId()+".effect."+effect, name);
    }


    public void addItemInfo(Item item, String key, String name) {
        this.add(item.getDescriptionId()+"." + key, name);
    }

    public void addItemUsageInfo(Item item, String name) {
        this.add(item.getDescriptionId()+".usage", name);
    }
    public void addItemUsageInfo(Item item, String name, int index) {
        this.add(item.getDescriptionId()+".usage"+index, name);
    }

}

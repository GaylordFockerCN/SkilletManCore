package com.p1nero.smc.util;

import com.p1nero.smc.SkilletManCoreMod;
import com.p1nero.smc.datagen.lang.SMCLangGenerator;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;

import java.awt.print.Book;

/**
 * 用于管理书籍。适用于掉落的书籍。如果要加到箱子里还是手动放吧
 * 顺便一提，这里有个好工具：
 * <a href="http://minecraft.tools/en/book.php">Minecraft Book Editor</a>
 *
 * @author P1nero
 */
public class BookManager {

    /**
     * 获取书籍，key和LangGenerator里面的bookKey对应。
     * {@link SMCLangGenerator#addBookAndContents(String bookKey, String bookTitle, String... pages)}
     */
    public static ItemStack getBook(String key, int pageCount, int old) {
        ItemStack book = new ItemStack(Items.WRITTEN_BOOK);
        book.getOrCreateTag().putBoolean(SkilletManCoreMod.MOD_ID + ":book", true);
        ListTag bookPages = new ListTag();

        for (int i = 1; i <= pageCount; i++)
            bookPages.add(StringTag.valueOf(Component.Serializer.toJson(Component.translatable(SkilletManCoreMod.MOD_ID + ".book." + key + "." + i))));

        book.addTagElement("pages", bookPages);//各页
        book.addTagElement("generation", IntTag.valueOf(old));//破损度
        book.addTagElement("author", StringTag.valueOf(Component.Serializer.toJson(Component.translatable(SkilletManCoreMod.MOD_ID + ".book.author." + key))));
        book.addTagElement("title", StringTag.valueOf(Component.Serializer.toJson(Component.translatable(SkilletManCoreMod.MOD_ID + ".book." + key))));
        return book;
    }

    public static String defaultJson(String original) {
        return "{\"text\":\"" + original + "\"}";
    }

    public static ItemStack getBook(int old, String author, String title, String... pages) {
        ItemStack book = new ItemStack(Items.WRITTEN_BOOK);
        book.getOrCreateTag().putBoolean(SkilletManCoreMod.MOD_ID + ":book", true);
        ListTag bookPages = new ListTag();

        for (String page : pages) {
            bookPages.add(StringTag.valueOf(page));
        }

        book.addTagElement("pages", bookPages);//各页
        book.addTagElement("generation", IntTag.valueOf(old));//破损度
        book.addTagElement("author", StringTag.valueOf(author));
        book.addTagElement("title", StringTag.valueOf(title));
        return book;
    }

    public static ItemStack getDefaultTextBook(int old, String author, String title, String... pages) {
        ItemStack book = new ItemStack(Items.WRITTEN_BOOK);
        book.getOrCreateTag().putBoolean(SkilletManCoreMod.MOD_ID + ":book", true);
        ListTag bookPages = new ListTag();

        for (String page : pages) {
            bookPages.add(StringTag.valueOf(defaultJson(page)));
        }

        book.addTagElement("pages", bookPages);//各页
        book.addTagElement("generation", IntTag.valueOf(old));//破损度
        book.addTagElement("author", StringTag.valueOf(author));
        book.addTagElement("title", StringTag.valueOf(title));
        return book;
    }

}

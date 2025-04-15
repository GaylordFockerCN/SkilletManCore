package com.p1nero.smc.util;

import com.p1nero.smc.SkilletManCoreMod;
import com.p1nero.smc.datagen.lang.SMCLangGenerator;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;

/**
 * 用于管理书籍。适用于掉落的书籍。如果要加到箱子里还是手动放吧
 * 顺便一提，这里有个好工具：
 * <a href="http://minecraft.tools/en/book.php">Minecraft Book Editor</a>
 * @author LZY
 */
public class BookManager {
    public record Book(String name, int pageCount){
        public ItemStack get(){
            return getBook(name,pageCount);
        }
    }

    /**
     * 获取书籍，key和LangGenerator里面的bookKey对应。
     * {@link SMCLangGenerator#addBookAndContents(String bookKey, String bookTitle, String... pages)}
     */
    public static ItemStack getBook(String key, int pageCount){
        ItemStack book = new ItemStack(Items.WRITTEN_BOOK);
        book.getOrCreateTag().putBoolean(SkilletManCoreMod.MOD_ID + ":book", true);
        ListTag bookPages = new ListTag();

        for (int i = 1; i <= pageCount; i++)
            bookPages.add(StringTag.valueOf(Component.Serializer.toJson(Component.translatable(SkilletManCoreMod.MOD_ID + ".book." + key + "." + i))));

        book.addTagElement("pages", bookPages);//页数
        book.addTagElement("generation", IntTag.valueOf(3));//破损度
        //不用I18n不知道为什么书会失效..... FIXME 感觉用了I18n的话放服务端会有bug
        book.addTagElement("author", StringTag.valueOf(I18n.get(SkilletManCoreMod.MOD_ID + ".book.author." + key)));
        book.addTagElement("title", StringTag.valueOf(I18n.get(SkilletManCoreMod.MOD_ID + ".book." + key)));
        return book;
    }

}

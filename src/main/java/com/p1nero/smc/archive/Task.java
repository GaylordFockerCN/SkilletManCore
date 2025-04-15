package com.p1nero.smc.archive;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * 由名字和内容俩Component构成
 */
public class Task {
    private Component name, content;

    public Component getName() {
        return name;
    }

    public Component getContent() {
        return content;
    }

    public Task(Component name, Component content) {
        this.name = name;
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(name.getString(), task.name.getString()) && Objects.equals(content.getString(), task.content.getString());
    }

    @Override
    public int hashCode() {
        if(name == null || content == null){
            return 0;
        }
        return Objects.hash(name.getString(), content.getString());
    }

    public Task setNameChatFormatting(ChatFormatting... chatFormatting){
        this.name = this.name.copy().withStyle(chatFormatting);
        return this;
    }

    public Task setContentChatFormatting(ChatFormatting... chatFormatting){
        this.content = this.content.copy().withStyle(chatFormatting);
        return this;
    }

    @NotNull
    public CompoundTag toNbt(){
        CompoundTag dialog = new CompoundTag();
        dialog.putString("message", Component.Serializer.toJson(name));
        dialog.putString("content", Component.Serializer.toJson(content));
        return dialog;
    }

    public static Task fromNbt(CompoundTag dialog){
        System.out.println(Component.Serializer.fromJson(dialog.getString("message")) + ", " +  Component.Serializer.fromJson(dialog.getString("content")));
        return new Task(Component.Serializer.fromJson(dialog.getString("message")), Component.Serializer.fromJson(dialog.getString("content")));
    }

}
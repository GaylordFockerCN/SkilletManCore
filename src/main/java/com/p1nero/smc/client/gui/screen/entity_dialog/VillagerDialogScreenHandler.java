package com.p1nero.smc.client.gui.screen.entity_dialog;

import com.p1nero.smc.capability.SMCCapabilityProvider;
import com.p1nero.smc.client.gui.screen.LinkListStreamDialogueScreenBuilder;
import com.p1nero.smc.client.gui.screen.entity_dialog.profession_dialog.*;
import com.p1nero.smc.datagen.lang.SMCLangGenerator;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class VillagerDialogScreenHandler {
    private static final Map<VillagerProfession, VillagerDialogBuilder> PROFESSION_VILLAGER_DIALOG_BUILDER_MAP = new HashMap<>();
    static {
        PROFESSION_VILLAGER_DIALOG_BUILDER_MAP.put(VillagerProfession.NONE, new NoneDialogBuilder());
        PROFESSION_VILLAGER_DIALOG_BUILDER_MAP.put(VillagerProfession.NITWIT, new NitWitDialogBuilder());
        PROFESSION_VILLAGER_DIALOG_BUILDER_MAP.put(VillagerProfession.LIBRARIAN, new LibrarianDialogBuilder());
        PROFESSION_VILLAGER_DIALOG_BUILDER_MAP.put(VillagerProfession.FARMER, new FarmerDialogBuilder());
        PROFESSION_VILLAGER_DIALOG_BUILDER_MAP.put(VillagerProfession.FISHERMAN, new FishmanDialogBuilder());
        PROFESSION_VILLAGER_DIALOG_BUILDER_MAP.put(VillagerProfession.BUTCHER, new ButcherDialogBuilder());
        PROFESSION_VILLAGER_DIALOG_BUILDER_MAP.put(VillagerProfession.SHEPHERD, new ShepherdDialogBuilder());
        PROFESSION_VILLAGER_DIALOG_BUILDER_MAP.put(VillagerProfession.CARTOGRAPHER, new CartographerDialogBuilder());

    }

    public static void onLanguageGen(SMCLangGenerator generator) {
        PROFESSION_VILLAGER_DIALOG_BUILDER_MAP.forEach((profession, villagerDialogBuilder) -> villagerDialogBuilder.onGenerateLang(generator));
    }

    @OnlyIn(Dist.CLIENT)
    public static void addDialogScreen(Villager villager) {
        LinkListStreamDialogueScreenBuilder screenBuilder;
        VillagerDialogBuilder villagerDialogBuilder = PROFESSION_VILLAGER_DIALOG_BUILDER_MAP.get(villager.getVillagerData().getProfession());
        if(villagerDialogBuilder == null) {
            screenBuilder = new LinkListStreamDialogueScreenBuilder(villager);
            buildDefaultDialog(screenBuilder);
        } else {
            screenBuilder = new LinkListStreamDialogueScreenBuilder(villager, villagerDialogBuilder.getName());
            villagerDialogBuilder.createDialog(screenBuilder, villager);
        }
        if(!screenBuilder.isEmpty()) {
            Minecraft.getInstance().setScreen(screenBuilder.build());
        }
    }

    public static void handle(ServerPlayer serverPlayer, Villager villager, byte interactionID) {
        PROFESSION_VILLAGER_DIALOG_BUILDER_MAP.get(villager.getVillagerData().getProfession()).handle(serverPlayer, villager, interactionID);
        SMCCapabilityProvider.getSMCPlayer(serverPlayer).setCurrentTalkingEntity(null);
    }

    public static void buildDefaultDialog(LinkListStreamDialogueScreenBuilder builder) {
        builder.start(0)
                .addFinalChoice(0, (byte) -1);
    }

    public static abstract class VillagerDialogBuilder{
        protected final VillagerProfession profession;
        public VillagerDialogBuilder(VillagerProfession profession) {
            this.profession = profession;
        }

        public void onGenerateLang(SMCLangGenerator generator) {

        }

        @OnlyIn(Dist.CLIENT)
        public abstract void createDialog(LinkListStreamDialogueScreenBuilder builder, Villager self);

        public void handle(ServerPlayer serverPlayer, Villager villager, byte interactionID) {

        }

        public Component getName() {
            return Component.translatable("villager.smc." + profession.name().toLowerCase(Locale.ROOT) + ".name");
        }

        public Component answer(int id, Object... objects) {
            Component component = Component.translatable("villager.smc." + profession.name().toLowerCase(Locale.ROOT) + ".ans." + id, objects);
            return Component.literal("\n").append(component);
        }

        public Component choice(int id, Object... objects) {
            return Component.translatable("villager.smc." + profession.name().toLowerCase(Locale.ROOT) + ".opt." + id, objects);
        }

    }

}

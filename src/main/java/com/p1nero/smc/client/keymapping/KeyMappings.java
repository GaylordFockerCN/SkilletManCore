package com.p1nero.smc.client.keymapping;

import com.p1nero.smc.SMCConfig;
import com.p1nero.smc.SkilletManCoreMod;
import com.p1nero.smc.archive.DataManager;
import com.p1nero.smc.network.SMCPacketHandler;
import com.p1nero.smc.network.PacketRelay;
import com.p1nero.smc.network.packet.serverbound.RequestExitSpectatorPacket;
import com.p1nero.smc.worldgen.dimension.SMCDimension;
import de.keksuccino.konkrete.json.jsonpath.internal.function.numeric.Min;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = {Dist.CLIENT})
public class KeyMappings {

	public static final MyKeyMapping SHOW_HINT = new MyKeyMapping(buildKey("show_hint"), GLFW.GLFW_KEY_J, "key.categories.smc");

	public static String buildKey(String name){
		return "key.smc." + name;
	}

	@SubscribeEvent
	public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
		event.register(SHOW_HINT);
	}

	@Mod.EventBusSubscriber(modid = SkilletManCoreMod.MOD_ID, value = Dist.CLIENT)
	public static class KeyPressHandler {

		@SubscribeEvent
		public static void onClientTick(TickEvent.ClientTickEvent event) {
			if(event.phase.equals(TickEvent.Phase.END)){
				while (SHOW_HINT.consumeClick()){
					if (Minecraft.getInstance().player != null && Minecraft.getInstance().screen == null && !Minecraft.getInstance().isPaused()) {
						SMCConfig.SHOW_HINT.set(!SMCConfig.SHOW_HINT.get());
						if(!SMCConfig.SHOW_HINT.get() && DataManager.hintUpdated.get(Minecraft.getInstance().player)) {
							DataManager.hintUpdated.put(Minecraft.getInstance().player, false);
						}
					}
				}
			}
		}

	}

}

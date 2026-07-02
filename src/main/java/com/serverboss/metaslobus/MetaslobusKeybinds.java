package com.serverboss.metaslobus;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fmlclient.registry.ClientRegistry;

@Mod.EventBusSubscriber(
	bus = Mod.EventBusSubscriber.Bus.FORGE,
	value = Dist.CLIENT
)
public final class MetaslobusKeybinds {

	private static final Minecraft MINECRAFT = Minecraft.getInstance();

	public static void register() {
		ClientRegistry.registerKeyBinding(CUSTOM_KEY);
	}

	public static final KeyMapping CUSTOM_KEY = new KeyMapping(
		"key.metaslobus.custom",
		KeyConflictContext.IN_GAME,
		InputConstants.Type.KEYSYM,
		InputConstants.KEY_G,
		"key.categories.metaslobus"
	);

	@SubscribeEvent
	public static void onKeyInput(InputEvent.KeyInputEvent event) {
		if (CUSTOM_KEY.consumeClick()) {
			ClientPacketListener connection = MINECRAFT.getConnection();
			if (connection != null) connection.send(new ServerbossCustomPacket("test"));
		}
	}

}

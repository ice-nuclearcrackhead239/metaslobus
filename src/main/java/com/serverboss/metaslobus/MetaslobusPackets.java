package com.serverboss.metaslobus;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class MetaslobusPackets {

	public static float SCOPE_UNDERSCORE_VALUE = 1.0F;

	public static void register() {
		ServerbossPacketManager.register(new ResourceLocation("svbcr", "shader"), MetaslobusPackets::setShader);
		ServerbossPacketManager.register(new ResourceLocation("svbcr", "scope"), MetaslobusPackets::setScope);
	}

	public static void setShader(FriendlyByteBuf buf) throws IndexOutOfBoundsException {
		ResourceLocation shaderPath = ResourceLocation.tryParse("shaders/post/" + buf.readUtf() + ".json");
		if (shaderPath == null) return;

		Minecraft.getInstance().gameRenderer.loadEffect(shaderPath);
	}

	public static void setScope(FriendlyByteBuf buf) throws IndexOutOfBoundsException {
		SCOPE_UNDERSCORE_VALUE = Float.parseFloat(buf.readUtf());
	}
}

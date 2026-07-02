package com.serverboss.metaslobus;

import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ServerboundCustomPayloadPacket;
import net.minecraft.resources.ResourceLocation;

public class ServerbossCustomPacket extends ServerboundCustomPayloadPacket {

	public ServerbossCustomPacket(String name) {
		super(new ResourceLocation("svbcr", name), new FriendlyByteBuf(Unpooled.EMPTY_BUFFER));
	}

	public ServerbossCustomPacket(String name, FriendlyByteBuf buf) {
		super(new ResourceLocation("svbcr", name), buf);
	}

	public static ServerbossCustomPacket of(String name, String data) {
		FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
		buf.writeUtf(data);
		return new ServerbossCustomPacket(name, buf);
	}

}

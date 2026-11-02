package com.serverboss.metaslobus.mixin;

import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientboundSetEntityMotionPacket.class)
public class FPMixin { //partially ripped from FastProjectileFix but ported to 1.17.1, only one half of this is active so its not a true fix its more of a mild balm applied to the wound
	@Final
	@Shadow
	@Mutable
	private int xa;

	@Final
	@Shadow
	@Mutable
	private int ya;

	@Final
	@Shadow
	@Mutable
	private int za;

	@Inject(method = "<init>(ILnet/minecraft/world/phys/Vec3;)V", at = @At(value = "RETURN"))
	public void ClientboundSetEntityMotionPacket(int entityId, Vec3 motionVector, CallbackInfo ci) {
		this.xa = (int)(motionVector.x * 8000.0D);
		this.ya = (int)(motionVector.y * 8000.0D);
		this.za = (int)(motionVector.z * 8000.0D);
	}
}

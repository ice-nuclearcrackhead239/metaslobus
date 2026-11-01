package com.serverboss.metaslobus.mixin;

import com.serverboss.metaslobus.MetaslobusPackets;
import net.minecraft.client.player.AbstractClientPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractClientPlayer.class)
public class CopeMixin {
	@Inject(method="getFieldOfViewModifier", at=@At("HEAD"), cancellable = true)
	public void getFieldOfViewModifier(CallbackInfoReturnable<Float> cir) {
		if(MetaslobusPackets.SCOPE_UNDERSCORE_VALUE != 1){
			cir.setReturnValue(MetaslobusPackets.SCOPE_UNDERSCORE_VALUE);
		}
	}
}

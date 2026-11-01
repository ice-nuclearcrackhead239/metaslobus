package com.serverboss.metaslobus.mixin;

import com.serverboss.metaslobus.MetaslobusPackets;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public class PlayerMixin {
	@Inject(method="isScoping", at=@At("HEAD"), cancellable = true)
	public void isScoping(CallbackInfoReturnable<Boolean> cir) {
		if(MetaslobusPackets.SCOPE_UNDERSCORE_VALUE != 1){
			cir.setReturnValue(true);
		}
	}
}

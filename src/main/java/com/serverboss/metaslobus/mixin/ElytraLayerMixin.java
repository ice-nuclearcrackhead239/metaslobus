package com.serverboss.metaslobus.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.layers.ElytraLayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(ElytraLayer.class)
public abstract class ElytraLayerMixin<T extends LivingEntity, M> {

	@Unique private static final List<ResourceLocation> CMD_ELYTRA_CACHE = new ArrayList<>();

	@Inject(
		method = "getElytraTexture(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/LivingEntity;)Lnet/minecraft/resources/ResourceLocation;",
		at = @At("RETURN"),
		cancellable = true,
		remap = false
	)
	private void getCmdElytraTexture(ItemStack stack, T entity, CallbackInfoReturnable<ResourceLocation> cir) {
		int cmd = readCustomModelData(stack);
		if (cmd < 0) return;

		ResourceLocation location = cir.getReturnValue();
		location = new ResourceLocation(location.getNamespace(), location.getPath().replace("elytra", "elytra/" + cmd));
		if (CMD_ELYTRA_CACHE.contains(location)) {
			cir.setReturnValue(location);
			return;
		}

		try {
			if (Minecraft.getInstance().getResourceManager().hasResource(location)) {
				CMD_ELYTRA_CACHE.add(location);
				cir.setReturnValue(location);
			}
		} catch (Exception ignored) {}
	}

	@Unique
	private static int readCustomModelData(ItemStack stack) {
		if (!stack.hasTag()) return -1;

		CompoundTag tag = stack.getTag();
		if (tag == null) return -1;

		if (!tag.contains("CustomModelData")) return -1;
		return tag.getInt("CustomModelData");
	}

}

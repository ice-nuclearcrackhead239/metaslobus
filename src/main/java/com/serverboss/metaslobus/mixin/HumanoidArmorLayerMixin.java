package com.serverboss.metaslobus.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.ArrayList;
import java.util.List;

@Mixin(HumanoidArmorLayer.class)
public class HumanoidArmorLayerMixin {

	@Unique private static final List<String> CMD_ARMOR_CACHE = new ArrayList<>();

	@ModifyVariable(
		method = "getArmorResource(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/EquipmentSlot;Ljava/lang/String;)Lnet/minecraft/resources/ResourceLocation;",
		at = @At(value = "STORE", ordinal = 1),
		ordinal = 3,
		remap = false
	)
	private String getCmdArmorTexture(String original, Entity entity, ItemStack stack) {
		int cmd = readCustomModelData(stack);
		if (cmd < 0) return original;

		String path = original.replace("armor/", "armor/" + cmd + "/");
		if (path.isEmpty()) return original;

		if (CMD_ARMOR_CACHE.contains(path)) return path;

		try {
			ResourceLocation location = new ResourceLocation(path);
			if (Minecraft.getInstance().getResourceManager().hasResource(location)) {
				CMD_ARMOR_CACHE.add(path);
				return path;
			}
		} catch (Exception ignored) {}

		return original;
	}

	@Unique
	private static int readCustomModelData(ItemStack stack) {
		if (!stack.hasTag()) return -1;

		Item item = stack.getItem();
		if (!(item instanceof ArmorItem)) return -1;

		CompoundTag tag = stack.getTag();
		if (tag == null) return -1;

		if (!tag.contains("CustomModelData")) return -1;
		return tag.getInt("CustomModelData");
	}

}

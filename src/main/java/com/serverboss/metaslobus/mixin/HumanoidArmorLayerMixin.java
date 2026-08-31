package com.serverboss.metaslobus.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.ForgeHooksClient;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Mixin(HumanoidArmorLayer.class)
public class HumanoidArmorLayerMixin {

	@Unique private static final List<String> CMD_ARMOR_CACHE = new ArrayList<>();

	@Redirect(
		method = "getArmorResource",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraftforge/client/ForgeHooksClient;getArmorTexture(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/item/ItemStack;Ljava/lang/String;Lnet/minecraft/world/entity/EquipmentSlot;Ljava/lang/String;)Ljava/lang/String;"
		),
		remap = false
	)
	private String getCmdArmorTexture(Entity entity, ItemStack stack, String original, EquipmentSlot slot, String type) {
		int cmd = readCustomModelData(stack);
		if (cmd < 0) return ForgeHooksClient.getArmorTexture(entity, stack, original, slot, type);

		String path = original.replace("armor/", "armor/" + cmd + "/");
		if (path.isEmpty()) return ForgeHooksClient.getArmorTexture(entity, stack, original, slot, type);

		if (CMD_ARMOR_CACHE.contains(path)) return path;

		try {
			ResourceLocation location = new ResourceLocation(path);
			if (Minecraft.getInstance().getResourceManager().hasResource(location)) {
				CMD_ARMOR_CACHE.add(path);
				return path;
			}
		} catch (Exception ignored) {}

		return ForgeHooksClient.getArmorTexture(entity, stack, original, slot, type);
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

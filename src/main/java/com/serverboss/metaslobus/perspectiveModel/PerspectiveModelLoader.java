package com.serverboss.metaslobus.perspectiveModel;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraftforge.client.model.IModelLoader;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.Map;

public class PerspectiveModelLoader implements IModelLoader<PerspectiveModelGeometry> {

	private static final Map<String, ItemTransforms.TransformType> KEY_MAP = Map.of(
		"none", ItemTransforms.TransformType.NONE,
		"third_person_left_hand", ItemTransforms.TransformType.THIRD_PERSON_LEFT_HAND,
		"third_person_right_hand", ItemTransforms.TransformType.THIRD_PERSON_RIGHT_HAND,
		"first_person_left_hand", ItemTransforms.TransformType.FIRST_PERSON_LEFT_HAND,
		"first_person_right_hand", ItemTransforms.TransformType.FIRST_PERSON_RIGHT_HAND,
		"head", ItemTransforms.TransformType.HEAD,
		"gui", ItemTransforms.TransformType.GUI,
		"ground", ItemTransforms.TransformType.GROUND,
		"fixed", ItemTransforms.TransformType.FIXED
	);

	@Override
	public void onResourceManagerReload(@NotNull ResourceManager manager) {}

	@Override
	public @NotNull PerspectiveModelGeometry read(@NotNull JsonDeserializationContext ctx, @NotNull JsonObject json) {
		Map<ItemTransforms.TransformType, ResourceLocation> models = new EnumMap<>(ItemTransforms.TransformType.class);

		for (var entry : KEY_MAP.entrySet()) {
			if (json.has(entry.getKey())) {
				models.put(entry.getValue(), new ResourceLocation(json.get(entry.getKey()).getAsString()));
			}
		}

		ResourceLocation defaultModel = null;
		if (json.has("default")) {
			defaultModel = new ResourceLocation(json.get("default").getAsString());
		}

		if (models.isEmpty() && defaultModel == null) {
			throw new JsonParseException("perspective_model requires at least one perspective key or a default");
		}

		return new PerspectiveModelGeometry(models, defaultModel);
	}

}

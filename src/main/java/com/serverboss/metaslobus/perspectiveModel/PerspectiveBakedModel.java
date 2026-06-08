package com.serverboss.metaslobus.perspectiveModel;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraftforge.client.model.BakedModelWrapper;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class PerspectiveBakedModel extends BakedModelWrapper<BakedModel> {

	private final Map<ItemTransforms.TransformType, BakedModel> models;
	private final BakedModel fallback;

	public PerspectiveBakedModel(Map<ItemTransforms.TransformType, BakedModel> models, BakedModel fallback) {
		super(fallback);
		this.fallback = fallback;
		this.models = models;
	}

	@Override
	public @NotNull BakedModel handlePerspective(@NotNull ItemTransforms.TransformType type, @NotNull PoseStack poseStack) {
		return resolve(type).handlePerspective(type, poseStack);
	}

	private BakedModel resolve(ItemTransforms.TransformType type) {
		if (models.containsKey(type)) return models.get(type);
		return switch (type) {
			case FIRST_PERSON_LEFT_HAND -> models.getOrDefault(ItemTransforms.TransformType.FIRST_PERSON_RIGHT_HAND, fallback);
			case FIRST_PERSON_RIGHT_HAND -> models.getOrDefault(ItemTransforms.TransformType.FIRST_PERSON_LEFT_HAND, fallback);
			case THIRD_PERSON_LEFT_HAND -> models.getOrDefault(ItemTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, fallback);
			case THIRD_PERSON_RIGHT_HAND -> models.getOrDefault(ItemTransforms.TransformType.THIRD_PERSON_LEFT_HAND, fallback);
			default -> fallback;
		};
	}

}

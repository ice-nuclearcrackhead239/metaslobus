package com.serverboss.metaslobus.perspectiveModel;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraftforge.client.model.BakedModelWrapper;

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
	public BakedModel handlePerspective(ItemTransforms.TransformType type, PoseStack poseStack) {
		return this.resolve(type).handlePerspective(type, poseStack);
	}

	private BakedModel resolve(ItemTransforms.TransformType type) {
		if (this.models.containsKey(type)) return this.models.get(type);

		return switch (type) {
			case THIRD_PERSON_RIGHT_HAND, THIRD_PERSON_LEFT_HAND, FIRST_PERSON_RIGHT_HAND, FIRST_PERSON_LEFT_HAND -> this.models.getOrDefault(type, fallback);
			default -> this.fallback;
		};
	}

}

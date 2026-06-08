package com.serverboss.metaslobus.perspectiveModel;

import com.mojang.datafixers.util.Pair;
import java.util.*;
import java.util.function.Function;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.IModelConfiguration;
import net.minecraftforge.client.model.geometry.IModelGeometry;
import org.jetbrains.annotations.Nullable;

public class PerspectiveModelGeometry
	implements IModelGeometry<PerspectiveModelGeometry>
{

	private final Map<ItemTransforms.TransformType, ResourceLocation> models;

	@Nullable
	private final ResourceLocation defaultModel;

	public PerspectiveModelGeometry(
		Map<ItemTransforms.TransformType, ResourceLocation> models,
		@Nullable ResourceLocation defaultModel
	) {
		this.models = models;
		this.defaultModel = defaultModel;
	}

	@Override
	public BakedModel bake(
		IModelConfiguration owner,
		ModelBakery bakery,
		Function<Material, TextureAtlasSprite> spriteGetter,
		ModelState modelTransform,
		ItemOverrides overrides,
		ResourceLocation modelLocation
	) {
		Map<ItemTransforms.TransformType, BakedModel> baked = new EnumMap<>(
			ItemTransforms.TransformType.class
		);
		for (var entry : models.entrySet()) {
			UnbakedModel unbaked = bakery.getModel(entry.getValue());
			baked.put(
				entry.getKey(),
				unbaked.bake(
					bakery,
					spriteGetter,
					modelTransform,
					entry.getValue()
				)
			);
		}

		BakedModel bakedDefault;
		if (defaultModel != null) {
			bakedDefault = bakery
				.getModel(defaultModel)
				.bake(bakery, spriteGetter, modelTransform, defaultModel);
		} else {
			bakedDefault = pickDefault(baked);
		}

		return new PerspectiveBakedModel(baked, bakedDefault);
	}

	@Override
	public Collection<Material> getTextures(
		IModelConfiguration iModelConfiguration,
		Function<ResourceLocation, UnbakedModel> function,
		Set<Pair<String, String>> set
	) {
		Set<Material> textures = new HashSet<>();

		for (ResourceLocation modelLocation : models.values()) {
			UnbakedModel unbaked = function.apply(modelLocation);
			if (unbaked != null) {
				textures.addAll(unbaked.getMaterials(function, set));
			}
		}

		if (defaultModel != null && !models.containsValue(defaultModel)) {
			UnbakedModel unbaked = function.apply(defaultModel);
			if (unbaked != null) {
				textures.addAll(unbaked.getMaterials(function, set));
			}
		}

		return textures;
	}

	private static BakedModel pickDefault(
		Map<ItemTransforms.TransformType, BakedModel> models
	) {
		return models.getOrDefault(
			ItemTransforms.TransformType.GUI,
			models.getOrDefault(
				ItemTransforms.TransformType.GROUND,
				models.values().iterator().next()
			)
		);
	}
}

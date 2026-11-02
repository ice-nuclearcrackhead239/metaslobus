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

public class PerspectiveModelGeometry implements IModelGeometry<PerspectiveModelGeometry> {

	protected final Map<ItemTransforms.TransformType, ResourceLocation> models;

	protected final ResourceLocation defaultModel;

	public PerspectiveModelGeometry(
		Map<ItemTransforms.TransformType, ResourceLocation> models, ResourceLocation defaultModel
	) {
		this.models = models;
		this.defaultModel = defaultModel;
	}

	@Override
	public BakedModel bake(
		IModelConfiguration owner, ModelBakery bakery, Function<Material, TextureAtlasSprite> spriteGetter,
		ModelState modelTransform, ItemOverrides overrides, ResourceLocation modelLocation
	) {
		Map<ItemTransforms.TransformType, BakedModel> baked = new EnumMap<>(ItemTransforms.TransformType.class);
		this.models.forEach((type, transform) -> {
			UnbakedModel unbaked = bakery.getModel(transform);
			baked.put(type, unbaked.bake(bakery, spriteGetter, modelTransform, transform));
		});

		BakedModel bakedDefault;
		if (this.defaultModel != null) {
			bakedDefault = bakery.getModel(this.defaultModel).bake(bakery, spriteGetter, modelTransform, this.defaultModel);
		} else {
			bakedDefault = pickDefault(baked);
		}

		return new PerspectiveBakedModel(baked, bakedDefault);
	}

	@Override
	public Collection<Material> getTextures(
		IModelConfiguration iModelConfiguration, Function<ResourceLocation, UnbakedModel> function, Set<Pair<String, String>> set
	) {
		Set<Material> textures = new HashSet<>();

		this.models.values().forEach(location -> {
			UnbakedModel unbaked = function.apply(location);
			if (unbaked != null) textures.addAll(unbaked.getMaterials(function, set));
		});

		if (this.defaultModel != null && !this.models.containsValue(this.defaultModel)) {
			UnbakedModel unbaked = function.apply(this.defaultModel);
			if (unbaked != null) textures.addAll(unbaked.getMaterials(function, set));
		}

		return textures;
	}

	public static BakedModel pickDefault(Map<ItemTransforms.TransformType, BakedModel> models) {
		return models.getOrDefault(ItemTransforms.TransformType.GUI,
			models.getOrDefault(ItemTransforms.TransformType.GROUND, models.values().iterator().next())
		);
	}

}

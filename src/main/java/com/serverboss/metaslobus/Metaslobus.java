package com.serverboss.metaslobus;

import com.serverboss.metaslobus.perspectiveModel.PerspectiveModelLoader;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod(Metaslobus.MOD_ID)
public final class Metaslobus {

	public static final String MOD_ID = "metaslobus";

	public static ResourceLocation of(String name) {
		return new ResourceLocation(MOD_ID, name);
	}

	@Mod.EventBusSubscriber(
		bus = Mod.EventBusSubscriber.Bus.MOD,
		value = Dist.CLIENT
	)
	public static class ClientEvents {

		@SubscribeEvent
		public static void onModelLoaderRegistry(ModelRegistryEvent event) {
			ModelLoaderRegistry.registerLoader(
				new ResourceLocation(MOD_ID, "perspective_model"),
				new PerspectiveModelLoader()
			);
			MetaslobusKeybinds.register();
			System.out.println("HOW MUCH");
			System.out.println("LONGER NOW...?");
			System.out.println("MY");
			System.out.println("METASLOBUS.");
		}

	}
}

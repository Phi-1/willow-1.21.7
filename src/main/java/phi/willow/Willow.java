package phi.willow;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import phi.willow.registry.*;

public class Willow implements ModInitializer {

	// TODO: Item#onItemEntityDestroyed is a really cool event for custom crafting shenanigans
	// TODO: HUD indicators when holding/wearing stuff you dont have proficiency for?
	// TODO: block tool level requirements for drops
    // TODO: manuals
    // TODO: finish screen
	// TODO: don't forget to add new tools to base minecraft tags, sledgehammer to pickaxes.json and such
	// TODO: importing client classes crashes server, do a check
    // TODO: fighting xp

	public static final String MOD_ID = "willow";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		WillowItems.initialize();
		WillowTags.initialize();
		WillowEffectsAndPotions.registerPotionRecipes();
		WillowEvents.register();
		WillowNetworking.initialize();
	}
}
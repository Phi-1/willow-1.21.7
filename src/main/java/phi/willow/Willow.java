package phi.willow;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import phi.willow.registry.*;

public class Willow implements ModInitializer {

    // TODO: add manuals to librarian trades
    // TODO: needs_diamond_tool blocks, iron is done for now
	// TODO: Item#onItemEntityDestroyed is a really cool event for custom crafting shenanigans
	// TODO: HUD indicators when holding/wearing stuff you dont have proficiency for?
	// TODO: don't forget to add new tools to base minecraft tags, sledgehammer to pickaxes.json and such
	// TODO: importing client classes crashes server, do a check
    // TODO: fruit salad (apple, melon, honey, beetroot and make it a normal salad?), pumpkin food, beetroot food (vegetable curry with pumpkin and potato, carrot?), highland stew (mutton, carrot, ?)
    // TODO: Mixin to disable rocket powered elytra flight
    // TODO: Limit inventory, backpack upgrades, refill blocks from nearby chest/minecart while building. PlayerInventory#getEmptySlot & probably ItemStack#isEmpty (then getemptyslot isnt necessary) &
    // TODO: brick layer item that places blocks from your other hand, with aoe options
    // TODO: lengthen days? -> based on season?
    // TODO: chance to keep rain after sleep, longer lasting rain? -> ServerWorld line 365
    // TODO: either disable bundles or make them use rabbit hide instead of leather
    // TODO: honey food -> some kind of sandwich? apple pie?
    // TODO: method of increasing block interaction range, gravity, step height, jump strength, mining efficiency
    // TODO: expertise levels? After Master, just level up numerically, gaining increased stats
    // TODO: add manuals to terralith loot tables

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
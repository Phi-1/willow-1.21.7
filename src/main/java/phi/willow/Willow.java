package phi.willow;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import phi.willow.data.WillowPersistentState;
import phi.willow.registry.WillowEvents;
import phi.willow.registry.WillowItems;
import phi.willow.registry.WillowNetworking;
import phi.willow.registry.WillowTags;
import sereneseasons.api.season.SeasonHelper;

import java.util.Optional;

public class Willow implements ModInitializer {

	// TODO: keep some test feedback on xp gain for a while, to see if any blocks are missing from the list
	// TODO: little tool icon with +number that floats away in corner of screen when breaking block or getting fighting xp?
	// TODO: feedback on profession levelup with sound
	// TODO: match blocks in gives_{profession}_xp tag with needs_{tier}_tool tag, store in ram for quick read, so xp quick to calc
	// ^ though BlockState.isIn() derives from java.Set, so maybe that's pretty fast already
	// TODO: figure out what to do with netherite tools, in master tier or add tier?
	// TODO: add custom tools to minecraft item tags for tools
	// TODO: sync player state data on xp gain
	// TODO: Item#onItemEntityDestroyed is a really cool event for custom crafting shenanigans
	// TODO: flint tool recipes
	// TODO: tooltips for profession level requirements
	// TODO: block trident throwing unless proficient
	// TODO: HUD indicators when holding/wearing stuff you dont have proficiency for?

	public static final String MOD_ID = "willow";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		WillowItems.initialize();
		WillowTags.initialize();
		WillowEvents.register();
		WillowNetworking.initialize();
	}
}
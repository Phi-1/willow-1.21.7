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
import phi.willow.registry.WillowItems;
import sereneseasons.api.season.SeasonHelper;

import java.util.Optional;

public class Willow implements ModInitializer {

	// TODO: apple blossom block
	// TODO: keep some test feedback on xp gain for a while, to see if any blocks are missing from the list
	// TODO: little tool icon with +number that floats away in corner of screen when breaking block or getting fighting xp?
	// TODO: feedback on profession levelup with sound
	// TODO: match blocks in gives_{profession}_xp tag with needs_{tier}_tool tag, store in ram for quick read, so xp quick to calc
	// ^ though BlockState.isIn() derives from java.Set, so maybe that's pretty fast already

	public static final String MOD_ID = "willow";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		PlayerBlockBreakEvents.AFTER.register((world, player, pos, blockState, blockEntity) -> {
			if (world.isClient)
				return;
			MinecraftServer server = world.getServer();
			if (server == null)
				return;
			WillowPersistentState state = WillowPersistentState.getServerState(server);
			state.getTestData().testNumber++;
			player.sendMessage(Text.literal(Integer.toString(state.getTestData().testNumber)), true);
			player.sendMessage(Text.literal(SeasonHelper.getSeasonState(world).getSeason().name()), false);
		});
		LootTableEvents.MODIFY.register(((key, tableBuilder, source, registries) -> {
			Optional<RegistryKey<LootTable>> zombie = EntityType.ZOMBIE.getLootTableKey();
			if (zombie.isEmpty())
				return;
			if (key == zombie.get() || LootTables.VILLAGE_WEAPONSMITH_CHEST == key) {
				tableBuilder.pool(LootPool.builder().with(ItemEntry.builder(Items.ANCIENT_DEBRIS)));
			}
		}));
		WillowItems.initialize();
	}
}
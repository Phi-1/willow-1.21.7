package phi.willow;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import phi.willow.data.WillowPersistentState;
import phi.willow.registry.WillowItems;

public class Willow implements ModInitializer {
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
		});
		WillowItems.initialize();
	}
}
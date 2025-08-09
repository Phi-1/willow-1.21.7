package phi.willow;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.text.Text;
import phi.willow.data.PlayerProfessionState;
import phi.willow.data.client.SyncedProfessionState;
import phi.willow.registry.WillowTags;

public class WillowClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ClientPlayNetworking.registerGlobalReceiver(PlayerProfessionState.PlayerProfessionStatePayload.ID, (payload, context) -> {
			// TODO: compute difference
			SyncedProfessionState.state = payload.state();
			Willow.LOGGER.info("SYNCED");
		});

		ItemTooltipCallback.EVENT.register((stack, context, type, lines) -> {
			// TODO: replace with text.translatable
			if (stack.isIn(WillowTags.Items.NOVICE_USABLE_TOOLS))
				lines.add(Text.literal("Novice").withColor(0x93e2b4));
			else if (stack.isIn(WillowTags.Items.APPRENTICE_USABLE_TOOLS))
				lines.add(Text.literal("Apprentice").withColor(0x93e2b4));
			else if (stack.isIn(WillowTags.Items.EXPERT_USABLE_TOOLS))
				lines.add(Text.literal("Expert").withColor(0x93e2b4));
			else if (stack.isIn(WillowTags.Items.MASTER_USABLE_TOOLS))
				lines.add(Text.literal("Master").withColor(0x93e2b4));
		});
	}
}
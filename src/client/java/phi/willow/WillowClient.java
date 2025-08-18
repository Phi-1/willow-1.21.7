package phi.willow;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import oshi.util.tuples.Pair;
import phi.willow.clienthooks.ScreenOpeningHooks;
import phi.willow.data.PlayerProfessionState;
import phi.willow.data.Profession;
import phi.willow.data.ProfessionLevel;
import phi.willow.data.client.SyncedProfessionState;
import phi.willow.registry.WillowTags;
import phi.willow.screens.LogbookScreen;
import phi.willow.util.ProfessionUtil;

import java.util.Map;

public class WillowClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		initializeClientHooks();

		ClientPlayNetworking.registerGlobalReceiver(PlayerProfessionState.PlayerProfessionStatePayload.ID, (payload, context) -> {
			// TODO: compute difference
			SyncedProfessionState.state = payload.state();
			Willow.LOGGER.info("SYNCED");
		});

		ItemTooltipCallback.EVENT.register((stack, context, type, lines) -> {
			if (stack.isIn(WillowTags.Items.NOVICE_USABLE_EQUIPMENT))
				lines.add(Text.translatable("willow.profession_tooltip_novice").withColor(0x93e2b4));
			else if (stack.isIn(WillowTags.Items.APPRENTICE_USABLE_EQUIPMENT))
				lines.add(Text.translatable("willow.profession_tooltip_apprentice").withColor(0x93e2b4));
			else if (stack.isIn(WillowTags.Items.EXPERT_USABLE_EQUIPMENT))
				lines.add(Text.translatable("willow.profession_tooltip_expert").withColor(0x93e2b4));
			else if (stack.isIn(WillowTags.Items.MASTER_USABLE_EQUIPMENT))
				lines.add(Text.translatable("willow.profession_tooltip_master").withColor(0x93e2b4));
		});
	}

//    private Map<Profession, Pair<Integer, Boolean>> computeDifference(PlayerProfessionState newState)
//    {
//        PlayerProfessionState oldState = SyncedProfessionState.state;
//        PlayerEntity player = MinecraftClient.getInstance().player;
//        for (Profession profession : Profession.values())
//        {
//            int oldXP = oldState.getXP(profession);
//            ProfessionLevel oldLevel = ProfessionUtil.getProfessionLevel(player, profession);
//            // TODO
//        }
//    }

	private void initializeClientHooks()
	{
		ScreenOpeningHooks.openLogbookScreen = () -> {
			MinecraftClient.getInstance().setScreen(new LogbookScreen());
		};
	}
}
package phi.willow;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.impl.client.rendering.hud.HudElementRegistryImpl;
import net.fabricmc.fabric.impl.client.rendering.hud.HudLayer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import oshi.util.tuples.Pair;
import phi.willow.clienthooks.ScreenOpeningHooks;
import phi.willow.data.PlayerProfessionState;
import phi.willow.data.Profession;
import phi.willow.data.ProfessionLevel;
import phi.willow.data.client.SyncedProfessionState;
import phi.willow.registry.WillowTags;
import phi.willow.rendering.XPPopupRenderer;
import phi.willow.screens.LogbookScreen;
import phi.willow.util.ProfessionUtil;

import java.util.HashMap;
import java.util.Map;

public class WillowClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		initializeClientHooks();

		ClientPlayNetworking.registerGlobalReceiver(PlayerProfessionState.PlayerProfessionStatePayload.ID, (payload, context) -> {
            context.client().execute(() -> {
                PlayerProfessionState newState = payload.state();
                var difference = computeDifference(newState);
                SyncedProfessionState.state = newState;
                handleXPGain(difference);
            });
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

        HudElementRegistry.addLast(Identifier.of(Willow.MOD_ID, "xp_popup_layer"), XPPopupRenderer::render);
	}

    /**
     *
     * @return A mapping from each profession to the difference in xp with the old state, and whether it leveled up
     */
    private Map<Profession, Pair<Integer, Boolean>> computeDifference(PlayerProfessionState newState)
    {
        Map<Profession, Pair<Integer, Boolean>> differences = new HashMap<>();
        PlayerProfessionState oldState = SyncedProfessionState.state;
        PlayerEntity player = MinecraftClient.getInstance().player;
        for (Profession profession : Profession.values())
        {
            int oldXP = oldState.getXP(profession);
            ProfessionLevel oldLevel = ProfessionUtil.getProfessionLevel(player, profession);
            int newXP = newState.getXP(profession);
            ProfessionLevel newLevel = ProfessionUtil.getLevelForXPValue(newXP);
            differences.put(profession, new Pair<>(newXP - oldXP, newLevel != oldLevel));
        }
        return differences;
    }

    private void handleXPGain(Map<Profession, Pair<Integer, Boolean>> diff)
    {
    // TODO
        XPPopupRenderer.createPopup(Profession.FARMING, ProfessionLevel.MASTER);
    }

	private void initializeClientHooks()
	{
		ScreenOpeningHooks.openLogbookScreen = () -> {
			MinecraftClient.getInstance().setScreen(new LogbookScreen());
		};
	}
}
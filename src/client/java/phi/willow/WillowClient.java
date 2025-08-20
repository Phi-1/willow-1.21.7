package phi.willow;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import phi.willow.clienthooks.ScreenOpeningHooks;
import phi.willow.data.PlayerProfessionState;
import phi.willow.data.Profession;
import phi.willow.data.ProfessionLevel;
import phi.willow.data.client.SyncedProfessionState;
import phi.willow.registry.WillowTags;
import phi.willow.rendering.XPPopupRenderer;
import phi.willow.screens.LogbookScreen;
import phi.willow.util.ProfessionUtil;

import java.util.ArrayList;
import java.util.List;

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
            PlayerEntity player = MinecraftClient.getInstance().player;
            // TODO: color text red if level is too high
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
    private List<XPDiff> computeDifference(PlayerProfessionState newState)
    {
        List<XPDiff> differences = new ArrayList<>();
        PlayerProfessionState oldState = SyncedProfessionState.state;
        PlayerEntity player = MinecraftClient.getInstance().player;
        for (Profession profession : Profession.values())
        {
            int oldXP = oldState.getXP(profession);
            ProfessionLevel oldLevel = ProfessionUtil.getProfessionLevel(player, profession);
            int newXP = newState.getXP(profession);
            ProfessionLevel newLevel = ProfessionUtil.getLevelForXPValue(newXP);
            int xpDiff = newXP - oldXP;
            if (xpDiff > 0)
                differences.add(new XPDiff(profession, newLevel, xpDiff, oldLevel != newLevel));
        }
        return differences;
    }

    private void handleXPGain(List<XPDiff> differences)
    {
        boolean handleLevelup = false;
        for (XPDiff diff : differences)
        {
            // Stop showing popups at max level
            if (diff.level != ProfessionLevel.MASTER)
                XPPopupRenderer.createPopup(diff.profession, diff.level);
            if (diff.isLevelup)
                handleLevelup = true;
        }
        World world = MinecraftClient.getInstance().world;
        if (world != null && handleLevelup)
            world.playSoundClient(SoundEvents.GOAT_HORN_SOUNDS.get(2).value(), SoundCategory.UI, 1.0f, 1.0f);
    }

	private void initializeClientHooks()
	{
		ScreenOpeningHooks.openLogbookScreen = () -> MinecraftClient.getInstance().setScreen(new LogbookScreen());
	}

    private record XPDiff(Profession profession, ProfessionLevel level, int xpDiff, boolean isLevelup) {}
}
package phi.willow.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import phi.willow.data.*;
import phi.willow.registry.WillowTags;
import phi.willow.data.client.SyncedProfessionState;

public class ProfessionUtil {

    public static ProfessionLevel getProfessionLevel(PlayerEntity player, Profession profession)
    {
        int xp;
        if (player.getWorld().isClient) {
            xp = SyncedProfessionState.state.getXP(profession);
        }
        else {
            PlayerProfessionStateHolder stateHolder = WillowPersistentState.getServerState(player.getServer()).getPlayerProfessionState();
            xp = stateHolder.getPlayerState(player).getXP(profession);
        }
        // TODO: cutoffs
        return ProfessionLevel.NOVICE;
    }

    public static boolean canUseToolAtLevel(ProfessionLevel level, ItemStack tool)
    {
        return switch (level)
        {
            case NOVICE -> tool.isIn(WillowTags.Items.NOVICE_USABLE_TOOLS);
            case APPRENTICE -> tool.isIn(WillowTags.Items.APPRENTICE_USABLE_TOOLS);
            case EXPERT -> tool.isIn(WillowTags.Items.EXPERT_USABLE_TOOLS);
            // master can just return true but keeping it like this in case more levels are added
            case MASTER -> tool.isIn(WillowTags.Items.MASTER_USABLE_TOOLS);
        };
    }

}

package phi.willow.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
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
        ProfessionLevel level = ProfessionLevel.NOVICE;
        int requiredXP = 0;
        for (int i = 0; i < ProfessionLevel.values().length; i++)
        {
            level = ProfessionLevel.values()[i];
            requiredXP += level.xpToNext;
            if (xp < requiredXP)
                break;
        }
        return level;
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

    public static ProfessionLevel getRequiredLevelForTool(ItemStack tool)
    {
        if (tool.isIn(WillowTags.Items.NOVICE_USABLE_TOOLS)) return ProfessionLevel.NOVICE;
        if (tool.isIn(WillowTags.Items.APPRENTICE_USABLE_TOOLS)) return ProfessionLevel.APPRENTICE;
        if (tool.isIn(WillowTags.Items.EXPERT_USABLE_TOOLS)) return ProfessionLevel.EXPERT;
        if (tool.isIn(WillowTags.Items.MASTER_USABLE_TOOLS)) return ProfessionLevel.MASTER;
        throw new IllegalStateException("Tried to read profession level for item that has none. This may be a bug in your code, or maybe you just forgot to add this item to the proper tag file: " + tool);
    }

    public static void levelUp(Profession profession, ServerPlayerEntity player, boolean onlyAddRequiredXP)
    {
        // TODO
//        PlayerProfessionState state = WillowPersistentState.getServerState(player.getServer()).getPlayerProfessionState().getPlayerState(player);
//        ProfessionLevel level = getProfessionLevel(player, profession);
//        int xp = state.getXP(profession);
//        if (onlyAddRequiredXP)
//        {
//            int requiredXP = level.totalXpForNext - xp;
//            state.setXP(profession, xp + requiredXP);
//            // TODO: levelup event?
//        }
//        else
//        {
//            state.setXP(profession, xp + level.xpToNext);
//            // TODO: levelup event
//        }
//        // TODO: don't level up if already max level, also in display don't show required xp at master
    }

}

package phi.willow.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import phi.willow.data.*;
import phi.willow.registry.WillowNetworking;
import phi.willow.registry.WillowTags;
import phi.willow.data.client.SyncedProfessionState;

public class ProfessionUtil {

    public static PlayerProfessionState getPlayerState(PlayerEntity player)
    {
        if (player instanceof ServerPlayerEntity serverPlayer)
            return WillowPersistentState.getServerState(serverPlayer.getServer()).getPlayerProfessionState().getPlayerState(serverPlayer);
        return SyncedProfessionState.state;
    }

    public static void setPlayerState(PlayerEntity player, PlayerProfessionState state)
    {
        if (player instanceof ServerPlayerEntity serverPlayer)
        {
            // TODO: for some reason this function is necessary. I thought state would just be passed by reference, but this function fixes bugs so apparently not. Why though?
            WillowPersistentState.getServerState(serverPlayer.getServer()).getPlayerProfessionState().setPlayerState(serverPlayer, state);
            return;
        }
        SyncedProfessionState.state = state;
    }

    public static ProfessionLevel getLevelForXPValue(int xp)
    {
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

    public static ProfessionLevel getProfessionLevel(PlayerEntity player, Profession profession)
    {
        PlayerProfessionState state = getPlayerState(player);
        int xp = state.getXP(profession);
        return getLevelForXPValue(xp);
    }

    public static int getXPTowardsNextLevel(PlayerEntity player, Profession profession)
    {
        PlayerProfessionState state = getPlayerState(player);
        ProfessionLevel level = getProfessionLevel(player, profession);
        int xp = state.getXP(profession);
        for (ProfessionLevel check : ProfessionLevel.values())
        {
            if (check.ordinal() >= level.ordinal())
                break;
            xp -= check.xpToNext;
        }
        return xp;
    }

    public static boolean canUseToolAtLevel(ProfessionLevel level, ItemStack tool)
    {
        return switch (level)
        {
            case NOVICE -> tool.isIn(WillowTags.Items.NOVICE_USABLE_EQUIPMENT);
            case APPRENTICE -> tool.isIn(WillowTags.Items.APPRENTICE_USABLE_EQUIPMENT);
            case EXPERT -> tool.isIn(WillowTags.Items.EXPERT_USABLE_EQUIPMENT);
            // master can just return true but keeping it like this in case more levels are added
            case MASTER -> tool.isIn(WillowTags.Items.MASTER_USABLE_EQUIPMENT);
        };
    }

    public static ProfessionLevel getRequiredLevelForTool(ItemStack tool)
    {
        if (tool.isIn(WillowTags.Items.NOVICE_USABLE_EQUIPMENT)) return ProfessionLevel.NOVICE;
        if (tool.isIn(WillowTags.Items.APPRENTICE_USABLE_EQUIPMENT)) return ProfessionLevel.APPRENTICE;
        if (tool.isIn(WillowTags.Items.EXPERT_USABLE_EQUIPMENT)) return ProfessionLevel.EXPERT;
        if (tool.isIn(WillowTags.Items.MASTER_USABLE_EQUIPMENT)) return ProfessionLevel.MASTER;
        throw new IllegalStateException("Tried to read profession level for item that has none. This may be a bug in your code, or maybe you just forgot to add this item to the proper tag file: " + tool);
    }

    public static void levelUpWithPlayerLevels(Profession profession, ServerPlayerEntity player)
    {
        PlayerProfessionState state = getPlayerState(player);
        ProfessionLevel level = getProfessionLevel(player, profession);
        if (level.playerLevelsToNext == 0 || player.experienceLevel < level.playerLevelsToNext)
            return;
        player.setExperienceLevel(player.experienceLevel - level.playerLevelsToNext);
        state.setXP(profession, state.getXP(profession) + level.xpToNext);
        setPlayerState(player, state);
        WillowNetworking.syncPlayerProfessionState(player, state);
    }

    public static void levelTo(ProfessionLevel level, Profession profession, ServerPlayerEntity player)
    {
        PlayerProfessionState state = getPlayerState(player);
        int current = state.getXP(profession);
        int needed = level.totalXPForNext - level.xpToNext;
        int diff = needed - current;
        if (diff <= 0)
            return;
        state.setXP(profession, current + diff);
        setPlayerState(player, state);
        WillowNetworking.syncPlayerProfessionState(player, state);
    }

    public static void increaseXP(ServerPlayerEntity player, Profession profession, int amount)
    {
        PlayerProfessionState state = getPlayerState(player);
        state.setXP(profession, state.getXP(profession) + amount);
        setPlayerState(player, state);
        WillowNetworking.syncPlayerProfessionState(player, state);
    }

    public static void gainBaseXP(Profession profession, ServerPlayerEntity player, float modifier, boolean goldBonus)
    {
        ProfessionLevel levelBefore = getProfessionLevel(player, profession);
        int goldFactor = goldBonus ? 8 : 1;
        // TODO: eventually maybe give different amounts of xp based on source, but for now that's too messy
//        int xpGain = profession.instanceXP * goldFactor * (xpSource.ordinal() + 1);
        int xpGain = (int) (profession.instanceXP * goldFactor * modifier);
        PlayerProfessionState state = getPlayerState(player);
        state.setXP(profession, state.getXP(profession) + xpGain);
        setPlayerState(player, state);
        // TODO: replace with more specific packet, like was a golden tool used, how much xp increase and in which profession. Would also fix the login levelup sound bug
        // TODO: separate packet for levelup
        WillowNetworking.syncPlayerProfessionState(player, state);
//        if (levelBefore != getProfessionLevel(player, profession))
//            pass;
            // TODO: then its a level up
    }

}

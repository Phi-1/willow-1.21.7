package phi.willow.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.network.packet.Packet;
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

    public static void levelUp(Profession profession, ServerPlayerEntity player, boolean onlyAddRequiredXP)
    {
        PlayerProfessionState state = getPlayerState(player);
        ProfessionLevel level = getProfessionLevel(player, profession);
        int xp = state.getXP(profession);
        int totalXpForNext = 0;
        if (onlyAddRequiredXP)
        {
            for (int i = 0; i <= level.ordinal(); i++)
            {
                totalXpForNext += ProfessionLevel.values()[i].xpToNext;
            }
            int requiredXP = totalXpForNext - xp;
            state.setXP(profession, xp + requiredXP);
            // TODO: levelup event?
        }
        else
        {
            state.setXP(profession, xp + level.xpToNext);
            // TODO: levelup event
        }
        // TODO: don't level up if already max level, also in display don't show required xp at master -> check that requiredForNext = 0
    }

    public static void increaseXP(Profession profession, ServerPlayerEntity player, boolean goldBonus)
    {
        ProfessionLevel levelBefore = getProfessionLevel(player, profession);
        int goldFactor = goldBonus ? 8 : 1;
        // TODO: eventually maybe give different amounts of xp based on source, but for now that's too messy
//        int xpGain = profession.instanceXP * goldFactor * (xpSource.ordinal() + 1);
        int xpGain = profession.instanceXP * goldFactor;
        PlayerProfessionState state = getPlayerState(player);
        state.setXP(profession, state.getXP(profession) + xpGain);
        // TODO: replace with more specific packet, like was a golden tool used, how much xp increase and in which profession
        // TODO: separate packet for levelup
        WillowNetworking.syncPlayerProfessionState(player, state);
//        if (levelBefore != getProfessionLevel(player, profession))
//            pass;
            // TODO: then its a level up
    }

}

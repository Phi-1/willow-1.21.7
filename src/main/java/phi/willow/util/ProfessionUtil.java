package phi.willow.util;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import phi.willow.Willow;
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
        for (ProfessionLevel level : ProfessionLevel.values())
        {
            if (level.xpToNext == 0 || xp < level.totalXPForNext)
                return level;
        }
        return ProfessionLevel.NOVICE;
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

    public static int getPlayerLevelsToNextProfessionLevel(PlayerEntity player, Profession profession)
    {
        int xp = getXPTowardsNextLevel(player, profession);
        ProfessionLevel level = getProfessionLevel(player, profession);
        float fraction = 1 - (float) xp / level.xpToNext;
        return (int) (level.playerLevelsToNext * fraction);
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
        // TODO: there's lots of duplication in functions in this class. Add more options, or cleaner interface on state getter? Basically every function gets the level, even if we already did
        PlayerProfessionState state = getPlayerState(player);
        ProfessionLevel level = getProfessionLevel(player, profession);
        int requiredLevels = getPlayerLevelsToNextProfessionLevel(player, profession);
        if (level.playerLevelsToNext == 0 || player.experienceLevel < requiredLevels)
            return;
        player.setExperienceLevel(player.experienceLevel - requiredLevels);
        state.setXP(profession, state.getXP(profession) + (level.xpToNext - getXPTowardsNextLevel(player, profession)));
        setPlayerState(player, state);
        ProfessionLevel newLevel = ProfessionUtil.getProfessionLevel(player, profession);
        WillowNetworking.syncPlayerProfessionState(player, state);
        onPlayerLevelup(player, profession, newLevel);
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
        onPlayerLevelup(player, profession, level);
    }

    public static void increaseXP(ServerPlayerEntity player, Profession profession, int amount)
    {
        ProfessionLevel levelBefore = getProfessionLevel(player, profession);
        PlayerProfessionState state = getPlayerState(player);
        state.setXP(profession, state.getXP(profession) + amount);
        setPlayerState(player, state);
        ProfessionLevel newLevel = getProfessionLevel(player, profession);
        WillowNetworking.syncPlayerProfessionState(player, state);
        if (levelBefore != newLevel)
            onPlayerLevelup(player, profession, newLevel);
    }

    public static void gainBaseXP(Profession profession, ServerPlayerEntity player, float modifier, boolean goldBonus)
    {
        ProfessionLevel levelBefore = getProfessionLevel(player, profession);
        int goldFactor = goldBonus ? 8 : 1;
        int xpGain = (int) (profession.instanceXP * goldFactor * modifier);
        PlayerProfessionState state = getPlayerState(player);
        state.setXP(profession, state.getXP(profession) + xpGain);
        setPlayerState(player, state);
        // TODO: replace with more specific packet, like was a golden tool used, how much xp increase and in which profession. Would also fix the login levelup sound bug
        WillowNetworking.syncPlayerProfessionState(player, state);
        ProfessionLevel newLevel = getProfessionLevel(player, profession);
        if (levelBefore != newLevel)
            onPlayerLevelup(player, profession, newLevel);
    }

    public static void onPlayerLevelup(ServerPlayerEntity player, Profession profession, ProfessionLevel level)
    {
        for (ServerPlayerEntity p : player.getServer().getPlayerManager().getPlayerList())
            p.sendMessage(Text.literal(player.getNameForScoreboard() + " has reached " + level.label + " in " + profession.label + "!"));
        ServerPlayNetworking.send(player, new LevelupS2CPacket(profession, level));
    }

    public record LevelupS2CPacket(Profession profession, ProfessionLevel level) implements CustomPayload
    {
        public static final Identifier PACKET_ID = Identifier.of(Willow.MOD_ID, "levelup_s2c_packet");
        public static final Id<LevelupS2CPacket> ID = new CustomPayload.Id<>(PACKET_ID);
        public static final PacketCodec<RegistryByteBuf, LevelupS2CPacket> PACKET_CODEC = PacketCodec.of(LevelupS2CPacket::encode, LevelupS2CPacket::decode);

        public static LevelupS2CPacket decode(RegistryByteBuf buf)
        {
            Profession profession = Profession.values()[buf.readInt()];
            ProfessionLevel level = ProfessionLevel.values()[buf.readInt()];
            return new LevelupS2CPacket(profession, level);
        }

        public void encode(RegistryByteBuf buf)
        {
            buf.writeInt(profession.ordinal());
            buf.writeInt(level.ordinal());
        }

        @Override
        public Id<? extends CustomPayload> getId() {
            return ID;
        }
    }

}

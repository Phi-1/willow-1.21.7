package phi.willow.data;

import com.mojang.serialization.Codec;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Uuids;

import java.util.*;

public class PlayerProfessionStateHolder {

    private static final Codec<Map<UUID, PlayerProfessionState>> MAPCODEC = Codec.unboundedMap(Uuids.CODEC, PlayerProfessionState.CODEC);
    public static final Codec<PlayerProfessionStateHolder> CODEC = MAPCODEC.xmap(
            PlayerProfessionStateHolder::new,
            PlayerProfessionStateHolder::getData
    );
    private final HashMap<UUID, PlayerProfessionState> playerStates = new HashMap<>();

    public PlayerProfessionStateHolder() {}

    public PlayerProfessionStateHolder(Map<UUID, PlayerProfessionState> map) {
        for (UUID uuid : map.keySet())
        {
            this.playerStates.put(uuid, map.get(uuid));
        }
    }

    public void setPlayerState(PlayerEntity player, PlayerProfessionState state)
    {
        playerStates.put(player.getUuid(), state);
    }

    public PlayerProfessionState getPlayerState(PlayerEntity player)
    {
        return playerStates.getOrDefault(player.getUuid(), new PlayerProfessionState(new ArrayList<>()));
    }

    public Map<UUID, PlayerProfessionState> getData() { return this.playerStates; }
}

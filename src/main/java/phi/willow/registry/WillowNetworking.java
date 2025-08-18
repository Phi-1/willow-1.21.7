package phi.willow.registry;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import phi.willow.Willow;
import phi.willow.data.PlayerProfessionState;

public class WillowNetworking {
    public static final Identifier PLAYER_PROFESSION_STATE_SYNC_PACKET_ID = Identifier.of(Willow.MOD_ID, "player_profession_state_payload");

    public static void initialize()
    {
        PayloadTypeRegistry.playS2C().register(PlayerProfessionState.PlayerProfessionStatePayload.ID, PlayerProfessionState.PlayerProfessionStatePayload.PACKET_CODEC);
    }

    public static void syncPlayerProfessionState(ServerPlayerEntity player, PlayerProfessionState state)
    {
        ServerPlayNetworking.send(player, new PlayerProfessionState.PlayerProfessionStatePayload(state));
    }
}

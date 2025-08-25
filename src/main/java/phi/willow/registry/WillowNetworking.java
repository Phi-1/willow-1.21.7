package phi.willow.registry;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import phi.willow.Willow;
import phi.willow.data.PlayerProfessionState;
import phi.willow.data.Profession;
import phi.willow.util.ProfessionUtil;

public class WillowNetworking {
    public static final Identifier TRY_LEVEL_PROFESSION_PACKET_ID = Identifier.of(Willow.MOD_ID, "try_level_profession_packet");
    public static final Identifier PLAYER_PROFESSION_STATE_SYNC_PACKET_ID = Identifier.of(Willow.MOD_ID, "player_profession_state_payload");

    public static void initialize()
    {
        PayloadTypeRegistry.playC2S().register(TryLevelProfessionC2SPacket.ID, TryLevelProfessionC2SPacket.PACKET_CODEC);
        PayloadTypeRegistry.playS2C().register(PlayerProfessionState.PlayerProfessionStatePayload.ID, PlayerProfessionState.PlayerProfessionStatePayload.PACKET_CODEC);
        PayloadTypeRegistry.playS2C().register(ProfessionUtil.LevelupS2CPacket.ID, ProfessionUtil.LevelupS2CPacket.PACKET_CODEC);

        ServerPlayNetworking.registerGlobalReceiver(TryLevelProfessionC2SPacket.ID, (packet, context) -> {
            context.server().execute(() -> ProfessionUtil.levelUpWithPlayerLevels(packet.profession, context.player()));
        });
    }

    public static void syncPlayerProfessionState(ServerPlayerEntity player, PlayerProfessionState state)
    {
        ServerPlayNetworking.send(player, new PlayerProfessionState.PlayerProfessionStatePayload(state));
    }

    public record TryLevelProfessionC2SPacket(Profession profession) implements CustomPayload {

        public static final CustomPayload.Id<TryLevelProfessionC2SPacket> ID = new CustomPayload.Id<>(WillowNetworking.TRY_LEVEL_PROFESSION_PACKET_ID);
        public static final PacketCodec<RegistryByteBuf, TryLevelProfessionC2SPacket> PACKET_CODEC = PacketCodec.of(TryLevelProfessionC2SPacket::encode, TryLevelProfessionC2SPacket::decode);

        private void encode(RegistryByteBuf buffer)
        {
            buffer.writeInt(this.profession.ordinal());
        }

        private static TryLevelProfessionC2SPacket decode(RegistryByteBuf buffer)
        {
            return new TryLevelProfessionC2SPacket(Profession.values()[buffer.readInt()]);
        }

        @Override
        public Id<? extends CustomPayload> getId() {
            return ID;
        }
    }
}

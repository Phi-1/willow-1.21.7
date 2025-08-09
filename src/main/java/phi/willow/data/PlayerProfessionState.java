package phi.willow.data;

import com.mojang.serialization.Codec;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import phi.willow.registry.WillowNetworking;

import java.util.ArrayList;
import java.util.List;

public class PlayerProfessionState {
    public static final Codec<PlayerProfessionState> CODEC = Codec.INT.listOf().xmap(
            PlayerProfessionState::new,
            PlayerProfessionState::getData
    );

    private final ArrayList<Integer> xpPerProfession = new ArrayList<>();

    public PlayerProfessionState(List<Integer> xpValues)
    {
        for (int i = 0; i < Profession.values().length; i++) {
            if (xpValues.size() < i + 1)
                this.xpPerProfession.add(0);
            else
                this.xpPerProfession.add(xpValues.get(i));
        }
    }
    public void setXP(Profession profession, int amount) {
        xpPerProfession.set(profession.ordinal(), amount);
    }

    public int getXP(Profession profession) {
        return xpPerProfession.get(profession.ordinal());
    }

    public List<Integer> getData() { return this.xpPerProfession.subList(0, Profession.values().length); }

    public record PlayerProfessionStatePayload(PlayerProfessionState state) implements CustomPayload
    {
        public static final CustomPayload.Id<PlayerProfessionStatePayload> ID = new CustomPayload.Id<>(WillowNetworking.PLAYER_PROFESSION_STATE_PACKET_ID);
        public static final PacketCodec<RegistryByteBuf, PlayerProfessionStatePayload> PACKET_CODEC = PacketCodec.tuple(PacketCodecs.codec(PlayerProfessionState.CODEC), PlayerProfessionStatePayload::state, PlayerProfessionStatePayload::new);

        @Override
        public Id<? extends CustomPayload> getId() {
            return ID;
        }
    }
}
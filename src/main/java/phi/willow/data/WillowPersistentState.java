package phi.willow.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateType;
import net.minecraft.world.World;

public class WillowPersistentState extends PersistentState {

    public static final Codec<WillowPersistentState> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            PlayerProfessionStateHolder.CODEC.fieldOf("playerProfessionState").forGetter(WillowPersistentState::getPlayerProfessionState),
            TestDataHolder.CODEC.fieldOf("testData").forGetter(WillowPersistentState::getTestData)
    ).apply(instance, WillowPersistentState::new));

    private TestDataHolder testData = new TestDataHolder(0);
    private PlayerProfessionStateHolder playerProfessionState = new PlayerProfessionStateHolder();

    public WillowPersistentState() {}

    public WillowPersistentState(PlayerProfessionStateHolder playerProfessionState) {
        this.playerProfessionState = playerProfessionState;
    }

    public WillowPersistentState(PlayerProfessionStateHolder playerProfessionState, TestDataHolder testData) {
        this.playerProfessionState = playerProfessionState;
        this.testData = testData;
    }

    public PlayerProfessionStateHolder getPlayerProfessionState() { return this.playerProfessionState; }
    public TestDataHolder getTestData() { return this.testData; }

    private static final PersistentStateType<WillowPersistentState> type = new PersistentStateType<>(
            "willow_persistent_state",
            WillowPersistentState::new, // If there's no 'WillowPersistentState' yet create one and refresh variables
            CODEC,
            null // Supposed to be an 'DataFixTypes' enum, but we can just pass null
    );

    public static WillowPersistentState getServerState(MinecraftServer server) {
        // (Note: arbitrary choice to use 'World.OVERWORLD' instead of 'World.END' or 'World.NETHER'.  Any work)
        ServerWorld serverWorld = server.getWorld(World.OVERWORLD);
        assert serverWorld != null;

        // The first time the following 'getOrCreate' function is called, it creates a brand new 'WillowPersistentState' and
        // stores it inside the 'PersistentStateManager'. The subsequent calls to 'getOrCreate' pass in the saved
        // 'WillowPersistentState' NBT on disk to our function 'WillowPersistentState::createFromNbt'.
        WillowPersistentState state = serverWorld.getPersistentStateManager().getOrCreate(type);
        state.markDirty();

        return state;
    }
}

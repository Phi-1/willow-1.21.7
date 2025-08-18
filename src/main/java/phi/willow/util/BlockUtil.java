package phi.willow.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BlockUtil {

    private static final Map<UUID, Direction> destroyedBlockFaces = new HashMap<>();

    public static BlockPos[] getAdjacent(BlockPos blockPos) {
        return new BlockPos[] {blockPos.north(), blockPos.east(), blockPos.south(), blockPos.west(), blockPos.up(), blockPos.down()};
    }

    public static void registerDestroyedBlockFace(PlayerEntity player, Direction face)
    {
        destroyedBlockFaces.put(player.getUuid(), face);
    }

    @Nullable
    public static Direction getLastDestroyedBlockFace(PlayerEntity player)
    {
        return destroyedBlockFaces.get(player.getUuid());
    }
}

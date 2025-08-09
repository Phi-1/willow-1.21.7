package phi.willow.util;

import net.minecraft.util.math.BlockPos;

public class BlockPosUtil {
    public static BlockPos[] getAdjacent(BlockPos blockPos) {
        return new BlockPos[] {blockPos.north(), blockPos.east(), blockPos.south(), blockPos.west(), blockPos.up(), blockPos.down()};
    }
}

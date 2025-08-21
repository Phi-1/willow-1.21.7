package phi.willow.items;

import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Rarity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import phi.willow.registry.WillowToolMaterials;
import phi.willow.util.BlockUtil;
import phi.willow.util.TickTimers;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SledgeHammerItem extends Item {

    public enum Type
    {
        SLEDGEHAMMER,
        HAMMER_OF_THE_DEEP
    }

    public final Type type;

    public static final List<UUID> preventAOEMineTriggersOn = new ArrayList<>();

    public SledgeHammerItem(Settings settings, Type type) {
        super(createSettings(settings, type));
        this.type = type;
    }

    private static Settings createSettings(Settings settings, Type type)
    {
        switch (type)
        {
            case SLEDGEHAMMER -> settings.pickaxe(WillowToolMaterials.SLEDGEHAMMER, 9.0f, -3.4f);
            case HAMMER_OF_THE_DEEP -> settings
                    .pickaxe(WillowToolMaterials.HAMMER_OF_THE_DEEP, 14.0f, -3.8f)
                    .rarity(Rarity.EPIC)
                    .fireproof();
        }
        return settings;
    }

    @Override
    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        super.postMine(stack, world, state, pos, miner);
        if (!(miner instanceof ServerPlayerEntity player))
            return true;
        if (preventAOEMineTriggersOn.contains(player.getUuid()))
            return true;
        if (!isCorrectForDrops(stack, state))
            return true;
        mineAround(stack, world, pos, player);
        return true;
    }

    public static void mineAround(ItemStack stack, World world, BlockPos pos, ServerPlayerEntity player)
    {
        Direction hitSide = BlockUtil.getLastDestroyedBlockFace(player);
        if (hitSide == null)
            return;
        Direction[] directions = getRotationAndExpansionDirections(hitSide);

        preventAOEMineTriggersOn.add(player.getUuid());
        TickTimers.schedule(() -> { preventAOEMineTriggersOn.remove(player.getUuid()); }, 1);

        for (BlockPos around : BlockPos.iterateInSquare(pos, 1, directions[0], directions[1]))
        {
            if (around == pos)
                continue;
            if (stack.getItem().isCorrectForDrops(stack, world.getBlockState(around)))
            {
                player.interactionManager.tryBreakBlock(around);
            }
        }
    }

    private static Direction[] getRotationAndExpansionDirections(Direction blockFace) {
        Direction rotation = switch (blockFace) {
            case UP, DOWN -> Direction.EAST;
            case NORTH, EAST, SOUTH, WEST -> blockFace.rotateYClockwise();
        };
        Direction expansion = switch (blockFace) {
            case UP, DOWN -> Direction.NORTH;
            case NORTH, EAST, SOUTH, WEST -> Direction.UP;
        };
        return new Direction[] {rotation, expansion};
    }
    // TODO: knockback in an aoe on attack?
}

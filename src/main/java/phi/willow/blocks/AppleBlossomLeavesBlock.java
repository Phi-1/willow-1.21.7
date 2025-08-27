package phi.willow.blocks;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.TintedParticleLeavesBlock;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.context.LootWorldContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import phi.willow.util.BlockUtil;
import sereneseasons.api.season.Season;
import sereneseasons.api.season.SeasonHelper;

import java.util.List;

public class AppleBlossomLeavesBlock extends TintedParticleLeavesBlock {

    public static final BooleanProperty HAS_APPLES = BooleanProperty.of("has_apples");

    public AppleBlossomLeavesBlock(AbstractBlock.Settings settings)
    {
        super(0.01F, settings);
        setDefaultState(getDefaultState().with(HAS_APPLES, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(HAS_APPLES);
        super.appendProperties(builder);
    }

    private ItemStack getDroppedApples(World world)
    {
        final int maxApples = 1;
        return new ItemStack(Items.APPLE, world.random.nextInt(maxApples) + 1);
    }

    private void dropApples(World world, BlockState state, BlockPos pos, Vec3d towards, boolean naturalDrop) {
        ItemStack appleStack = getDroppedApples(world);
        double xSpeed = (towards.getX() - pos.getX()) / 8;
        double ySpeed = naturalDrop ? -0.8 : 0.4;
        double zSpeed = (towards.getZ() - pos.getZ()) / 8;
        ItemEntity appleEntity = new ItemEntity(world, pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f, appleStack, xSpeed, ySpeed, zSpeed);
        world.spawnEntity(appleEntity);
        world.setBlockState(pos, state.with(HAS_APPLES, false));
        world.playSound(null, pos, SoundEvents.BLOCK_AZALEA_LEAVES_BREAK, SoundCategory.BLOCKS);
    }

    private boolean isInSeason(World world, BlockPos pos) {
        Season season = SeasonHelper.getSeasonState(world).getSeason();
        return season == Season.SUMMER || season == Season.AUTUMN || SeasonHelper.usesTropicalSeasons(world.getBiome(pos));
    }

    private void tryGrowApples(World world, BlockPos pos, BlockState state) {
        // Only grow apples if an adjacent position is empty
        for (BlockPos checkPos : BlockUtil.getAdjacent(pos))
        {
            if (world.getBlockState(checkPos).isReplaceable())
            {
                world.setBlockState(pos, state.with(HAS_APPLES, true));
                break;
            }
        }
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (!player.getAbilities().allowModifyWorld || !state.get(HAS_APPLES)) {
            return super.onUse(state, world, pos, player, hit);
        }
        if (!world.isClient)
        {
            dropApples(world, state, pos, new Vec3d(player.getX(), player.getY(), player.getZ()), false);
        }
        return ActionResult.SUCCESS;
    }

    @Override
    protected boolean hasRandomTicks(BlockState state) {
        return true;
    }

    @Override
    protected void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        boolean seasons = FabricLoader.getInstance().isModLoaded("sereneseasons");
        // Grow apples
        if (!state.get(HAS_APPLES) && random.nextInt(300) == 0) {
            if (seasons) {
                if (isInSeason(world, pos))
                    tryGrowApples(world, pos, state);
            }
            else {
                tryGrowApples(world, pos, state);
            }
        }
        if (seasons)
        {
            // Remove apples off-season
            if (!isInSeason(world, pos) && state.get(HAS_APPLES)) {
                world.setBlockState(pos, state.with(HAS_APPLES, false));
            }
            // Sometimes drop apples in late autumn
            Season.SubSeason subSeason = SeasonHelper.getSeasonState(world).getSubSeason();
            if (subSeason == Season.SubSeason.LATE_AUTUMN && state.get(HAS_APPLES)) {
                if (random.nextInt(120) == 0) {
                    dropApples(world, state, pos, new Vec3d(pos.getX(), pos.down().getY(), pos.getZ()), true);
                }
            }
        }
        super.randomTick(state, world, pos, random);
    }

    @Override
    protected List<ItemStack> getDroppedStacks(BlockState state, LootWorldContext.Builder builder) {
        List<ItemStack> drops = super.getDroppedStacks(state, builder);
        if (state.get(HAS_APPLES))
        {
            drops.add(getDroppedApples(builder.getWorld()));
        }
        return drops;
    }
}

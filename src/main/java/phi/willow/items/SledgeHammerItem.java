package phi.willow.items;

import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SledgeHammerItem extends Item {
    public SledgeHammerItem(Settings settings) {
        super(settings);
    }

    @Override
    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        return super.postMine(stack, world, state, pos, miner);
        // TODO: break blocks around, damage durability unless block hardness is 0 (see super)
    }

    // TODO: knockback in an aoe on attack?
}

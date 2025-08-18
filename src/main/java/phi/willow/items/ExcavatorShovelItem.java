package phi.willow.items;

import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShovelItem;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Rarity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import phi.willow.registry.WillowToolMaterials;

public class ExcavatorShovelItem extends ShovelItem {

    public ExcavatorShovelItem(Settings settings) {
        super(WillowToolMaterials.EXCAVATOR, 8.0f, -2.8f, settings.rarity(Rarity.RARE));
    }

    @Override
    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        super.postMine(stack, world, state, pos, miner);
        if (!(miner instanceof ServerPlayerEntity player))
            return true;
        if (SledgeHammerItem.preventAOEMineTriggersOn.contains(player.getUuid()))
            return true;
        if (!isCorrectForDrops(stack, state))
            return true;
        SledgeHammerItem.mineAround(stack, world, pos, player);
        return true;
    }
}

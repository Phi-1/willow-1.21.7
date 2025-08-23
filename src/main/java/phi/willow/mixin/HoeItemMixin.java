package phi.willow.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.HoeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import phi.willow.data.Profession;
import phi.willow.data.ProfessionLevel;
import phi.willow.util.ProfessionUtil;

import java.util.List;

@Mixin(HoeItem.class)
public class HoeItemMixin {
    // TODO: make hoes break in aoe when leftclicked -> probs in playermanagermixin, or event i guess

    @Inject(method = "useOnBlock", at = @At("HEAD"), cancellable = true)
    public void harvestAOE(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir)
    {
        PlayerEntity player = context.getPlayer();
        ItemStack stack = context.getStack();
        if (player == null || !stack.isIn(ItemTags.HOES))
            return;
        ProfessionLevel toolLevel = ProfessionUtil.getRequiredLevelForTool(stack);
        ProfessionLevel level = ProfessionUtil.getProfessionLevel(player, Profession.FARMING);
        if (!ProfessionUtil.canUseToolAtLevel(level, stack))
            return;
        BlockPos pos = context.getBlockPos();
        // TODO: check if clicked block is a crop, else till soil in aoe -> only till one if shift clicking?
        if (!(player instanceof ServerPlayerEntity serverPlayer))
        {
            return;
        }
        Iterable<? extends BlockPos> harvestPositions = switch (toolLevel)
        {
            case MASTER -> BlockPos.iterateInSquare(pos, 2, Direction.NORTH, Direction.EAST);
            case EXPERT -> BlockPos.iterateInSquare(pos, 1, Direction.NORTH, Direction.EAST);
            case APPRENTICE -> List.of(pos, pos.north(), pos.east(), pos.south(), pos.west());
            case NOVICE -> List.of(pos);
        };
        boolean clickedCrop = false;
        for (BlockPos harvestPos : harvestPositions)
        {
            BlockState state = player.getWorld().getBlockState(harvestPos);
            Block block = state.getBlock();
            if (!(block instanceof CropBlock crop))
            {
                // If clicked block isn't a crop, don't continue checking the others
                // TODO: prolly move this check outside the loop somehow
                if (harvestPos == pos)
                    break;
                continue;
            }
            clickedCrop = true;
            IntProperty age = ((CropBlockInvoker) crop).invokeGetAgeProperty();
            int maxAge = crop.getMaxAge();
            if (state.get(age) != maxAge)
                continue;
            player.getWorld().breakBlock(harvestPos, true);
            player.getWorld().setBlockState(harvestPos, state.with(age, 0));
            // TODO: find good damage value
            final int damagePerCrop = 5;
            // NOTE: item#damage already takes into account unbreaking
            stack.damage(damagePerCrop, player, LivingEntity.getSlotForHand(context.getHand()));
            ProfessionUtil.gainBaseXP(Profession.FARMING, serverPlayer, 1, false);
        }
        if (clickedCrop)
            cir.setReturnValue(ActionResult.SUCCESS);
    }
}

package phi.willow.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import phi.willow.data.Profession;
import phi.willow.data.ProfessionLevel;
import phi.willow.util.BlockPosUtil;
import phi.willow.util.ProfessionUtil;

import java.util.ArrayList;
import java.util.List;

@Mixin(Item.class)
public class ItemMixin {

    @Inject(method = "useOnBlock", at = @At("HEAD"))
    public void harvestAOE(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir)
    {
        PlayerEntity player = context.getPlayer();
        ItemStack stack = context.getStack();
        if (player == null || player.getWorld().isClient || !stack.isIn(ItemTags.HOES))
            return;
        ProfessionLevel toolLevel = ProfessionUtil.getRequiredLevelForTool(stack);
        ProfessionLevel level = ProfessionUtil.getProfessionLevel(player, Profession.FARMING);
        if (!ProfessionUtil.canUseToolAtLevel(level, stack))
            return;
        BlockPos pos = context.getBlockPos();
        // TODO
        // center
        // center + adjacent(not actually because that one checks above and below
        // center + squareAround 1
        // center + squarearound 2
    }
}

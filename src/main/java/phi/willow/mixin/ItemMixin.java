package phi.willow.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import phi.willow.Willow;
import phi.willow.data.Profession;
import phi.willow.data.ProfessionLevel;
import phi.willow.items.TheHeraldItem;
import phi.willow.registry.WillowItems;
import phi.willow.registry.WillowTags;
import phi.willow.util.BlockPosUtil;
import phi.willow.util.ProfessionUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    @Inject(method = "onItemEntityDestroyed", at = @At("HEAD"))
    public void createHerald(ItemEntity entity, CallbackInfo ci)
    {
        TheHeraldItem.tryCreateHerald(entity);
    }
}

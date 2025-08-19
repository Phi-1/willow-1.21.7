package phi.willow.mixin;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BlocksAttacksComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import phi.willow.data.Profession;
import phi.willow.util.ProfessionUtil;

@Mixin(Item.class)
public abstract class ItemMixin {

    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    public void shieldRequiresProfessionLevel(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<ActionResult> cir)
    {
        ItemStack stack = user.getStackInHand(hand);
        BlocksAttacksComponent blocksAttacksComponent = stack.get(DataComponentTypes.BLOCKS_ATTACKS);
        if (blocksAttacksComponent == null)
            return;
        // TODO: test once xp is implemented, fail might need to be pass?
        if (!ProfessionUtil.canUseToolAtLevel(ProfessionUtil.getProfessionLevel(user, Profession.FIGHTING), stack))
            cir.setReturnValue(ActionResult.FAIL);
    }

}

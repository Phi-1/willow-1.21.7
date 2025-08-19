package phi.willow.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.TridentItem;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import phi.willow.data.Profession;
import phi.willow.data.ProfessionLevel;
import phi.willow.util.ProfessionUtil;

@Mixin(TridentItem.class)
public abstract class TridentItemMixin {

    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    public void failIfNotSkilled(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<ActionResult> cir)
    {
        ItemStack stack = user.getStackInHand(hand);
        if (!stack.isOf(Items.TRIDENT))
            return;
        ProfessionLevel level = ProfessionUtil.getProfessionLevel(user, Profession.FIGHTING);
        if (!ProfessionUtil.canUseToolAtLevel(level, stack))
            cir.setReturnValue(ActionResult.FAIL);
    }

}

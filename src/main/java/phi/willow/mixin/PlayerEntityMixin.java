package phi.willow.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalFloatRef;
import net.minecraft.block.BlockState;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.ItemTags;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import phi.willow.data.Profession;
import phi.willow.data.ProfessionLevel;
import phi.willow.registry.WillowEffectsAndPotions;
import phi.willow.registry.WillowItems;
import phi.willow.statuseffects.LightningResistanceStatusEffect;
import phi.willow.util.ProfessionUtil;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {

    @Unique
    private static final float REDUCED_MINING_SPEED = 0.1f;
    @Unique
    private static final float ATTACK_SPEED_FACTOR = 3.0f;

    @Shadow @Final
    PlayerInventory inventory;

    @Inject(method = "getAttackCooldownProgressPerTick", at = @At("RETURN"), cancellable = true)
    public void getAttackCooldownProgressPerTick(CallbackInfoReturnable<Float> cir)
    {
        ItemStack heldItem = this.inventory.getSelectedStack();
        if (!(heldItem.isIn(ItemTags.SWORDS) || heldItem.isIn(ItemTags.AXES)))
            return;
        ProfessionLevel level = ProfessionUtil.getProfessionLevel(self(), Profession.FIGHTING);
        if (!ProfessionUtil.canUseToolAtLevel(level, heldItem))
            cir.setReturnValue(cir.getReturnValueF() * ATTACK_SPEED_FACTOR);
    }

    @Inject(method = "getBlockBreakingSpeed", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getMiningSpeedMultiplier(Lnet/minecraft/block/BlockState;)F", shift = At.Shift.BY, by = 2))
    public void getBlockBreakingSpeed(BlockState block, CallbackInfoReturnable<Float> cir, @Local(ordinal = 0) LocalFloatRef f)
    {
        ItemStack heldItem = this.inventory.getSelectedStack();
        if (heldItem.isOf(Items.AIR))
            return;
        ProfessionLevel level = ProfessionLevel.NOVICE;
        boolean isTool = false;
        if (heldItem.isIn(ItemTags.PICKAXES) || heldItem.isIn(ItemTags.SHOVELS)) {
            isTool = true;
            level = ProfessionUtil.getProfessionLevel(self(), Profession.MINING);
        }
        else if (heldItem.isIn(ItemTags.AXES)) {
            isTool = true;
            level = ProfessionUtil.getProfessionLevel(self(), Profession.WOODCUTTING);
        }
        else if (heldItem.isIn(ItemTags.HOES)) {
            isTool = true;
            level = ProfessionUtil.getProfessionLevel(self(), Profession.FARMING);
        }
        if (isTool && !ProfessionUtil.canUseToolAtLevel(level, heldItem))
            f.set(REDUCED_MINING_SPEED);
    }

    @ModifyVariable(method = "damage", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private float checkForLightningResistance(float amount)
    {
        StatusEffectInstance instance = self().getStatusEffect(WillowEffectsAndPotions.LIGHTNING_RESISTANCE);
        if (instance != null)
        {
            return LightningResistanceStatusEffect.getReducedDamage(amount, instance.getAmplifier());
        }
        return amount;
    }

    @Unique
    private PlayerEntity self() {
        return (PlayerEntity)(Object) this;
    }
}

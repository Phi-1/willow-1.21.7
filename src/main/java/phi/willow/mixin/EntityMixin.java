package phi.willow.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin
{
    // TODO: check back later if arrows still deflect, this should at least make it look like they don't even if they don't deal damage either
    @Inject(method = "sidedDamage", at = @At("RETURN"), cancellable = true)
    public void stopDeflectingArrows(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir)
    {
        Entity self = self();
        if (self instanceof PlayerEntity)
            return;
        if (source.isOf(DamageTypes.ARROW))
            cir.setReturnValue(true);
    }

    private Entity self()
    {
        return (Entity) (Object) this;
    }
}

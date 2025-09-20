package phi.willow.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.mob.ShulkerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin
{
    // TODO: check back later
    @Inject(method = "isInvulnerableTo", at = @At("RETURN"), cancellable = true)
    public void tryFixWeirdInvulnerability(ServerWorld world, DamageSource source, CallbackInfoReturnable<Boolean> cir)
    {
        Entity self = self();
        if (self instanceof PlayerEntity || self instanceof ShulkerEntity)
            return;
        if (source.isOf(DamageTypes.ARROW))
            cir.setReturnValue(false);
    }

    @Unique
    private Entity self()
    {
        return (Entity) (Object) this;
    }
}

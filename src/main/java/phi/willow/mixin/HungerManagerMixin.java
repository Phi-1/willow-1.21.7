package phi.willow.mixin;

import net.minecraft.entity.player.HungerManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(HungerManager.class)
public abstract class HungerManagerMixin
{

    @ModifyVariable(method = "addExhaustion", at = @At("HEAD"), argsOnly = true)
    public float reduceExhaustionGain(float value)
    {
        return value * 0.5f;
    }
}

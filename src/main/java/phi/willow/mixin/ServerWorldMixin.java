package phi.willow.mixin;

import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.concurrent.ThreadLocalRandom;


@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin {

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;resetWeather()V"))
    public void keepTheRain(ServerWorld instance)
    {
        if (ThreadLocalRandom.current().nextInt(3) == 0)
            return;
        instance.resetWeather();
    }

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;setTimeOfDay(J)V"))
    public void wakeUpEarlier(ServerWorld instance, long timeOfDay)
    {
        instance.setTimeOfDay(timeOfDay - 1500);
    }

}

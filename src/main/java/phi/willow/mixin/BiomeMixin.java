package phi.willow.mixin;

import net.minecraft.util.math.ColorHelper;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Biome.class)
public abstract class BiomeMixin {

    @Inject(method = "getWaterColor", at = @At("RETURN"), cancellable = true)
    public void makeWaterLessBlue(CallbackInfoReturnable<Integer> cir)
    {
        int base = cir.getReturnValue();
        float alpha = ColorHelper.getAlphaFloat(base);
        float red = ColorHelper.getRedFloat(base);
        float green = ColorHelper.getGreenFloat(base);
        float blue = ColorHelper.getBlueFloat(base);
        if (blue > 0.8)
            cir.setReturnValue(ColorHelper.fromFloats(alpha, red, green, blue * 0.66f));
    }

    @Inject(method = "getWaterFogColor", at = @At("RETURN"), cancellable = true)
    public void makeWaterFogLessBlue(CallbackInfoReturnable<Integer> cir)
    {
        int base = cir.getReturnValue();
        float alpha = ColorHelper.getAlphaFloat(base);
        float red = ColorHelper.getRedFloat(base);
        float green = ColorHelper.getGreenFloat(base);
        float blue = ColorHelper.getBlueFloat(base);
        if (blue > 0.8)
            cir.setReturnValue(ColorHelper.fromFloats(alpha, red, green, blue * 0.66f));
    }

}

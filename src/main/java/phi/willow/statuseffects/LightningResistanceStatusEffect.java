package phi.willow.statuseffects;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.particle.ParticleTypes;

public class LightningResistanceStatusEffect extends StatusEffect {
    public LightningResistanceStatusEffect() {
        super(StatusEffectCategory.BENEFICIAL, 0xc9dfe5, ParticleTypes.ELECTRIC_SPARK);
    }

    public static float getReducedDamage(float baseDamage, int amplifier)
    {
        return baseDamage * (amplifier > 0 ? 0.2f : 0.4f);
    }

    // TODO: effect, lang, icon
}

package phi.willow.statuseffects;

import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Identifier;
import phi.willow.Willow;

public class ReachStatusEffect extends StatusEffect {

    public static final Identifier REACH_ATTRIBUTE_ID = Identifier.of(Willow.MOD_ID, "reach_attribute");
    private static final double EFFECT_STRENGTH = 0.2;

    public ReachStatusEffect() {
        // TODO: choose particle effect
        super(StatusEffectCategory.BENEFICIAL, 12404736, ParticleTypes.CURRENT_DOWN);
        this.addAttributeModifier(EntityAttributes.BLOCK_INTERACTION_RANGE, REACH_ATTRIBUTE_ID, EFFECT_STRENGTH, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE);
    }
}

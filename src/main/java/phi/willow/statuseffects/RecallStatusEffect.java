package phi.willow.statuseffects;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.server.world.ServerWorld;
import org.jetbrains.annotations.Nullable;

public class RecallStatusEffect extends StatusEffect {
    public RecallStatusEffect() {
        super(StatusEffectCategory.BENEFICIAL, 0x6acce8);
    }

    @Override
    public void applyInstantEffect(ServerWorld world, @Nullable Entity effectEntity, @Nullable Entity attacker, LivingEntity target, int amplifier, double proximity) {
        super.applyInstantEffect(world, effectEntity, attacker, target, amplifier, proximity);
        // TODO: figure out how instant effects work
    }
}

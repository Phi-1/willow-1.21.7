package phi.willow.registry;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import phi.willow.Willow;
import phi.willow.statuseffects.LightningResistanceStatusEffect;

public class WillowEffects {

    public static final RegistryEntry<StatusEffect> LIGHTNING_RESISTANCE = Registry.registerReference(Registries.STATUS_EFFECT, Identifier.of(Willow.MOD_ID, "lightning_resistance"), new LightningResistanceStatusEffect());

    public static void initialize() {}

}

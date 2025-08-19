package phi.willow.registry;

import net.fabricmc.fabric.api.registry.FabricBrewingRecipeRegistryBuilder;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import phi.willow.Willow;
import phi.willow.statuseffects.LightningResistanceStatusEffect;
import phi.willow.statuseffects.ReachStatusEffect;

public class WillowEffectsAndPotions {

    public static final RegistryEntry<StatusEffect> LIGHTNING_RESISTANCE = Registry.registerReference(Registries.STATUS_EFFECT, Identifier.of(Willow.MOD_ID, "lightning_resistance"), new LightningResistanceStatusEffect());
    public static final RegistryEntry<StatusEffect> REACH = Registry.registerReference(Registries.STATUS_EFFECT, Identifier.of(Willow.MOD_ID, "reach"), new ReachStatusEffect());

    public static final Potion BUILDERS_POTION = Registry.register(Registries.POTION,
            Identifier.of(Willow.MOD_ID, "builders_potion"),
            new Potion("builders_potion", new StatusEffectInstance(REACH, 6000, 0)));
    public static final Potion BUILDERS_POTION_LONG = Registry.register(Registries.POTION,
            Identifier.of(Willow.MOD_ID, "builders_potion_long"),
            new Potion("builders_potion_long", new StatusEffectInstance(REACH, 14400, 0)));
    public static final Potion BUILDERS_POTION_STRONG = Registry.register(Registries.POTION,
            Identifier.of(Willow.MOD_ID, "builders_potion_strong"),
            new Potion("builders_potion_strong", new StatusEffectInstance(REACH, 6000, 1)));

    public static final Potion MINERS_POTION = Registry.register(Registries.POTION,
            Identifier.of(Willow.MOD_ID, "miners_potion"),
            new Potion("miners_potion", new StatusEffectInstance(StatusEffects.HASTE, 6000, 0)));
    public static final Potion MINERS_POTION_LONG = Registry.register(Registries.POTION,
            Identifier.of(Willow.MOD_ID, "miners_potion_long"),
            new Potion("miners_potion_long", new StatusEffectInstance(StatusEffects.HASTE, 14400, 0)));
    public static final Potion MINERS_POTION_STRONG = Registry.register(Registries.POTION,
            Identifier.of(Willow.MOD_ID, "miners_potion_strong"),
            new Potion("builders_potion_strong", new StatusEffectInstance(StatusEffects.HASTE, 6000, 1)));

    public static void registerPotionRecipes() {
        FabricBrewingRecipeRegistryBuilder.BUILD.register(builder -> {
            builder.registerPotionRecipe(Potions.AWKWARD, WillowItems.GOLDEN_BEETROOT, Registries.POTION.getEntry(BUILDERS_POTION));
            builder.registerPotionRecipe(Registries.POTION.getEntry(BUILDERS_POTION), Items.REDSTONE, Registries.POTION.getEntry(BUILDERS_POTION_LONG));
            builder.registerPotionRecipe(Registries.POTION.getEntry(BUILDERS_POTION), Items.GLOWSTONE_DUST, Registries.POTION.getEntry(BUILDERS_POTION_STRONG));
            // TODO: miner's potion with amethyst shard
            builder.registerPotionRecipe(Potions.AWKWARD, Items.AMETHYST_SHARD, Registries.POTION.getEntry(MINERS_POTION));
            builder.registerPotionRecipe(Registries.POTION.getEntry(MINERS_POTION), Items.REDSTONE, Registries.POTION.getEntry(MINERS_POTION_LONG));
            builder.registerPotionRecipe(Registries.POTION.getEntry(MINERS_POTION), Items.GLOWSTONE_DUST, Registries.POTION.getEntry(MINERS_POTION_STRONG));
        });
    }

}

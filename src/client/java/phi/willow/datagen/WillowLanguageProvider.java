package phi.willow.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;
import phi.willow.Willow;
import phi.willow.registry.WillowEffectsAndPotions;
import phi.willow.registry.WillowItems;

import java.util.concurrent.CompletableFuture;

public class WillowLanguageProvider extends FabricLanguageProvider {

    public WillowLanguageProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, "en_us", registryLookup);
    }

    @Override
    public void generateTranslations(RegistryWrapper.WrapperLookup wrapperLookup, TranslationBuilder translationBuilder) {
        translationBuilder.add(WillowEffectsAndPotions.LIGHTNING_RESISTANCE.value(), "Lightning Resistance");
        translationBuilder.add(WillowEffectsAndPotions.REACH.value(), "Reach");
        translationBuilder.add(WillowEffectsAndPotions.RECALL.value(), "Recall");

        translationBuilder.add(Identifier.of("item.minecraft", "potion.effect.builders_potion"), "Builder's Potion");
        translationBuilder.add(Identifier.of("item.minecraft", "potion.effect.builders_potion_long"), "Builder's Potion");
        translationBuilder.add(Identifier.of("item.minecraft", "potion.effect.builders_potion_strong"), "Builder's Potion");

        translationBuilder.add(Identifier.of("item.minecraft", "potion.effect.miners_potion"), "Miner's Potion");
        translationBuilder.add(Identifier.of("item.minecraft", "potion.effect.miners_potion_long"), "Miner's Potion");
        translationBuilder.add(Identifier.of("item.minecraft", "potion.effect.miners_potion_strong"), "Miner's Potion");

        translationBuilder.add(Identifier.of("item.minecraft", "potion.effect.recall_potion"), "Recall Potion");

        translationBuilder.add(WillowItems.THE_HERALD, "The Herald");
        translationBuilder.add(WillowItems.SLEDGEHAMMER, "Sledgehammer");
        translationBuilder.add(WillowItems.HAMMER_OF_THE_DEEP, "Hammer of the Deep");
        translationBuilder.add(WillowItems.EXCAVATOR, "Excavator");

        translationBuilder.add(WillowItems.KINDLING, "Kindling");
        translationBuilder.add(WillowItems.GOLDEN_BEETROOT, "Golden Beetroot");
        translationBuilder.add(WillowItems.ECHOIC_CATALYST, "Echoic Catalyst");
        translationBuilder.add(WillowItems.ECHOIC_UPGRADE_SMITHING_TEMPLATE, "Echoic Upgrade Smithing Template");

        translationBuilder.add(WillowItems.TOOL_HANDLE, "Tool Handle");
        translationBuilder.add(WillowItems.FLINT_AXE_HEAD, "Flint Axe Head");
        translationBuilder.add(WillowItems.FLINT_HOE_HEAD, "Flint Hoe Head");
        translationBuilder.add(WillowItems.FLINT_BLADE, "Flint Blade");
        translationBuilder.add(WillowItems.FLINT_PICKAXE_HEAD, "Flint Pickaxe Head");
        translationBuilder.add(WillowItems.FLINT_SHOVEL_HEAD, "Flint Shovel Head");

        translationBuilder.add(WillowItems.JOURNEYMANS_LOGBOOK, "Journeyman's Logbook");
        translationBuilder.add(WillowItems.APPRENTICE_MINING_MANUAL, "Apprentice Mining Manual");
        translationBuilder.add(WillowItems.APPRENTICE_WOODCUTTING_MANUAL, "Apprentice Woodcutting Manual");
        translationBuilder.add(WillowItems.APPRENTICE_FARMING_MANUAL, "Apprentice Farming Manual");
        translationBuilder.add(WillowItems.APPRENTICE_FIGHTING_MANUAL, "Apprentice Fighting Manual");
        translationBuilder.add(WillowItems.EXPERT_MINING_MANUAL, "Expert Mining Manual");
        translationBuilder.add(WillowItems.EXPERT_WOODCUTTING_MANUAL, "Expert Woodcutting Manual");
        translationBuilder.add(WillowItems.EXPERT_FARMING_MANUAL, "Expert Farming Manual");
        translationBuilder.add(WillowItems.EXPERT_FIGHTING_MANUAL, "Expert Fighting Manual");
        translationBuilder.add(WillowItems.MASTER_MINING_MANUAL, "Master Mining Manual");
        translationBuilder.add(WillowItems.MASTER_WOODCUTTING_MANUAL, "Master Woodcutting Manual");
        translationBuilder.add(WillowItems.MASTER_FARMING_MANUAL, "Master Farming Manual");
        translationBuilder.add(WillowItems.MASTER_FIGHTING_MANUAL, "Master Fighting Manual");

        translationBuilder.add(WillowItems.BAKED_EGG, "Baked Egg");
        translationBuilder.add(WillowItems.SPRING_SALAD, "Seasonal Salad");
        translationBuilder.add(WillowItems.POTATO_SALAD, "Potato Salad");
        translationBuilder.add(WillowItems.EGG_SANDWICH, "Egg Sandwich");
        translationBuilder.add(WillowItems.GRANDMAS_APPLE_PIE, "Grandma's Apple Pie");
        translationBuilder.add(WillowItems.PUMPKIN_CURRY, "Pumpkin Curry");

        translationBuilder.add(Identifier.of(Willow.MOD_ID, "profession_tooltip_novice"), "Novice");
        translationBuilder.add(Identifier.of(Willow.MOD_ID, "profession_tooltip_apprentice"), "Apprentice");
        translationBuilder.add(Identifier.of(Willow.MOD_ID, "profession_tooltip_expert"), "Expert");
        translationBuilder.add(Identifier.of(Willow.MOD_ID, "profession_tooltip_master"), "Master");
    }
}

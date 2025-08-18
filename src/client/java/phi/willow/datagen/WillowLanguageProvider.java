package phi.willow.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;
import phi.willow.Willow;
import phi.willow.registry.WillowItems;

import java.util.concurrent.CompletableFuture;

public class WillowLanguageProvider extends FabricLanguageProvider {

    public WillowLanguageProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, "en_us", registryLookup);
    }

    @Override
    public void generateTranslations(RegistryWrapper.WrapperLookup wrapperLookup, TranslationBuilder translationBuilder) {
        translationBuilder.add(WillowItems.THE_HERALD, "The Herald");
        translationBuilder.add(WillowItems.JOURNEYMANS_LOGBOOK, "Journeyman's Logbook");
        translationBuilder.add(WillowItems.SLEDGEHAMMER, "Sledgehammer");
        translationBuilder.add(WillowItems.HAMMER_OF_THE_DEEP, "Hammer of the Deep");
        translationBuilder.add(WillowItems.EXCAVATOR, "Excavator");
        translationBuilder.add(WillowItems.KINDLING, "Kindling");
        translationBuilder.add(WillowItems.TOOL_HANDLE, "Tool Handle");
        translationBuilder.add(WillowItems.FLINT_AXE_HEAD, "Flint Axe Head");
        translationBuilder.add(WillowItems.FLINT_HOE_HEAD, "Flint Hoe Head");
        translationBuilder.add(WillowItems.FLINT_BLADE, "Flint Blade");
        translationBuilder.add(WillowItems.FLINT_PICKAXE_HEAD, "Flint Pickaxe Head");
        translationBuilder.add(WillowItems.FLINT_SHOVEL_HEAD, "Flint Shovel Head");

        translationBuilder.add(Identifier.of(Willow.MOD_ID, "profession_tooltip_novice"), "Novice");
        translationBuilder.add(Identifier.of(Willow.MOD_ID, "profession_tooltip_apprentice"), "Apprentice");
        translationBuilder.add(Identifier.of(Willow.MOD_ID, "profession_tooltip_expert"), "Expert");
        translationBuilder.add(Identifier.of(Willow.MOD_ID, "profession_tooltip_master"), "Master");
    }
}

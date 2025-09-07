package phi.willow.registry;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import phi.willow.Willow;
import phi.willow.data.Profession;
import phi.willow.data.ProfessionLevel;
import phi.willow.items.*;

import java.util.List;
import java.util.function.Function;

public class WillowItems {
    // Weapons
    // TODO: reaper scythe, sword and hoe, big aoe attacks, lifesteal? Sweeping range attribute modifier
    // TODO: check if entering toolmat multiple times stacks somehow
    public static final Item REAPER_SCYTHE = register(
            "reaper_scythe",
            settings -> new HoeItem(ToolMaterial.NETHERITE, 0.0f, 0.0f, settings),
            new Item.Settings()
                    .sword(ToolMaterial.NETHERITE, 4.0f, -2.8f)
                    .attributeModifiers(AttributeModifiersComponent.builder()
                            .add(EntityAttributes.SWEEPING_DAMAGE_RATIO, new EntityAttributeModifier(Identifier.of(Willow.MOD_ID, "reaper_scythe_sweeping"), 1.0f, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.HAND)
                            .build())
                    .rarity(Rarity.EPIC)
    );

    // Tools
    // TODO: add cool recipe for turning sledgehammer into hammer of the deep (mine sculk with echo shards in inventory?, hit warden with echo shards in inventory?)
    public static final Item SLEDGEHAMMER = register(
            "sledgehammer",
            settings -> new SledgeHammerItem(settings, SledgeHammerItem.Type.SLEDGEHAMMER),
            new Item.Settings()
    );
    public static final Item HAMMER_OF_THE_DEEP = register(
            "hammer_of_the_deep",
            settings -> new SledgeHammerItem(settings, SledgeHammerItem.Type.HAMMER_OF_THE_DEEP),
            new Item.Settings()
    );
    public static final Item EXCAVATOR = register(
            "excavator",
            ExcavatorShovelItem::new,
            new Item.Settings()
    );

    public static final Item THE_HERALD = register(
            "the_herald",
            TheHeraldItem::new,
            new Item.Settings()
    );

    // Items
    public static final Item TOOL_HANDLE = register("tool_handle", Item::new, new Item.Settings());
    public static final Item FLINT_PICKAXE_HEAD = register("flint_pickaxe_head", Item::new, new Item.Settings());
    public static final Item FLINT_AXE_HEAD = register("flint_axe_head", Item::new, new Item.Settings());
    public static final Item FLINT_SHOVEL_HEAD = register("flint_shovel_head", Item::new, new Item.Settings());
    public static final Item FLINT_HOE_HEAD = register("flint_hoe_head", Item::new, new Item.Settings());
    public static final Item FLINT_BLADE = register("flint_blade", Item::new, new Item.Settings());
    public static final Item JOURNEYMANS_LOGBOOK = register("journeymans_logbook", JourneymansLogbookItem::new, new Item.Settings());
    public static final Item KINDLING = register("kindling", Item::new, new Item.Settings());
    public static final Item ECHOIC_CATALYST = register("echoic_catalyst", Item::new, new Item.Settings());
    public static final Item ECHOIC_UPGRADE_SMITHING_TEMPLATE = register("echoic_upgrade_smithing_template", settings ->
        new SmithingTemplateItem(
                // TODO: translatable
                // TODO: any other items here too
                Text.literal("Sledgehammer"),
                Text.literal("Echoic Catalyst"),
                // TODO: if any other items end up using this template, add here
                Text.literal("Add Sledgehammer"),
                Text.literal("Add Echoic Catalyst"),
                List.of(Identifier.ofVanilla("container/slot/pickaxe")),
                List.of(Identifier.ofVanilla("container/slot/ingot")),
                settings
        ), new Item.Settings().rarity(Rarity.UNCOMMON));

    // Ingredients
    public static final Item GOLDEN_BEETROOT = register("golden_beetroot", Item::new, new Item.Settings());

    // Food
    public static final Item BAKED_EGG = register("baked_egg", Item::new, new Item.Settings().food(WillowFoodComponents.BAKED_EGG));
    public static final Item SPRING_SALAD = register("spring_salad", Item::new, new Item.Settings().food(WillowFoodComponents.SPRING_SALAD).useRemainder(Items.BOWL).recipeRemainder(Items.GLASS_BOTTLE));
    public static final Item POTATO_SALAD = register("potato_salad", Item::new, new Item.Settings().food(WillowFoodComponents.POTATO_SALAD).useRemainder(Items.BOWL));
    public static final Item EGG_SANDWICH = register("egg_sandwich", Item::new, new Item.Settings().food(WillowFoodComponents.EGG_SANDWICH));
    public static final Item GRANDMAS_APPLE_PIE = register("grandmas_apple_pie", Item::new, new Item.Settings().food(WillowFoodComponents.GRANDMAS_APPLE_PIE).recipeRemainder(Items.BUCKET));
    public static final Item PUMPKIN_CURRY = register("pumpkin_curry", Item::new, new Item.Settings().food(WillowFoodComponents.PUMPKIN_CURRY).useRemainder(Items.BOWL));
    // Manuals
    public static final Item APPRENTICE_MINING_MANUAL = register("apprentice_mining_manual", settings -> new BaseManualItem(settings, Profession.MINING, ProfessionLevel.APPRENTICE), new Item.Settings());
    public static final Item APPRENTICE_WOODCUTTING_MANUAL = register("apprentice_woodcutting_manual", settings -> new BaseManualItem(settings, Profession.WOODCUTTING, ProfessionLevel.APPRENTICE), new Item.Settings());
    public static final Item APPRENTICE_FARMING_MANUAL = register("apprentice_farming_manual", settings -> new BaseManualItem(settings, Profession.FARMING, ProfessionLevel.APPRENTICE), new Item.Settings());
    public static final Item APPRENTICE_FIGHTING_MANUAL = register("apprentice_fighting_manual", settings -> new BaseManualItem(settings, Profession.FIGHTING, ProfessionLevel.APPRENTICE), new Item.Settings());
    public static final Item EXPERT_MINING_MANUAL = register("expert_mining_manual", settings -> new BaseManualItem(settings, Profession.MINING, ProfessionLevel.EXPERT), new Item.Settings());
    public static final Item EXPERT_WOODCUTTING_MANUAL = register("expert_woodcutting_manual", settings -> new BaseManualItem(settings, Profession.WOODCUTTING, ProfessionLevel.EXPERT), new Item.Settings());
    public static final Item EXPERT_FARMING_MANUAL = register("expert_farming_manual", settings -> new BaseManualItem(settings, Profession.FARMING, ProfessionLevel.EXPERT), new Item.Settings());
    public static final Item EXPERT_FIGHTING_MANUAL = register("expert_fighting_manual", settings -> new BaseManualItem(settings, Profession.FIGHTING, ProfessionLevel.EXPERT), new Item.Settings());
    public static final Item MASTER_MINING_MANUAL = register("master_mining_manual", settings -> new BaseManualItem(settings, Profession.MINING, ProfessionLevel.MASTER), new Item.Settings());
    public static final Item MASTER_WOODCUTTING_MANUAL = register("master_woodcutting_manual", settings -> new BaseManualItem(settings, Profession.WOODCUTTING, ProfessionLevel.MASTER), new Item.Settings());
    public static final Item MASTER_FARMING_MANUAL = register("master_farming_manual", settings -> new BaseManualItem(settings, Profession.FARMING, ProfessionLevel.MASTER), new Item.Settings());
    public static final Item MASTER_FIGHTING_MANUAL = register("master_fighting_manual", settings -> new BaseManualItem(settings, Profession.FIGHTING, ProfessionLevel.MASTER), new Item.Settings());

    public static Item register(String name, Function<Item.Settings, Item> itemFactory, Item.Settings settings)
    {
        RegistryKey<Item> itemKey = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Willow.MOD_ID, name));
        Item item = itemFactory.apply(settings.registryKey(itemKey));
        Registry.register(Registries.ITEM, itemKey, item);
        return item;
    }

    public static void initialize() {}
}

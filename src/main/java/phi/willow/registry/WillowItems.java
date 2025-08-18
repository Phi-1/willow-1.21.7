package phi.willow.registry;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.HoeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ToolMaterial;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import phi.willow.Willow;
import phi.willow.items.ExcavatorShovelItem;
import phi.willow.items.JourneymansLogbookItem;
import phi.willow.items.SledgeHammerItem;
import phi.willow.items.TheHeraldItem;

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

    // TODO: recipe
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
    // TODO: recipe
    public static final Item JOURNEYMANS_LOGBOOK = register("journeymans_logbook", JourneymansLogbookItem::new, new Item.Settings());
    public static final Item KINDLING = register("kindling", Item::new, new Item.Settings());

    // Food

    public static Item register(String name, Function<Item.Settings, Item> itemFactory, Item.Settings settings)
    {
        RegistryKey<Item> itemKey = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Willow.MOD_ID, name));
        Item item = itemFactory.apply(settings.registryKey(itemKey));
        Registry.register(Registries.ITEM, itemKey, item);
        return item;
    }

    public static void initialize() {}
}

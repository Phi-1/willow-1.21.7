package phi.willow.registry;

import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import phi.willow.Willow;
import phi.willow.items.JourneymansLogbookItem;
import phi.willow.items.SledgeHammerItem;
import phi.willow.items.TheHeraldItem;

import java.util.function.Function;

public class WillowItems {
    // Weapons

    // Tools
    // TODO: all data files
    public static final Item SLEDGEHAMMER = register(
            "sledgehammer",
            SledgeHammerItem::new,
            new Item.Settings()
    );

    public static final Item THE_HERALD = register(
            "the_herald",
            TheHeraldItem::new,
            new Item.Settings()
    );

    // Items
    // TODO: all data files
    public static final Item JOURNEYMANS_LOGBOOK = register("journeymans_logbook", JourneymansLogbookItem::new, new Item.Settings());

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

package phi.willow.registry;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import phi.willow.Willow;

public class WillowTags {

    public static class Items
    {
        public static TagKey<Item> NOVICE_USABLE_EQUIPMENT = TagKey.of(RegistryKeys.ITEM, Identifier.of(Willow.MOD_ID, "novice_usable_equipment"));
        public static TagKey<Item> APPRENTICE_USABLE_EQUIPMENT = TagKey.of(RegistryKeys.ITEM, Identifier.of(Willow.MOD_ID, "apprentice_usable_equipment"));
        public static TagKey<Item> EXPERT_USABLE_EQUIPMENT = TagKey.of(RegistryKeys.ITEM, Identifier.of(Willow.MOD_ID, "expert_usable_equipment"));
        public static TagKey<Item> MASTER_USABLE_EQUIPMENT = TagKey.of(RegistryKeys.ITEM, Identifier.of(Willow.MOD_ID, "master_usable_equipment"));
        public static TagKey<Item> SLEDGEHAMMER_TOOL_MATERIALS = TagKey.of(RegistryKeys.ITEM, Identifier.of(Willow.MOD_ID, "sledgehammer_tool_materials"));
        public static TagKey<Item> HAMMER_OF_THE_DEEP_TOOL_MATERIALS = TagKey.of(RegistryKeys.ITEM, Identifier.of(Willow.MOD_ID, "hammer_of_the_deep_tool_materials"));
    }

    public static class Blocks
    {
        public static TagKey<Block> NEEDS_WOODEN_TOOL = TagKey.of(RegistryKeys.BLOCK, Identifier.of(Willow.MOD_ID, "needs_wooden_tool"));
    }

    public static void initialize() {}
}

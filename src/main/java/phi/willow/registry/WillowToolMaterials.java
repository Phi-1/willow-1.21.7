package phi.willow.registry;

import net.minecraft.item.ToolMaterial;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.ItemTags;

public class WillowToolMaterials {
    // durability, mining speed, damage modifier, enchantability, repair materials
    public static final ToolMaterial SLEDGEHAMMER = new ToolMaterial(BlockTags.INCORRECT_FOR_STONE_TOOL, 250, 0.8f, 0.0f, 6, WillowTags.Items.SLEDGEHAMMER_TOOL_MATERIALS);
    public static final ToolMaterial HAMMER_OF_THE_DEEP = new ToolMaterial(BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 1800, 2.0f, 0.0f, 12, WillowTags.Items.HAMMER_OF_THE_DEEP_TOOL_MATERIALS);
    public static final ToolMaterial THE_HERALD = new ToolMaterial(BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 1400, 6.0f, 0.0f, 8, WillowTags.Items.THE_HERALD_TOOL_MATERIALS);
    public static final ToolMaterial EXCAVATOR = new ToolMaterial(BlockTags.INCORRECT_FOR_IRON_TOOL, 800, 1.3f, 0.0f, 14, ItemTags.IRON_TOOL_MATERIALS);
}

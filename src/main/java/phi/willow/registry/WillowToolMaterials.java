package phi.willow.registry;

import net.minecraft.item.ToolMaterial;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.ItemTags;

public class WillowToolMaterials {
    // TODO: refine enchantment value once materials are determined. Speaking of, materials.
    // durability, mining speed, damage modifier, enchantability, repair materials
    public static final ToolMaterial SLEDGEHAMMER = new ToolMaterial(BlockTags.INCORRECT_FOR_DIAMOND_TOOL, 1800, 4.0f, 8.0f, 12, ItemTags.DIRT);
}

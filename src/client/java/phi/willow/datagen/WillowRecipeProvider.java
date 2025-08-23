package phi.willow.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.recipe.RecipeExporter;
import net.minecraft.data.recipe.RecipeGenerator;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import phi.willow.registry.WillowItems;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class WillowRecipeProvider extends FabricRecipeProvider {

    public WillowRecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected RecipeGenerator getRecipeGenerator(RegistryWrapper.WrapperLookup wrapperLookup, RecipeExporter recipeExporter) {
        return new RecipeGenerator(wrapperLookup, recipeExporter) {
            @Override
            public void generate() {
                RegistryWrapper.Impl<Item> itemLookup = registries.getOrThrow(RegistryKeys.ITEM);
                // Sledgehammer
                createShaped(RecipeCategory.TOOLS, WillowItems.SLEDGEHAMMER, 1)
                        .pattern("ccc")
                        .pattern("csc")
                        .pattern(" s ")
                        .input('c', Items.STONE)
                        .input('s', Items.STICK)
                        .group("sledgehammer")
                        .criterion(hasItem(Items.STONE), conditionsFromItem(Items.STONE))
                        .offerTo(exporter);
                // Hammer of the Deep
                createShaped(RecipeCategory.TOOLS, WillowItems.HAMMER_OF_THE_DEEP, 1)
                        .pattern("ede")
                        .pattern("dhd")
                        .pattern(" s ")
                        .input('e', Items.ECHO_SHARD)
                        .input('d', Items.DIAMOND)
                        .input('h', WillowItems.SLEDGEHAMMER)
                        .input('s', Items.STICK)
                        .group("hammer_of_the_deep")
                        .criterion(hasItem(Items.ECHO_SHARD), conditionsFromItem(Items.ECHO_SHARD))
                        .offerTo(exporter);
                // Kindling
                createShapeless(RecipeCategory.MISC, WillowItems.KINDLING, 1)
                        .input(Items.STICK, 4)
                        .group("kindling")
                        .criterion(hasItem(Items.STICK), conditionsFromItem(Items.STICK))
                        .offerTo(exporter);
                // Golden Beetroot
                createShaped(RecipeCategory.BREWING, WillowItems.GOLDEN_BEETROOT, 1)
                        .pattern("nnn")
                        .pattern("nbn")
                        .pattern("nnn")
                        .input('n', Items.GOLD_NUGGET)
                        .input('b', Items.BEETROOT)
                        .group("golden_beetroot")
                        .criterion(hasItem(Items.GOLD_NUGGET), conditionsFromItem(Items.GOLD_NUGGET))
                        .offerTo(exporter);
                // Journeyman's Logbook
                createShapeless(RecipeCategory.MISC, WillowItems.JOURNEYMANS_LOGBOOK, 1)
                        .input(WillowItems.TOOL_HANDLE, 1)
                        .input(Items.BOOK, 1)
                        .group("journeymans_logbook")
                        .criterion(hasItem(Items.BOOK), conditionsFromItem(Items.BOOK))
                        .offerTo(exporter);
                // Tool Handle
                createShaped(RecipeCategory.TOOLS, WillowItems.TOOL_HANDLE, 1)
                        .pattern(" s ")
                        .pattern(" s ")
                        .input('s', Items.STICK)
                        .group("tool_handle")
                        .criterion(hasItem(Items.STICK), conditionsFromItem(Items.STICK))
                        .offerTo(exporter);
                // Flint Blade
                createShaped(RecipeCategory.COMBAT, WillowItems.FLINT_BLADE, 1)
                        .pattern("f")
                        .input('f', Items.FLINT)
                        .group("flint_blade")
                        .criterion(hasItem(Items.FLINT), conditionsFromItem(Items.FLINT))
                        .offerTo(exporter);
                // Flint Hoe Head
                createShaped(RecipeCategory.TOOLS, WillowItems.FLINT_HOE_HEAD, 1)
                        .pattern("ff")
                        .input('f', Items.FLINT)
                        .group("flint_hoe_head")
                        .criterion(hasItem(Items.FLINT), conditionsFromItem(Items.FLINT))
                        .offerTo(exporter);
                // Flint Axe Head
                createShaped(RecipeCategory.TOOLS, WillowItems.FLINT_AXE_HEAD, 1)
                        .pattern("ff")
                        .pattern("f ")
                        .input('f', Items.FLINT)
                        .group("flint_axe_head")
                        .criterion(hasItem(Items.FLINT), conditionsFromItem(Items.FLINT))
                        .offerTo(exporter);
                // Flint Pickaxe Head
                // FIXME: I'd like this to be a horizontally mirrored axe, however minecraft interprets that as identical to the axe recipe for some reason.
                createShaped(RecipeCategory.TOOLS, WillowItems.FLINT_PICKAXE_HEAD, 1)
                        .pattern(" f")
                        .pattern("ff")
                        .input('f', Items.FLINT)
                        .group("flint_pickaxe_head")
                        .criterion(hasItem(Items.FLINT), conditionsFromItem(Items.FLINT))
                        .offerTo(exporter);
                // Flint Shovel Head
                createShaped(RecipeCategory.TOOLS, WillowItems.FLINT_SHOVEL_HEAD, 1)
                        .pattern("f")
                        .pattern("f")
                        .input('f', Items.FLINT)
                        .group("flint_shovel_head")
                        .criterion(hasItem(Items.FLINT), conditionsFromItem(Items.FLINT))
                        .offerTo(exporter);

                // FOOD
                offerSmelting(List.of(Items.EGG), RecipeCategory.FOOD, WillowItems.BAKED_EGG, 0.1f, 200, "baked_egg");
                createShapeless(RecipeCategory.FOOD, WillowItems.SPRING_SALAD, 1)
                        .input(Items.BEETROOT)
                        .input(Items.CARROT)
                        .input(Items.HONEY_BOTTLE)
                        .input(Items.BOWL)
                        .group("spring_salad")
                        .criterion(hasItem(Items.HONEY_BOTTLE), conditionsFromItem(Items.HONEY_BOTTLE))
                        .offerTo(exporter);

                // NOTE: Only generate these once and then move them to minecraft namespace to replace vanilla recipes
//                // Wooden Sword
//                createShapeless(RecipeCategory.COMBAT, Items.WOODEN_SWORD, 1)
//                        .input(WillowItems.FLINT_BLADE)
//                        .input(WillowItems.TOOL_HANDLE)
//                        .group("flint_knife")
//                        .criterion(hasItem(WillowItems.FLINT_BLADE), conditionsFromItem(WillowItems.FLINT_BLADE))
//                        .offerTo(exporter);
//                // Wooden Axe
//                createShapeless(RecipeCategory.TOOLS, Items.WOODEN_AXE, 1)
//                        .input(WillowItems.FLINT_AXE_HEAD)
//                        .input(WillowItems.TOOL_HANDLE)
//                        .group("flint_axe")
//                        .criterion(hasItem(WillowItems.FLINT_AXE_HEAD), conditionsFromItem(WillowItems.FLINT_AXE_HEAD))
//                        .offerTo(exporter);
//                // Wooden Pickaxe
//                createShapeless(RecipeCategory.TOOLS, Items.WOODEN_PICKAXE, 1)
//                        .input(WillowItems.FLINT_PICKAXE_HEAD)
//                        .input(WillowItems.TOOL_HANDLE)
//                        .group("flint_pickaxe")
//                        .criterion(hasItem(WillowItems.FLINT_PICKAXE_HEAD), conditionsFromItem(WillowItems.FLINT_PICKAXE_HEAD))
//                        .offerTo(exporter);
//                // Wooden Hoe
//                createShapeless(RecipeCategory.TOOLS, Items.WOODEN_HOE, 1)
//                        .input(WillowItems.FLINT_HOE_HEAD)
//                        .input(WillowItems.TOOL_HANDLE)
//                        .group("flint_hoe")
//                        .criterion(hasItem(WillowItems.FLINT_HOE_HEAD), conditionsFromItem(WillowItems.FLINT_HOE_HEAD))
//                        .offerTo(exporter);
//                // Wooden Shovel
//                createShapeless(RecipeCategory.TOOLS, Items.WOODEN_SHOVEL, 1)
//                        .input(WillowItems.FLINT_SHOVEL_HEAD)
//                        .input(WillowItems.TOOL_HANDLE)
//                        .group("flint_shovel")
//                        .criterion(hasItem(WillowItems.FLINT_SHOVEL_HEAD), conditionsFromItem(WillowItems.FLINT_SHOVEL_HEAD))
//                        .offerTo(exporter);
            }
        };
    }

    @Override
    public String getName() {
        return "WillowRecipeGenerator";
    }
}

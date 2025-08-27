package phi.willow.registry;

import net.minecraft.component.type.FoodComponent;

public class WillowFoodComponents {
    public static final FoodComponent BAKED_EGG = new FoodComponent.Builder().nutrition(4).saturationModifier(0.6f).build();
    public static final FoodComponent SPRING_SALAD = new FoodComponent.Builder().nutrition(8).saturationModifier(0.6f).build();
    // TODO: baked egg, baked potato + sweet berries?
    public static final FoodComponent POTATO_SALAD = new FoodComponent.Builder().nutrition(6).saturationModifier(0.8f).build();
    // TODO: something with dried kelp? -> egg sandwich, baked egg, bread, kelp
    public static final FoodComponent EGG_SANDWICH = new FoodComponent.Builder().nutrition(8).saturationModifier(0.8f).build();
    // TODO: apple pie with glowberries? -> some cinnamon equivalent? / spice. 3 apples, 3 wheat, 1 glowberry, 1 milk, 1 sugar
    public static final FoodComponent GRANDMAS_APPLE_PIE = new FoodComponent.Builder().nutrition(8).saturationModifier(0.6f).build();
    // TODO: pumpkin curry -> pumpkin (x2?), mushroom, beetroot
    public static final FoodComponent PUMPKIN_CURRY = new FoodComponent.Builder().nutrition(8).saturationModifier(0.6f).build();
    // TODO: dessert with melons, cocoa beans, sugar, milk? -> apple, melon, cocoa beans, honey
    public static final FoodComponent FRUIT_SALAD = new FoodComponent.Builder().nutrition(6).saturationModifier(0.6f).build();
}

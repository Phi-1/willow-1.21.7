package phi.willow.registry;

import net.minecraft.component.type.FoodComponent;

public class WillowFoodComponents {
    public static final FoodComponent BAKED_EGG = new FoodComponent.Builder().nutrition(4).saturationModifier(0.6f).build();
    public static final FoodComponent SPRING_SALAD = new FoodComponent.Builder().nutrition(8).saturationModifier(0.6f).build();
    public static final FoodComponent POTATO_SALAD = new FoodComponent.Builder().nutrition(7).saturationModifier(0.8f).build();
    public static final FoodComponent EGG_SANDWICH = new FoodComponent.Builder().nutrition(8).saturationModifier(0.8f).build();
    public static final FoodComponent GRANDMAS_APPLE_PIE = new FoodComponent.Builder().nutrition(6).saturationModifier(0.6f).build();
    public static final FoodComponent PUMPKIN_CURRY = new FoodComponent.Builder().nutrition(8).saturationModifier(0.6f).build();
    // TODO: dessert with melons, cocoa beans, sugar, milk? -> apple, melon, cocoa beans, honey
    public static final FoodComponent FRUIT_SALAD = new FoodComponent.Builder().nutrition(6).saturationModifier(0.6f).build();
}

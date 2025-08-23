package phi.willow.registry;

import net.minecraft.component.type.FoodComponent;

public class WillowFoodComponents {
    public static final FoodComponent BAKED_EGG = new FoodComponent.Builder().nutrition(4).saturationModifier(0.6f).build();
    public static final FoodComponent SPRING_SALAD = new FoodComponent.Builder().nutrition(8).saturationModifier(0.6f).build();
    // TODO: baked egg, baked potato + ?
    public static final FoodComponent POTATO_SALAD = new FoodComponent.Builder().nutrition(6).saturationModifier(0.8f).build();
    // TODO: something with dried kelp?
}

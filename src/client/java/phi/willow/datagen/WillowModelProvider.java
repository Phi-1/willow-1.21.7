package phi.willow.datagen;

import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.data.BlockStateModelGenerator;
import net.minecraft.client.data.ItemModelGenerator;
import net.minecraft.client.data.Models;
import phi.willow.registry.WillowItems;

public class WillowModelProvider extends FabricModelProvider {

    public WillowModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {

    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        // TODO: there are options for dyeable overlay and tinted overlay, sounds cool
        itemModelGenerator.register(WillowItems.JOURNEYMANS_LOGBOOK, Models.GENERATED);
        itemModelGenerator.register(WillowItems.THE_HERALD, Models.GENERATED);
        itemModelGenerator.register(WillowItems.SLEDGEHAMMER, Models.GENERATED);
        itemModelGenerator.register(WillowItems.HAMMER_OF_THE_DEEP, Models.GENERATED);
        itemModelGenerator.register(WillowItems.EXCAVATOR, Models.GENERATED);

        itemModelGenerator.register(WillowItems.KINDLING, Models.GENERATED);
        itemModelGenerator.register(WillowItems.GOLDEN_BEETROOT, Models.GENERATED);
        itemModelGenerator.register(WillowItems.ECHOIC_CATALYST, Models.GENERATED);
        itemModelGenerator.register(WillowItems.ECHOIC_UPGRADE_SMITHING_TEMPLATE, Models.GENERATED);

        itemModelGenerator.register(WillowItems.TOOL_HANDLE, Models.GENERATED);
        itemModelGenerator.register(WillowItems.FLINT_HOE_HEAD, Models.GENERATED);
        itemModelGenerator.register(WillowItems.FLINT_SHOVEL_HEAD, Models.GENERATED);
        itemModelGenerator.register(WillowItems.FLINT_AXE_HEAD, Models.GENERATED);
        itemModelGenerator.register(WillowItems.FLINT_BLADE, Models.GENERATED);
        itemModelGenerator.register(WillowItems.FLINT_PICKAXE_HEAD, Models.GENERATED);

        itemModelGenerator.register(WillowItems.APPRENTICE_MINING_MANUAL, Models.GENERATED);
        itemModelGenerator.register(WillowItems.APPRENTICE_WOODCUTTING_MANUAL, Models.GENERATED);
        itemModelGenerator.register(WillowItems.APPRENTICE_FARMING_MANUAL, Models.GENERATED);
        itemModelGenerator.register(WillowItems.APPRENTICE_FIGHTING_MANUAL, Models.GENERATED);
        itemModelGenerator.register(WillowItems.EXPERT_MINING_MANUAL, Models.GENERATED);
        itemModelGenerator.register(WillowItems.EXPERT_WOODCUTTING_MANUAL, Models.GENERATED);
        itemModelGenerator.register(WillowItems.EXPERT_FARMING_MANUAL, Models.GENERATED);
        itemModelGenerator.register(WillowItems.EXPERT_FIGHTING_MANUAL, Models.GENERATED);
        itemModelGenerator.register(WillowItems.MASTER_MINING_MANUAL, Models.GENERATED);
        itemModelGenerator.register(WillowItems.MASTER_WOODCUTTING_MANUAL, Models.GENERATED);
        itemModelGenerator.register(WillowItems.MASTER_FARMING_MANUAL, Models.GENERATED);
        itemModelGenerator.register(WillowItems.MASTER_FIGHTING_MANUAL, Models.GENERATED);

        itemModelGenerator.register(WillowItems.BAKED_EGG, Models.GENERATED);
        itemModelGenerator.register(WillowItems.SPRING_SALAD, Models.GENERATED);
        itemModelGenerator.register(WillowItems.POTATO_SALAD, Models.GENERATED);
        itemModelGenerator.register(WillowItems.EGG_SANDWICH, Models.GENERATED);
        itemModelGenerator.register(WillowItems.GRANDMAS_APPLE_PIE, Models.GENERATED);
        itemModelGenerator.register(WillowItems.PUMPKIN_CURRY, Models.GENERATED);
    }

}

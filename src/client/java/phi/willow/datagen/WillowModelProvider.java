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
        itemModelGenerator.register(WillowItems.THE_HERALD, Models.GENERATED);
        itemModelGenerator.register(WillowItems.JOURNEYMANS_LOGBOOK, Models.GENERATED);
    }

}

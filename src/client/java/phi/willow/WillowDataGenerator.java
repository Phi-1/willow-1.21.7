package phi.willow;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import phi.willow.datagen.WillowModelProvider;
import phi.willow.datagen.WillowLanguageProvider;

public class WillowDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

		pack.addProvider(WillowModelProvider::new);
		pack.addProvider(WillowLanguageProvider::new);
	}
}

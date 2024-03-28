package com.github.teamfusion.just_a_slingshot.data;

import com.github.teamfusion.just_a_slingshot.data.client.ModelGenerator;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class DataGeneration implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator gen) {
        final FabricDataGenerator.Pack pack = gen.createPack();
        pack.addProvider(ModelGenerator::new);

    }
}
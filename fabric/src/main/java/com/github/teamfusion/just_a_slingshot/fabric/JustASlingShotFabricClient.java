package com.github.teamfusion.just_a_slingshot.fabric;

import com.github.teamfusion.just_a_slingshot.JustASlingShot;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;

public class JustASlingShotFabricClient implements ClientModInitializer {


    @Override
    public void onInitializeClient() {
        ModelLoadingRegistry.INSTANCE.registerModelProvider((manager, out) -> {
            out.accept(new ModelResourceLocation(
                    new ResourceLocation(JustASlingShot.MOD_ID, "slingshot_back"),
                    "inventory"));
        });
    }


}
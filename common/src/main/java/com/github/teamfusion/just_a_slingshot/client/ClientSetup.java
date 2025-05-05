package com.github.teamfusion.just_a_slingshot.client;

import com.github.teamfusion.just_a_slingshot.client.network.JustASlingShotClientNetwork;
import com.github.teamfusion.just_a_slingshot.common.item.slingshot.SlingshotItem;
import com.github.teamfusion.just_a_slingshot.common.item.slingshot.SlingshotPouchItem;
import com.github.teamfusion.just_a_slingshot.common.registry.EntityTypeRegistry;
import com.github.teamfusion.just_a_slingshot.common.registry.ItemRegistry;
import com.github.teamfusion.just_a_slingshot.platform.client.RenderRegistry;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;

public class ClientSetup {

    public static void onBootstrap() {
        JustASlingShotClientNetwork.registerReceivers();
        RenderRegistry.entityModel(EntityTypeRegistry.THROWN_DAMAGEABLE, ThrownItemRenderer::new);
        RenderRegistry.entityModel(EntityTypeRegistry.THROWN_EYE_OF_ENDER, ThrownItemRenderer::new);
    }

    public static void onInitialized() {
        ItemProperties.register(ItemRegistry.SLINGSHOT.get(), new ResourceLocation("pull"), (stack, level, entity, seed) -> {
            if (entity == null) {
                return 0.0f;
            }
            if (entity.getUseItem() != stack) {
                return 0.0f;
            }
            return SlingshotItem.getPowerForTime(stack.getUseDuration() - entity.getUseItemRemainingTicks());
        });
        ItemProperties.register(ItemRegistry.SLINGSHOT.get(), new ResourceLocation("pulling"), (stack, level, entity, seed) -> {
            return entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F;
        });
        ItemProperties.register(ItemRegistry.SLINGSHOT_POUCH.get(), new ResourceLocation("full"), (stack, level, entity, seed) -> {
            return SlingshotPouchItem.getFullnessDisplay(stack) > 0.0F ? 1.0F : 0.0F;
        });
    }
}
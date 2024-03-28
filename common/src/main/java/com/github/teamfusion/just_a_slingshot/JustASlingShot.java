package com.github.teamfusion.just_a_slingshot;

import com.github.teamfusion.just_a_slingshot.client.ClientSetup;
import com.github.teamfusion.just_a_slingshot.common.CommonSetup;
import com.github.teamfusion.just_a_slingshot.common.registry.CreativeTabRegistry;
import com.github.teamfusion.just_a_slingshot.common.registry.EntityTypeRegistry;
import com.github.teamfusion.just_a_slingshot.common.registry.ItemRegistry;
import com.github.teamfusion.just_a_slingshot.platform.ModInstance;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

public class JustASlingShot {
    public static final String MOD_ID = "just_a_slingshot";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final ModInstance INSTANCE = ModInstance.create(MOD_ID)
            .common(CommonSetup::onBootstrap)
            .postCommon(CommonSetup::onInitialized)
            .client(ClientSetup::onBootstrap)
            .postClient(ClientSetup::onInitialized)
            .build();

    public static void bootstrap() {
        INSTANCE.bootstrap();

        // ========== MISCELLANEOUS REGISTRY ==========
        ItemRegistry.ITEMS.register();
        CreativeTabRegistry.CREATIVE_TABS.register();

        // ========== ENTITY REGISTRY ==========
        EntityTypeRegistry.ENTITIES.register();
    }
}
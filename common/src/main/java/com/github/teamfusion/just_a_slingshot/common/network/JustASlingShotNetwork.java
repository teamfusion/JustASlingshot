package com.github.teamfusion.just_a_slingshot.common.network;

import com.github.teamfusion.just_a_slingshot.JustASlingShot;
import net.minecraft.resources.ResourceLocation;

public interface JustASlingShotNetwork {
    ResourceLocation
            SELECT_SYNC_PACKET = new ResourceLocation(JustASlingShot.MOD_ID, "select_sync_packet");

    static void registerReceivers() {
    }


}
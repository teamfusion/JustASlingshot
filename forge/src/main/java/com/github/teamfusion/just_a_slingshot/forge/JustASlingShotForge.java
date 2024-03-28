package com.github.teamfusion.just_a_slingshot.forge;

import com.github.teamfusion.just_a_slingshot.JustASlingShot;
import com.github.teamfusion.just_a_slingshot.platform.common.worldgen.BiomeManager;
import net.minecraftforge.fml.common.Mod;

@Mod(JustASlingShot.MOD_ID)
public class JustASlingShotForge {
    public JustASlingShotForge() {
        JustASlingShot.bootstrap();
        BiomeManager.setup();
    }
}
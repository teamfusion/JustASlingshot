package com.github.teamfusion.just_a_slingshot.common.registry;

import com.github.teamfusion.just_a_slingshot.JustASlingShot;
import com.github.teamfusion.just_a_slingshot.platform.CoreRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;

import java.util.function.Supplier;
import java.util.stream.Stream;

public class CreativeTabRegistry {
    public static final CoreRegistry<CreativeModeTab> CREATIVE_TABS = CoreRegistry.of(BuiltInRegistries.CREATIVE_MODE_TAB, JustASlingShot.MOD_ID);

    public static final Supplier<CreativeModeTab> JUST_A_SLINGSHOT = create("just_a_slingshot", () -> dev.architectury.registry.CreativeTabRegistry.create(builder -> {
        builder.title(Component.translatable("itemGroup.just_a_slingshot"))
                .icon(() -> ItemRegistry.SLINGSHOT.get().getDefaultInstance())
                .displayItems((parameters, output) -> {
                    output.acceptAll(Stream.of(
                            ItemRegistry.SLINGSHOT,
                            ItemRegistry.SLINGSHOT_POUCH,
                            ItemRegistry.PEBBLE
                    ).map(sup -> {
                        return sup.get().getDefaultInstance();
                    }).toList());
                }).build();
    }));

    public static <T extends CreativeModeTab> Supplier<T> create(String key, Supplier<T> entry) {
        return CREATIVE_TABS.create(key, entry);
    }
}
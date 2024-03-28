package com.github.teamfusion.just_a_slingshot.common.registry;

import com.github.teamfusion.just_a_slingshot.JustASlingShot;
import com.github.teamfusion.just_a_slingshot.common.item.slingshot.SlingshotItem;
import com.github.teamfusion.just_a_slingshot.common.item.slingshot.SlingshotPouchItem;
import com.github.teamfusion.just_a_slingshot.platform.CoreRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public class ItemRegistry {
    public static final CoreRegistry<Item> ITEMS = CoreRegistry.of(BuiltInRegistries.ITEM, JustASlingShot.MOD_ID);
    public static final Supplier<Item> SLINGSHOT = create("slingshot", () -> new SlingshotItem(new Item.Properties().durability(320)));

    public static final Supplier<Item> PEBBLE = create("pebble", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> SLINGSHOT_POUCH = create("slingshot_pouch", () -> new SlingshotPouchItem(new Item.Properties().stacksTo(1)));

    public static <T extends Item> Supplier<T> create(String key, Supplier<T> entry) {
        return ITEMS.create(key, entry);
    }
}
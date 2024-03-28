package com.github.teamfusion.just_a_slingshot.common.registry;

import com.github.teamfusion.just_a_slingshot.JustASlingShot;
import com.github.teamfusion.just_a_slingshot.common.entity.projectile.ThrownDamageableEntity;
import com.github.teamfusion.just_a_slingshot.platform.CoreRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

import java.util.function.Supplier;

public class EntityTypeRegistry {
    public static final CoreRegistry<EntityType<?>> ENTITIES = CoreRegistry.of(BuiltInRegistries.ENTITY_TYPE, JustASlingShot.MOD_ID);
    public static final Supplier<EntityType<ThrownDamageableEntity>> THROWN_DAMAGEABLE = create("thrown_damageable_projectile", EntityType.Builder.<ThrownDamageableEntity>of(ThrownDamageableEntity::new, MobCategory.MISC).sized(0.3F, 0.3F));

    private static <T extends Entity> Supplier<EntityType<T>> create(String key, EntityType.Builder<T> builder) {
        return ENTITIES.create(key, () -> builder.build(key));
    }
}
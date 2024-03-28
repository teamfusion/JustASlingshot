package com.github.teamfusion.just_a_slingshot.common;

import com.github.teamfusion.just_a_slingshot.common.item.slingshot.BuckshotSlingshotBehavior;
import com.github.teamfusion.just_a_slingshot.common.item.slingshot.SlingshotBehavior;
import com.github.teamfusion.just_a_slingshot.common.item.slingshot.SlingshotItem;
import com.github.teamfusion.just_a_slingshot.common.network.JustASlingShotNetwork;
import com.github.teamfusion.just_a_slingshot.common.network.JustASlingShotServerNetwork;
import com.github.teamfusion.just_a_slingshot.common.registry.ItemRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class CommonSetup {
    public static void onBootstrap() {
    }

    public static void onAmmoInit() {
        SlingshotItem.registerAmmo(Items.EGG, new SlingshotBehavior());
        SlingshotItem.registerAmmo(Items.SNOWBALL, new SlingshotBehavior());
        SlingshotItem.registerAmmo(ItemRegistry.PEBBLE.get(), new SlingshotBehavior());
        SlingshotItem.registerAmmo(Items.POISONOUS_POTATO, new SlingshotBehavior());
        SlingshotItem.registerAmmo(Items.PUFFERFISH, new SlingshotBehavior());
        SlingshotItem.registerAmmo(Items.SPIDER_EYE, new SlingshotBehavior());
        SlingshotItem.registerAmmo(Items.IRON_NUGGET, new SlingshotBehavior());
        SlingshotItem.registerAmmo(Items.GOLD_NUGGET, new SlingshotBehavior());
        SlingshotItem.registerAmmo(Items.RAW_IRON, new SlingshotBehavior());
        SlingshotItem.registerAmmo(Items.RAW_GOLD, new SlingshotBehavior());
        SlingshotItem.registerAmmo(Items.RAW_COPPER, new SlingshotBehavior());
        SlingshotItem.registerAmmo(Items.IRON_INGOT, new SlingshotBehavior());
        SlingshotItem.registerAmmo(Items.GOLD_INGOT, new SlingshotBehavior());
        SlingshotItem.registerAmmo(Items.COPPER_INGOT, new SlingshotBehavior());
        SlingshotItem.registerAmmo(Items.NETHERITE_INGOT, new SlingshotBehavior());
        SlingshotItem.registerAmmo(Items.DIAMOND, new SlingshotBehavior());
        SlingshotItem.registerAmmo(Items.EMERALD, new SlingshotBehavior());
        SlingshotItem.registerAmmo(Items.BRICK, new SlingshotBehavior());
        SlingshotItem.registerAmmo(Items.NETHER_BRICK, new SlingshotBehavior());
        SlingshotItem.registerAmmo(Items.BEETROOT_SEEDS, new BuckshotSlingshotBehavior());
        SlingshotItem.registerAmmo(Items.MELON_SEEDS, new BuckshotSlingshotBehavior());
        SlingshotItem.registerAmmo(Items.PUMPKIN_SEEDS, new BuckshotSlingshotBehavior());
        SlingshotItem.registerAmmo(Items.WHEAT_SEEDS, new BuckshotSlingshotBehavior());
        SlingshotItem.registerAmmo(Items.SPLASH_POTION, new SlingshotBehavior() {
            @Override
            public float getXRot() {
                return 10F;
            }

            @Override
            public float getMaxPower() {
                return 1.5F;
            }

            @Override
            public Projectile getProjectile(Level level, BlockPos pos, LivingEntity shooter, ItemStack stack, float power) {
                return new ThrownPotion(level, shooter);
            }
        });
        SlingshotItem.registerAmmo(Items.EXPERIENCE_BOTTLE, new SlingshotBehavior() {
            @Override
            public float getXRot() {
                return 10F;
            }

            @Override
            public float getMaxPower() {
                return 1.5F;
            }

            @Override
            public Projectile getProjectile(Level level, BlockPos pos, LivingEntity shooter, ItemStack stack, float power) {
                return new ThrownExperienceBottle(level, shooter);
            }
        });
        SlingshotItem.registerAmmo(Items.FIRE_CHARGE, new SlingshotBehavior() {

            @Override
            public Projectile getProjectile(Level level, BlockPos pos, LivingEntity shooter, ItemStack stack, float power) {
                LargeFireball largeFireball = new LargeFireball(EntityType.FIREBALL, level);
                Vec3 vec3 = shooter.getViewVector(1.0F);
                largeFireball.xPower = vec3.x * 0.05F * power;
                largeFireball.yPower = -0.03F;
                largeFireball.zPower = vec3.z * 0.05F * power;
                largeFireball.setPos(shooter.getX(), shooter.getEyeY(), shooter.getZ());
                return largeFireball;
            }
        });
        SlingshotItem.registerAmmo(Items.LINGERING_POTION, new SlingshotBehavior() {
            @Override
            public float getXRot() {
                return 10F;
            }

            @Override
            public float getMaxPower() {
                return 1.5F;
            }

            @Override
            public Projectile getProjectile(Level level, BlockPos pos, LivingEntity shooter, ItemStack stack, float power) {
                return new ThrownPotion(level, shooter);
            }
        });

        SlingshotItem.registerAmmo(Items.ENDER_PEARL, new SlingshotBehavior() {
            public float getMaxPower() {
                return 2.0F;
            }
            @Override
            public Projectile getProjectile(Level level, BlockPos pos, LivingEntity shooter, ItemStack stack, float power) {
                return new ThrownEnderpearl(level, shooter);
            }
        });
    }

    public static void onInitialized() {
        onAmmoInit();

        JustASlingShotNetwork.registerReceivers();
        JustASlingShotServerNetwork.registerReceivers();
    }
}
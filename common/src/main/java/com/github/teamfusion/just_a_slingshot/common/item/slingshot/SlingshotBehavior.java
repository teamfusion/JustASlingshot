package com.github.teamfusion.just_a_slingshot.common.item.slingshot;

import com.github.teamfusion.just_a_slingshot.api.IHoney;
import com.github.teamfusion.just_a_slingshot.common.entity.projectile.ThrownDamageableEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;

public class SlingshotBehavior {
    public Projectile getProjectile(Level level, BlockPos pos, LivingEntity shooter, ItemStack stack, float power) {
        return new ThrownDamageableEntity(level, shooter);
    }

    public void addProjectileEffects(Level level, LivingEntity shooter, Projectile slingshotProjectile, ItemStack stack) {
        if (slingshotProjectile instanceof ThrownDamageableEntity thrownDamageableEntity) {
            thrownDamageableEntity.setBaseDamage(3.0F);
            if (stack.getItem() == Items.POTION || stack.getItem() == Items.HONEY_BOTTLE) {
                //set egg and snowball damage
                thrownDamageableEntity.setBaseDamage(1F);
            }
            if (stack.getItem() == Items.EGG || stack.getItem() == Items.SNOWBALL) {
                //set egg and snowball damage
                thrownDamageableEntity.setBaseDamage(1F);
            }
            if (stack.is(Items.SLIME_BALL) || stack.is(Items.MAGMA_CREAM)) {
                thrownDamageableEntity.setBaseDamage(1F);
            }
            if (stack.is(ItemTags.VILLAGER_PLANTABLE_SEEDS)) {
                thrownDamageableEntity.setBaseDamage(0F);
            }
            if (stack.getItem() == Items.GOLD_NUGGET || stack.getItem() == Items.IRON_NUGGET) {
                thrownDamageableEntity.setBaseDamage(2F);
            }
            if (stack.getItem() == Items.PUFFERFISH || stack.getItem() == Items.SPIDER_EYE) {
                thrownDamageableEntity.setBaseDamage(2F);
            }
            if (stack.getItem() == Items.RAW_GOLD || stack.getItem() == Items.RAW_IRON || stack.getItem() == Items.RAW_COPPER) {
                thrownDamageableEntity.setBaseDamage(2.5F);
            }
            if (stack.getItem() == Items.BRICK || stack.getItem() == Items.NETHER_BRICK || stack.getItem() == Items.IRON_INGOT || stack.getItem() == Items.COPPER_INGOT) {
                thrownDamageableEntity.setBaseDamage(3F);
            }
            if (stack.getItem() == Items.NETHERITE_INGOT || stack.getItem() == Items.GOLD_INGOT) {
                thrownDamageableEntity.setBaseDamage(4F);
            }
            if (stack.getItem() == Items.DIAMOND || stack.getItem() == Items.EMERALD) {
                thrownDamageableEntity.setBaseDamage(3F);
            }
        }
        if (slingshotProjectile instanceof ThrowableItemProjectile projectile) {
            projectile.setItem(stack);
        }
    }

    public void shootBehavior(Level level, BlockPos pos, LivingEntity shooter, ItemStack stack, float power) {
        Projectile slingshotProjectile = this.getProjectile(level, pos, shooter, stack, power);

        slingshotProjectile.setOwner(shooter);
        slingshotProjectile.shootFromRotation(shooter, shooter.getXRot(), shooter.getYRot(), this.getXRot(), power * this.getMaxPower(), 1.0F);
        this.addProjectileEffects(level, shooter, slingshotProjectile, stack);
        level.addFreshEntity(slingshotProjectile);
    }

    public void hitEntityBehavior(Level level, BlockPos pos, Entity shooter, LivingEntity target, ItemStack stack) {
        if (stack.is(Items.SPIDER_EYE) || stack.is(Items.PUFFERFISH)) {
            target.addEffect(new MobEffectInstance(MobEffects.POISON, 100), shooter);
        }
        if (stack.is(Items.POTION) && PotionUtils.getPotion(stack) == Potions.WATER) {
            target.extinguishFire();
        }
        if (stack.is(Items.HONEY_BOTTLE) && target instanceof IHoney honey) {
            honey.setJust_a_slingshot$honeyTick(120);
        }
        if (stack.is(Items.CHORUS_FRUIT)) {
            if (!level.isClientSide) {
                double d = target.getX();
                double e = target.getY();
                double f = target.getZ();

                for (int i = 0; i < 16; ++i) {
                    double g = target.getX() + (target.getRandom().nextDouble() - (double) 0.5F) * (double) 16.0F;
                    double h = Mth.clamp(target.getY() + (double) (target.getRandom().nextInt(16) - 8), (double) level.getMinBuildHeight(), (double) (level.getMinBuildHeight() + ((ServerLevel) level).getLogicalHeight() - 1));
                    double j = target.getZ() + (target.getRandom().nextDouble() - (double) 0.5F) * (double) 16.0F;
                    if (target.isPassenger()) {
                        target.stopRiding();
                    }

                    Vec3 vec3 = target.position();
                    if (target.randomTeleport(g, h, j, true)) {
                        level.gameEvent(GameEvent.TELEPORT, vec3, GameEvent.Context.of(target));
                        SoundEvent soundEvent = target instanceof Fox ? SoundEvents.FOX_TELEPORT : SoundEvents.CHORUS_FRUIT_TELEPORT;
                        level.playSound((Player) null, d, e, f, soundEvent, SoundSource.PLAYERS, 1.0F, 1.0F);
                        target.playSound(soundEvent, 1.0F, 1.0F);
                        break;
                    }
                }
            }
        }
        if (stack.is(Items.MAGMA_CREAM)) {
            target.setSecondsOnFire(4);
        }

        if (stack.getItem() instanceof SpawnEggItem eggItem && level instanceof ServerLevel serverLevel) {
            spawnOffspringFromSpawnEgg((EntityType<? extends Mob>) eggItem.getType(stack.getTag()), serverLevel, target.position(), stack);
        }
    }

    private Optional<Mob> spawnOffspringFromSpawnEgg(EntityType<? extends Mob> entityType, ServerLevel serverLevel, Vec3 vec3, ItemStack itemStack) {
        if (itemStack.getItem() instanceof SpawnEggItem eggItem && !this.spawnsEntity(eggItem, itemStack.getTag(), entityType)) {
            return Optional.empty();
        } else {
            Mob mob2 = (Mob) entityType.create(serverLevel);


            if (mob2 == null) {
                return Optional.empty();
            } else {
                mob2.moveTo(vec3.x(), vec3.y(), vec3.z(), 0.0F, 0.0F);
                mob2.finalizeSpawn(serverLevel, serverLevel.getCurrentDifficultyAt(mob2.blockPosition()), MobSpawnType.SPAWN_EGG, (SpawnGroupData) null, null);

                serverLevel.addFreshEntityWithPassengers(mob2);
                if (itemStack.hasCustomHoverName()) {
                    mob2.setCustomName(itemStack.getHoverName());
                }

                return Optional.of(mob2);

            }
        }
    }

    private boolean spawnsEntity(SpawnEggItem eggItem, @Nullable CompoundTag compoundTag, EntityType<?> entityType) {
        return Objects.equals(eggItem.getType(compoundTag), entityType);
    }

    public boolean bounce(ItemStack stack) {
        if (stack.is(Items.SLIME_BALL) || stack.is(Items.MAGMA_CREAM)) {
            return true;
        }
        return false;
    }

    public void hitBlockBehavior(Level level, BlockPos pos, Entity shooter, ItemStack stack) {
        if (stack.getItem() instanceof SpawnEggItem eggItem && level instanceof ServerLevel serverLevel) {
            spawnOffspringFromSpawnEgg((EntityType<? extends Mob>) eggItem.getType(stack.getTag()), serverLevel, pos.getCenter(), stack);
        }
    }

    public float getMaxPower() {
        return 2.5F;
    }

    public float getXRot() {
        return 0F;
    }
}

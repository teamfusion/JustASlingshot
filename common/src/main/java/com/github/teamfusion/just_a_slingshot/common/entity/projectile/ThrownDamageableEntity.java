package com.github.teamfusion.just_a_slingshot.common.entity.projectile;

import com.github.teamfusion.just_a_slingshot.common.item.slingshot.SlingshotBehavior;
import com.github.teamfusion.just_a_slingshot.common.item.slingshot.SlingshotItem;
import com.github.teamfusion.just_a_slingshot.common.registry.EntityTypeRegistry;
import com.github.teamfusion.just_a_slingshot.common.registry.ItemRegistry;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class ThrownDamageableEntity extends ThrowableItemProjectile {
    private double baseDamage = 2.0D;
    @Nullable
    private SlingshotBehavior slingshotBehavior;

    public ThrownDamageableEntity(EntityType<? extends ThrownDamageableEntity> entityType, Level world) {
        super(entityType, world);
    }

    public ThrownDamageableEntity(Level world, LivingEntity living) {
        super(EntityTypeRegistry.THROWN_DAMAGEABLE.get(), living, world);
    }

    public ThrownDamageableEntity(Level world, double x, double y, double z) {
        super(EntityTypeRegistry.THROWN_DAMAGEABLE.get(), x, y, z, world);
    }

    @Override
    public void setItem(ItemStack itemStack) {
        super.setItem(itemStack);
        this.slingshotBehavior = SlingshotItem.getAmmoBehavior(itemStack.getItem());
    }

    @Override
    public void handleEntityEvent(byte b) {
        if (b == 3) {
            double d = 0.08;
            for (int i = 0; i < 8; ++i) {
                this.level().addParticle(new ItemParticleOption(ParticleTypes.ITEM, this.getItem()), this.getX(), this.getY(), this.getZ(), ((double) this.random.nextFloat() - 0.5) * 0.08, ((double) this.random.nextFloat() - 0.5) * 0.08, ((double) this.random.nextFloat() - 0.5) * 0.08);
            }
        }
    }

    @Override
    protected void onHit(HitResult hitResult) {
        super.onHit(hitResult);
        if (!this.level().isClientSide) {
            if (this.getItem().is(Items.FIRE_CHARGE)) {
                this.level().explode(this.getOwner(), this.getX(), this.getY(), this.getZ(), 0.8F, Level.ExplosionInteraction.NONE);
            }
            if (slingshotBehavior != null) {
                if (!slingshotBehavior.bounce(this.getItem()) || hitResult instanceof EntityHitResult) {
                    this.level().broadcastEntityEvent(this, (byte) 3);
                    this.discard();
                }
            } else {
                this.level().broadcastEntityEvent(this, (byte) 3);
                this.discard();
            }

        }
    }

    @Override
    protected void onHitBlock(BlockHitResult blockHitResult) {
        if (this.getItem().getItem() instanceof ItemNameBlockItem blockItem && this.getItem().is(ItemTags.VILLAGER_PLANTABLE_SEEDS)) {
            if (this.level().getBlockState(blockHitResult.getBlockPos()).is(Blocks.FARMLAND)) {
                if (blockHitResult.getDirection() == Direction.UP) {
                    this.level().setBlock(blockHitResult.getBlockPos().above(), blockItem.getBlock().defaultBlockState(), 3);
                }
            }
        }

        if (slingshotBehavior != null) {
            if (slingshotBehavior.bounce(this.getItem())) {
                if (this.getDeltaMovement().length() < 0.25) {
                    if (!this.level().isClientSide) {
                        this.level().broadcastEntityEvent(this, (byte) 3);
                        this.discard();
                    }
                } else {
                    Vec3i direction = blockHitResult.getDirection().getNormal();
                    switch (blockHitResult.getDirection()) {
                        case UP, SOUTH, EAST -> direction = direction.multiply(-1);
                        default -> {
                        }
                    }
                    direction = new Vec3i(direction.getX() == 0 ? 1 : direction.getX(), direction.getY() == 0 ? 1 : direction.getY(), direction.getZ() == 0 ? 1 : direction.getZ());
                    this.setDeltaMovement(this.getDeltaMovement().multiply(new Vec3(direction.getX(), direction.getY(), direction.getZ())).scale(0.9F));
                    this.playSound(SoundEvents.SLIME_ATTACK, 0.5F, 1.0F);
                }
            } else {
                if (slingshotBehavior != null) {
                    slingshotBehavior.hitBlockBehavior(this.level(), this.blockPosition(), this.getOwner(), this.getItem());
                }
            }
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        super.onHitEntity(entityHitResult);
        Entity entity = entityHitResult.getEntity();
        float f = (float) this.getDeltaMovement().length();
        int i = Mth.ceil(Mth.clamp((double) f * this.baseDamage, 0.0D, 2.147483647E9D) * 0.5F);

        if (i > 0) {
            entity.hurt(this.damageSources().thrown(this, this.getOwner()), i);
        }

        if (entity instanceof LivingEntity living) {
            if (slingshotBehavior != null) {
                slingshotBehavior.hitEntityBehavior(this.level(), entity.blockPosition(), this.getOwner(), living, this.getItem());
            }
        }
    }


    public void setBaseDamage(double p_70239_1_) {
        this.baseDamage = p_70239_1_;
    }

    public double getBaseDamage() {
        return this.baseDamage;
    }

    @Override
    protected Item getDefaultItem() {
        return ItemRegistry.PEBBLE.get();
    }
}
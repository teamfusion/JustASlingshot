package com.github.teamfusion.just_a_slingshot.common.entity.projectile;

import com.github.teamfusion.just_a_slingshot.common.registry.EntityTypeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class ThrownEyeOfEnder extends ThrowableProjectile implements ItemSupplier {
    private static final EntityDataAccessor<ItemStack> DATA_ITEM_STACK = SynchedEntityData.defineId(ThrownEyeOfEnder.class, EntityDataSerializers.ITEM_STACK);

    private double tx;
    private double ty;
    private double tz;
    private int life;
    private boolean surviveAfterDeath;

    public ThrownEyeOfEnder(EntityType<? extends ThrownEyeOfEnder> entityType, Level level) {
        super(entityType, level);
    }

    public ThrownEyeOfEnder(Level level, double d, double e, double f) {
        this(EntityTypeRegistry.THROWN_EYE_OF_ENDER.get(), level);
        this.setPos(d, e, f);
    }

    public void setItem(ItemStack itemStack) {
        if (!itemStack.is(Items.ENDER_EYE) || itemStack.hasTag()) {
            this.getEntityData().set(DATA_ITEM_STACK, itemStack.copyWithCount(1));
        }

    }

    private ItemStack getItemRaw() {
        return (ItemStack) this.getEntityData().get(DATA_ITEM_STACK);
    }

    public ItemStack getItem() {
        ItemStack itemStack = this.getItemRaw();
        return itemStack.isEmpty() ? new ItemStack(Items.ENDER_EYE) : itemStack;
    }

    @Override
    protected void defineSynchedData() {
        this.getEntityData().define(DATA_ITEM_STACK, ItemStack.EMPTY);
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double d) {
        double e = this.getBoundingBox().getSize() * (double) 4.0F;
        if (Double.isNaN(e)) {
            e = (double) 4.0F;
        }

        e *= (double) 64.0F;
        return d < e * e;
    }

    public void signalTo(BlockPos blockPos) {
        double d = (double) blockPos.getX();
        int i = blockPos.getY();
        double e = (double) blockPos.getZ();
        double f = d - this.getX();
        double g = e - this.getZ();
        double h = Math.sqrt(f * f + g * g);
        if (h > (double) 14.0F) {
            this.tx = this.getX() + f / h * (double) 14.0F;
            this.tz = this.getZ() + g / h * (double) 14.0F;
            this.ty = this.getY() + (double) 8.0F;
        } else {
            this.tx = d;
            this.ty = (double) i;
            this.tz = e;
        }

        this.life = 0;
        this.surviveAfterDeath = this.random.nextInt(15) > 0;
    }

    @Override
    public void lerpMotion(double d, double e, double f) {
        this.setDeltaMovement(d, e, f);
        if (this.xRotO == 0.0F && this.yRotO == 0.0F) {
            double g = Math.sqrt(d * d + f * f);
            this.setYRot((float) (Mth.atan2(d, f) * (double) (180F / (float) Math.PI)));
            this.setXRot((float) (Mth.atan2(e, g) * (double) (180F / (float) Math.PI)));
            this.yRotO = this.getYRot();
            this.xRotO = this.getXRot();
        }

    }

    @Override
    public void tick() {
        this.baseTick();
        Vec3 vec3 = this.getDeltaMovement();
        double d = this.getX() + vec3.x;
        double e = this.getY() + vec3.y;
        double f = this.getZ() + vec3.z;
        double g = vec3.horizontalDistance();
        this.setXRot(lerpRotation(this.xRotO, (float) (Mth.atan2(vec3.y, g) * (double) (180F / (float) Math.PI))));
        this.setYRot(lerpRotation(this.yRotO, (float) (Mth.atan2(vec3.x, vec3.z) * (double) (180F / (float) Math.PI))));
        if (!this.level().isClientSide) {
            double h = this.tx - d;
            double i = this.tz - f;
            float j = (float) Math.sqrt(h * h + i * i);
            float k = (float) Mth.atan2(i, h);
            double l = Mth.lerp(0.0025, g, (double) j);
            double m = vec3.y;
            if (j < 1.0F) {
                l *= 0.8;
                m *= 0.8;
            }

            int n = this.getY() < this.ty ? 1 : -1;
            vec3 = new Vec3(Math.cos((double) k) * l, m + ((double) n - m) * (double) 0.015F, Math.sin((double) k) * l);
            this.setDeltaMovement(vec3);
        }

        float o = 0.25F;
        if (this.isInWater()) {
            for (int p = 0; p < 4; ++p) {
                this.level().addParticle(ParticleTypes.BUBBLE, d - vec3.x * (double) 0.25F, e - vec3.y * (double) 0.25F, f - vec3.z * (double) 0.25F, vec3.x, vec3.y, vec3.z);
            }
        } else {
            this.level().addParticle(ParticleTypes.PORTAL, d - vec3.x * (double) 0.25F + this.random.nextDouble() * 0.6 - 0.3, e - vec3.y * (double) 0.25F - (double) 0.5F, f - vec3.z * (double) 0.25F + this.random.nextDouble() * 0.6 - 0.3, vec3.x, vec3.y, vec3.z);
        }

        if (!this.level().isClientSide) {
            this.setPos(d, e, f);
            ++this.life;
            if (this.life > 80 && !this.level().isClientSide) {
                this.playSound(SoundEvents.ENDER_EYE_DEATH, 1.0F, 1.0F);
                this.discard();
                if (this.surviveAfterDeath) {
                    this.level().addFreshEntity(new ItemEntity(this.level(), this.getX(), this.getY(), this.getZ(), this.getItem()));
                } else {
                    this.level().levelEvent(2003, this.blockPosition(), 0);
                }
            }
        } else {
            this.setPosRaw(d, e, f);
        }

    }

    protected static float lerpRotation(float f, float g) {
        while (g - f < -180.0F) {
            f -= 360.0F;
        }

        while (g - f >= 180.0F) {
            f += 360.0F;
        }

        return Mth.lerp(0.2F, f, g);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) {
        ItemStack itemStack = this.getItemRaw();
        if (!itemStack.isEmpty()) {
            compoundTag.put("Item", itemStack.save(new CompoundTag()));
        }

    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) {
        ItemStack itemStack = ItemStack.of(compoundTag.getCompound("Item"));
        this.setItem(itemStack);
    }

    @Override
    public float getLightLevelDependentMagicValue() {
        return 1.0F;
    }

    @Override
    public boolean isAttackable() {
        return false;
    }

}

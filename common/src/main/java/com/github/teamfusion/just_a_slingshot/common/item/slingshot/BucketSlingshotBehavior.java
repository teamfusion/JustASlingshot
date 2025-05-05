package com.github.teamfusion.just_a_slingshot.common.item.slingshot;

import com.github.teamfusion.just_a_slingshot.common.entity.projectile.ThrownDamageableEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MobBucketItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class BucketSlingshotBehavior extends SlingshotBehavior {

    @Override
    public void shootBehavior(Level level, BlockPos pos, LivingEntity shooter, ItemStack stack, float power) {
        if (stack.getItem() instanceof MobBucketItem bucketItem && level instanceof ServerLevel serverLevel) {
            Entity slingshotProjectile = bucketItem.type.create(serverLevel);
            if (slingshotProjectile instanceof LivingEntity living) {
                if (living instanceof Bucketable bucketable) {
                    bucketable.loadFromBucketTag(stack.getOrCreateTag());
                    bucketable.setFromBucket(true);
                }
                slingshotProjectile.setPos(shooter.getEyePosition());
                shootFromRotation(shooter, living, shooter.getXRot(), shooter.getYRot(), this.getXRot(), power * this.getMaxPower(), 1.0F);

                level.addFreshEntity(slingshotProjectile);
            }
            if (shooter instanceof Player player) {
                if (!player.isCreative()) {
                    if (!player.addItem(Items.BUCKET.getDefaultInstance())) {
                        player.drop(Items.BUCKET.getDefaultInstance(), false);
                    }
                }
            }
        }
    }

    public void shoot(LivingEntity entity, double d, double e, double f, float g, float h) {
        Vec3 vec3 = (new Vec3(d, e, f)).normalize().add(entity.getRandom().triangle((double) 0.0F, 0.0172275 * (double) h), entity.getRandom().triangle((double) 0.0F, 0.0172275 * (double) h), entity.getRandom().triangle((double) 0.0F, 0.0172275 * (double) h)).scale((double) g);
        entity.setDeltaMovement(vec3);
        double i = vec3.horizontalDistance();
        entity.setYRot((float) (Mth.atan2(vec3.x, vec3.z) * (double) (180F / (float) Math.PI)));
        entity.setXRot((float) (Mth.atan2(vec3.y, i) * (double) (180F / (float) Math.PI)));
        entity.yRotO = entity.getYRot();
        entity.xRotO = entity.getXRot();
    }

    public void shootFromRotation(Entity entity, LivingEntity shootedEntity, float f, float g, float h, float i, float j) {
        float k = -Mth.sin(g * ((float) Math.PI / 180F)) * Mth.cos(f * ((float) Math.PI / 180F));
        float l = -Mth.sin((f + h) * ((float) Math.PI / 180F));
        float m = Mth.cos(g * ((float) Math.PI / 180F)) * Mth.cos(f * ((float) Math.PI / 180F));
        this.shoot(shootedEntity, (double) k, (double) l, (double) m, i, j);
        Vec3 vec3 = entity.getDeltaMovement();
        shootedEntity.setDeltaMovement(shootedEntity.getDeltaMovement().add(vec3.x, entity.onGround() ? (double) 0.0F : vec3.y, vec3.z));
    }

    @Override
    public void addProjectileEffects(Level level, LivingEntity shooter, Projectile slingshotProjectile, ItemStack stack) {
        if (slingshotProjectile instanceof ThrownDamageableEntity thrownDamageableEntity) {
            thrownDamageableEntity.setBaseDamage(0.0F);

        }
    }
}

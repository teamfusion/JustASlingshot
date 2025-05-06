package com.github.teamfusion.just_a_slingshot.common.item.slingshot;

import com.github.teamfusion.just_a_slingshot.common.entity.projectile.ThrownDamageableEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class FeedSlingshotBehavior extends SlingshotBehavior {

    @Override
    public void hitEntityBehavior(Level level, BlockPos pos, Entity shooter, LivingEntity target, ItemStack stack) {
        super.hitEntityBehavior(level, pos, shooter, target, stack);
        if (target instanceof Animal animal && animal.isFood(stack)) {
            animal.setInLoveTime(600);
            level.broadcastEntityEvent(animal, (byte) 18);
        }
        if (target instanceof Player animal) {
            animal.getFoodData().eat(stack.getItem(), stack);
            animal.playSound(SoundEvents.GENERIC_EAT);
        }
    }

    @Override
    public void addProjectileEffects(Level level, LivingEntity shooter, Projectile slingshotProjectile, ItemStack stack) {
        if (slingshotProjectile instanceof ThrownDamageableEntity thrownDamageableEntity) {
            thrownDamageableEntity.setBaseDamage(0.0F);

        }
    }
}

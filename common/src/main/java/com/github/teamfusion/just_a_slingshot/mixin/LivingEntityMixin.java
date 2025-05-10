package com.github.teamfusion.just_a_slingshot.mixin;

import com.github.teamfusion.just_a_slingshot.api.IHoney;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements IHoney {
    @Unique
    private int just_a_slingshot$honeyTick;

    public LivingEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "baseTick", at = @At("TAIL"))
    public void baseTick(CallbackInfo ci) {
        if (this.just_a_slingshot$honeyTick > 0) {
            --this.just_a_slingshot$honeyTick;
            if (!this.level().isClientSide() && this.level() instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(ParticleTypes.FALLING_HONEY, this.getRandomX(this.getBbWidth()), this.getRandomY(), this.getRandomZ(this.getBbWidth()), 1, 0D, 0D, 0D, 0D);
            }
        }
    }

    @Unique
    public void setJust_a_slingshot$honeyTick(int just_a_slingshot$honeyTick) {
        this.just_a_slingshot$honeyTick = just_a_slingshot$honeyTick;
    }

    @Unique
    public int getJust_a_slingshot$honeyTick() {
        return just_a_slingshot$honeyTick;
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    protected void addAdditionalSaveData(CompoundTag compoundTag, CallbackInfo ci) {
        compoundTag.putInt("HoneyTick", this.getJust_a_slingshot$honeyTick());
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    protected void readAdditionalSaveData(CompoundTag compoundTag, CallbackInfo ci) {
        this.setJust_a_slingshot$honeyTick(compoundTag.getInt("HoneyTick"));
    }
}

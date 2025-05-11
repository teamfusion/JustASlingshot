package com.github.teamfusion.just_a_slingshot.mixin;

import com.github.teamfusion.just_a_slingshot.api.IHoney;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements IHoney {
    @Shadow
    @Nullable
    public abstract AttributeInstance getAttribute(Attribute attribute);

    private static final UUID SPEED_MODIFIER_HONEY_UUID = UUID.fromString("80302681-6d85-459b-884c-426d52a96beb");

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

    @Inject(method = "aiStep", at = @At("HEAD"))
    public void aiTick(CallbackInfo ci) {
        this.just_a_slingshot$removeHoney();
        this.just_a_slingshot$tryAddHoney();
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

    @Unique
    protected void just_a_slingshot$removeHoney() {
        AttributeInstance attributeInstance = this.getAttribute(Attributes.MOVEMENT_SPEED);
        if (attributeInstance != null) {
            if (attributeInstance.getModifier(SPEED_MODIFIER_HONEY_UUID) != null) {
                attributeInstance.removeModifier(SPEED_MODIFIER_HONEY_UUID);
            }

        }
    }

    @Unique
    protected void just_a_slingshot$tryAddHoney() {
        int i = this.getJust_a_slingshot$honeyTick();
        if (i > 0) {
            AttributeInstance attributeInstance = this.getAttribute(Attributes.MOVEMENT_SPEED);
            if (attributeInstance == null) {
                return;
            }

            float f = -0.05F;
            attributeInstance.addTransientModifier(new AttributeModifier(SPEED_MODIFIER_HONEY_UUID, "Honey slow", (double) f, AttributeModifier.Operation.ADDITION));
        }


    }
}

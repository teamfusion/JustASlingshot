package com.github.teamfusion.just_a_slingshot.mixin.client;

import com.github.teamfusion.just_a_slingshot.client.RenderHelper;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class GuiMixin {

    @Shadow
    private int screenWidth;

    @Shadow
    private int screenHeight;

    @Inject(method = "render", at = @At(value = "TAIL"))
    public void render(GuiGraphics guiGraphics, float f, CallbackInfo ci) {
        RenderHelper.renderSlingshotPouchSlot(guiGraphics, f);
    }
}

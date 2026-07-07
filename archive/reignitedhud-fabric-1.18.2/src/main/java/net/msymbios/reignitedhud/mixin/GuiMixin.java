package net.msymbios.reignitedhud.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Gui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class GuiMixin {

    // -- Methods --

    @Inject(method = "renderEffects", at = @At("HEAD"), cancellable = true)
    protected void renderEffects(PoseStack poseStack, CallbackInfo callbackInfo) {
        callbackInfo.cancel();
    } // renderEffects ()

    @Inject(method = "renderExperienceBar", at = @At("HEAD"), cancellable = true)
    public void renderExperienceBar(PoseStack poseStack, int i, CallbackInfo callbackInfo) {
        callbackInfo.cancel();
    } // renderExperienceBar ()

    @Inject(method = "renderPlayerHealth", at = @At("HEAD"), cancellable = true)
    private void renderPlayerHealth(PoseStack poseStack, CallbackInfo callbackInfo) {
        callbackInfo.cancel();
    } // renderPlayerHealth ()

    @Inject(method = "renderVehicleHealth", at = @At("HEAD"), cancellable = true)
    private void renderVehicleHealth(PoseStack poseStack, CallbackInfo callbackInfo) {
        callbackInfo.cancel();
    } // renderVehicleHealth ()

} // Class GuiMixin
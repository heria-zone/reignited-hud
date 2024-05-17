package net.msymbios.reignitedhud.mixin;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class GuiMixin {

    // -- Methods --

    @Inject(method = "renderEffects", at = @At("HEAD"), cancellable = true)
    protected void renderEffects(GuiGraphics guiGraphics, CallbackInfo callbackInfo) {
        callbackInfo.cancel();
    } // renderEffects ()

    @Inject(method = "renderExperienceBar", at = @At("HEAD"), cancellable = true)
    public void renderExperienceBar(GuiGraphics guiGraphics, int i, CallbackInfo callbackInfo) {
        callbackInfo.cancel();
    } // renderExperienceBar ()

    @Inject(method = "renderPlayerHealth", at = @At("HEAD"), cancellable = true)
    private void renderPlayerHealth(GuiGraphics guiGraphics, CallbackInfo callbackInfo) {
        callbackInfo.cancel();
    } // renderPlayerHealth ()

    @Inject(method = "renderVehicleHealth", at = @At("HEAD"), cancellable = true)
    private void renderVehicleHealth(GuiGraphics guiGraphics, CallbackInfo callbackInfo) {
        callbackInfo.cancel();
    } // renderVehicleHealth ()

} // Class GuiMixin
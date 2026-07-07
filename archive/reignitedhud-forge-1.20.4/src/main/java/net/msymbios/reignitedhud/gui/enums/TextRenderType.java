package net.msymbios.reignitedhud.gui.enums;

import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.util.FormattedCharSequence;
import org.joml.Matrix4f;

import java.awt.*;

/**
 * Text render types for rendering text in different styles
 */
public enum TextRenderType {

    // -- Values --

    NORMAL(TESClientUtil::renderDefaultStyleText, colour -> 0),
    DROP_SHADOW(TESClientUtil::renderDropShadowStyleText, colour -> TESClientUtil.multiplyARGBColour(colour, 0.25f)),
    GLOWING(TESClientUtil::renderGlowingStyleText, colour -> TESClientUtil.multiplyARGBColour(colour, 0.25f)),
    OUTLINED(TESClientUtil::renderOutlineStyleText, colour -> 0);

    // -- Variables --

    private final StyledTextRenderer style;
    private final Int2IntFunction outlineColourGenerator;

    // -- Constructor --

    TextRenderType(StyledTextRenderer style, Int2IntFunction outlineColourGenerator) {
        this.style = style;
        this.outlineColourGenerator = outlineColourGenerator;
    }

    // -- Methods --

    public void render(final Font fontRenderer, final PoseStack poseStack, final Component component, float x, float y, int colour, MultiBufferSource.BufferSource bufferSource) {
        render(fontRenderer, poseStack.last().pose(), component.getVisualOrderText(), x, y, colour, this.outlineColourGenerator.applyAsInt(colour), LightTexture.FULL_BRIGHT, bufferSource);
    }

    public void render(final Font fontRenderer, final Matrix4f pose, final FormattedCharSequence text, float x, float y, int colour, int outlineColour, int packedLight, final MultiBufferSource.BufferSource bufferSource) {
        this.style.render(fontRenderer, pose, text, x, y, colour, outlineColour, packedLight, bufferSource);
    } // render ()

    public int getOutlineColour(int colour) {
        return this.outlineColourGenerator.applyAsInt(colour);
    }

} // Class TextRenderType
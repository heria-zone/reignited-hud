package net.msymbios.reignitedhud.gui.render;

import com.mojang.blaze3d.font.GlyphInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.msymbios.reignitedhud.common.mixin.client.GuiGraphicsAccessor;
import net.msymbios.reignitedhud.gui.enums.TextRenderType;
import org.joml.Matrix4f;

import java.util.function.BiConsumer;

public class RenderDraw {

    // -- Methods --

    /**
     * Draw some text on screen at a given position, offset for the text's height and width
     */
    public static void centerTextForRender(Component text, float x, float y, BiConsumer<Float, Float> renderRunnable) {
        renderRunnable.accept(x - Minecraft.getInstance().font.width(text) / 2f, y + (Minecraft.getInstance().font.lineHeight - 1) / 2f);
    } // centerTextForRender ()

    /**
     * Wrapper for {@link GuiGraphics#drawString} to make it easier/more consistent to use
     * @param guiGraphics The GuiGraphics instance for the current render state
     * @param font The font instance to use for rendering the text
     * @param text The text to draw
     * @param x The x position on the screen to render at
     * @param y The y position on the screen to render at
     * @param colour The {@link net.minecraft.util.FastColor packed int} colour for the text
     */
    public static void drawText(GuiGraphics guiGraphics, Font font, String text, float x, float y, int colour) {
        drawText(guiGraphics, font, Component.literal(text), x, y, colour);
    }

    /**
     * Wrapper for {@link GuiGraphics#drawString} to make it easier/more consistent to use
     * @param guiGraphics The GuiGraphics instance for the current render state
     * @param font The font instance to use for rendering the text
     * @param text The text to draw
     * @param x The x position on the screen to render at
     * @param y The y position on the screen to render at
     * @param colour The {@link net.minecraft.util.FastColor packed int} colour for the text
     */
    public static void drawText(GuiGraphics guiGraphics, Font font, Component text, float x, float y, int colour) {
        renderDefaultStyleText(font, guiGraphics.pose().last().pose(), text.getVisualOrderText(), x, y, colour, TextRenderType.NORMAL.getOutlineColour(colour), LightTexture.FULL_BRIGHT, guiGraphics.bufferSource());
        ((GuiGraphicsAccessor)guiGraphics).callFlushIfUnmanaged();
    }

    /**
     * Render text with no additional styling
     */
    public static void renderDefaultStyleText(final Font fontRenderer, final Matrix4f pose, final FormattedCharSequence text, float x, float y, int colour, int outlineColour, int packedLight, final MultiBufferSource.BufferSource bufferSource) {
        fontRenderer.drawInBatch(text, x, y, colour, false, pose, bufferSource, Font.DisplayMode.NORMAL, outlineColour, packedLight);
    }

    /**
     * Wrapper for {@link GuiGraphics#drawString} to make it easier/more consistent to use
     * @param guiGraphics The GuiGraphics instance for the current render state
     * @param font The font instance to use for rendering the text
     * @param text The text to draw
     * @param x The x position on the screen to render at
     * @param y The y position on the screen to render at
     * @param colour The {@link net.minecraft.util.FastColor packed int} colour for the text
     */
    public static void drawTextWithShadow(GuiGraphics guiGraphics, Font font, String text, float x, float y, int colour) {
        drawTextWithShadow(guiGraphics, font, Component.literal(text), x, y, colour);
    }

    /**
     * Wrapper for {@link GuiGraphics#drawString} to make it easier/more consistent to use, with a drop shadow
     * @param guiGraphics The GuiGraphics instance for the current render state
     * @param text The text to draw
     * @param x The x position on the screen to render at
     * @param y The y position on the screen to render at
     * @param colour The {@link net.minecraft.util.FastColor packed int} colour for the text
     */
    public static void drawTextWithShadow(GuiGraphics guiGraphics, Font font, Component text, float x, float y, int colour) {
        renderDropShadowStyleText(font, guiGraphics.pose().last().pose(), text.getVisualOrderText(), x, y, colour, TextRenderType.DROP_SHADOW.getOutlineColour(colour), LightTexture.FULL_BRIGHT, guiGraphics.bufferSource());
        ((GuiGraphicsAccessor)guiGraphics).callFlushIfUnmanaged();
    }

    /**
     * Render text with an 'drop-shadow' style - The text has a shadow of itself offset to the bottom right a handful of pixels
     */
    public static void renderDropShadowStyleText(final Font fontRenderer, final Matrix4f pose, final FormattedCharSequence text, float x, float y, int colour, int outlineColour, int packedLight, final MultiBufferSource.BufferSource bufferSource) {
        final int borderColour = (outlineColour & -67108864) == 0 ? outlineColour | -16777216 : outlineColour;
        final Font.StringRenderOutput outlineOutput = fontRenderer.new StringRenderOutput(bufferSource, 0, 0, borderColour, false, pose, Font.DisplayMode.NORMAL, packedLight);
        final float[] newX = new float[] {x};

        text.accept((currentPosition, style, codePoint) -> {
            GlyphInfo glyphInfo = fontRenderer.getFontSet(style.getFont()).getGlyphInfo(codePoint, fontRenderer.filterFishyGlyphs);
            outlineOutput.x = newX[0] + glyphInfo.getShadowOffset();
            outlineOutput.y = y + glyphInfo.getShadowOffset();
            newX[0] += glyphInfo.getAdvance(style.isBold());

            return outlineOutput.accept(currentPosition, style.withColor(borderColour), codePoint);
        });

        Font.StringRenderOutput output = fontRenderer.new StringRenderOutput(bufferSource, x, y, (colour & -67108864) == 0 ? colour | -16777216 : colour, false, pose, Font.DisplayMode.POLYGON_OFFSET, packedLight);

        text.accept(output);
        output.finish(0, x);
    }

    /**
     * Wrapper for {@link GuiGraphics#drawString} to make it easier/more consistent to use
     * @param guiGraphics The GuiGraphics instance for the current render state
     * @param font The font instance to use for rendering the text
     * @param text The text to draw
     * @param x The x position on the screen to render at
     * @param y The y position on the screen to render at
     * @param colour The {@link net.minecraft.util.FastColor packed int} colour for the text
     */
    public static void drawTextWithGlow(GuiGraphics guiGraphics, Font font, String text, float x, float y, int colour) {
        drawTextWithGlow(guiGraphics, font, Component.literal(text), x, y, colour);
    }

    /**
     * Wrapper for {@link GuiGraphics#drawString} to make it easier/more consistent to use, with a glowing/full outline
     * @param guiGraphics The GuiGraphics instance for the current render state
     * @param text The text to draw
     * @param x The x position on the screen to render at
     * @param y The y position on the screen to render at
     * @param colour The {@link net.minecraft.util.FastColor packed int} colour for the text
     */
    public static void drawTextWithGlow(GuiGraphics guiGraphics, Font font, Component text, float x, float y, int colour) {
        renderGlowingStyleText(font, guiGraphics.pose().last().pose(), text.getVisualOrderText(), x, y, colour, TextRenderType.GLOWING.getOutlineColour(colour), LightTexture.FULL_BRIGHT, guiGraphics.bufferSource());
        ((GuiGraphicsAccessor)guiGraphics).callFlushIfUnmanaged();
    }

    /**
     * Render text with a 'glowing' style - The text is surrounded on all sides by a thick border with maximum brightness
     */
    public static void renderGlowingStyleText(final Font fontRenderer, final Matrix4f pose, final FormattedCharSequence text, float x, float y, int colour, int outlineColour, int packedLight, final MultiBufferSource.BufferSource bufferSource) {
        fontRenderer.drawInBatch8xOutline(text, x, y, colour, outlineColour, pose, bufferSource, packedLight);
    }

    /**
     * Wrapper for {@link GuiGraphics#drawString} to make it easier/more consistent to use
     * @param guiGraphics The GuiGraphics instance for the current render state
     * @param font The font instance to use for rendering the text
     * @param text The text to draw
     * @param x The x position on the screen to render at
     * @param y The y position on the screen to render at
     * @param colour The {@link net.minecraft.util.FastColor packed int} colour for the text
     */
    public static void drawTextWithOutline(GuiGraphics guiGraphics, Font font, String text, float x, float y, int colour) {
        drawTextWithOutline(guiGraphics, font, Component.literal(text), x, y, colour);
    }

    /**
     * Wrapper for {@link GuiGraphics#drawString} to make it easier/more consistent to use, with a thin outline
     * @param guiGraphics The GuiGraphics instance for the current render state
     * @param text The text to draw
     * @param x The x position on the screen to render at
     * @param y The y position on the screen to render at
     * @param colour The {@link net.minecraft.util.FastColor packed int} colour for the text
     */
    public static void drawTextWithOutline(GuiGraphics guiGraphics, Font font, Component text, float x, float y, int colour) {
        renderOutlineStyleText(font, guiGraphics.pose().last().pose(), text.getVisualOrderText(), x, y, colour, TextRenderType.OUTLINED.getOutlineColour(colour), LightTexture.FULL_BRIGHT, guiGraphics.bufferSource());
        ((GuiGraphicsAccessor)guiGraphics).callFlushIfUnmanaged();
    }

    /**
     * Render text with an 'outlined' style - The text is surrounded on all sides by a thin border
     */
    public static void renderOutlineStyleText(final Font fontRenderer, final Matrix4f pose, final FormattedCharSequence text, float x, float y, int colour, int outlineColour, int packedLight, final MultiBufferSource.BufferSource bufferSource) {
        final int borderColour = (outlineColour & -67108864) == 0 ? outlineColour | -16777216 : outlineColour;
        final Font.StringRenderOutput outlineOutput = fontRenderer.new StringRenderOutput(bufferSource, 0, 0, borderColour, false, pose, Font.DisplayMode.NORMAL, packedLight);

        for (float deltaX = -1; deltaX <= 1; deltaX++) {
            for (float deltaY = -1; deltaY <= 1; deltaY++) {
                if (deltaX == 0 ^ deltaY == 0) {
                    final float[] newX = new float[] {x};
                    final float offsetX = deltaX;
                    final float offsetY = deltaY;

                    text.accept((currentPosition, style, codePoint) -> {
                        GlyphInfo glyphInfo = fontRenderer.getFontSet(style.getFont()).getGlyphInfo(codePoint, fontRenderer.filterFishyGlyphs);
                        outlineOutput.x = newX[0] + offsetX * glyphInfo.getShadowOffset() * 0.6f;
                        outlineOutput.y = y + offsetY * glyphInfo.getShadowOffset() * 0.6f;
                        newX[0] += glyphInfo.getAdvance(style.isBold());

                        return outlineOutput.accept(currentPosition, style.withColor(borderColour), codePoint);
                    });
                }
            }
        }

        Font.StringRenderOutput output = fontRenderer.new StringRenderOutput(bufferSource, x, y, (colour & -67108864) == 0 ? colour | -16777216 : colour, false, pose, Font.DisplayMode.POLYGON_OFFSET, packedLight);

        text.accept(output);
        output.finish(0, x);
    }

} // Class RenderDraw
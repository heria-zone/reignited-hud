package net.msymbios.reignitedhud.gui.internal;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.msymbios.reignitedhud.config.ReignitedHudConfig;
import net.msymbios.reignitedhud.config.ReignitedHudID;

public class RenderDrawCallback {

    // -- Methods --

    /**
     * Draws a custom-sized texture on the screen.
     *
     * @param x The x-coordinate of the top-left corner of the texture
     * @param y The y-coordinate of the top-left corner of the texture
     * @param u The u-coordinate of the top-left corner of the texture in texture space
     * @param v The v-coordinate of the top-left corner of the texture in texture space
     * @param width The width of the texture
     * @param height The height of the texture
     * @param textureWidth The width of the entire texture
     * @param textureHeight The height of the entire texture
     */
    public static void drawCustomSizedTexture(int x, int y, float u, float v, int width, int height, float textureWidth, float textureHeight) {
        // Get the tessellator instance
        Tesselator tesselator = RenderSystem.renderThreadTesselator();
        // Get the buffer builder from the tessellator
        BufferBuilder bufferbuilder = tesselator.getBuilder();

        // Calculate the texture coordinate factors
        float f = 1.0F / textureWidth;
        float f1 = 1.0F / textureHeight;

        // Enable blending for transparency
        GlStateManager._enableBlend();
        // Set the blend function
        GlStateManager._blendFunc(GlStateManager.SourceFactor.SRC_ALPHA.value, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA.value); // Set blend function    // Start drawing the texture

        // Start drawing the texture
        bufferbuilder.begin(7, DefaultVertexFormat.POSITION_TEX);
        bufferbuilder.vertex(x, y + height, -1000.0).uv(u * f, (v + (float)height) * f1).endVertex();
        bufferbuilder.vertex(x + width, y + height, -1000.0).uv((u + (float)width) * f, (v + (float)height) * f1).endVertex();
        bufferbuilder.vertex((x + width), (y), -1000.0).uv((u + (float)width) * f, v * f1).endVertex();
        bufferbuilder.vertex((x), (y), -1000.0).uv(u * f, v * f1).endVertex();
        tesselator.end();
    } // drawCustomSizedTexture ()

    /**
     * Draws a string using the specified font at the given position with the specified color.
     *
     * @param string The string to draw
     * @param posX The x-coordinate of the position to draw the string
     * @param posY The y-coordinate of the position to draw the string
     * @param color The color of the string
     */
    public static void drawFont(String string, int posX, int posY, int color) {
        // Get the font renderer from the Minecraft instance
        Font font = Minecraft.getInstance().font;
        // Draw the string at the specified position with the specified color
        font.draw(new PoseStack(), string, posX, posY, color);
        // Reset the blend color
        GlStateManager._clearColor(1.0F, 1.0F, 1.0F, 1.0F);
    } // drawFont ()

    /**
     * Draws a string with a shadow using the specified main color and shadow color.
     *
     * @param string The string to be drawn
     * @param posX The x-coordinate of the starting position
     * @param posY The y-coordinate of the starting position
     * @param color The color of the main text
     * @param shadow The color of the shadow
     */
    public static void drawFontWithShadow(String string, int posX, int posY, int color, int shadow) {
        // Create a new pose stack
        PoseStack pose = new PoseStack();
        // Get the font renderer instance
        Font font = Minecraft.getInstance().font;
        // Draw the shadow of the string at an offset position
        font.drawShadow(pose, string, (posX + 1), (posY + 1), shadow, false);
        // Draw the main text at the specified position
        font.drawShadow(pose, string, posX, posY, color, false);
        // Reset the blend color
        GlStateManager._clearColor(1.0F, 1.0F, 1.0F, 1.0F);
    } // drawFontWithShadow ()

    /**
     * Draws a text with a shadow at the specified position using the main color.
     *
     * @param text the text to draw
     * @param posX the x-coordinate position
     * @param posY the y-coordinate position
     * @param color the main color to use
     */
    public static void drawFontWithShadow(String text, int posX, int posY, int color) {
        // Get the font renderer instance
        Font font = Minecraft.getInstance().font;
        // Draw the text with shadow using the specified parameters
        font.drawShadow(new PoseStack(), text, posX, posY, color, true);
        // Reset the blend color
        GlStateManager._clearColor(1.0F, 1.0F, 1.0F, 1.0F);
    } // drawFontWithShadow ()

    /**
     * Draws a bold font with shadows around the text at the specified position using the main color and shadow color.
     *
     * @param text the text to be drawn
     * @param posX the x-coordinate position
     * @param posY the y-coordinate position
     * @param color the main color of the text
     * @param shadow the color of the shadow
     */
    public static void drawFontBold(String text, int posX, int posY, int color, int shadow) {
        // Create a new matrix stack
        PoseStack pose = new PoseStack();

        // Get the font renderer instance
        Font font = Minecraft.getInstance().font;

        // Draw shadows around the text
        font.drawShadow(pose, text, (posX + 1), posY, shadow, false);
        font.drawShadow(pose, text, (posX - 1), posY, shadow, false);
        font.drawShadow(pose, text, posX, (posY + 1), shadow, false);
        font.drawShadow(pose, text, posX, (posY - 1), shadow, false);

        // Draw the main text
        font.drawShadow(pose, text, posX, posY, color, false);

        // Reset the blend color
        GlStateManager._clearColor(1.0F, 1.0F, 1.0F, 1.0F);
    } // drawFontBold ()

    /**
     * Draws a font with shadow aligned to the right side of the specified position using the main color and shadow color.
     *
     * @param text the text to be drawn
     * @param posX the x-coordinate position
     * @param posY the y-coordinate position
     * @param color the main color of the text
     * @param shadow the color of the shadow
     */
    public static void drawFontWithShadowRightAligned(String text, int posX, int posY, int color, int shadow) {
        // Calculate the new x-coordinate position for right alignment
        int newX = posX - getStringWidth(text);
        // Draw the font with shadow at the newly calculated position
        drawFontWithShadow(text, newX, posY, color, shadow);
    } // drawFontWithShadowRightAligned ()

    /**
     * Draws the given text with a shadow at the specified position, centered horizontally.
     *
     * @param text      the text to be drawn
     * @param posX        the x-coordinate position for centering
     * @param posY        the y-coordinate position for drawing
     * @param color   the color of the main text
     * @param shadow the color of the shadow
     */
    public static void drawFontWithShadowCentered(String text, int posX, int posY, int color, int shadow) {
        // Calculate the new x-coordinate position for right alignment
        int newX = posX - getStringWidth(text) / 2;
        // Draw the font with shadow at the centered position
        drawFontWithShadow(text, newX, posY, color, shadow);
    } // drawFontWithShadowCentered ()

    /**
     * Draws a text with bold font centered at the specified position with main and shadow colors.
     *
     * @param text The text to be drawn
     * @param posX The x-coordinate of the center position
     * @param posY The y-coordinate of the center position
     * @param color The main color of the font
     * @param shadow The shadow color of the font
     */
    public static void drawFontBoldCentered(String text, int posX, int posY, int color, int shadow) {
        // Calculate the new x-coordinate to center the text
        int newX = posX - getStringWidth(text) / 2;
        // Draw the text with bold font at the new x-coordinate and specified y-coordinate
        drawFontBold(text, newX, posY, color, shadow);
    } // drawFontBoldCentered ()

    /**
     * Calculates the width of the specified string in pixels.
     *
     * @param string The string for which the width is to be calculated
     * @return The width of the string in pixels
     */
    public static int getStringWidth(String string) {
        Font font = Minecraft.getInstance().font;
        return font.width(string);
    } // getStringWidth ()

    /**
     * Draws an icon at the specified position.
     *
     * @param posX The x-coordinate of the top-left corner of the icon
     * @param posY The y-coordinate of the top-left corner of the icon
     * @param row The row of the icon
     * @param pos The position of the icon
     */
    public static void drawIcon(int posX, int posY, int row, int pos) {
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.gui.blit(new PoseStack(), posX, posY, pos * 10 - 10, row * 10 - 10, 10, 10);
    } // drawIcon ()

    /**
     * Draws the player icon at the specified position with the given size.
     *
     * @param posX the x-coordinate of the position
     * @param posY the y-coordinate of the position
     * @param size the size of the player icon
     */
    public static void drawPlayerIcon(int posX, int posY, int size) {
        // Draw the player icon with custom sized textures
        drawCustomSizedTexture(posX, posY, (float)size, (float)size, size, size, (float)(size * 8), (float)(size * 8));
        drawCustomSizedTexture(posX, posY, (float)(size * 5), (float)size, size, size, (float)(size * 8), (float)(size * 8));
    } // drawPlayerIcon ()

    /**
     * Draws a medium-sized bar at the specified position with the given parameters.
     *
     * @param posX The x-coordinate of the top-left corner of the bar
     * @param posY The y-coordinate of the top-left corner of the bar
     * @param bar The value representing the size of the bar (ranges from 1 to 10)
     * @param fill The variable determining the fill amount of the bar (ranges from 0.0 to 1.0)
     */
    public static void drawMediumBar( int posX, int posY, int bar, float fill) {
        // Create a new matrix stack
        PoseStack pose = new PoseStack();
        Minecraft minecraft = Minecraft.getInstance();

        // Calculate the position in the texture for the bar & bar background
        int barNumber = bar * 10 - 10;
        int barNumberBG = bar * 10 - 4;

        // Render the bar
        minecraft.gui.blit(pose, posX, posY, 0, barNumber, 91, 5);

        // Render the filled portion of the bar based on the variable fill
        minecraft.gui.blit(pose, posX + 1, posY + 1, 1, barNumberBG, (int)(fill * 89.0F), 3);
    } // drawMediumBar ()

    /**
     * Draws a long bar on the screen.
     *
     * @param posX the x-coordinate of the bar
     * @param posY the y-coordinate of the bar
     * @param bar the value of the bar (normalized from 0 to 1)
     * @param var the variation of the bar (normalized from 0 to 1)
     */
    public static void drawLongBar(int posX, int posY, int bar, float var) {
        // Create a new matrix stack
        PoseStack pose = new PoseStack();
        Minecraft minecraft = Minecraft.getInstance();

        // Calculate the position of the bar in the texture
        int barNumber = bar * 10 - 10;
        int barNumberBG = bar * 10 - 4;

        // Draw the background of the bar
        minecraft.gui.blit(pose, posX, posY, 91, barNumber, 121, 5);

        // Draw the filled part of the bar based on the variation
        minecraft.gui.blit(pose, posX + 1, posY + 1, 92, barNumberBG, (int)(var * 119.0F), 3);
    } // drawLongBar ()

    /**
     * Draw a durability bar on the screen.
     *
     * @param posX the x-coordinate of the top-left corner of the bar
     * @param posY the y-coordinate of the top-left corner of the bar
     * @param bar the durability level of the bar (from 0 to 1)
     * @param var the variation of the bar (from 0 to 1)
     */
    public static void drawDurabilityBar(int posX, int posY, int bar, float var) {
        // Create a new matrix stack
        PoseStack pose = new PoseStack();
        Minecraft minecraft = Minecraft.getInstance();

        // Calculate the position of the bar and background based on the durability level
        int barNumber = bar * 10 - 10;
        int barNumberBG = bar * 10 - 4;

        // Draw the durability bar and background
        minecraft.gui.blit(pose, posX, posY, 212, 76 + barNumber, 37, 5);
        minecraft.gui.blit(pose, posX + 1, posY + 1, 213, 76 + barNumberBG, (int)(var * 35.0F), 3);
    } // drawDurabilityBar ()

    /**
     * Adds a durability display for the given ItemStack at the specified position.
     *
     * @param stack The ItemStack for which to display durability
     * @param pos The position offset for the display
     */
    public static void addDurabilityDisplay(ItemStack stack, int pos) {
        // Create a new matrix stack
        PoseStack pose = new PoseStack();

        // Get the Minecraft instance and the player
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;

        // Get the Minecraft instance and the player
        if (player == null || stack.getItem() == Items.AIR) return;

        // Calculate additional offset based on player's vehicle
        int posY = (!ReignitedHudConfig.PLAYER_SKIN &&
                !ReignitedHudConfig.PLAYER_HEALTH &&
                !ReignitedHudConfig.PLAYER_USERNAME &&
                !ReignitedHudConfig.ARMOR_LEVEL &&
                !ReignitedHudConfig.ARMOR_TOUGHNESS &&
                !ReignitedHudConfig.FOOD_LEVEL &&
                !ReignitedHudConfig.FOOD_SATURATION)
                ? 0 : 50;

        int add = 0;
        if (player.getVehicle() instanceof LivingEntity) {
            if (player.getVehicle() instanceof Horse && ((Horse)player.getVehicle()).isTamed()) add += 33;
            else add += 27;
        }

        int count = 0;

        // Calculate the count of items with the same type and durability in player's inventory
        int maxDurability;
        for(maxDurability = 0; maxDurability < player.inventory.getContainerSize(); ++maxDurability) {
            ItemStack item = player.inventory.getItem(maxDurability);
            if (item.getItem() == stack.getItem() && stack.getMaxDamage() == item.getMaxDamage() && stack.getTag() == item.getTag()) {
                count += item.getCount();
            }
        }

        maxDurability = stack.getMaxDamage() + 1;
        int durability = stack.getMaxDamage() - stack.getDamageValue() + 1;
        String display = "" + count;
        float var = 35.0F;
        int color = 16777215;
        int isdmgbl = 2;

        // Calculate the display details for damageable items
        if (stack.isDamageableItem()) {
            display = "" + durability;
            isdmgbl = 0;
            var = (float)durability / (float)maxDurability;
            color = 5635925;
            int tex = 1;

            // Adjust color and texture based on durability percentage
            if ((double)durability < (double)maxDurability * 0.75) {
                color = 12251978;
                tex = 2;
            }

            if ((double)durability < (double)maxDurability * 0.5) {
                color = 16777045;
                tex = 3;
            }

            if ((double)durability < (double)maxDurability * 0.25) {
                color = 16743765;
                tex = 4;
            }

            if ((double)durability < (double)maxDurability * 0.05) {
                color = 16733525;
                tex = 5;
            }

            // Draw the durability bar on the screen
            minecraft.getTextureManager().bind(ReignitedHudID.TEX_HUD_BAR);
            drawDurabilityBar(34, 24 + posY + pos + add, tex, var);
        }

        // Draw the base HUD elements
        minecraft.getTextureManager().bind(ReignitedHudID.TEX_HUD_BASE);
        minecraft.gui.blit(pose, 34, 11 + posY  + isdmgbl + pos + add, 49, 0, 39, 14);
        minecraft.gui.blit(pose, 15, 10 + posY  + pos + add, 29, 0, 20, 20);
        minecraft.getItemRenderer().renderGuiItem(stack, 17, 12 + posY  + pos + add);
        drawFontWithShadow(display, 38, 14 + posY  + isdmgbl + pos + add, color, 0);
    } // addDurabilityDisplay ()

} // Class RenderDrawCallback
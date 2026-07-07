package net.msymbios.reignitedhud.gui.element;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.player.Player;
import net.msymbios.reignitedhud.config.ReignitedHudConfig;
import net.msymbios.reignitedhud.gui.internal.RenderDrawCallback;
import net.msymbios.reignitedhud.gui.render.RenderDraw;

/**
 * HUD elements for the default rendering
 */
public class HudElements {

    // -- Methods --

    public static int renderPlayerSkin(GuiGraphics graphics, Minecraft minecraft, float tick, Player player, float opacity, boolean inWorldHud) {
        return 0;
    } // renderPlayerSkin ()

    public static int renderPlayerUsername (GuiGraphics graphics, Minecraft minecraft, float tick, Player player, float opacity, boolean inWorldHud) {
        if(inWorldHud) {
            RenderDraw.centerTextForRender(player.getName().getString(), 0, 0, (x, y) -> TESAPI.getConfig().inWorldHudEntityNameFontStyle().render(minecraft.font, graphics.pose(), player.getName().getString(), x, y, FastColor.ARGB32.color((int)(opacity * 255f), 255, 255, 255), graphics.bufferSource()));
        } else {
            TESAPI.getConfig().hudEntityNameFontStyle().render(minecraft.font, graphics.pose(), player.getName().getString(), 0, 0, FastColor.ARGB32.color((int)(opacity * 255f), 255, 255, 255), graphics.bufferSource());
        }

        /*// Get the player's name as a string
        String playerName = player.getName().getString();

        // Set the position for rendering the player's name
        int posX = !ReignitedHudConfig.PLAYER_SKIN.get() ? 5 : 48;
        int posY = 13;

        // Set the color for rendering the player's name
        int color = 16777215;

        // Render the player's name on the HUD with a shadow
        RenderDrawCallback.drawFontWithShadow(graphics, playerName, posX, posY, color);*/

        return minecraft.font.lineHeight;
    } // renderPlayerUsername ()

} // Class
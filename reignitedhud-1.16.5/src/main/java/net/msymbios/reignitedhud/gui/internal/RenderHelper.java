package net.msymbios.reignitedhud.gui.internal;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.horse.HorseEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.msymbios.reignitedhud.config.ReignitedHudID;

public class RenderHelper {

    // -- Methods --

    public static void drawCustomSizedTexture(int x, int y, float u, float v, int width, int height, float textureWidth, float textureHeight) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuilder();
        float f = 1.0F / textureWidth;
        float f1 = 1.0F / textureHeight;

        GlStateManager._enableBlend(); // Enable blending for transparency
        GlStateManager._blendFunc(GlStateManager.SourceFactor.SRC_ALPHA.value, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA.value); // Set blend function

        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.vertex(x, y + height, -1000.0).uv(u * f, (v + (float)height) * f1).endVertex();
        bufferbuilder.vertex(x + width, y + height, -1000.0).uv((u + (float)width) * f, (v + (float)height) * f1).endVertex();
        bufferbuilder.vertex((x + width), (y), -1000.0).uv((u + (float)width) * f, v * f1).endVertex();
        bufferbuilder.vertex((x), (y), -1000.0).uv(u * f, v * f1).endVertex();
        tessellator.end();
    } // drawCustomSizedTexture ()

    public static void drawFont(MatrixStack matrix, String string, int posX, int posY, int color) {
        FontRenderer fontrenderer = Minecraft.getInstance().font;
        fontrenderer.draw(matrix, string, posX, posY, color);
        GlStateManager._blendColor(1.0F, 1.0F, 1.0F, 1.0F);
    } // drawFont ()

    public static void drawFontWithShadow(MatrixStack matrix, String string, int posX, int posY, int maincolor, int shadowcolor) {
        FontRenderer fontrenderer = Minecraft.getInstance().font;
        fontrenderer.drawShadow(matrix, string, (float)(posX + 1), (float)(posY + 1), shadowcolor, false);
        fontrenderer.drawShadow(matrix, string, (float)posX, (float)posY, maincolor, false);
        GlStateManager._blendColor(1.0F, 1.0F, 1.0F, 1.0F);
    } // drawFontWithShadow ()

    public static void drawFontWithShadow(MatrixStack matrix, String string, int posX, int posY, int maincolor) {
        FontRenderer fontrenderer = Minecraft.getInstance().font;
        fontrenderer.drawShadow(matrix, string, (float)posX, (float)posY, maincolor, true);
        GlStateManager._blendColor(1.0F, 1.0F, 1.0F, 1.0F);
    } // drawFontWithShadow ()

    public static void drawFontBold(MatrixStack matrix, String string, int posX, int posY, int maincolor, int shadowcolor) {
        FontRenderer fontrenderer = Minecraft.getInstance().font;
        fontrenderer.drawShadow(matrix, string, (float)(posX + 1), (float)posY, shadowcolor, false);
        fontrenderer.drawShadow(matrix, string, (float)(posX - 1), (float)posY, shadowcolor, false);
        fontrenderer.drawShadow(matrix, string, (float)posX, (float)(posY + 1), shadowcolor, false);
        fontrenderer.drawShadow(matrix, string, (float)posX, (float)(posY - 1), shadowcolor, false);
        fontrenderer.drawShadow(matrix, string, (float)posX, (float)posY, maincolor, false);
        GlStateManager._blendColor(1.0F, 1.0F, 1.0F, 1.0F);
    } // drawFontBold ()

    public static void drawFontWithShadowRightAligned(MatrixStack matrix, String string, int posX, int posY, int maincolor, int shadowcolor) {
        FontRenderer fontrenderer = Minecraft.getInstance().font;
        drawFontWithShadow(matrix, string, posX - fontrenderer.width(string), posY, maincolor, shadowcolor);
    } // drawFontWithShadowRightAligned ()

    public static void drawFontWithShadowCentered(MatrixStack matrix, String string, int posX, int posY, int maincolor, int shadowcolor) {
        FontRenderer fontrenderer = Minecraft.getInstance().font;
        drawFontWithShadow(matrix, string, posX - getStringWidth(string) / 2, posY, maincolor, shadowcolor);
    } // drawFontWithShadowCentered ()

    public static void drawFontBoldCentered(MatrixStack matrix, String string, int posX, int posY, int maincolor, int shadowcolor) {
        FontRenderer fontrenderer = Minecraft.getInstance().font;
        drawFontBold(matrix, string, posX - getStringWidth(string) / 2, posY, maincolor, shadowcolor);
    } // drawFontBoldCentered ()

    public static int getStringWidth(String string) {
        FontRenderer fontrenderer = Minecraft.getInstance().font;
        return fontrenderer.width(string);
    } // getStringWidth ()

    public static void drawIcon(MatrixStack matrix, int posX, int posY, int row, int pos) {
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.gui.blit(matrix, posX, posY, pos * 10 - 10, row * 10 - 10, 10, 10);
    } // drawIcon ()

    public static void drawPlayerIcon(int posX, int posY, int size) {
        drawCustomSizedTexture(posX, posY, (float)size, (float)size, size, size, (float)(size * 8), (float)(size * 8));
        drawCustomSizedTexture(posX, posY, (float)(size * 5), (float)size, size, size, (float)(size * 8), (float)(size * 8));
    } // drawPlayerIcon ()

    public static void drawMediumBar(MatrixStack matrix, int posX, int posY, int bar, float var) {
        Minecraft minecraft = Minecraft.getInstance();
        int barnumber = bar * 10 - 10;
        int barnumberbg = bar * 10 - 4;
        minecraft.gui.blit(matrix, posX, posY, 0, barnumber, 91, 5);
        minecraft.gui.blit(matrix, posX + 1, posY + 1, 1, barnumberbg, (int)(var * 89.0F), 3);
    } // drawMediumBar ()

    public static void drawLongBar(MatrixStack matrix, int posX, int posY, int bar, float var) {
        Minecraft minecraft = Minecraft.getInstance();
        int barnumber = bar * 10 - 10;
        int barnumberbg = bar * 10 - 4;
        minecraft.gui.blit(matrix, posX, posY, 91, barnumber, 121, 5);
        minecraft.gui.blit(matrix, posX + 1, posY + 1, 92, barnumberbg, (int)(var * 119.0F), 3);
    } // drawLongBar ()

    public static void drawDurabBar(MatrixStack matrix, int posX, int posY, int bar, float var) {
        Minecraft minecraft = Minecraft.getInstance();
        int barnumber = bar * 10 - 10;
        int barnumberbg = bar * 10 - 4;
        minecraft.gui.blit(matrix, posX, posY, 212, 76 + barnumber, 37, 5);
        minecraft.gui.blit(matrix, posX + 1, posY + 1, 213, 76 + barnumberbg, (int)(var * 35.0F), 3);
    } // drawDurabBar ()

    public static void addDurabilityDisplay(ItemStack stack, int pos) {
        Minecraft minecraft = Minecraft.getInstance();
        ClientPlayerEntity player = minecraft.player;
        if (stack.getItem() != Items.AIR) {
            int add = 0;
            if (player.getVehicle() instanceof LivingEntity) {
                if (player.getVehicle() instanceof HorseEntity && ((HorseEntity)player.getVehicle()).isTamed()) {
                    add += 33;
                } else {
                    add += 27;
                }
            }

            int count = 0;

            int maxdmg;
            for(maxdmg = 0; maxdmg < player.inventory.getContainerSize(); ++maxdmg) {
                ItemStack invstack = player.inventory.getItem(maxdmg);
                if (invstack.getItem() == stack.getItem() && stack.getMaxDamage() == invstack.getMaxDamage() && stack.getTag() == invstack.getTag()) {
                    count += invstack.getCount();
                }
            }

            maxdmg = stack.getMaxDamage() + 1;
            int dmg = stack.getMaxDamage() - stack.getDamageValue() + 1;
            String display = "" + count;
            float var = 35.0F;
            int color = 16777215;
            int isdmgbl = 2;
            if (stack.isDamageableItem()) {
                display = "" + dmg;
                isdmgbl = 0;
                var = (float)dmg / (float)maxdmg;
                color = 5635925;
                int tex = 1;
                if ((double)dmg < (double)maxdmg * 0.75) {
                    color = 12251978;
                    tex = 2;
                }

                if ((double)dmg < (double)maxdmg * 0.5) {
                    color = 16777045;
                    tex = 3;
                }

                if ((double)dmg < (double)maxdmg * 0.25) {
                    color = 16743765;
                    tex = 4;
                }

                if ((double)dmg < (double)maxdmg * 0.05) {
                    color = 16733525;
                    tex = 5;
                }

                minecraft.textureManager.bind(ReignitedHudID.TEX_HUD_BAR);
                drawDurabBar(new MatrixStack(), 34, 74 + pos + add, tex, var);
            }

            minecraft.textureManager.bind(ReignitedHudID.TEX_HUD_BASE);
            minecraft.gui.blit(new MatrixStack(), 34, 61 + isdmgbl + pos + add, 49, 0, 39, 14);
            minecraft.gui.blit(new MatrixStack(), 15, 60 + pos + add, 29, 0, 20, 20);
            net.minecraft.client.renderer.RenderHelper.setupForFlatItems();
            minecraft.getItemRenderer().renderGuiItem(stack, 17, 62 + pos + add);
            net.minecraft.client.renderer.RenderHelper.setupForFlatItems();
            drawFontWithShadow(new MatrixStack(), display, 38, 64 + isdmgbl + pos + add, color, 0);
        }

    } // addDurabilityDisplay ()

    // =========================================================

    public static void drawEntityStats(LivingEntity entity, String text, float var, double x, double y, double z, float playerYaw, float playerPitch) {
        Minecraft minecraft = Minecraft.getInstance();
        float height1 = 0.0F;
        float height2 = 5.0F;
        float height3 = 6.0F;
        float height4 = 9.0F;
        float width = 121.0F;
        float f = 0.00390625F;
        float u = 91.0F;
        float u2 = u + 1.0F;
        float var1 = var * (width - 2.0F);
        if (!entity.isCrouching()) {
            height1 = 10.0F;
            height2 = 15.0F;
            height3 = 16.0F;
            height4 = 19.0F;
        }

        GlStateManager._pushMatrix();
        GlStateManager._translated((float)x, (float)y, (float)z);
        //GlStateManager.func_187432_a(0.0F, 1.0F, 0.0F);
        GlStateManager._rotatef(-playerYaw, 0.0F, 1.0F, 0.0F);
        GlStateManager._rotatef(playerPitch, 1.0F, 0.0F, 0.0F);
        GlStateManager._scalef(-0.025F, -0.025F, 0.025F);
        GlStateManager._enableDepthTest();
        GlStateManager._enableBlend();
        GlStateManager._blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA.value, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA.value, GlStateManager.SourceFactor.ONE.value, GlStateManager.DestFactor.ZERO.value);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuilder();
        minecraft.textureManager.bind(ReignitedHudID.TEX_HUD_BAR);
        GlStateManager._blendColor(1.0F, 1.0F, 1.0F, 1.0F);
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.vertex(-width / 2.0F, 5.0, 0.02).uv(u * f, height1 * f).endVertex();
        bufferbuilder.vertex(width / 2.0F, 5.0, 0.02).uv((u + 121.0F) * f, height1 * f).endVertex();
        bufferbuilder.vertex(width / 2.0F, 0.0, 0.02).uv((u + 121.0F) * f, height2 * f).endVertex();
        bufferbuilder.vertex(-width / 2.0F, 0.0, 0.02).uv(u * f, height2 * f).endVertex();
        bufferbuilder.vertex(-width / 2.0F + 1.0F, 4.0, 0.01).uv(u2 * f, height3 * f).endVertex();
        bufferbuilder.vertex(-width / 2.0F + 1.0F + var1, 4.0, 0.01).uv((u2 + var1) * f, height3 * f).endVertex();
        bufferbuilder.vertex(-width / 2.0F + 1.0F + var1, 1.0, 0.01).uv((u2 + var1) * f, height4 * f).endVertex();
        bufferbuilder.vertex(-width / 2.0F + 1.0F, 1.0, 0.01).uv(u2 * f, height4 * f).endVertex();
        tessellator.end();

        drawFontWithShadow(new MatrixStack(), text, -(getStringWidth(text) / 2), -5, 16777215);
        GlStateManager._disableDepthTest();
        GlStateManager._disableBlend();
        GlStateManager._blendColor(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager._popMatrix();
    } // drawEntityOnScreen ()

    /*public static void getPlayerModel(int posX, int posY, int scale, float mouseX, float mouseY, Entity entity) {
        GlStateManager._disableDepthTest();
        GlStateManager._pushMatrix();
        GlStateManager._translated((float)posX, (float)posY, 50.0F);
        GlStateManager._scalef((float)(-scale), (float)scale, (float)scale);
        GlStateManager._rotatef(180.0F, 0.0F, 0.0F, 1.0F);
        float f = entity.yRotO;
        float f1 = entity.yRot;
        float f2 = entity.xRot;
        float f3 = entity.getYHeadRot();
        float f4 = entity.yRot;
        GlStateManager._rotatef(135.0F, 0.0F, 1.0F, 0.0F);
        net.minecraft.client.renderer.RenderHelper.setupForFlatItems();
        GlStateManager._rotatef(-135.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager._rotatef(-((float)Math.atan((double)(mouseY / 40.0F))) * 20.0F, 1.0F, 0.0F, 0.0F);
        entity.yRotO = (float)Math.atan((double)(mouseX / 40.0F)) * 20.0F;
        entity.yRot = (float)Math.atan((double)(mouseX / 40.0F)) * 40.0F;
        entity.xRot = -((float)Math.atan((double)(mouseY / 40.0F))) * 20.0F;
        entity.setYBodyRot(entity.yRotO);
        entity.setYHeadRot(entity.yRotO);
        GlStateManager._translatef(0.0F, 0.0F, 0.0F);
        EntityRendererManager rendermanager = Minecraft.getInstance().getEntityRenderDispatcher();
        //rendermanager.func_178631_a(180.0F);
        rendermanager.setRenderShadow(false);
        //rendermanager.func_188391_a(entity, 0.0, 0.0, 0.0, 0.0F, 1.0F, false);
        rendermanager.setRenderShadow(true);
        entity.yRotO = f;
        entity.yRot = f1;
        entity.xRot = f2;
        entity.setYHeadRot(f3);
        entity.setYBodyRot(f4);
        GlStateManager._popMatrix();
        net.minecraft.client.renderer.RenderHelper.setupForFlatItems();
        GlStateManager._enableCull();
        GlStateManager._depthMask(true);
        GlStateManager._enableAlphaTest();
        //GlStateManager.func_179138_g(OpenGlHelper.field_77478_a);
    } // getPlayerModel ()*/

} // Class RenderHelper
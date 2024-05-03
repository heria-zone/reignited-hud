package net.msymbios.reignitedhud.gui;

import com.google.common.collect.Ordering;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.entity.IJumpingMount;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.horse.HorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.msymbios.reignitedhud.config.ReignitedHudID;
import net.msymbios.reignitedhud.gui.internal.RenderHelper;
import org.lwjgl.opengl.GL11;
import net.minecraft.entity.ai.attributes.Attributes;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

public class GuiWidget {

    // -- Variables --
    Minecraft minecraft = Minecraft.getInstance();

    // -- Constructors --

    public GuiWidget() {}

    // -- Methods --

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void renderOverlay(RenderGameOverlayEvent.Pre event) {
        RenderGameOverlayEvent.ElementType type = event.getType();
        if (type == RenderGameOverlayEvent.ElementType.ARMOR || type == RenderGameOverlayEvent.ElementType.AIR || type == RenderGameOverlayEvent.ElementType.BOSSINFO || type == RenderGameOverlayEvent.ElementType.EXPERIENCE || type == RenderGameOverlayEvent.ElementType.FOOD || type == RenderGameOverlayEvent.ElementType.HEALTH || type == RenderGameOverlayEvent.ElementType.HEALTHMOUNT || type == RenderGameOverlayEvent.ElementType.JUMPBAR || type == RenderGameOverlayEvent.ElementType.POTION_ICONS)
            event.setCanceled(true);

        ClientPlayerEntity player = this.minecraft.player;
        MainWindow scaled = minecraft.getWindow();
        if (this.minecraft.player != null && !this.minecraft.player.isSpectator()) {
            if (type != RenderGameOverlayEvent.ElementType.TEXT) return;
            GlStateManager._blendColor(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glPushMatrix();
            this.getWidgetBase(player);
            this.getPlayerHealthBar(player);
            this.getPlayerAirBar(player, scaled);
            this.getMountInfo(player);
            this.getFoodValue(player);
            this.getSatuValue(player);
            this.getArmorValue(player);

            //if (Config.cfgDurabilities) this.getDurabilities(player);
            this.getDurabilities(player);

            this.getEffects(player, scaled);
            //if (Config.cfgTime) this.getClock(player, scaled);
            this.getClock(scaled);

            GL11.glPopMatrix();
        }

    } // renderOverlay ()

    private void getWidgetBase(ClientPlayerEntity player) {
        GameProfile profile = player.getGameProfile();
        ResourceLocation playerskin = DefaultPlayerSkin.getDefaultSkin();
        if (!profile.equals((Object)null)) {
            Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> map = this.minecraft.getSkinManager().getInsecureSkinInformation(profile);
           if (map.containsKey(MinecraftProfileTexture.Type.SKIN)) playerskin = this.minecraft.getSkinManager().registerTexture((MinecraftProfileTexture)map.get(MinecraftProfileTexture.Type.SKIN), MinecraftProfileTexture.Type.SKIN);
           else playerskin = DefaultPlayerSkin.getDefaultSkin(PlayerEntity.createPlayerUUID(profile));
        }

        this.minecraft.textureManager.bind(ReignitedHudID.TEX_HUD_BAR);
        this.minecraft.gui.blit(new MatrixStack(), 13, 13, 227, 0, 5, 25);
        this.minecraft.gui.blit(new MatrixStack(), 14, 14, 223, 1, 3, 23 - (int)(player.experienceLevel  * 23.0F));
        this.minecraft.textureManager.bind(ReignitedHudID.TEX_HUD_BASE);
        this.minecraft.gui.blit(new MatrixStack(), 15, 11, 0, 0, 29, 29);
        RenderHelper.drawFontWithShadow(new MatrixStack(), player.getName().getString(), 48, 13, 16777215);
        this.minecraft.getTextureManager().bind(playerskin);
        RenderHelper.drawPlayerIcon(21, 17, 17);

        String enchantedPoints = String.valueOf(player.experienceLevel);
        RenderHelper.drawFontBoldCentered(new MatrixStack(), enchantedPoints, 30, 35, 13172623, 2957570);

        if(this.minecraft.level != null) {
            if (this.minecraft.level.isClientSide) {
                this.minecraft.textureManager.bind(ReignitedHudID.TEX_HUD_ICON);
                RenderHelper.drawIcon(new MatrixStack(),25, 11, 4, 2);
            }
        }
    } // getWidgetBase ()

    private void getPlayerHealthBar(ClientPlayerEntity player) {
        float var = 1.0F;
        float health = player.getHealth();
        float maxhealth = player.getMaxHealth();

        if (health >= maxhealth) health = maxhealth;
        if (health / maxhealth < 1.0F) var = health / maxhealth;

        int bar = 1;
        if (player.hasEffect(Effects.ABSORPTION)) bar = 2;
        if (player.hasEffect(Effects.REGENERATION)) bar = 3;
        if (player.hasEffect(Effects.DAMAGE_BOOST)) bar = 4;

        this.minecraft.textureManager.bind(ReignitedHudID.TEX_HUD_BAR);
        RenderHelper.drawMediumBar(new MatrixStack(), 48, 24, bar, var);
    } // getPlayerHealthBar ()

    private void getPlayerAirBar(ClientPlayerEntity player, MainWindow scaled) {
        int screenWidth = scaled.getGuiScaledWidth();
        int screenHeight = scaled.getGuiScaledHeight();
        if (player.isUnderWater()) {
            int posX = (screenWidth - 121) / 2;
            int posY = screenHeight - 47;
            float air = (float)player.getAirSupply();
            this.minecraft.textureManager.bind(ReignitedHudID.TEX_HUD_BAR);
            RenderHelper.drawLongBar(new MatrixStack(), posX, posY, 5, air / 300.0F);

            int texX = 40;
            if (player.getAirSupply() > 2 && player.getAirSupply() <= 5) texX = 50;
            if (player.getAirSupply() > 0 && player.getAirSupply() <= 2) texX = 60;

            this.minecraft.textureManager.bind(ReignitedHudID.TEX_HUD_ICON);
            if (player.getAirSupply() < player.getMaxAirSupply())
                this.minecraft.gui.blit(new MatrixStack(), screenWidth / 2 - 4, posY - 2, texX, 0, 10, 10);
        }
    } // getPlayerAirBar ()

    private void getMountInfo(ClientPlayerEntity player) {
        if (player.getVehicle() instanceof LivingEntity) {
            LivingEntity mount = (LivingEntity)player.getVehicle();
            float health = 1.0F;

            if (mount.getHealth() < mount.getMaxHealth())
                health = mount.getHealth() / mount.getMaxHealth();

            this.minecraft.textureManager.bind(ReignitedHudID.TEX_HUD_BASE);
            if (mount instanceof HorseEntity && ((HorseEntity)mount).isTamed()) {
                this.minecraft.gui.blit(new MatrixStack(), 15, 56, 0, 29, 6, 25);
            } else {
                this.minecraft.gui.blit(new MatrixStack(), 15, 56, 6, 29, 6, 19);
            }

            this.minecraft.textureManager.bind(ReignitedHudID.TEX_HUD_ICON);
            RenderHelper.drawIcon(new MatrixStack(), 23, 57, 1, 10);
            this.minecraft.textureManager.bind(ReignitedHudID.TEX_HUD_BAR);
            RenderHelper.drawMediumBar(new MatrixStack(), 23, 68, 1, health);

            if (mount instanceof IJumpingMount && ((HorseEntity)mount).isTamed())
                RenderHelper.drawMediumBar(new MatrixStack(), 23, 74, 5, player.getJumpRidingScale());

            String mountstat = mount.getDisplayName().getString();
            RenderHelper.drawFontWithShadow(new MatrixStack(), mountstat, 35, 58, 16777215);
        }

    } // getMountInfo ()

    private void getFoodValue(ClientPlayerEntity player) {
        String hunger = player.getFoodData().getFoodLevel() + "";
        int icon = 1;
        if (player.hasEffect(Effects.HUNGER)) icon = 2;

        this.minecraft.textureManager.bind(ReignitedHudID.TEX_HUD_ICON);
        RenderHelper.drawIcon(new MatrixStack(), 47, 31, 1, icon);
        int color = 11960912;
        int shadow = 3349772;
        if (player.hasEffect(Effects.HUNGER)) {
            color = 7636056;
            shadow = 1710089;
        }

        RenderHelper.drawFontWithShadow(new MatrixStack(), hunger, 59, 32, color, shadow);
    } // getFoodValue ()

    private void getSatuValue(ClientPlayerEntity player) {
        ItemStack heldmain = player.getMainHandItem();
        ItemStack heldoff = player.getOffhandItem();
        String hunger = player.getFoodData().getFoodLevel() + "";
        String satur = (int)player.getFoodData().getSaturationLevel() + "";
        int posXicon = 59 + RenderHelper.getStringWidth(hunger) + 5;
        int posXtext = posXicon + 8 + 4;
        this.minecraft.textureManager.bind(ReignitedHudID.TEX_HUD_ICON);
        int icon = 3;
        if (player.hasEffect(Effects.HUNGER)) icon = 4;

        RenderHelper.drawIcon(new MatrixStack(), posXicon, 31, 1, icon);
        int color = 14533185;
        int color2 = 15919288;
        int shadow = 3682053;
        RenderHelper.drawFontWithShadow(new MatrixStack(), satur, posXtext, 32, color, shadow);
        /*if (IgniteHUD.hasAppleSkin) {
            String text = "";
            if (heldmain != null && FoodHelper.isFood(heldmain)) {
                text = text + "+" + MathHelper.round2(FoodHelper.getModifiedFoodValues(heldmain, player).getSaturationIncrement());
            }

            if (heldoff != null && !FoodHelper.isFood(heldmain) && FoodHelper.isFood(heldoff)) {
                text = text + "+" + MathHelper.round2(FoodHelper.getModifiedFoodValues(heldoff, player).getSaturationIncrement());
            }

            RenderHelper.drawFontWithShadow(text, posXtext - 6, 42, color2, shadow);
        }*/

    } // getSatuValue ()

    private void getArmorValue(ClientPlayerEntity player) {
        int armor = (int)player.getAttributeValue(Attributes.ARMOR);
        int toughness = (int)player.getAttributeValue(Attributes.ARMOR_TOUGHNESS);
        String hunger = player.getFoodData().getFoodLevel() + "";
        String satur = (int)player.getFoodData().getSaturationLevel() + "";
        int posXicon = 59 + RenderHelper.getStringWidth(hunger) + 5 + 8 + 4 + RenderHelper.getStringWidth(satur) + 5;
        int posXtext = posXicon + 8 + 4;

        if (armor > 0) {
            this.minecraft.textureManager.bind(ReignitedHudID.TEX_HUD_ICON);
            RenderHelper.drawIcon(new MatrixStack(), posXicon, 31, 1, 8);
            RenderHelper.drawFontWithShadow(new MatrixStack(), armor + "", posXtext, 32, 12106180, 1579034);
        }

        if (player.getAttributeValue(Attributes.ARMOR) > 0.0) {
            if (armor > 0) {
                posXicon += 12 + RenderHelper.getStringWidth(toughness + "") + 5;
                posXtext += 12 + RenderHelper.getStringWidth(toughness + "") + 5;
            }

            this.minecraft.textureManager.bind(ReignitedHudID.TEX_HUD_ICON);
            RenderHelper.drawIcon(new MatrixStack(), posXicon, 31, 1, 9);
            RenderHelper.drawFontWithShadow(new MatrixStack(), toughness + "", posXtext, 32, 12106180, 1579034);
        }

    } // getArmorValue ()

    private void getDurabilities(ClientPlayerEntity player) {
        ItemStack head = player.getItemBySlot(EquipmentSlotType.HEAD);
        ItemStack chest = player.getItemBySlot(EquipmentSlotType.CHEST);
        ItemStack legs = player.getItemBySlot(EquipmentSlotType.LEGS);
        ItemStack feet = player.getItemBySlot(EquipmentSlotType.FEET);
        ItemStack mainhand = player.getMainHandItem();
        ItemStack offhand = player.getOffhandItem();
        int pos = 0;

        RenderHelper.addDurabilityDisplay(head, pos);
        if (head.getItem() != Items.AIR) pos += 25;

        RenderHelper.addDurabilityDisplay(chest, pos);
        if (chest.getItem() != Items.AIR) pos += 25;

        RenderHelper.addDurabilityDisplay(legs, pos);
        if (legs.getItem() != Items.AIR) pos += 25;

        RenderHelper.addDurabilityDisplay(feet, pos);
        if (feet.getItem() != Items.AIR) pos += 25;

        RenderHelper.addDurabilityDisplay(mainhand, pos);
        if (mainhand.getItem() != Items.AIR) pos += 25;

        RenderHelper.addDurabilityDisplay(offhand, pos);
    } // getDurabilities ()

    private void getEffects(ClientPlayerEntity player, MainWindow scaled) {
        int screenWidth = scaled.getGuiScaledWidth();
        int screenHeight = scaled.getGuiScaledHeight();
        Collection<EffectInstance> collection = player.getActiveEffects();
        if (!collection.isEmpty()) {
            GlStateManager._disableLighting();
            int i = 0;
            int j = 0;
            Iterator var8 = Ordering.natural().reverse().sortedCopy(collection).iterator();

            while(var8.hasNext()) {
                EffectInstance potioneffect = (EffectInstance)var8.next();
                Effect potion = potioneffect.getEffect();
                if (potioneffect.showIcon()) {
                    int posX = screenWidth;
                    int posY = screenHeight - 26;
                    String duration = String.format("%d:%02d", potioneffect.getDuration() / 1200, (potioneffect.getDuration() % 1200) / 20);
                    int icon = 195;
                    if (potion.getEffect() == Effects.MOVEMENT_SPEED) icon = 0;
                    if (potion.getEffect() == Effects.MOVEMENT_SLOWDOWN) icon = 1;
                    if (potion.getEffect() == Effects.DIG_SPEED) icon = 2;
                    if (potion.getEffect() == Effects.DIG_SLOWDOWN) icon = 3;
                    if (potion.getEffect() == Effects.DAMAGE_BOOST) icon = 4;
                    if (potion.getEffect() == Effects.WEAKNESS) icon = 5;
                    if (potion.getEffect() == Effects.POISON) icon = 6;
                    if (potion.getEffect() == Effects.REGENERATION) icon = 7;
                    if (potion.getEffect() == Effects.INVISIBILITY) icon = 8;
                    if (potion.getEffect() == Effects.HUNGER) icon = 9;
                    if (potion.getEffect() == Effects.JUMP) icon = 10;
                    if (potion.getEffect() == Effects.CONFUSION) icon = 11;
                    if (potion.getEffect() == Effects.NIGHT_VISION) icon = 12;
                    if (potion.getEffect() == Effects.BLINDNESS) icon = 13;
                    if (potion.getEffect() == Effects.DAMAGE_RESISTANCE) icon = 14;
                    if (potion.getEffect() == Effects.FIRE_RESISTANCE) icon = 15;
                    if (potion.getEffect() == Effects.WATER_BREATHING) icon = 16;
                    if (potion.getEffect() == Effects.WITHER) icon = 17;
                    if (potion.getEffect() == Effects.ABSORPTION) icon = 18;
                    if (potion.getEffect() == Effects.LEVITATION) icon = 19;
                    if (potion.getEffect() == Effects.GLOWING) icon = 20;
                    if (potion.getEffect() == Effects.LUCK) icon = 21;
                    if (potion.getEffect() == Effects.UNLUCK) icon = 22;
                    if (potion.getEffect() == Effects.HEALTH_BOOST) icon = 23;

                    if (potion.isBeneficial()) {
                        ++i;
                        posX -= 33 * i;
                        posY -= 24;
                    } else {
                        ++j;
                        posX -= 33 * j;
                    }

                    float f = 1.0F;
                    if (potioneffect.getDuration() <= 200) {
                        int j1 = 10 - potioneffect.getDuration() / 20;
                        f = net.minecraft.util.math.MathHelper.clamp((float)potioneffect.getDuration() / 10.0F / 5.0F * 0.5F, 0.0F, 0.5F) +
                                net.minecraft.util.math.MathHelper.sin((float)potioneffect.getDuration() * 3.1415927F / 5.0F) *
                                        net.minecraft.util.math.MathHelper.clamp((float)j1 / 10.0F * 0.25F, 0.0F, 0.25F);
                    }

                    GlStateManager._blendColor(1.0F, 1.0F, 1.0F, 1.0F);
                    this.minecraft.textureManager.bind(ReignitedHudID.TEX_HUD_BASE);
                    this.minecraft.gui.blit(new MatrixStack(), posX, posY, 88, 0, 29, 21);
                    GlStateManager._blendColor(1.0F, 1.0F, 1.0F, f);
                    this.minecraft.textureManager.bind(ReignitedHudID.TEX_HUD_EFFECT);
                    this.minecraft.gui.blit(new MatrixStack(), posX + 6, posY - 3, icon % 14 * 18, icon / 14 * 18, 18, 18);
                    RenderHelper.drawFontBoldCentered(new MatrixStack(), duration, posX + 15, posY + 10, potion.getColor(), 0);
                }
            }
        }

    } // getPotionEffects ()

    private void getClock(MainWindow scaled) {
        int screenWidth = scaled.getGuiScaledWidth();
        int screenHeight = scaled.getGuiScaledHeight();
        int row = 1;
        int icon = 11;
        int color = 16768359;
        long time = 0L;

        time = this.minecraft.level.getDayTime();
        int hour = (int)((time / 1000L + 6L) % 24L);
        int minute = (int)(60L * (time % 1000L) / 1000L);
        int day = (int)(time % 24000L);
        if (day > 12542 && day < 23450) {
            icon = 12;
            color = 7765652;
        }

        String hourdisplay = "" + hour;
        if (hour < 10) hourdisplay = "0" + hour;

        String minutedisplay = "" + minute;
        if (minute < 10) minutedisplay = "0" + minute;

        this.minecraft.textureManager.bind(ReignitedHudID.TEX_HUD_BASE);
        this.minecraft.gui.blit(new MatrixStack(),screenWidth / 2 - 22, screenHeight - 36, 165, 0, 44, 14);
        this.minecraft.textureManager.bind(ReignitedHudID.TEX_HUD_ICON);
        RenderHelper.drawIcon(new MatrixStack(),screenWidth / 2 - 19, screenHeight - 33, row, icon);
        RenderHelper.drawFontWithShadowCentered(new MatrixStack(),hourdisplay + ":" + minutedisplay, screenWidth / 2 + 5, screenHeight - 32, color, 0);
    } // getClock ()

} // Class GuiWidget
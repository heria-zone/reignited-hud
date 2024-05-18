package net.msymbios.reignitedhud.gui;

import com.google.common.collect.Ordering;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PlayerRideableJumping;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.msymbios.reignitedhud.config.ReignitedHudConfig;
import net.msymbios.reignitedhud.config.ReignitedHudID;
import net.msymbios.reignitedhud.gui.internal.RenderDrawCallback;
import net.msymbios.reignitedhud.util.MathHelper;

import java.util.Collection;
import java.util.Map;

public class GuiWidget {

    // -- Variables --
    Minecraft minecraft = Minecraft.getInstance();
    public static GuiWidget INSTANCE;

    // -- Constructors --

    public GuiWidget() {}

    // -- Methods --

    public static void register() {
        INSTANCE = new GuiWidget();
        HudRenderCallback.EVENT.register(INSTANCE::renderOverlay);
    } // register ()

    /**
     * Renders the game overlay based on the event type.
     *
     */
    public void renderOverlay(PoseStack poseStack, float v) {
        // Get the player and the game window
        LocalPlayer player = this.minecraft.player;
        Window scaled = minecraft.getWindow();

        // Check if the player is not a null
        if (player == null) return;

        // Check if the player is not a spectator
        if (this.minecraft.player != null) {
            GlStateManager._clearColor(1.0F, 1.0F, 1.0F, 1.0F);

            // Render different parts of the HUD
            if (ReignitedHudConfig.PLAYER_SKIN) this.getWidgetBase(player);
            if (ReignitedHudConfig.PLAYER_USERNAME) this.getPlayerName(player);
            if (ReignitedHudConfig.PLAYER_HEALTH) this.getPlayerHealthBar(player);
            if (ReignitedHudConfig.PLAYER_AIR_SUPPLY) this.getPlayerAirBar(player, scaled);

            this.getFoodAndArmor(player);
            this.getItems(player);

            if (ReignitedHudConfig.MOUNT) this.getMountInfo(player);
            if (ReignitedHudConfig.EFFECT) this.getEffects(player, scaled);
            if (ReignitedHudConfig.CLOCK_TIME) this.getClock(scaled);
        }

    } // renderOverlay ()

    /**
     * Updates the widget base for the player.
     *
     * @param player The client player entity
     */
    private void getWidgetBase(LocalPlayer player) {
        // Create a new matrix stack
        PoseStack pose = new PoseStack();

        // Get the player's game profile
        GameProfile profile = player.getGameProfile();

        // Initialize the player's skin with the default skin
        ResourceLocation playerSkin = DefaultPlayerSkin.getDefaultSkin();

        // Check if the player's profile is not null
        if (!profile.equals((Object)null)) {
            // Get the player's skin information
            Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> map = this.minecraft.getSkinManager().getInsecureSkinInformation(profile);

            // Check if the skin map contains the player's skin
            if (map.containsKey(MinecraftProfileTexture.Type.SKIN)) {
                // Update the player's skin with the retrieved skin
                playerSkin = this.minecraft.getSkinManager().registerTexture(map.get(MinecraftProfileTexture.Type.SKIN), MinecraftProfileTexture.Type.SKIN);
            } else {
                // Use the default skin if the player's skin is not found
                playerSkin = DefaultPlayerSkin.getDefaultSkin(Player.createPlayerUUID(profile));
            }
        }

        // Render HUD elements

        // Render HUD bar
        this.minecraft.getTextureManager().bind(ReignitedHudID.TEX_HUD_BAR);
        minecraft.gui.blit(pose, 13, 13, 227, 0, 5, 25);

        // Render dynamic HUD element based on player's experience progress
        minecraft.gui.blit(pose, 14, 14, 223, 1, 3, 23 - (int)(player.experienceProgress  * 23.0F));

        // Render HUD base
        this.minecraft.getTextureManager().bind(ReignitedHudID.TEX_HUD_BASE);
        minecraft.gui.blit(pose, 15, 11, 0, 0, 29, 29);

        // Bind player's skin texture and render player icon on HUD
        this.minecraft.getTextureManager().bind(playerSkin);
        RenderDrawCallback.drawPlayerIcon(21, 17, 17);

        // Display the player's experience level
        String enchantedPoints = String.valueOf(player.experienceLevel);
        RenderDrawCallback.drawFontBoldCentered(enchantedPoints, 30, 35, 13172623, 2957570);

        // Check if the game level exists and is set to hard difficulty
        if(this.minecraft.level != null) {
            if (this.minecraft.level.getDifficulty() == Difficulty.HARD) {
                // Display a specific icon for hard difficulty
                this.minecraft.getTextureManager().bind(ReignitedHudID.TEX_HUD_ICON);
                RenderDrawCallback.drawIcon(25, 11, 4, 2);
            }
        }
    } // getWidgetBase ()

    /**
     * Renders the player's name on the Heads-Up Display (HUD).
     *
     * @param player The player entity whose name will be rendered
     */
    public void getPlayerName(LocalPlayer player) {
        // Get the player's name as a string
        String playerName = player.getName().getString();

        // Set the position for rendering the player's name
        int posX = !ReignitedHudConfig.PLAYER_SKIN ? 5 : 48;
        int posY = 13;

        // Set the color for rendering the player's name
        int color = 16777215;

        // Render the player's name on the HUD with a shadow
        RenderDrawCallback.drawFontWithShadow(playerName, posX, posY, color);
    } // getPlayerName ()

    /**
     * Updates and renders the player's health bar based on their current health and effects.
     *
     * @param player The client player entity to retrieve health and effects from
     */
    private void getPlayerHealthBar(LocalPlayer player) {
        // Calculate the fill amount of current health to max health
        float fill = Math.min(1.0F, player.getHealth() / player.getMaxHealth());

        // Set the position for rendering
        int posX = !ReignitedHudConfig.PLAYER_SKIN ? 5 : 48;
        int posY = 24;

        // Determine the type of health bar based on player effects
        int bar = 1;
        if (player.hasEffect(MobEffects.ABSORPTION)) bar = 2;
        if (player.hasEffect(MobEffects.HUNGER)) bar = 3;
        if (player.hasEffect(MobEffects.WITHER)) bar = 4;

        // Bind the health bar texture and render the bar
        this.minecraft.getTextureManager().bind(ReignitedHudID.TEX_HUD_BAR);
        RenderDrawCallback.drawMediumBar(posX, posY, bar, fill);
    } // getPlayerHealthBar ()

    /**
     * Updates the player's air bar on the HUD if the player is underwater.
     *
     * @param player The player entity
     * @param scaled The Window object containing the scaled GUI dimensions
     */
    private void getPlayerAirBar(LocalPlayer player, Window scaled) {
        // Get the screen width and height from the scaled GUI dimensions
        int screenWidth = scaled.getGuiScaledWidth();
        int screenHeight = scaled.getGuiScaledHeight();

        // Check if the player is underwater
        if (player.isEyeInFluid(FluidTags.WATER) || Math.min(player.getAirSupply(), player.getMaxAirSupply()) < player.getMaxAirSupply()) {
            this.minecraft.getProfiler().popPush("air");

            // Calculate the position of the air bar on the screen
            int posX = (screenWidth - 121) / 2;
            int posY = screenHeight - 47;

            // Get the player's current air supply
            float air = (float)player.getAirSupply();

            // Bind the air bar texture and render the air bar based on the player's air supply
            this.minecraft.getTextureManager().bind(ReignitedHudID.TEX_HUD_BAR);
            RenderDrawCallback.drawLongBar(posX, posY, 5, air / 300.0F);

            // Determine the texture X position based on the player's air supply level
            int texX = 40;
            if (player.getAirSupply() > 2 && player.getAirSupply() <= 5) texX = 50;
            if (player.getAirSupply() > 0 && player.getAirSupply() <= 2) texX = 60;

            // Bind the air bar icon texture and render the icon if the player's air supply is not full
            this.minecraft.getTextureManager().bind(ReignitedHudID.TEX_HUD_ICON);
            minecraft.gui.blit(new PoseStack(), screenWidth / 2 - 4, posY - 2, texX, 0, 10, 10);

            this.minecraft.getProfiler().pop();
        }

    } // getPlayerAirBar ()

    /**
     * Gets the mount information for the player and displays it on the HUD.
     * If the player is riding a living entity, it shows the mount's health, type, and name.
     * If the mount is a tamed HorseEntity, it displays additional information.
     *
     * @param player The client player entity
     */
    private void getMountInfo(LocalPlayer player) {
        if (player.getVehicle() instanceof LivingEntity) {
            // Create a new matrix stack
            PoseStack pose = new PoseStack();

            LivingEntity mount = (LivingEntity)player.getVehicle();
            float health = 1.0F;

            // Calculate the health percentage of the mount
            if (mount.getHealth() < mount.getMaxHealth())
                health = mount.getHealth() / mount.getMaxHealth();

            // Display the base HUD texture
            this.minecraft.getTextureManager().bind(ReignitedHudID.TEX_HUD_BASE);

            // Check if the mount is a tamed HorseEntity to display the appropriate icon
            if (mount instanceof Horse && ((Horse)mount).isTamed()) {
                minecraft.gui.blit(pose, 15, 56, 0, 29, 6, 25);
            } else {
                minecraft.gui.blit(pose, 15, 56, 6, 29, 6, 19);
            }

            // Display the icon texture
            this.minecraft.getTextureManager().bind(ReignitedHudID.TEX_HUD_ICON);
            RenderDrawCallback.drawIcon(23, 57, 1, 10);

            // Display the health bar texture
            this.minecraft.getTextureManager().bind(ReignitedHudID.TEX_HUD_BAR);
            RenderDrawCallback.drawMediumBar(23, 68, 1, health);

            // Check if the mount implements IJumpingMount and is tamed to display the jump bar
            if (mount instanceof PlayerRideableJumping && ((Horse)mount).isTamed())
                RenderDrawCallback.drawMediumBar(23, 74, 5, player.getJumpRidingScale());

            // Display the mount's name on the HUD
            String mountStat = mount.getDisplayName().getString();
            RenderDrawCallback.drawFontWithShadow(mountStat, 35, 58, 16777215);
        }

    } // getMountInfo ()

    private void getFoodAndArmor(LocalPlayer player) {
        // GENERAL
        int color, shadow;

        String text;
        int icon;

        int iconPosX = !ReignitedHudConfig.PLAYER_SKIN ? 4 : 47;
        int iconPosY = 31;

        int textPosX = !ReignitedHudConfig.PLAYER_SKIN ? 16 : 59;
        int textPosY = 32;

        // FOOD
        if (ReignitedHudConfig.FOOD_LEVEL) {
            text = String.valueOf(player.getFoodData().getFoodLevel());     // Get the player's hunger value as a string
            icon = player.hasEffect(MobEffects.HUNGER) ? 2 : 1;             // Determine the icon to display based on player's hunger effect

            // Set & Adjust default color and shadow for the food value display if player has hunger effect or not
            color = player.hasEffect(MobEffects.HUNGER) ? 7636056 : 11960912;
            shadow = player.hasEffect(MobEffects.HUNGER) ? 1710089 : 3349772;
            this.minecraft.getTextureManager().bind(ReignitedHudID.TEX_HUD_ICON);

            // Draw the food icon on the HUD
            RenderDrawCallback.drawIcon(iconPosX, iconPosY, 1, icon);

            // Draw the food value on the HUD with appropriate color and shadow
            RenderDrawCallback.drawFontWithShadow(text, textPosX, textPosY, color, shadow);

            int spacing = RenderDrawCallback.getStringWidth(text) + 5 + 8 + 4;
            iconPosX += spacing;
            textPosX += spacing;
        }

        // SATURATION
        if (ReignitedHudConfig.FOOD_SATURATION) {
            text = String.valueOf((int)player.getFoodData().getSaturationLevel());
            icon = player.hasEffect(MobEffects.HUNGER) ? 3 : 4;

            // Set colors for text and shadow
            color = 14533185;
            shadow = 3682053;
            this.minecraft.getTextureManager().bind(ReignitedHudID.TEX_HUD_ICON);

            // Draw hunger icon
            RenderDrawCallback.drawIcon(iconPosX, iconPosY, 1, icon);

            // Draw saturation level with shadow
            RenderDrawCallback.drawFontWithShadow(text, textPosX, textPosY, color, shadow);

            int spacing = RenderDrawCallback.getStringWidth(text) + 5 + 8 + 4;
            iconPosX += spacing;
            textPosX += spacing;
        }

        // ARMOR
        int armor = (int)player.getAttributeValue(Attributes.ARMOR);
        if (ReignitedHudConfig.ARMOR_LEVEL && armor > 0) {
            // Get the player's armor value
            text = String.valueOf(armor);
            icon = 8;

            // Set colors for text and shadow
            color = 12106180;
            shadow = 1579034;
            this.minecraft.getTextureManager().bind(ReignitedHudID.TEX_HUD_ICON);

            RenderDrawCallback.drawIcon(iconPosX, iconPosY, 1, icon);
            RenderDrawCallback.drawFontWithShadow(text, textPosX, textPosY, color, shadow);

            int spacing = RenderDrawCallback.getStringWidth(text) + 5 + 8 + 4;
            iconPosX += spacing;
            textPosX += spacing;
        }

        // TOUGHNESS
        int toughness = (int)player.getAttributeValue(Attributes.ARMOR_TOUGHNESS);
        if (ReignitedHudConfig.ARMOR_TOUGHNESS && toughness > 0.0) {
            // Get the player's armor toughness value
            text = String.valueOf(toughness);
            icon = 9;

            // Set colors for text and shadow
            color = 12106180;
            shadow = 1579034;
            this.minecraft.getTextureManager().bind(ReignitedHudID.TEX_HUD_ICON);

            RenderDrawCallback.drawIcon(iconPosX, iconPosY, 1, icon);
            RenderDrawCallback.drawFontWithShadow(text, textPosX, textPosY, color, shadow);
        }
    } // getFoodAndArmor ()


    /**
     * Updates the HUD to display the durability of the player's equipped items.
     * This includes the durability bars for the head, chest, legs, feet, main hand, and offhand items.
     *
     * @param player The client player entity to retrieve equipped items from
     */
    private void getItems(LocalPlayer player) {
        // Initialize position for rendering
        int pos = 0;

        if (ReignitedHudConfig.ITEM_EQUIPMENT) {
            // Retrieve equipped items
            ItemStack head = player.getItemBySlot(EquipmentSlot.HEAD);
            ItemStack chest = player.getItemBySlot(EquipmentSlot.CHEST);
            ItemStack legs = player.getItemBySlot(EquipmentSlot.LEGS);
            ItemStack feet = player.getItemBySlot(EquipmentSlot.FEET);

            // Display durability for the head item
            RenderDrawCallback.addDurabilityDisplay(head, pos);
            if (head.getItem() != Items.AIR) pos += 25;

            // Display durability for the chest item
            RenderDrawCallback.addDurabilityDisplay(chest, pos);
            if (chest.getItem() != Items.AIR) pos += 25;

            // Display durability for the legs item
            RenderDrawCallback.addDurabilityDisplay(legs, pos);
            if (legs.getItem() != Items.AIR) pos += 25;

            // Display durability for the feet item
            RenderDrawCallback.addDurabilityDisplay(feet, pos);
            if (feet.getItem() != Items.AIR) pos += 25;
        }

        if (ReignitedHudConfig.ITEM_HAND) {
            ItemStack mainHandItem = player.getMainHandItem();
            ItemStack offhandItem = player.getOffhandItem();

            // Display durability for the main hand item
            RenderDrawCallback.addDurabilityDisplay(mainHandItem, pos);
            if (mainHandItem.getItem() != Items.AIR) pos += 25;

            // Display durability for the offhand item
            RenderDrawCallback.addDurabilityDisplay(offhandItem, pos);
        }
    } // getItems ()

    /**
     * Retrieves and renders the active effects of the player on the screen.
     *
     * @param player The client player entity to retrieve active effects from
     * @param scaled The scaled window for GUI rendering
     */
    private void getEffects(LocalPlayer player, Window scaled) {
        // Get the screen width and height
        int screenWidth = scaled.getGuiScaledWidth();
        int screenHeight = scaled.getGuiScaledHeight();

        // Retrieve the active effects of the player
        Collection<MobEffectInstance> collection = player.getActiveEffects();

        // Check if there are any active effects to render
        if (!collection.isEmpty()) {
            // Initialize counters for beneficial and harmful effects
            int beneficialEffectsCount = 0;
            int harmfulEffectsCount = 0;

            // Iterate through each active effect
            for (MobEffectInstance potionEffect : Ordering.natural().reverse().sortedCopy(collection)) {
                MobEffect potion = potionEffect.getEffect();

                // Check if the effect has an icon to display
                if (potionEffect.showIcon()) {
                    int posX = screenWidth;
                    int posY = screenHeight - 26;

                    // Format the duration of the effect for display
                    String duration = String.format("%d:%02d", potionEffect.getDuration() / 1200, (potionEffect.getDuration() % 1200) / 20);

                    // Determine the icon index based on the effect type
                    int icon = getEffectIconIndex(potion);

                    // Adjust position based on beneficial or harmful effect
                    if (potion.isBeneficial()) {
                        beneficialEffectsCount++;
                        posX -= 33 * beneficialEffectsCount;
                        posY -= 24;
                    } else {
                        harmfulEffectsCount++;
                        posX -= 33 * harmfulEffectsCount;
                    }

                    // Calculate the transparency of the effect based on duration
                    float transparency = calculateEffectTransparency(potionEffect);

                    // Render the effect icon and duration
                    GlStateManager._clearColor(1.0F, 1.0F, 1.0F, 1.0F);
                    this.minecraft.getTextureManager().bind(ReignitedHudID.TEX_HUD_BASE);
                    minecraft.gui.blit(new PoseStack(), posX, posY, 88, 0, 29, 21);
                    GlStateManager._clearColor(1.0F, 1.0F, 1.0F, transparency);
                    this.minecraft.getTextureManager().bind(ReignitedHudID.TEX_HUD_EFFECT);
                    minecraft.gui.blit(new PoseStack(), posX + 6, posY - 3, icon % 14 * 18, icon / 14 * 18, 18, 18);
                    RenderDrawCallback.drawFontBoldCentered(duration, posX + 15, posY + 10, potion.getColor(), 0);
                }
            }
        }

    } // getPotionEffects ()

    /**
     * Updates the clock display on the HUD based on the in-game time.
     *
     * @param scaled The scaled window to get dimensions from
     */
    private void getClock(Window scaled) {
        // Get the screen width and height from the scaled window
        int screenWidth = scaled.getGuiScaledWidth();
        int screenHeight = scaled.getGuiScaledHeight();

        // Initialize variables for clock display
        int row = 1;
        int icon = 11;
        int color = 16768359;
        long time = 0L;

        // Get the current in-game time
        time = this.minecraft.level.getDayTime();
        int hour = (int)((time / 1000L + 6L) % 24L);
        int minute = (int)(60L * (time % 1000L) / 1000L);
        int day = (int)(time % 24000L);

        // Adjust icon and color based on in-game day time
        if (day > 12542 && day < 23450) {
            icon = 12;
            color = 7765652;
        }

        // Format hour and minute display with leading zeros if needed
        String hourdisplay = "" + hour;
        if (hour < 10) hourdisplay = "0" + hour;

        String minutedisplay = "" + minute;
        if (minute < 10) minutedisplay = "0" + minute;

        // Bind textures and draw clock elements on the HUD
        this.minecraft.getTextureManager().bind(ReignitedHudID.TEX_HUD_BASE);
        minecraft.gui.blit(new PoseStack(), screenWidth / 2 - 22, screenHeight - 36, 165, 0, 44, 14);

        this.minecraft.getTextureManager().bind(ReignitedHudID.TEX_HUD_ICON);
        RenderDrawCallback.drawIcon(screenWidth / 2 - 19, screenHeight - 33, row, icon);
        RenderDrawCallback.drawFontWithShadowCentered(hourdisplay + ":" + minutedisplay, screenWidth / 2 + 5, screenHeight - 32, color, 0);
    } // getClock ()

    // UTILITY
    /**
     * Determines the icon index based on the given effect type.
     *
     * @param effect The type of effect to get the icon index for
     * @return The icon index corresponding to the effect type
     */
    private int getEffectIconIndex(MobEffect effect) {
        if (effect == MobEffects.MOVEMENT_SPEED) return 0;
        else if (effect == MobEffects.MOVEMENT_SLOWDOWN) return 1;
        else if (effect == MobEffects.DIG_SPEED) return 2;
        else if (effect == MobEffects.DIG_SLOWDOWN) return 3;
        else if (effect == MobEffects.DAMAGE_BOOST) return 4;
        else if (effect == MobEffects.WEAKNESS) return 5;
        else if (effect == MobEffects.POISON) return 6;
        else if (effect == MobEffects.REGENERATION) return 7;
        else if (effect == MobEffects.INVISIBILITY) return 8;
        else if (effect == MobEffects.HUNGER) return 9;
        else if (effect == MobEffects.JUMP) return 10;
        else if (effect == MobEffects.CONFUSION) return 11;
        else if (effect == MobEffects.NIGHT_VISION) return 12;
        else if (effect == MobEffects.BLINDNESS) return 13;
        else if (effect == MobEffects.DAMAGE_RESISTANCE) return 14;
        else if (effect == MobEffects.FIRE_RESISTANCE) return 15;
        else if (effect == MobEffects.WATER_BREATHING) return 16;
        else if (effect == MobEffects.WITHER) return 17;
        else if (effect == MobEffects.ABSORPTION) return 18;
        else if (effect == MobEffects.LEVITATION) return 19;
        else if (effect == MobEffects.GLOWING) return 20;
        else if (effect == MobEffects.LUCK) return 21;
        else if (effect == MobEffects.UNLUCK) return 22;
        else if (effect == MobEffects.HEALTH_BOOST) return 23;
        else return 195; // Default icon index
    } // getEffectIconIndex ()

    /**
     * Calculates the transparency of the effect based on its duration.
     *
     * @param potionEffect The effect instance to calculate transparency for
     * @return The transparency value for rendering the effect
     */
    private float calculateEffectTransparency(MobEffectInstance potionEffect) {
        // No change in transparency if duration is over 200
        if (potionEffect.getDuration() > 200) return 1.0F;

        float durationRatio = (float) potionEffect.getDuration() / 10.0F / 5.0F;
        float sinValue = (float) Math.sin(potionEffect.getDuration() * Math.PI / 5.0F);
        float clampValue = MathHelper.clamp((10 - (float) potionEffect.getDuration() / 20) / 10.0F * 0.25F, 0.0F, 0.25F);

        return durationRatio * 0.5F + sinValue * clampValue;
    } // calculateEffectTransparency ()

} // Class GuiWidget
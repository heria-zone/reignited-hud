package net.msymbios.reignitedhud.gui;

import com.google.common.collect.Ordering;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.DefaultPlayerSkin;

import net.minecraft.resources.ResourceLocation;
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
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.client.gui.IIngameOverlay;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.msymbios.reignitedhud.config.ReignitedHudConfig;
import net.msymbios.reignitedhud.config.ReignitedHudID;
import net.msymbios.reignitedhud.gui.internal.RenderDrawCallback;
import net.msymbios.reignitedhud.util.MathHelper;
import org.lwjgl.opengl.GL11;

import java.util.Collection;
import java.util.Map;

import static net.minecraft.client.gui.GuiComponent.blit;

public class GuiWidget {

    // -- Variables --
    Minecraft minecraft = Minecraft.getInstance();

    // -- Constructors --

    public GuiWidget() {}

    // -- Methods --

    /*@SubscribeEvent(priority = EventPriority.HIGHEST)
    public void renderEntityStats(RenderLivingEvent.Pre<LivingEntity, EntityModel<LivingEntity>> event) {
        ClientPlayerEntity player = this.minecraft.player;
        EntityRendererManager render = event.getRenderer().getDispatcher();
        FontRenderer font = render.getFont();
        LivingEntity entityBase = event.getEntity();
        double distance = (double)entityBase.distanceTo(render.camera.getEntity());
        if (/*ReignitedHudConfig.cfgTarget && entityBase instanceof LivingEntity && !(entityBase instanceof ClientPlayerEntity) && !entityBase.isAlive() && /*distance < 20.0 && entityBase != player.getVehicle()) {
            LivingEntity entity = (LivingEntity)entityBase;
            float curHealth = entity.getHealth();
            float maxHealth = entity.getMaxHealth();
            float health = 1.0F;
            if (curHealth / maxHealth < 1.0F) {
                health = curHealth / maxHealth;
            }

            String entityStat = entity.getDisplayName().getString();
            Entity viewingEntity = render.camera.getEntity();
            float playerYaw = render.camera.getYRot();
            float playerPitch = render.camera.getXRot();

            if (this.minecraft.player.isCrouching())
                playerPitch = -playerPitch;

            double entityX = entity.position().x * (double)event.getPartialRenderTick();
            double entityY = entity.position().y * (double)event.getPartialRenderTick();
            double entityZ = entity.position().z * (double)event.getPartialRenderTick();
            double viewingX = viewingEntity.position().x * (double)event.getPartialRenderTick();
            double viewingY = viewingEntity.position().y * (double)event.getPartialRenderTick();
            double viewingZ = viewingEntity.position().z * (double)event.getPartialRenderTick();
            double x = entityX - viewingX;
            double y = entityY - viewingY + (double)entity.getEyeHeight() + 0.4;
            if (entity instanceof WitchEntity) y += 0.4;

            double z = entityZ - viewingZ;
            if (!entity.isInvisible()) {
                RenderDrawCallback.drawEntityStats(entity, entityStat, health, x, y, z, playerYaw, playerPitch);
            }
        }

    } // renderEntityStats ()*/

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void removeRenderOverlay(RenderGameOverlayEvent.PreLayer event) {
        // Get the overlay of the overlay element
        IIngameOverlay overlay = event.getOverlay();

        // Cancel rendering for specific overlay types
        if (overlay == ForgeIngameGui.ARMOR_LEVEL_ELEMENT || overlay == ForgeIngameGui.AIR_LEVEL_ELEMENT ||
                overlay == ForgeIngameGui.EXPERIENCE_BAR_ELEMENT || overlay == ForgeIngameGui.POTION_ICONS_ELEMENT ||
                overlay == ForgeIngameGui.FOOD_LEVEL_ELEMENT || overlay == ForgeIngameGui.PLAYER_HEALTH_ELEMENT ||
                overlay == ForgeIngameGui.MOUNT_HEALTH_ELEMENT || overlay == ForgeIngameGui.JUMP_BAR_ELEMENT) {
            event.setCanceled(true);
        }
    } // removeRenderOverlay ()

    /**
     * Renders the game overlay based on the event type.
     *
     * @param event The RenderGameOverlayEvent.Pre event
     */
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void renderOverlay(RenderGameOverlayEvent.Pre event) {
        // Get the player and the game window
        LocalPlayer player = this.minecraft.player;
        Window scaled = minecraft.getWindow();

        // Check if the player is not a null
        if (player == null) return;

        // Check if the player is not a spectator
        if (this.minecraft.player != null) {
            // Render specific elements for TEXT overlay
            if (event.getType() != RenderGameOverlayEvent.ElementType.TEXT) return;

            GlStateManager._clearColor(1.0F, 1.0F, 1.0F, 1.0F);

            // Render different parts of the HUD
            this.getWidgetBase(player);
            this.getPlayerHealthBar(player);
            this.getPlayerAirBar(player, scaled);
            this.getFoodValue(player);
            this.getSatuValue(player);
            this.getArmorValue(player);

            // Conditionally render additional HUD elements based on configuration
            if (ReignitedHudConfig.DURABILITY.get()) this.getDurability(player);
            if (ReignitedHudConfig.MOUNT.get()) this.getMountInfo(player);
            if (ReignitedHudConfig.EFFECT.get()) this.getEffects(player, scaled);
            if (ReignitedHudConfig.TIME.get()) this.getClock(scaled);
        }

    } // renderOverlay ()

    /**
     * Updates the widget base for the player.
     *
     * @param player The client player entity
     */
    private void getWidgetBase(LocalPlayer player) {
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
        RenderSystem.setShaderTexture(0, ReignitedHudID.TEX_HUD_BAR);
        this.minecraft.gui.blit(new PoseStack(), 13, 13, 227, 0, 5, 25);

        // Render dynamic HUD element based on player's experience progress
        this.minecraft.gui.blit(new PoseStack(), 14, 14, 223, 1, 3, 23 - (int)(player.experienceProgress  * 23.0F));

        // Render HUD base
        RenderSystem.setShaderTexture(0, ReignitedHudID.TEX_HUD_BASE);
        this.minecraft.gui.blit(new PoseStack(), 15, 11, 0, 0, 29, 29);

        // Render player's name on the HUD
        RenderDrawCallback.drawFontWithShadow(player.getName().getString(), 48, 13, 16777215);

        // Bind player's skin texture and render player icon on HUD
        RenderSystem.setShaderTexture(0, playerSkin);
        RenderDrawCallback.drawPlayerIcon(21, 17, 17);

        // Display the player's experience level
        String enchantedPoints = String.valueOf(player.experienceLevel);
        RenderDrawCallback.drawFontBoldCentered(enchantedPoints, 30, 35, 13172623, 2957570);

        // Check if the game level exists and is set to hard difficulty
        if(this.minecraft.level != null) {
            if (this.minecraft.level.getDifficulty() == Difficulty.HARD) {
                // Display a specific icon for hard difficulty
                RenderSystem.setShaderTexture(0, ReignitedHudID.TEX_HUD_ICON);
                RenderDrawCallback.drawIcon(25, 11, 4, 2);
            }
        }
    } // getWidgetBase ()

    /**
     * Updates and renders the player's health bar based on their current health and effects.
     *
     * @param player The client player entity to retrieve health and effects from
     */
    private void getPlayerHealthBar(LocalPlayer player) {
        // Calculate the fill amount of current health to max health
        float fill = Math.min(1.0F, player.getHealth() / player.getMaxHealth());

        // Determine the type of health bar based on player effects
        int bar = 1;
        if (player.hasEffect(MobEffects.ABSORPTION)) bar = 2;
        if (player.hasEffect(MobEffects.REGENERATION)) bar = 3;
        if (player.hasEffect(MobEffects.DAMAGE_BOOST)) bar = 4;

        // Bind the health bar texture and render the bar
        RenderSystem.setShaderTexture(0, ReignitedHudID.TEX_HUD_BAR);
        RenderDrawCallback.drawMediumBar(48, 24, bar, fill);
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
        if (player.isUnderWater()) {
            // Calculate the position of the air bar on the screen
            int posX = (screenWidth - 121) / 2;
            int posY = screenHeight - 47;

            // Get the player's current air supply
            float air = (float)player.getAirSupply();

            // Bind the air bar texture and render the air bar based on the player's air supply
            RenderSystem.setShaderTexture(0, ReignitedHudID.TEX_HUD_BAR);
            RenderDrawCallback.drawLongBar(posX, posY, 5, air / 300.0F);

            // Determine the texture X position based on the player's air supply level
            int texX = 40;
            if (player.getAirSupply() > 2 && player.getAirSupply() <= 5) texX = 50;
            if (player.getAirSupply() > 0 && player.getAirSupply() <= 2) texX = 60;

            // Bind the air bar icon texture and render the icon if the player's air supply is not full
            RenderSystem.setShaderTexture(0, ReignitedHudID.TEX_HUD_ICON);
            if (player.getAirSupply() < player.getMaxAirSupply())
                this.minecraft.gui.blit(new PoseStack(), screenWidth / 2 - 4, posY - 2, texX, 0, 10, 10);
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
            LivingEntity mount = (LivingEntity)player.getVehicle();
            float health = 1.0F;

            // Calculate the health percentage of the mount
            if (mount.getHealth() < mount.getMaxHealth())
                health = mount.getHealth() / mount.getMaxHealth();

            // Display the base HUD texture
            RenderSystem.setShaderTexture(0, ReignitedHudID.TEX_HUD_BASE);

            // Check if the mount is a tamed HorseEntity to display the appropriate icon
            if (mount instanceof Horse && ((Horse)mount).isTamed()) {
                this.minecraft.gui.blit(new PoseStack(), 15, 56, 0, 29, 6, 25);
            } else {
                this.minecraft.gui.blit(new PoseStack(), 15, 56, 6, 29, 6, 19);
            }

            // Display the icon texture
            RenderSystem.setShaderTexture(0, ReignitedHudID.TEX_HUD_ICON);
            RenderDrawCallback.drawIcon(23, 57, 1, 10);

            // Display the health bar texture
            RenderSystem.setShaderTexture(0, ReignitedHudID.TEX_HUD_BAR);
            RenderDrawCallback.drawMediumBar(23, 68, 1, health);

            // Check if the mount implements IJumpingMount and is tamed to display the jump bar
            if (mount instanceof PlayerRideableJumping && ((Horse)mount).isTamed())
                RenderDrawCallback.drawMediumBar(23, 74, 5, player.getJumpRidingScale());

            // Display the mount's name on the HUD
            String mountStat = mount.getDisplayName().getString();
            RenderDrawCallback.drawFontWithShadow(mountStat, 35, 58, 16777215);
        }

    } // getMountInfo ()

    /**
     * Updates the display of the player's food value on the HUD.
     *
     * @param player The player entity whose food value will be displayed
     */
    private void getFoodValue(LocalPlayer player) {
        // Get the player's hunger value as a string
        String hunger = String.valueOf(player.getFoodData().getFoodLevel());

        // Determine the icon to display based on player's hunger effect
        int icon = player.hasEffect(MobEffects.HUNGER) ? 2 : 1;

        // Bind the food icon texture
        RenderSystem.setShaderTexture(0, ReignitedHudID.TEX_HUD_ICON);

        // Draw the food icon on the HUD
        RenderDrawCallback.drawIcon(47, 31, 1, icon);

        // Set default color and shadow for the food value display
        int color = 11960912;
        int shadow = 3349772;

        // Adjust color and shadow if player has hunger effect
        if (player.hasEffect(MobEffects.HUNGER)) {
            color = 7636056;
            shadow = 1710089;
        }

        // Draw the food value on the HUD with appropriate color and shadow
        RenderDrawCallback.drawFontWithShadow(hunger, 59, 32, color, shadow);
    } // getFoodValue ()

    /**
     * Updates the HUD to display the player's hunger and saturation levels.
     * @param player The player entity for which to update the HUD
     */
    private void getSatuValue(LocalPlayer player) {
        ItemStack heldmain = player.getMainHandItem();
        ItemStack heldoff = player.getOffhandItem();

        // Get hunger and saturation levels as strings
        String hunger = String.valueOf(player.getFoodData().getFoodLevel());
        String saturation = String.valueOf((int)player.getFoodData().getSaturationLevel());

        // Calculate positions for HUD elements
        int posXIcon = 59 + RenderDrawCallback.getStringWidth(hunger) + 5;
        int posXtext = posXIcon + 8 + 4;
        RenderSystem.setShaderTexture(0, ReignitedHudID.TEX_HUD_ICON);

        // Set the correct icon based on player's effect
        int icon = player.hasEffect(MobEffects.HUNGER) ? 3 : 4;

        // Draw hunger icon
        RenderDrawCallback.drawIcon(posXIcon, 31, 1, icon);

        // Set colors for text and shadow
        int color = 14533185;
        int color2 = 15919288;
        int shadow = 3682053;

        // Draw saturation level with shadow
        RenderDrawCallback.drawFontWithShadow(saturation, posXtext, 32, color, shadow);

        // Display additional saturation information if AppleSkin mod is present
        /*if (IgniteHUD.hasAppleSkin) {
            String text = "";

            // Add saturation increment from held main item if it is food
            if (heldmain != null && FoodHelper.isFood(heldmain))
                text = text + "+" + MathHelper.round2(FoodHelper.getModifiedFoodValues(heldmain, player).getSaturationIncrement());

            // Add saturation increment from held offhand item if it is food and main hand item is not
            if (heldoff != null && !FoodHelper.isFood(heldmain) && FoodHelper.isFood(heldoff))
                text = text + "+" + MathHelper.round2(FoodHelper.getModifiedFoodValues(heldoff, player).getSaturationIncrement());

            // Draw additional saturation information with shadow
            RenderDrawCallback.drawFontWithShadow(text, posXtext - 6, 42, color2, shadow);
        }*/

    } // getSatuValue ()

    /**
     * Updates the HUD to display the player's armor values.
     *
     * @param player The player entity for which to update the HUD
     */
    private void getArmorValue(LocalPlayer player) {
        // Get the player's armor and toughness values
        int armor = (int)player.getAttributeValue(Attributes.ARMOR);
        int toughness = (int)player.getAttributeValue(Attributes.ARMOR_TOUGHNESS);

        // Get hunger and saturation levels as strings
        String hunger = String.valueOf(player.getFoodData().getFoodLevel());
        String saturation = String.valueOf((int) player.getFoodData().getSaturationLevel());

        // Calculate positions for HUD elements
        int posXicon = 59 + RenderDrawCallback.getStringWidth(hunger) + 5 + 8 + 4 + RenderDrawCallback.getStringWidth(saturation) + 5;
        int posXtext = posXicon + 8 + 4;

        // Draw armor icon and value if player has armor
        if (armor > 0) {
            RenderSystem.setShaderTexture(0, ReignitedHudID.TEX_HUD_ICON);
            RenderDrawCallback.drawIcon(posXicon, 31, 1, 8);
            RenderDrawCallback.drawFontWithShadow(armor + "", posXtext, 32, 12106180, 1579034);
        }

        // Draw toughness icon and value if player has toughness
        if (player.getAttributeValue(Attributes.ARMOR) > 0.0) {
            if (armor > 0) {
                posXicon += 12 + RenderDrawCallback.getStringWidth(toughness + "") + 5;
                posXtext += 12 + RenderDrawCallback.getStringWidth(toughness + "") + 5;
            }

            RenderSystem.setShaderTexture(0, ReignitedHudID.TEX_HUD_ICON);
            RenderDrawCallback.drawIcon(posXicon, 31, 1, 9);
            RenderDrawCallback.drawFontWithShadow(toughness + "", posXtext, 32, 12106180, 1579034);
        }

    } // getArmorValue ()

    /**
     * Updates the HUD to display the durability of the player's equipped items.
     * This includes the durability bars for the head, chest, legs, feet, main hand, and offhand items.
     *
     * @param player The client player entity to retrieve equipped items from
     */
    private void getDurability(LocalPlayer player) {
        // Retrieve equipped items
        ItemStack head = player.getItemBySlot(EquipmentSlot.HEAD);
        ItemStack chest = player.getItemBySlot(EquipmentSlot.CHEST);
        ItemStack legs = player.getItemBySlot(EquipmentSlot.LEGS);
        ItemStack feet = player.getItemBySlot(EquipmentSlot.FEET);
        ItemStack mainHandItem = player.getMainHandItem();
        ItemStack offhandItem = player.getOffhandItem();

        // Initialize position for rendering
        int pos = 0;

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

        // Display durability for the main hand item
        RenderDrawCallback.addDurabilityDisplay(mainHandItem, pos);
        if (mainHandItem.getItem() != Items.AIR) pos += 25;

        // Display durability for the offhand item
        RenderDrawCallback.addDurabilityDisplay(offhandItem, pos);
    } // getDurability ()

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
            //GlStateManager.ligh();

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
                    RenderSystem.setShaderTexture(0, ReignitedHudID.TEX_HUD_BASE);
                    this.minecraft.gui.blit(new PoseStack(), posX, posY, 88, 0, 29, 21);
                    GlStateManager._clearColor(1.0F, 1.0F, 1.0F, transparency);
                    RenderSystem.setShaderTexture(0, ReignitedHudID.TEX_HUD_EFFECT);
                    this.minecraft.gui.blit(new PoseStack(), posX + 6, posY - 3, icon % 14 * 18, icon / 14 * 18, 18, 18);

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
        RenderSystem.setShaderTexture(0, ReignitedHudID.TEX_HUD_BASE);
        this.minecraft.gui.blit(new PoseStack(), screenWidth / 2 - 22, screenHeight - 36, 165, 0, 44, 14);

        RenderSystem.setShaderTexture(0, ReignitedHudID.TEX_HUD_ICON);
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
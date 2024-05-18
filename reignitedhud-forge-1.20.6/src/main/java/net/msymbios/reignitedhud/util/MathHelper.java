package net.msymbios.reignitedhud.util;

public class MathHelper {

    // -- Methods --

    public static float clamp(float value, float min, float max) {
        if (value < min) return min;
        if (value > max) return max;
        return value;
    } // clamp ()

    public static float round2(float value) {
        return (float)Math.round(value * 10.0F) / 10.0F;
    } // round2 ()

} //  Class MathHelper
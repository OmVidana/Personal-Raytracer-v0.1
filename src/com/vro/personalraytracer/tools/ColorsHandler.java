package com.vro.personalraytracer.tools;

import java.awt.*;

/**
 * Color Handler class. Helps operating with colors.
 *
 * @author Omar Vidaña Rodríguez.
 */
public class ColorsHandler {

    /**
     * Clamp float.
     *
     * @param value the value
     * @param min   the min
     * @param max   the max
     * @return the float
     */
    public static float clamp(double value, double min, double max) {
        if (value < min) {
            return (float) min;
        }
        if (value > max) {
            return (float) max;
        }
        return (float) value;
    }

    /**
     * Add color color.
     *
     * @param original   the original
     * @param otherColor the other color
     * @return the color
     */
    public static Color addColor(Color original, Color otherColor) {
        float red = clamp((original.getRed() / 255.0) + (otherColor.getRed() / 255.0), 0, 1);
        float green = clamp((original.getGreen() / 255.0) + (otherColor.getGreen() / 255.0), 0, 1);
        float blue = clamp((original.getBlue() / 255.0) + (otherColor.getBlue() / 255.0), 0, 1);
        return new Color(red, green, blue);
    }

    /**
     * Remove color color.
     *
     * @param original   the original
     * @param otherColor the other color
     * @return the color
     */
    public static Color removeColor(Color original, Color otherColor) {
        float red = clamp((original.getRed() / 255.0) - (otherColor.getRed() / 255.0), 0, 1);
        float green = clamp((original.getGreen() / 255.0) - (otherColor.getGreen() / 255.0), 0, 1);
        float blue = clamp((original.getBlue() / 255.0) - (otherColor.getBlue() / 255.0), 0, 1);
        return new Color(red, green, blue);
    }

    /**
     * Multiply color.
     *
     * @param original   the original
     * @param otherColor the other color
     * @return the color
     */
    public static Color multiply(Color original, Color otherColor) {
        float r = clamp((original.getRed() * otherColor.getRed()), 0, 1);
        float g = clamp((original.getGreen() * otherColor.getGreen()), 0, 1);
        float b = clamp((original.getBlue() * otherColor.getBlue()), 0, 1);
        return new Color(r, g, b);
    }

    /**
     * Multiply color.
     *
     * @param original the original
     * @param scalar   the scalar
     * @return the color
     */
    public static Color multiply(Color original, double scalar) {
        float r = clamp((original.getRed() * scalar), 0, 1);
        float g = clamp((original.getGreen() * scalar), 0, 1);
        float b = clamp((original.getBlue() * scalar), 0, 1);
        return new Color(r, g, b);
    }

}

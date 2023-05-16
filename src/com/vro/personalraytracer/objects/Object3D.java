package com.vro.personalraytracer.objects;

import com.vro.personalraytracer.tools.Vector3D;

import java.awt.*;

/**
 * The type Object 3 d.
 */
public abstract class Object3D implements IsIntersectable {
    private Color color;
    private Vector3D position;

    /**
     * Instantiates a new Object 3 d.
     *
     * @param position the position
     * @param color    the color
     */
    public Object3D(Vector3D position, Color color) {
        setPosition(position);
        setColor(color);
    }

    /**
     * Gets color.
     *
     * @return the color
     */
    public Color getColor() {
        return color;
    }

    /**
     * Sets color.
     *
     * @param color the color
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * Gets position.
     *
     * @return the position
     */
    public Vector3D getPosition() {
        return position;
    }

    /**
     * Sets position.
     *
     * @param position the position
     */
    public void setPosition(Vector3D position) {
        this.position = position;
    }

}

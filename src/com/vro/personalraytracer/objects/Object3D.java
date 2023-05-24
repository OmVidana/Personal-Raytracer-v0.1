package com.vro.personalraytracer.objects;

import com.vro.personalraytracer.tools.Vector3D;

import java.awt.*;

/**
 * Abstract Class Object3D. Creates an Object3D to be rendered.
 *
 * @author Omar Vidaña Rodríguez - complemented by Jafet Rodríguez' Code.
 */
public abstract class Object3D implements IsIntersectable {
    private Color color;
    private Vector3D position;
    private Vector3D scale;

    /**
     * Instantiates a new Object3D.
     *
     * @param position Object3D Position.
     * @param color    Object3D Color.
     */
    public Object3D(Vector3D position, Color color) {
        setPosition(position);
        setColor(color);
        setScale(new Vector3D(1, 1, 1));
    }

    /**
     * Instantiates a new Object3D in the scale given.
     *
     * @param position Object3D Position.
     * @param scale    Object3D Scale.
     * @param color    Object3D Color.
     */
    public Object3D(Vector3D position, Vector3D scale, Color color) {
        setPosition(position);
        setColor(color);
        setScale(scale);
    }

    /**
     * Gets color.
     *
     * @return Object3D Color.
     */
    public Color getColor() {
        return color;
    }

    /**
     * Sets color.
     *
     * @param color Object3D Color.
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * Gets position.
     *
     * @return Object3D Position.
     */
    public Vector3D getPosition() {
        return position;
    }

    /**
     * Sets position.
     *
     * @param position Object3D Position.
     */
    public void setPosition(Vector3D position) {
        this.position = position;
    }

    /**
     * Gets scale.
     *
     * @return Vector3D Scale.
     */
    public Vector3D getScale() {
        return scale;
    }

    /**
     * Sets scale.
     *
     * @param scale Sets the Vector3D Scale to grow the Object.
     */
    public void setScale(Vector3D scale) {
        this.scale = scale;
    }

    /**
     * Increase size.
     *
     * @param scale Vector3D Scale. Grows the Object based on the given Vector.
     */
    public abstract void increaseSize(Vector3D scale);

    /**
     * Decrease size.
     *
     * @param scale Vector3D Scale. Reduces the Object based on the given Vector.
     */
    public abstract void decreaseSize(Vector3D scale);
}

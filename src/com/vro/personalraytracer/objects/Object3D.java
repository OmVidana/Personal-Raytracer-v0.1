package com.vro.personalraytracer.objects;

import com.vro.personalraytracer.materials.BlinnPhongShading;
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
    private BlinnPhongShading material;
    /**
     * The Is reflective.
     */
    boolean isReflective;
    /**
     * The Is refractive.
     */
    boolean isRefractive;
    /**
     * The Reflective k.
     */
    double reflectiveK;
    /**
     * The Refractive k.
     */
    double refractiveK;

    /**
     * Instantiates a new Object3D.
     *
     * @param position     Object3D Position.
     * @param color        Object3D Color.
     * @param isReflective Reflectivity Boolean.
     * @param reflectiveK  Reflectivity Constant.
     * @param isRefractive Refractivity Boolean (WIP).
     * @param refractiveK  Refractivity Constant..
     */
    public Object3D(Vector3D position, Color color, boolean isReflective, double reflectiveK, boolean isRefractive, double refractiveK) {
        setPosition(position);
        setColor(color);
        setScale(new Vector3D(1, 1, 1));
    }

    /**
     * Instantiates a new Object3D in the scale given.
     *
     * @param position     Object3D Position.
     * @param scale        Object3D Scale.
     * @param color        Object3D Color.
     * @param isReflective the is reflective
     * @param reflectiveK  the reflective k
     * @param isRefractive the is refractive
     * @param refractiveK  the refractive k
     */
    public Object3D(Vector3D position, Vector3D scale, Color color, boolean isReflective, double reflectiveK, boolean isRefractive, double refractiveK) {
        setPosition(position);
        setColor(color);
        setScale(scale);
        setReflective(isReflective);
        setReflectiveK(reflectiveK);
        setReflective(isRefractive);
        setRefractiveK(refractiveK);
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
     * Gets material.
     *
     * @return Gets the Material to access its components.
     */
    public BlinnPhongShading getMaterial() {
        return material;
    }

    /**
     * Sets Material.
     *
     * @param material Assigns the BlinnPhongShading to the Object3D.
     */
    public void setMaterial(BlinnPhongShading material) {
        this.material = material;
    }

    /**
     * Is reflective Boolean.
     *
     * @return Reflectivity Boolean.
     */
    public boolean isReflective() {
        return isReflective;
    }

    /**
     * Sets Reflectivity Constant.
     *
     * @param reflective Reflectivity.
     */
    public void setReflective(boolean reflective) {
        isReflective = reflective;
    }

    /**
     * Is refractive boolean.
     *
     * @return Refractivity Boolean.
     */
    public boolean isRefractive() {
        return isRefractive;
    }

    /**
     * Sets Refractivity Constant.
     *
     * @param refractive Refractivity Constant.
     */
    public void setRefractive(boolean refractive) {
        isRefractive = refractive;
    }

    /**
     * Gets Reflective Constant.
     *
     * @return Reflective.Constant.
     */
    public double getReflectiveK() {
        return reflectiveK;
    }

    /**
     * Sets Reflective Constant Value.
     *
     * @param reflectiveK Reflective Constant Value.
     */
    public void setReflectiveK(double reflectiveK) {
        this.reflectiveK = reflectiveK;
    }

    /**
     * Gets Refractive Constant Value.
     *
     * @return Refractive Constant Value.
     */
    public double getRefractiveK() {
        return refractiveK;
    }

    /**
     * Sets Refractive Constant Value.
     *
     * @param refractiveK Refractive Constant Value.
     */
    public void setRefractiveK(double refractiveK) {
        this.refractiveK = refractiveK;
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

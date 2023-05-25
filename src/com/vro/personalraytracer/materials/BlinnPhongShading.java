package com.vro.personalraytracer.materials;

import com.vro.personalraytracer.objects.lights.Light;
import com.vro.personalraytracer.tools.ColorsHandler;
import com.vro.personalraytracer.tools.Intersection;
import com.vro.personalraytracer.tools.Vector3D;

import java.awt.*;

import static com.vro.personalraytracer.ParallelRT.raycast;

/**
 * Blinn Phong Shadding Class. It helps to obtain the Ambient, Diffuse and Specular.
 *
 * @author Omar Vidaña Rodríguez.
 */
public class BlinnPhongShading {

    /**
     * The Ambient factor.
     */
    double ambientFactor;
    /**
     * The Diffuse factor.
     */
    double diffuseFactor;
    /**
     * The Specular factor.
     */
    double specularFactor;
    /**
     * The Specular exponent.
     */
    double specularExponent;


    /**
     * Instantiates a new Blinn Phong Shading to save the values and relate them to an Object3D.
     *
     * @param ambientFactor    Ambient RGB Value.
     * @param diffuseFactor    Diffuse RGB Value.
     * @param specularFactor   Specular RGB Value.
     * @param specularExponent Shininess Value.
     */
    public BlinnPhongShading(double ambientFactor, double diffuseFactor, double specularFactor, double specularExponent) {
        setAmbientFactor(ambientFactor);
        setDiffuseFactor(diffuseFactor);
        setSpecularFactor(specularFactor);
        setSpecularExponent(specularExponent);
    }

    /**
     * Gets Ambient value.
     *
     * @return Ambient value.
     */
    public double getAmbientFactor() {
        return ambientFactor;
    }

    /**
     * Sets Ambient value.
     *
     * @param ambient Ambient value.
     */
    public void setAmbientFactor(double ambient) {
        this.ambientFactor = ambient;
    }

    /**
     * Gets Diffuse value.
     *
     * @return Diffuse value.
     */
    public double getDiffuseFactor() {
        return diffuseFactor;
    }

    /**
     * Sets Diffuse value.
     *
     * @param diffuseFactor Diffuse value.
     */
    public void setDiffuseFactor(double diffuseFactor) {
        this.diffuseFactor = diffuseFactor;
    }

    /**
     * Gets Specular Value.
     *
     * @return Specular value.
     */
    public double getSpecularFactor() {
        return specularFactor;
    }

    /**
     * Sets Specular value.
     *
     * @param specularFactor Specular value.
     */
    public void setSpecularFactor(double specularFactor) {
        this.specularFactor = specularFactor;
    }

    /**
     * Gets Specular value.
     *
     * @return Specular value.
     */
    public double getSpecularExponent() {
        return specularExponent;
    }

    /**
     * Sets Shininess.
     *
     * @param specularExponent Shininess.
     */
    public void setSpecularExponent(double specularExponent) {
        this.specularExponent = specularExponent;
    }

    /**
     * Gets Ambient RGB value.
     *
     * @param intersection Light Intersection with Object.
     * @return Ambient RGB value.
     */
    public Color getAmbient(Intersection intersection) {
        return ColorsHandler.multiply(intersection.getObject().getColor(), getAmbientFactor());
    }

    /**
     * Gets Diffuse RGB value.
     *
     * @param intersection Light Intersection with Object.
     * @param light        Light Object which is in checking the Object in Intersection.
     * @return Diffuse RGB value.
     */
    public Color getDiffuse(Intersection intersection, Light light) {
        double diffuseFactor = Math.max(light.getNDotL(intersection), 0);
        Color lightDiffuseColor = ColorsHandler.multiply(light.getColor(), getDiffuseFactor());
        return ColorsHandler.multiply(lightDiffuseColor, getAmbientFactor());
    }

    /**
     * Gets Specular RGB value.
     *
     * @param intersection Light Intersection with Object.
     * @param light        Light Object which is in checking the Object in Intersection.
     * @param N            Vector Normal from
     * @param H            the h
     * @return the specular
     */
    public Color getSpecular(Intersection intersection, Light light, Vector3D N, Vector3D H) {
        Color specularColor = ColorsHandler.multiply(intersection.getObject().getColor(), getSpecularFactor());
        double shininess = Math.pow(Vector3D.dotProduct(N, H), getSpecularExponent());
        return ColorsHandler.multiply(ColorsHandler.multiply(specularColor, light.getColor()), shininess);
    }
}

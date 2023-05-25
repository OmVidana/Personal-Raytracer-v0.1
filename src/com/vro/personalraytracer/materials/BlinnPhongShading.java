package com.vro.personalraytracer.materials;

import com.vro.personalraytracer.objects.lights.Light;
import com.vro.personalraytracer.tools.ColorsHandler;
import com.vro.personalraytracer.tools.Intersection;
import com.vro.personalraytracer.tools.Vector3D;

import java.awt.*;

import static com.vro.personalraytracer.ParallelRT.raycast;

public class BlinnPhongShading {

    double ambientFactor;
    double diffuseFactor;
    double specularFactor;
    double specularExponent;
    boolean isReflective;
    boolean isRefractive;

    public BlinnPhongShading(double ambient, double diffuseFactor, double specularFactor, double specularExponent, boolean isReflective, boolean isRefractive) {
        setAmbientFactor(ambient);
        setDiffuseFactor(diffuseFactor);
        setSpecularFactor(specularFactor);
        setSpecularExponent(specularExponent);
        setReflective(isReflective);

    }

    public double getAmbientFactor() {
        return ambientFactor;
    }

    public void setAmbientFactor(double ambient) {
        this.ambientFactor = ambient;
    }

    public double getDiffuseFactor() {
        return diffuseFactor;
    }

    public void setDiffuseFactor(double diffuseFactor) {
        this.diffuseFactor = diffuseFactor;
    }

    public double getSpecularFactor() {
        return specularFactor;
    }

    public void setSpecularFactor(double specularFactor) {
        this.specularFactor = specularFactor;
    }

    public double getSpecularExponent() {
        return specularExponent;
    }

    public void setSpecularExponent(double specularExponent) {
        this.specularExponent = specularExponent;
    }

    public boolean isReflective() {
        return isReflective;
    }

    public void setReflective(boolean reflective) {
        isReflective = reflective;
    }

    public boolean isRefractive() {
        return isRefractive;
    }

    public void setRefractive(boolean refractive) {
        isRefractive = refractive;
    }

    public Color getAmbient(Intersection intersection) {
        return ColorsHandler.multiply(intersection.getObject().getColor(), getAmbientFactor());
    }

    public Color getDiffuse(Intersection intersection, Light light) {
        double diffuseFactor = Math.max(light.getNDotL(intersection), 0);
        Color lightDiffuseColor = ColorsHandler.multiply(light.getColor(), getDiffuseFactor());
        return ColorsHandler.multiply(lightDiffuseColor, getAmbientFactor());
    }

    public Color getSpecular(Intersection intersection, Light light, Vector3D N, Vector3D H) {
        Color specularColor = ColorsHandler.multiply(intersection.getObject().getColor(), getSpecularFactor());
        double shininess = Math.pow(Vector3D.dotProduct(N, H), getSpecularExponent());
        return ColorsHandler.multiply(ColorsHandler.multiply(specularColor, light.getColor()), shininess);
    }

    public static Vector3D directionReflection(Vector3D N, Vector3D V) {
        return Vector3D.vectorAddition(Vector3D.scalarMultiplication(Vector3D.scalarMultiplication(N, Vector3D.dotProduct(V,N)), -2), V);
    }

//    public Color getRefraction() {
//        return refraction;
//    }
}

package com.vro.personalraytracer.materials;

import com.vro.personalraytracer.objects.Object3D;
import com.vro.personalraytracer.objects.lights.Light;
import com.vro.personalraytracer.tools.ColorsHandler;
import com.vro.personalraytracer.tools.Intersection;
import com.vro.personalraytracer.tools.Ray;
import com.vro.personalraytracer.tools.Vector3D;

import java.awt.*;

import static com.vro.personalraytracer.ParallelRT.raycast;

public class BlinnPhongShading {

    double ambient;
    double diffuseFactor;
    double specularFactor;
    double specularExponent;
    boolean isReflective;

    boolean isRefractive;

    public BlinnPhongShading(double ambient, double diffuseFactor, double specularFactor, double specularExponent, boolean isReflective) {
        setAmbient(ambient);
        setDiffuseFactor(diffuseFactor);
        setSpecularFactor(specularFactor);
        setSpecularExponent(specularExponent);
        setReflective(isReflective);
    }

    public double getAmbient() {
        return ambient;
    }

    public void setAmbient(double ambient) {
        this.ambient = ambient;
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

    public Color getAmbient(Intersection intersection, double ambient) {
        return ColorsHandler.multiply(intersection.getObject().getColor(), ambient);
    }

    public Color getDiffuse(Intersection intersection, Light light, double scalar) {
        Vector3D N = intersection.getNormal();
        Vector3D P = intersection.getPosition();
        Vector3D L = Vector3D.normalize(Vector3D.vectorSubstraction(light.getPosition(), intersection.getPosition()));
        double diffuseFactor = Math.max(light.getNDotL(intersection), 0);
        Color lightDiffuseColor = ColorsHandler.multiply(light.getColor(), diffuseFactor);
        return ColorsHandler.multiply(lightDiffuseColor, scalar);
    }

    public Color getSpecular(Intersection intersection, Vector3D cameraPos, Light light, double scalar, double exponent) {
        Vector3D N = intersection.getNormal();
        Vector3D P = intersection.getPosition();
        Vector3D L = Vector3D.normalize(Vector3D.vectorSubstraction(light.getPosition(), intersection.getPosition()));
        Vector3D V = Vector3D.normalize(Vector3D.vectorSubstraction(cameraPos, P));
        Vector3D H = Vector3D.normalize(Vector3D.vectorAddition(V,L));
        Color specularColor = ColorsHandler.multiply(intersection.getObject().getColor(), scalar);
        double shininess = Math.pow(Vector3D.dotProduct(N, H), exponent);
        return ColorsHandler.multiply(ColorsHandler.multiply(specularColor, light.getColor()), shininess);
    }

//    public Color getRefraction() {
//        return refraction;
//    }
}

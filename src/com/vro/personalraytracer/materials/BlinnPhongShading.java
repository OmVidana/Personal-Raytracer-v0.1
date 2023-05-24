package com.vro.personalraytracer.materials;

import com.vro.personalraytracer.objects.lights.Light;
import com.vro.personalraytracer.tools.ColorsHandler;
import com.vro.personalraytracer.tools.Intersection;
import com.vro.personalraytracer.tools.Vector3D;

import java.awt.*;

public class BlinnPhongShading {
    
    public static Color getAmbient(Intersection intersection, double ambient) {
        return ColorsHandler.multiply(intersection.getObject().getColor(), ambient);
    }

    public static Color getDiffuse(Intersection intersection, Light light, double scalar) {
        Vector3D N = intersection.getNormal();
        Vector3D P = intersection.getPosition();
        Vector3D L = Vector3D.normalize(Vector3D.vectorSubstraction(light.getPosition(), intersection.getPosition()));
        double diffuseFactor = Math.max(light.getNDotL(intersection), 0);
        Color lightDiffuseColor = ColorsHandler.multiply(light.getColor(), diffuseFactor);
        return ColorsHandler.multiply(lightDiffuseColor, scalar);
    }

    public static Color getSpecular(Intersection intersection, Vector3D cameraPos, Light light, double scalar, double exponent) {
        Vector3D N = intersection.getNormal();
        Vector3D P = intersection.getPosition();
        Vector3D L = Vector3D.normalize(Vector3D.vectorSubstraction(light.getPosition(), intersection.getPosition()));
        Vector3D V = Vector3D.normalize(Vector3D.vectorSubstraction(cameraPos, P));
        Vector3D H = Vector3D.normalize(Vector3D.vectorAddition(V,L));
        Color specularColor = ColorsHandler.multiply(intersection.getObject().getColor(), scalar);
        double shininess = Math.pow(Vector3D.dotProduct(N, H), exponent);
        return ColorsHandler.multiply(ColorsHandler.multiply(specularColor, light.getColor()), shininess);
    }

//    public static Color getReflection() {
//        return reflection;
//    }
//
//    public static Color getRefraction() {
//        return refraction;
//    }
}

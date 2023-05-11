package com.vro.personalraytracer.objects.lights;

import com.vro.personalraytracer.tools.Intersection;
import com.vro.personalraytracer.tools.Vector3D;

import java.awt.*;

public class PointLight extends Light {

    public PointLight(Vector3D position, Color color, double intensity) {
        super(position, color, intensity);

    }

    @Override
    public double getNDotL(Intersection intersection) {
        Vector3D N = intersection.getNormal();
        Vector3D L = Vector3D.normalize(Vector3D.vectorSubstraction(getPosition(), intersection.getPosition()));
        return Math.max(Vector3D.dotProduct(N,L), 0.0);
    }
}

package com.vro.personalraytracer.objects.lights;

import com.vro.personalraytracer.tools.Intersection;
import com.vro.personalraytracer.tools.Vector3D;

import java.awt.*;

public class DirectionalLight extends Light{
    private Vector3D direction;

    public DirectionalLight(Vector3D direction, Color color, double intensity) {
        super(new Vector3D(), color, intensity);
        setDirection(Vector3D.normalize(direction));
    }

    public Vector3D getDirection() {
        return direction;
    }

    public void setDirection(Vector3D direction) {
        this.direction = direction;
    }

    @Override
    public double getNDotL(Intersection intersection) {
        return Math.max(Vector3D.dotProduct(intersection.getNormal(), Vector3D.scalarMultiplication(getDirection(), -1.0)), 0.0);
    }
}

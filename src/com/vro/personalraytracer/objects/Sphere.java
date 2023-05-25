package com.vro.personalraytracer.objects;

import com.vro.personalraytracer.tools.Intersection;
import com.vro.personalraytracer.tools.Ray;
import com.vro.personalraytracer.tools.Vector3D;

import java.awt.*;

/**
 * Class Sphere which inherits from Object3D.
 *
 * @author Omar Vidaña Rodríguez
 */
public class Sphere extends Object3D {
    private double radius;

    /**
     * Instantiates a new Sphere.
     *
     * @param center the center
     * @param radius the radius
     * @param color  the color
     */
    public Sphere(Vector3D center, double radius, Color color, boolean isReflective, double reflectiveK, boolean isRefractive, double refractiveK) {
        super(center, color, isReflective, reflectiveK, isRefractive, refractiveK);
        setRadius(radius);
    }

    @Override
    public Intersection getIntersection(Ray ray) {
        Vector3D L = Vector3D.vectorSubstraction(ray.getOrigin(), getPosition());
        double tca = Vector3D.dotProduct(ray.getDirection(), L);
        double L2 = Math.pow(L.getMagnitude(), 2);

        double d2 = Math.pow(tca, 2) - L2 + Math.pow(getRadius(), 2);

        if (d2 >= 0) {
            double d = Math.sqrt(d2);
            double t0 = -tca + d;
            double t1 = -tca - d;

            double distance = Math.min(t0, t1);
            Vector3D position = Vector3D.vectorAddition(ray.getOrigin(), Vector3D.scalarMultiplication(ray.getDirection(), distance));
            Vector3D normal = Vector3D.normalize(Vector3D.vectorSubstraction(position, getPosition()));
            return new Intersection(position, distance, normal, this);
        }

        return null;
    }

    /**
     * Gets radius.
     *
     * @return the radius
     */
    public double getRadius() {
        return radius;
    }

    /**
     * Sets radius.
     *
     * @param radius the radius
     */
    public void setRadius(double radius) {
        this.radius = radius;
    }

    @Override
    public void increaseSize(Vector3D scale) {
        double length = (scale.getX() + scale.getY() + scale.getZ());
        setRadius(getRadius() + length);
    }

    @Override
    public void decreaseSize(Vector3D scale) {
        double length = (scale.getX() + scale.getY() + scale.getZ());
        setRadius(getRadius() - length);
    }
}

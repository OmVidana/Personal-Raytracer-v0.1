package com.vr.raytracer.objects;

import com.vr.raytracer.utils.Intersection;
import com.vr.raytracer.utils.Ray;
import com.vr.raytracer.utils.Vector3D;

import java.awt.Color;

public class Sphere extends Object3D{
    private double radius;

    public Sphere(Vector3D center, double radius) {
        super(center, new Color(0, 0, 0));
        setRadius(radius);
    }

    public Sphere(Vector3D center, double radius, Color color) {
        super(center, color);
        setRadius(radius);
    }

    public Sphere(Vector3D center, double radius, int red, int green, int blue) {
        super(center, new Color(red, green, blue));
        setRadius(radius);
    }

    public Sphere(double x, double y, double z, double radius, Color color) {
        super(new Vector3D(x, y, z), color);
        setRadius(radius);
    }

    public Sphere(double x, double y, double z, double radius, int red, int green, int blue) {
        super(new Vector3D(x, y, z), new Color(red, green, blue));
        setRadius(radius);
    }

    @Override
    public Intersection getIntersection(Ray ray) {
        Vector3D L = Vector3D.vectorSubtraction(ray.getOrigin(), getPosition());
        double tca = Vector3D.dotProduct(ray.getDirection(), L);
        double L2 = Math.pow(Vector3D.getMagnitude(L), 2);

        double d2 = Math.pow(tca, 2) - L2 + Math.pow(getRadius(), 2);

        if(d2 >= 0){
            double d = Math.sqrt(d2);
            double t0 = -tca + d;
            double t1 = -tca - d;

            double distance = Math.min(t0, t1);
            Vector3D position = Vector3D.vectorAddition(ray.getOrigin(), Vector3D.scalarMultiplication(ray.getDirection(), distance));
            Vector3D normal = Vector3D.normalize(Vector3D.vectorSubtraction(position, getPosition()));
            return new Intersection(position, distance, normal, this);
        }

        return null;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

}

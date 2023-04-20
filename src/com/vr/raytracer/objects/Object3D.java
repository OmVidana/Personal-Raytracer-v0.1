package com.vr.raytracer.objects;

import com.vr.raytracer.utils.Intersection;
import com.vr.raytracer.utils.Ray;
import com.vr.raytracer.utils.Vector3D;

import java.awt.Color;

public abstract class Object3D {
    private Color color;
    private Vector3D position;

    public Object3D(Vector3D position, Color color) {
        setPosition(position);
        setColor(color);
    }

    public abstract Intersection getIntersection(Ray ray);

    public void setPosition(Vector3D position) {
        this.position = position;
    }

    public Vector3D getPosition() {
        return position;
    }

    public void moveX(double x) {
        this.position.setX(x);
    }

    public void moveY(double y) {
        this.position.setY(y);
    }

    public void moveZ(double z) {
        this.position.setZ(z);
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }
}

package com.vro.personalraytracer.tools;



public class Ray {
    private Vector3D origin;
    private Vector3D direction;

    public Ray(Vector3D origin, Vector3D direction) {
        setOrigin(origin);
        setDirection(direction);
    }

    public Vector3D getOrigin() {
        return origin;
    }

    private void setOrigin(Vector3D origin) {
        this.origin = origin;
    }

    public Vector3D getDirection() {
        return Vector3D.normalize(direction);
    }

    private void setDirection(Vector3D direction) {
        this.direction = direction;
    }
}
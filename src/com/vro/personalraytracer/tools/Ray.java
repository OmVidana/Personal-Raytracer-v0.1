package com.vro.personalraytracer.tools;


/**
 * The type Ray.
 */
public class Ray {
    private Vector3D origin;
    private Vector3D direction;

    /**
     * Instantiates a new Ray.
     *
     * @param origin    the origin
     * @param direction the direction
     */
    public Ray(Vector3D origin, Vector3D direction) {
        setOrigin(origin);
        setDirection(direction);
    }

    /**
     * Gets origin.
     *
     * @return the origin
     */
    public Vector3D getOrigin() {
        return origin;
    }

    private void setOrigin(Vector3D origin) {
        this.origin = origin;
    }

    /**
     * Gets direction.
     *
     * @return the direction
     */
    public Vector3D getDirection() {
        return Vector3D.normalize(direction);
    }

    private void setDirection(Vector3D direction) {
        this.direction = direction;
    }
}
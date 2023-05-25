package com.vro.personalraytracer.tools;


/**
 *
 * @autor Jafet Rodríguez - implemented by Omar Vidaña Rodríguez.
 * Class Type Ray. Used to calculate colors and directions.
 */
public class Ray {
    private Vector3D origin;
    private Vector3D direction;

    /**
     * Instantiates a new Ray.
     *
     * @param origin    Vector3D Ray Origin.
     * @param direction Vector3D Ray Direction.
     */
    public Ray(Vector3D origin, Vector3D direction) {
        setOrigin(origin);
        setDirection(direction);
    }

    /**
     * Gets Origin.
     *
     * @return origin
     */
    public Vector3D getOrigin() {
        return origin;
    }


    public void setOrigin(Vector3D origin) {
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


    public void setDirection(Vector3D direction) {
        this.direction = direction;
    }
}
package com.vro.personalraytracer.objects.lights;

import com.vro.personalraytracer.tools.Intersection;
import com.vro.personalraytracer.tools.Vector3D;

import java.awt.*;

/**
 * Class Directional Light. Create a Light that illuminates where it points.
 *
 * @author Omar Vidaña Rodríguez - complemented by Jafet Rodríguez' Code.
 */
public class DirectionalLight extends Light{
    private Vector3D direction;

    /**
     * Instantiates a new Directional light. And its direction.
     *
     * @param origin    Vector3D. Origin of the Light.
     * @param end       Vector3D. Where the Light ends.
     * @param color     Light Color.
     * @param intensity Light Intensity.
     */
    public DirectionalLight(Vector3D origin, Vector3D end, Color color, double intensity) {
        super(origin, color, intensity);
        setDirection(Vector3D.normalize(Vector3D.vectorSubstraction(end, origin)));
    }

    /**
     * Gets the Light Direction.
     *
     * @return Vector3D. Light Direction.
     */
    public Vector3D getDirection() {
        return direction;
    }

    /**
     * Sets the Light Direction.
     *
     * @param direction Vector3D Light Direction.
     */
    public void setDirection(Vector3D direction) {
        this.direction = direction;
    }

    @Override
    public double getNDotL(Intersection intersection) {
        return Math.max(Vector3D.dotProduct(intersection.getNormal(), Vector3D.scalarMultiplication(getDirection(), -1.0)), 0.0);
    }

//    @Override
//    public Intersection getShadowIntersection(Intersection intersection) {
//        return null;
//    }

    @Override
    public void increaseSize(Vector3D scale) {
        setIntensity(getIntensity() + scale.getMagnitude());
    }

    @Override
    public void decreaseSize(Vector3D scale) {
        setIntensity(getIntensity() - scale.getMagnitude());
    }
}

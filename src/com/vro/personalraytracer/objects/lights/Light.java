package com.vro.personalraytracer.objects.lights;

import com.vro.personalraytracer.objects.Object3D;
import com.vro.personalraytracer.tools.Intersection;
import com.vro.personalraytracer.tools.Ray;
import com.vro.personalraytracer.tools.Vector3D;

import java.awt.*;

/**
 * Abstract Class Light. Any Type of Light will extend this class.
 * @author Omar Vidaña Rodríguez - complemented by Jafet Rodríguez' Code.
 */
public abstract class Light extends Object3D {

    private double intensity;

    /**
     * Instantiates a new Light.
     *
     * @param position  Light position
     * @param color     Light color
     * @param intensity Light intensity
     */
    public Light(Vector3D position, Color color, double intensity) {
        super(position, color);
        setIntensity(intensity);
    }

    /**
     * Gets the Light intensity.
     *
     * @return Light intensity
     */
    public double getIntensity() {
        return intensity;
    }

    /**
     * Sets the Light intensity.
     *
     * @param intensity Light intensity
     */
    public void setIntensity(double intensity) {
        this.intensity = intensity;
    }

    /**
     * Gets n dot l.
     *
     * @param intersection Light intersection
     * @return Dot product of N and L.
     * <pre>
     * Where:
     * <br>
     *  N - Object Normal.
     * <br>
     *  L - Light Direction.
     * </pre>
     */
    public abstract double getNDotL(Intersection intersection);

    public Intersection getIntersection(Ray ray) {
        return new Intersection(new Vector3D(), -1, new Vector3D(), null);
    }
}
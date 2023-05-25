package com.vro.personalraytracer.objects.lights;

import com.vro.personalraytracer.objects.Object3D;
import com.vro.personalraytracer.tools.Intersection;
import com.vro.personalraytracer.tools.Ray;
import com.vro.personalraytracer.tools.Vector3D;

import java.awt.*;

/**
 * Abstract Class Light. Any Type of Light will extend this class.
 *
 * @author Omar Vidaña Rodríguez - complemented by Jafet Rodríguez' Code.
 */
public abstract class Light extends Object3D {

    private double intensity;

    /**
     * Instantiates a new Light.
     *
     * @param position  Light Position.
     * @param color     Light Color.
     * @param intensity Light Intensity.
     */
    public Light(Vector3D position, Color color, double intensity) {
        super(position, color, false, 0, false, 0);
        setIntensity(intensity);
    }

    /**
     * Gets the Light intensity.
     *
     * @return Double. Light Intensity.
     */
    public double getIntensity() {
        return intensity;
    }

    /**
     * Sets the Light intensity.
     *
     * @param intensity Light Intensity.
     */
    public void setIntensity(double intensity) {
        this.intensity = intensity;
    }

    /**
     * Gets the N dot product L.
     *
     * @param intersection Light Intersection.
     * @return Double. Dot product of N and L. <pre> Where: <br>  N - Object Normal. <br>  L - Light Direction. </pre>.
     */
    public abstract double getNDotL(Intersection intersection);

//    public abstract Intersection getShadowIntersection(Intersection intersection);

    public Intersection getIntersection(Ray ray) {
        return new Intersection(new Vector3D(), -1, new Vector3D(), null);
    }
}
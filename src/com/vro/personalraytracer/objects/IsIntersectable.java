package com.vro.personalraytracer.objects;

import com.vro.personalraytracer.tools.Intersection;
import com.vro.personalraytracer.tools.Ray;

/**
 * Interface which has the function to check if a Ray intersects an Object3D.
 * @author Omar Vidaña Rodríguez - Based on Jafet Rodríguez's Code.
 */
public interface IsIntersectable {
    /**
     * Gets the intersection between an Object3D and a Ray.
     *
     * @param ray the ray.
     * @return Intersection. Contains a position, distance, normal and Object3D values.
     */
    Intersection getIntersection(Ray ray);

}

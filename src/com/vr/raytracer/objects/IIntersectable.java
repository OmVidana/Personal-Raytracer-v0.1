package com.vr.raytracer.objects;

import com.vr.raytracer.tools.Intersection;
import com.vr.raytracer.tools.Ray;

public interface IIntersectable {
    Intersection getIntersection(Ray ray);
}

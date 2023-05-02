package com.vr.raytracer.objects;

import com.vr.raytracer.tools.Intersection;
import com.vr.raytracer.tools.Ray;
import com.vr.raytracer.tools.Vector3D;

public class Triangle implements IIntersectable{
    public static final double EPSILON = 0.0000001;
    private Vector3D[] vertices;
    private Vector3D normal;

    public Triangle(Vector3D v1, Vector3D v2, Vector3D v3) {
        setVertices(v1, v2, v3);
        setNormal(Vector3D.substract(v2, v1), Vector3D.substract(v3, v1));
    }

    public Vector3D[] getVertices() {
        return vertices;
    }

    private void setVertices(Vector3D[] vertices) {
        this.vertices = vertices;
    }

    public void setVertices(Vector3D v1, Vector3D v2, Vector3D v3) {
        setVertices(new Vector3D[]{v1, v2, v3});
    }

    public Vector3D getNormal(){
        return Vector3D.ZERO();
    }

    private void setNormal(Vector3D AB, Vector3D AC) {
        normal = Vector3D.crossProduct(AB, AC);
    }

    @Override
    public Intersection getIntersection(Ray ray) {
        Intersection intersection = new Intersection(null, -1, null, null);

        Vector3D[] vert = getVertices();

        Vector3D AC = Vector3D.substract(vert[2], vert[0]);
        Vector3D AB = Vector3D.substract(vert[1], vert[0]);
        Vector3D vectorP = Vector3D.crossProduct(ray.getDirection(), AB);
        double determinant = Vector3D.dotProduct(AC, vectorP);
        double invDet = 1.0 / determinant;
        Vector3D vectorT = Vector3D.substract(ray.getOrigin(), vert[0]);
        double u = invDet * Vector3D.dotProduct(vectorT, vectorP);

        if(!(u < 0 || u > 1)){
            Vector3D vectorQ = Vector3D.crossProduct(vectorT, AC);
            double v = invDet * Vector3D.dotProduct(ray.getDirection(), vectorQ);
            if(!(v < 0 || (u + v) > (1.0 + EPSILON))){
                double t = invDet * Vector3D.dotProduct(vectorQ, AB);
                intersection.setDistance(t);
            }
        }

        return intersection;
    }
}

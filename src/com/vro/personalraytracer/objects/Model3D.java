package com.vro.personalraytracer.objects;

import com.vro.personalraytracer.tools.Barycentric;
import com.vro.personalraytracer.tools.Intersection;
import com.vro.personalraytracer.tools.Ray;
import com.vro.personalraytracer.tools.Vector3D;

import java.awt.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Class Model3D. Creates a Model 3D which extends Object3D. This model is created from Triangles and their normals.
 *
 * @author Omar Vidaña Rodríguez - complemented by Jafet Rodríguez' Code.
 */
public class Model3D extends Object3D {
    private List<Triangle> triangles;

    /**
     * Instantiates a new Model3D.
     *
     * @param position  Vector3D Model3D Position.
     * @param triangles Triangle[] Model3D Triangles.
     * @param color     Model3D Color.
     */
    public Model3D(Vector3D position, Triangle[] triangles, Color color) {
        super(position, color);
        setTriangles(triangles);
    }

    /**
     * Gets the Model3D Triangles.
     *
     * @return List of Triangles.
     */
    public List<Triangle> getTriangles() {
        return triangles;
    }

    /**
     * Sets the Model3D Triangles.
     *
     * @param triangles Triangle[], array of Triangles.
     */
    public void setTriangles(Triangle[] triangles) {
        Vector3D position = getPosition();
        Set<Vector3D> uniqueVertices = new HashSet<>();
        for (Triangle triangle : triangles) {
            uniqueVertices.addAll(Arrays.asList(triangle.getVertices()));
        }

        for (Vector3D vertex : uniqueVertices) {
            vertex.setX(vertex.getX() + position.getX());
            vertex.setY(vertex.getY() + position.getY());
            vertex.setZ(vertex.getZ() + position.getZ());
        }

        this.triangles = Arrays.asList(triangles);
    }

    @Override
    public Intersection getIntersection(Ray ray) {
        double distance = -1;
        Vector3D normal = new Vector3D();
        Vector3D position = new Vector3D();

        for (Triangle triangle : getTriangles()) {
            Intersection intersection = triangle.getIntersection(ray);
            double intersectionDistance = intersection.getDistance();
            if (intersection != null && intersectionDistance > 0 &&
                    (intersectionDistance < distance || distance < 0)) {
                distance = intersectionDistance;
                position = Vector3D.vectorAddition(ray.getOrigin(), Vector3D.scalarMultiplication(ray.getDirection(), distance));
                //normal = triangle.getNormal();
                normal = new Vector3D();
                double[] uVw = Barycentric.CalculateBarycentricCoordinates(position, triangle);
                Vector3D[] normals = triangle.getNormals();
                for (int i = 0; i < uVw.length; i++) {
                    normal = Vector3D.vectorAddition(normal, Vector3D.scalarMultiplication(normals[i], uVw[i]));
                }
            }
        }

        if (distance == -1) {
            return null;
        }

        return new Intersection(position, distance, normal, this);
    }
}

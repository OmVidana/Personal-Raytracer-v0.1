package com.vr.raytracer.objects;

import com.vr.raytracer.utils.Intersection;
import com.vr.raytracer.utils.Ray;
import com.vr.raytracer.utils.Vector3D;

import java.awt.*;

public class Camera extends Object3D{
    private int width;
    private int height;
    private double fovH;
    private double fovV;
    private double defaultZ = 15.0;

    public Camera(Vector3D position, int width, int height, double fovH, double fovV) {
        super(position, Color.WHITE);
        setWidth(width);
        setHeight(height);
        setFovH(fovH);
        setFovV(fovV);
    }

    public Vector3D[][] calculatePositionsToRay() {
        double angleMaxX = getFovH() / 2.0;
        double radiusMaxX = getDefaultZ() / Math.cos(Math.toRadians(angleMaxX));
        double maxX = Math.sin(Math.toRadians(angleMaxX)) * radiusMaxX;
        double minX = -maxX;

        double angleMaxY = getFovV() / 2.0;
        double radiusMaxY = getDefaultZ() / Math.cos(Math.toRadians(angleMaxY));
        double maxY = Math.sin(Math.toRadians(angleMaxY)) * radiusMaxY;
        double minY = -maxY;

        Vector3D[][] positions = new Vector3D[getWidth()][getHeight()];
        double posZ = getDefaultZ();

        for (int x = 0; x < positions.length; x++) {
            for (int y = 0; y < positions[x].length; y++) {
                double posX = minX + ((maxX - minX) / getWidth()) * x;
                double posY = maxY - ((maxY - minY) / getHeight()) * y;
                positions[x][y] = new Vector3D(posX, posY, posZ);
            }
        }

        return positions;
    }

    @Override
    public Intersection getIntersection(Ray ray) {
        return new Intersection(Vector3D.ZERO(), -1, Vector3D.ZERO(), null);
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public double getFovH() {
        return fovH;
    }

    public void setFovH(double fovH) {
        this.fovH = fovH;
    }

    public double getFovV() {
        return fovV;
    }

    public void setFovV(double fovV) {
        this.fovV = fovV;
    }

    public double getDefaultZ() {
        return defaultZ;
    }
}

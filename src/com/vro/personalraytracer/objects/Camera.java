package com.vro.personalraytracer.objects;

import com.vro.personalraytracer.tools.Intersection;
import com.vro.personalraytracer.tools.Ray;
import com.vro.personalraytracer.tools.Vector3D;

import java.awt.*;

/**
 * Class Camera. Creates a Camera which extends from Object3D.
 *
 * @author Omar Vidaña Rodríguez - complemented by Jafet Rodríguez' Code.
 */
public class Camera extends Object3D {
    private int width;
    private int height;
    private double fovH;
    private double fovV;
    private double defaultZ;
    private double[] nearFarPlanes = new double[2];

    /**
     * Instantiates a new Camera in the position given with the following parameters.
     *
     * @param position  Vector3D Camera Position.
     * @param width     Pixel's Width.
     * @param height    Pixel's Height.
     * @param fovH      Horizontal Field of View.
     * @param fovV      Vertical Field of View.
     * @param nearPlane Clipping Near Plane.
     * @param farPlane  Clipping Far Plane.
     */
    public Camera(Vector3D position, int width, int height, double fovH, double fovV, double nearPlane, double farPlane) {
        super(position, Color.BLACK);
        setWidth(width);
        setHeight(height);
        setFovH(fovH);
        setFovV(fovV);
        setNearFarPlanes(new double[]{nearPlane + getPosition().getZ(), farPlane + getPosition().getZ()});
        setDefaultZ(farPlane + getPosition().getZ());
    }

    /**
     * Calculates each Ray position and returns it in a Vector3D[width][height].
     *
     * @return Vector3D[width][height]. Positions to Each Ray.
     */
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
                double posX = minX + (((maxX - minX) / getWidth()) * x);
                double posY = maxY - (((maxY - minY) / getHeight()) * y);
                positions[x][y] = new Vector3D(posX, posY, posZ);
            }
        }

        return positions;
    }

    @Override
    public Intersection getIntersection(Ray ray) {
        return new Intersection(new Vector3D(), -1, new Vector3D(), null);
    }

    /**
     * Gets the Camera Width.
     *
     * @return Int. Width Value.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Sets the Camera Width.
     *
     * @param width Width Value.
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * Gets the Camera Height.
     *
     * @return Int. Height Value.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Sets the Camera Height.
     *
     * @param height Height Value.
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * Gets the Camera Horizontal Field of View.
     *
     * @return Double. Horizontal Field of View Value.
     */
    public double getFovH() {
        return fovH;
    }

    /**
     * Sets the Camera Horizontal Field of View.
     *
     * @param fovH Horizontal Field of View Value.
     */
    public void setFovH(double fovH) {
        this.fovH = fovH;
    }

    /**
     * Gets the Camera Vertical Field of View.
     *
     * @return Double. Vertical Field of View Value.
     */
    public double getFovV() {
        return fovV;
    }

    /**
     * Sets the Camera Vertical Field of View.
     *
     * @param fovV Vertical Field of View Value.
     */
    public void setFovV(double fovV) {
        this.fovV = fovV;
    }

    /**
     * Gets the Default Z Value. This value represents where the rays are going to.
     *
     * @return Double. DefaultZ Value.
     */
    public double getDefaultZ() {
        return defaultZ;
    }

    /**
     * Sets the Default Z Value.
     * @param defaultZ DefaultZ Value.
     */
    public void setDefaultZ(double defaultZ) {
        this.defaultZ = defaultZ;
    }

    /**
     * Gets the Near and Far Planes Values.
     *
     * @return Double[]. Size 2 where Near Plane is in [0] and Far Plane in [1].
     */
    public double[] getNearFarPlanes() {
        return nearFarPlanes;
    }

    /**
     * Sets the Near and Far Planes Value.
     * @param nearFarPlanes Double Array with a size of 2.
     */
    public void setNearFarPlanes(double[] nearFarPlanes) {
        this.nearFarPlanes = nearFarPlanes;
    }

    @Override
    public void increaseSize(Vector3D scale) {

    }

    @Override
    public void decreaseSize(Vector3D scale) {

    }
}

package com.vro.personalraytracer.tools;

public class Vector3D {
    private double x;
    private double y;
    private double z;
    private double magnitude;

    public Vector3D() {
        setVector3D(0.0, 0.0, 0.0);
    }

    public Vector3D(double x, double y, double z) {
        setVector3D(x, y, z);
    }

    public Vector3D(Vector3D vector) {
        setVector3D(vector);
    }

    public void setVector3D(double x, double y, double z) {
        setX(x);
        setY(y);
        setZ(z);
        setMagnitude();
    }

    public void setVector3D(Vector3D vector) {
        setX(vector.x);
        setY(vector.y);
        setZ(vector.z);
        setMagnitude();
    }

    public void setX(double x) {
        this.x = x;
        setMagnitude();
    }

    public void setY(double y) {
        this.y = y;
        setMagnitude();
    }

    public void setZ(double z) {
        this.z = z;
        setMagnitude();
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public void setMagnitude() {
        magnitude = Math.sqrt(dotProduct(this, this));
    }

    public double getMagnitude() {
        return magnitude;
    }

    public static double dotProduct(Vector3D v1, Vector3D v2) {
        return (v1.getX() * v2.getX()) + (v1.getY() * v2.getY()) + (v1.getZ() * v2.getZ());
    }

    public static Vector3D crossProduct(Vector3D v1, Vector3D v2) {
        return new Vector3D(
                (v1.getY() * v2.getZ()) - (v1.getZ() * v2.getY()),
                (v1.getZ() * v2.getX()) - (v1.getX() * v2.getZ()),
                (v1.getX() * v2.getY()) - (v1.getY() * v2.getX())
        );
    }

    public static Vector3D vectorAddition(Vector3D v1, Vector3D v2) {
        return new Vector3D(v1.getX() + v2.getX(), v1.getY() + v2.getY(), v1.getZ() + v2.getZ());
    }

    public void add(Vector3D v2) {
        this.x += v2.x;
        this.y += v2.y;
        this.z += v2.z;
    }

    public static Vector3D vectorSubstraction(Vector3D v1, Vector3D v2) {
        return new Vector3D(v1.getX() - v2.getX(), v1.getY() - v2.getY(), v1.getZ() - v2.getZ());
    }

    public void substract(Vector3D v2) {
        this.x -= v2.x;
        this.y -= v2.y;
        this.z -= v2.z;
    }

    public static Vector3D scalarMultiplication(Vector3D v1, double scalar) {
        return new Vector3D(v1.getX() * scalar, v1.getY() * scalar, v1.getZ() * scalar);
    }

    public void multiply(double scalar) {
        this.x *= scalar;
        this.y *= scalar;
        this.z *= scalar;
    }

    public static Vector3D normalize(Vector3D v1) {
        double mag = v1.getMagnitude();
        return new Vector3D(v1.getX() / mag, v1.getY() / mag, v1.getZ() / mag);
    }

    @Override
    public String toString() {
        return "<" + getX() + ", " + getY() + ", " + getZ() + ">";
    }
}


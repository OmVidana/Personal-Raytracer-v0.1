package com.vr.raytracer.utils;

public class Vector3D {
    public static final Vector3D ZERO = new Vector3D(0,0,0);
    private double x;
    private double y;
    private double z;
    private double magnitude;

    public Vector3D() {
        this(0,0,0);

    }

    public Vector3D(Vector3D vector) {
        setX(vector.x);
        setY(vector.y);
        setZ(vector.z);
        setMagnitude();
    }

    public Vector3D(double x, double y, double z) {
        setX(x);
        setY(y);
        setZ(z);
        setMagnitude();
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
        magnitude = Math.sqrt((x*x) + (y*y) + (z*z));
    }

    public static double getMagnitude(Vector3D vector) {
        return Math.sqrt(Vector3D.dotProduct(vector, vector));
    }

    public void vectorAddition(Vector3D vectorAdded) {
        this.x += vectorAdded.x;
        this.y += vectorAdded.y;
        this.z += vectorAdded.z;
        setMagnitude();
    }

    public static Vector3D vectorAddition(Vector3D vector1, Vector3D vector2) {
        return new Vector3D(
                vector1.x + vector2.x,
                vector1.y + vector2.y,
                vector1.z + vector2.z
        );
    }

    public void vectorSubtraction(Vector3D vectorSubtracted) {
        this.x -= vectorSubtracted.x;
        this.y -= vectorSubtracted.y;
        this.z -= vectorSubtracted.z;
        setMagnitude();
    }
    public static Vector3D vectorSubtraction(Vector3D vector1, Vector3D vector2) {
        return new Vector3D(
                vector1.x - vector2.x,
                vector1.y - vector2.y,
                vector1.z - vector2.z
        );
    }
    public void scalarMultiplication(double scalar) {
        this.x *= scalar;
        this.y *= scalar;
        this.z *= scalar;
        setMagnitude();
    }

    public static Vector3D scalarMultiplication(Vector3D vector1, double scalar) {
        return new Vector3D(
                vector1.x * scalar,
                vector1.y * scalar,
                vector1.z * scalar
        );
    }

    public void crossProduct(Vector3D crossElement) {
        this.x = this.y * crossElement.z - crossElement.y * this.z;
        this.y = this.x * crossElement.z - crossElement.x * this.z;
        this.z = this.x * crossElement.y - crossElement.x * this.y;
        setMagnitude();
    }

    public static Vector3D crossProduct(Vector3D vector1, Vector3D vector2) {
        return new Vector3D(
                vector1.y * vector2.z - vector2.y * vector1.z,
                vector1.x * vector2.z - vector2.x * vector1.z,
                vector1.x * vector2.y - vector2.x * vector1.y
        );
    }

    public static double dotProduct(Vector3D vector1, Vector3D vector2){
        return (vector1.getX() * vector2.getX()) + (vector1.getY() * vector2.getY()) + (vector1.getZ() * vector2.getZ());
    }

    public static Vector3D normalize(Vector3D vector1){
        return new Vector3D(vector1.getX() / vector1.magnitude, vector1.getY() / vector1.magnitude, vector1.getZ() / vector1.magnitude);
    }

    public Vector3D clone() {
        return new Vector3D(getX(), getY(), getZ());
    }

    public static Vector3D ZERO() {
        return ZERO.clone();
    }

    @Override
    public String toString() {
        return  "<" + getX() + ", " + getY() + ", " + getZ() + ">";
    }
}

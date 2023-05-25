package com.vro.personalraytracer.tools;

/**
 * Vector3D class. Gives you room to use Vectors in a 3D spacio and operate them.
 */
public class Vector3D {
    private double x;
    private double y;
    private double z;
    private double magnitude;

    /**
     * Instantiates a new Vector3D.
     */
    public Vector3D() {
        setVector3D(0.0, 0.0, 0.0);
    }

    /**
     * Instantiates a new Vector3D.
     *
     * @param x the x
     * @param y the y
     * @param z the z
     */
    public Vector3D(double x, double y, double z) {
        setVector3D(x, y, z);
    }

    /**
     * Instantiates a new Vector3D.
     *
     * @param vector the vector
     */
    public Vector3D(Vector3D vector) {
        setVector3D(vector);
    }

    /**
     * Sets Vector3D.
     *
     * @param x the x
     * @param y the y
     * @param z the z
     */
    public void setVector3D(double x, double y, double z) {
        setX(x);
        setY(y);
        setZ(z);
        setMagnitude();
    }

    /**
     * Sets Vector3D.
     *
     * @param vector the vector
     */
    public void setVector3D(Vector3D vector) {
        setX(vector.x);
        setY(vector.y);
        setZ(vector.z);
        setMagnitude();
    }

    /**
     * Sets x.
     *
     * @param x the x
     */
    public void setX(double x) {
        this.x = x;
        setMagnitude();
    }

    /**
     * Sets y.
     *
     * @param y the y
     */
    public void setY(double y) {
        this.y = y;
        setMagnitude();
    }

    /**
     * Sets z.
     *
     * @param z the z
     */
    public void setZ(double z) {
        this.z = z;
        setMagnitude();
    }

    /**
     * Gets x.
     *
     * @return the x
     */
    public double getX() {
        return x;
    }

    /**
     * Gets y.
     *
     * @return the y
     */
    public double getY() {
        return y;
    }

    /**
     * Gets z.
     *
     * @return the z
     */
    public double getZ() {
        return z;
    }

    /**
     * Sets magnitude.
     */
    public void setMagnitude() {
        magnitude = Math.sqrt(dotProduct(this, this));
    }

    /**
     * Gets magnitude.
     *
     * @return the magnitude
     */
    public double getMagnitude() {
        return magnitude;
    }

    /**
     * Dot product double.
     *
     * @param v1 the v 1
     * @param v2 the v 2
     * @return the double
     */
    public static double dotProduct(Vector3D v1, Vector3D v2) {
        return (v1.getX() * v2.getX()) + (v1.getY() * v2.getY()) + (v1.getZ() * v2.getZ());
    }

    /**
     * Cross product Vector3D.
     *
     * @param v1 the v 1
     * @param v2 the v 2
     * @return the Vector3D
     */
    public static Vector3D crossProduct(Vector3D v1, Vector3D v2) {
        return new Vector3D(
                (v1.getY() * v2.getZ()) - (v1.getZ() * v2.getY()),
                (v1.getZ() * v2.getX()) - (v1.getX() * v2.getZ()),
                (v1.getX() * v2.getY()) - (v1.getY() * v2.getX())
        );
    }

    /**
     * Vector addition Vector3D.
     *
     * @param v1 the v 1
     * @param v2 the v 2
     * @return the Vector3D
     */
    public static Vector3D vectorAddition(Vector3D v1, Vector3D v2) {
        return new Vector3D(v1.getX() + v2.getX(), v1.getY() + v2.getY(), v1.getZ() + v2.getZ());
    }

    /**
     * Add.
     *
     * @param v2 the v 2
     */
    public void add(Vector3D v2) {
        this.x += v2.x;
        this.y += v2.y;
        this.z += v2.z;
    }

    /**
     * Vector substraction Vector3D.
     *
     * @param v1 the v 1
     * @param v2 the v 2
     * @return the Vector3D
     */
    public static Vector3D vectorSubstraction(Vector3D v1, Vector3D v2) {
        return new Vector3D(v1.getX() - v2.getX(), v1.getY() - v2.getY(), v1.getZ() - v2.getZ());
    }

    /**
     * Substract.
     *
     * @param v2 the v 2
     */
    public void substract(Vector3D v2) {
        this.x -= v2.x;
        this.y -= v2.y;
        this.z -= v2.z;
    }

    /**
     * Scalar multiplication Vector3D.
     *
     * @param v1     the v 1
     * @param scalar the scalar
     * @return the Vector3D
     */
    public static Vector3D scalarMultiplication(Vector3D v1, double scalar) {
        return new Vector3D(v1.getX() * scalar, v1.getY() * scalar, v1.getZ() * scalar);
    }

    /**
     * Multiply.
     *
     * @param scalar the scalar
     */
    public void multiply(double scalar) {
        this.x *= scalar;
        this.y *= scalar;
        this.z *= scalar;
    }

    /**
     * Normalize Vector3D.
     *
     * @param v1 the v 1
     * @return the Vector3D
     */
    public static Vector3D normalize(Vector3D v1) {
        double mag = v1.getMagnitude();
        return new Vector3D(v1.getX() / mag, v1.getY() / mag, v1.getZ() / mag);
    }

    @Override
    public String toString() {
        return "<" + getX() + ", " + getY() + ", " + getZ() + ">";
    }
}


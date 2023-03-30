package com.vr.raytracer.tools;

public class Vector3D {
    private double x;
    private double y;
    private double z;

    public Vector3D(double x, double y, double z) {
        setVector3D(x, y, z);
    }

    public Vector3D getVector3D() {
        return new Vector3D(x, y, z);
    }

    public void setVector3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3D vectorAddition(Vector3D initialVector, Vector3D vectorAdded){
        return new Vector3D(initialVector.x + vectorAdded.x, initialVector.y + vectorAdded.y, initialVector.z + vectorAdded.z);

    }
}

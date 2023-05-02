package com.vr.raytracer.tools;

import com.vr.raytracer.objects.Camera;
import com.vr.raytracer.objects.Object3D;

import java.util.ArrayList;
import java.util.List;

public class Scene {

    private Camera camera;
    private List<Object3D> objects;

    public Scene() {
        setObjects(new ArrayList<>());
    }

    public Camera getCamera() {
        return camera;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public void addObject(Object3D object){
        getObjects().add(object);
    }

    public List<Object3D> getObjects() {
        if(objects == null){
            objects = new ArrayList<>();
        }
        return objects;
    }

    public void setObjects(List<Object3D> objects) {
        this.objects = objects;
    }
}


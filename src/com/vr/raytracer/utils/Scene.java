package com.vr.raytracer.utils;

import com.vr.raytracer.objects.Camera;
import com.vr.raytracer.objects.Object3D;

import java.util.ArrayList;
import java.util.List;

public class Scene {
    private Camera camera;
    private List<Object3D> sceneObjects;

    public Scene() {
        setSceneObjects(new ArrayList<>());
    }

    public Camera getCamera() {
        return camera;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public List<Object3D> getSceneObjects() {
        if(sceneObjects == null) {
            sceneObjects = new ArrayList<>();
        }
        return sceneObjects;
    }

    public void setSceneObjects(List<Object3D> sceneObjects) {
        this.sceneObjects = sceneObjects;
    }

    public void addObject(Object3D object) {
        getSceneObjects().add(object);
    }
}

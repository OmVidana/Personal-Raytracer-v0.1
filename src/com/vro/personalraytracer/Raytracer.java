package com.vro.personalraytracer;

import com.vro.personalraytracer.objects.*;
import com.vro.personalraytracer.objects.lights.DirectionalLight;
import com.vro.personalraytracer.objects.lights.Light;
import com.vro.personalraytracer.objects.lights.PointLight;
import com.vro.personalraytracer.tools.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * The type Raytracer.
 */
public class Raytracer {
    /**
     * The constant renderPath.
     */
    public static final String renderPath = "src/com/vro/personalraytracer/renders/";

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        System.out.println(new Date());

        Scene selectedScene = sceneManager();

        BufferedImage image = raytrace(selectedScene);
        File outputImage = new File(renderPath + "shadowBox1.png");
        try {
            ImageIO.write(image, "png", outputImage);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(new Date());
    }

    /**
     * Raytrace buffered image.
     *
     * @param scene the scene
     * @return the buffered image
     */
    public static BufferedImage raytrace(Scene scene) {
        Camera mainCamera = scene.getCamera();
        double[] nearFarPlanes = mainCamera.getNearFarPlanes();
//        double cameraZ = mainCamera.getPosition().getZ();
        BufferedImage image = new BufferedImage(mainCamera.getWidth(), mainCamera.getHeight(), BufferedImage.TYPE_INT_RGB);
        java.util.List<Object3D> objects = scene.getObjects();
        java.util.List<Light> lights = scene.getLights();

        Vector3D[][] positionsToRaytrace = mainCamera.calculatePositionsToRay();
        for (int i = 0; i < positionsToRaytrace.length; i++) {
            for (int j = 0; j < positionsToRaytrace[i].length; j++) {
                double x = positionsToRaytrace[i][j].getX() + mainCamera.getPosition().getX();
                double y = positionsToRaytrace[i][j].getY() + mainCamera.getPosition().getY();
                double z = positionsToRaytrace[i][j].getZ() + mainCamera.getPosition().getZ();

                Ray ray = new Ray(mainCamera.getPosition(), new Vector3D(x, y, z));
                Intersection closestIntersection = raycast(ray, objects, null, new double[]{nearFarPlanes[0], nearFarPlanes[1]});

                Color pixelColor = Color.BLACK;
                if (closestIntersection != null) {
                    Color objColor = closestIntersection.getObject().getColor();

                    boolean insideShadow = false;
                    for (Light light : lights) {
                        double nDotL = light.getNDotL(closestIntersection);
                        double intensity = light.getIntensity() * nDotL;
                        double lightDistance = Vector3D.vectorSubstraction(light.getPosition(), closestIntersection.getPosition()).getMagnitude();
                        Color lightColor = light.getColor();

                        double[] lightColors = new double[]{lightColor.getRed() / 255.0, lightColor.getGreen() / 255.0, lightColor.getBlue() / 255.0};
                        double[] objColors = new double[]{objColor.getRed() / 255.0, objColor.getGreen() / 255.0, objColor.getBlue() / 255.0};

                        for (int colorIndex = 0; colorIndex < objColors.length; colorIndex++) {
                            objColors[colorIndex] *= (intensity / Math.pow(lightDistance, 2)) * lightColors[colorIndex];
                        }

                        Color diffuse = new Color(clamp(objColors[0], 0, 1), clamp(objColors[1], 0, 1), clamp(objColors[2], 0, 1));
                        pixelColor = addColor(pixelColor, diffuse);
                    }
                }

                image.setRGB(i, j, pixelColor.getRGB());
            }
        }

        return image;
    }

    /**
     * Clamp float.
     *
     * @param value the value
     * @param min   the min
     * @param max   the max
     * @return the float
     */
    public static float clamp(double value, double min, double max) {
        if (value < min) {
            return (float) min;
        }
        if (value > max) {
            return (float) max;
        }
        return (float) value;
    }

    /**
     * Add color color.
     *
     * @param original   the original
     * @param otherColor the other color
     * @return the color
     */
    public static Color addColor(Color original, Color otherColor) {
        float red = clamp((original.getRed() / 255.0) + (otherColor.getRed() / 255.0), 0, 1);
        float green = clamp((original.getGreen() / 255.0) + (otherColor.getGreen() / 255.0), 0, 1);
        float blue = clamp((original.getBlue() / 255.0) + (otherColor.getBlue() / 255.0), 0, 1);
        return new Color(red, green, blue);
    }

    /**
     * Raycast intersection.
     *
     * @param ray            the ray
     * @param objects        the objects
     * @param caster         the caster
     * @param clippingPlanes the clipping planes
     * @return the intersection
     */
    public static Intersection raycast(Ray ray, List<Object3D> objects, Object3D caster, double[] clippingPlanes) {
        Intersection closestIntersection = null;

        for (Object3D currentObj : objects) {
            if (caster == null || !currentObj.equals(caster)) {
                Intersection intersection = currentObj.getIntersection(ray);
                if (intersection != null) {
                    double distance = intersection.getDistance();
                    double intersectionZ = intersection.getPosition().getZ();
                    if (distance >= 0 &&
                            (closestIntersection == null || distance < closestIntersection.getDistance()) &&
                            (clippingPlanes == null || (intersectionZ >= clippingPlanes[0] && intersectionZ <= clippingPlanes[1]))) {
                        closestIntersection = intersection;
                    }
                }
            }
        }

        return closestIntersection;
    }

    /**
     * Scene manager scene.
     *
     * @return the scene
     */
    public static Scene sceneManager() {
        Scene finalScene;

        Scene scene01 = new Scene();
        scene01.setCamera(new Camera(new Vector3D(0, 0, -4), 300, 300, 90, 90, 0.6, 50.0));
        scene01.addLight(new DirectionalLight(new Vector3D(0,1,0), new Vector3D(0, 1, 1), Color.WHITE, 20));
        scene01.addLight(new PointLight(new Vector3D(0, 3, 0), Color.WHITE, 10));
        scene01.addLight(new PointLight(new Vector3D(0, 3, 8), Color.WHITE, 15));
        scene01.addObject(OBJReader.getModel3D("BowlingFloor.obj", new Vector3D(0,-2,0), new Vector3D(1,1,1), new Color(180,100,45)));
        scene01.addObject(OBJReader.getModel3D("BowlingWall.obj", new Vector3D(0,-2,12.5), new Vector3D(1,1,1), new Color(11,1,74)));
        scene01.addObject(OBJReader.getModel3D("BowlingBall.obj", new Vector3D(1.25,-2,2.5), new Vector3D(1,1,1), Color.WHITE));
        scene01.addObject(OBJReader.getModel3D("BowlingPin.obj", new Vector3D(0,-3,10), new Vector3D(1,1,1), Color.WHITE));
//        scene01.addObject(new Sphere(new Vector3D(-2,0,2),1, new Vector3D(2,1,1), Color.BLUE));
        Scene scene02 = new Scene();
        scene02.setCamera(new Camera(new Vector3D(0, 0, -4), 300, 300, 90, 90, 0.6, 50.0));
//        scene02.addLight(new PointLight(new Vector3D(0, -10, 5.5), Color.WHITE, 20));
        scene02.addLight(new PointLight(new Vector3D(0, -10, 5.5), Color.WHITE, 20));
//        scene02.addLight(new DirectionalLight(new Vector3D(0, -10, 0), new Vector3D(0, -10, 5.5), Color.WHITE, 30));
        scene02.addObject(OBJReader.getModel3D("Cube.obj", new Vector3D(0,-0.5,6), new Vector3D(1,1,1), Color.BLUE));
        scene02.addObject(OBJReader.getModel3D("Floor.obj", new Vector3D(0,-1,6), new Vector3D(1.5,1.5,1.5), Color.YELLOW));
        scene02.addObject(OBJReader.getModel3D("Container.obj", new Vector3D(0,-4,5), new Vector3D(1.5,1.5,1.5), Color.DARK_GRAY));
        scene02.addObject(OBJReader.getModel3D("BowlingPin.obj", new Vector3D(0,-8,7), new Vector3D(1,1,1), Color.GREEN));

        Scene scene03 = new Scene();
        scene03.setCamera(new Camera(new Vector3D(0, 0, -4), 200, 200, 60, 60, 0.6, 50.0));
        scene03.addLight(new PointLight(new Vector3D(0, 2, 3.5), Color.WHITE, 10));
        scene03.addLight(new PointLight(new Vector3D(0, -1, 2), Color.WHITE, 10));
        scene03.addObject(OBJReader.getModel3D("Floor.obj", new Vector3D(0,-2,5), new Vector3D(1.5,1.5,1.5), Color.YELLOW));
        scene03.addObject(OBJReader.getModel3D("BowlingBall.obj", new Vector3D(0,-2,5), new Vector3D(0.5,0.5,0.5), Color.BLUE));


        finalScene = scene01;
        return finalScene;
    }
}

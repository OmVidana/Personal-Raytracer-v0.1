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
        File outputImage = new File(renderPath + "testDefZ.png");
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

                    for (Light light : lights) {
                        double nDotL = light.getNDotL(closestIntersection);
                        double intensity = light.getIntensity() * nDotL;
                        Color lightColor = light.getColor();

                        double[] lightColors = new double[]{lightColor.getRed() / 255.0, lightColor.getGreen() / 255.0, lightColor.getBlue() / 255.0};
                        double[] objColors = new double[]{objColor.getRed() / 255.0, objColor.getGreen() / 255.0, objColor.getBlue() / 255.0};
                        for (int colorIndex = 0; colorIndex < objColors.length; colorIndex++) {
                            objColors[colorIndex] *= intensity * lightColors[colorIndex];
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
        scene01.setCamera(new Camera(new Vector3D(0, 0, -4), 500, 500, 90, 90, 0.6, 50.0));
        scene01.addLight(new DirectionalLight(new Vector3D(), new Vector3D(0.0, 0.0, 1.0), Color.WHITE, 2.2));

        scene01.addObject(new Sphere(new Vector3D(0.5, 1, 8), 0.8, Color.RED));
        scene01.addObject(new Sphere(new Vector3D(0.1, 1, 6), 0.5, Color.BLUE));
        scene01.addObject(new Model3D(new Vector3D(-1, -1, 3),
                new Triangle[]{
                        new Triangle(new Vector3D(), new Vector3D(1, 0, 0), new Vector3D(1, -1, 0)),
                        new Triangle(new Vector3D(), new Vector3D(1, -1, 0), new Vector3D(0, -1, 0))},
                Color.GREEN));
        scene01.addObject(OBJReader.getModel3D("Cube.obj", new Vector3D(0, -2.5, 1), Color.CYAN));
        scene01.addObject(OBJReader.getModel3D("SmallTeapot.obj", new Vector3D(0, -2.5, 1), Color.CYAN));
        scene01.addObject(OBJReader.getModel3D("SmallTeapot.obj", new Vector3D(2, -2.5, 25), Color.BLUE));

        Scene scene02 = new Scene();
        scene02.setCamera(new Camera(new Vector3D(0, 0, -4), 800, 800, 60, 60, 0.6, 50.0));
        scene02.addLight(new DirectionalLight(new Vector3D(), new Vector3D(0.0, 0.0, 1.0), Color.WHITE, 0.5));
        scene02.addLight(new DirectionalLight(new Vector3D(), new Vector3D(0.0, -0.1, 0.1), Color.WHITE, 0.2));
        scene02.addLight(new DirectionalLight(new Vector3D(), new Vector3D(-0.2, -0.1, 0.0), Color.WHITE, 0.2));
        scene02.addObject(new Sphere(new Vector3D(0.0, 1.0, 5.0), 0.5, Color.RED));
        scene02.addObject(new Sphere(new Vector3D(0.5, 1.0, 4.5), 0.25, new Color(200, 255, 0)));
        scene02.addObject(new Sphere(new Vector3D(0.35, 1.0, 4.5), 0.3, Color.BLUE));
        scene02.addObject(new Sphere(new Vector3D(4.85, 1.0, 4.5), 0.3, Color.PINK));
        scene02.addObject(new Sphere(new Vector3D(2.85, 1.0, 304.5), 0.5, Color.BLUE));
        scene02.addObject(OBJReader.getModel3D("Cube.obj", new Vector3D(0f, -2.5, 1.0), Color.WHITE));
        scene02.addObject(OBJReader.getModel3D("CubeQuad.obj", new Vector3D(-3.0, -2.5, 3.0), Color.GREEN));
        scene02.addObject(OBJReader.getModel3D("SmallTeapot.obj", new Vector3D(2.0, -1.0, 1.5), Color.BLUE));
        scene02.addObject(OBJReader.getModel3D("Ring.obj", new Vector3D(2.0, -1.0, 1.5), Color.BLUE));

        Scene scene03 = new Scene();
        scene03.setCamera(new Camera(new Vector3D(0, 0, -4), 500, 500, 60, 60, 0.6, 50));
        scene03.addLight(new PointLight(new Vector3D(0, 0, 5), Color.WHITE, 0.7));
        scene03.addObject(new Sphere(new Vector3D(-2, 0, 5), 0.3, Color.BLUE));
        scene03.addObject(new Sphere(new Vector3D(2, 0, 5), 0.3, Color.PINK));
        scene03.addObject(new Sphere(new Vector3D(0, 0, 10), 0.5, Color.BLUE));

        finalScene = scene01;
        return finalScene;
    }
}

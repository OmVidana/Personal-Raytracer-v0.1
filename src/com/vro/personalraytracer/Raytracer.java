package com.vro.personalraytracer;

import com.vro.personalraytracer.materials.BlinnPhongShading;
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
import java.util.Objects;

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
                        double lightFallOff = (intensity / Math.pow(lightDistance, 2));
                        double[] lightColors = new double[]{lightColor.getRed() / 255.0, lightColor.getGreen() / 255.0, lightColor.getBlue() / 255.0};
                        double[] objColors = new double[]{objColor.getRed() / 255.0, objColor.getGreen() / 255.0, objColor.getBlue() / 255.0};

                        for (Object3D object : objects) {
                            if (!object.equals(closestIntersection.getObject())) {
                                Ray shadowRay = new Ray(closestIntersection.getPosition(), Vector3D.normalize(Vector3D.vectorSubstraction(light.getPosition(), closestIntersection.getPosition())));
                                Intersection shadowIntersection = object.getIntersection(shadowRay);
                                if (shadowIntersection != null && shadowIntersection.getDistance() > 0 && shadowIntersection.getDistance() < closestIntersection.getDistance()) {
                                    insideShadow = true;
                                    break;
                                }
                            }
                        }

                        if (!insideShadow) {
                            for (int colorIndex = 0; colorIndex < objColors.length; colorIndex++) {
                                objColors[colorIndex] *= lightFallOff * ColorsHandler.addColor(ColorsHandler.addColor(closestIntersection.getObject().getMaterial().getAmbient(closestIntersection, 0.02), closestIntersection.getObject().getMaterial().getDiffuse(closestIntersection, light, 0.25)), closestIntersection.getObject().getMaterial().getSpecular(closestIntersection, mainCamera.getPosition(), light, 0.75, 100)).getRed() * lightColors[colorIndex];
                            }

                            Color diffuse = new Color(ColorsHandler.clamp(objColors[0], 0, 1), ColorsHandler.clamp(objColors[1], 0, 1), ColorsHandler.clamp(objColors[2], 0, 1));
                            pixelColor = ColorsHandler.addColor(pixelColor, diffuse);
                        }
                    }
                }

                image.setRGB(i, j, pixelColor.getRGB());
            }
        }

        return image;
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
        scene01.setCamera(new Camera(new Vector3D(0, 0, -4), 640, 360, 90, 60, 0.6, 50.0));
//        scene01.addLight(new DirectionalLight(new Vector3D(0, 1, 0), new Vector3D(0, 1, 1), Color.WHITE, 1));
        scene01.addLight(new PointLight(new Vector3D(4, 3, 7), Color.WHITE, 0.4));
        scene01.addLight(new PointLight(new Vector3D(-4, 3, 7), Color.WHITE, 0.4));
        scene01.addObject(OBJReader.getModel3D("BowlingFloor.obj", new Vector3D(0, -2, 0), new Vector3D(1, 1, 1), new Color(180, 100, 45)));
        scene01.addObject(OBJReader.getModel3D("BowlingWall.obj", new Vector3D(0, -2, 12.5), new Vector3D(1, 1, 1), new Color(11, 1, 74)));
        scene01.addObject(OBJReader.getModel3D("BowlingBall.obj", new Vector3D(1.25, -2, 2.5), new Vector3D(1, 1, 1), Color.WHITE));
        scene01.addObject(OBJReader.getModel3D("BowlingPin.obj", new Vector3D(0, -3, 10), new Vector3D(1, 1, 1), Color.WHITE));
        Scene scene02 = new Scene();
        scene02.setCamera(new Camera(new Vector3D(0, 0, -4), 300, 300, 90, 90, 0.6, 50.0));
        scene02.addLight(new PointLight(new Vector3D(-3, 3, 7), Color.WHITE, 0.9));
//        scene02.addLight(new PointLight(new Vector3D(0, -8, 8), Color.WHITE, 0.9));
        scene02.addLight(new DirectionalLight(new Vector3D(0, -10, 0), new Vector3D(0, -10, 8), Color.WHITE, 0.7));
        scene02.addObject(OBJReader.getModel3D("BowlingBall.obj", new Vector3D(0, 0, 6.5), new Vector3D(0.5, 0.5, 0.5), Color.BLUE));
        scene02.addObject(OBJReader.getModel3D("Floor.obj", new Vector3D(0, -1, 6), new Vector3D(1.5, 1.5, 1.5), Color.YELLOW));
        scene02.addObject(OBJReader.getModel3D("Container.obj", new Vector3D(0, -4, 5), new Vector3D(1.5, 1.5, 1.5), Color.DARK_GRAY));
        scene02.addObject(OBJReader.getModel3D("BowlingPin.obj", new Vector3D(0, -8, 7), new Vector3D(1, 1, 1), Color.GREEN));

        Scene scene03 = new Scene();
        scene03.setCamera(new Camera(new Vector3D(0, 0, -4), 300, 300, 60, 60, 0.6, 50.0));
//        scene03.addLight(new PointLight(new Vector3D(3, 2, 3.5), Color.WHITE, 0.4));
//        scene03.addLight(new PointLight(new Vector3D(2, 2, 3), Color.WHITE, 0.1));
        scene03.addLight(new PointLight(new Vector3D(0, 0, 1), Color.WHITE, 0.3));

        Object3D bowlingFloor = OBJReader.getModel3D("Floor.obj", new Vector3D(0, -1, 4), new Vector3D(1.5, 1.5, 1.5), Color.YELLOW);

        Objects.requireNonNull(bowlingFloor).setMaterial(new BlinnPhongShading(0.20, 0.05, 0.12, 100, false));
        scene03.addObject(bowlingFloor);

        Object3D bowlingBall = OBJReader.getModel3D("BowlingBall.obj", new Vector3D(0, 0, 3), new Vector3D(0.75, 0.75, 0.75), Color.BLUE);
        Objects.requireNonNull(bowlingBall).setMaterial(new BlinnPhongShading(0.20, 0.05, 0.12, 100, true));
        scene03.addObject(bowlingBall);

        Object3D wall = OBJReader.getModel3D("VanDerWaal.obj", new Vector3D(0,0, 4), new Vector3D(2, 2, 2), Color.RED);
        Objects.requireNonNull(wall).setMaterial(new BlinnPhongShading(0.20, 0.05, 0.12, 100, true));
        scene03.addObject(wall);
        finalScene = scene03;
        return finalScene;
    }
}

package com.vro.personalraytracer;

import com.vro.personalraytracer.materials.BlinnPhongShading;
import com.vro.personalraytracer.objects.Camera;
import com.vro.personalraytracer.objects.Object3D;
import com.vro.personalraytracer.objects.Sphere;
import com.vro.personalraytracer.objects.lights.DirectionalLight;
import com.vro.personalraytracer.objects.lights.Light;
import com.vro.personalraytracer.objects.lights.PointLight;
import com.vro.personalraytracer.tools.*;

import java.awt.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * The type Parallel rt.
 */
public class ParallelRT {
    /**
     * The constant renderPath.
     */
    public static final String renderPath = "src/com/vro/personalraytracer/renders/";
    /**
     * The constant render.
     */
    public static BufferedImage render;

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {

        System.out.println(new Date());

        Scene selectedScene = sceneManager();
        render = new BufferedImage(selectedScene.getCamera().getWidth(), selectedScene.getCamera().getHeight(), BufferedImage.TYPE_INT_RGB);

        parallelMethod(selectedScene);

        File outputImage = new File(renderPath + "OmarVidaña_Render.png");
        try {
            ImageIO.write(render, "png", outputImage);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(new Date());
    }

    /**
     * Parallel method.
     *
     * @param scene the scene
     */
    public static void parallelMethod(Scene scene) {
        Camera mainCamera = scene.getCamera();
        double[] nearFarPlanes = mainCamera.getNearFarPlanes();
        List<Object3D> objects = scene.getObjects();
        List<Light> lights = scene.getLights();

        Vector3D[][] positionsToRaytrace = mainCamera.calculatePositionsToRay();

        ExecutorService executorService = Executors.newFixedThreadPool(6);
        for (int x = 0; x < render.getWidth(); x++) {
            for (int y = 0; y < render.getHeight(); y++) {
                Runnable runnable = raytrace(x, y, mainCamera, nearFarPlanes, objects, lights, positionsToRaytrace, 3);
                executorService.execute(runnable);
            }
        }
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(180, TimeUnit.MINUTES)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            if (!executorService.isTerminated()) {
                System.err.println("Cancel non-finished");
            }
        }
        executorService.shutdownNow();
    }

    /**
     * Raytrace runnable.
     *
     * @param i                   the
     * @param j                   the j
     * @param mainCamera          the main camera
     * @param nearFarPlanes       the near far planes
     * @param objects             the objects
     * @param lights              the lights
     * @param positionsToRaytrace the positions to raytrace
     * @param depth               the depth
     * @return the runnable
     */
    public static Runnable raytrace(int i, int j, Camera mainCamera, double[] nearFarPlanes, List<Object3D> objects, List<Light> lights, Vector3D[][] positionsToRaytrace, int depth) {

        return new Runnable() {
            @Override
            public void run() {
                Color pixelColor = traceRay(depth);
                setRGB(i, j, pixelColor.getRGB());
            }

            private Color traceRay(int depthRecursive) {
                double x = positionsToRaytrace[i][j].getX() + mainCamera.getPosition().getX();
                double y = positionsToRaytrace[i][j].getY() + mainCamera.getPosition().getY();
                double z = positionsToRaytrace[i][j].getZ() + mainCamera.getPosition().getZ();

                Ray ray = new Ray(mainCamera.getPosition(), new Vector3D(x, y, z));
                Intersection closestIntersection = raycast(ray, objects, null, nearFarPlanes);

                Color pixelColor = Color.BLACK;
                if (closestIntersection != null) {
                    Color objColor = closestIntersection.getObject().getColor();

                    boolean insideShadow = false;
                    for (Light light : lights) {
                        double nDotL = light.getNDotL(closestIntersection);
                        double intensity = light.getIntensity() * nDotL;
                        double lightDistance = Vector3D.vectorSubstraction(light.getPosition(), closestIntersection.getPosition()).getMagnitude();
                        Color lightColor = light.getColor();
                        double lightFallOff = (intensity / Math.pow(lightDistance, 3));
                        double[] lightColors = new double[]{lightColor.getRed() / 255.0, lightColor.getGreen() / 255.0, lightColor.getBlue() / 255.0};
                        double[] objColors = new double[]{objColor.getRed() / 255.0, objColor.getGreen() / 255.0, objColor.getBlue() / 255.0};

                        //Values for BlinnPhongShading
                        Vector3D N = closestIntersection.getNormal();
                        Vector3D P = closestIntersection.getPosition();
                        Vector3D L = Vector3D.normalize(Vector3D.vectorSubstraction(light.getPosition(), closestIntersection.getPosition()));
                        Vector3D V = Vector3D.normalize(Vector3D.vectorSubstraction(P, mainCamera.getPosition()));
                        Vector3D H = Vector3D.normalize(Vector3D.vectorAddition(V,L));

                        for (Object3D object : objects) {
                            if (!object.equals(closestIntersection.getObject())) {
                                Ray shadowRay = new Ray(closestIntersection.getPosition(), Vector3D.vectorAddition(Vector3D.normalize(Vector3D.vectorSubstraction(light.getPosition(), closestIntersection.getPosition())), Vector3D.scalarMultiplication(Vector3D.normalize(Vector3D.vectorSubstraction(light.getPosition(), closestIntersection.getPosition())), 0.001)));
                                Intersection shadowIntersection = object.getIntersection(shadowRay);
                                if (shadowIntersection != null && shadowIntersection.getDistance() > 0 && shadowIntersection.getDistance() < closestIntersection.getDistance()) {
                                    insideShadow = true;
                                    break;
                                }
                            }
                        }

                        if (!insideShadow) {
                            for (int colorIndex = 0; colorIndex < objColors.length; colorIndex++) {
                                objColors[colorIndex] *= lightFallOff * ColorsHandler.addColor(ColorsHandler.addColor(closestIntersection.getObject().getMaterial().getAmbient(closestIntersection), closestIntersection.getObject().getMaterial().getDiffuse(closestIntersection, light)), closestIntersection.getObject().getMaterial().getSpecular(closestIntersection, light, N, H)).getRed() * lightColors[colorIndex];
                            }

                            Color diffuse = new Color(ColorsHandler.clamp(objColors[0], 0, 1), ColorsHandler.clamp(objColors[1], 0, 1), ColorsHandler.clamp(objColors[2], 0, 1));
                            pixelColor = ColorsHandler.addColor(pixelColor, diffuse);
                        }

                        // Reflection
                        if (depthRecursive > 0 && closestIntersection.getObject().getMaterial().isReflective()) {
                            Vector3D reflectDirection = BlinnPhongShading.directionReflection(N, V);
                            Ray reflectionRay = new Ray(closestIntersection.getPosition(), Vector3D.vectorAddition(reflectDirection, Vector3D.scalarMultiplication(reflectDirection,0.001)));
                            Intersection reflectionIntersection = raycast(reflectionRay, objects, closestIntersection.getObject(), nearFarPlanes);

                            if (reflectionIntersection != null) {
                                Color reflection = traceRay(depthRecursive - 1);
                                pixelColor = ColorsHandler.addColor(pixelColor, reflection);
                            }
                        }
                    }
                }
                return pixelColor;
            }
        };
    }

    /**
     * Sets rgb.
     *
     * @param x          the x
     * @param y          the y
     * @param pixelColor the pixel color
     */
    public static synchronized void setRGB(int x, int y, int pixelColor) {
        render.setRGB(x, y, pixelColor);
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
        scene01.setCamera(new Camera(new Vector3D(0, 0, -4), 160, 90, 90, 60, 0.6, 50.0));

        scene01.addLight(new PointLight(new Vector3D(0, 4, 9), Color.WHITE, 0.3));
        scene01.addLight(new PointLight(new Vector3D(4, 4, 6), Color.WHITE, 0.3));
        scene01.addLight(new PointLight(new Vector3D(-4, 4, 6), Color.WHITE, 0.3));
        scene01.addLight(new PointLight(new Vector3D(0, 4, 3), Color.WHITE, 0.3));

        Object3D bowlingFloor = OBJReader.getModel3D("BowlingFloor.obj", new Vector3D(0, -0.5, 1), new Vector3D(1, 1, 1), new Color(180, 100, 45));
        Objects.requireNonNull(bowlingFloor).setMaterial(new BlinnPhongShading(0.10, 0.03, 0.30, 60, true, false));
        scene01.addObject(bowlingFloor);

        Object3D bowlingWall = OBJReader.getModel3D("BowlingWall.obj", new Vector3D(0, -2, 12.5), new Vector3D(1, 1, 1), new Color(11, 1, 74));
        Objects.requireNonNull(bowlingWall).setMaterial(new BlinnPhongShading(0.20, 0.08, 0.22, 45, false, false));
        scene01.addObject(bowlingWall);

        Object3D bowlingBall = OBJReader.getModel3D("BowlingBall.obj", new Vector3D(1.25, 0, 2.5), new Vector3D(1, 1, 1), Color.WHITE);
        Objects.requireNonNull(bowlingBall).setMaterial(new BlinnPhongShading(0.3, 0.15, 0.36, 45, false, false));
        scene01.addObject(bowlingBall);

        Object3D bowlingPin = OBJReader.getModel3D("BowlingPin.obj", new Vector3D(0, -3, 10), new Vector3D(1, 1, 1), Color.WHITE);
        Objects.requireNonNull(bowlingPin).setMaterial(new BlinnPhongShading(0.20, 0.05, 0.12, 45, true, false));
        scene01.addObject(bowlingPin);

        Object3D bowlingPinDecoration = OBJReader.getModel3D("BowlingPin.obj", new Vector3D(-10, -1, 10), new Vector3D(2, 2, 2), Color.WHITE);
        Objects.requireNonNull(bowlingPinDecoration).setMaterial(new BlinnPhongShading(0.20, 0.05, 0.24, 45, true, false));
        scene01.addObject(bowlingPinDecoration);

        Object3D bowlingPinDecoration2 = OBJReader.getModel3D("BowlingPin.obj", new Vector3D(10, -1, 10), new Vector3D(2, 2, 2), Color.WHITE);
        Objects.requireNonNull(bowlingPinDecoration2).setMaterial(new BlinnPhongShading(0.20, 0.05, 0.24, 60, true, false));
        scene01.addObject(bowlingPinDecoration2);

        Object3D bowlingCeiling = OBJReader.getModel3D("Ceiling.obj", new Vector3D(0, 8, 12), new Vector3D(12, 1, 12), Color.RED);
        Objects.requireNonNull(bowlingCeiling).setMaterial(new BlinnPhongShading(0.20, 0.05, 0.24, 45, false, false));
        scene01.addObject(bowlingCeiling);

        Scene scene02 = new Scene();
        scene02.setCamera(new Camera(new Vector3D(0, 0, -4), 370, 180, 90, 60, 0.5, 20.0));

        scene02.addLight(new PointLight(new Vector3D(0, 3, -2), Color.WHITE, 0.4));
        scene02.addLight(new PointLight(new Vector3D(0, 3, 16), Color.WHITE, 0.4));

        Object3D momCow = OBJReader.getModel3D("Cow.obj", new Vector3D(0, -1, 5), new Vector3D(1, 1, 1), new Color(30, 23, 9));
        Objects.requireNonNull(momCow).setMaterial(new BlinnPhongShading(0.20, 0.05, 0.24, 45, false, false));
        scene02.addObject(momCow);

        Object3D babyCow = OBJReader.getModel3D("Cow.obj", new Vector3D(2, -1, 5), new Vector3D(0.5, 0.5, 0.5), new Color(180, 160, 140));
        Objects.requireNonNull(babyCow).setMaterial(new BlinnPhongShading(0.20, 0.05, 0.24, 45, false, false));
        scene02.addObject(babyCow);

//        Object3D ufo = OBJReader.getModel3D("Ufo.obj", new Vector3D(0, 2, 1), new Vector3D(1, 1, 1), Color.LIGHT_GRAY);
//        Objects.requireNonNull(babyCow).setMaterial(new BlinnPhongShading(0.24, 0.07, 0.13, 55, true, false));
//        scene02.addObject(ufo);

        Object3D floor = OBJReader.getModel3D("Floor.obj", new Vector3D(0, -2, 5), new Vector3D(15, 1, 15), new Color(0, 45, 2));
        Objects.requireNonNull(bowlingFloor).setMaterial(new BlinnPhongShading(0.19, 0.11, 0.15, 60, false, false));
        scene02.addObject(bowlingFloor);

        Object3D hay = OBJReader.getModel3D("HayBale.obj", new Vector3D(-3, -1, 5), new Vector3D(1, 1, 1), new Color(154, 131, 14));
        Objects.requireNonNull(hay).setMaterial(new BlinnPhongShading(0.19, 0.11, 0.15, 60, false, false));
        scene02.addObject(hay);

        finalScene = scene02;
        return finalScene;
    }
}

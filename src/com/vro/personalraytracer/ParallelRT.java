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

        File outputImage = new File(renderPath + "OmarVidaña_Render2.png");
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

                        // Reflection and Refraction
                        if (depthRecursive > 0 && (closestIntersection.getObject().isReflective() || closestIntersection.getObject().isRefractive())) {
                            //Reflection
                            Vector3D reflectDirection = Vector3D.vectorAddition(Vector3D.scalarMultiplication(Vector3D.scalarMultiplication(N, Vector3D.dotProduct(V,N)), -2), V);
                            Ray reflectionRay = new Ray(P, Vector3D.vectorAddition(reflectDirection, Vector3D.scalarMultiplication(reflectDirection,0.001)));
                            Intersection reflectionIntersection = raycast(reflectionRay, objects, closestIntersection.getObject(), nearFarPlanes);

                            //Reflection
                            if (reflectionIntersection != null) {
                                Color reflection = traceRay(depthRecursive - 1);
                                pixelColor = ColorsHandler.addColor(pixelColor, ColorsHandler.multiply(reflection, closestIntersection.getObject().getReflectiveK()));
                            }
                            //Refraction
//                            if (refractionIntersection != null) {
//                                Color refraction = traceRay(depthRecursive - 1);
//                                pixelColor = ColorsHandler.addColor(pixelColor, ColorsHandler.multiply(refraction, (closestIntersection.getObject().getRefractiveK())));
//                            }
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
        scene01.setCamera(new Camera(new Vector3D(0, 0, -4), 198, 108, 90, 60, 0.5, 20.0));

        scene01.addLight(new PointLight(new Vector3D(-6,4,0), new Color(80, 90, 127), 0.55));
        scene01.addLight(new PointLight(new Vector3D(6,3,0), new Color(80, 90, 127), 0.6));

        Object3D momCow = OBJReader.getModel3D("CowY90.obj", new Vector3D(-1, -2, 1), new Vector3D(1, 1, 1), new Color(30, 23, 9));
        Objects.requireNonNull(momCow).setMaterial(new BlinnPhongShading(0.20, 0.1, 0.24, 45));
        momCow.setReflective(false);
        momCow.setReflectiveK(0);
        scene01.addObject(momCow);

        Object3D babyCow = OBJReader.getModel3D("CowY90.obj", new Vector3D(5, -2, 2.5), new Vector3D(0.5, 0.5, 0.5), new Color(180, 160, 140));
        Objects.requireNonNull(babyCow).setMaterial(new BlinnPhongShading(0.20, 0.15, 0.24, 90));
        momCow.setReflective(false);
        momCow.setReflectiveK(0);
        scene01.addObject(babyCow);

        Object3D floor = OBJReader.getModel3D("Floor.obj", new Vector3D(0, -2, 3.5), new Vector3D(30, 1, 30), new Color(0, 45, 2));
        Objects.requireNonNull(floor).setMaterial(new BlinnPhongShading(0.19, 0.11, 0.25, 80));
        momCow.setReflective(false);
        momCow.setReflectiveK(0);
        scene01.addObject(floor);

        Object3D sky = OBJReader.getModel3D("Wall.obj", new Vector3D(0, -2, 9), new Vector3D(25, 25, 1), new Color(2, 12, 79));
        Objects.requireNonNull(sky).setMaterial(new BlinnPhongShading(0.19, 0.11, 0.21, 80));
        sky.setReflective(false);
        sky.setReflectiveK(0);
        scene01.addObject(sky);

        Object3D hay = OBJReader.getModel3D("HayBaleY45.obj", new Vector3D(-8, -4, 1), new Vector3D(1, 1, 1), new Color(154, 131, 14));
        Objects.requireNonNull(hay).setMaterial(new BlinnPhongShading(0.19, 0.11, 0.15, 60));
        hay.setReflective(false);
        hay.setReflectiveK(0);
        scene01.addObject(hay);

        Object3D hay2 = OBJReader.getModel3D("HayBaleY45.obj", new Vector3D(10.5, -2, 5.5), new Vector3D(0.9, 0.6, 0.8), new Color(154, 131, 14));
        Objects.requireNonNull(hay2).setMaterial(new BlinnPhongShading(0.22, 0.17, 0.29, 60));
        hay2.setReflective(false);
        hay2.setReflectiveK(0);
        scene01.addObject(hay2);

        Object3D hay3 = OBJReader.getModel3D("HayBale.obj", new Vector3D(-8, -2, 7), new Vector3D(1, 1, 1), new Color(154, 131, 14));
        Objects.requireNonNull(hay3).setMaterial(new BlinnPhongShading(0.25, 0.14, 0.2, 60));
        hay3.setReflective(false);
        hay3.setReflectiveK(0);
        scene01.addObject(hay3);

        Object3D hay4 = OBJReader.getModel3D("HayBale.obj", new Vector3D(8, -2, 7), new Vector3D(1, 1, 1), new Color(154, 131, 14));
        Objects.requireNonNull(hay4).setMaterial(new BlinnPhongShading(0.19, 0.14, 0.21, 60));
        hay4.setReflective(false);
        hay4.setReflectiveK(0);
        scene01.addObject(hay4);

        Scene scene02 = new Scene();

        scene02.setCamera(new Camera(new Vector3D(0, 0, -4), 198, 108, 90, 60, 0.5, 20.0));

        scene02.addLight(new PointLight(new Vector3D(6,4.5,1), new Color(80, 90, 127), 0.55));
        scene02.addLight(new PointLight(new Vector3D(0,4,1), new Color(80, 90, 127), 0.55));
        scene02.addLight(new PointLight(new Vector3D(-6,4.5,1), new Color(80, 90, 127), 0.55));
        scene02.addLight(new PointLight(new Vector3D(0, 3, 8), new Color(80, 90, 127), 0.25));


        Object3D floor2 = OBJReader.getModel3D("Floor.obj", new Vector3D(0, -2, 3.5), new Vector3D(30, 1, 30), new Color(0, 45, 2));
        Objects.requireNonNull(floor2).setMaterial(new BlinnPhongShading(0.19, 0.11, 0.25, 80));
        floor2.setReflective(false);
        floor2.setReflectiveK(0);
        scene02.addObject(floor2);

        Object3D sky2 = OBJReader.getModel3D("Wall.obj", new Vector3D(0, -2, 9), new Vector3D(25, 25, 1), new Color(2, 12, 79));
        Objects.requireNonNull(sky2).setMaterial(new BlinnPhongShading(0.19, 0.11, 0.21, 80));
        sky2.setReflective(false);
        sky2.setReflectiveK(0);
        scene01.addObject(sky2);

        Object3D momCow2 = OBJReader.getModel3D("render02.obj", new Vector3D(-1, -1.5, 6), new Vector3D(0.75, 0.75, 0.75), new Color(30, 23, 9));
        Objects.requireNonNull(momCow2).setMaterial(new BlinnPhongShading(0.20, 0.1, 0.24, 45));
        momCow2.setReflective(false);
        momCow2.setReflectiveK(0);
        scene02.addObject(momCow2);

        Object3D babyCow2 = OBJReader.getModel3D("CowY90_2.obj", new Vector3D(6, -2, 2), new Vector3D(0.5, 0.5, 0.5), new Color(70, 30, 10));
        Objects.requireNonNull(babyCow2).setMaterial(new BlinnPhongShading(0.20, 0.15, 0.24, 90));
        babyCow2.setReflective(false);
        babyCow2.setReflectiveK(0);
        scene02.addObject(babyCow2);

        Object3D hayB = OBJReader.getModel3D("HayBaleY45.obj", new Vector3D(-6, -2, 2.5), new Vector3D(0.5, 0.5, 0.5), new Color(100, 100, 10));
        Objects.requireNonNull(hayB).setMaterial(new BlinnPhongShading(0.20, 0.15, 0.24, 120));
        hayB.setReflective(false);
        hayB.setReflectiveK(0);
        scene02.addObject(hayB);

        Object3D hayB2 = OBJReader.getModel3D("HayBaleY90.obj", new Vector3D(6, -2, 4), new Vector3D(0.5, 0.5, 0.5), new Color(100, 100, 10));
        Objects.requireNonNull(hayB2).setMaterial(new BlinnPhongShading(0.24, 0.19, 0.27, 120));
        hayB2.setReflective(false);
        hayB2.setReflectiveK(0);
        scene02.addObject(hayB2);

        Object3D ufo = OBJReader.getModel3D("Ufo.obj", new Vector3D(0, 4.5, 6), new Vector3D(0.5, 0.5, 0.5), new Color(175, 175, 45));
        Objects.requireNonNull(ufo).setMaterial(new BlinnPhongShading(0.20, 0.15, 0.19, 60));
        ufo.setReflective(true);
        ufo.setReflectiveK(0.48);
        scene02.addObject(ufo);

        Scene scene03 = new Scene();

        scene03.setCamera(new Camera(new Vector3D(0, 0, -4), 198, 108, 90, 60, 0.5, 24.0));

        scene03.addLight(new PointLight(new Vector3D(0, -5, 5.5), Color.WHITE, 0.6));
        scene03.addLight(new PointLight(new Vector3D(0, 1, 0), Color.LIGHT_GRAY, 0.45));

        Object3D ufo2 = OBJReader.getModel3D("Ufo.obj", new Vector3D(0, 4, 5), new Vector3D(1, 1, 1), new Color(159, 124, 64));
        Objects.requireNonNull(ufo2).setMaterial(new BlinnPhongShading(0.20, 0.13, 0.19, 65));
        ufo2.setReflective(true);
        ufo2.setReflectiveK(0.84);
        scene03.addObject(ufo2);

        Object3D cowFly = OBJReader.getModel3D("CowAbdused.obj", new Vector3D(0, -4.5, 8), new Vector3D(1, 1, 1), new Color(70, 30, 10));
        Objects.requireNonNull(cowFly).setMaterial(new BlinnPhongShading(0.20, 0.13, 0.19, 65));
        cowFly.setReflective(true);
        cowFly.setReflectiveK(0.75);
        scene03.addObject(cowFly);

        Object3D hayB3 = OBJReader.getModel3D("HayBaleY45.obj", new Vector3D(8, -4.5, 8), new Vector3D(1, 1, 1), new Color(153, 106, 11));
        Objects.requireNonNull(hayB3).setMaterial(new BlinnPhongShading(0.20, 0.13, 0.19, 65));
        hayB3.setReflective(true);
        hayB3.setReflectiveK(0.75);
        scene03.addObject(hayB3);

        Object3D hayB4 = OBJReader.getModel3D("HayBaleY90.obj", new Vector3D(-8, -4.5, 8), new Vector3D(1, 1, 1), new Color(153, 156, 11));
        Objects.requireNonNull(hayB4).setMaterial(new BlinnPhongShading(0.20, 0.13, 0.19, 65));
        hayB4.setReflective(true);
        hayB4.setReflectiveK(0.86);
        scene03.addObject(hayB4);

        finalScene = scene03;

        return finalScene;
    }
}

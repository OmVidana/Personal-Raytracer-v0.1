import com.vr.raytracer.objects.*;
import com.vr.raytracer.tools.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Date;
import java.util.List;

public class Raytracer {

    public static void main(String[] args) {
        System.out.println(new Date());

        Scene scene01 = new Scene();
        scene01.setCamera(new Camera(new Vector3D(0, 0, -4), 800, 800, 60, 60, 0.6, 50.0));
        scene01.addObject(new Sphere(new Vector3D(0.5, 1, 8), 0.8, Color.RED));
        scene01.addObject(new Sphere(new Vector3D(0.1, 1, 6), 0.5, Color.BLUE));
        scene01.addObject(new Model3D(new Vector3D(-1, -0.5, 3),
                new Triangle[]{
                        new Triangle(Vector3D.ZERO(), new Vector3D(1, 0, 0), new Vector3D(1, -1, 0)),
                        new Triangle(Vector3D.ZERO(), new Vector3D(1, -1, 0), new Vector3D(0, -1, 0))},
                Color.GREEN));
        //scene01.addObject(OBJReader.getModel3D("Cube.obj", new Vector3D(0, -2.5, 1), Color.CYAN));
        scene01.addObject(OBJReader.getModel3D("src/objs/" + "SmallTeapot.obj", new Vector3D(0, -2.5, 1), Color.CYAN));

        BufferedImage image = raytrace(scene01);
        File outputImage = new File("src/renders/" + "render.png");
        try {
            ImageIO.write(image, "png", outputImage);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(new Date());
    }

    public static BufferedImage raytrace(Scene scene) {
        Camera mainCamera = scene.getCamera();
        double[] nearFarPlanes = mainCamera.getNearFarPlanes();
        double cameraZ = mainCamera.getPosition().getZ();
        BufferedImage image = new BufferedImage(mainCamera.getWidth(), mainCamera.getHeight(), BufferedImage.TYPE_INT_RGB);
        List<Object3D> objects = scene.getObjects();

        Vector3D[][] positionsToRaytrace = mainCamera.calculatePositionsToRay();
        for (int i = 0; i < positionsToRaytrace.length; i++) {
            for (int j = 0; j < positionsToRaytrace[i].length; j++) {
                double x = positionsToRaytrace[i][j].getX() + mainCamera.getPosition().getX();
                double y = positionsToRaytrace[i][j].getY() + mainCamera.getPosition().getY();
                double z = positionsToRaytrace[i][j].getZ() + mainCamera.getPosition().getZ();

                Ray ray = new Ray(mainCamera.getPosition(), new Vector3D(x, y, z));
                Intersection closestIntersection = raycast(ray, objects, null, new double[]{cameraZ + nearFarPlanes[0], cameraZ + nearFarPlanes[1]});

                Color pixelColor = Color.BLACK;
                if (closestIntersection != null) {
                    pixelColor = closestIntersection.getObject().getColor();
                }
                image.setRGB(i, j, pixelColor.getRGB());
            }
        }

        return image;
    }

    public static Intersection raycast(Ray ray, List<Object3D> objects, Object3D caster, double[] clippingPlanes) {
        Intersection closestIntersection = null;

        for (int k = 0; k < objects.size(); k++) {
            Object3D currentObj = objects.get(k);
            if (!currentObj.equals(caster)) {
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

}

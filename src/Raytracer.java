import com.vr.raytracer.objects.Camera;
import com.vr.raytracer.objects.Object3D;
import com.vr.raytracer.objects.Sphere;
import com.vr.raytracer.utils.Intersection;
import com.vr.raytracer.utils.Ray;
import com.vr.raytracer.utils.Scene;
import com.vr.raytracer.utils.Vector3D;

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
        scene01.setCamera(new Camera(new Vector3D(0,0,-4), 100, 100, 60, 60));
        scene01.addObject(new Sphere(0.5,1,8,2, Color.RED));
        scene01.addObject(new Sphere(0.1,1,6,1, Color.BLUE));

        BufferedImage image = raytrace(scene01);
        File outputImage = new File( "src/renders/"+"image.png");
        try{
            ImageIO.write(image, "png", outputImage);
        } catch (Exception e){
            e.printStackTrace();
        }

        System.out.println(new Date());
    }

    public static BufferedImage raytrace(Scene scene) {
        Camera mainCamera = scene.getCamera();
        BufferedImage image = new BufferedImage(mainCamera.getWidth(), mainCamera.getHeight(), BufferedImage.TYPE_INT_RGB);
        List<Object3D> objects = scene.getSceneObjects();
        Vector3D[][] positionsToRaytrace = mainCamera.calculatePositionsToRay();
        for (int i = 0; i < positionsToRaytrace.length; i++) {
            for (int j = 0; j < positionsToRaytrace[i].length; j++) {
                double x = positionsToRaytrace[i][j].getX() + mainCamera.getPosition().getX();
                double y = positionsToRaytrace[i][j].getY() + mainCamera.getPosition().getY();
                double z = positionsToRaytrace[i][j].getZ() + mainCamera.getPosition().getZ();

                Ray ray = new Ray(mainCamera.getPosition(), new Vector3D(x, y, z));
                Intersection closestIntersection = raycast(ray, objects, null);

                Color pixelColor = Color.WHITE;
                if(closestIntersection != null){
                    pixelColor = closestIntersection.getObject().getColor();
                }
                image.setRGB(i, j, pixelColor.getRGB());
            }
        }

        return image;
    }

    public static Intersection raycast(Ray ray, List<Object3D> objects, Object3D caster){
        Intersection closestIntersection = null;

        for(int k = 0; k < objects.size(); k++){
            Object3D currentObj = objects.get(k);
            if(caster == null || !currentObj.equals(caster)){
                Intersection intersection = currentObj.getIntersection(ray);
                if(intersection != null){
                    double distance = intersection.getDistance();
                    if(distance >= 0 && (closestIntersection == null || distance < closestIntersection.getDistance())){
                        closestIntersection = intersection;
                    }
                }
            }
        }

        return closestIntersection;
    }
}
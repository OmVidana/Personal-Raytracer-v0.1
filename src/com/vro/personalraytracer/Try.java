//package com.vro.personalraytracer;
//
//public class Try {
//    for (Object3D object : objects) {
//        if (!object.equals(closestIntersection.getObject())) {
//            Intersection shadowIntersection = object.getIntersection(new Ray(closestIntersection.getPosition(), light.getShadowRayDirection(closestIntersection)));
//
//            if (shadowIntersection != null && shadowIntersection.getDistance() > 0 && shadowIntersection.getDistance() < closestIntersection.getDistance()) {
//                inShadow = true;
//                break;
//            }
//        }
//    }
//
//    if (!inShadow) {
//        for (int colorIndex = 0; colorIndex < objColors.length; colorIndex++) {
//            objColors[colorIndex] = intensity lightColors[colorIndex];
//        }
//
//        Color diffuse = new Color(clamp(objColors[0], 0, 1), clamp(objColors[1], 0, 1), clamp(objColors[2], 0, 1));
//        pixelColor = addColor(pixelColor, diffuse);
//    }
//    @Override
//    public Vector3D getShadowRayDirection(Intersection intersection) {
//        return Vector3D.normalize(Vector3D.substract(getPosition(), intersection.getPosition()));
//    }
//    @Override
//    public Vector3D getShadowRayDirection(Intersection intersection) {
//        return Vector3D.scalarMultiplication(getDirection(), -1.0);
//    }
//}

package hr.fer.zemris.java.raytracer;

import hr.fer.zemris.java.raytracer.model.GraphicalObject;
import hr.fer.zemris.java.raytracer.model.IRayTracerProducer;
import hr.fer.zemris.java.raytracer.model.IRayTracerResultObserver;
import hr.fer.zemris.java.raytracer.model.LightSource;
import hr.fer.zemris.java.raytracer.model.Point3D;
import hr.fer.zemris.java.raytracer.model.Ray;
import hr.fer.zemris.java.raytracer.model.RayIntersection;
import hr.fer.zemris.java.raytracer.model.Scene;
import hr.fer.zemris.java.raytracer.viewer.RayTracerViewer;

/**
 * {@code RayCaster} class is a class that casts rays and creates scene
 * snapshots by using ray-tracing technique.
 * <p>
 * This class does the same thing as {@link RayCasterParallel} but doesn't use
 * more than one thread for data processing.
 * 
 * @author Karlo Vrbić
 * @version 1.0
 */
public class RayCaster {

    /** Default ambient color intensity. */
    private static final short AMBIENT_COLOR = 15;

    /**
     * Starting point of a program.
     * 
     * @param args
     *            Command-line argument
     */
    public static void main(String[] args) {
        RayTracerViewer.show(
                getIRayTracerProducer(),
                new Point3D(10, 0, 0),
                new Point3D(0, 0, 0),
                new Point3D(0, 0, 10),
                20,
                20);
    }

    /**
     * Returns a tracer producer which is able to create scene snapshots by
     * using ray-tracing technique
     * 
     * @return the tracer producer
     */
    private static IRayTracerProducer getIRayTracerProducer() {
        return new IRayTracerProducer() {
            @Override
            public void produce(
                    Point3D eye,
                    Point3D view,
                    Point3D viewUp,
                    double horizontal,
                    double vertical,
                    int width,
                    int height,
                    long requestNo,
                    IRayTracerResultObserver observer) {
                System.out.println("Započinjem izračune...");

                short[] red = new short[width * height];
                short[] green = new short[width * height];
                short[] blue = new short[width * height];

                Point3D zAxis = view.sub(eye).normalize();
                Point3D yAxis = viewUp.modifyNormalize().sub(zAxis.scalarMultiply(viewUp.scalarProduct(zAxis)))
                        .normalize();
                Point3D xAxis = zAxis.vectorProduct(yAxis).normalize();

                Point3D screenCorner = view.sub(xAxis.scalarMultiply(horizontal / 2))
                        .add(yAxis.scalarMultiply(vertical / 2));

                Scene scene = RayTracerViewer.createPredefinedScene();

                short[] rgb = new short[3];
                int offset = 0;

                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        Point3D screenPoint = screenCorner
                                .add(xAxis.scalarMultiply((double) x / (width - 1) * horizontal))
                                .sub(yAxis.scalarMultiply((double) y / (height - 1) * vertical));
                        Ray ray = Ray.fromPoints(eye, screenPoint);

                        tracer(scene, ray, rgb);

                        red[offset] = rgb[0] > 255 ? 255 : rgb[0];
                        green[offset] = rgb[1] > 255 ? 255 : rgb[1];
                        blue[offset] = rgb[2] > 255 ? 255 : rgb[2];

                        offset++;
                    }
                }

                if (check(red))
                    System.out.println("All red are 15");
                if (check(green))
                    System.out.println("All green are 15");
                if (check(blue))
                    System.out.println("All blue are 15");

                System.out.println("Izračuni gotovi...");
                observer.acceptResult(red, green, blue, requestNo);
                System.out.println("Dojava gotova...");
            }

            private boolean check(short[] arr) {
                for (int i = 0; i < arr.length; i++)
                    if (arr[i] != 15)
                        return false;

                return true;
            }
        };
    }

    /**
     * Traces image using ray-casting model and fills results into {@code rgb}
     * array.
     * 
     * @param scene
     *            current scene
     * @param ray
     *            ray from eye to point in scene
     * @param rgb
     *            color light
     */
    private static void tracer(Scene scene, Ray ray, short[] rgb) {
        short[] newRGB = new short[3];
        RayIntersection intersection = getClosestIntersection(scene, ray);

        // if there is no intersection there is only ambient light
        if (intersection != null) {
            newRGB = determineColorFor(scene, ray, intersection);
        } else {
            newRGB[0] = AMBIENT_COLOR;
            newRGB[1] = AMBIENT_COLOR;
            newRGB[2] = AMBIENT_COLOR;
        }

        rgb[0] = newRGB[0];
        rgb[1] = newRGB[1];
        rgb[2] = newRGB[2];
    }

    /**
     * Calculates the color of the {@code intersection} for specified
     * {@code ray} in the specified {@code scene}.
     * 
     * @param scene
     *            the scene
     * @param ray
     *            the ray
     * @param intersection
     *            the ray intersection
     * @return array containing three {@code short} elements that represents RGB
     *         color code
     */
    private static short[] determineColorFor(Scene scene, Ray ray, RayIntersection intersection) {
        short[] rgb = new short[3];

        rgb[0] = AMBIENT_COLOR;
        rgb[1] = AMBIENT_COLOR;
        rgb[2] = AMBIENT_COLOR;

        for (LightSource light : scene.getLights()) {
            Ray r = Ray.fromPoints(light.getPoint(), intersection.getPoint());
            RayIntersection s = getClosestIntersection(scene, r);

            if (s == null)
                continue;

            double distanceFromEye = light.getPoint().sub(intersection.getPoint()).norm();
            double distanceFromLight = light.getPoint().sub(s.getPoint()).norm();

            if (Double.compare(distanceFromLight + 0.01, distanceFromEye) < 0)
                continue;

            addDiffusseComponent(light, rgb, s);
            addReflectiveComponent(light, rgb, s, ray);
        }

        return rgb;
    }

    /**
     * Returns closest intersection of specified ray on given scene. If no
     * intersection is found {@code null} will be returned.
     * 
     * @param scene
     *            scene in which intersection must be found
     * @param ray
     *            ray used to find intersection
     * @return the closest intersection of specified ray on given scene
     */
    private static RayIntersection getClosestIntersection(Scene scene, Ray ray) {
        if (scene == null)
            throw new NullPointerException("Argument scene cannot be null reference!");
        if (ray == null)
            throw new NullPointerException("Argument ray cannot be null reference!");

        RayIntersection closest = null;

        for (GraphicalObject object : scene.getObjects()) {
            RayIntersection curr = object.findClosestRayIntersection(ray);

            if (curr != null && (closest == null || curr.getDistance() < closest.getDistance())) {
                closest = curr;
            }
        }

        return closest;
    }

    /**
     * Calculates the diffuse component of the light and stores it in
     * {@code rgb} parameter.
     * 
     * @param light
     *            light source
     * @param rgb
     *            color of the light represented by RGB model
     * @param intersection
     *            intersection we're calculating light for
     */
    private static void addDiffusseComponent(LightSource light, short[] rgb, RayIntersection intersection) {
        Point3D n = intersection.getNormal();
        Point3D l = light.getPoint().sub(intersection.getPoint()).normalize();

        double tmp = l.scalarProduct(n);

        rgb[0] += light.getR() * intersection.getKdr() * Math.max(tmp, 0);
        rgb[1] += light.getG() * intersection.getKdg() * Math.max(tmp, 0);
        rgb[2] += light.getB() * intersection.getKdb() * Math.max(tmp, 0);
    }

    /**
     * Calculates the reflective component of the light and stores it in
     * {@code rgb} parameter.
     * 
     * @param light
     *            light source
     * @param rgb
     *            color of the light represented by RGB model
     * @param intersection
     *            intersection we're calculating light for
     * @param ray
     *            Ray used to find intersection.
     */
    private static void addReflectiveComponent(LightSource light, short[] rgb, RayIntersection intersection, Ray ray) {
        Point3D n = intersection.getNormal();
        Point3D l = light.getPoint().sub(intersection.getPoint());
        Point3D projection = n.scalarMultiply(l.scalarProduct(n));

        Point3D r = projection.add(projection.negate().add(l).scalarMultiply(-1)).normalize();
        Point3D v = ray.start.sub(intersection.getPoint()).normalize();

        double cos = r.scalarProduct(v);

        if (cos >= 0) {
            cos = Math.pow(cos, intersection.getKrn());

            rgb[0] += light.getR() * intersection.getKrr() * cos;
            rgb[1] += light.getG() * intersection.getKrg() * cos;
            rgb[2] += light.getB() * intersection.getKrb() * cos;
        }
    }

}

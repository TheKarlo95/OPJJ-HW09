package hr.fer.zemris.java.raytracer.model;

import java.util.Arrays;

/**
 * {@code Sphere} is a class that represents a spherical spherical object.
 * <p>
 * This class is implementation of a {@link GraphicalObject} interface.
 * 
 * @author Karlo Vrbić
 * @version 1.0
 * @see GraphicalObject
 */
public class Sphere extends GraphicalObject {

    /** Center point. */
    private Point3D center;
    /** Sphere radius. */
    private double radius;

    /** Coefficient of diffuse component for red color. */
    private double kdr;
    /** Coefficient of diffuse component for green color. */
    private double kdg;
    /** Coefficient of diffuse component for blue color. */
    private double kdb;

    /** Coefficient of reflective component for red color. */
    private double krr;
    /** Coefficient of reflective component for green color. */
    private double krg;
    /** Coefficient of reflective component for blue color. */
    private double krb;
    /**
     * Coefficient n of reflective component of the light. It describes surface
     * roughness and its reflective properties
     */
    private double krn;

    /**
     * Constructs a new {@code Sphere} object from specified parameters.
     * 
     * @param center
     *            center point
     * @param radius
     *            sphere radius
     * @param kdr
     *            diffuse component of the red light
     * @param kdg
     *            diffuse component of the green light
     * @param kdb
     *            diffuse component of the blue light
     * @param krr
     *            reflective component of the red light
     * @param krg
     *            reflective component of the green light
     * @param krb
     *            reflective component of the blue light
     * @param krn
     *            coefficient n of reflective component of the light
     * @throws NullPointerException
     *             if argument {@code center} or {@code radius} is a null
     *             reference
     * @throws IllegalArgumentException
     *             if radius is not a positive number
     */
    public Sphere(Point3D center, double radius, double kdr, double kdg, double kdb, double krr, double krg, double krb,
            double krn) {
        if (center == null)
            throw new NullPointerException("Center or radius cannot be a null reference!");

        if (radius <= 0)
            throw new IllegalArgumentException("Radius must be a positive number!");

        checkLight(kdr, kdg, kdb, "Diffuse");
        checkLight(krr, krg, krb, "Reflective");

        this.center = center;
        this.radius = radius;
        this.kdr = kdr;
        this.kdg = kdg;
        this.kdb = kdb;
        this.krr = krr;
        this.krg = krg;
        this.krb = krb;
        this.krn = krn;
    }

    @Override
    public RayIntersection findClosestRayIntersection(Ray ray) {
        Point3D tmp = ray.start.sub(Sphere.this.center); // Ts - C

        // factors of a quadratic polynom(a * x^2 + b * x + c) used to determine
        // intersection
        double a = 1; // = 1
        double b = tmp.scalarMultiply(2.0).scalarProduct(ray.direction);
        double c = tmp.scalarProduct(tmp) - Math.pow(Sphere.this.radius, 2);

        double discriminant = b * b - 4 * a * c;

        // if there is no real roots of a polynom there is no intersections
        if (discriminant < 0)
            return null;

        Double[] roots = new Double[2];
        roots[0] = (-b + Math.sqrt(discriminant)) / (2 * a);
        roots[1] = (-b - Math.sqrt(discriminant)) / (2 * a);

        Arrays.sort(roots, (v1, v2) -> Double.compare(v1, v2));

        // calculate inner and outer intersection
        Point3D[] intersections = new Point3D[2];
        intersections[0] = ray.start.add(ray.direction.scalarMultiply(roots[0]));
        intersections[1] = ray.start.add(ray.direction.scalarMultiply(roots[1]));

        // ... and their distance from ray start point
        Double[] distance = new Double[2];
        distance[0] = intersections[0].sub(ray.start).norm();
        distance[1] = intersections[0].sub(ray.start).norm();

        // put closer intersection and its distance to index 0
        if (distance[0] > distance[1]) {
            double tmpDistance = distance[1];
            Point3D tmpIntersection = intersections[1];

            distance[1] = distance[0];
            intersections[1] = intersections[0];

            distance[0] = tmpDistance;
            intersections[0] = tmpIntersection;
        }

        return new SphereRayIntersection(intersections[0], distance[0],
                intersections[0].sub(Sphere.this.center).norm() > radius);
    }

    /**
     * Checks if specified color of light is valid.
     * 
     * @param kr
     *            red coefficient
     * @param kg
     *            green coefficient
     * @param kb
     *            blue coefficient
     * @param name
     *            name to be added to the exception message
     * @throws IllegalArgumentException
     *             if either of arguments {@code r}, {@code g} and {@code b} are
     *             less than 0 and bigger than 255
     */
    private static void checkLight(double kr, double kg, double kb, String name) {
        if (kr < 0 || kr > 1.0 || kg < 0 || kg > 1.0 || kb < 0 || kb > 1.0) {
            throw new IllegalArgumentException(String.format("%s component coefficient of light (%.2f,%.2f,%.2f) is"
                    + " invalid.", name, kr, kg, kb));
        }
    }

    /**
     * {@code SphereRayIntersection} class represents an intersections of ray
     * and sphere.
     * <p>
     * This class is implementation of the {@link RayIntersection}.
     * 
     * @author Karlo Vrbić
     * @version 1.0
     * @see RayIntersection
     */
    class SphereRayIntersection extends RayIntersection {

        /**
         * Constructs a new {@code SphereRayIntersection} object with specified
         * parameters.
         * 
         * @param point
         *            point of intersection
         * @param distance
         *            distance between start of ray and intersection
         * @param outer
         *            flag that indicates if this intersection is outer
         *            intersection
         */
        protected SphereRayIntersection(Point3D point, double distance, boolean outer) {
            super(point, distance, outer);
        }

        @Override
        public Point3D getNormal() {
            return getPoint().sub(Sphere.this.center).normalize();
        }

        @Override
        public double getKdr() {
            return Sphere.this.kdr;
        }

        @Override
        public double getKdg() {
            return Sphere.this.kdg;
        }

        @Override
        public double getKdb() {
            return Sphere.this.kdb;
        }

        @Override
        public double getKrr() {
            return Sphere.this.krr;
        }

        @Override
        public double getKrg() {
            return Sphere.this.krg;
        }

        @Override
        public double getKrb() {
            return Sphere.this.krb;
        }

        @Override
        public double getKrn() {
            return Sphere.this.krn;
        }
    }

}

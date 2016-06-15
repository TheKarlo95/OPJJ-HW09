package hr.fer.zemris.java.fractals.complex;

/**
 * {@code ComplexRootedPolynomial} class represents a polynom with a constant
 * complex factors.
 * <p>
 * When creating an instance of this class input roots/zeroes of polynom you
 * want instead of factors.
 * 
 * @author Karlo Vrbić
 * @version 1.0
 */
public class ComplexRootedPolynomial {

    /**
     * Array of roots of this polynomial.
     */
    Complex[] roots;

    /**
     * Constructs a new {@code ComplexRootedPolynomial} with specified roots.
     * 
     * @param roots
     *            roots of a polynomial
     * @throws NullPointerException
     *             if roots argument is null or one of it elements is null
     */
    public ComplexRootedPolynomial(Complex... roots) {
        if (roots == null)
            throw new NullPointerException("Roots of polynomial cannot be a null reference!");

        for (Complex root : roots) {
            if (root == null)
                throw new NullPointerException("Root of polynomial cannot be a null reference!");
        }

        if (roots.length == 0) {
            this.roots = new Complex[] { new Complex(0, 0) };
        } else {
            this.roots = roots;
        }
    }

    /**
     * Computes polynomial value at given point z
     * 
     * @param z
     *            the point which we want to evaluate polynomial
     * @return the result of polynomial at given point
     * @throws NullPointerException
     *             if argument {@code z} is a null reference
     */
    public Complex apply(Complex z) {
        if (z == null)
            throw new NullPointerException("You cannot evaluate value of this polynomial with null reference!");

        Complex result = Complex.ONE;

        for (Complex root : roots) {
            result = result.multiply(z.sub(root));
        }

        return result;
    }

    /**
     * Converts this {@code ComplexRootedPolynomial} to
     * {@link ComplexPolynomial} type.
     * 
     * @return {@link ComplexPolynomial} representation of this object
     */
    public ComplexPolynomial toComplexPolynom() {
        ComplexPolynomial result = new ComplexPolynomial(roots[0].negate(), new Complex(1, 0));

        for (int i = 1; i < roots.length; i++) {
            result = result.multiply(new ComplexPolynomial(roots[i].negate(), new Complex(1, 0)));
        }

        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (Complex root : roots) {
            sb.append("[z - (");
            sb.append(root);
            sb.append(")] * ");
        }

        return sb.substring(0, sb.length() - 3);
    }

    /**
     * Checks if specified {@code Complex} number is close to one of the roots.
     * 
     * @param z
     *            the complex number
     * @param treshold
     *            maximum distance between this and specified number
     * @return index of the root number is close to; -1 otherwise
     */
    public int indexOfClosestRootFor(Complex z, double treshold) {
        if (z == null)
            throw new NullPointerException("Complex number z cannot be a null reference!");

        if (treshold < 0)
            throw new IllegalArgumentException("Treshold cannot be a negative number!");

        int index = -1;
        double minLength = Double.MAX_VALUE;

        for (int i = 0; i < roots.length; i++) {
            double length = z.distance(roots[i]);

            if (length <= treshold && length < minLength) {
                index = i;
                minLength = length;
            }
        }

        return index == -1 ? -1 : index + 1;
    }

}

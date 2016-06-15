package hr.fer.zemris.java.fractals.complex;

import java.util.Arrays;

/**
 * {@code ComplexPolynomial} class represents a polynom with a constant complex
 * factors.
 * <p>
 * When creating an instance of this class input factors in this order: factor
 * for x^0 goes first, for x^1 goes second and so on.
 * <p>
 * <b>Note:</b>This class doesn't have {@code hashcode()} method overridden so
 * it's not safe to store this class in HashMap or something similar.
 * 
 * @author Karlo Vrbić
 * @version 1.0
 */
public class ComplexPolynomial {

    /**
     * Array of factors/coefficients of this polynomial. Factor at index i is
     * factor of z^i
     */
    private final Complex[] factors;

    /**
     * Constructs a new {@code ComplexPolynomial} with specified factors.
     * 
     * @param factors
     *            factors of a polynomial
     * @throws NullPointerException
     *             if factors argument is null or one of it elements is null
     */
    public ComplexPolynomial(Complex... factors) {
        if (factors == null)
            throw new NullPointerException("Factors of polynomial cannot be a null reference!");

        for (Complex factor : factors) {
            if (factor == null)
                throw new NullPointerException("Factor of polynomial cannot be a null reference!");
        }

        if (factors.length == 0) {
            this.factors = new Complex[] { new Complex(0, 0) };
        } else {
            this.factors = factors;
        }
    }

    /**
     * Returns the order of this polynomial; eg. For (7+2i)z^3+2z^2+5z+1 returns
     * 3.
     * 
     * @return the order of this polynomial
     */
    public short order() {
        return (short) factors.length;
    }

    /**
     * Returns a {@code ComplexPolynomial} object whose value is (this × c).
     * 
     * @param p
     *            polynomial to be multiplied with this polynomial
     * @return this × c
     * @throws NullPointerException
     *             if argument {@code p} is a null reference
     */
    public ComplexPolynomial multiply(ComplexPolynomial p) {
        if (p == null)
            throw new NullPointerException("You cannot multiply complex polynimal with null reference!");

        int totalLength = factors.length + p.factors.length - 1;
        Complex[] result = new Complex[totalLength];

        for (int i = 0; i < totalLength; i++) {
            result[i] = Complex.ZERO;
        }

        for (int i = 0; i < factors.length; i++) {
            for (int j = 0; j < p.factors.length; j++) {
                result[i + j] = result[i + j].add(factors[i].multiply(p.factors[j]));
            }
        }

        return new ComplexPolynomial(result);
    }

    /**
     * Computes first derivative of this polynomial; e.g. For
     * (7+2i)z^3+2z^2+5z+1 returns (21+6i)z^2+4z+5
     * 
     * @return the first derivative of this polynomial
     */
    public ComplexPolynomial derive() {
        if (factors.length == 1)
            return new ComplexPolynomial(Complex.ZERO);

        Complex[] result = new Complex[factors.length - 1];

        for (int i = 1; i <= result.length; i++)
            result[i - 1] = factors[i].multiply(new Complex(i, 0));

        return new ComplexPolynomial(result);
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

        if (z.equals(Complex.ZERO))
            return factors[0];

        Complex result = Complex.ZERO;

        for (int i = 0; i < factors.length; i++) {
            result = result.add(z.power(i).multiply(factors[i]));
        }

        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        int order = factors.length;

        for (int i = order - 1; i >= 0; i--) {
            sb.append("(");
            sb.append(factors[i]);
            sb.append(")");

            sb.append(variableToString('z', i));
            sb.append(" + ");
        }

        // removing last '('
        int lastIndex = sb.toString().lastIndexOf("(");
        sb.replace(lastIndex, lastIndex + 1, "");

        return sb.substring(0, sb.length() - 4);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ComplexPolynomial other = (ComplexPolynomial) obj;
        if (!Arrays.equals(factors, other.factors))
            return false;
        return true;
    }

    /**
     * Formats the variable sign and returns it.
     * <p>
     * Returned string looks like:
     * <ul>
     * <li>"" - for every variable sign and power of 0
     * <li>"z" - for variable sign 'z' and power of 1
     * <li>"z^n" - for variable sign 'z' and power of n
     * </ul>
     * 
     * @param variable
     *            variable sign
     * @param power
     *            power of the variable
     * @return formated string of variable and its power
     */
    private static String variableToString(char variable, int power) {
        if (power == 0) {
            return "";
        } else if (power == 1) {
            return "" + variable;
        } else {
            return variable + "^" + power;
        }
    }

}

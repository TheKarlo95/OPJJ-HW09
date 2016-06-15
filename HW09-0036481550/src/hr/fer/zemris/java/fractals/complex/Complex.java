package hr.fer.zemris.java.fractals.complex;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * {@code Complex} class is a representation of a Complex number, i.e. a number
 * which has both a real and imaginary part.
 * <p>
 * <b>Note:</b>This class doesn't have {@code hashcode()} method overridden so
 * it's not safe to store this class in HashMap or something similar.
 * 
 * @author Karlo Vrbić
 * @version 1.0
 */
public class Complex {

    /** Number 0 represented by this class. */
    public static final Complex ZERO = new Complex(0, 0);

    /** Number 1 represented by this class. */
    public static final Complex ONE = new Complex(1, 0);

    /** Number -1 represented by this class. */
    public static final Complex ONE_NEG = new Complex(-1, 0);

    /** Number i represented by this class. */
    public static final Complex IM = new Complex(0, 1);

    /** Number -i represented by this class. */
    public static final Complex IM_NEG = new Complex(0, -1);

    /** Real part of this complex number */
    private final double re;

    /** Imaginary part of this complex number */
    private final double im;

    /**
     * Constructs a new {@code Complex} object representing number 0.
     */
    public Complex() {
        this(0, 0);
    }

    /**
     * Constructs a new {@code Complex} object with specified real and imaginary
     * parts.
     * 
     * @param re
     *            the real part of complex number
     * @param im
     *            the imaginary part of complex number
     */
    public Complex(double re, double im) {
        this.re = re;
        this.im = im;
    }

    /**
     * Returns a {@code Complex} object with specified real and imaginary parts.
     * <p>
     * <b>Note:</b> This is generally the preferred way of getting
     * {@code Complex} objects. This method doesn't always returns a newly
     * constructed object. For numbers like:
     * <ul>
     * <li>{@code 0}
     * <li>{@code 1}
     * <li>{@code -1}
     * <li>{@code i}
     * <li>{@code -i}
     * </ul>
     * same object is returned.
     * 
     * @param re
     *            the real part of complex number
     * @param im
     *            the imaginary part of complex number
     * @return {@code Complex} object with real and imaginary parts specified by
     *         arguments
     */
    public static Complex valueOf(double re, double im) {
        if (re == 0.0 && im == 0) {
            return ZERO;
        } else if (im == 0.0) {
            if (re == 1)
                return ONE;
            if (re == -1)
                return ONE_NEG;
        } else if (re == 0.0) {
            if (im == 1)
                return IM;
            if (im == -1)
                return IM_NEG;
        }

        return new Complex(re, im);
    }

    /**
     * Returns a {@code Complex} object with real and imaginary parts specified
     * by {@code line} argument.
     * <p>
     * Type of input that is recognized by this method are(<b>Note:</b>
     * {@code a} represents real part of the number, {@code b} represents
     * imaginary part of the number and {@code i} represents imaginary unit):
     * <ul>
     * <li>{@code a + ib}
     * <li>{@code a - ib}
     * <li>{@code a} or {@code -a}
     * <li>{@code ib} or {@code -ib}
     * <li>{@code 0} or {@code i0} or {@code 0 + i0} or {@code 0 - i0}
     * </ul>
     * 
     * @param line
     *            string representing complex number
     * @return {@code Complex} object with real and imaginary parts specified by
     *         string argument
     * @throws NullPointerException
     *             if argument {@code line} is a null reference
     * @throws IllegalArgumentException
     *             if line arguments length is equal to 0 after triming
     */
    public static Complex valueOf(String line) {
        if (line == null)
            throw new NullPointerException("You cannot evaluate value of a null reference!");

        if (line.trim().length() == 0)
            throw new IllegalArgumentException("You cannot evaluate value of an empty string!");

        String[] parts = line.trim().split("\\s+");
        Complex output;

        if (parts.length == 3) {
            if (!parts[0].matches("^[+-]?[0-9]+(.[0-9]*)?$"))
                throw new IllegalArgumentException("First part of argument doesn't have right format!");
            if (!parts[1].matches("^[+-]$"))
                throw new IllegalArgumentException("Second part of argument doesn't have right format!");
            if (!parts[2].matches("^i([0-9]+(.[0-9]*)?)?$"))
                throw new IllegalArgumentException("Third part of argument doesn't have right format!");

            parts[2] = parts[2].replace("i", "");

            if (parts[2].isEmpty()) {
                if (parts[1].equals("+")) {
                    parts[2] = "1";
                } else {
                    parts[2] = "-1";
                }
            }

            double real = Double.parseDouble(parts[0]);
            double imag = Double.parseDouble(parts[2].replace("i", ""));

            if (parts[1].equals("+")) {
                output = Complex.valueOf(real, imag);
            } else if (parts[1].equals("-")) {
                output = Complex.valueOf(real, -imag);
            } else {
                throw new IllegalArgumentException("Invalid argument provided!");
            }
        } else if (parts.length == 1) {
            if (parts[0].matches("^[+-]?[0-9]+(.[0-9]*)?$")) {
                output = Complex.valueOf(Double.parseDouble(parts[0]), 0);
            } else if (parts[0].matches("^[+-]?i([0-9]+(.[0-9]*)?)?$")) {
                if (parts[0].equals("i") || parts[0].equals("+i")) {
                    output = Complex.valueOf(0, 1);
                } else if (parts[0].equals("-i")) {
                    output = Complex.valueOf(0, -1);
                } else {
                    output = Complex.valueOf(0, Double.parseDouble(parts[0].replace("i", "")));
                }
            } else {
                throw new IllegalArgumentException("Invalid argument provided!");
            }

        } else {
            throw new IllegalArgumentException(
                    "String argument must consist of three or one part separated by spaces!");
        }

        return output;
    }

    /**
     * Returns the absolute value of a complex value.
     * 
     * @return the absolute value of a complex value.
     */
    public double module() {
        return Math.sqrt(re * re + im * im);
    }

    /**
     * Returns a {@code Complex} object whose value is (this × c).
     * 
     * @param c
     *            value to be multiplied by this {@code Complex} number
     * @return this × c
     * @throws NullPointerException
     *             if argument {@code c} is a null reference
     */
    public Complex multiply(Complex c) {
        if (c == null)
            throw new NullPointerException("Factor cannot be null!");

        return valueOf(this.re * c.re - this.im * c.im, this.re * c.im + this.im * c.re);
    }

    /**
     * Returns a {@code Complex} object whose value is (this ÷ c).
     * 
     * @param c
     *            value by which this {@code Complex} number is to be divided
     * @return this ÷ c
     * @throws NullPointerException
     *             if argument {@code c} is a null reference
     * @throws ArithmeticException
     *             if argument {@code c} equals to 0
     */
    public Complex divide(Complex c) {
        if (c == null)
            throw new NullPointerException("Divisor cannot be null!");

        if (c.equals(ZERO))
            throw new ArithmeticException("You are trying to divide with 0!");

        double divisor = c.re * c.re + c.im * c.im;

        double re = (this.re * c.re + this.im * c.im) / divisor;
        double im = (this.im * c.re - this.re * c.im) / divisor;

        return valueOf(re, im);
    }

    /**
     * Returns a {@code Complex} object whose value is (this + c).
     * 
     * @param c
     *            value to be added to this {@code Complex} number
     * @return this + c
     * @throws NullPointerException
     *             if argument {@code c} is a null reference
     */
    public Complex add(Complex c) {
        if (c == null)
            throw new NullPointerException("Addend cannot be null!");

        return valueOf(this.re + c.re, this.im + c.im);
    }

    /**
     * Returns a {@code Complex} object whose value is (this - c).
     * 
     * @param c
     *            value to be subtracted from this {@code Complex} number
     * @return this - c
     * @throws NullPointerException
     *             if argument {@code c} is a null reference
     */
    public Complex sub(Complex c) {
        if (c == null)
            throw new NullPointerException("Subtrahend cannot be null!");

        return valueOf(this.re - c.re, this.im - c.im);
    }

    /**
     * Returns a {@code Complex} object whose value is (-this).
     * 
     * @return -this
     */
    public Complex negate() {
        return valueOf(-re, -im);
    }

    /**
     * Returns a {@code Complex} object whose value is (this<sup>n</sup>).
     * 
     * @param n
     *            power to raise this BigDecimal to.
     * @return this<sup>n</sup>
     * @throws IllegalArgumentException
     *             if argument {@code n} is a negative number
     * @throws ArithmeticException
     *             if argument {@code n} is equal to 0 and this object equals to
     *             0
     */
    public Complex power(int n) {
        if (n < 0)
            throw new IllegalArgumentException("Argument of power must be non-negative integer!");

        if (this.equals(Complex.ZERO) && n == 0)
            throw new ArithmeticException("Result for 0^0 is undefined!");

        double powerOfModule = Math.pow(module(), n);
        double argument = Math.atan2(im, re);

        double re = powerOfModule * Math.cos(n * argument);
        double im = powerOfModule * Math.sin(n * argument);

        return valueOf(re, im);
    }

    /**
     * Returns the list of roots of a {@code Complex} number.
     * 
     * @param n
     *            the degree of the root
     * @return the list of roots
     * @throws IllegalArgumentException
     *             if argument {@code n} is a non-positive number
     */
    public List<Complex> root(int n) {
        if (n <= 0)
            throw new IllegalArgumentException("Argument of power must be positive integer!");

        double rootOfModule = Math.pow(module(), 1.0 / n);
        double argument = Math.atan2(im, re);

        List<Complex> roots = new ArrayList<>(n);

        for (int i = 0; i < n; i++) {
            double angle = (argument + 2 * Math.PI * i) / n;

            double re = rootOfModule * Math.cos(angle);
            double im = rootOfModule * Math.sin(angle);

            roots.add(valueOf(re, im));
        }

        return roots;
    }

    /**
     * Indicates whether some other object is "equal to" this one but with
     * precision.
     * <p>
     * Precision is specified by epsilon parameter. If we compare (1 + i) with
     * (1.000001 + i) with precision 0.00001 for example, this method will
     * return true because |1 - 1000001| &gt; 0.00001.
     * 
     * @param obj
     *            the reference object with which to compare
     * @param epsilon
     *            the precision of comparison
     * @return {@code true} if this object is the same as the obj argument;
     *         {@code false} otherwise
     */
    public boolean equals(Object obj, double epsilon) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Complex other = (Complex) obj;
        if (Double.doubleToLongBits(im) != Double.doubleToLongBits(other.im))
            if (Math.abs(this.im - other.im) >= epsilon)
                return false;
        if (Double.doubleToLongBits(re) != Double.doubleToLongBits(other.re))
            if (Math.abs(this.re - other.re) >= epsilon)
                return false;

        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(im);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(re);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Complex other = (Complex) obj;
        if (Double.doubleToLongBits(im) != Double.doubleToLongBits(other.im))
            return false;
        if (Double.doubleToLongBits(re) != Double.doubleToLongBits(other.re))
            return false;
        return true;
    }

    @Override
    public String toString() {
        DecimalFormat df = new DecimalFormat("0.####");
        if (re != 0) {
            if (im > 0) {
                return String.format("%s + i%s", df.format(re), df.format(im));
            } else if (im < 0) {
                return String.format("%s - i%s", df.format(re), df.format(-im));
            } else {
                return String.format("%s", df.format(re));
            }
        } else {
            if (im > 0) {
                return String.format("i%s", df.format(im));
            } else if (im < 0) {
                return String.format("-i%s", df.format(-im));
            } else {
                return "0";
            }
        }
    }

    /**
     * Returns distance between this and specified complex number.
     * 
     * @param z
     *            the complex number
     * @return distance between this and specified complex number
     */
    double distance(Complex z) {
        return Math.pow(Math.pow(re - z.re, 2) + Math.pow(im - z.im, 2), 1.0 / 2);
    }

}

package hr.fer.zemris.java.fractals.complex;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import hr.fer.zemris.java.fractals.complex.Complex;
import hr.fer.zemris.java.fractals.complex.ComplexPolynomial;

/**
 * Test class for {@link ComplexPolynomial} class.
 * <p>
 * <b>Note:</b>These tests aren't complete so full functionality of
 * {@code ComplexPolynomial} class isn't guaranteed.
 * 
 * @author Karlo Vrbić
 * @version 1.0
 * @see ComplexPolynomial
 */
@SuppressWarnings("javadoc")
public class ComplexPolynomialTests {

    ComplexPolynomial p1;
    ComplexPolynomial p2;

    // test for: constructor

    @Before
    @Test
    public void constructorTest_Success() {
        p1 = new ComplexPolynomial(Complex.valueOf(2, 1), Complex.valueOf(1, 0), Complex.valueOf(0, -4));
        p2 = new ComplexPolynomial(Complex.valueOf(3, -1), Complex.valueOf(-4, -1));
    }

    @Test(expected = NullPointerException.class)
    public void constructorTest_FailNull() {
        new ComplexPolynomial((Complex[]) null);
    }

    @Test(expected = NullPointerException.class)
    public void constructorTest_FailNullElement() {
        new ComplexPolynomial(Complex.valueOf(2, 1), null, Complex.valueOf(0, -4));
    }

    // test for: order(): int

    @Test
    public void orderTest_Success() {
        assertEquals(3, p1.order());
        assertEquals(2, p2.order());
    }

    // test for: multiply(ComplexPolynomial): ComplexPolynomial

    @Test
    public void multiplyTest_Success() {
        ComplexPolynomial expected = new ComplexPolynomial(Complex.valueOf(7, 1), Complex.valueOf(-4, -7),
                Complex.valueOf(-8, -13), Complex.valueOf(-4, 16));

        assertEquals(expected, p1.multiply(p2));
        assertEquals(expected, p2.multiply(p1));
    }

    @Test(expected = NullPointerException.class)
    public void multiplyTest_FailNull() {
        p1.multiply(null);
    }

    // test for: derive(): ComplexPolynomial

    @Test
    public void deriveTest_Success() {
        assertEquals(new ComplexPolynomial(Complex.valueOf(1, 0), Complex.valueOf(0, -8)), p1.derive());
        assertEquals(new ComplexPolynomial(Complex.valueOf(-4, -1)), p2.derive());
        assertEquals(new ComplexPolynomial(new Complex[] {}), new ComplexPolynomial(new Complex[] {}).derive());
    }

    @Test(expected = NullPointerException.class)
    public void deriveTest_FailNull() {
        p1.multiply(null);
    }

    // test for: apply(Complex): Complex

    @Test
    public void applyTest_Success() {
        assertTrue(Complex.valueOf(2, 1).equals(p1.apply(Complex.ZERO), 0.001));
        assertTrue(Complex.valueOf(-19, -32).equals(p1.apply(Complex.valueOf(3, -1)), 0.001));
        assertTrue(Complex.valueOf(-15, 15).equals(p1.apply(Complex.valueOf(-1, 2)), 0.001));

        assertTrue(Complex.valueOf(3, -1).equals(p2.apply(Complex.ZERO), 0.001));
        assertTrue(Complex.valueOf(7, -17).equals(p2.apply(Complex.valueOf(0, 4)), 0.001));
        assertTrue(Complex.valueOf(-79, -30).equals(p2.apply(Complex.valueOf(21, 2)), 0.001));
    }

    @Test(expected = NullPointerException.class)
    public void applyTest_FailNull() {
        p1.apply(null);
    }
}

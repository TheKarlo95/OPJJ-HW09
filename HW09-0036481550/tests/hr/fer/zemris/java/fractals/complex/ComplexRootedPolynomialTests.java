package hr.fer.zemris.java.fractals.complex;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import hr.fer.zemris.java.fractals.complex.Complex;
import hr.fer.zemris.java.fractals.complex.ComplexPolynomial;
import hr.fer.zemris.java.fractals.complex.ComplexRootedPolynomial;

/**
 * Test class for {@link ComplexRootedPolynomial} class.
 * <p>
 * <b>Note:</b>These tests aren't complete so full functionality of
 * {@code ComplexRootedPolynomial} class isn't guaranteed.
 * 
 * @author Karlo Vrbić
 * @version 1.0
 * @see ComplexRootedPolynomial
 */
@SuppressWarnings("javadoc")
public class ComplexRootedPolynomialTests {

    ComplexRootedPolynomial p1;
    ComplexRootedPolynomial p2;

    // tests for: constructor

    @Before
    @Test
    public void constructorTest_Success() {
        p1 = new ComplexRootedPolynomial(Complex.valueOf(2, 1), Complex.valueOf(1, 0), Complex.valueOf(0, -4));
        p2 = new ComplexRootedPolynomial(Complex.valueOf(3, -1), Complex.valueOf(-4, -1));
    }

    @Test(expected = NullPointerException.class)
    public void constructorTest_FailNull() {
        new ComplexRootedPolynomial((Complex[]) null);
    }

    @Test(expected = NullPointerException.class)
    public void constructorTest_FailNullElement() {
        new ComplexRootedPolynomial(Complex.valueOf(2, 1), null, Complex.valueOf(0, -4));
    }

    // tests for: apply(Complex): Complex

    @Test
    public void applyTest_Success() {
        assertEquals(Complex.valueOf(-4, 8), p1.apply(Complex.ZERO));
        assertEquals(Complex.valueOf(15, -15), p1.apply(Complex.valueOf(3, -1)));
        assertEquals(Complex.valueOf(44, 32), p1.apply(Complex.valueOf(-1, 2)));

        assertEquals(Complex.valueOf(-13, 1), p2.apply(Complex.ZERO));
        assertEquals(Complex.valueOf(-37, 5), p2.apply(Complex.valueOf(0, 4)));
        assertEquals(Complex.valueOf(441, 129), p2.apply(Complex.valueOf(21, 2)));
    }

    @Test(expected = NullPointerException.class)
    public void applyTest_FailNull() {
        p1.apply(null);
    }

    // tests for: toComplexPolynom(): ComplexPolynomial

    @Test
    public void toComplexPolynomTest_Success() {
        ComplexPolynomial expected1 = new ComplexPolynomial(Complex.valueOf(-4, 8), Complex.valueOf(6, -11),
                Complex.valueOf(-3, 3), Complex.valueOf(1, 0));
        ComplexPolynomial expected2 = new ComplexPolynomial(Complex.valueOf(-13, 1), Complex.valueOf(1, 2),
                Complex.valueOf(1, 0));

        assertEquals(expected1, p1.toComplexPolynom());
        assertEquals(expected2, p2.toComplexPolynom());
    }

    // tests for: indexOfClosestRootFor(Complex): int

    @Test
    public void indexOfClosestRootForTest_Success() {
        assertEquals(-1, p1.indexOfClosestRootFor(Complex.valueOf(0, 0), 0));
        assertEquals(1, p1.indexOfClosestRootFor(Complex.valueOf(2, 2), 2));
        assertEquals(2, p1.indexOfClosestRootFor(Complex.valueOf(0, 0), 1));
        assertEquals(3, p1.indexOfClosestRootFor(Complex.valueOf(0, -7), 3.1));

        assertEquals(-1, p2.indexOfClosestRootFor(Complex.valueOf(0, 0), 0.1));
        assertEquals(1, p2.indexOfClosestRootFor(Complex.valueOf(3, 0), 1.5));
        assertEquals(2, p2.indexOfClosestRootFor(Complex.valueOf(-5, -2), 3));
    }

    @Test(expected = NullPointerException.class)
    public void indexOfClosestRootForTest_FailNull() {
        p1.indexOfClosestRootFor(null, 0.1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void indexOfClosestRootForTest_FailNegativeTreshold() {
        p1.indexOfClosestRootFor(Complex.valueOf(0, 0), -0.1);
    }
}

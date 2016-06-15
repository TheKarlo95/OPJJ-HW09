package hr.fer.zemris.java.fractals.complex;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import hr.fer.zemris.java.fractals.complex.Complex;

/**
 * Test class for {@link Complex} class.
 * <p>
 * <b>Note:</b>These tests aren't complete so full functionality of
 * {@code Complex} class isn't guaranteed.
 * 
 * @author Karlo Vrbić
 * @version 1.0
 * @see Complex
 */
@SuppressWarnings("javadoc")
public class ComplexTests {

    private static final Complex C1 = new Complex(3, 5);
    private static final Complex C2 = new Complex(6, -2);
    private static final Complex C3 = new Complex(-2, 3);
    private static final Complex C4 = new Complex(-5, -2);

    // tests for: valueOf(String): Complex
    
    @Test
    public void valueOfTest_Success() {
        assertEqual(Complex.ZERO, Complex.valueOf("0"));
        assertEqual(Complex.ZERO, Complex.valueOf("i0"));
        assertEqual(Complex.ZERO, Complex.valueOf("0 + i0"));
        assertEqual(Complex.ZERO, Complex.valueOf("0 - i0"));
        
        assertEqual(Complex.valueOf(6, 0), Complex.valueOf("6"));
        assertEqual(Complex.valueOf(-9, 0), Complex.valueOf("-9"));
        assertEqual(Complex.valueOf(0, 2), Complex.valueOf("i2"));
        assertEqual(Complex.valueOf(0, -5), Complex.valueOf("-i2"));
        assertEqual(Complex.valueOf(0, 1), Complex.valueOf("i"));
        assertEqual(Complex.valueOf(0, -1), Complex.valueOf("-i"));
        
        assertEqual(Complex.valueOf(3, 2.1), Complex.valueOf("3 + i2.1"));
        assertEqual(Complex.valueOf(6, -9.1), Complex.valueOf("6 - i9.1"));
        assertEqual(Complex.valueOf(-32.1, 2.4), Complex.valueOf("-32.1 + i2.4"));
        assertEqual(Complex.valueOf(-0.5, -9.2), Complex.valueOf("-0.5 - i9.2"));
    }

    // tests for: module(): double

    private void assertEqual(Complex valueOf, Complex valueOf2) {
        // TODO Auto-generated method stub
        
    }

    @Test
    public void moduleTest_Success() {
        assertEquals(0.0, Complex.ZERO.module(), 0.0);

        assertEquals(5.831, C1.module(), 0.001);
        assertEquals(6.325, C2.module(), 0.001);
        assertEquals(3.606, C3.module(), 0.001);
        assertEquals(5.385, C4.module(), 0.001);
    }

    // test for: multiply(Complex): Complex

    @Test
    public void multiplyTest_Success() {
        assertEquals(Complex.ZERO, C1.multiply(Complex.ZERO));

        assertEquals(new Complex(-5, -31), C4.multiply(C1));
        assertEquals(new Complex(-6, 22), C3.multiply(C2));
        assertEquals(new Complex(-6, 22), C2.multiply(C3));
        assertEquals(new Complex(-5, -31), C1.multiply(C4));
    }

    @Test(expected = NullPointerException.class)
    public void multiplyTest_FailNull() {
        C1.multiply(null);
    }

    // test for: divide(Complex): Complex

    @Test
    public void divideTest_Success() {
        assertEquals(Complex.ZERO, C1.multiply(Complex.ZERO));

        assertEquals(new Complex(-0.7353, 0.5588), C4.divide(C1));
        assertEquals(new Complex(-0.45, 0.35), C3.divide(C2));
        assertEquals(new Complex(-1.3846, -1.0769), C2.divide(C3));
        assertEquals(new Complex(-0.8621, -0.6552), C1.divide(C4));
    }

    @Test(expected = NullPointerException.class)
    public void divideTest_FailNull() {
        C1.divide(null);
    }

    @Test(expected = ArithmeticException.class)
    public void divideTest_FailZero() {
        C1.divide(Complex.ZERO);
    }

    // test for: add(Complex): Complex

    @Test
    public void addTest_Success() {
        assertEquals(Complex.ZERO, C1.multiply(Complex.ZERO));

        assertEquals(new Complex(-2, 3), C4.add(C1));
        assertEquals(new Complex(4, 1), C3.add(C2));
        assertEquals(new Complex(4, 1), C2.add(C3));
        assertEquals(new Complex(-2, 3), C1.add(C4));
    }

    @Test(expected = NullPointerException.class)
    public void addTest_FailNull() {
        C1.add(null);
    }

    // test for: sub(Complex): Complex

    @Test
    public void subTest_Success() {
        assertEquals(Complex.ZERO, C1.multiply(Complex.ZERO));

        assertEquals(new Complex(-8, -7), C4.sub(C1));
        assertEquals(new Complex(-8, 5), C3.sub(C2));
        assertEquals(new Complex(8, -5), C2.sub(C3));
        assertEquals(new Complex(8, 7), C1.sub(C4));
    }

    @Test(expected = NullPointerException.class)
    public void subTest_FailNull() {
        C1.sub(null);
    }

    // test for: negate(): Complex

    @Test
    public void negateTest_Success() {
        assertEquals(Complex.ZERO, C1.multiply(Complex.ZERO));

        assertEquals(new Complex(-3, -5), C1.negate());
        assertEquals(new Complex(-6, 2), C2.negate());
        assertEquals(new Complex(2, -3), C3.negate());
        assertEquals(new Complex(5, 2), C4.negate());
    }

    // test for: power(int): Complex

    @Test
    public void powerTest_Success() {
        assertEquals(Complex.ONE, C1.power(0));

        assertEquals(C1, C1.power(1));
        assertEquals(new Complex(32, -24), C2.power(2));
        assertEquals(new Complex(46, 9), C3.power(3));
        assertEquals(new Complex(41, 840), C4.power(4));
    }

    @Test(expected = IllegalArgumentException.class)
    public void powerTest_FailNegative() {
        C1.power(-3);
    }

    @Test(expected = ArithmeticException.class)
    public void powerTest_FailZeroPowerOfZero() {
        Complex.ZERO.power(0);
    }

    // test for: root(int): List<Complex>

    @Test
    public void rootTest_Success() {

        List<Complex> expected1 = new ArrayList<>();
        List<Complex> expected2 = new ArrayList<>();
        List<Complex> expected3 = new ArrayList<>();
        List<Complex> expected4 = new ArrayList<>();

        expected1.add(new Complex(3, 5));

        expected2.add(new Complex(2.4824, -0.4028));
        expected2.add(new Complex(-2.4824, 0.4028));

        expected3.add(new Complex(1.1532, 1.0106));
        expected3.add(new Complex(-1.4519, 0.4934));
        expected3.add(new Complex(0.2986, -1.504));

        expected4.add(new Complex(1.1746, -0.97));
        expected4.add(new Complex(0.97, 1.1746));
        expected4.add(new Complex(-1.1746, 0.97));
        expected4.add(new Complex(-0.97, -1.1746));

        assertEqualLists(expected1, C1.root(1));
        assertEqualLists(expected2, C2.root(2));
        assertEqualLists(expected3, C3.root(3));
        assertEqualLists(expected4, C4.root(4));
    }

    @Test(expected = IllegalArgumentException.class)
    public void rootTest_FailNegative() {
        C1.root(-3);
    }

    private <T> void assertEqualLists(List<T> expected, List<T> actual) {
        for (T actualElement : actual) {
            boolean contains = false;

            for (T expectedElement : expected) {
                if (expectedElement.equals(actualElement)) {
                    contains = true;
                    break;
                }
            }

            if (!contains) {
                fail("Element " + actualElement + " not found!");
            }
        }
    }
}

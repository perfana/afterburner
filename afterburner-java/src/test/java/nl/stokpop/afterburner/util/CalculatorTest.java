package nl.stokpop.afterburner.util;

import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CalculatorTest {

    @Test
    public void isPrimeTest() {
        assertFalse(Calculator.isPrime(1), "1 is not prime");
        assertTrue(Calculator.isPrime(2), "2 is prime");
        assertTrue(Calculator.isPrime(5), "5 is prime");
        assertFalse(Calculator.isPrime(6), "6 is not prime");
        assertTrue(Calculator.isPrime(11), "11 is prime");
        assertTrue(Calculator.isPrime(17), "17 is prime");
        assertFalse(Calculator.isPrime(100), "100 is no prime");
        assertTrue(Calculator.isPrime(999631), "999631 is prime");
    }

}
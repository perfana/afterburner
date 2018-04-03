package nl.stokpop.afterburner.matrix;

import org.junit.Test;

import static org.junit.Assert.*;

public class MatrixCalculatorTest {

    @Test
    public void multiplyTest1() throws InvalidMatrixException {
        long[][] matrixA = new long[1][1];
        matrixA[0][0] = 2;

        long[][] matrixB = new long[1][1];
        matrixB[0][0] = 3;

        long[][] matrixC = new long[1][1];
        matrixC[0][0] = 6;

        long[][] multiplyMatrix = MatrixCalculator.multiply(matrixA, matrixB);

        MatrixEqualResult equal = MatrixCalculator.areEqual(multiplyMatrix, matrixC);
        assertTrue(equal.getMessage(), equal.areEqual());
    }

    @Test
    public void multiplyTest2() throws InvalidMatrixException {
        long[][] matrixA = new long[1][1];
        matrixA[0][0] = 2;

        long[][] matrixB = new long[1][2];
        matrixB[0][0] = 3;
        matrixB[0][1] = 5;

        long[][] matrixC = new long[1][2];
        matrixC[0][0] = 6;
        matrixC[0][1] = 10;

        long[][] multiplyMatrix = MatrixCalculator.multiply(matrixA, matrixB);

        MatrixEqualResult equal = MatrixCalculator.areEqual(multiplyMatrix, matrixC);
        assertTrue(equal.getMessage(), equal.areEqual());
    }

    @Test
    public void multiplyTest3() throws InvalidMatrixException {

        // To multiply an m×n matrix by an n×p matrix, the ns must be the same,
        // and the result is an m×p matrix.
        // see example: https://www.mathsisfun.com/algebra/matrix-multiplying.html
        long[][] matrixA = new long[2][3];
        matrixA[0][0] = 1;
        matrixA[0][1] = 2;
        matrixA[0][2] = 3;
        matrixA[1][0] = 4;
        matrixA[1][1] = 5;
        matrixA[1][2] = 6;

        long[][] matrixB = new long[3][2];
        matrixB[0][0] = 7;
        matrixB[0][1] = 8;
        matrixB[1][0] = 9;
        matrixB[1][1] = 10;
        matrixB[2][0] = 11;
        matrixB[2][1] = 12;

        long[][] matrixC = new long[2][2];
        matrixC[0][0] = 58;
        matrixC[0][1] = 64;
        matrixC[1][0] = 139;
        matrixC[1][1] = 154;

        long[][] multiplyMatrix = MatrixCalculator.multiply(matrixA, matrixB);

        MatrixEqualResult equal = MatrixCalculator.areEqual(multiplyMatrix, matrixC);
        assertTrue(equal.getMessage(), equal.areEqual());
    }

    @Test
    public void multiplyTest4() throws InvalidMatrixException {

        // To multiply an m×n matrix by an n×p matrix, the ns must be the same,
        // and the result is an m×p matrix.
        int matrixAm = 200;
        int matrixN = 300;
        int matrixBp = 200;
        long[][] matrixA = new long[matrixAm][matrixN];
        matrixA[0][0] = 1;
        matrixA[0][1] = 2;
        matrixA[0][2] = 3;
        matrixA[1][0] = 4;
        matrixA[1][1] = 5;
        matrixA[1][2] = 6;

        long[][] matrixB = new long[matrixN][matrixBp];
        matrixB[0][0] = 7;
        matrixB[0][1] = 8;
        matrixB[1][0] = 9;
        matrixB[1][1] = 10;
        matrixB[2][0] = 11;
        matrixB[2][1] = 12;

        long[][] matrixC = new long[matrixAm][matrixBp];
        matrixC[0][0] = 58;
        matrixC[0][1] = 64;
        matrixC[1][0] = 139;
        matrixC[1][1] = 154;

        long[][] multiplyMatrix = MatrixCalculator.multiply(matrixA, matrixB);

        MatrixEqualResult equal = MatrixCalculator.areEqual(multiplyMatrix, matrixC);
        assertTrue(equal.getMessage(), equal.areEqual());
    }

    @Test
    public void simpleMagicSquareTest1() throws InvalidMatrixException {
        long[][] square = MatrixCalculator.simpleMagicSquare(1);

        long[][] expectedSquare = new long[1][1];
        expectedSquare[0][0] = 0;

        MatrixEqualResult equal = MatrixCalculator.areEqual(square, expectedSquare);
        assertTrue(equal.getMessage(), equal.areEqual());
    }

    @Test
    public void simpleMagicSquareTest2() throws InvalidMatrixException {
        long[][] square = MatrixCalculator.simpleMagicSquare(2);

        long[][] expectedSquare = new long[2][2];
        expectedSquare[0][0] = 1;
        expectedSquare[0][1] = 2;
        expectedSquare[1][0] = 2;
        expectedSquare[1][1] = 1;

        MatrixEqualResult equal = MatrixCalculator.areEqual(square, expectedSquare);
        assertTrue(equal.getMessage(), equal.areEqual());
    }

    @Test
    public void simpleMagicSquareTest3() throws InvalidMatrixException {
        long[][] square = MatrixCalculator.simpleMagicSquare(3);

        long[][] expectedSquare = new long[3][3];
        expectedSquare[0][0] = 1;
        expectedSquare[0][1] = 2;
        expectedSquare[0][2] = 3;
        expectedSquare[1][0] = 2;
        expectedSquare[1][1] = 3;
        expectedSquare[1][2] = 1;
        expectedSquare[2][0] = 3;
        expectedSquare[2][1] = 1;
        expectedSquare[2][2] = 2;

        MatrixEqualResult equal = MatrixCalculator.areEqual(square, expectedSquare);
        assertTrue(equal.getMessage(), equal.areEqual());
    }

    @Test
    public void identitySquareTest1() throws InvalidMatrixException {
        long[][] square = MatrixCalculator.identitySquare(1);

        long[][] expectedSquare = new long[1][1];
        expectedSquare[0][0] = 1;

        MatrixEqualResult equal = MatrixCalculator.areEqual(square, expectedSquare);
        assertTrue(equal.getMessage(), equal.areEqual());
    }

    @Test
    public void identitySquareTest2() throws InvalidMatrixException {
        long[][] square = MatrixCalculator.identitySquare(3);

        long[][] expectedSquare = new long[3][3];
        expectedSquare[0][0] = 1;
        expectedSquare[0][1] = 0;
        expectedSquare[0][2] = 0;
        expectedSquare[1][0] = 0;
        expectedSquare[1][1] = 1;
        expectedSquare[1][2] = 0;
        expectedSquare[2][0] = 0;
        expectedSquare[2][1] = 0;
        expectedSquare[2][2] = 1;

        MatrixEqualResult equal = MatrixCalculator.areEqual(square, expectedSquare);
        assertTrue(equal.getMessage(), equal.areEqual());
    }


    @Test
    public void identityAndMagicSquareTest1() throws InvalidMatrixException {

        long[][] magicSquare = MatrixCalculator.simpleMagicSquare(40);
        long[][] identitySquare = MatrixCalculator.identitySquare(40);

        long[][] calculatedSquare = MatrixCalculator.multiply(magicSquare, identitySquare);

        MatrixEqualResult equal = MatrixCalculator.areEqual(magicSquare, calculatedSquare);
        assertTrue(equal.getMessage(), equal.areEqual());
    }

}
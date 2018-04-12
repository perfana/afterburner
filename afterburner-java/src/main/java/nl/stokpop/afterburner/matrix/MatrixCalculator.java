package nl.stokpop.afterburner.matrix;

public class MatrixCalculator {

    private MatrixCalculator() {
    }

    /**
     * To multiply an m×n matrix by an n×p matrix, the ns must be the same,
     * and the result is an m×p matrix.
     *
     * Note: this is rows x columns! (or "y by x" or matrix[row][column]) starting from top-left.
     *
     * On x,y and row,col: https://stackoverflow.com/questions/2203525/are-the-x-y-and-row-col-attributes-of-a-two-dimensional-array-backwards
     *
     * The minimum matrix should be 1 by 1. All matrices should have same length arrays.
     *
     * https://www.mathsisfun.com/algebra/matrix-multiplying.html
     * 
     * @param matrixA a long[m][n] matrix
     * @param matrixB a long[n][p] matrix
     * @return a long[m][p] matrix
     */
    public static long[][] multiply(long[][] matrixA, long[][] matrixB) throws InvalidMatrixException {
        final int matrixAm = matrixA.length;
        final int matrixBn = matrixB.length;

        if (matrixAm == 0) { throw new InvalidMatrixException("MatrixA should be have least 1 row"); }
        if (matrixBn == 0) { throw new InvalidMatrixException("MatrixB should be have least 1 row"); }

        final int matrixAn = matrixA[0].length;
        final int matrixBp = matrixB[0].length;

        if (matrixAn == 0) { throw new InvalidMatrixException("MatrixA should be have least 1 column"); }
        if (matrixBp == 0) { throw new InvalidMatrixException("MatrixB should be have least 1 column"); }

        if (matrixAn != matrixBn) {
            String message = String.format("MatrixA has %d columns, MatrixB has %d rows, which should be equal.", matrixAn, matrixBn);
            throw new InvalidMatrixException(message);
        }

        final long[][] matrixC = new long[matrixAm][matrixBp];

        for (int m = 0; m < matrixAm; m++) {
            for (int p = 0; p < matrixBp; p++) {
                long sum = 0;
                for (int n = 0; n < matrixAn; n++) {
                    long valueA = matrixA[m][n];
                    long valueB = matrixB[n][p];
                    sum = sum + (valueA * valueB);
                    if (n == matrixAn - 1) {
                        matrixC[m][p] = sum;
                    }
                }

            }
        }
        return matrixC;
    }

    public static MatrixEqualResult areEqual(final long[][] matrixA, final long[][] matrixB) throws InvalidMatrixException {
        final int matrixAx = matrixA.length;
        final int matrixBx = matrixB.length;

        if (matrixAx == 0) { throw new InvalidMatrixException("MatrixA should be have least 1 row"); }
        if (matrixBx == 0) { throw new InvalidMatrixException("MatrixB should be have least 1 row"); }

        final int matrixAy = matrixA[0].length;
        final int matrixBy = matrixB[0].length;

        if (matrixAy == 0) { throw new InvalidMatrixException("MatrixA should be have least 1 column"); }
        if (matrixBy == 0) { throw new InvalidMatrixException("MatrixB should be have least 1 column"); }

        if (matrixAx != matrixBx) {
            String message = String.format("MatrixA has %d rows, MatrixB has %d rows, which should be equal.", matrixAx, matrixBx);
            return new MatrixEqualResult(false, message);
        }

        if (matrixAy != matrixBy) {
            String message = String.format("MatrixA has %d columns, MatrixB has %d columns, which should be equal.", matrixAx, matrixBx);
            return new MatrixEqualResult(false, message);
        }

        for (int x = 0; x < matrixAx; x++) {
            for (int y = 0; y < matrixAy; y++) {
                long aValue = matrixA[x][y];
                long bValue = matrixB[x][y];
                if (aValue != bValue) {
                    String message = String.format("MatrixA has value %d in [%d][%d], MatrixB value %d, which should be equal.",
                            aValue, x, y, bValue);
                    return new MatrixEqualResult(false, message);
                }
            }
        }
        return new MatrixEqualResult(true, "Matrices are equal.");
    }

    /**
     * Create a simple magic square matrix, where each row and each column contains each number just once.
     * @param size size of the square
     * @return long matrix containing the square
     */
    public static long[][] simpleMagicSquare(int size) {
        long[][] square = new long[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int value = (j + i) % size;
                square[i][j] = value + 1;
            }
        }
        return square;
    }

    /**
     * Create an identity square.
     * @param size size of the square
     * @return long matrix containing the square
     */
    public static long[][] identitySquare(int size) {
        long[][] square = new long[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int value = i == j ? 1 : 0;
                square[i][j] = value;
            }
        }
        return square;
    }
}

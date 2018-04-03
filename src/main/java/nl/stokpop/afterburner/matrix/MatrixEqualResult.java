package nl.stokpop.afterburner.matrix;

public class MatrixEqualResult {
    private final boolean areEqual;
    private final String message;

    public MatrixEqualResult(final boolean areEqual, final String message) {
        this.areEqual = areEqual;
        this.message = message;
    }

    public boolean areEqual() {
        return areEqual;
    }

    public String getMessage() {
        return message;
    }
}

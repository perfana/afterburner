package nl.stokpop.afterburner;

import nl.stokpop.afterburner.matrix.InvalidMatrixException;
import nl.stokpop.afterburner.matrix.MatrixCalculator;
import nl.stokpop.afterburner.matrix.MatrixEqualResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CpuBurner {

    private static final Logger log = LoggerFactory.getLogger(CpuBurner.class);
    
    private final AfterburnerProperties props;

    public CpuBurner(final AfterburnerProperties props) {
        this.props = props;
    }

    @RequestMapping("/cpu/magic-identity-check")
    public BurnerHello magicIdentityCheck(
            @RequestParam(value = "matrixSize", defaultValue = "10") int matrixSize) throws InvalidMatrixException {

        long startTime = System.currentTimeMillis();

        log.info("Calculate magic matrix identity for matrix size [{}].", matrixSize);

        long[][] simpleMagicSquare = MatrixCalculator.simpleMagicSquare(matrixSize);
        long[][] identitySquare = MatrixCalculator.identitySquare(matrixSize);

        long[][] multiplyMatrix = MatrixCalculator.multiply(simpleMagicSquare, identitySquare);

        MatrixEqualResult matrixEqualResult = MatrixCalculator.areEqual(simpleMagicSquare, multiplyMatrix);

        String message = String.format("A simple magic square multiplied by an identity square of size [%d] [%s] to the magic square.",
                matrixSize, matrixEqualResult.areEqual() ? "is equal" : "is not equal");

        long durationMillis = System.currentTimeMillis() - startTime;
        return new BurnerHello(message, props.getAfterburnerName(), durationMillis);
    }

}

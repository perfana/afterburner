package io.perfana.afterburner.controller;

import brave.Span;
import brave.Tracer;
import io.swagger.v3.oas.annotations.Operation;
import io.perfana.afterburner.AfterburnerProperties;
import io.perfana.afterburner.domain.BurnerMessage;
import io.perfana.afterburner.matrix.InvalidMatrixException;
import io.perfana.afterburner.matrix.MatrixCalculator;
import io.perfana.afterburner.matrix.MatrixEqualResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CpuBurner {

    private static final Logger log = LoggerFactory.getLogger(CpuBurner.class);
    public static final String MATRIX_SIZE_TAG = "matrix-size";

    private final AfterburnerProperties props;

    Tracer tracer;

    public CpuBurner(final AfterburnerProperties props, Tracer tracer) {
        this.props = props;
        this.tracer = tracer;
    }

    @Operation(summary = "Spend some time on CPU doing some magic matrix calculations.")
    @GetMapping("/cpu/magic-identity-check")
    public BurnerMessage magicIdentityCheck(
            @RequestParam(value = "matrixSize", defaultValue = "10") int matrixSize) throws InvalidMatrixException {

        long startTime = System.currentTimeMillis();

        log.info("Calculate magic matrix identity for matrix size [{}].", matrixSize);

        long[][] simpleMagicSquare;
        long[][] identitySquare;
        
        Span matrixInitSpan = tracer.nextSpan().name("matrix-init").start();
        try (Tracer.SpanInScope ws = tracer.withSpanInScope(matrixInitSpan)) {
            simpleMagicSquare = MatrixCalculator.simpleMagicSquare(matrixSize);
            identitySquare = MatrixCalculator.identitySquare(matrixSize);
        }
        finally {
            matrixInitSpan.tag(MATRIX_SIZE_TAG, String.valueOf(matrixSize)).finish();
        }

        long[][] multiplyMatrix;
        Span matrixMultiplySpan = tracer.nextSpan().name("matrix-multiply").start();
        try (Tracer.SpanInScope ws = tracer.withSpanInScope(matrixMultiplySpan.start())) {
            multiplyMatrix = MatrixCalculator.multiply(simpleMagicSquare, identitySquare);
        }
        finally {
            matrixMultiplySpan.tag(MATRIX_SIZE_TAG, String.valueOf(matrixSize)).finish();
        }

        MatrixEqualResult matrixEqualResult;
        Span matrixEqualCheckSpan = tracer.nextSpan().name("matrix-equal-check").start();
        try (Tracer.SpanInScope ws = tracer.withSpanInScope(matrixEqualCheckSpan.start())) {
            matrixEqualResult = MatrixCalculator.areEqual(simpleMagicSquare, multiplyMatrix);
        }
        finally {
            matrixEqualCheckSpan.tag(MATRIX_SIZE_TAG, String.valueOf(matrixSize)).finish();
        }

        String message = String.format("A simple magic square multiplied by an identity square of size [%d] [%s] to the magic square.",
                matrixSize, matrixEqualResult.areEqual() ? "is equal" : "is not equal");

        long durationMillis = System.currentTimeMillis() - startTime;
        return new BurnerMessage(message, props.getName(), durationMillis);
    }

}

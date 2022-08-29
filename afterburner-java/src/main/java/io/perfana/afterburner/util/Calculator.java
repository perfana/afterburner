package io.perfana.afterburner.util;

import java.time.Duration;

public class Calculator {

    public static boolean isPrime(long n) {
        return isPrime(n, 0L);
    }

    /**
     * Determine prime based on:
     * https://en.wikipedia.org/wiki/Primality_test#Pseudocode
     *
     * Finding a prime is somewhat slower :-)
     */
    public static boolean isPrime(long n, long artificialDelayMillis) {
        if (n <= 3) {
            return n > 1;
        }
        else if (n % 2 == 0 || n % 3 == 0) {
            return false;
        }
        int i = 5;

        while (i * i <= n) {
            if (n % i == 0 || n % (i + 2) == 0) {
                return false;
            }
            i = i + 6;
        }
        Sleeper.sleep(Duration.ofMillis(artificialDelayMillis));
        return true;
    }
}

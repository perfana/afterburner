package com.stokpop.afterburner.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BigFatBastard {

    private final List<BigThing> lotsOfBigThings;
    private final Random random = new Random(System.currentTimeMillis());

    public BigFatBastard(int numberOfBigThings) {
        List<BigThing> things = new ArrayList<>();
        for (int i = 0; i < numberOfBigThings; i++) {
            things.add(new BigThing(createSomeNumbers()));
        }
        this.lotsOfBigThings = things;

    }

    private long[] createSomeNumbers() {
        return random.longs(100).toArray();
    }

    public int size() {
        return lotsOfBigThings.size();
    }
}

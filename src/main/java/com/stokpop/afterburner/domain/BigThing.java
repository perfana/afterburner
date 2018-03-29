package com.stokpop.afterburner.domain;

import java.util.Calendar;

public class BigThing {

    private final Calendar creationDate;
    private final long[] valuesOnDate;

    public BigThing(final long... valuesOnDate) {
        this.valuesOnDate = valuesOnDate;
        this.creationDate = Calendar.getInstance();
    }

    public Calendar getCreationDate() {
        return creationDate;
    }

    public long[] getValuesOnDate() {
        return valuesOnDate;
    }
}

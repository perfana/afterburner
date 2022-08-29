package io.perfana.afterburner.domain;

import java.util.Calendar;

public class MusicScore {

    private final Calendar creationDate;
    private final long[] valuesOnDate;

    public MusicScore(final long... valuesOnDate) {
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

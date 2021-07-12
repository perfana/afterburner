package nl.stokpop.afterburner.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MusicMachineMemory {

    private final List<MusicScore> lotsOfMusicScores;
    private final Random random = new Random(System.currentTimeMillis());

    public MusicMachineMemory(int numberOfMusicScores, int musicScoreLength) {
        List<MusicScore> things = new ArrayList<>();
        for (int i = 0; i < numberOfMusicScores; i++) {
            things.add(new MusicScore(createSomeMusicNotes(musicScoreLength)));
        }
        this.lotsOfMusicScores = things;
    }

    /**
     * Create a range of random midi notes for given length.
     * Maps to the 88 keys of a piano, see http://newt.phys.unsw.edu.au/jw/notes.html
     * @return an array of random midi notes
     */
    private long[] createSomeMusicNotes(int musicScoreLength) {
        return random.longs(musicScoreLength).map(l -> (Math.abs(l) % 88) + 21).toArray();
    }

    public int size() {
        return lotsOfMusicScores.size();
    }
}

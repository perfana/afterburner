package io.perfana.afterburner.domain;

import java.util.ArrayList;
import java.util.List;

public class MusicMachine {
    private static List<MusicMachineMemory> musicMachineMemories = new ArrayList<>();

    public static List<MusicMachineMemory> getMusicMachineMemories() {
        return musicMachineMemories;
    }

    public static void setMusicMachineMemories(List<MusicMachineMemory> musicMachineMemories) {
        MusicMachine.musicMachineMemories = musicMachineMemories;
    }
}

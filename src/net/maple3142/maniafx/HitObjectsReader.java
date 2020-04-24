package net.maple3142.maniafx;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class HitObjectsReader {
    public List<Note> read() throws Exception {
        var notes = new ArrayList<Note>();
        var p = getClass().getResource("/yasashisani.txt").toURI();
        var lines = Files.readAllLines(Paths.get(p));
        for (var line : lines) {
            var toks = line.split(",");
            if (toks.length < 4) continue;
            var x = Integer.parseInt(toks[0]);
            int lane = (x - 64) / 128;
            int start = Integer.parseInt(toks[2]);
            int duration = Integer.parseInt(toks[3]);
            var note = new Note(start, start + duration, lane);
            notes.add(note);
        }
        return notes;
    }
}

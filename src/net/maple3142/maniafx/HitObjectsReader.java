package net.maple3142.maniafx;

import net.maple3142.maniafx.notes.LongNote;
import net.maple3142.maniafx.notes.Note;

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
            if (duration < 20) {
                notes.add(new Note(start, lane));
            } else {
                notes.add(new LongNote(start, start + duration, lane));
            }
        }
        return notes;
    }
}

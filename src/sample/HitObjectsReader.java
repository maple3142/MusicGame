package sample;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.Deque;

public class HitObjectsReader {
    public Deque<Note>[] read() throws Exception {
        var lanes = new Deque[4];
        for (int i = 0; i < 4; i++) {
            lanes[i] = new ArrayDeque<>();
        }
        var p = getClass().getResource("hitobjects.txt").toURI();
        var lines = Files.readAllLines(Paths.get(p));
        for (var line : lines) {
            var toks = line.split(",");
            if (toks.length < 3) continue;
            var x = Integer.parseInt(toks[0]);
            int lane = (x - 64) / 128;
            long time = Long.parseLong(toks[2]) * 1000 * 1000;
            var note = new Note(time);
            lanes[lane].add(note);
        }
        return lanes;
    }
}

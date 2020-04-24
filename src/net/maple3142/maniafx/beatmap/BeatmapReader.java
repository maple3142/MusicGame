package net.maple3142.maniafx.beatmap;

import javafx.scene.media.Media;
import javafx.util.Pair;
import net.maple3142.maniafx.notes.LongNote;
import net.maple3142.maniafx.notes.Note;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BeatmapReader {
    private Path beatmapDir;

    public BeatmapReader(Path beatmapDir) {
        this.beatmapDir = beatmapDir;
    }

    public List<Beatmap> read() throws IOException {
        var bms = new ArrayList<Beatmap>();
        var osuFiles = Files.list(beatmapDir).filter(path -> path.toString().toLowerCase().endsWith(".osu")).collect(Collectors.toList());
        for (var osu : osuFiles) {
            var lines = Files.readAllLines(osu);
            var s = parse(lines);
            var general = parseInner(s.get("General"));
            var metadata = parseInner(s.get("Metadata"));
            var hitObjects = s.get("HitObjects");
            if (general == null) continue;
            if (!general.get("Mode").equals("3")) continue; // not mania map
            if (!general.containsKey("AudioFilename")) continue; // unlikely, but just be sure
            if (metadata == null) continue; // unlikely, but just be sure
            if (hitObjects == null) continue; // unlikely, but just be sure
            var bm = new Beatmap();
            bm.setMetadata(BeatmapMetadata.fromMap(metadata));
            var musicPath = Paths.get(beatmapDir.toString(), general.get("AudioFilename"));
            bm.setMusic(new Media(musicPath.toUri().toString()));
            var p = parseHitObjects(hitObjects);
            bm.setNotes(p.getKey());
            bm.setNumLanes(p.getValue());
            bms.add(bm);
        }
        return bms;
    }

    private Map<String, String> parseInner(List<String> lines) {
        if (lines == null) return null;
        var hm = new HashMap<String, String>();
        for (var line : lines) {
            var tokens = line.split(":");
            if (tokens.length != 2) continue;
            hm.put(tokens[0], tokens[1].strip());
        }
        return hm;
    }

    private Map<String, List<String>> parse(List<String> lines) {
        var hm = new HashMap<String, List<String>>();
        var tmpList = new ArrayList<String>();
        String currentEntry = null;
        for (var line : lines) {
            if (line.startsWith("[")) {
                if (currentEntry != null) {
                    hm.put(currentEntry, tmpList);
                    tmpList = new ArrayList<>();
                }
                line = line.strip();
                currentEntry = line.substring(1, line.length() - 1);
            } else if (currentEntry != null) {
                tmpList.add(line);
            }
        }
        if (currentEntry != null) {
            hm.put(currentEntry, tmpList);
        }
        return hm;
    }

    private Pair<List<Note>, Integer> parseHitObjects(List<String> lines) {
        var notes = new ArrayList<Note>();
        int maxLane = 0;
        for (var line : lines) {
            var tokens = line.split(":")[0].split(",");
            if (tokens.length < 4) continue;
            var x = Integer.parseInt(tokens[0]);
            int lane = (x - 64) / 128;
            int start = Integer.parseInt(tokens[2]);
            int type = Integer.parseInt(tokens[3]);
            if ((type & 1) != 0) { // type normal note
                notes.add(new Note(start, lane));
            } else if ((type & 1 << 7) != 0) { // type hold
                assert (tokens.length >= 6);
                int end = Integer.parseInt(tokens[5]);
                notes.add(new LongNote(start, end, lane));
            }
            if (lane > maxLane) {
                maxLane = lane;
            }
        }
        return new Pair<>(notes, maxLane + 1);
    }
}

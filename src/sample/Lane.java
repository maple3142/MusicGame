package sample;

import java.util.*;

class NotePair {
    public Note note;
    public int time;

    public NotePair(Note note, int time) {
        this.note = note;
        this.time = time;
    }
}

public class Lane {
    public Set<Note> notes;
    public Queue<NotePair> starting = new LinkedList<>();
    public Queue<NotePair> ending = new LinkedList<>();
    public Set<Note> currentNotes = new HashSet<>();
    public char keyCode;

    public void insertNotes(List<Note> notes) {
        notes.sort(Comparator.comparingInt(a -> a.endTime));
        for (var note : notes) {
            ending.add(new NotePair(note, note.endTime));
        }
        notes.sort(Comparator.comparingInt(a -> a.startTime));
        for (var note : notes) {
            starting.add(new NotePair(note, note.startTime));
        }
        this.notes = new HashSet<>(notes);
    }
}

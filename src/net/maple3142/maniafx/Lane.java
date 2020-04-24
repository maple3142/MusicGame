package net.maple3142.maniafx;

import net.maple3142.maniafx.notes.Note;

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
    public Queue<Note> currentNotes = new ArrayDeque<>();
    public char keyCode;

    public void insertNotes(List<Note> notes) {
        notes.sort(Comparator.comparingInt(Note::getEndTime));
        for (var note : notes) {
            ending.add(new NotePair(note, note.getEndTime()));
        }
        notes.sort(Comparator.comparingInt(Note::getStartTime));
        for (var note : notes) {
            starting.add(new NotePair(note, note.getStartTime()));
        }
        this.notes = new HashSet<>(notes);
    }
}

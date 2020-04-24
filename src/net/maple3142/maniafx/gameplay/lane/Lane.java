package net.maple3142.maniafx.gameplay.lane;

import net.maple3142.maniafx.gameplay.note.Note;
import net.maple3142.maniafx.utils.Pair;

import java.util.*;

public class Lane {
    public Set<Note> notes;
    public Queue<Pair<Note,Integer>> starting = new LinkedList<>();
    public Queue<Pair<Note,Integer>> ending = new LinkedList<>();
    public Queue<Note> currentNotes = new ArrayDeque<>();
    public char keyCode;

    public void insertNotes(List<Note> notes) {
        notes.sort(Comparator.comparingInt(Note::getEndTime));
        for (var note : notes) {
            ending.add(new Pair<>(note, note.getEndTime()));
        }
        notes.sort(Comparator.comparingInt(Note::getStartTime));
        for (var note : notes) {
            starting.add(new Pair<>(note, note.getStartTime()));
        }
        this.notes = new HashSet<>(notes);
    }
}

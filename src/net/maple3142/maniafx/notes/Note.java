package net.maple3142.maniafx.notes;

public interface Note {
    boolean isShortNote();

    int getLaneNum();

    int getStartTime();

    int getEndTime();

    void setState(NoteState state);

    NoteState getState();
}

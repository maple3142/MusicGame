package net.maple3142.maniafx.notes;

public class Note {
    private int startTime;
    private int laneNum;
    public NoteState state = NoteState.NORMAL;

    public Note(int startTime, int laneNum) {
        this.startTime = startTime;
        this.laneNum = laneNum;
    }

    public boolean isShortNote() {
        return true;
    }

    public int getLaneNum() {
        return this.laneNum;
    }

    public int getStartTime() {
        return this.startTime;
    }

    public int getEndTime() {
        return this.startTime;
    }
}

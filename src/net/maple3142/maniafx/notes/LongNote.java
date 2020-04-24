package net.maple3142.maniafx.notes;

public class LongNote implements Note {
    private int startTime;
    private int endTime;
    private int laneNum;
    public NoteState state = NoteState.NORMAL;

    public LongNote(int startTime, int endTime, int laneNum) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.laneNum = laneNum;
    }

    public LongNote(LongNote note) {
        this.startTime = note.startTime;
        this.endTime = note.endTime;
        this.laneNum = note.laneNum;
        this.state = note.state;
    }

    public boolean isShortNote() {
        return false;
    }

    public int getLaneNum() {
        return laneNum;
    }

    public int getStartTime() {
        return startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setState(NoteState state) {
        this.state = state;
    }

    public NoteState getState() {
        return this.state;
    }
}

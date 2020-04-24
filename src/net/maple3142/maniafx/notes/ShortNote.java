package net.maple3142.maniafx.notes;

public class ShortNote implements Note {
    private int startTime;
    private int laneNum;
    public NoteState state = NoteState.NORMAL;

    public ShortNote(int startTime, int laneNum) {
        this.startTime = startTime;
        this.laneNum = laneNum;
    }

    public ShortNote(ShortNote note) {
        this.startTime = note.startTime;
        this.laneNum = note.laneNum;
        this.state = note.state;
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

    public void setState(NoteState state) {
        this.state = state;
    }

    public NoteState getState() {
        return this.state;
    }

}

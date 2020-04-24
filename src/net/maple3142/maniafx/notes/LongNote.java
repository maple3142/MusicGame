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

    @Override
    public void setState(NoteState state) {
        this.state = state;
    }

    @Override
    public NoteState getState() {
        return this.state;
    }
}
package net.maple3142.maniafx.notes;

public class LongNote extends Note {
    private int endTime;

    public LongNote(int startTime,int endTime, int laneNum) {
        super(startTime, laneNum);
        this.endTime=endTime;
    }

    @Override
    public boolean isShortNote() {
        return false;
    }

    @Override
    public int getEndTime() {
        return endTime;
    }
}

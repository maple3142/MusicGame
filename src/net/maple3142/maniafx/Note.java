package net.maple3142.maniafx;

public class Note {
    public int startTime;
    public int endTime;
    public int laneNum;
    public NoteState state = NoteState.NORMAL;

    public Note(int startTime, int endTime, int laneNum) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.laneNum = laneNum;
    }

    boolean isShortNote() {
        return endTime - startTime <= 20;
    }
}

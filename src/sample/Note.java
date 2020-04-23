package sample;

public class Note {
    public int startTime;
    public int endTime;
    public int laneNum;
    public NoteState state;

    public Note(int startTime, int endTime,int laneNum) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.laneNum = laneNum;
        this.state = NoteState.NORMAL;
    }
}

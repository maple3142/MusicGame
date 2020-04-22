package sample;

public class Note {
    public long startTime;
    public NoteState state;

    public Note(long startTime) {
        this.startTime = startTime;
        this.state = NoteState.NORMAL;
    }
}

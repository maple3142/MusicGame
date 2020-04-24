package net.maple3142.maniafx.beatmap;

import javafx.scene.media.Media;
import net.maple3142.maniafx.notes.Note;

import java.util.List;

public class Beatmap {
    private BeatmapMetadata metadata;
    private int numLanes;
    private List<Note> notes;
    private Media music;

    public BeatmapMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(BeatmapMetadata metadata) {
        this.metadata = metadata;
    }

    public int getNumLanes() {
        return numLanes;
    }

    public void setNumLanes(int numLanes) {
        this.numLanes = numLanes;
    }

    public List<Note> getNotes() {
        return notes;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }

    public Media getMusic() {
        return music;
    }

    public void setMusic(Media music) {
        this.music = music;
    }
}

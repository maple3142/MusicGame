package net.maple3142.maniafx.gameplay;

import net.maple3142.maniafx.gameplay.beatmap.Beatmap;

public class GameResult {
    private Beatmap bm;
    private GameData data;

    public GameResult(Beatmap bm, GameData data) {
        this.bm = bm;
        this.data = data;
    }

    public String getTitle() {
        return bm.getMetadata().getTitleUnicode();
    }

    public String getDifficulty() {
        return bm.getMetadata().getVersion();
    }

    public String getArtist() {
        return bm.getMetadata().getArtistUnicode();
    }

    public int getScore() {
        return data.getScore();
    }

    public int getMaxCombo() {
        return data.getMaxCombo();
    }

    public int countNotes(NoteResult type) {
        return data.countNotes(type);
    }

    public double getAccuracy(){
        return data.caculateTotalAccuracy();
    }
}

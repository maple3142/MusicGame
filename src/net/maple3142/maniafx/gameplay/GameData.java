package net.maple3142.maniafx.gameplay;

import java.util.ArrayList;
import java.util.List;

public class GameData {
    private int score = 0;
    private int maxCombo = 0;
    private int combo = 0;
    private List<NoteResult> results = new ArrayList<>();
    private EventHandler<GameData> onChanged;

    public void setOnUpdated(EventHandler<GameData> onChanged) {
        this.onChanged = onChanged;
    }

    public void resetData() {
        score = 0;
        maxCombo = 0;
        combo = 0;
        results.clear();
        notifyUpdate();
    }

    private void notifyUpdate() {
        if (onChanged != null) {
            onChanged.invoke(this);
        }
    }

    public void putResult(NoteResult result) {
        results.add(result);
        if (result == NoteResult.SCORE_0) {
            combo = 0;
        } else {
            combo++;
            maxCombo = Math.max(combo, maxCombo);
            int baseScore = result.getBaseScore();
            score += (baseScore + baseScore * combo / 25);
        }
        notifyUpdate();
    }

    public double caculateTotalAccuracy() {
        var d = results.stream().mapToDouble(NoteResult::getAccuracy).average();
        return d.isPresent() ? d.getAsDouble() : 0;
    }

    public int countNotes(NoteResult type) {
        return (int) results.stream().filter(r -> r == type).count();
    }

    public int getScore() {
        return score;
    }

    public int getMaxCombo() {
        return maxCombo;
    }

    public int getCombo() {
        return combo;
    }
}

package net.maple3142.maniafx.gameplay;

public enum NoteResult {
    SCORE_300(300, 100),
    SCORE_200(200, 70),
    SCORE_100(100, 30),
    SCORE_0(0, 0);

    private double accuracy;
    private int baseScore;

    NoteResult(int baseScore, double accuracy) {
        this.baseScore = baseScore;
        this.accuracy = accuracy;
    }

    public int getBaseScore() {
        return baseScore;
    }

    public double getAccuracy() {
        return accuracy;
    }
}

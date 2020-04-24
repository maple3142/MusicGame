package net.maple3142.maniafx;

public class LaneToKeyConverter {
    private int laneNum;

    public LaneToKeyConverter(int laneNum) throws IllegalArgumentException {
        if (laneNum != 4) throw new IllegalArgumentException("Invalid lane number");
        this.laneNum = laneNum;
    }

    public char laneNumToKey(int lane) {
        if (lane >= laneNum || lane < 0) return ' ';
        if (laneNum == 4) {
            if (lane == 0) return 'D';
            if (lane == 1) return 'F';
            if (lane == 2) return 'J';
            return 'K';
        }
        return ' ';
    }

    public int keyToLaneNum(char key) {
        if (laneNum == 4) {
            if (key == 'D') return 0;
            if (key == 'F') return 1;
            if (key == 'J') return 2;
            if (key == 'K') return 3;
        }
        return -1;
    }
}

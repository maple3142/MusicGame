package net.maple3142.maniafx;

public class LaneToKeyConverter {
    private int laneNum;

    public LaneToKeyConverter(int laneNum) throws IllegalArgumentException {
        if (laneNum < 4 || laneNum > 7) throw new IllegalArgumentException("Invalid lane number");
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
        if (laneNum == 5) {
            if (lane == 0) return 'D';
            if (lane == 1) return 'F';
            if (lane == 2) return ' ';
            if (lane == 3) return 'J';
            return 'K';
        }
        if (laneNum == 6) {
            if (lane == 0) return 'S';
            if (lane == 1) return 'D';
            if (lane == 2) return 'F';
            if (lane == 3) return 'J';
            if (lane == 4) return 'K';
            return 'L';
        }
        if (laneNum == 7) {
            if (lane == 0) return 'S';
            if (lane == 1) return 'D';
            if (lane == 2) return 'F';
            if (lane == 3) return ' ';
            if (lane == 4) return 'J';
            if (lane == 5) return 'K';
            return 'L';
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
        if (laneNum == 5) {
            if (key == 'D') return 0;
            if (key == 'F') return 1;
            if (key == ' ') return 2;
            if (key == 'J') return 3;
            if (key == 'K') return 4;
        }
        if (laneNum == 6) {
            if (key == 'S') return 0;
            if (key == 'D') return 1;
            if (key == 'F') return 2;
            if (key == 'J') return 3;
            if (key == 'K') return 4;
            if (key == 'L') return 5;
        }
        if (laneNum == 7) {
            if (key == 'S') return 0;
            if (key == 'D') return 1;
            if (key == 'F') return 2;
            if (key == ' ') return 3;
            if (key == 'J') return 4;
            if (key == 'K') return 5;
            if (key == 'L') return 6;
        }
        return -1;
    }
}

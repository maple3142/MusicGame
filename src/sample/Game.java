package sample;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;

import java.util.ArrayDeque;
import java.util.Deque;

public class Game {
    private int width;
    private int height;
    private int padding;
    private int totalLaneWidth;
    private int laneWidth;
    final private long accuracyRange = 50 * 1000 * 1000L; // 50 ms
    final private int speed = 50; // how much pixels should note move per 1/10 second
    final private int lanesNum = 4;
    final private int bottomPadding = 40;
    final private int noteHeight = 10;
    final private Color laneColor = Color.MEDIUMPURPLE;
    final private Color noteColor = Color.WHITE;
    private Deque<Note>[] lanes = new Deque[lanesNum];
    private Deque<Note>[] missedLanes = new Deque[lanesNum];
    private MediaPlayer player;

    public void setLanes(Deque[] lanes) {
        this.lanes = lanes;
    }

    public void setPlayer(MediaPlayer player) {
        this.player = player;
    }

    public Game(int width, int height) {
        this.width = width;
        this.height = height;
        padding = (int) (width * 0.3);
        totalLaneWidth = width - 2 * padding;
        laneWidth = totalLaneWidth / lanesNum;
        for (int i = 0; i < lanesNum; i++) {
            lanes[i] = new ArrayDeque<>();
            missedLanes[i] = new ArrayDeque<>();
        }

        // test notes
        lanes[0].add(new Note(2000 * 1000 * 1000L));
        lanes[2].add(new Note(2500 * 1000 * 1000L));
    }

    private void drawGame(GraphicsContext ctx) {
        ctx.setFill(laneColor);
        ctx.fillRect(padding, 0, totalLaneWidth, height);
        for (int i = 0; i <= lanesNum; i++) {
            int x = padding + i * laneWidth;
            ctx.setFill(Color.BLACK);
            ctx.strokeLine(x, 0, x, height);
        }
        ctx.setFill(Color.DARKBLUE);
        ctx.fillRect(padding, height - bottomPadding - noteHeight, totalLaneWidth, noteHeight);
    }

    private long firstLoopTime = 0;
    private long lastNow = 0;

    public void loop(long now, GraphicsContext ctx) {
        // now is nanoseconds
        ctx.clearRect(0, 0, width, height);
        drawGame(ctx);
        if (firstLoopTime == 0) {
            firstLoopTime = now;
            if (player != null) {
                player.play();
            }
            return;
        }
        long currentTime = now - firstLoopTime;
        lastNow = now;
        int heightToTop = height - bottomPadding - noteHeight / 2;
        long topTime = currentTime + (heightToTop / speed) * 100 * 1000 * 1000;
        for (int i = 0; i < lanesNum; i++) {
            var lane = lanes[i];
            for (var note : lane) {
                if (note.state == NoteState.CLEARED) {
                    continue;
                }
                if (note.startTime > topTime) {
                    continue;
                }
                if (note.startTime < currentTime - accuracyRange) {
                    note.state = NoteState.MISSED;
                    lane.removeFirst();
                    missedLanes[i].add(note);
                }
                long range = topTime - currentTime;
                double percent = (double) (topTime - note.startTime) / range;
                int h = (int) (heightToTop * percent);
                if (note.state == NoteState.NORMAL) {
                    ctx.setFill(noteColor);
                } else if (note.state == NoteState.MISSED) {
                    ctx.setFill(Color.RED);
                }
                ctx.fillRect(padding + i * laneWidth + 1, h, laneWidth - 2, noteHeight);
            }
        }
        for (int i = 0; i < lanesNum; i++) {
            var lane = missedLanes[i];
            for (var note : lane) {
                long range = topTime - currentTime;
                double percent = (double) (topTime - note.startTime) / range;
                int h = (int) (heightToTop * percent);
                ctx.setFill(Color.RED);
                ctx.fillRect(padding + i * laneWidth + 1, h, laneWidth - 2, noteHeight);
            }
        }
    }

    public void onKeyPressed(KeyEvent event) {
        var code = event.getCode().getChar();
        if (code.length() == 1) {
            char c = code.charAt(0);
            Deque<Note> lane = null;
            switch (c) {
                case 'D':
                    lane = lanes[0];
                    break;
                case 'F':
                    lane = lanes[1];
                    break;
                case 'J':
                    lane = lanes[2];
                    break;
                case 'K':
                    lane = lanes[3];
                    break;
            }
            if (lane == null || lane.size() == 0) return;
            long currentTime = lastNow - firstLoopTime;
            var note = lane.getFirst();
            if (currentTime >= note.startTime - accuracyRange && currentTime <= note.startTime + accuracyRange) {
                note.state = NoteState.CLEARED;
                lane.removeFirst();
            }
        }
    }
}

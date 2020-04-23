package sample;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Game {
    private int width;
    private int height;
    private int padding;
    private int totalLaneWidth;
    private int laneWidth;
    private int numLanes;
    private int combo;
    final private int accuracyRange = 50; // 50 ms
    final private int speed = 80; // how much pixels should note move per 1/10 second
    final private int bottomPadding = 40;
    final private int hitBarHeight = 10;
    final private Color laneColor = Color.MEDIUMPURPLE;
    final private Color lanePressedColor = Color.PURPLE;
    final private Color noteColor = Color.WHITE;
    final private Color hitBarColor = Color.DARKBLUE;
    final private Color hitBarEffectColor = Color.YELLOW;
    private List<Lane> lanes = new ArrayList<>();
    private Set<Character> pressedKeys = new HashSet<>();
    private MediaPlayer player;

    public void setBeatmap(Beatmap bm) {
        this.numLanes = bm.numLanes;
        for (int i = 0; i < numLanes; i++) {
            int finalI = i;
            var notes = bm.notes.stream().filter(n -> finalI == n.laneNum).collect(Collectors.toList());
            var lane = new Lane();
            lane.insertNotes(notes);
            this.lanes.add(lane);
            if (i == 0) lane.keyCode = 'D';
            if (i == 1) lane.keyCode = 'F';
            if (i == 2) lane.keyCode = 'J';
            if (i == 3) lane.keyCode = 'K';
        }
        laneWidth = totalLaneWidth / numLanes;
    }

    public void setPlayer(MediaPlayer player) {
        this.player = player;
    }

    public Game(int width, int height) {
        this.width = width;
        this.height = height;
        padding = (int) (width * 0.3);
        totalLaneWidth = width - 2 * padding;
    }

    private void drawLanes(GraphicsContext ctx) {
        ctx.setFill(laneColor);
        ctx.fillRect(padding, 0, totalLaneWidth, height);
        for (int i = 0; i <= numLanes; i++) {
            int x = padding + i * laneWidth;
            ctx.setFill(Color.BLACK);
            ctx.strokeLine(x, 0, x, height);
        }
    }

    private void drawHitBar(GraphicsContext ctx) {
        ctx.setFill(hitBarColor);
        ctx.fillRect(padding, height - bottomPadding - hitBarHeight, totalLaneWidth, hitBarHeight);
    }

    private long firstLoopTime = 0;

    public void loop(long now, GraphicsContext ctx) {
        // now is nanoseconds
        ctx.clearRect(0, 0, width, height);
        drawLanes(ctx);
        drawHitBar(ctx);
        if (firstLoopTime == 0) {
            firstLoopTime = now;
            combo = 0;
            if (player != null) {
                player.play();
            }
            return;
        }
        int currentTime = (int) ((now - firstLoopTime) / (1000 * 1000));
        int heightToTop = height - bottomPadding - hitBarHeight / 2;
        int topTime = currentTime + heightToTop * 100 / speed;
        int bottomTime = currentTime - (bottomPadding + hitBarHeight / 2) * 100 / speed;
        for (int i = 0; i < numLanes; i++) {
            var lane = lanes.get(i);
            while (!lane.starting.isEmpty() && lane.starting.peek().time <= currentTime + accuracyRange) {
                lane.currentNotes.add(lane.starting.poll().note);
            }
            if (pressedKeys.contains(lane.keyCode)) {
                boolean hasNoteCleared = false;
                for (var note : lane.currentNotes) {
                    if (note.state == NoteState.NORMAL) {
                        note.state = NoteState.CLEARED;
                        combo++;
                        hasNoteCleared = true;
                    }
                }
                ctx.setFill(lanePressedColor);
                int x = padding + i * laneWidth;
                ctx.fillRect(x + 1, height - bottomPadding, laneWidth - 2, height);
                if (hasNoteCleared) {
                    ctx.setFill(hitBarEffectColor);
                    ctx.fillRect(x + 1, height - bottomPadding - hitBarHeight, laneWidth - 2, hitBarHeight);
                }
            }
            while (!lane.ending.isEmpty() && lane.ending.peek().time <= currentTime - accuracyRange) {
                var note = lane.ending.poll().note;
                lane.currentNotes.remove(note);
                if (note.state != NoteState.CLEARED) {
                    note.state = NoteState.MISSED;
                    combo = 0;
                }
            }
            for (var note : lane.notes) {
                if (note.state == NoteState.CLEARED) {
                    continue;
                }
                if (note.startTime > topTime || note.endTime < bottomTime) {
                    continue;
                }
                int duration = note.endTime - note.startTime;
                int noteHeight = (duration * speed) / 100;
                if (noteHeight < hitBarHeight) noteHeight = hitBarHeight;
                int range = topTime - currentTime;
                double percent = (double) (topTime - note.startTime) / range;
                int topDistance = (int) (heightToTop * percent);
                if (note.state == NoteState.NORMAL) {
                    ctx.setFill(noteColor);
                } else if (note.state == NoteState.MISSED) {
                    ctx.setFill(Color.RED);
                }
                ctx.fillRect(padding + i * laneWidth + 1, topDistance, laneWidth - 2, noteHeight);
            }
        }
        ctx.setFill(Color.BLACK);
        ctx.fillText(String.valueOf(combo), 10, 10);
    }

    public void onKeyPressed(KeyEvent event) {
        var code = event.getCode().getChar();
        if (code.length() > 0) {
            pressedKeys.add(code.charAt(0));
        }
    }

    public void onKeyReleased(KeyEvent event) {
        var code = event.getCode().getChar();
        if (code.length() > 0) {
            pressedKeys.remove(code.charAt(0));
        }
    }
}

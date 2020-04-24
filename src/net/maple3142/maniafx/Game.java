package net.maple3142.maniafx;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
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
    private LaneToKeyConverter keyConverter;
    final private int range100 = 200;
    final private int range200 = 150;
    final private int range300 = 100;
    final private int speed = 80; // how much pixels should note move per 1/10 second
    final private int bottomPadding = 200;
    final private int hitBarHeight = 10;
    final private Color laneColor = Color.MEDIUMPURPLE;
    final private Color lanePressedColor = Color.PURPLE;
    final private Color noteColor = Color.WHITE;
    final private Color hitBarColor = Color.DARKBLUE;
    final private Color hitBarEffectColor = Color.YELLOWGREEN;
    private List<Lane> lanes = new ArrayList<>();
    private Set<Character> pressedKeys = new HashSet<>();
    private MediaPlayer player;

    private Canvas bgCanvas;
    private GraphicsContext bgCtx;
    private Canvas noteCanvas;
    private GraphicsContext noteCtx;
    private Canvas keyCanvas;
    private GraphicsContext keyCtx;
    private HUD hud;

    public void setBeatmap(Beatmap bm) {
        this.numLanes = bm.numLanes;
        keyConverter = new LaneToKeyConverter(bm.numLanes);
        for (int i = 0; i < numLanes; i++) {
            int finalI = i;
            var notes = bm.notes.stream().filter(n -> finalI == n.laneNum).collect(Collectors.toList());
            var lane = new Lane();
            lane.insertNotes(notes);
            this.lanes.add(lane);
            lane.keyCode = keyConverter.laneNumToKey(i);
        }
        laneWidth = totalLaneWidth / numLanes;
        drawLanes();
        drawHitBar();
    }

    public void setPlayer(MediaPlayer player) {
        this.player = player;
    }

    public Game(Pane root, int w, int h) {
        bgCanvas = new Canvas(w, h);
        bgCtx = bgCanvas.getGraphicsContext2D();
        noteCanvas = new Canvas(w, h);
        noteCtx = noteCanvas.getGraphicsContext2D();
        keyCanvas = new Canvas(w, h);
        keyCtx = keyCanvas.getGraphicsContext2D();
        hud = new HUD(new Canvas(w, h));
        root.getChildren().addAll(bgCanvas, noteCanvas, keyCanvas, hud.canvas);
        width = (int) bgCanvas.getWidth();
        height = (int) bgCanvas.getHeight();
        padding = (int) (width * 0.3);
        totalLaneWidth = width - 2 * padding;
    }

    private void drawLanes() {
        bgCtx.setFill(laneColor);
        bgCtx.fillRect(padding, 0, totalLaneWidth, height);
        for (int i = 0; i <= numLanes; i++) {
            int x = padding + i * laneWidth;
            bgCtx.setFill(Color.BLACK);
            bgCtx.strokeLine(x, 0, x, height);
        }
    }

    private void drawHitBar() {
        bgCtx.setFill(hitBarColor);
        bgCtx.fillRect(padding, height - bottomPadding - hitBarHeight, totalLaneWidth, hitBarHeight);
    }

    private long firstLoopTime = 0;
    private int currentTime;

    public void loop(long now) {
        // now is nanoseconds
        noteCtx.clearRect(0, 0, width, height);
        keyCtx.clearRect(0, 0, width, height);
        if (firstLoopTime == 0) {
            firstLoopTime = now;
            if (player != null) {
                player.play();
            }
            return;
        }
        currentTime = (int) ((now - firstLoopTime) / (1000 * 1000));
        int heightToTop = height - bottomPadding - hitBarHeight / 2;
        int topTime = currentTime + heightToTop * 100 / speed;
        int bottomTime = currentTime - (bottomPadding + hitBarHeight / 2) * 100 / speed;
        for (int i = 0; i < numLanes; i++) {
            var lane = lanes.get(i);
            while (!lane.starting.isEmpty() && lane.starting.peek().time <= currentTime + range100) {
                lane.currentNotes.add(lane.starting.poll().note);
            }
            while (!lane.ending.isEmpty() && lane.ending.peek().time <= currentTime - range100) {
                var note = lane.ending.poll().note;
                lane.currentNotes.remove(note);
                if (note.state == NoteState.NORMAL || note.state == NoteState.PRESSED) {
                    note.state = NoteState.MISSED;
                    hud.setCombo(0);
                }
            }
            {
                if (pressedKeys.contains(lane.keyCode)) {
                    keyCtx.setFill(lanePressedColor);
                } else {
                    keyCtx.setFill(laneColor);
                }
                int x = padding + i * laneWidth;
                keyCtx.fillRect(x + 1, height - bottomPadding, laneWidth - 2, height);
            }
            for (var note : lane.notes) {
                if (note.state == NoteState.CLEARED) {
                    continue;
                }
                if (note.startTime > topTime && note.endTime < bottomTime) {
                    continue;
                }
                int duration = note.endTime - note.startTime;
                int noteHeight = (duration * speed) / 100;
                if (noteHeight < hitBarHeight) noteHeight = hitBarHeight;
                int range = topTime - currentTime;
                double percent = (double) (topTime - note.startTime) / range;
                int topDistance = (int) (heightToTop * percent);
                if (note.state == NoteState.NORMAL) {
                    noteCtx.setFill(noteColor);
                } else if (note.state == NoteState.MISSED) {
                    noteCtx.setFill(Color.RED);
                }
                noteCtx.fillRect(padding + i * laneWidth + 1, topDistance, laneWidth - 2, noteHeight);
            }
        }
    }

    public void onKeyPressed(KeyEvent event) {
        var code = event.getCode().getChar();
        if (code.length() > 0) {
            char c = code.charAt(0);
            pressedKeys.add(c);
            int num = keyConverter.keyToLaneNum(c);
            if (num == -1) return; // not a valid key
            var lane = lanes.get(num);
            for (var note : lane.currentNotes) {

                if (note.startTime - range100 <= currentTime && currentTime <= note.endTime + range100) {
                    if (note.isShortNote()) {
                        if (note.state != NoteState.CLEARED) {
                            note.state = NoteState.CLEARED;
                            hud.addCombo();
                            if (note.startTime - range300 <= currentTime && currentTime <= note.endTime + range300) {
                                hud.addScoreWeighed(300);
                            }
                            if (note.startTime - range200 <= currentTime && currentTime <= note.endTime + range200) {
                                hud.addScoreWeighed(200);
                            }
                            if (note.startTime - range100 <= currentTime && currentTime <= note.endTime + range100) {
                                hud.addScoreWeighed(100);
                            }
                            break; // each press should only clear one note
                        }
                    } else {
                        note.state = NoteState.PRESSED;
                    }
                }
            }
        }
    }

    public void onKeyReleased(KeyEvent event) {
        var code = event.getCode().getChar();
        if (code.length() > 0) {
            char c = code.charAt(0);
            pressedKeys.remove(c);
            int num = keyConverter.keyToLaneNum(c);
            if (num == -1) return; // not a valid key
            var lane = lanes.get(num);
            for (var note : lane.currentNotes) {
                if (note.isShortNote()) continue;
                if (note.state == NoteState.PRESSED && note.startTime - range100 <= currentTime && currentTime <= note.endTime + range100) {
                    note.state = NoteState.CLEARED;
                } else if (note.state != NoteState.CLEARED) {
                    note.state = NoteState.MISSED;
                    hud.setCombo(0);
                }
            }
        }
    }
}

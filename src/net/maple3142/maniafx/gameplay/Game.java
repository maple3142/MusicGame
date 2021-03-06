package net.maple3142.maniafx.gameplay;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import net.maple3142.maniafx.gameplay.beatmap.Beatmap;
import net.maple3142.maniafx.gameplay.lane.Lane;
import net.maple3142.maniafx.gameplay.lane.LaneToKeyConverter;
import net.maple3142.maniafx.gameplay.note.NoteState;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Game {
    private int width;
    private int height;
    private int padding;
    private int totalLaneWidth;
    private int numLanes;
    private LaneToKeyConverter keyConverter;
    final private int laneWidth = 40;
    final private int range0 = 200;
    final private int range100 = 150;
    final private int range200 = 100;
    final private int range300 = 50;
    final private int speed = 80; // how much pixels should note move per 1/10 second
    final private int bottomPadding = 150;
    final private int hitBarHeight = 10;
    final private Color laneColor = Color.MEDIUMPURPLE;
    final private Color lanePressedColor = Color.PURPLE;
    final private Color noteColor = Color.WHITE;
    final private Color hitBarColor = Color.DARKBLUE;
    final private Color hitBarEffectColor = Color.YELLOWGREEN;
    private List<Lane> lanes;
    private MediaPlayer player;
    private KeyBoardEventWrapper kbWrapper;

    private Beatmap bm;
    private GameData data;

    private Pane root;
    private Canvas bgCanvas;
    private GraphicsContext bgCtx;
    private Canvas noteCanvas;
    private GraphicsContext noteCtx;
    private Canvas keyCanvas;
    private GraphicsContext keyCtx;
    private HUD hud;

    private EventHandler<GameResult> gameEndEventHandler;

    public void setBeatmap(Beatmap bm) {
        this.bm = bm;
        this.player = new MediaPlayer(bm.getMusic());
        this.numLanes = bm.getNumLanes();
        keyConverter = new LaneToKeyConverter(numLanes);
        this.lanes = new ArrayList<>();
        var notes = bm.getClonedNotes();
        for (int i = 0; i < numLanes; i++) {
            int finalI = i;
            var laneNotes = notes.stream().filter(n -> finalI == n.getLaneNum()).collect(Collectors.toList());
            var lane = new Lane();
            lane.insertNotes(laneNotes);
            this.lanes.add(lane);
            lane.keyCode = keyConverter.laneNumToKey(i);
        }
        totalLaneWidth = numLanes * laneWidth;
        padding = (width - totalLaneWidth) / 2;
        drawLanes();
        drawHitBar();
        data.resetData();
    }

    public void setOnEnd(EventHandler<GameResult> f) {
        this.gameEndEventHandler = f;
    }

    private long startTime;
    private int currentTime;

    public void start() {
        var timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                loop(now);
            }
        };
        player.setOnReady(() -> {
            timer.start();
            player.play();
            startTime = System.nanoTime();
        });
        player.setOnEndOfMedia(() -> {
            timer.stop();
            if (gameEndEventHandler != null) {
                gameEndEventHandler.invoke(new GameResult(bm, data));
            }
        });
        root.requestFocus();
    }

    public Game(Pane root, int w, int h) {
        bgCanvas = new Canvas(w, h);
        bgCtx = bgCanvas.getGraphicsContext2D();
        noteCanvas = new Canvas(w, h);
        noteCtx = noteCanvas.getGraphicsContext2D();
        keyCanvas = new Canvas(w, h);
        keyCtx = keyCanvas.getGraphicsContext2D();
        hud = new HUD(new Canvas(w, h));
        root.getChildren().addAll(bgCanvas, noteCanvas, keyCanvas, hud.getCanvas());
        kbWrapper = new KeyBoardEventWrapper(root);
        kbWrapper.setOnKeyDown(this::onKeyDown);
        kbWrapper.setOnKeyUp(this::onKeyUp);
        data = new GameData();
        hud.bind(data);
        hud.draw(data); // initial draw
        width = w;
        height = h;
        this.root = root;
    }

    public void resize(int w, int h) {
        bgCanvas.setWidth(w);
        bgCanvas.setHeight(h);
        noteCanvas.setWidth(w);
        noteCanvas.setHeight(h);
        keyCanvas.setWidth(w);
        keyCanvas.setHeight(h);
        hud.resize(w, h);
        width = w;
        height = h;
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


    public void loop(long now) {
        // now is nanoseconds
        noteCtx.clearRect(0, 0, width, height);
        keyCtx.clearRect(0, 0, width, height);
        currentTime = (int) ((now - startTime) / (1000 * 1000));
        int heightToTop = height - bottomPadding - hitBarHeight / 2;
        int topTime = currentTime + heightToTop * 100 / speed;
        int bottomTime = currentTime - (bottomPadding + hitBarHeight / 2) * 100 / speed;
        for (int i = 0; i < numLanes; i++) {
            var lane = lanes.get(i);
            while (!lane.starting.isEmpty() && lane.starting.peek().getSecond() <= currentTime + range0) {
                assert (!lane.starting.isEmpty()); // without this, idk why IDEA is complaining possible NullPointerException
                lane.currentNotes.add(lane.starting.poll().getFirst());
            }
            while (!lane.ending.isEmpty() && lane.ending.peek().getSecond() <= currentTime - range0) {
                assert (!lane.ending.isEmpty()); // same as above
                var note = lane.ending.poll().getFirst();
                lane.currentNotes.remove(note);
                if (note.getState() == NoteState.NORMAL || note.getState() == NoteState.PRESSED) {
                    note.setState(NoteState.MISSED);
                    data.putResult(NoteResult.SCORE_0);
                }
            }
            {
                if (kbWrapper.isPressed(lane.keyCode)) {
                    keyCtx.setFill(lanePressedColor);
                } else {
                    keyCtx.setFill(laneColor);
                }
                int x = padding + i * laneWidth;
                keyCtx.fillRect(x + 1, height - bottomPadding, laneWidth - 2, height);
            }
            for (var note : lane.notes) {
                if (note.getState() == NoteState.CLEARED) {
                    continue;
                }
                if (note.getStartTime() > topTime && note.getEndTime() < bottomTime) {
                    continue;
                }
                int noteHeight;
                if (note.isShortNote()) {
                    noteHeight = hitBarHeight;
                } else {
                    noteHeight = (note.getEndTime() - note.getStartTime()) * speed / 100;
                }
                int range = topTime - currentTime;
                double percent = (double) (topTime - note.getEndTime()) / range;
                int topDistance = (int) (heightToTop * percent);
                if (note.getState() == NoteState.NORMAL) {
                    noteCtx.setFill(noteColor);
                } else if (note.getState() == NoteState.MISSED) {
                    noteCtx.setFill(Color.RED);
                }
                noteCtx.fillRect(padding + i * laneWidth + 1, topDistance, laneWidth - 2, noteHeight);
            }
        }
    }

    public void onKeyDown(KeyEvent event) {
        var code = event.getCode().getChar();
        if (code.length() > 0) {
            char c = code.charAt(0);
            int num = keyConverter.keyToLaneNum(c);
            if (num == -1) return; // not a valid key
            var lane = lanes.get(num);
            for (var note : lane.currentNotes) {
                // all notes is in [currentTime-range0, currentTime+range0]
                if (note.getState() == NoteState.CLEARED) continue;
                if (note.isShortNote()) {
                    note.setState(NoteState.CLEARED);
                    if (Math.abs(note.getStartTime() - currentTime) <= range300) {
                        data.putResult(NoteResult.SCORE_300);
                    } else if (Math.abs(note.getStartTime() - currentTime) <= range200) {
                        data.putResult(NoteResult.SCORE_200);
                    } else if (Math.abs(note.getStartTime() - currentTime) <= range100) {
                        data.putResult(NoteResult.SCORE_100);
                    } else {
                        note.setState(NoteState.MISSED);
                        data.putResult(NoteResult.SCORE_0);
                    }
                    break; // each press should only handle one note
                } else {
                    if (Math.abs(note.getStartTime() - currentTime) <= range100) {
                        note.setState(NoteState.PRESSED);
                    } else {
                        note.setState(NoteState.MISSED);
                    }
                    break;
                }
            }
        }
    }

    public void onKeyUp(KeyEvent event) {
        var code = event.getCode().getChar();
        if (code.length() > 0) {
            char c = code.charAt(0);
            int num = keyConverter.keyToLaneNum(c);
            if (num == -1) return; // not a valid key
            var lane = lanes.get(num);
            for (var note : lane.currentNotes) {
                if (note.isShortNote() || note.getState() == NoteState.CLEARED) continue;
                if (note.getState() == NoteState.PRESSED && Math.abs(note.getEndTime() - currentTime) <= range100) {
                    note.setState(NoteState.CLEARED);
                    if (Math.abs(note.getEndTime() - currentTime) <= range300) {
                        data.putResult(NoteResult.SCORE_300);
                    } else if (Math.abs(note.getEndTime() - currentTime) <= range200) {
                        data.putResult(NoteResult.SCORE_200);
                    } else if (Math.abs(note.getEndTime() - currentTime) <= range100) {
                        data.putResult(NoteResult.SCORE_100);
                    }
                    break;
                } else if (note.getState() != NoteState.MISSED) {
                    note.setState(NoteState.MISSED);
                    data.putResult(NoteResult.SCORE_0);
                }
            }
        }
    }
}

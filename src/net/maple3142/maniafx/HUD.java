package net.maple3142.maniafx;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class HUD {
    public Canvas canvas;
    private GraphicsContext ctx;
    private int width;
    private int height;
    private int combo = 0;
    private int score = 0;

    public HUD(Canvas canvas) {
        this.canvas = canvas;
        this.ctx = canvas.getGraphicsContext2D();
        width = (int) canvas.getWidth();
        height = (int) canvas.getHeight();
        draw();
    }

    private void draw() {
        ctx.clearRect(0, 0, width, height);

        ctx.setFill(Color.BLACK);

        // combo
        ctx.setTextAlign(TextAlignment.LEFT);
        ctx.setFont(new Font(20));
        ctx.fillText(combo + "x", 20, height - 20);

        // score
        ctx.setTextAlign(TextAlignment.RIGHT);
        ctx.setFont(new Font(20));
        ctx.fillText(String.valueOf(score), width - 20, 20);
    }

    public void setCombo(int combo) {
        this.combo = combo;
        draw();
    }

    public void addCombo() {
        this.combo++;
        draw();
    }

    public void setScore(int score) {
        this.score = score;
        draw();
    }

    public void addScoreWeighed(int score) {
        this.score += (score + score * combo / 25);
        draw();
    }
}

package sample;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class HUD {
    public Canvas canvas;
    private GraphicsContext ctx;
    private int width;
    private int height;
    private int combo = 0;

    public HUD(Canvas canvas) {
        this.canvas = canvas;
        this.ctx = canvas.getGraphicsContext2D();
        width = (int) canvas.getWidth();
        height = (int) canvas.getHeight();
        draw();
    }

    private void draw() {
        ctx.clearRect(0, 0, width, height);
        ctx.setFont(new Font(20));
        ctx.setFill(Color.BLACK);
        ctx.fillText(String.valueOf(combo), 20, 40);
    }

    public void setCombo(int combo) {
        this.combo = combo;
        draw();
    }

    public void addCombo() {
        setCombo(combo + 1);
    }
}

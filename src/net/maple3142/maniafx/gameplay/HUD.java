package net.maple3142.maniafx.gameplay;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class HUD {
    private Canvas canvas;
    private GraphicsContext ctx;
    private int width;
    private int height;

    public HUD(Canvas canvas) {
        this.canvas = canvas;
        this.ctx = canvas.getGraphicsContext2D();
    }

    public void resize(int w, int h) {
        canvas.setWidth(w);
        canvas.setHeight(h);
        width = w;
        height = h;
    }

    public void bind(GameData data) {
        data.setOnUpdated(this::draw);
    }

    public void draw(GameData data) {
        ctx.clearRect(0, 0, width, height);

        ctx.setFill(Color.BLACK);

        // combo
        ctx.setTextAlign(TextAlignment.LEFT);
        ctx.setFont(new Font(20));
        ctx.fillText(data.getCombo() + "x", 20, height - 20);

        // score
        ctx.setTextAlign(TextAlignment.RIGHT);
        ctx.setFont(new Font(20));
        ctx.fillText(String.valueOf(data.getScore()), width - 20, 40);
    }

    public Canvas getCanvas() {
        return canvas;
    }
}

package net.maple3142.maniafx.views;

import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import net.maple3142.maniafx.Main;
import net.maple3142.maniafx.Views;
import net.maple3142.maniafx.gameplay.Game;
import net.maple3142.maniafx.gameplay.beatmap.Beatmap;

public class GameScreen {
    private Game game;

    @FXML
    private Pane root;

    @FXML
    private void initialize() {
        game = new Game(root, 0, 0);
        game.setOnEnd(result -> {
            Main.getStage().setScene(Views.getResultScreenScene());
            Views.getResultScreenController().setGameResult(result);
        });
    }

    public void playBeatmap(Beatmap bm) {
        game.resize((int) root.getWidth(), (int) root.getHeight());
        game.setBeatmap(bm);
        game.start();
    }
}

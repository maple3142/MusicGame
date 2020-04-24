package net.maple3142.maniafx;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import net.maple3142.maniafx.beatmap.BeatmapReader;

import java.nio.file.Paths;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        int w = 600;
        int h = 800;
        var root = new Pane();
        var scene = new Scene(root, w, h);
        stage.setTitle("Test Game");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        root.requestFocus();
        var bmr = new BeatmapReader(Paths.get(getClass().getResource("/KimitoDarekanoYasashisani").toURI()));
        var list = bmr.read();
        var game = new Game(root, w, h);
        game.setBeatmap(list.get(1));
        root.setOnKeyPressed(game::onKeyPressed);
        root.setOnKeyReleased(game::onKeyReleased);
        game.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
